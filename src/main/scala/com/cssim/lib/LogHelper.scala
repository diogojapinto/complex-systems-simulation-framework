package com.cssim.lib

import org.apache.log4j.Logger

/**
  * Utility trait to provide classes with simple logging functionality, using Log4J
  */
trait LogHelper {
  val loggerName = this.getClass.getName

  /**
    * Logger object to be used by classes to output messages
    */
  lazy val logger = Logger.getLogger(loggerName)
}
