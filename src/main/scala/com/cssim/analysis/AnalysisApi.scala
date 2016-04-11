package com.cssim.analysis

import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import sun.reflect.generics.reflectiveObjects.NotImplementedException


abstract class AnalysisApi(implicit val moduleName: String) {

  // Answer with complete(<json>)
  def getHandler(request: List[String]): Route = throw new NotImplementedException()

  def socketHandler(request: List[String]): Route = throw new NotImplementedException()

  def plotHandler(request: List[String]): Route = throw new NotImplementedException()

  lazy val route =
    pathPrefix(moduleName) {
      path("get" / Segments) { requestSegments =>
        get {
          getHandler(requestSegments)
        }

      } /* ~
      pathPrefix("socket") ~
      pathPrefix("plot")*/
    }
}
