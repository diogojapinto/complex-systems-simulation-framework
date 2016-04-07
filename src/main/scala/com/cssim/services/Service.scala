package com.cssim.services


final case class Service(worker: AnalysisWorker,
                         dataModel: AnalysisDataModel,
                         api: AnalysisApi)