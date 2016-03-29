/**
  * Created by dpinto on 28/03/2016.
  */

import akka.stream.stage._
class DigestCalculator(algorithm: String) extends GraphStage[FlowShape[ByteString, ByteString]] {
  val in = Inlet[ByteString]("DigestCalculator.in")
  val out = Outlet[ByteString]("DigestCalculator.out")
  override val shape = FlowShape.of(in, out)

  override def createLogic(inheritedAttributes: Attributes): GraphStageLogic = new GraphStageLogic(shape) {
    val digest = MessageDigest.getInstance(algorithm)

    setHandler(out, new OutHandler {
      override def onPull(): Trigger = {
        pull(in)
      }
    })

    setHandler(in, new InHandler {
      override def onPush(): Trigger = {
        val chunk = grab(in)
        digest.update(chunk.toArray)
        pull(in)
      }

      override def onUpstreamFinish(): Unit = {
        emit(out, ByteString(digest.digest()))
        completeStage()
      }
    })

  }
}
val digest: Source[ByteString, NotUsed] = data.via(new DigestCalculator("SHA-256"))

