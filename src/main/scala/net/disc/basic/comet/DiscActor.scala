package net.disc.basic.comet

import scala.actors._
import scala.actors.Actor._
import net.liftweb.http._
import net.liftweb._
import mapper._
import S._
import SHtml._
import util._
import Helpers._
import js._
import JsCmds._
import JE._
import net.liftweb.http.js._
import net.liftweb.http.js.JsCmds._
import net.liftweb.common._
import net.liftweb.mapper._
import net.liftweb.util.Helpers._
import scala.xml._
import S._
import net.liftweb.util._
import JE._
import js.jquery._
import JqJsCmds._
import JqJE._

import net.disc.basic.lib._

class DiscActor extends CometActor {
  override def defaultPrefix = Full("disc")

  override def lifespan = Full(3 minutes)

  private lazy val discManager: DiscManager = DiscController.getManager

  override def localSetup {
    discManager ! Subscribe(this)
    super.localSetup
  }

  override def localShutdown {
    discManager ! Unsubscribe(this)
    super.localShutdown
  }

  def render =  {
    <div class="game-container">
      <div id="who">
        {discManager.playerList.map(n => <div id={n.actor.toString} style={"top:" + n.x + "px; left:" + n.y + "px; background-color:" + n.color  + ""} class="player"></div>)}
      </div>

      {Script(JsCmds.Function("post_to_server", List("cmd", "params"),jsonCall(JsVar("cmd"), JsVar("params"))))}
   </div>
  }

  def updateUser(user:User) = JsRaw("u('"+user.actor.toString+"',"+user.x+","+user.y+")")

  override def lowPriority = {
    case Inside(who) => 
      partialUpdate(AppendHtml("who", <div id={who.actor.toString} style={"background-color:"+who.color} class="player"></div>) & updateUser(who))
    case UpdatePlayer(user) =>
      partialUpdate(updateUser(user))
  }

  override def handleJson(in: Any): JsCmd = {
    in match {
      case JsonCmd("control",_,params:Map[String,String],_) =>
        discManager ! Control(this, params("direction"))
        Noop

      case JsonCmd(_,_,d,_) => println("handleJson(): no match" + d); Noop
    }
  }
}
