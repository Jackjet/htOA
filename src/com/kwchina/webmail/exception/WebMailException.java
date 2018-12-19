package com.kwchina.webmail.exception;

import java.io.*;

/*
 * WebMailException.java 
 */

public class WebMailException extends Exception {

	Exception nested;

	public WebMailException() {
		super();
	}

	public WebMailException(String s) {
		super(s);
	}

	public WebMailException(Exception ex) {
		super(ex.getMessage());
		nested = ex;
	}

	public void printStackTrace(PrintStream ps) {
		super.printStackTrace(ps);
		if (nested != null) {
			try {
				ps.println("==> Nested exception: ");
			} catch (Exception ex) {
			}
			nested.printStackTrace(ps);
		}
	}

	public void printStackTrace(PrintWriter ps) {
		super.printStackTrace(ps);
		if (nested != null) {
			try {
				ps.println("==> Nested exception: ");
			} catch (Exception ex) {
			}
			nested.printStackTrace(ps);
		}
	}

}
