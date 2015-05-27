package models

import play.api.db._
import play.api.Play.current
import anorm._
import anorm.{ResultSetParser,SqlQueryResult,RowParser,SQL,SqlQuery}, SqlParser._
import models.SqlResultParser._

case class ContentBlock (cblock_id:Int, cblock_title:String, cblock_content:String, cblock_timestamp:String, cblock_bgcolor:String, cblock_bgimg:String, cblock_link:String) {
  def this() = this(0,"","","","","","")
}

object ContentBlock {
  val CBlockPars: RowParser[ContentBlock] = {
	int("cblock_id") ~
	str("cblock_title") ~
	str("cblock_content") ~
	str("cblock_timestamp") ~
	str("cblock_bgcolor") ~
	str("cblock_bgimg") ~
	str("cblock_link") map {
		case cblock_id ~ cblock_title ~ cblock_content ~ cblock_timestamp ~ cblock_bgcolor ~ cblock_bgimg ~ cblock_link => 
          ContentBlock(cblock_id,cblock_title,cblock_content,cblock_timestamp,cblock_bgcolor,cblock_bgimg,cblock_link)
	}
  }
}

object Content {
  def getContent(page_id:Int,lang_id:String): (String,List[ContentBlock]) = DB.withConnection {
    implicit connection => 
    val res: SqlQueryResult = SQL("""
      select a.cblock_id,cblock_title,cblock_content,to_char(cblock_timestamp,'yyyy-mm-dd') cblock_timestamp,cblock_bgcolor,cblock_bgimg,cblock_link
      from content_block a, (
        select content_order,cblock_id
        from content
        where page_id = {page_id}
        and lang_id = {lang_id}
      ) b 
      where a.cblock_id = b.cblock_id
      order by content_order asc;
    """).on("page_id" -> page_id,"lang_id" -> lang_id).executeQuery() 
    val result0 = new ContentBlock()
    parseSqlResult[ContentBlock](res,ContentBlock.CBlockPars,result0)
  }
} 

