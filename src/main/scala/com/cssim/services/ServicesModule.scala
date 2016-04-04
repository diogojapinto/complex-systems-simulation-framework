package com.cssim.services

import com.cssim.SystemManager
import com.cssim.services.analysis.AnalysisModule
import com.cssim.services.api.ApiModule


abstract class ServicesModule {
  this: SystemManager =>

  val moduleServices: List[(AnalysisModule, ApiModule)]

  services.append(moduleServices: _*)
}
