package com.cssim.lib

import scala.concurrent.duration.Duration


abstract class AgentAction {
  val agentIdentifier: String
  val attributes: Map[String, DataType]
  val duration: Duration
}
