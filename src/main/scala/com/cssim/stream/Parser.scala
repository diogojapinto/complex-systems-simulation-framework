package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Flow
import com.cssim.lib.AgentAction

/**
  * Trait used in a class that defines a mapping function from elements that arrive in a String
  * to properly instantiated AgentAction elements. This function is used to map the incoming elements ingested by a
  * source into workable ones.
  */
trait Parser {

  /**
    * User-defined mapping function.
    *
    * @param obj individual element received in String format
    * @return an AgentAction element
    */
  def parse(obj: String): AgentAction

  /**
    * Utility function that frames the defined mapping function onto a Akka Streams Flow element
    *
    * @return Flow element installable into an Akka Stream graph
    */
  def apply(): Flow[String, AgentAction, NotUsed] =
    Flow[String]
      .map { obj =>

        try {
          parse(obj)

        } catch {
          case e: Exception =>
            println("Exception found while parsing:")
            e.printStackTrace()

            AgentAction.default
        }
      }
}