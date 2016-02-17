package com.cs_sim.simulation.framework

import akka.actor.{Actor, ActorRef}


/**
  * Created by diogo on 27-01-2016.
  */

trait Simulant extends Actor {

  import Clock._

  val clock: ActorRef

  def handleSimMessage(msg: Any)

  def simStarting(): Unit = {}

  override def receive = {
    case Stop =>
      context stop self
    case Ping(time) =>
      if (time == 1) simStarting()
      clock ! Pong(time, self)
    case msg =>
      handleSimMessage(msg)
  }
}
