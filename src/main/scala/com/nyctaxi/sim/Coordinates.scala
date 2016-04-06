package com.nyctaxi.sim

import com.cssim.lib.NumericalContinuous

class Latitude(lat: Double) extends NumericalContinuous(lat)
class Longitude(lon: Double) extends NumericalContinuous(lon)

// TODO: edit value conversion functions

object Latitude {

  private def latitude2Numerical(value: Double): Double = value
  private def numerical2Latitude(value: Double): Double = value

  def unapply(latitude: Latitude): Option[NumericalContinuous] = Some(NumericalContinuous(latitude2Numerical(latitude.value)))
  def apply(arg: NumericalContinuous): Latitude = new Latitude(numerical2Latitude(arg.value))

  def apply(arg: Double) = new Latitude(arg)
}

object Longitude {

  private def longitude2Numerical(value: Double): Double = value
  private def numerical2Longitude(value: Double): Double = value

  def unapply(longitude: Longitude): Option[NumericalContinuous] = Some(NumericalContinuous(longitude2Numerical(longitude.value)))
  def apply(arg: NumericalContinuous): Longitude = new Longitude(numerical2Longitude(arg.value))

  def apply(arg: Double) = new Longitude(arg)
}