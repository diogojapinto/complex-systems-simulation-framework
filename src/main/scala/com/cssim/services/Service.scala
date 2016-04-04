package com.cssim.services

import com.cssim.services.analysis.AnalysisModule
import com.cssim.services.api.ApiModule


final case class Service(analysisModule: AnalysisModule, apiModule: ApiModule)