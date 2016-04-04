package com.cssim

import akka.actor.{ActorSystem, Props}
import akka.stream.{ActorMaterializer, ClosedShape}
import akka.stream.scaladsl.{GraphDSL, RunnableGraph, Sink}
import com.cssim.services.analysis.AnalysisModule
import com.cssim.services.api.{ApiModule, Server}
import com.cssim.stream.StreamIngestor

import scala.collection.mutable


class SystemManager(ingestor: StreamIngestor[Any]) {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  // prepare stream processing graph components
  val sourceIngestor = ingestor()

  val runnableGraph =
    RunnableGraph.fromGraph(GraphDSL.create(sourceIngestor) { implicit b =>
      src =>
        import GraphDSL.Implicits._

        val sink = Sink.foreach(println)

        src ~> sink

        ClosedShape
    })

  val streamGraphThread = new Thread {
    override def run: Unit = {
      runnableGraph.run()
    }
  }

  // prepare API server
  val services = mutable.ListBuffer.empty[(AnalysisModule, ApiModule)]
  val server = system.actorOf(Props(new Server), "server")

  import Server._

  val serverThread = new Thread {
    override def run: Unit = {
      server ! Start()
    }
  }

  def init(): Unit = {
    serverThread.start
    streamGraphThread.start
  }
}
