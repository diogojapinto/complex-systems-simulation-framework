package com.cssim.services.services

import akka.actor.Props
import akka.http.scaladsl.server.Route
import akka.stream.Attributes
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import com.cssim.SystemManager
import com.cssim.lib.AgentAction
import com.cssim.services.AnalysisDataModel.{ProcessedData, Request}
import com.cssim.services._

trait EchoServiceModule extends Service("echo") {

  case class EchoRequest() extends Request

  case class EchoData(data: AgentAction) extends ProcessedData

  class EchoDataModel extends AnalysisDataModel {

    var lastValue: EchoData = _

    override def processRequest(request: Request): ProcessedData = request match {
      case EchoRequest() => lastValue
    }

    override def storeData(data: ProcessedData): Unit = data match {
      case echoData@EchoData(_) => lastValue = echoData
    }
  }

  object EchoWorker extends AnalysisWorker {

    override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
      new GraphStageLogic(shape) {

        setHandlers(in, out, new InHandler with OutHandler {
          override def onPush(): Unit = {
            pull(in)
          }

          override def onPull(): Unit = {
            push(out, EchoData(grab(in)))
          }
        })
      }
  }

  object EchoApi extends AnalysisApi {

    override def getHandler(request: List[String]): Route = ???
  }
}
