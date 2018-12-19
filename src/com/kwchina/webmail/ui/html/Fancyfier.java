package com.kwchina.webmail.ui.html;

import java.util.regex.*;

/*
 * Fancyfier.java
 */

/**
 * Do some fancifying with the messages. Also filters JavaScript.
 * 
 */
public class Fancyfier {

	public Fancyfier() {

	}

	private static Pattern[] regs = null;

	private static Pattern uri = null;

	private static String[] repls = { "<IMG SRC=\"/images/emoticon11.gif\">",
			"<IMG SRC=\"/images/emoticon12.gif\">",
			"<IMG SRC=\"/images/emoticon13.gif\">",
			"<IMG SRC=\"/images/emoticon14.gif\">",
			"<IMG SRC=\"/images/emoticon11.gif\">",
			"<IMG SRC=\"/images/emoticon12.gif\">",
			"<IMG SRC=\"/images/emoticon13.gif\">",
			"<IMG SRC=\"/images/emoticon14.gif\">",
			"<IMG SRC=\"/images/emoticon21.gif\">",
			"<IMG SRC=\"/images/emoticon22.gif\">",
			"<IMG SRC=\"/images/emoticon23.gif\">",
			"<IMG SRC=\"/images/emoticon24.gif\">",
			"<IMG SRC=\"/images/emoticon31.gif\">",
			"<IMG SRC=\"/images/emoticon32.gif\">",
			"<IMG SRC=\"/images/emoticon33.gif\">",
			"<IMG SRC=\"/images/emoticon34.gif\">",
			"<IMG SRC=\"/images/emoticon41.gif\">",
			"<IMG SRC=\"/images/emoticon42.gif\">",
			"<IMG SRC=\"/images/emoticon43.gif\">",
			"<IMG SRC=\"/images/emoticon44.gif\">",
			"<IMG SRC=\"/images/emoticon51.gif\">",
			"<IMG SRC=\"/images/emoticon52.gif\">", };

	public static void init() {
		try {
			// Smiley substitution
			String[] temp = { ":-\\)", ":-\\(", ":-O", ":\\)", ":\\(", ":O",
					":\\|", ";-\\)", ";-\\(", ";-O", ";-\\|", "B-\\)", "B-\\(",
					"B-O", "B-\\|", "%-\\)", "%-\\(", "%-O", "%-\\|", ":-X",
					"\\}:->" };
			regs = new Pattern[temp.length];
			for (int i = 0; i < temp.length; i++) {
				regs[i] = Pattern.compile(temp[i]);
			}
			// Link highlighting
			// uri=new
			// RE("http\\:\\/\\/(.+)(html|\\/)(\\S|\\-|\\+|\\.|\\\|\\:)");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String apply(String s) {
		if (regs == null) {
			init();
		}
		
		String retval = s;
		for (int i = 0; i < regs.length; i++) {
			Matcher m = regs[i].matcher(retval);
			retval = m.replaceAll(repls[i]);
			// retval=regs[i].substituteAll(retval,repls[i]);
		}
		return retval;
	}

} 
