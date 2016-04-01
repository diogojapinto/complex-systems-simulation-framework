package com.cssim.services

import com.cssim.services.analysis.AnalysisModule
import com.cssim.services.api.ApiModule

/**
  * Created by dpinto on 01/04/2016.
  */
case class Service(analysisModule: AnalysisModule, apiModule: ApiModule)
