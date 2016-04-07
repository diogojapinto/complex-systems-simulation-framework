package com.nyctaxi.sim

import com.cssim.lib.NumericalDiscrete
import com.cssim.stream.Parser
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, Duration}

import scala.collection.mutable


object TripsParser extends Parser {

  val taxisLatestPickup = mutable.Map[String, DateTime]()

  val TaxiIdIndex = 1
  val PickupTimeIndex = 5
  val DurationIndex = 8
  val PickupLongitudeIndex = 10
  val PickupLatitudeIndex = 11
  val DropOffLongitudeIndex = 12
  val DropOffLatitudeIndex = 13

  private val datePattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  override def parse(obj: String): Trip = {
    val elements = obj.stripMargin.split(",")

    val sourceId = elements(TaxiIdIndex)
    val duration = NumericalDiscrete(elements(DurationIndex).toInt)
    val pickupTime = DateTime.parse(elements(PickupTimeIndex), datePattern)
    val pickupLatitude = Latitude(elements(PickupLatitudeIndex).toDouble)
    val pickupLongitude = Longitude(elements(PickupLongitudeIndex).toDouble)
    val dropOffLatitude = Latitude(elements(DropOffLatitudeIndex).toDouble)
    val dropOffLongitude = Longitude(elements(DropOffLongitudeIndex).toDouble)

    // compute delay since last event
    val latestPickup = taxisLatestPickup.getOrElseUpdate(sourceId, pickupTime)

    val delay = new Duration(pickupTime.getMillis - latestPickup.getMillis())

    val trip = Trip(sourceId, delay)
    trip.setPickupCoordinates(pickupLatitude, pickupLongitude)
    trip.setDropOffCoordinates(dropOffLatitude, dropOffLongitude)

    trip
  }
}
