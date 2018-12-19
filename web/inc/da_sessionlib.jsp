<%@ page contentType="text/html; charset=utf-8"%>
<%@ page import = "java.net.*,java.util.*"%>

<%!
	void output_badpage_top(String loginname, javax.servlet.jsp.JspWriter output) {
		try {
		output.println("<script>");
		output.println("alert('user="+loginname+" 没有登录,系统自动返回登录页面');");
		output.println("top.window.location.href='/';");
		output.println("</script>");
		} catch ( java.io.IOException e) {}
	}

	void output_badpage_target(String loginname, javax.servlet.jsp.JspWriter output, String target) {
		try {
		output.println("<script>");
		output.println("alert('user="+loginname+" 没有登录,系统自动返回登录页面');");
		output.println(target+".location.href='/';");
		output.println("window.close()");
		output.println("</script>");
		} catch ( java.io.IOException e) {}
	}

	String getCookieValue(Cookie[] cookies, String cookieName, String defaultValue) {
	for(int i=0; i < cookies.length; i++) { 
	Cookie cookie = cookies[i]; 
	if (cookieName.equals(cookie.getName())) 
		try {
		return(java.net.URLDecoder.decode(cookie.getValue(),"ISO-8859-1")); 
		} catch( java.io.UnsupportedEncodingException e) {
		return null;
		}
	} 
	return(defaultValue); 
	} 

	Cookie getCookie(Cookie[] cookies, String cookieName) { 
	for(int i=0; i < cookies.length; i++) { 
	Cookie cookie = cookies[i]; 
	if (cookieName.equals(cookie.getName())) 
		return cookie; 
	} 
	return null; 
	} 

%>