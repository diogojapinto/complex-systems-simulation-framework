/**
  * Created by dpinto on 28/03/2016.
  */

def balancer[In, Out](worker: Flow[In, Out, Any], workerCount: Int): Flow[In, Out, NotUsed] = {
  import GraphDSL.Implicits._

  Flow.fromGraph(GraphDSL.create() { implicit b =>
    val balancer = b.add(Balance[In](workerCount, waitForAllDownstreams = true))
    val merge = b.add(Merge[Out](workerCount))

    for (_ <- 1 to workerCount) {
      // for each worker, add an edge from the balancer to the worker, then wire
      // it to the merge element
      balancer ~> worker ~> merge
    }

    FlowShape(balancer.in, merge.out)
  })
}

val processedJobs: Source[Result, NotUsed] = myJobs.via(balancer(worker, 3))

