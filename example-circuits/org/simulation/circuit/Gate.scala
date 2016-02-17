package org.simulation.circuit

import akka.actor.ActorRef
import org.simulation.circuit.Circuit.{Observer, SetSignal, SignalChanged}
import org.simulation.framework.{Clock, Simulant}

/**
  * Created by dpinto on 17/02/2016.
  */
abstract class Gate(in1: ActorRef, in2: ActorRef, out: ActorRef, circuit: Circuit) extends Simulant {

  import Clock._

  def computeOutput(s1: Boolean, s2: Boolean): Boolean

  val delay: Int

  val clock = circuit.clock
  clock ! this

  in1 ! Observer(self)
  in2 ! Observer(self)

  var s1, s2 = false

  def handleSimMessage(msg: Any): Unit = {
    msg match {
      case SignalChanged(w, sig) =>
        if (w == in1)
          s1 = sig
        if (w == in2)
          s2 = sig

        clock ! AfterDelay(
          delay,
          SetSignal(computeOutput(s1, s2)),
          out)
    }
  }
}
