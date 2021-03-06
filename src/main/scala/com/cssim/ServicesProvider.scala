package com.cssim

import akka.actor.ActorRef
import com.cssim.analysis.AnalysisApi
import com.cssim.analysis.AnalysisDataModel.AnalysisDataModelProps

import scala.collection.mutable

/**
  * Class from which SystemManager and custom services are subclassed.
  * Enables the usage of the Stackable Trait pattern to add in compile time services to the global SystemManager
  */
abstract class ServicesProvider {

  /**
    *
    */
  protected val analysisGraphComponents = mutable.Buffer.empty[(String, AnalysisDataModelProps)]
  protected val analysisApis = mutable.Buffer.empty[AnalysisApi]
  implicit val analysisDataModelActors = mutable.Map.empty[String, ActorRef]

  def addAnalysisModule(name: String,
                        dataModel: AnalysisDataModelProps,
                        api: AnalysisApi): Unit = {

    analysisGraphComponents.+=:((name, dataModel))
    analysisApis.+=:(api)
  }
}
