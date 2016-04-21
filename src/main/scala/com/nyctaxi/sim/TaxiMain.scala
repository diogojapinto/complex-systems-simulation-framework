package com.nyctaxi.sim

import com.cssim.SystemManager
import com.cssim.analysis.services.echo.EchoServiceModule
import com.cssim.stream.StreamIngestor

object TaxiMain extends App {
  val manager = new SystemManager(StreamIngestor(TripsSource, TripsParser)) with EchoServiceModule

  manager.init()
}
