package jp.utokyo.waterservice.snippet

import jp.utokyo.waterservice.model.User
import net.liftweb.http.{S, SHtml, StatefulSnippet}
import xml.{Text, NodeSeq}
import SHtml._
import net.liftweb.util.Helpers
import Helpers._
import net.liftweb.common.Full
import net.liftweb.mapper.By

/**
 *
 * User: takeshita
 * Create: 12/02/19 21:57
 */

class SignUp extends StatefulSnippet with QuerySupport {
  def dispatch: SignUp#DispatchIt = {
    case "signIn" => signIn _
    case "signUp" => signUp _
    case "currentUser" => currentUser _
  }
  
  def currentUser(n : NodeSeq) : NodeSeq = {
    if(User.currentUser.isEmpty){
      Text("未ログイン")
    }else{
      bind("e",n,
        "id" -> User.currentUser.open_!.id.is.toString,
        "name" -> User.currentUser.open_!.fullName
      )
    }
  }

  def signIn(n : NodeSeq) : NodeSeq = {
    bind("e",n,
    "submit" -> SHtml.submit ("ログイン", () => {
      User.find(By(User.username,paramAsString("username"))).map( u => {

        if(u.myPassword.is == paramAsString("password")){
          User.logUserIn(u)
          S.redirectTo("/index.html")
        }else{
          S.warning("パスワードが違います。")
        }
        "ok"
      }).openOr({
        S.warning("ユーザーが存在しません")
        "ng"
      })
    }))

  }
  def signUp(n : NodeSeq) : NodeSeq = {
    
    bind("e",n,
    "submit" -> SHtml.submit("作成" , () => {
      val p1 = paramAsString("password1")
      val p2 = paramAsString("password2")
      if(p1 != p2){
        throw new Exception("Input same password!")
      }
      val user = User.create
      user.myPassword(p1)
      user.username(paramAsString("username"))
      user.firstName(paramAsString("first_name"))
      user.lastName(paramAsString("family_name"))
      user.email(paramAsString("email"))
      user.phoneNumber(paramAsString("phone"))
      user.save()

      User.logUserIn(user)
      S.redirectTo("/index.html")
    }))

  }
}
