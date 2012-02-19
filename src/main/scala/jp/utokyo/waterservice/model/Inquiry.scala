package jp.utokyo.waterservice.model

import net.liftweb.mapper._
import java.util.Date

/**
 *
 * User: takeshita
 * Create: 12/02/12 18:14
 */
object Inquiry  extends Inquiry with LongKeyedMetaMapper[Inquiry] with CRUDify[Long, Inquiry]{



  def findInquiriesFor(userId : Long,  start : Int , count : Int) = {
    findAll(By(member,userId),OrderBy(created, Descending),StartAt(start),MaxRows(count))
  }

  def findLatestInquiries(start : Int , count : Int) = {
    findAll(OrderBy(created,Descending),StartAt(start),MaxRows(count))
  }


}

class Inquiry extends LongKeyedMapper[Inquiry] with IdPK{
  def getSingleton = Inquiry


  object member extends MappedLongForeignKey(this,Member)// with SimpleTextInput[Inquiry]

  object receiver extends MappedLongForeignKey(this,User)

  object inCharge extends MappedLongForeignKey(this,User)

  object land extends MappedLongForeignKey(this,Land)


  object situation extends MappedLongForeignKey(this,Situation)
  object situationExtra extends MappedString(this,100)
  object detail extends MappedText(this)
  object note extends MappedText(this)

  object created extends MappedDateTime(this)
  object status extends MappedEnum(this,InquiryStatus)

}

object InquiryStatus extends Enumeration{

  val NotProcessed = Value("未担当")
  val Process = Value("対応中")
  val Resolved = Value("解決(終了認証待ち)")
  val Feedback = Value("差し戻し")
  val Done = Value("終了")

}


object Record  extends Record with LongKeyedMetaMapper[Record] with CRUDify[Long, Record]{


  def findRecords(p : Inquiry) = {
    findAll(By(parent,p),OrderBy(wrote , Descending))
  }

}

class Record extends LongKeyedMapper[Record] with IdPK{
  def getSingleton = Record

  object parent extends MappedLongForeignKey(this,Inquiry)

  object writer extends MappedLongForeignKey(this,User)

  object message extends MappedText(this)

  object wrote extends MappedDateTime(this){
    override def defaultValue: Date = new Date()
  }

}


object Contact  extends Contact with LongKeyedMetaMapper[Contact] with CRUDify[Long, Contact]{


}

class Contact extends LongKeyedMapper[Contact] with IdPK{
  def getSingleton = Contact

  object parent extends MappedLongForeignKey(this,Inquiry)

  object finish extends MappedBoolean(this)

  object who extends MappedLongForeignKey(this,User)
  object whoExtra extends MappedString(this,100)
  object what extends MappedString(this,200)
  object created extends MappedDateTime(this)
  object finished extends MappedDateTime(this)

}





