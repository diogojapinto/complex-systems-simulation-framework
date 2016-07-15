package com.nyctaxi.sim

import java.util

import org.deeplearning4j.datasets.iterator.DataSetIterator
import org.nd4j.linalg.dataset.DataSet
import org.nd4j.linalg.dataset.api.DataSetPreProcessor

/**
  * Created by diogo on 04-05-2016.
  */
object TripsIterator extends DataSetIterator{

  val nrDataFields = 4

  // Total nr lines: 14776615
  override def next(num: Int): DataSet = ???

  override def batch(): Int = ???

  override def cursor(): Int = ???

  override def totalExamples(): Int = ???

  override def inputColumns(): Int = nrDataFields

  override def setPreProcessor(preProcessor: DataSetPreProcessor): Unit = ???

  override def getLabels: util.List[String] = ???

  override def totalOutcomes(): Int = nrDataFields

  override def reset(): Unit = ???

  override def numExamples(): Int = ???

  override def next(): DataSet = ???

  override def hasNext: Boolean = ???
}
