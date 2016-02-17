package org.simulation.circuit

import akka.actor.{ActorRef, Props}
import org.simulation.circuit.Circuit.{SignalChanged, Observer}
import org.simulation.framework.Clock.AddSimulant
import org.simulation.framework.Simulant

/**
  * Created by dpinto on 17/02/2016.
  */

object Probe {

  def apply(wire: ActorRef, circuit: Circuit) = new Probe(wire, circuit)

  def props(wire: ActorRef, circuit: Circuit) = Props(apply(wire, circuit))
}

class Probe(wire: ActorRef, circuit: Circuit) extends Simulant {
  val clock = circuit.clock
  clock ! AddSimulant(self)
  wire ! Observer(self)

  def handleSimMessage(msg: Any): Unit = {
    msg match {
      case SignalChanged(w, s) =>
        println("signal " + w + " changed to " + s)
    }
  }
}
