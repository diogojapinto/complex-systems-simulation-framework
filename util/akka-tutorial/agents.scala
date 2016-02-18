/**
  * Created by dpinto on 18/02/2016.
  */

import scala.concurrent.ExecutionContext.Implicits.global
import akka.agent.Agent
val agent = Agent(5)

val result = agent()

agent send 7

agent send (_ + 1)
agent send (_ * 2)

val result2 = agent.get