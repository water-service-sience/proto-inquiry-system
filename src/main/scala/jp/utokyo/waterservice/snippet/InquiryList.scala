package jp.utokyo.waterservice.snippet

import net.liftweb.http.StatefulSnippet
import xml.NodeSeq
import jp.utokyo.waterservice.model.{Inquiry => InquiryDB}
import net.liftweb.util.Helpers._
import java.text.SimpleDateFormat

/**
 *
 * User: takeshita
 * Create: 12/02/19 17:43
 */

class InquiryList extends StatefulSnippet {
  def dispatch  = {
    case "list" => list _
  }

  lazy val page = 0

  val ItemsParPage = 20

  def list(n : NodeSeq) : NodeSeq = {
    val inquiries = InquiryDB.findLatestInquiries(page * ItemsParPage,ItemsParPage)


    inquiries.flatMap( inq => {
      bind("e",n,
        "id" -> {
          <span>{inq.id.is.toString}(<a href={"/inquiry_detail?id=" + inq.id}>対応する</a>)</span>
        },
        "status" -> inq.status.is.toString,
        "member" -> inq.member.obj.map(_.fullName.is).getOrElse("不明"),
        "place" -> inq.land.obj.map(_.shortName.is).getOrElse("不明"),
        "received" -> new SimpleDateFormat("yyyy/MM/dd HH:mm").format(inq.created.is),
        "incharge" -> inq.inCharge.obj.map(_.fullName).getOrElse("担当者無し")
      )
    })

  }


}
