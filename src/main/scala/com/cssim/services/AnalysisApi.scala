package com.cssim.services

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import sun.reflect.generics.reflectiveObjects.NotImplementedException


abstract class AnalysisApi {
  var moduleName = this.getClass.getSimpleName.toLowerCase.stripSuffix("api")

  // Answer with complete(<json>)
  def getHandler(request: List[String]): Route = throw new NotImplementedException()
  def socketHandler(request: List[String]): Route = throw new NotImplementedException()
  def plotHandler(request: List[String]): Route = throw new NotImplementedException()

  lazy val route =
    path(moduleName) {
      path("get" / Segments) { requestSegments =>
        get {
            getHandler(requestSegments)
        }

      }/* ~
      pathPrefix("socket") ~
      pathPrefix("plot")*/
    }
}
