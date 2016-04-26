package com.cssim.analysis

import akka.http.scaladsl.model.ws.Message
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.Flow
import sun.reflect.generics.reflectiveObjects.NotImplementedException


abstract class AnalysisApi(implicit val moduleName: String) {

  def getHandler(request: List[String]): Route = throw new NotImplementedException()

  def socketHandler(request: List[String]): Flow[Message, Message, Any] = throw new NotImplementedException()

  def dashboardPath: String = throw new NotImplementedException()

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
          pathPrefix("dashboard") {
            pathSingleSlash {
              getFromFile(s"$dashboardPath/index.html")
            } ~
              getFromDirectory(s"$dashboardPath")
          }
      }
}
