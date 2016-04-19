package com.nyctaxi.sim

/**
  * Created by dpinto on 23/03/2016.
  */

import akka.Done
import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.concurrent.duration._

object WebSocketTest extends App {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  import system.dispatcher

  // Future[Done] is the materialized value of Sink.foreach,
  // emitted when the stream completes
  val incoming: Sink[Message, Future[Done]] =
    Sink.foreach[Message] {
      case message: TextMessage.Strict =>
        println(message.text)
    }

  // send this as a message over the WebSocket
  val outgoing = Source.single(TextMessage(""))
  // flow to use (note: not re-usable!)
  val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest("ws://localhost:8080/echo/socket/"))
  //val webSocketFlow = Http().webSocketClientFlow(WebSocketRequest("ws://echo.websocket.org"))

  // the materialized value is a tuple with
  // upgradeResponse is a Future[WebSocketUpgradeResponse] that
  // completes or fails when the connection succeeds or fails
  // and closed is a Future[Done] with the stream completion from the incoming sink

  val (upgradeResponse, closed) =
    outgoing
      //.merge(Source.actorPublisher[TextMessage.Strict](RequestPublisher.props))
      .merge(Source.tick(10 seconds, 10 seconds, TextMessage("{action: 'stayalive'}")))
      .viaMat(webSocketFlow)(Keep.right) // keep the materialized Future[WebSocketUpgradeResponse]
      .toMat(incoming)(Keep.both) // also keep the Future[Done]
      .run()

  // just like a regular http request we can get 404 NotFound etc.
  // that will be available from upgrade.response
  val connected = upgradeResponse.flatMap { upgrade =>
    if (upgrade.response.status == StatusCodes.OK) {
      Future.successful(Done)
    } else {
      throw new RuntimeException(s"Connection failed: ${upgrade.response.status}")
    }
  }

  // in a real application you would not side effect here
  connected.onComplete(println)
  closed.foreach(_ => println("closed"))
}