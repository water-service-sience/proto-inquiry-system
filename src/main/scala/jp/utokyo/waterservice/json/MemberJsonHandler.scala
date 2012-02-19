package jp.utokyo.waterservice.json

import net.liftweb.http.rest.RestHelper
import net.liftweb.http.{S, GetRequest, Req}
import net.liftweb.json.JsonAST.{JField, JObject, JString, JArray,JDouble}
import jp.utokyo.waterservice.model.{Land, Member}
import net.liftweb.json.JsonDSL._
import net.liftweb.json._

/**
 *
 * User: takeshita
 * Create: 12/02/12 22:28
 */

object MemberJsonHandler extends RestHelper{

  serve{
    case Req("json" :: "search" :: "member" :: Nil,_,GetRequest) => {
      val term = S.param("term").openOr("")
      if(term.length >= 0){
        val members = Member.findForAgriKey(term) ::: (Member.findLike(term) ::: Member.findFromPhoneNumber(term) distinct)

        JArray(members.map(m => {
          JObject(List(
            JField("mId",JString(m.id.is.toString)),
            JField("agriKey",JString(m.agriKey.is)),
            JField("phone",JString(m.phoneNumber.is)),
            JField("value",JString(m.fullName)),
            JField("label",JString(m.fullName))
          ))
        }))
      }else{
        JArray(Nil)
      }
    }
    case Req("json" :: "find" :: "land" :: Nil , _ ,GetRequest) => {
      val memberId = S.param("mId").openOr("0").toLong

      val lands = Land.findFor(memberId)
      JArray( lands.map( land => {
        JObject(
          List(
            JField("id",JString(land.id.is.toString)),
            JField("lng",JDouble(land.longitde.is)),
            JField("lat",JDouble(land.latitude.is)),
            JField("address",JString(land.address.is))
            ))
      }))
    }
  }

}