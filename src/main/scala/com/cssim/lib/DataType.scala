package com.cssim.lib


abstract class DataType
case class Categorical(value: String) extends DataType
case class NumericalDiscrete(value: Int) extends DataType
case class NumericalContinuous(value: Double) extends DataType