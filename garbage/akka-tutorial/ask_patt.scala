/**
  * Created by dpinto on 16/02/2016.
  */

import akka.pattern.{ ask, pipe }
import akka.util.Timeout
import scala.concurrent.duration._
import system.dispatcher

import scala.concurrent.Future

// The ExecutionContext that will be used
final case class Result(x: Int, s: String, d: Double)
case object Request

implicit val timeout = Timeout(5 seconds) // needed for `?` below

val f: Future[Result] =
  for {
    x <- ask(actorA, Request).mapTo[Int] // call pattern directly
    s <- (actorB ask Request).mapTo[String] // call by implicit conversion
    d <- (actorC ? Request).mapTo[Double] // call by symbolic name
  } yield Result(x, s, d)

f pipeTo actorD // .. or ..
pipe(f) to actorD

