/**
  * Created by dpinto on 18/02/2016.
  */

import akka.agent.Agent

import scala.concurrent.ExecutionContext.Implicits.global
//import system.dispatcher

val agent1 = Agent(3)
val agent2 = Agent(5)

// uses foreach
for (value <- agent1)
  println(value)

// uses map
val agent3 = for (value <- agent1) yield value + 1

// or using map directly
val agent4 = agent1 map (_ + 1)

// uses flatMap
val agent5 = for {
  value1 <- agent1
  value2 <- agent2
} yield value1 + value2

