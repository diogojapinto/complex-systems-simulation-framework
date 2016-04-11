package com.cssim.learning.behavior.trees

import akka.actor.ActorRef
import com.cssim.analysis.AnalysisDataModel.AnalysisDataModelProps
import com.cssim.analysis.{AnalysisApi, AnalysisDataModel, AnalysisWorker}

import scala.collection.mutable

/**
  * Created by dpinto on 11/04/2016.
  */
class ServicesProvider {

  val analysisGraphComponents = mutable.Buffer.empty[(String, AnalysisWorker, AnalysisDataModelProps)]
  val analysisApis = mutable.Buffer.empty[AnalysisApi]
  val analysisDataModelActors = mutable.Map.empty[String, ActorRef]

  def addAnalysisModule(name: String,
                        worker: AnalysisWorker,
                        dataModel: AnalysisDataModelProps,
                        api: AnalysisApi): Unit = {

    analysisGraphComponents.+=:((name, worker, dataModel))
    analysisApis.+=:(api)
  }

}
