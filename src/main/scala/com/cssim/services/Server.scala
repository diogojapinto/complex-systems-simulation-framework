package com.cssim.services

import akka.actor.{Actor, PoisonPill}
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Directives._
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer


object Server {
  case class Start()
  case class Stop()

}

class Server(routes: Route*) extends Actor {

  import Server._

  var running = false

  override def receive = {
    case Start() if !running =>
    case Stop() if running =>
  }

  def start(): Unit = {
    implicit val materializer = ActorMaterializer()
    implicit val ec = context.dispatcher

    val compoundRoute = routes.reduceLeft(_ ~ _)

    val bindingFuture = Http()(context.system).bindAndHandle(compoundRoute, "localhost", 8080)

    println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
    Console.readLine() // for the future transformations
    bindingFuture
      .flatMap(_.unbind()) // trigger unbinding from the port
      .onComplete(_ ⇒ self ! PoisonPill) // and shutdown when done
  }
}
