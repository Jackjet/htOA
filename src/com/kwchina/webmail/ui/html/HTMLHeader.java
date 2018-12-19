package com.kwchina.webmail.ui.html;

/*
 * HTMLHeader.java
 * 
 */
/**
 * A HTML header used by HTMLDocument.
 * 
 */
public class HTMLHeader {

	private String title;

	public HTMLHeader(String title) {
		this.title = title;
	}

	public String toString() {
		String s = "<!-- HTML-Header created by webmail.ui.html package -->\n";
		s += "<HTML>\n";
		s += "  <HEAD>\n";
		s += "    <TITLE>" + title + "</TITLE>\n";
		s += "  </HEAD>\n";
		return s;
	}
}
