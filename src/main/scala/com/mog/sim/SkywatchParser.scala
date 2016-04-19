package com.mog.sim

import com.cssim.lib.AgentAction
import com.cssim.stream.Parser
import org.joda.time.format.DateTimeFormat
import org.joda.time.{DateTime, DateTimeZone}
import play.api.libs.json.Json

import scala.collection.mutable


object SkywatchParser extends Parser {

  val taxisLatestPickup = mutable.Map[String, DateTime]()
  private val datePattern = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss")

  override def parse(obj: String): AgentAction = {

    val json = Json.parse(obj.replace("\\", "\\\\"))

    val insertDate = new DateTime(((json \ "insert_time").get).toString().toLong)



    AgentAction.default
  }
}
