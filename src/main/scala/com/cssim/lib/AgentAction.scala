package com.cssim.lib

import org.joda.time.Duration
import play.api.libs.json.{Writes, _}

import scala.collection.mutable

/**
  * Companion object for the class AgentAction.
  */
object AgentAction {

  /**
    * Returns a default AgentAction
    *
    * @return default AgentAction object
    */
  def default: AgentAction = new AgentAction {
    override val sourceId: String = "empty"
    override val delay: Duration = new Duration(0)
    override val attributes: mutable.Map[String, DataType] = mutable.Map.empty
    override val targetId: String = "empty"
  }
}

/**
  * Class that encodes the information that flows in the application's stream processing pipeline.
  */
abstract class AgentAction {

  /**
    * unique identifier of the action's source
    */
  val sourceId: String

  /**
    *   unique identifier of the action's source
    */
  val targetId: String
  val attributes: mutable.Map[String, DataType]

  /**
    * amount of time passed since previous action
    */
  val delay: Duration

  /**
    * warns the Simulation engine that this event is not to be simulated
    */
  val FeedForwardOnly = false

  /**
    * Converts a given AgentAction into a properly formed JSON element
    */
  private implicit val agentActionWrites = new Writes[AgentAction] {

    override def writes(action: AgentAction) = {

      val jsonObj = Json.obj(
        "sourceId" -> action.sourceId,
        "targetId" -> action.targetId
      )

      val attributesJsonObj =
        attributes.foldLeft(Json.obj()) { case (accum, (key, data)) =>
          data match {
            case CategoricalNominal(value) => accum + (key -> Json.toJson(value))
            case Quantitative(value) => accum + (key -> Json.toJson(value))
            case CategoricalOrdered(value) => accum + (key -> Json.toJson(value))
          }
        }

      jsonObj.deepMerge(attributesJsonObj)
    }
  }

  /**
    * Represents an AgentAction as a String, in JSON format
    *
    * @return a JSON String
    */
  override def toString: String = {
    Json.toJson(this).toString()
  }
}
