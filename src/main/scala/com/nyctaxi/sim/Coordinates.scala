package com.nyctaxi.sim

import com.cssim.lib.Quantitative

class Latitude(lat: Double) extends Quantitative(lat)
class Longitude(lon: Double) extends Quantitative(lon)

// TODO: edit value conversion functions

object Latitude {

  private def latitude2Numerical(value: Double): Double = value
  private def numerical2Latitude(value: Double): Double = value

  def unapply(latitude: Latitude): Option[Quantitative] = Some(Quantitative(latitude2Numerical(latitude.value)))
  def apply(arg: Quantitative): Latitude = new Latitude(numerical2Latitude(arg.value))

  def apply(arg: Double) = new Latitude(arg)
}

object Longitude {

  private def longitude2Numerical(value: Double): Double = value
  private def numerical2Longitude(value: Double): Double = value

  def unapply(longitude: Longitude): Option[Quantitative] = Some(Quantitative(longitude2Numerical(longitude.value)))
  def apply(arg: Quantitative): Longitude = new Longitude(numerical2Longitude(arg.value))

  def apply(arg: Double) = new Longitude(arg)
}