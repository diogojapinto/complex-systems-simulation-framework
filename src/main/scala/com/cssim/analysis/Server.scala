package com.cssim.analysis

import akka.actor.{Actor, ActorLogging, PoisonPill}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer


object Server {

  case class Start()

  case class Stop()

}

class Server(apis: Seq[AnalysisApi]) extends Actor with ActorLogging {

  import Server._

  var running = false

  override def receive = {
    case Start() if !running => start()
    case Stop() if running =>
  }

  def start(): Unit = {
    implicit val materializer = ActorMaterializer()
    implicit val ec = context.dispatcher

    if (apis.size <= 0) {
      log.warning("There are no services available. Server not starting")
      return
    }

    val compoundRoute =
      apis
        .map(api => api.route)
        .reduceLeft(_ ~ _)

    val bindingFuture = Http()(context.system).bindAndHandle(compoundRoute, "localhost", 8080)

    log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    Console.readLine() // for the future transformations
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ self ! PoisonPill) // and shutdown when done
  }
}
