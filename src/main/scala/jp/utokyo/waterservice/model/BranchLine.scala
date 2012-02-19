package jp.utokyo.waterservice.model

import net.liftweb.mapper._
import xml.NodeSeq
import net.liftweb.common.{Full, Box}


/**
 *
 * User: takeshita
 * Create: 12/02/10 0:52
 */

object BranchLine  extends BranchLine with LongKeyedMetaMapper[BranchLine] with CRUDify[Long, BranchLine]{



}

class BranchLine extends LongKeyedMapper[BranchLine] with IdPK{
  def getSingleton = BranchLine

  object name extends MappedString(this,100)

}

object SubBranchLine  extends SubBranchLine with LongKeyedMetaMapper[SubBranchLine] with CRUDify[Long, SubBranchLine]{


  def findFor( branchLineId : Long) = {
    findAll( By(branchLine,branchLineId) )
  }

}

class SubBranchLine extends LongKeyedMapper[SubBranchLine] with IdPK{
  def getSingleton = SubBranchLine

  object branchLine extends MappedLongForeignKey(this,BranchLine) with SimpleTextInput[SubBranchLine]
  object name extends MappedString(this,100)

}

