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

  private var discCommandsActors: List[LiftActor] = Nil
  private var i = 1;

  def act = {
    link(ActorWatcher)
    loop {
      react {
        case Subscribe(act) =>
          println("I just suscribed actor:" + act)
          discCommandsActors = act :: discCommandsActors
          discCommandsActors.foreach(_ ! Inside("I just suscribed" + act.toString))

          case Unsubscribe(act) =>
          println("I just UNsuscribed actor:" + act)
          discCommandsActors = discCommandsActors.filter(_ ne act)

          case Message(what) =>
          discCommandsActors.foreach(_ ! IndexedMessage(i,what))
          i = i + 1

        case _ => println("Manager - fallthru case")
      }
    }
  }
}

case class Subscribe(act: LiftActor)
case class Unsubscribe(act: LiftActor)
case class Inside(who: String)
case class Message(what: String)
case class IndexedMessage(id: Int, what:String)