package jp.utokyo.waterservice.snippet

import net.liftweb.util.Helpers._
import net.liftweb.http.{SHtml, S, StatefulSnippet}
import jp.utokyo.waterservice.model.{Record, User, InquiryStatus, Inquiry => InquiryDB}
import xml.{Text, NodeSeq}

/**
 *
 * User: takeshita
 * Create: 12/02/19 18:32
 */

class InquiryEdit extends StatefulSnippet with QuerySupport with FormSupport{
  def dispatch = {
    case "id" => showId _
    case "member" => memberInfo _
    case "info" => info _
    case "edit" => edit _
    case "place" => place _
    case "records" => records _
    case "addRecord" => addRecord _
  }
  
  lazy val inquiryId = paramAsLong("id")

  lazy val inquiry = {
    InquiryDB.findByKey(inquiryId).open_!
  }

  def showId(n : NodeSeq) : NodeSeq = {
    Text(inquiryId.toString)
  }

  def memberInfo(n : NodeSeq) : NodeSeq = {
    val member = inquiry.member.obj.open_!
    bind("e",n,
      "name" ->{ member.fullName.is + "(" + member.readFamilyName + " " + member.readFirstName + ")"},
      "agriKey" -> member.agriKey.is,
      "phone" -> member.phoneNumber.is,
      "address" -> member.address.is
    )
  }

  def info(n : NodeSeq) : NodeSeq = {
    bind("e",n,
      "created" -> dateFormat(inquiry.created.is),
      "receiver" -> inquiry.receiver.obj.map(_.fullName).openOr("不明"),
      "situation" -> inquiry.situation.obj.map(_.name.is).openOr("不明"),
      "detail" -> inquiry.detail.is
    )
  }

  import SHtml._
  
  def edit(n : NodeSeq) : NodeSeq = {
    bind("e",n,
      "status" -> {
        def c(v:InquiryStatus.Value) = v.id.toString -> v.toString
        val seq : Seq[(String,String)] = InquiryStatus.values.toSeq.map(c(_))
        selectForm("status",seq,Some(inquiry.status.is.id.toString))
      } ,
      "incharge" -> {
        selectForm("inCharge",("0" -> "担当者無し") :: User.findAll().map(u => u.id.is.toString -> u.fullName),
          inquiry.inCharge.obj.map(_.id.is.toString).toOption)
      },
      "submit" -> submit ("更新",() => {
        val builder = new StringBuilder()
        val newStatus = InquiryStatus(paramAsLong("status").toInt)
        val newInCharge = paramAsLongOr("inCharge",0)
        if(newStatus != inquiry.status.is){
          builder.append("ステータスを%sに変更\r\n".format(newStatus))
        }
        if(newInCharge != inquiry.inCharge.is){
          builder.append("担当を%sに変更\r\n".format(User.findByKey(newInCharge).map(_.fullName).openOr("担当なし")))
        }
        inquiry.status(newStatus)
        inquiry.inCharge(newInCharge)
        inquiry.save()
        if(builder.length > 0){
          val r = Record.create
          r.parent(inquiry.id.is)
          r.message(builder.toString())
          r.writer(User.currentUser.map(_.id.is).openOr(0L))
          r.save()

        }
        S.redirectTo(S.request.map(r => r.uri + "?id=" + inquiry.id.is).open_!)
      })
    )
  }

  def place(n : NodeSeq) : NodeSeq = {
    val land = inquiry.land.obj.open_!
    
    bind("e",n,
    "lng" -> land.longitde.is.toString,
    "lat" -> land.latitude.is.toString)
  }

  def records(n : NodeSeq) : NodeSeq = {
    val records = Record.findRecords(inquiry)
    records.flatMap( r => {
      bind("e",n,
        "writer" -> r.writer.obj.map(_.fullName).openOr("不明"),
        "wrote" -> dateFormat(r.wrote.is),
        "message" ->  r.message.is
      )
    })
  }

  def addRecord(n : NodeSeq) : NodeSeq = {
    bind("e",n,
      "submit" -> submit ("書き込み" , () => {
        val message = S.param("message").open_!
        val r = Record.create
        r.parent(inquiry.id.is)
        r.message(message)
        r.writer(User.currentUser.map(_.id.is).openOr(0L))
        r.save()
        S.redirectTo(S.request.map(r => r.uri + "?id=" + inquiry.id.is).open_!)
        
      }))

  }
}
