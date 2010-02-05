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

object DiscActor {
   var prefixNum = 0;
   def nextNum() = {
       prefixNum = prefixNum + 1;
       prefixNum
   } 
}

class DiscActor extends CometActor {
  override def defaultPrefix = Full("disc" + nextNum)

  private lazy val discManager: DiscManager = DiscController.getManager

  override def localSetup {
    discManager ! Subscribe(this)
    super.localSetup
  }

  override def localShutdown {
    super.localShutdown
  }

  def render =  {
    <span>
      <div id="messages">
      </div>
      <div id="who">
      </div>
      <textarea id="message"></textarea>
      <button id="postingButton" onclick={jsonCall("post_message", JE.JsObj("message" -> JE.ValById("message")))}>
        Post
      </button>
    </span>
  }

  override def lowPriority = {
    case Inside(who) => 
      partialUpdate(AppendHtml("who", <div>{who}</div>))
    case IndexedMessage(id,what) => 
      partialUpdate(PrependHtml("messages", <div class="message">{what}</div>)) 
  }

  override def handleJson(in: Any): JsCmd = {
    in match {
      case JsonCmd("post_message",_,params:Map[String,String],_) =>
        if(params("message").length > 0)
          discManager ! Message(params("message"))
        Noop
      case JsonCmd(_,_,d,_) => println("handleJson(): no match" + d); Noop
    }
  }
}