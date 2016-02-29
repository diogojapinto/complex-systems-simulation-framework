/**
  * Created by dpinto on 29/02/2016.
  */
/*
val splitLines = Framing.delimiter(ByteString("\n"), 256)

val csvUploads =
  path("metadata" / LongNumber) { id =>
    entity(as[Multipart.FormData]) { formData =>
      val done = formData.parts.mapAsync(1) {
        case b: BodyPart if b.filename.exists(_.endsWith(".csv")) =>
          b.entity.dataBytes
            .via(splitLines)
            .map(_.utf8String.split(",").toVector)
            .runForeach(csv =>
              metadataActor ! MetadataActor.Entry(id, csv))
        case _ => Future.successful(Unit)
      }.runWith(Sink.ignore)

      // when processing have finished create a response for the user
      onSuccess(done) {
        complete {
          "ok!"
        }
      }
    }
  }

*/