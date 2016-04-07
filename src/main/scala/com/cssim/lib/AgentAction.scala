package com.cssim.lib

import org.joda.time.Duration
import play.api.libs.json.Writes
import play.api.libs.json._

import scala.collection.mutable


abstract class AgentAction {
  val sourceId: String
  val targetId: String
  val attributes: mutable.Map[String, DataType]

  // amount of time passed since previous action
  val delay: Duration

  // warns the Simulation engine that this event is not to be simulated
  val FeedForwardOnly = false

  implicit val agentActionWrites = new Writes[AgentAction] {

    override def writes(action: AgentAction) = {

      val jsonObj = Json.obj(
        "sourceId" -> action.sourceId,
        "targetId" -> action.targetId
      )

      val attributesJsonObj =
        attributes.foldLeft(Json.obj()) { case (accum, (key, data)) =>
          data match {
            case Categorical(value) => accum + (key -> Json.toJson(value))
            case NumericalContinuous(value) => accum + (key -> Json.toJson(value))
            case NumericalDiscrete(value) => accum + (key -> Json.toJson(value))
          }
        }

      jsonObj.deepMerge(attributesJsonObj)
    }
  }

  override def toString: String = {
    Json.toJson(this).toString()
  }
}
