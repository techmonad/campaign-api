package com.techmonad.json

import org.json4s._
import org.json4s.native.JsonMethods.{parse => jParser}
import org.json4s.native.Serialization.{write => jWrite}


object JsonHelper {

  implicit val formats = DefaultFormats

  def write[T <: AnyRef](value: T): String = jWrite(value)

  def parse(value: String): JValue = jParser(value)

}
