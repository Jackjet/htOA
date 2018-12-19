package com.kwchina.core.cms.util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HtmlUtil {
	private static final String regEx_script = "<script[^>]*?>[\\s\\S]*?<\\/script>"; // 定义script的正则表达式
    private static final String regEx_style = "<style[^>]*?>[\\s\\S]*?<\\/style>"; // 定义style的正则表达式
    private static final String regEx_html = "<[^>]+>"; // 定义HTML标签的正则表达式
    private static final String regEx_space = "\\s*|\t|\r|\n";//定义空格回车换行符
    
    /**
     * @param htmlStr
     * @return
     *  删除Html标签
     */
    public static String delHTMLTag(String htmlStr) {
    	htmlStr = htmlStr.replaceAll("<br />", "\r\n");
    	htmlStr = htmlStr.replaceAll("<br/>", "\r\n");
    	htmlStr = htmlStr.replaceAll("<br>", "\r\n");
    	
        Pattern p_script = Pattern.compile(regEx_script, Pattern.CASE_INSENSITIVE);
        Matcher m_script = p_script.matcher(htmlStr);
        htmlStr = m_script.replaceAll(""); // 过滤script标签

        Pattern p_style = Pattern.compile(regEx_style, Pattern.CASE_INSENSITIVE);
        Matcher m_style = p_style.matcher(htmlStr);
        htmlStr = m_style.replaceAll(""); // 过滤style标签

        Pattern p_html = Pattern.compile(regEx_html, Pattern.CASE_INSENSITIVE);
        Matcher m_html = p_html.matcher(htmlStr);
        htmlStr = m_html.replaceAll(""); // 过滤html标签

//        Pattern p_space = Pattern.compile(regEx_space, Pattern.CASE_INSENSITIVE);
//        Matcher m_space = p_space.matcher(htmlStr);
//        htmlStr = m_space.replaceAll(""); // 过滤空格回车标签
        return htmlStr.trim(); // 返回文本字符串
    }
    
    public static String getTextFromHtml(String htmlStr){
    	htmlStr = delHTMLTag(htmlStr);
    	htmlStr = htmlStr.replaceAll("&nbsp;", "");
    	htmlStr = htmlStr.substring(0, htmlStr.indexOf("。")+1);
    	return htmlStr;
    }
    
    public static void main(String[] args) {
    	String str = "<p style=\"LINE-HEIGHT: 23pt; MARGIN: 0cm 0cm 0pt; LAYOUT-GRID-MODE: char; mso-line-height-rule: exactly\" class=\"MsoNormal\"><span style=\"FONT-FAMILY: 黑体; FONT-SIZE: 15pt; mso-hansi-font-family: 黑体\">各部门：<span lang=\"EN-US\"><o:p></o:p></span></span></p><p style=\"LINE-HEIGHT: 23pt; TEXT-INDENT: 30pt; MARGIN: 0cm 0cm 0pt; LAYOUT-GRID-MODE: char; mso-line-height-rule: exactly; mso-char-indent-count: 2.0\" class=\"MsoNormal\"><span style=\"FONT-FAMILY: 黑体; FONT-SIZE: 15pt; mso-hansi-font-family: 黑体\">根据集团<span lang=\"EN-US\">2015</span>年国庆节放假的有关通知，结合公司实际情况，现安排如下：<span lang=\"EN-US\"><o:p></o:p></span></span></p><p style=\"LINE-HEIGHT: 23pt; TEXT-INDENT: 30pt; MARGIN: 0cm 0cm 0pt; LAYOUT-GRID-MODE: char; mso-line-height-rule: exactly; mso-char-indent-count: 2.0; mso-layout-grid-align: none\" class=\"MsoNormal\"><span style=\"FONT-FAMILY: 黑体; FONT-SIZE: 15pt; mso-hansi-font-family: 黑体\"><st1:chsdate w:st=\"on\" year=\"2013\" month=\"10\" day=\"1\" islunardate=\"False\" isrocdate=\"False\"><span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">1</span>日</st1:chsdate>至<st1:chsdate w:st=\"on\" year=\"2013\" month=\"10\" day=\"7\" islunardate=\"False\" isrocdate=\"False\"><span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">7</span>日</st1:chsdate>放假<span lang=\"EN-US\">7</span>天， <span lang=\"EN-US\">10</span>月<span lang=\"EN-US\">10</span>日（星期六）上班。</span></p><p style=\"LINE-HEIGHT: 23pt; TEXT-INDENT: 30pt; MARGIN: 0cm 0cm 0pt; LAYOUT-GRID-MODE: char; mso-line-height-rule: exactly; mso-char-indent-count: 2.0; mso-layout-grid-align: none\" class=\"MsoNormal\"><span style=\"FONT-FAMILY: 黑体; FONT-SIZE: 15pt; mso-hansi-font-family: 黑体\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 总经理办公室</span></p><p style=\"LINE-HEIGHT: 23pt; TEXT-INDENT: 30pt; MARGIN: 0cm 0cm 0pt; LAYOUT-GRID-MODE: char; mso-line-height-rule: exactly; mso-char-indent-count: 2.0; mso-layout-grid-align: none\" class=\"MsoNormal\"><span style=\"FONT-FAMILY: 黑体; FONT-SIZE: 15pt; mso-hansi-font-family: 黑体\">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 2015年9月25日<span lang=\"EN-US\"><o:p></o:p></span></span></p>";
		System.out.println(getTextFromHtml(str));
	}
}