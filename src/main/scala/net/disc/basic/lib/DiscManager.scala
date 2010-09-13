package net.disc.basic.lib

import scala.actors._
import Actor._

import net.liftweb._
import http._
import util._
import Helpers._
import net.liftweb.actor._

import scala.collection.immutable._

class DiscManager extends Actor {

  var playerList: List[(User)] = Nil

  def getUser(actor:LiftActor) = {
    playerList.filter((a) => a.actor == actor).head
  }

  def updateAfterControl(user:User) = {
    playerList.foreach(_.actor ! UpdatePlayer(user))
  }

  def generateRandomColor = {
    val r = new scala.util.Random
    var R:java.lang.Integer = new Integer(r.nextInt(255))
    var G:java.lang.Integer = new Integer(r.nextInt(255))
    var B:java.lang.Integer = new Integer(r.nextInt(255))
    String.format("#%x%x%x",R,G,B)
  }

  def act = {
    link(ActorWatcher)
    loop {
      react {
        case Subscribe(act) =>
          val user = new User(act,100,100, generateRandomColor)
          playerList = user :: playerList 
          playerList.foreach(_.actor ! Inside(user))

        case Unsubscribe(act) =>
          playerList = playerList.filter(_.actor ne act)

        case Control(who,what) =>
          val user = getUser(who)
          if (what == "up")
            user.x -= 10
          if (what == "down")
            user.x += 10
          if (what == "right")
            user.y += 10
          if (what == "left")
            user.y -= 10
         updateAfterControl(user)

        case _ => println("Manager - fallthru case")
      }
    }
  }
}

class User(actorVar:LiftActor, xVar:Int,yVar:Int, colorVar:String){
  var actor = actorVar
  var x = xVar
  var y = yVar
  var color= colorVar
}
case class UpdatePlayer(user:User)
case class Subscribe(act: LiftActor)
case class Unsubscribe(act: LiftActor)
case class Inside(user: User)
case class Control(who:LiftActor, direction:String)
