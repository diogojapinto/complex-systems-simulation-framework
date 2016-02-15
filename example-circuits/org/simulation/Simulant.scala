package org.simulation

import scala.actors.Actor
import scala.actors.Actor._

/**
  * Created by diogo on 27-01-2016.
  */
trait Simulant extends Actor {

  val clock: Clock
  def handleSimMessage(msg: Any)

  def simStarting(): Unit = { }

  override def act(): Unit = {
    loop {
      react {
        case Stop => exit()
        case Ping(time) =>
          if (time == 1) simStarting()
          clock ! Pong(time, self)
        case msg => handleSimMessage(msg)
      }
    }
  }
  start()
}
