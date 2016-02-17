package org.simulation.circuit

import akka.actor.ActorRef

/**
  * Created by diogo on 27-01-2016.
  */
trait Adders extends Circuit {

  def halfAdder(a: ActorRef, b: ActorRef, s: ActorRef, c: ActorRef) {

    val d = wire
    val e = wire
    orGate(a, b, d)
    andGate(a, b, c)
    inverter(c, e)
    andGate(d, e, s)
  }

  def fullAdder(a: ActorRef, b: ActorRef, cin: ActorRef, sum: ActorRef, cout: ActorRef) {

    val s = wire
    val c1 = wire
    val c2 = wire
    halfAdder(a, cin, s, c1)
    halfAdder(b, s, sum, c2)
    orGate(c1, c2, cout)
  }
}
