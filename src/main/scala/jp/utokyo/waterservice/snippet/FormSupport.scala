package jp.utokyo.waterservice.snippet

/**
 *
 * User: takeshita
 * Create: 12/02/19 20:01
 */

trait FormSupport {
  
  
  def selectForm(name : String,  values : Seq[(String, String)] , select : Option[String]) = {
    <select name={name}>
      {
        values.map( v => {
          if( select.map(_ == v._1).getOrElse(false)){
            <option value={v._1.toString} selected="true">{v._2}</option>
          }else{
            <option value={v._1.toString}>{v._2}</option>
          }
        })
      }
    </select>
  }

}
