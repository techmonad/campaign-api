package com.techmonad.logger

import org.slf4j.LoggerFactory
import org.slf4j.Logger


trait Logging {

  protected val logger: Logger = LoggerFactory.getLogger(this.getClass())

  protected def debug(message: => String): Unit = logger.debug(message)

  protected def debug(message: => String, exception: => Throwable): Unit = logger.debug(message, exception)

  protected def info(message: => String, exception: => Throwable): Unit = logger.info(message, exception)

  protected def info(message: => String): Unit = logger.info(message)

  protected def warn(message: => String): Unit = logger.warn(message)

  protected def warn(message: => String, exception: => Throwable): Unit = logger.warn(message, exception)

  protected def error(message: => String): Unit = logger.error(message)

  protected def error(message: => String, exception: => Throwable): Unit = logger.error(message, exception)


}
