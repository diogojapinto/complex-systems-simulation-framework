package com.cs_sim.learning.behavior

/**
  * Created by dpinto on 23/02/2016.
  */
abstract class Composite(val nodes: Seq[Node]) extends Node("d<")

class Selector(nodes: Seq[Node]) extends Composite(nodes)

object Selector {
  def apply(nodes: Node*): Selector = new Selector(nodes)

  def unapplySeq(nodes: Seq[Node]): Option[Seq[Node]] = Some(nodes)
}

//class Sequence extends Composite


object Main extends App {
  val sel = (Node("a"), Node("b"), Node("c"))


  sel match {
    case Selector(rest@_*) => {
      println(rest)
    }
    case a@_ => println("hello")
  }
}