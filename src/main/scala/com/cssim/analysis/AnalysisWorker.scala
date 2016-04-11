package com.cssim.analysis

import akka.stream.stage.{GraphStage, GraphStageLogic}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.cssim.lib.AgentAction
import AnalysisDataModel.ProcessedData

abstract class AnalysisWorker extends GraphStage[FlowShape[AgentAction, ProcessedData]] {

  val in: Inlet[AgentAction] = Inlet("AnalysisWorker.in")
  val out: Outlet[ProcessedData] = Outlet("AnalysisWorker.out")

  override def shape: FlowShape[AgentAction, ProcessedData] = new FlowShape(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic

}
