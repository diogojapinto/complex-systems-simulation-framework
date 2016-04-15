package com.cssim.analysis.services

import akka.actor.Props
import akka.http.scaladsl.server.Route
import akka.stream.Attributes
import akka.stream.stage.{GraphStageLogic, InHandler, OutHandler}
import com.cssim.SystemManager
import com.cssim.lib.AgentAction
import com.cssim.analysis.AnalysisDataModel.{ProcessedData, Request}
import com.cssim.analysis._
import akka.http.scaladsl.server.Directives._
import com.cssim.learning.behavior.trees.ServicesProvider
import akka.pattern.ask
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._

object EchoServiceModule {
  case object EchoRequest extends Request

  case class EchoData(data: AgentAction) extends ProcessedData
}

trait EchoServiceModule extends ServicesProvider {
  this: SystemManager =>

  import EchoServiceModule._

  implicit val moduleName = "echo"

  class EchoDataModel extends AnalysisDataModel {

    var lastValue: EchoData = EchoData(AgentAction.default)

    override def processRequest(request: Request): ProcessedData = request match {
      case EchoRequest => lastValue
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
            println(grab(in))
            pull(in)
          }

          override def onPull(): Unit = {
            push(out, EchoData(grab(in)))
          }

        })
      }
  }

  object EchoApi extends AnalysisApi {

    override def getHandler(request: List[String]): Route = {
      implicit val timeout: Timeout = 30 second
      val request = analysisDataModelActors(moduleName) ? EchoRequest
      val result = Await.result(request, 30 second).asInstanceOf[EchoData]
      val resultData = result match {case EchoData(data) => data}

      complete(resultData.toString)
    }
  }

  val dataModel = Props(new EchoDataModel)
  val worker = EchoWorker
  val api = EchoApi


  addAnalysisModule(moduleName, worker, dataModel, api)
}
