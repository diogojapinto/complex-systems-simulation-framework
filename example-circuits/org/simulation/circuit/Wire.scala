package org.simulation.circuit

import akka.actor.{Props, ActorRef}
import org.simulation.framework.{Clock, Simulant}

/**
  * Created by dpinto on 17/02/2016.
  */

object Wire {
  def apply(name: String, circuit: Circuit, init: Boolean) = new Wire(name, circuit, init)

  def apply(name: String, circuit: Circuit) = new Wire(name, circuit, false)

  def apply(circuit: Circuit) = new Wire("unnamed", circuit, false)

  def props(name: String = "unnamed", circuit: Circuit, init: Boolean = false) =
    Props(apply(name, circuit, init))
}

class Wire(name: String, circuit: Circuit, init: Boolean) extends Simulant {

  import Clock._
  import Circuit._

  val clock = circuit.clock
  clock ! AddSimulant(self)

  private var sigVal = init
  private var observers: List[ActorRef] = List()

  def handleSimMessage(msg: Any): Unit = {
    msg match {
      case SetSignal(s) =>
        if (s != sigVal) {
          sigVal = s
          signalObservers()
        }
      case Observer(obs) =>
        addObserver(obs)
    }
  }

  def signalObservers(): Unit = {
    for (obs <- observers)
      clock ! AfterDelay(
        WireDelay,
        SignalChanged(self, sigVal),
        obs)
  }

  override def simStarting(): Unit = {
    signalObservers()
  }

  def addObserver(obs: ActorRef): Unit = {
    observers = obs :: observers
  }

  override def toString = "Wire(" + name + ")"
}