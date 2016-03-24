package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cssim.lib.AgentAction

/**
  * Created by dpinto on 24/03/2016.
  */
trait Parser {
  def parser: Flow[String, AgentAction, NotUsed]
}
