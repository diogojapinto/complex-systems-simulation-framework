package com.cs_sim.api

/**
  * Created by dpinto on 18/02/2016.
  */

import com.twitter.concurrent.AsyncStream
import com.twitter.finatra.http.Controller

class StreamingController extends Controller {

  post("/tweets") { tweets: AsyncStream[Tweet] =>
    tweets map { tweet =>
      "Created tweet " + tweet
    }
  }
}