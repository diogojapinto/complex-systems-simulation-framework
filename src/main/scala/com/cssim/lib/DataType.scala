package com.cssim.lib

/**
  * Super-class for the case classes that encase the agent action's data
  */
abstract class DataType

/**
  * Data type for unordered categorical data
  *
  * @param value data represented as a String
  */
case class CategoricalNominal(val value: String) extends DataType

/**
  * Data type for ordered categorical data.
  * In order to use, must define a subclass
  *
  * @param value integer value corresponding to the data's category
  */
abstract case class CategoricalOrdered(val value: Long) extends DataType {
  val possibleValues: Set[Long]
}

/**
  * Data type for continuous quantitative data.
  * Values should be scales to values between 0 and 1. For that, create a subclass (e.g. NewData) that defines the
  * following functions in a companion object:
  *
  * def unapply(arg: NewData): Option[Quantitative] = Some(Quantitative(newData2Quantitative(arg.value)))
  * def apply(arg: Quantitative): NewData = new Latitude(numerical2NewData(arg.value))
  *
  * @param value quantitative value
  */
case class Quantitative(val value: Double) extends DataType