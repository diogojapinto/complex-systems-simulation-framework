package com.cssim.analysis

import com.cssim.SystemManager
import com.cssim.learning.behavior.trees.ServicesProvider
import com.cssim.analysis.AnalysisDataModel.AnalysisDataModelProps


abstract class Service extends ServicesProvider {
  this: SystemManager =>

  val moduleName: String
  val worker: AnalysisWorker
  val dataModel: AnalysisDataModelProps
  val api: AnalysisApi

  addAnalysisModule(moduleName, worker, dataModel, api)
}
