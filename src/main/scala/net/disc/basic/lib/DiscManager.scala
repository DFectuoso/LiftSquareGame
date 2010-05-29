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

  private var discCommandsActors: List[(LiftActor)] = Nil
  private var nickNameList: List[(User)] = Nil
  private var i = 1;

  def getNickNameList = nickNameList

  def getUser(nick:String) = {
    nickNameList.filter((a) => a.nick == nick)
  }

  def updateAfterControl(user:List[User]) = {
   discCommandsActors.foreach(_ ! UpdatePlayer(user.head))
  }

  def act = {
    link(ActorWatcher)
    loop {
      react {
        case Subscribe(act,nickname) =>
          println("I just suscribed actor:" + act + " with Nickname " + nickname)
          val user = new User(i,nickname,0,0)
          nickNameList = user :: nickNameList
          i = i + 1
          discCommandsActors = act :: discCommandsActors
          discCommandsActors.foreach(_ ! Inside(user))

        case Unsubscribe(act) =>
          println("I just UNsuscribed actor:" + act)
          discCommandsActors = discCommandsActors.filter(_ ne act)

        case Message(what) =>
          discCommandsActors.foreach(_ ! IndexedMessage(i,what))
          i = i + 1
      
        case Control(who,what) =>
          val user = getUser(who)
          if (what == "up")
            user.map(u => u.x = u.x - 10)
          if (what == "down")
            user.map(u => u.x = u.x + 10)
          if (what == "right")
            user.map(u => u.y = u.y + 10)
          if (what == "left")
            user.map(u => u.y = u.y - 10)
         updateAfterControl(user)

        case _ => println("Manager - fallthru case")
      }
    }
  }
}

case class UpdatePlayer(user:User)
class User(idVar:Int,nickVar:String, xVar:Int,yVar:Int){
  var id = idVar
  var nick = nickVar
  var x = xVar
  var y = yVar
}
case class Subscribe(act: LiftActor, Nickname:String)
case class Unsubscribe(act: LiftActor)
case class Inside(user: User)
case class Message(what: String)
case class Control(who:String, direction:String)
case class IndexedMessage(id: Int, what:String)
