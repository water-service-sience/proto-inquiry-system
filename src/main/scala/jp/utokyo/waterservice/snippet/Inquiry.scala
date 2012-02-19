package jp.utokyo.waterservice.snippet

import xml.NodeSeq

import net.liftweb.util.Helpers
import net.liftweb.http.js.JsCmds.SetHtml
import jp.utokyo.waterservice.model._
import jp.utokyo.waterservice.model.{Inquiry => InquiryDB}
import net.liftweb.common.{Failure, Full, Empty}
import net.liftweb.http.{S, SHtml, StatefulSnippet}
import java.text.{ParseException, SimpleDateFormat}
import SHtml._
import Helpers._

/**
 *
 * User: takeshita
 * Create: 12/02/10 1:19
 */

class Inquiry extends StatefulSnippet with QuerySupport with FormSupport {


  def dispatch  = {
    case "inputForm" => inputForm _
  }



  def inputForm( n : NodeSeq) : NodeSeq = {

    
    def fromDateAndTime = {
      val date = S.param("created_date").open_!
      val time = S.param("created_time").open_!

      val v = date + " " + time
      new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(v)
    }


    bind("e",n,
      "situation" -> {
        selectForm("situation",Situation.findAll().map(v => v.id.is.toString -> v.name.is),None )
      },
      "receiver" -> {
        selectForm("receiver",User.findAll().map(u => {
          u.id.is.toString -> u.fullName
        }),User.currentUser.map(_.id.is.toString).toOption)
      },
      "submit" -> submit("受付",() => {
        val inquiry = InquiryDB.create
        inquiry.member(paramAsLong("member_id"))
        inquiry.land(paramAsLong("place_id"))
        inquiry.created(fromDateAndTime)
        inquiry.detail(S.param("detail").openOr(""))
        inquiry.situation(paramAsLong("situation"))
        inquiry.receiver(paramAsLong("receiver"))
        inquiry.save()
      })
    )


  }



}