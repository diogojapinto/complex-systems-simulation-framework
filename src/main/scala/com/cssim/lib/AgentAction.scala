package com.cssim.lib

import scala.concurrent.duration.Duration


abstract class AgentAction  {
  val sourceId: String
  val targetId: String
  val attributes: Map[String, DataType]
  val delay: Duration
}
