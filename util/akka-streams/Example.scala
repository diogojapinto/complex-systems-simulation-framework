/**
  * Created by dpinto on 01/03/2016.
  */

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream._
import akka.stream.scaladsl._
import akka.util.ByteString

import scala.concurrent.Future


object Example extends App {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val source: Source[Int, NotUsed] = Source(1 to 100)

  source.runForeach(i => println(i))(materializer)


  val factorials = source.scan(BigInt(1))((acc, next) => acc * next)

  val result: Future[

    akka.stream.IOResult

    ] =
    factorials
      .map(num => ByteString(s"$num\n"))
      .runWith(FileIO.toFile(new File("factorials.txt")))


}


