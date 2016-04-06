package com.nyctaxi.sim

import com.cssim.lib.{AgentAction, DataType}
import org.joda.time.Duration

import scala.collection.mutable

object Trip {
  def apply(taxiId: String, delay: Duration) = new Trip(taxiId, delay)
}

class Trip(val taxiId: String, val delay: Duration) extends AgentAction {
  override val attributes: scala.collection.mutable.Map[String, DataType] = mutable.Map.empty

  override val sourceId: String = taxiId
  override val targetId: String = sourceId

  def setPickupCoordinates(lat: Latitude, lon: Longitude) = {
    attributes("pickupLatitude") = lat
    attributes("pickupLongitude") = lon
  }

  def setDropOffCoordinates(lat: Latitude, lon: Longitude) = {
    attributes("dropoffLatitude") = lat
    attributes("dropoffLongitude") = lon
  }
}
