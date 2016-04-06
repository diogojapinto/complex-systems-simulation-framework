package com.nyctaxi.sim

import java.io.File

import akka.NotUsed
import akka.stream.scaladsl.{FileIO, Framing, Keep, Source}
import akka.util.ByteString
import com.cssim.stream.StreamSource


object TripsSource extends StreamSource {

  private val filePath = "./data/taxi/trip_1.csv"

  if (!new java.io.File(filePath).exists) {
    System.err.println("File not found")
    println("Current directory: " + new java.io.File(".").getCanonicalPath)
    System.exit(1)
  }

  val file = new File(filePath)

  val source: Source[String, NotUsed] =
    Source.empty.concatMat(
      FileIO.fromFile(file))(Keep.left)
      .via(Framing.delimiter(
        ByteString("\n"), maximumFrameLength = 300, allowTruncation = true))
      .map(_.utf8String)
      .drop(1)
}
