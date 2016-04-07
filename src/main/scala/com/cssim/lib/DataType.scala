package com.cssim.lib


abstract class DataType
case class Categorical(val value: String) extends DataType
case class NumericalDiscrete(val value: Long) extends DataType
case class NumericalContinuous(val value: Double) extends DataType