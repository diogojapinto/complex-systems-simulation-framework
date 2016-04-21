package com.cssim.stream

import akka.NotUsed
import akka.stream.SourceShape
import akka.stream.scaladsl.{GraphDSL, Source}
import com.cssim.lib.AgentAction

/**
  * Companion object for StreamIngestor class.
  *
  * Defines a factory method for creation of StreamIngestor instances.
  */
object StreamIngestor {

  /**
    * Factory method for creation of StreamIngestor instances
    *
    * @param sourceEnv element that implements the StreamSource trait
    * @param parserEnv element that implements the Parser trait
    * @return new StreamIngestor element
    */
  def apply(sourceEnv: StreamSource, parserEnv: Parser) = new StreamIngestor(sourceEnv, parserEnv)
}

/**
  * Class responsible for connecting the source element from a StreamSource to the flow from a Parser.
  *
  * @param sourceEnv element that implements the StreamSource trait
  * @param parserEnv element that implements the Parser trait
  */
class StreamIngestor(sourceEnv: StreamSource, parserEnv: Parser) {

  val source = sourceEnv()
  val parser = parserEnv()

  val sourceGraph: Source[AgentAction, (NotUsed, NotUsed)] =
    Source.fromGraph(GraphDSL.create(source, parser)((_, _)) { implicit b =>
      (streamSource, parserFlow) =>
        import GraphDSL.Implicits._

        streamSource ~> parserFlow

        SourceShape(parserFlow.out)
    })

  /**
    * Utility function that retrieves the compound source element, one that outputs AgentAction elements
    *
    * @return compound source element
    */
  def apply(): Source[AgentAction, (NotUsed, NotUsed)] = sourceGraph
}
