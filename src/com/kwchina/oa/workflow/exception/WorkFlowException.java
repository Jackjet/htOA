package com.kwchina.oa.workflow.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class WorkFlowException extends Exception {
	Exception nested;

	public WorkFlowException() {
		super();
	}

	public WorkFlowException(String s) {
		super(s);
	}

	public WorkFlowException(Exception ex) {
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
