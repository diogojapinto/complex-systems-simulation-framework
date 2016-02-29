import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.ws.{Message, TextMessage, BinaryMessage}
import akka.http.scaladsl.testkit.WSProbe
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Flow, Source, Sink}
import akka.util.ByteString
import akka.http.scaladsl.server.Directives._

import scala.concurrent.duration._

/**
  * Created by dpinto on 29/02/2016.
  */

object SocketMain extends App {
  implicit val system = ActorSystem("my-system")
  implicit val materializer = ActorMaterializer()
  implicit val ec = system.dispatcher


  def greeter: Flow[Message, Message, Any] =
    Flow[Message].mapConcat {
      case tm: TextMessage ⇒
        TextMessage(Source.single("Hello ") ++ tm.textStream ++ Source.single("!")) :: Nil
      case bm: BinaryMessage ⇒
        // ignore binary messages but drain content to avoid the stream being clogged
        bm.dataStream.runWith(Sink.ignore)
        Nil
    }

  val websocketRoute =
    path("greeter") {
      handleWebsocketMessages(greeter)
    }

  val bindingFuture = Http().bindAndHandle(websocketRoute, "localhost", 8080)

  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  Console.readLine() // for the future transformations
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ ⇒ system.shutdown()) // and shutdown when done
}

/*


import org.scalatest._
import play.api.test._
import play.api.test.Helpers._
import org.scalatestplus.play._


// tests:
// create a testing probe representing the client-side
val wsClient = WSProbe()

// WS creates a Websocket request for testing
WS("/greeter", wsClient.flow) ~> websocketRoute ~>
  check {
    // check response for WS Upgrade headers
    isWebsocketUpgrade shouldEqual true

    // manually run a WS conversation
    wsClient.sendMessage("Peter")
    wsClient.expectMessage("Hello Peter!")

    wsClient.sendMessage(BinaryMessage(ByteString("abcdef")))
    wsClient.expectNoMessage(100 millis)

    wsClient.sendMessage("John")
    wsClient.expectMessage("Hello John!")

    wsClient.sendCompletion()
    wsClient.expectCompletion()
  }

*/