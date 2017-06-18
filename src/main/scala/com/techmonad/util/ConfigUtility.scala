package com.techmonad.util

import com.typesafe.config.ConfigFactory


object ConfigUtility {

  private val config = ConfigFactory.load()

  def getStringConf(path: String): String = config.getString(path)

}
