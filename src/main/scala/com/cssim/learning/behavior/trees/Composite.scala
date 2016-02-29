package com.cssim.learning.behavior.trees

/**
  * Created by dpinto on 23/02/2016.
  */
abstract class Composite(val nodes: Seq[Node]) extends Node("d<")

class Selector(nodes: Seq[Node]) extends Composite(nodes)

object Selector {
  def apply(nodes: Node*): Selector = new Selector(nodes)

  def unapplySeq(nodes: Seq[Node]): Option[Seq[Node]] = Some(nodes)
}

class Sequence(nodes: Seq[Node]) extends Composite(nodes)
