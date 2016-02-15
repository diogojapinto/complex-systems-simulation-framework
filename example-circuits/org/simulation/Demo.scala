package org.simulation

/**
  * Created by diogo on 27-01-2016.
  */
object Demo extends App {

  val circuit = new Circuit with Adders // with Multiplexers with FlipFlops with MultiCoreProcessors
  import circuit._

  val ain = new Wire("ain", true)
  val bin = new Wire("bin", false)
  val cin = new Wire("cin", true)
  val sout = new Wire("sout")
  val cout = new Wire("cout")

  probe(ain)
  probe(bin)
  probe(cin)
  probe(sout)
  probe(cout)

  fullAdder(ain, bin, cin, sout, cout)

  circuit.start()
}
