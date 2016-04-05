package com.nyctaxi.sim

import com.cssim.lib.{AgentAction, DataType}
import com.cssim.stream.Parser

import scala.concurrent.duration.Duration
import scala.concurrent.duration._


object TripsParser extends Parser {
  override def parse(obj: String): AgentAction =
    new AgentAction {override val agentIdentifier: String = "odih"
    override val delay: Duration = 3 seconds
    override val attributes: Map[String, DataType] = Map.empty
  }
}
