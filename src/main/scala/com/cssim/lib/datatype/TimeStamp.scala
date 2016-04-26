package com.cssim.lib.datatype

import com.cssim.lib.CategoricalOrdered
import org.joda.time.DateTime
import org.joda.time.format.DateTimeFormat

/*object TimeStamp {

  private val datePattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  private def timeStamp2Numerical(value: String): Long = DateTime.parse(value).getMillis / 1000
  private def numerical2TimeStamp(value: Long): String = (new DateTime(value * 1000)).toString(datePattern)

  def unapply(timeStamp: TimeStamp): Option[CategoricalOrdered] = Some(CategoricalOrdered(timeStamp2Numerical(timeStamp.dateTime)))
  def apply(arg: CategoricalOrdered): TimeStamp = new TimeStamp(numerical2TimeStamp(arg.value))

  def apply(dateTime: String) = new TimeStamp(dateTime)
}

class TimeStamp(val dateTime: String) extends CategoricalOrdered(TimeStamp.timeStamp2Numerical(dateTime))

*/