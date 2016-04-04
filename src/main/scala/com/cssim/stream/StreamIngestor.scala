package com.cssim.stream

import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl.{Flow, GraphDSL, Sink, Source}
import com.cssim.lib.AgentAction


case class StreamIngestor[T](sourceEnv: StreamSource[T], parserEnv: Parser[T]) {

  type T

  val source = sourceEnv()
  val parser = parserEnv()

  val sourceGraph: Source[AgentAction, (NotUsed, NotUsed)] =
    Source.fromGraph(GraphDSL.create(source, parser)((_, _)) { implicit b =>
      (streamSource, parserFlow) =>
        import GraphDSL.Implicits._

        source ~> parser

        SourceShape(parser.shape.out)
    })

  def apply(): Source[AgentAction, (NotUsed, NotUsed)] = sourceGraph

}
