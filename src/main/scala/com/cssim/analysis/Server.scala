package com.cssim.analysis

import akka.actor.{Actor, ActorLogging, PoisonPill}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer

/**
  * Companion Object for the Server class
  *
  * Defines messages that may be sent to modify the behavior of a Server instance
  */
object Server {

  /**
    * Message to start a Server instance
    */
  case object Start


  /**
    * Message to stop a Server instance
    */
  case object Stop
}

/**
  * Serves through a REST API the analysis services.
  *
  * @param apis collection of API's that define routes the server must serve
  */
class Server(apis: Seq[AnalysisApi]) extends Actor with ActorLogging {

  import Server._

  private var running = false

  override def receive = {
    case Start if !running =>
      running = true
      start()
    case Stop if running =>
  }


  private def start(): Unit = {
    implicit val materializer = ActorMaterializer()
    implicit val ec = context.dispatcher

    if (apis.size <= 0) {
      log.warning("There are no services available. Server not starting")
      return
    }

    val compoundRoute =
      apis
        .map(api => api.route)
        .reduceLeft(_ ~ _)  // build compound route by properly appending api's routes

    val bindingFuture = Http()(context.system).bindAndHandle(compoundRoute, "localhost", 8080)

    log.info(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    Console.readLine() // for the future transformations
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ self ! PoisonPill) // and shutdown when done
  }
}
