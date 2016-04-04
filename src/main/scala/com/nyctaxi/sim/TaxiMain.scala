package com.nyctaxi.sim

import java.io.File

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Keep, Sink, Source}
import akka.util.ByteString

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.duration._

object TaxiMain extends App {

  case class Tick()

  implicit val system = ActorSystem("reactive-csv")
  implicit val materializer = ActorMaterializer()

  val filePath = "./src/main/resources/example"

  if (!new java.io.File(filePath).exists) {
    System.err.println("File not found")
    println("Current directory: " + new java.io.File(".").getCanonicalPath)
    System.exit(1)
  }

  val tickSource = Source.tick(1 second, 2 second, Tick())

  val file = new File(filePath)

  FileIO.fromFile(file)
    .via(Framing.delimiter(
      ByteString("\n"), maximumFrameLength = 300, allowTruncation = true))
    .map(_.utf8String)
    .map { elem =>
      println(elem)
      elem
    }
    .mergeMat(tickSource)(Keep.right)
    .runWith(Sink.ignore).onComplete { a =>
    println(a)
    system.terminate()
  }
}
