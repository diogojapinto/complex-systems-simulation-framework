import akka.NotUsed
import akka.actor.ActorSystem
import akka.stream.{OverflowStrategy, ClosedShape, ActorMaterializer}
import akka.stream.scaladsl._

import scala.concurrent.Future

/**
  * Created by dpinto on 01/03/2016.
  */
/*
final case class Author(handle: String)

final case class Hashtag(name: String)

final case class Tweet(author: Author, timestamp: Long, body: String) {
  def hashtags: Set[Hashtag] =
    body.split(" ").collect { case t if t.startsWith("#") => Hashtag(t) }.toSet
}



object ReactiveTweets extends App {
  val akka = Hashtag("#akka")

  implicit val system = ActorSystem("reactive-tweets")
  implicit val materializer = ActorMaterializer()

  val tweets: Source[Tweet, Unit] = ???

  val authors: Source[Author, NotUsed] =
    tweets
      .filter(_.hashtags.contains(akka))
      .map(_.author)

  //authors.runWith(Sink.foreach(println))
  authors.runForeach(println)

  /////////////////////////////////////
  val hashtags: Source[Hashtag, NotUsed] = tweets.mapConcat(_.hashtags.toList)



  ///////////////////////////////////////

  val writeAuthors: Sink[Author, Unit] = ???
  val writeHashtags: Sink[Hashtag, Unit] = ???
  val g = RunnableGraph.fromGraph(GraphDSL.create() { implicit b =>
    import GraphDSL.Implicits._

    val bcast = b.add(Broadcast[Tweet](2))
    tweets ~> bcast.in
    bcast.out(0) ~> Flow[Tweet].map(_.author) ~> writeAuthors
    bcast.out(1) ~> Flow[Tweet].mapConcat(_.hashtags.toList) ~> writeHashtags
    ClosedShape
  })
  g.run()

  ////////////////////////////////////////

  tweets
    .buffer(10, OverflowStrategy.dropHead)
    .map(slowComputation)
    .runWith(Sink.ignore)

  //////////////////////////////////////////

  val count: Flow[Tweet, Int, NotUsed] = Flow[Tweet].map(_ => 1)

  val sumSink: Sink[Int, Future[Int]] = Sink.fold[Int, Int](0)(_ + _)

  val counterGraph: RunnableGraph[Future[Int]] =
    tweets
      .via(count)
      .toMat(sumSink)(Keep.right)

  val sum: Future[Int] = counterGraph.run()

  sum.foreach(c => println(s"Total tweets processed: $c"))


}

*/
