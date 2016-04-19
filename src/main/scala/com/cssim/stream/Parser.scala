package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cssim.lib.AgentAction

trait Parser extends {

  def parse(obj: String): AgentAction

  def apply(): Flow[String, AgentAction, NotUsed] =
    Flow[String]
      .map { obj =>
        try {
          parse(obj)
        } catch {
          case e: Exception =>
            println("Exception found while parsing")
            e.printStackTrace()

            AgentAction.default
        }
      }
}