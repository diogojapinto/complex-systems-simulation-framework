package com.cssim.learning.behavior.trees

import akka.actor.ActorRef
import com.cssim.analysis.AnalysisDataModel.AnalysisDataModelProps
import com.cssim.analysis.AnalysisApi

import scala.collection.mutable


class ServicesProvider {

  val analysisGraphComponents = mutable.Buffer.empty[(String, AnalysisDataModelProps)]
  val analysisApis = mutable.Buffer.empty[AnalysisApi]
  val analysisDataModelActors = mutable.Map.empty[String, ActorRef]

  def addAnalysisModule(name: String,
                        dataModel: AnalysisDataModelProps,
                        api: AnalysisApi): Unit = {

    analysisGraphComponents.+=:((name, dataModel))
    analysisApis.+=:(api)
  }
}
