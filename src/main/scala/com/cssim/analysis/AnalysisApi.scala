package com.cssim.analysis

import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Flow, Source}
import sun.reflect.generics.reflectiveObjects.NotImplementedException


abstract class AnalysisApi(implicit val moduleName: String) {

  // Answer with complete(<json>)
  def getHandler(request: List[String]): Route = throw new NotImplementedException()

  def socketHandler(request: List[String]): Flow[Message, Message, Any] = throw new NotImplementedException()

  def plotHandler(request: List[String]): Route = throw new NotImplementedException()

  lazy val route =
    pathPrefix(moduleName) {
      path("get" / Segments) { requestSegments =>
        get {
          getHandler(requestSegments)
        }
      } ~
      path("socket" / Segments) { requestSegments =>
        handleWebSocketMessages(socketHandler(requestSegments))
      } ~
      path("plot" / Segments) { requestSegments =>
        get {
          plotHandler(requestSegments)
        }
      }
    }
}
