import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Tcp.{IncomingConnection, ServerBinding}
import akka.stream.scaladsl._
import akka.stream.io.Framing
import akka.util.ByteString

import scala.concurrent.Future

/**
  * Created by dpinto on 29/02/2016.
  */

object Main extends App {


  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher
/*
  val binding: Future[ServerBinding] =
    Tcp().bind("127.0.0.1", 8888).to(Sink.ignore).run()

  binding.map { b =>
    b.unbind() onComplete {
      case _ => // ...
    }
  }~*/


  val connections: Source[IncomingConnection, Future[ServerBinding]] =
    Tcp().bind("127.0.0.1", 8888)
  connections runForeach { connection =>
    println(s"New connection from: ${connection.remoteAddress}")

    val echo = Flow[ByteString]
      .via(Framing.delimiter(
        ByteString("\n"),
        maximumFrameLength = 256,
        allowTruncation = true))
      .map(_.utf8String)
      .map(_ + "!!!\n")
      .map(ByteString(_))

    connection.handleWith(echo)
  }
}