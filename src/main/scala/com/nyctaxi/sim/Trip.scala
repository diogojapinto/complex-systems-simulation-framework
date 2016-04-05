package com.nyctaxi.sim

import com.cssim.lib.{AgentAction, DataType}

import scala.collection.mutable
import scala.concurrent.duration._


class Trip(sourceId: String, targetId: String, delay: Duration) extends AgentAction {
  override val attributes: scala.collection.mutable.Map[String, DataType] = mutable.Map.empty

  def setSourceCoordinates(lat: Latitude, lon: Longitude) = {
    attributes("sourceLatitude") = lat
    attributes("sourceLongitude") = lon
  }
}
