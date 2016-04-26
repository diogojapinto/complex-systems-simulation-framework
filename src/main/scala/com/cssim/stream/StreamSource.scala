package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Source

/**
  * Trait used in classes that define a source.
  *
  * This source may be defined through various methods, all of which
  * base upon the utilities provided by Akka Streams package:
  *
  * Construct a Source from atomic components:
  * http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-flows-and-basics.html#Defining_sources__sinks_and_flows
  *
  * Compose a Source through smaller components using GraphDSL:
  * http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-graphs.html
  *
  * Compose a custom, atomic, GraphStage Source component:
  * http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-customize.html#stream-customize-scala
  *
  * Cookbook resource:
  * http://doc.akka.io/docs/akka/2.4.2/scala/stream/stream-cookbook.html#stream-cookbook-scala
  *
  */
trait StreamSource {

  /**
    * Method for creation a return of stream source element
    *
    * @return Source element that retrieves a single agent action per string
    */
  def source: Source[String, NotUsed]

  /**
    * Utility function to return the user defined' source element
    *
    * @return the stream's source object
    */
  def apply(): Source[String, NotUsed] = source
}

