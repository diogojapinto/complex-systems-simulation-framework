package org.simulation.framework

import akka.actor.{PoisonPill, Actor, ActorRef, Props}

/**
  * Created by diogo on 27-01-2016.
  */

object Clock {

  case class Ping(time: Int)

  case class Pong(time: Int, from: ActorRef)

  case class WorkItem(time: Int, msg: Any, target: ActorRef)

  case class AfterDelay(delay: Int, msg: Any, target: ActorRef)

  case object Start

  case object Stop

  case class AddSimulant(sim: ActorRef)

  def props: Props = Props(new Clock)
}

class Clock extends Actor {

  import Clock._

  private var running = false
  private var currentTime = 0
  private var agenda: List[WorkItem] = List()
  private var allSimulants: List[ActorRef] = List()
  private var busySimulants: Set[ActorRef] = Set.empty

  def add(sim: ActorRef): Unit = {
    allSimulants = sim :: allSimulants
  }

  def receive = {
    case AfterDelay(delay, msg, target) =>
      val item = WorkItem(currentTime + delay, msg, target)
      agenda = insert(agenda, item)

    case Pong(time, sim) =>
      assert(time == currentTime)
      assert(busySimulants contains sim)
      busySimulants -= sim
      if (running && busySimulants.isEmpty)
        advance()

    case Start =>
      running = true
      if (busySimulants.isEmpty)
        advance()

    case AddSimulant(sim: ActorRef) =>
      add(sim)
  }

  private def advance(): Unit = {
    if (agenda.isEmpty && currentTime > 0) {
      println("** Agenda empty. Clock exiting at time " + currentTime + ".")
      //self ! PoisonPill

      import scala.concurrent.ExecutionContext.Implicits.global

      context.system.terminate().foreach { _ =>
        println("Actor system was shut down")
      }
      return
    }

    currentTime += 1
    println("Advancing to time " + currentTime)

    processCurrentEvents()
    for (sim <- allSimulants)
      sim ! Ping(currentTime)

    busySimulants = Set.empty ++ allSimulants
  }

  private def processCurrentEvents(): Unit = {
    val todoNow = agenda.takeWhile(_.time <= currentTime)

    agenda = agenda.drop(todoNow.length)

    for (WorkItem(time, msg, target) <- todoNow) {
      assert(time == currentTime)
      target ! msg
    }
  }

  private def insert(ag: List[WorkItem], item: WorkItem): List[WorkItem] = {
    if (ag.isEmpty || item.time < ag.head.time) item :: ag
    else ag.head :: insert(ag.tail, item)
  }
}