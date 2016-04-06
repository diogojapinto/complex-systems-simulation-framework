package com.cssim.lib.datatype

import com.cssim.lib.NumericalDiscrete
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

object TimeStamp {

  private val DatePattern = DateTimeFormat.forPattern("MM/dd/yyyy HH:mm:ss")

  private def timeStamp2Numerical(value: String): Long = DateTime.parse(value).getMillis / 1000
  private def numerical2TimeStamp(value: Long): String = (new DateTime(value * 1000)).toString(DatePattern)

  def unapply(timeStamp: TimeStamp): Option[NumericalDiscrete] = Some(NumericalDiscrete(timeStamp2Numerical(timeStamp.dateTime)))
  def apply(arg: NumericalDiscrete): TimeStamp = new TimeStamp(numerical2TimeStamp(arg.value))

  def apply(dateTime: String) = new TimeStamp(dateTime)
}

class TimeStamp(val dateTime: String) extends NumericalDiscrete(TimeStamp.timeStamp2Numerical(dateTime))

