package com.mog.sim

import com.cssim.SystemManager
import com.cssim.analysis.services.echo.EchoServiceModule
import com.cssim.stream.StreamIngestor
import com.typesafe.scalalogging.LazyLogging


object SkywatchMain extends App with LazyLogging {

  val manager = new SystemManager(new StreamIngestor(SkywatchSource, SkywatchParser)) with EchoServiceModule  // ver tese 30

  manager.init()
}
