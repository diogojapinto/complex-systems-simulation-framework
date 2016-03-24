package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Source

/**
  * Created by dpinto on 24/03/2016.
  */
trait StreamSource {
  def source: Source[Any, NotUsed]
}
