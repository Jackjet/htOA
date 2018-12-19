package com.kwchina.core.jsptag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

public class CharacterEncode extends BodyTagSupport {

	public CharacterEncode() {
	}

	public int doStartTag() {
		return 2;
	}

	public int doEndTag() {
		return 6;
	}

	public void setFrom(String fromValue) {
		setValue("from", fromValue);
	}

	public void setTo(String toValue) {
		setValue("to", toValue);
	}

	public int doAfterBody() {
		String bcString = getBodyContent().getString();
		try {
			String fromEncoding = (String) getValue("from");
			String toEncoding = (String) getValue("to");
			bcString = new String(bcString.getBytes(fromEncoding), toEncoding);
		} catch (UnsupportedEncodingException ex) {
		}
		JspWriter out = getBodyContent().getEnclosingWriter();
		try {
			out.print(bcString);
			out.flush();
		} catch (IOException e) {
			super.pageContext.getServletContext().log("[EXCEPTION] while outputing content...");
		}
		return 0;
	}
}