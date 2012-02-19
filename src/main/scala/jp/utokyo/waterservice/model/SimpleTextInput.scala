package jp.utokyo.waterservice.model

import xml.Elem
import net.liftweb.http.S._
import net.liftweb.common.Full._
import net.liftweb.mapper.MappedField._
import net.liftweb.common.{Empty, Full, Box}
import net.liftweb.mapper.{MappedField, IsElem}

/**
 *
 * User: takeshita
 * Create: 12/02/10 1:08
 */

trait SimpleTextInput[T <: net.liftweb.mapper.Mapper[T]] extends MappedField[Long, T]  {

  override def _toForm: Box[Elem] =
    fmapFunc({s: List[String] => this.setFromAny(s)}){name =>
      Full(appendFieldId(<input type="text"
                                name={name}
                                value={is.toString} />))}


  override def toForm: Box[Elem] = {
    _toForm
  }
}