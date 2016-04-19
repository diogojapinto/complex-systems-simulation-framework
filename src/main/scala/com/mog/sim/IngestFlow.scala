package com.mog.sim

import com.cssim.lib.{AgentAction, DataType}
import org.joda.time.Duration

import scala.collection.mutable

object IngestFlow {
  def apply(sourceId: String, targetId: String, delay: Duration): IngestFlow =
    new IngestFlow(sourceId, targetId, delay)
}

class IngestFlow(val sourceId: String, val targetId: String, val delay: Duration) extends AgentAction {

  override val FeedForwardOnly = true

  override val attributes = mutable.Map.empty[String, DataType]


}
