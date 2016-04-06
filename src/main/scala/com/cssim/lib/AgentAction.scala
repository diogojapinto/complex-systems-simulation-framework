package com.cssim.lib

import org.joda.time.Duration

import scala.collection.mutable


abstract class AgentAction  {
  val sourceId: String
  val targetId: String
  val attributes: mutable.Map[String, DataType]

  // amount of time passed since previous action
  val delay: Duration

  // warns the Simulation engine that this event is not to be simulated
  val FeedForwardOnly = false
}
