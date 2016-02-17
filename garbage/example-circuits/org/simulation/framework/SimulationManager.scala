package org.simulation.framework

import akka.actor.{Actor, ActorRef, ActorSystem, Props}

/**
  * Created by dpinto on 17/02/2016.
  */

object SimulationManager {
  var elemCounter = 0
}

trait SimulationManager {

  import SimulationManager._

  def systemName: String

  val system = ActorSystem(systemName)

  def registerElement(props: Props): ActorRef = {
    elemCounter += 1
    system.actorOf(props, "Actor_"+ elemCounter)
  }
}
