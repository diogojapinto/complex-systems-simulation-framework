package org.simulation

import org.simulation.circuit.{Adders, Circuit}

/**
  * Created by diogo on 27-01-2016.
  */
object Demo extends App {

  val circuit = new Circuit with Adders // with Multiplexers with FlipFlops with MultiCoreProcessors
  import circuit._

  val ain = wire("ain", true)
  val bin = wire("bin", false)
  val cin = wire("cin", true)
  val sout = wire("sout")
  val cout = wire("cout")

  probe(ain)
  probe(bin)
  probe(cin)
  probe(sout)
  probe(cout)

  fullAdder(ain, bin, cin, sout, cout)

  start()
}
