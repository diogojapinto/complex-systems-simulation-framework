package com.cssim.services

import com.cssim.SystemManager


abstract class Service(name: String) {
  this: SystemManager =>

  val worker: AnalysisWorker
  val dataModel: AnalysisDataModel
  val api: AnalysisApi

  api.moduleName = name

  addAnalysisModule(name, worker, dataModel, api)
}
