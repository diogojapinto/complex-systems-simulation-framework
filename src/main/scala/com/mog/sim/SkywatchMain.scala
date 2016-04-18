package com.mog.sim

import com.cssim.SystemManager
import com.cssim.analysis.services.echo.EchoServiceModule
import com.cssim.stream.StreamIngestor

/**
  * Created by dpinto on 18/04/2016.
  */
object SkywatchMain extends App{
  val manager = new SystemManager(new StreamIngestor(, )) with EchoServiceModule

  manager.init()
}
