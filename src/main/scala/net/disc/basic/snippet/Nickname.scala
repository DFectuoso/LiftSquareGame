package net.disc.basic.snippet

import net.liftweb.common._
import net.liftweb._
import net.liftweb.util._
import http._
import mapper._
import scala.xml._

object nick extends SessionVar("")

class Nickname {
  def render(xhtml:NodeSeq):NodeSeq = {
    S.param("nickname") match {
      case Full(n) => nick(n)
      case _ => nick("")
    }
    nick.is match {
      case n if n != "" =>  <lift:comet type="DiscActor" /> 
      case _ => renderNicknameForm
    }
  }

  def renderNicknameForm:NodeSeq= {
    <div class="login">
      <form type="post">
        <h1>Nickname</h1>
        <input type="text" name="nickname"/>
        <input type="submit" value="NOW NOW NOW"/>
      </form>
    </div>
  }
}
