package com.cssim.analysis

import akka.actor.Props
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.actor.{ActorSubscriber, MaxInFlightRequestStrategy, RequestStrategy}
import com.cssim.lib.AgentAction

import scala.collection.mutable

/**
  * Companion object for the AnalysisDataModel class
  *
  * defines auxiliary classes, to be sub-classed by the analysis services
  */
object AnalysisDataModel {
  type AnalysisDataModelProps = Props

  abstract class ProcessedData
  abstract class DataRequest
}

/**
  * This class is responsible for receiving the incoming AgentAction data objects
  */
abstract class AnalysisDataModel extends ActorSubscriber {

  import AnalysisDataModel._

  val MaxQueueSize = 10
  val queue = mutable.Queue.empty[ProcessedData]

  override protected val requestStrategy: RequestStrategy = new MaxInFlightRequestStrategy(max=MaxQueueSize) {
    override def inFlightInternally: Int = queue.size
  }
""
  def storeData(data: AgentAction): Unit

  def processRequest(request: DataRequest): ProcessedData

  override def receive: Receive = {
    case OnNext(data: AgentAction) =>
      storeData(data)
    case request: DataRequest =>
      sender ! processRequest(request)
    case _ =>
      sender ! "Invalid request"
  }
}
