package net.disc.basic.lib

import scala.collection.mutable.HashMap

import net.liftweb._
import util._
import http._

object DiscController {
  val DiscManager = new DiscManager
  DiscManager.start

  def getManager():DiscManager = {
    DiscManager
  }
}
