package org.simulation.circuit

import akka.actor.{Props, ActorDSL, ActorRef}
import org.simulation.framework.{Clock, Simulant, SimulationManager}

/**
  * Created by diogo on 27-01-2016.
  */

object Circuit {

  // simulation messages
  case class SetSignal(sig: Boolean)

  case class SignalChanged(wire: ActorRef, sig: Boolean)

  case class Observer(obs: ActorRef)

  // delay constants
  val WireDelay = 1
  val InverterDelay = 2
  val OrGateDelay = 3
  val AndGateDelay = 3
}

class Circuit extends SimulationManager {

  import Clock._
  import Circuit._

  lazy val systemName = "circuit-simulation"

  private[circuit] val clock = registerElement(Clock.props)

  private val dummyWire = registerElement(Wire.props("dummy", this))

  def wire(name: String, init: Boolean): ActorRef = registerElement(Wire.props(name, this, init))

  def wire(name: String): ActorRef = wire(name, false)

  def wire: ActorRef = wire("unnammed", false)

  def orGate(in1: ActorRef, in2: ActorRef, output: ActorRef) =
    ActorDSL.actor(system)(
      new Gate(in1, in2, output, this) {
        val delay = OrGateDelay

        def computeOutput(s1: Boolean, s2: Boolean) = s1 || s2
      }
    )

  def andGate(in1: ActorRef, in2: ActorRef, output: ActorRef) =
    ActorDSL.actor(system)(
      new Gate(in1, in2, output, this) {
        val delay = AndGateDelay

        def computeOutput(s1: Boolean, s2: Boolean) = s1 && s2
      }
    )

  def inverter(input: ActorRef, output: ActorRef) =
    ActorDSL.actor(system)(
      new Gate(input, dummyWire, output, this) {
        val delay = InverterDelay

        def computeOutput(s1: Boolean, s2: Boolean) = !s1
      }
    )

  // misc. utility methods
  def probe(wire: ActorRef) =
    registerElement(Probe.props(wire, this))

  def start(): Unit = {
    clock ! Start
  }
}