package jp.utokyo.waterservice.model

import net.liftweb.mapper._


/**
 *
 * User: takeshita
 * Create: 12/02/12 18:14
 */

object Member  extends Member with LongKeyedMetaMapper[Member] with CRUDify[Long, Member]{

  var phonePrefix = "090"

  def findLike( name : String) = {
    findAll( Like(this.name, "%" + name + "%")) :::
      findAll( Like(this.readFamilyName, name + "%")) :::
      findAll( Like(this.readFirstName, name + "%"))
  }

  def findFromPhoneNumber( phoneNumberStartWith : String) = {
    findAll(Like(this.phoneNumber, phoneNumberStartWith + "%")) :::
    findAll(Like(this.phoneNumber, phonePrefix + phoneNumberStartWith + "%"))
  }

  def findForAgriKey(agriKey : String) = {
    findAll(By(this.agriKey, agriKey))
  }

}
class Member extends LongKeyedMapper[Member] with IdPK{
  def getSingleton = Member

  object name extends MappedString(this,100){
    override def dbIndexed_? = true
  }
  object readFamilyName extends MappedString(this,100){
    override def dbIndexed_? = true
  }
  object readFirstName extends MappedString(this,100){
    override def dbIndexed_? = true
  }
  object agriKey extends MappedString(this,100) {
    override def dbIndexed_? = true

  }
  object address extends MappedString(this,200)
  object phoneNumber extends MappedString(this,30){
    override def dbIndexed_? = true
  }

  def fullName = name

}

object Land  extends Land with LongKeyedMetaMapper[Land] with CRUDify[Long, Land]{

  def findFor( memberId : Long) = {
    findAll( By(this.owner, memberId))
  }

}
class Land extends LongKeyedMapper[Land] with IdPK{
  def getSingleton = Land

  object owner extends MappedLongForeignKey(this,Member) with SimpleTextInput[Land]
  object shortName extends MappedString(this,100)
  object landKey extends MappedString(this,100)
  object address extends MappedString(this,200)
  object latitude extends MappedDouble(this)
  object longitde extends MappedDouble(this)

}