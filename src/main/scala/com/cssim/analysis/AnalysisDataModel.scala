package com.cssim.analysis

import akka.NotUsed
import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.actor.ActorPublisherMessage.{Cancel, Request}
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.actor.{ActorPublisher, ActorSubscriber, MaxInFlightRequestStrategy, RequestStrategy}
import akka.stream.scaladsl.Source
import com.cssim.analysis.services.echo.EchoServiceModule.{EchoData, EchoSocketSource}
import com.cssim.lib.AgentAction

import scala.annotation.tailrec
import scala.collection.mutable

/**
  * Companion object for the AnalysisDataModel class
  *
  * defines auxiliary classes, to be sub-classed by the analysis services
  */
object AnalysisDataModel {
  type AnalysisDataModelProps = Props


  abstract class DataRequest

  /**
    * Envelope class for processed data (to be subclassed). Should define a meaningful '.toString' method, as
    * messages delivered to requesters suffer this transformation
    */
  abstract class ProcessedData
}

/**
  * This class is responsible for processing and storing the incoming AgentAction data objects, as well as answering
  * the incoming requests from the API
  */
abstract class AnalysisDataModel extends ActorSubscriber {

  import AnalysisDataModel._

  val MaxQueueSize = 10
  val queue = mutable.Queue.empty[ProcessedData]

  /**
    * List of actors that are subscribed to new data elements.
    */
  val subscribedActors = mutable.Buffer.empty[ActorRef]

  override protected val requestStrategy: RequestStrategy = new MaxInFlightRequestStrategy(max=MaxQueueSize) {
    override def inFlightInternally: Int = queue.size
  }

  def storeData(data: AgentAction): Unit

  def processRequest(request: DataRequest): ProcessedData

  def establishProcessedDataPublisher: Source[Message, ActorRef] = {
    class StreamPublisher extends ActorPublisher[Message] {

      val MaxBufferSize = 100
      var buf = Vector.empty[ProcessedData]

      override def receive: Receive = {
        case data: ProcessedData if buf.size != MaxBufferSize =>
          if (buf.isEmpty && totalDemand > 0)
            onNext(TextMessage(data.toString))
          else {
            buf :+= data
            deliverBuf()
          }
        case Request(_) =>
          deliverBuf()
        case Cancel =>
          context.stop(self)
      }

      @tailrec final def deliverBuf(): Unit =
        if (totalDemand > 0) {
          // totalDemand is a Long and could be larger than
          // what buf.splitAt can accept
          if (totalDemand <= Int.MaxValue) {
            val (use, keep) = buf.splitAt(totalDemand.toInt)
            buf = keep
            use.foreach(data => onNext(TextMessage(data.toString)))
          } else {
            val (use, keep) = buf.splitAt(Int.MaxValue)
            buf = keep
            use.foreach(data => onNext(TextMessage(data.toString)))
            deliverBuf()
          }
        }
    }

    val source =
      Source
        .actorPublisher(Props(new StreamPublisher))
        .mapMaterializedValue {
          actorRef =>
            subscribedActors.append(actorRef)
            actorRef
        }

    source
  }

  //def dataPublisherBehavior(request: DataRequest): ProcessedData => String

  def broadcastProcessedData(processedData: ProcessedData): Unit = {
    subscribedActors.map(actorRef => actorRef ! processedData)
  }

  override def receive: Receive = {
    case OnNext(data: AgentAction) =>
      storeData(data)
    case request: DataRequest =>
      sender ! processRequest(request)
    case _ =>
      sender ! "Invalid request"
  }
}
