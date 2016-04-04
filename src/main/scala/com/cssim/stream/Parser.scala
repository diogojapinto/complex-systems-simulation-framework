package com.cssim.stream

import akka.NotUsed
import akka.stream.scaladsl.Flow
import akka.stream.stage.{GraphStage, GraphStageLogic, InHandler, OutHandler}
import akka.stream.{Attributes, FlowShape, Inlet, Outlet}
import com.cssim.lib.AgentAction

trait Parser[T] extends GraphStage[FlowShape[T, AgentAction]] {

  def parse(obj: T): AgentAction

  def apply(): Flow[T, AgentAction, NotUsed] =
    Flow[T]
      .map(parse)

  private[this] val in = Inlet[T]("Parser.in")
  private[this] val out = Outlet[AgentAction]("Parser.out")

  override private[this] val shape = FlowShape(in, out)

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