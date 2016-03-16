import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import akka.stream.scaladsl._

import scala.concurrent.Future
import scala.util.{Failure, Success}

/**
  * Created by dpinto on 01/03/2016.
  */

object SimpleExample extends App {

  implicit val system = ActorSystem("QuickStart")
  implicit val materializer = ActorMaterializer()

  val source = Source(1 to 10)
  val sink = Sink.fold[Int, Int](0)(_ + _)

  // connect the Source to the Sink, obtaining a RunnableGraph
  val runnable: RunnableGraph[Future[Int]] = source.toMat(sink)(Keep.right)

  // materialize the flow and get the value of the FoldSink
  val sum: Future[Int] = runnable.run()

  /*
  sum onComplete {
    case Success(value) => println(value)
    case Failure(t) => println("An error has occured: " + t.getMessage)
  }
  */

  // SOURCES CREATION

  // Create a source from an Iterable
  Source(List(1, 2, 3))

  // Create a source from a Future
  Source.fromFuture(Future.successful("Hello Streams!"))

  // Create a source from a single element
  Source.single("only one element")

  // an empty source
  Source.empty

  // Sink that folds over the stream and returns a Future
  // of the final result as its materialized value
  Sink.fold[Int, Int](0)(_ + _)

  // Sink that returns a Future as its materialized value,
  // containing the first element of the stream
  Sink.head

  // A Sink that consumes a stream without doing anything with the elements
  Sink.ignore

  // A Sink that executes a side-effecting call for every element of the stream
  Sink.foreach[String](println(_))

  // Explicitly creating and wiring up a Source, Sink and Flow
  Source(1 to 6).via(Flow[Int].map(_ * 2)).to(Sink.foreach(println(_)))

  // Starting from a Source
  val source = Source(1 to 6).map(_ * 2)
  source.to(Sink.foreach(println(_)))

  // Starting from a Sink
  val sink: Sink[Int, NotUsed] = Flow[Int].map(_ * 2).to(Sink.foreach(println(_)))
  Source(1 to 6).to(sink)

  // Broadcast to a sink inline
  val otherSink: Sink[Int, NotUsed] =
    Flow[Int].alsoTo(Sink.foreach(println(_))).to(Sink.ignore)
  Source(1 to 6).to(otherSink)


}