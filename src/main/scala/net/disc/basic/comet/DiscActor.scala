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

import net.disc.basic.snippet._

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

  private lazy val discManager: DiscManager = DiscController.getManager

  override def localSetup {
    discManager ! Subscribe(this, nick.is)
    super.localSetup
  }

  def render =  {
    <div class="game-container">
      <head><script type="text/javascript" src="/scripts/game.js"></script></head>
      <div id="who">
        {discManager.getNickNameList.map(n => <div id={n}>{n}</div>)}
      </div>

      <div id="messages"/>
      <div id="chat-input">
        <textarea id="message-textarea"></textarea>
        <button id="posting-button" onclick={(jsonCall("post_message", JE.JsObj("message" -> JE.ValById("message-textarea"))) & Call("clearChat")) }>
          Chat
        </button>
      </div>
   </div>
  }

  override def lowPriority = {
    case Inside(who) => 
      partialUpdate(AppendHtml("who", <div id={who}>{who}</div>))
    case IndexedMessage(id,what) =>
      val scroll = JqId("messages") >> JqScrollToBottom
      val append = AppendHtml("messages", <div class="message">{what}</div>)
      partialUpdate(append & scroll) 
  }

  override def handleJson(in: Any): JsCmd = {
    in match {
      case JsonCmd("post_message",_,params:Map[String,String],_) =>
        if(params("message").trim.length > 0)
          discManager ! Message(nick.is + ":" + params("message"))
        Noop

      case JsonCmd(_,_,d,_) => println("handleJson(): no match" + d); Noop
    }
  }
}
