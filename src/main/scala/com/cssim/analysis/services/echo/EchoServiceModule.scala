package com.cssim.analysis.services.echo

import akka.actor.{ActorRef, Props}
import akka.http.scaladsl.model.ws.Message
import akka.stream.scaladsl.Source
import com.cssim.SystemManager
import com.cssim.analysis.AnalysisDataModel.{ProcessedData, DataRequest}
import com.cssim.analysis._
import com.cssim.learning.behavior.trees.ServicesProvider
import com.cssim.lib.AgentAction

/**
  * Example service. Echoes the last seen data to Get and Websocket requests.
  */
object EchoServiceModule {

  val EchoServiceIdentifier = "echo"

  /**
    * Request message used to request the last element seen in the stream
    */
  case object EchoDataRequest extends DataRequest

  /**
    * Request message to establish a continuous flow of the incoming data
    */
  case object ContinuousEchoDataRequest extends DataRequest

  /**
    * Envelope for a single AgentAction atomic data element
    *
    * @param data the AgentAction data element
    */
  case class EchoData(data: AgentAction) extends ProcessedData {
    override def toString: String = data.toString
  }

  case class EchoSocketSource(source: Source[Message, ActorRef]) extends ProcessedData

}

trait EchoServiceModule extends ServicesProvider {
  this: SystemManager =>

  import EchoServiceModule._

  /**
    * Module name, defined as implicit so that module components automatically access it
    */
  implicit val moduleName = EchoServiceIdentifier

  /**
    * Responsible for storing the incoming data, as well as answering requests from the API
    */
  class EchoDataModel extends AnalysisDataModel {

    /**
      * Stores the last seen piece of data. Updated in each incoming data element.
      */
    var lastValue: EchoData = EchoData(AgentAction.default)

    override def storeData(data: AgentAction): Unit = {
      lastValue = EchoData(data)

      broadcastProcessedData(lastValue)
    }


    override def processRequest(request: DataRequest): ProcessedData = request match {
      case EchoDataRequest => lastValue
      case ContinuousEchoDataRequest =>
        val source = establishProcessedDataPublisher
        EchoSocketSource(source)
    }
  }

  object EchoApi extends AnalysisApi {

    override def getHandler(request: List[String]): String = {
      requestProcessedData(EchoServiceIdentifier, EchoDataRequest).toString
    }

    override def socketHandler(request: List[String]): Source[Message, Any] = {

      val source = requestProcessedData(EchoServiceIdentifier, ContinuousEchoDataRequest) match {
        case EchoSocketSource(src) => src
      }

      source
    }

    override val dashboardPath = "resources/skywatch"
  }

  val dataModel = Props(new EchoDataModel)
  val api = EchoApi


  addAnalysisModule(moduleName, dataModel, api)
}
