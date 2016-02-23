package com.cs_sim.api

/**
  * Created by dpinto on 18/02/2016.
  */

import com.twitter.finatra.http.test.{HttpTest, EmbeddedHttpServer}
import com.twitter.finatra.httpclient.RequestBuilder

class StreamingServerFeatureTest extends FeatureTest with HttpTest {

  override val server = new EmbeddedHttpServer(
    new StreamingServer,
    streamResponse = true)

  "post streaming json" in {
    val request = RequestBuilder.post("/tweets").chunked

    val tweets = for (i <- 1 to 100) yield {
      Tweet(text = s"msg $i", location = Some("US"))
    }

    // Write to request in separate thread
    pool {
      writeJsonArray(request, tweets, delayMs = 25)
    }

    val response = server.httpRequest(request)
    response.printAsyncStrings()
  }
}