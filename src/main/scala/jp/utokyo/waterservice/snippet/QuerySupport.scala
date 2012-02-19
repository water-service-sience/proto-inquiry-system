package jp.utokyo.waterservice.snippet

import net.liftweb.http.S
import java.text.{ParseException, SimpleDateFormat}
import java.util.Date

/**
 *
 * User: takeshita
 * Create: 12/02/19 18:33
 */

trait QuerySupport {

  def paramAsDate( name : String) = {

    try{
      S.param(name).map(v => new SimpleDateFormat("yyyy/MM/dd HH:mm").parse(v)).get
    }catch{
      case e : ParseException => {
        println("Value:%s is not date format!(%s)".format(name,S.param(name).get))
        throw e
      }
      case e => {
        println("Value:%s is not exist".format(name))
        throw e
      }
    }
  }

  def paramAsLongOr(name : String,default : Long) = {
    try{paramAsLong(name)}
    catch{
      case e : Exception => default
    }
  }

  def paramAsLong( name : String) = {

    try{
      S.param(name).map( _.toLong).get
    }catch{
      case e : NumberFormatException => {
        println("Value:%s is not number!(%s)".format(name,S.param(name).get))
        throw e
      }
      case e => {
        println("Value:%s is not exist".format(name))
        throw e
      }
    }


  }
  def paramAsString(name : String) = {
    try{
      S.param(name).open_!
    }catch{
      case e => {
        println("Value:%s is not exist".format(name))
        throw e
      }
    }
  }

  def dateFormat(date : Date) = {
    new SimpleDateFormat("yyyy/MM/dd HH:mm").format(date)
  }

}
