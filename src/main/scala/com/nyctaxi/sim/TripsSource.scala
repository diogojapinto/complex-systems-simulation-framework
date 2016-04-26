package com.nyctaxi.sim

import java.io.File

import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{FileIO, Framing, Keep, Source}
import akka.util.ByteString
import com.cssim.stream.StreamSource


object TripsSource extends StreamSource {

  private var tempSource: Source[String, NotUsed] = null

  for (i <- 1 to 12) {
    val filePath = s"./data/taxi/trip_$i.csv"

    if (!new java.io.File(filePath).exists) {
      System.err.println("File not found")
      println("Current directory: " + new java.io.File(".").getCanonicalPath)
      System.exit(1)
    }

    val file = new File(filePath)

    val fileSource: Source[String, NotUsed] =
      Source.empty.concatMat(
        FileIO.fromFile(file))(Keep.left)
        .via(Framing.delimiter(
          ByteString("\n"), maximumFrameLength = 600, allowTruncation = true))
        .map(_.utf8String)
        .drop(1)

    if (tempSource == null) {
      tempSource = fileSource
    } else {
      tempSource = tempSource.concat(fileSource)
    }
  }

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  val source: Source[String, NotUsed] = tempSource

}
