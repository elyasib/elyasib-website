package controllers

import play.api._
import play.api.mvc._
import play.api.mvc.Request._
import models._
import models.Page._
import play.api.Play.current

object Application extends Controller {

  def home(langVar: String, pageName: String) = Action { request =>
    val currntLang = Language.getCurrntLang(langVar)
    if (currntLang._1 == "0") {
      val otherLangs = Language.getOtherLangs(langVar)
      val langsNum:Int = otherLangs._2.length
      val page = getPage(pageName)
      if (page._1 == "0") {
        val frontScreen = FScreen.getFScreen(page._2(0).page_id,langVar)
        val topMenu = TopMenu.getTopMenu(page._2(0).page_id,langVar) 
        val content = Content.getContent(page._2(0).page_id,langVar)
        if (content._1 == "0") {
          page._2(0).ptype_id match {
            case 1 => {
                Ok(views.html.home(currntLang._2,otherLangs._2,langsNum,frontScreen._2(0).fscrn_title,frontScreen._2(0).fscrn_abstract,topMenu._2,content._2))
            }
            case 2  => {
                Ok(views.html.text(currntLang._2,otherLangs._2,langsNum,frontScreen._2(0).fscrn_title,frontScreen._2(0).fscrn_abstract,topMenu._2,content._2))
            }
            case _ => {
                Ok(views.html.notFound(langVar,request.host + request.uri, try {request.headers("referer")} catch {case e: Exception => "http://www.elyasibsantiago.com"}))
            }
          }
        } else 
          Ok(views.html.notFound(langVar,request.host + request.uri, try {request.headers("referer")} catch {case e: Exception => "http://www.elyasibsantiago.com"}))
      } else 
        Ok(views.html.notFound(langVar,request.host + request.uri, try {request.headers("referer")} catch {case e: Exception => "http://www.elyasibsantiago.com"}))
    } else
      Ok(views.html.notFound(langVar,request.host + request.uri, try {request.headers("referer")} catch {case e: Exception => "http://www.elyasibsantiago.com"}))
  }
}
