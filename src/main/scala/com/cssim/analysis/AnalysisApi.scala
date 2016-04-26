package com.cssim.analysis

import akka.actor.ActorRef
import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Source}
import akka.util.Timeout
import com.cssim.analysis.AnalysisDataModel.{DataRequest, ProcessedData}
import sun.reflect.generics.reflectiveObjects.NotImplementedException
import akka.pattern.ask

import scala.collection.mutable
import scala.concurrent.Await
import scala.concurrent.duration._


abstract class AnalysisApi(implicit val moduleName: String,
                           implicit val analysisDataModelActors: mutable.Map[String, ActorRef]) {

  implicit val timeout: Timeout = 15 seconds

  def getHandler(request: List[String]): String = throw new NotImplementedException()

  def socketHandler(request: List[String]): Source[Message, Any] = throw new NotImplementedException()

  def dashboardPath: String = throw new NotImplementedException()

  def requestProcessedData(dataModelIdentifier: String, dataRequest: DataRequest): ProcessedData = {
    val request = analysisDataModelActors(dataModelIdentifier) ? dataRequest
    val result = Await.result(request, timeout.duration).asInstanceOf[ProcessedData]

    result
  }

  lazy val route =
    pathPrefix(moduleName) {
      path("get" / Segments) { requestSegments =>
        get {
          complete(getHandler(requestSegments))
        }
      } ~
        path("socket" / Segments) { requestSegments =>
          handleWebSocketMessages(
            Flow[Message].merge(
              socketHandler(requestSegments)
            )
          )
        } ~
        pathPrefix("dashboard") {
          pathSingleSlash {
            getFromFile(s"$dashboardPath/index.html")
          } ~
            getFromDirectory(s"$dashboardPath")
        }
    }
}
