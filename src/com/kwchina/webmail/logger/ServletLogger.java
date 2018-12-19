package com.kwchina.webmail.logger;

import com.kwchina.webmail.config.ConfigurationListener;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.servlet.WebMailInit;
import com.kwchina.webmail.storage.Storage;

/**
 * ServletLogger.java
 */

public class ServletLogger implements Logger, ConfigurationListener {

	protected WebMailInit parent;

	protected Storage store;

	protected int loglevel;

	public ServletLogger(WebMailServer parent, Storage st)
			throws WebMailException {
		this.store = st;

		if (!(parent instanceof WebMailInit)) {
			throw new WebMailException(
					"ServletLogger can only be used by a Servlet!");
		} else {
			this.parent = (WebMailInit) parent;
		}

		// 设置log内容
		parent.getConfigScheme().configRegisterIntegerKey(this, "LOGLEVEL",
				"5", "How much debug output will be written in the logfile");

		// 从webmail.xml中获取log level
		initLog();
	}

	protected void initLog() {
		try {
			loglevel = Integer.parseInt(store.getConfig("LOGLEVEL"));
		} catch (NumberFormatException e) {
			loglevel = 5;
		}
	}

	public void notifyConfigurationChange(String key) {
		initLog();
	}

	public void log(int level, String message) {
		if (level <= loglevel) {
			String s = "LEVEL " + level;
			switch (level) {
			case Storage.LOG_DEBUG:
				s = "DEBUG   ";
				break;
			case Storage.LOG_INFO:
				s = "INFO    ";
				break;
			case Storage.LOG_WARN:
				s = "WARNING ";
				break;
			case Storage.LOG_ERR:
				s = "ERROR   ";
				break;
			case Storage.LOG_CRIT:
				s = "CRITICAL";
				break;
			}
			
			//parent.getServletContext().log(s + " - " + message);
		}
	}

	public void log(int level, Exception ex) {
		if (level <= loglevel) {
			String s = "LEVEL " + level;
			switch (level) {
			case Storage.LOG_DEBUG:
				s = "DEBUG   ";
				break;
			case Storage.LOG_INFO:
				s = "INFO    ";
				break;
			case Storage.LOG_WARN:
				s = "WARNING ";
				break;
			case Storage.LOG_ERR:
				s = "ERROR   ";
				break;
			case Storage.LOG_CRIT:
				s = "CRITICAL";
				break;
			}
			
			//parent.getServletContext().log(s + " - An exception occured", ex);
		}
	}

	public void shutdown() {
	}

}
