package com.cssim

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.http.scaladsl.server.Route
import akka.stream.scaladsl.{Broadcast, GraphDSL, RunnableGraph, Sink}
import akka.stream.{ActorMaterializer, ClosedShape}
import com.cssim.lib.AgentAction
import com.cssim.services.{AnalysisApi, AnalysisDataModel, AnalysisWorker, Server}
import com.cssim.stream.StreamIngestor
import org.reactivestreams.Subscriber

import scala.collection.mutable


class SystemManager(ingestor: StreamIngestor) {

  implicit val system = ActorSystem()
  implicit val materializer = ActorMaterializer()

  private val analysisGraphComponents = mutable.Buffer.empty[(String, AnalysisWorker, AnalysisDataModel)]
  private val analysisApis = mutable.Buffer.empty[AnalysisApi]

  def addAnalysisModule(name: String,
                        worker: AnalysisWorker,
                        dataModel: AnalysisDataModel,
                        api: AnalysisApi): Unit = {

    analysisGraphComponents.+=:((name, worker, dataModel))
    analysisApis.+=:(api)
  }

  // prepare stream processing graph components
  val sourceIngestor = ingestor()

  def runnableGraph =
    RunnableGraph.fromGraph(GraphDSL.create(sourceIngestor) { implicit b =>
      src =>
        import GraphDSL.Implicits._

        // TODO: Exchange by broadcaster to services
        val broadcaster = b.add(new Broadcast[AgentAction](analysisGraphComponents.size, false))

        for ((name, worker, model) <- analysisGraphComponents) {
          broadcaster ~> worker ~> Sink.actorSubscriber(model.props)
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
