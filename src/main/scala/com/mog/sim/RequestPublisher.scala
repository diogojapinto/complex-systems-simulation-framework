package com.mog.sim

import akka.actor.Props
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.actor.ActorPublisher

import scala.concurrent.duration._

object RequestPublisher {
  def props: Props = Props[RequestPublisher]

  case object Tick

}

class RequestPublisher extends ActorPublisher[TextMessage.Strict] {

  import akka.stream.actor.ActorPublisherMessage._
  import RequestPublisher._
  import scala.concurrent.ExecutionContext.Implicits.global

  val tick =
    context.system.scheduler.schedule(1 second, 10 second, self, Tick)

  override def receive: Receive = {
    case Tick => onNext(TextMessage(
      """{
        |    action: 'stayalive'
        |}""".stripMargin))
    case action: String => println(action)
    case Request(_) =>
      //onNext(TextMessage("oidho"))
    case Cancel =>
      context.stop(self)
  }
}
