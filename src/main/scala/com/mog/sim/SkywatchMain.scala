package com.mog.sim

import com.cssim.SystemManager
import com.cssim.analysis.services.echo.EchoServiceModule
import com.cssim.stream.StreamIngestor


object SkywatchMain extends App{
  val manager = new SystemManager(new StreamIngestor(SkywatchSource, SkywatchParser)) with EchoServiceModule

  manager.init()
}
