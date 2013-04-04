/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package univ.test;

import java.net.URL;
import univ.DepartmentUrlRedriver;

/**
 *
 * @author skiloop
 */
public class GoogleParserTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        String str = "</li></ul></div></div></div></div><div id=\"appbar\"><div id=\"extabar\"><div id=\"topabar\" style=\"position:relative\"><div class=\"ab_tnav_wrp\"><div><div id=resultStats>找到约 385,000 条结果<nobr>  （用时 0.30 秒）&nbsp;</nobr></div></div>  </div></div><div id=\"botabar\" style=\"display:none\"><div></div></div></div><div></div></div><div class=\"mw\" id=\"ucs\"></div><div class=\"mw\"><div id=\"rcnt\" style=\"clear:both;position:relative;zoom:1\"><div id=\"leftnavc\"></div><div id=\"center_col\"><span id=\"taw\" style=\"margin-right:0\"><div></div><div class=\"med\" style=\"padding:0 8px\"></div></span><div class=\"med\" id=\"res\" role=\"main\"><div id=\"topstuff\"></div><div id=\"search\"><!--a--><h2 class=\"hd\">搜索结果</h2><div id=\"ires\"><ol eid=\"MydDUb_jJMmZiQes_oD4BA\" id=\"rso\"><!--m--><li class=\"g\"><div class=\"vsc\" sig=\"j3V\">  <div data-ved=\"0CC8QkgowAA\">  <div data-ved=\"0CDAQkQowAA\"> </div>   </div>   <h3 class=\"r\"><a href=\"http://www.bit.edu.cn/xxgk/xysz/yhxy/index.htm\" target=_blank class=l onmousedown=\"return rwt(this,'','','','1','AFQjCNGyFCxTkby5rXrqKxGjI3Ga8e2GlQ','g8pZAPyEVPFOMnQOmEGHgg','0CDEQFjAA','','',event)\"><em>宇航学院</em> - <em>北京理工大学</em></a></h3><div class=\"s\"><div class=\"f kv\"><cite><span class=bc>www.<b>bit</b>.edu.cn &rsaquo; <a href=\"http://www.bit.edu.cn/xxgk/index.htm\" onmousedown=\"return rwt(this,'','','','1','AFQjCNGXAmPqy_TL6LwmUp3eZczAgpBKYQ','q4w5LhDiS17JDjIBU8eG5g','0CDIQ6QUoADAA','','',event)\" target=_blank>学校概况</a> &rsaquo; <a href=\"http://www.bit.edu.cn/xxgk/xysz/index.htm\" onmousedown=\"return rwt(this,'','','','1','AFQjCNEIvvhL3Q4xGsM5qyArusNL_SCSuA','q3fh1WOL8CZdlOxeShhHmw','0CDMQ6QUoATAA','','',event)\" target=_blank>学院设置</a></span></cite><span class=\"gl\"> - <a href=\"http://webcache.googleusercontent.com/search?q=cache:Ug6LxgWhhjkJ:www.bit.edu.cn/xxgk/xysz/yhxy/index.htm+&amp;cd=1&amp;hl=zh-CN&amp;ct=clnk&amp;gl=cn\" onmousedown=\"return rwt(this,'','','','1','AFQjCNGJGTdPHHfJfoEc10Ev9S3slmuLUw','Nezwt9HYaIQQoCFuHUGWaw','0CDUQIDAA','','',event)\" target=\"_blank\" target=\"_blank\">网页快照</a></span><span class=\"vshid\"><a href=\"/search?hl=zh-CN&amp;newwindow=1&amp;safe=strict&amp;qscrl=1&amp;q=related:www.bit.edu.cn/xxgk/xysz/yhxy/index.htm+%E5%8C%97%E4%BA%AC%E7%90%86%E5%B7%A5%E5%A4%A7%E5%AD%A6+%E5%AE%87%E8%88%AA%E5%AD%A6%E9%99%A2&amp;tbo=1&amp;sa=X&amp;ei=MydDUb_jJMmZiQes_oD4BA&amp;ved=0CDYQHzAA\">类似结果</a></span></div><div class=\"esc slp\" id=\"poS0\" style=\"display:none\">您已公开地对此项 +1。&nbsp;<a href=\"#\" class=\"fl\">撤消</a></div><span class=\"f\">20+ 项 &ndash; </span><span class=\"st\">您现在的位置： 首页» 学校概况» 学院设置» <em>宇航学院</em> <b>...</b><br></span><table class=\"tsnip\"><tbody><tr><td><em>北京理工大学</em>第二届飞行器创新大赛评比结果揭晓<td>2013-01-07<tr><td>北<em>理工宇航学院</em>举行新年联谊会<td>2012-12-28</table></div></div><!--n--></li><!--m--><li class=\"g\"><div class=\"vsc\" sig=\"4Au\">  <div data-ved=\"0CDkQkgowAQ\">  <div data-ved=\"0CDoQkQowAQ\"> </div>   </div>   <h3 class=\"r\"><a href=\"http://sae.bit.edu.cn/\" target=_blank class=l onmousedown=\"return rwt(this,'','','','2','AFQjCNFE9jXIKkAckklfPtec6ee1ja8icQ','kuqb_rNxbqBFCThEv9ZABQ','0CDsQFjAB','','',event)\"><em>宇航学院</em> - <em>北京理工大学</em></a></h3><div class=\"s\"><div class=\"f kv\"><cite>sae.bit.edu.cn/</cite><span class=\"gl\"> ";
        String url=DepartmentUrlRedriver.googleParser(str);
        System.out.println(url);        
        System.out.println(url.split("\\.",2)[1]);
    }
}
