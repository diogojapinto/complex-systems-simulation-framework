package com.nyctaxi.sim

import org.deeplearning4j.nn.api.OptimizationAlgorithm
import org.deeplearning4j.nn.conf.{MultiLayerConfiguration, NeuralNetConfiguration, Updater}
import org.deeplearning4j.nn.conf.distribution.UniformDistribution
import org.deeplearning4j.nn.conf.layers.{GravesLSTM, RnnOutputLayer}
import org.deeplearning4j.nn.multilayer.MultiLayerNetwork
import org.deeplearning4j.nn.weights.WeightInit
import org.deeplearning4j.optimize.listeners.ScoreIterationListener
import org.nd4j.linalg.lossfunctions.LossFunctions.LossFunction

/**
  * Created by diogo on 04-05-2016.
  */
object TripsLSTMSim extends App {

  // settings
  val lstmLayerSize = 200

  // build iterator
  val iter = TripsIterator

  //Set up network configuration:
  val conf: MultiLayerConfiguration = new NeuralNetConfiguration.Builder()
    .optimizationAlgo(OptimizationAlgorithm.STOCHASTIC_GRADIENT_DESCENT)
    .iterations(1)
    .learningRate(0.1)
    .rmsDecay(0.95)
    .seed(12345)
    .regularization(true)
    .l2(0.001)
    .list(3)
    .layer(0, new GravesLSTM.Builder()
      .nIn(iter.inputColumns)
      .nOut(lstmLayerSize)
      .updater(Updater.RMSPROP)
      .activation("tanh")
      .weightInit(WeightInit.DISTRIBUTION)
      .dist(new UniformDistribution(-0.08, 0.08))
      .build())
    .layer(1, new GravesLSTM.Builder()
      .nIn(lstmLayerSize)
      .nOut(lstmLayerSize)
      .updater(Updater.RMSPROP)
      .activation("tanh")
      .weightInit(WeightInit.DISTRIBUTION)
      .dist(new UniformDistribution(-0.08, 0.08))
      .build())
    .layer(2, new RnnOutputLayer.Builder(LossFunction.MCXENT)
    .activation("softmax")
    .updater(Updater.RMSPROP)
    .nIn(lstmLayerSize)
    .nOut(iter.totalOutcomes)
      .weightInit(WeightInit.DISTRIBUTION)
      .dist(new UniformDistribution(-0.08, 0.08))
      .build())
    .pretrain(false)
    .backprop(true)
    .build()
  val net: MultiLayerNetwork = new MultiLayerNetwork(conf)
  net.init()
  net.setListeners(new ScoreIterationListener(1))


  //Print the  number of parameters in the network (and for each layer)
  val layers = net.getLayers
  var totalNumParams = 0

  for (i <- Array.range(0, layers.length))
  while (i < layers.length) {
    {
      val nParams: Int = layers(i).numParams
      System.out.println("Number of parameters in layer " + i + ": " + nParams)
      totalNumParams += nParams
    }
  }
  System.out.println("Total number of network parameters: " + totalNumParams)
}
