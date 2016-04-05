package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.cssim.lib.AgentAction

trait Parser extends GraphStage[FlowShape[String, AgentAction]] {

  def parse(obj: String): AgentAction

  def apply(): Flow[String, AgentAction, NotUsed] =
    Flow[String]
      .map(parse)

  private[this] val in = Inlet[String]("Parser.in")
  private[this] val out = Outlet[AgentAction]("Parser.out")

  override val shape = new FlowShape(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic =
    new GraphStageLogic(shape) {

      setHandlers(in, out, new InHandler with OutHandler {
        override def onPush(): Unit = {
          pull(in)
        }

        override def onPull(): Unit = {
          push(out, parse(grab(in)))
        }
      })
    }
}