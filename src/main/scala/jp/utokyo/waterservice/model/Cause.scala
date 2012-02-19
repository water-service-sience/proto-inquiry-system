package jp.utokyo.waterservice.model

import net.liftweb.mapper._


/**
 *
 * User: takeshita
 * Create: 12/02/12 18:18
 */
object Situation  extends Situation with LongKeyedMetaMapper[Situation] with CRUDify[Long, Situation]{


}

class Situation extends LongKeyedMapper[Situation] with IdPK{
  def getSingleton = Situation
  
  object name extends MappedString(this,100)
  object explanation extends MappedText(this)
}

class Cause {

}