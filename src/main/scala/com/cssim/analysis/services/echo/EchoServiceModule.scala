package com.cssim.analysis.services.echo

import akka.NotUsed
import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.pattern.ask
import akka.stream.Attributes
import akka.stream.actor.ActorPublisher
import akka.stream.actor.ActorPublisherMessage.{Cancel, Request}
import akka.stream.scaladsl.{Flow, Source}
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import akka.util.Timeout
import com.cssim.SystemManager
import com.cssim.analysis.AnalysisDataModel.{ProcessedData, DataRequest}
import com.cssim.analysis._
import com.cssim.learning.behavior.trees.ServicesProvider
import com.cssim.lib.AgentAction

import scala.annotation.tailrec
import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._

object EchoServiceModule {

  case object EchoDataRequest$ extends DataRequest

  case object ContinuousEchoDataRequest$ extends DataRequest

  case class EchoData(data: AgentAction) extends ProcessedData

  case class EchoSocketSource(source: Source[Message, ActorRef]) extends ProcessedData

}

trait EchoServiceModule extends ServicesProvider {
  this: SystemManager =>

  import EchoServiceModule._

  implicit val moduleName = "echo"

  class EchoDataModel extends AnalysisDataModel {

    var lastValue: EchoData = EchoData(AgentAction.default)
    var subscribedActors = mutable.Buffer.empty[ActorRef]


    override def storeData(data: ProcessedData): Unit = data match {
      case echoData@EchoData(_) =>
        lastValue = echoData
        subscribedActors.map(actorRef => actorRef ! echoData)
    }


    override def processRequest(request: DataRequest): ProcessedData = request match {
      case EchoDataRequest$ => lastValue
      case ContinuousEchoDataRequest$ =>

        class EchoActor extends ActorPublisher[Message] {

          val MaxBufferSize = 100
          var buf = Vector.empty[AgentAction]

          override def receive: Receive = {
            case EchoData(data) if buf.size != MaxBufferSize =>
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
              /*
               * totalDemand is a Long and could be larger than
               * what buf.splitAt can accept
               */
              if (totalDemand <= Int.MaxValue) {
                val (use, keep) = buf.splitAt(totalDemand.toInt)
                buf = keep
                use.foreach(action => onNext(TextMessage(action.toString)))
              } else {
                val (use, keep) = buf.splitAt(Int.MaxValue)
                buf = keep
                use.foreach(action => onNext(TextMessage(action.toString)))
                deliverBuf()
              }
            }
        }

        val source =
          Source
            .actorPublisher(Props(new EchoActor))
            .mapMaterializedValue {
              actorRef =>
                subscribedActors.append(actorRef)
                actorRef
            }

        EchoSocketSource(source)
    }
  }

  object EchoWorker extends AnalysisWorker {

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {

        setHandler(in, new InHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPush(): Unit = {
            push(out, EchoData(grab(in)))
          }
        })

        setHandler(out, new OutHandler {
          @scala.throws[Exception](classOf[Exception])
          override def onPull(): Unit = {
            pull(in)
          }
        })
      }
  }

  object EchoApi extends AnalysisApi {

    override def getHandler(request: List[String]): Route = {
      implicit val timeout: Timeout = 30 second
      val request = analysisDataModelActors(moduleName) ? EchoDataRequest$
      val result = Await.result(request, 30 second).asInstanceOf[EchoData]
      val resultData = result match {
        case EchoData(data) => data
      }

      complete(resultData.toString)
    }

    override def socketHandler(request: List[String]): Flow[Message, Message, Any] = {

      implicit val timeout: Timeout = 30 second
      val request = analysisDataModelActors(moduleName) ? ContinuousEchoDataRequest$
      val result = Await.result(request, 30 second).asInstanceOf[EchoSocketSource]

      val source = result match {
        case EchoSocketSource(src) => src
      }

      Flow[Message].merge(source)
    }
  }

  val dataModel = Props(new EchoDataModel)
  val worker = EchoWorker
  val api = EchoApi


  addAnalysisModule(moduleName, worker, dataModel, api)
}
