package com.cssim

import akka.actor.{ActorSystem, Props}
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink}
import akka.stream.{ActorMaterializer, ClosedShape}
import com.cssim.learning.behavior.trees.ServicesProvider
import com.cssim.lib.AgentAction
import com.cssim.analysis._
import com.cssim.stream.StreamIngestor


class SystemManager(val ingestor: StreamIngestor) extends ServicesProvider {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  // prepare stream processing graph components
  val sourceIngestor = ingestor()

  def runnableGraph =
    RunnableGraph.fromGraph(GraphDSL.create(sourceIngestor) { implicit b =>
      src =>
        import GraphDSL.Implicits._

        val broadcaster = b.add(new Broadcast[AgentAction](analysisGraphComponents.size, false))

        for ((name, worker, model) <- analysisGraphComponents) {
          broadcaster ~> worker ~> Sink.actorSubscriber(model).mapMaterializedValue{actorRef => println(name); analysisDataModelActors.+=(name -> actorRef)}// ~> b.materializedValue.map(actorRef => analysisDataModelActors.+=(name -> actorRef)
        }

        src ~> broadcaster

        ClosedShape
    })

  private val streamGraphThread = new Thread {
    override def run: Unit = {
      runnableGraph.run()
    }
  }

  // prepare API server
  lazy val server = system.actorOf(Props(new Server(analysisApis)), "server")

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
