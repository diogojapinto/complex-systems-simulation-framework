package com.cssim.lib


final abstract case class DataType()
case class Categorical(value: String)
case class NumericalDiscrete(value: Int)
case class NumericalContinuous(value: Double)