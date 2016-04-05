package com.cssim.stream

import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl.{GraphDSL, Source}
import com.cssim.lib.AgentAction


case class StreamIngestor(sourceEnv: StreamSource, parserEnv: Parser) {

  type T

  val source = sourceEnv()
  val parser = parserEnv()

  val sourceGraph: Source[AgentAction, (NotUsed, NotUsed)] =
    Source.fromGraph(GraphDSL.create(source, parser)((_, _)) { implicit b =>
      (streamSource, parserFlow) =>
        import GraphDSL.Implicits._

        streamSource ~> parserFlow

        SourceShape(parserFlow.out)
    })

  def apply(): Source[AgentAction, (NotUsed, NotUsed)] = sourceGraph

}
