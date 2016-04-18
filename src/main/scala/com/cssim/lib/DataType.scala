package com.cssim.lib

/**
  * Super-class for the case classes that encase the agent action's data
  */
abstract class DataType

/**
  * Data type for unordered categorical data
  * @param value data represented as a String
  */
case class CategoricalNominal(val value: String) extends DataType

/**
  *
  * @param value
  */
case class CategoricalOrdered(val value: Long) extends DataType
case class Quantitative(val value: Double) extends DataType