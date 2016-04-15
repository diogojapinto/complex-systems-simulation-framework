package com.cssim.analysis

import akka.actor.Props
import akka.stream.actor.ActorSubscriberMessage._
import akka.stream.actor.{ActorSubscriber, MaxInFlightRequestStrategy, RequestStrategy}

import scala.collection.mutable

object AnalysisDataModel {
  type AnalysisDataModelProps = Props

  abstract class ProcessedData
  abstract class DataRequest
}

abstract class AnalysisDataModel extends ActorSubscriber {

  import AnalysisDataModel._

  val MaxQueueSize = 10
  val queue = mutable.Queue.empty[ProcessedData]

  override protected val requestStrategy: RequestStrategy = new MaxInFlightRequestStrategy(max=MaxQueueSize) {
    override def inFlightInternally: Int = queue.size
  }

  def storeData(data: ProcessedData): Unit

  def processRequest(request: DataRequest): ProcessedData

  override def receive: Receive = {
    case OnNext(data: ProcessedData) =>
      storeData(data)
    case request: DataRequest =>
      sender ! processRequest(request)
    case _ =>
      sender ! "Invalid request"
  }
}
