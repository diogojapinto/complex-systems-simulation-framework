package com.mog.sim

import akka.NotUsed
import akka.stream.scaladsl.{Keep, Source}
import com.cssim.stream.StreamSource
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.util.Timeout
import com.github.jeroenr.tepkin.MongoClient
import com.github.jeroenr.bson.BsonDocument
import com.github.jeroenr.bson.BsonDsl._
import com.typesafe.config.ConfigFactory

import scala.concurrent.duration._


object SkywatchSource extends StreamSource {

  implicit val timeout: Timeout = 24 hours

  // Retrieve the Logging Server IP
  val conf = ConfigFactory.load()

  // Create client for connecting with the database
  val client = MongoClient(conf.getString("loggingLocation"))

  import client.{context, ec}

  val db = client("CatalogedData")

  // Retrieve already existing flows' data in batch
  val flowBatchCollection = db("Flow_Transformation")

  val flowBatchDataSource =
    flowBatchCollection
      .find(BsonDocument.empty)
      .mapConcat(identity) // flatten the list of results into a stream of individual elements
      .map(_.toJson(extended = false))

  // Create a tailable cursor for the capped collection, in order to receive further updates
  val flowCappedCollection = db("InfoLog_cap")

  val query = "collection" := "Flow_Transformation" // filter by the new items that correspond to the
  // Flow_Transformation collection

  val flowCursorDataSource =
    flowCappedCollection
      .find(query, tailable = true)
      .mapConcat(identity)
      .map(_.toJson(extended = false))

  override def source: Source[String, NotUsed] =
    flowBatchDataSource
      .concat(flowCursorDataSource)
      .mergeMat(Source.empty)(Keep.right)

}
