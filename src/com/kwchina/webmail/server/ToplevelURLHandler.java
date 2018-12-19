package com.kwchina.webmail.server;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;

import javax.servlet.ServletException;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.kwchina.webmail.authenticator.AuthDisplayMngr;
import com.kwchina.webmail.exception.DocumentNotFoundException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.storage.Storage;
import com.kwchina.webmail.ui.html.HTMLDocument;
import com.kwchina.webmail.ui.xml.XHTMLDocument;
import com.kwchina.webmail.xml.XMLGenericModel;

/*
 * ToplevelURLHandler.java  
 */

public class ToplevelURLHandler implements URLHandler {

	WebMailServer parent;

	// Hashtable urlhandlers;
	URLHandlerTree urlhandlers;

	public ToplevelURLHandler(WebMailServer parent) {
		System.err.println("- Initializing WebMail URL Handler ... done.");
		urlhandlers = new URLHandlerTree("/");
		urlhandlers.addHandler("/", this);
		this.parent = parent;
	}

	public void registerHandler(String url, URLHandler handler) {
		// urlhandlers.put(url,handler);
		urlhandlers.addHandler(url, handler);
		// System.err.println("Tree changed: "+urlhandlers.toString());
	}

	public String getURL() {

		return "/";

	}

	public String getName() {
		return "TopLevelURLHandler";
	}

	public String getDescription() {
		return "";
	}

	//错误处理页面
	public HTMLDocument handleException(Exception ex, HTTPSession session, HTTPRequestHeader header)
			throws ServletException {
		try {
			
			String basePath = parent.getProperty("webmail.template.path") + System.getProperty("file.separator");
			String stylePath = basePath;
			
			if(session!=null) {
				session.setException(ex);
				//String theme = parent.getDefaultTheme();
				//Locale locale = Locale.getDefault();
	
				//if (session instanceof WebMailSession) {
				//	WebMailSession sess = (WebMailSession) session;
					//theme = sess.getUser().getTheme();
					//locale = sess.getUser().getPreferredLocale();
				//}
				stylePath += "error.xsl";
				return new XHTMLDocument(session.getModel(), stylePath);
			}else{
				
				Document sysdoc = parent.getStorage().getSystemData().getRoot();
				stylePath = basePath + "timeout.xsl";	
				
				return new XHTMLDocument(sysdoc, stylePath);
			}		
			
			//return new XHTMLDocument(session.getModel(), parent.getStorage().getStylesheet("error.xsl", locale, theme));
		} catch (Exception myex) {
			parent.getStorage().log(Storage.LOG_ERR, "Error while handling exception:");
			parent.getStorage().log(Storage.LOG_ERR, myex);
			parent.getStorage().log(Storage.LOG_ERR, "The handled exception was:");
			parent.getStorage().log(Storage.LOG_ERR, ex);
			throw new ServletException(myex);
		}
	}

	// 根据相关URL，生成HTMLDocument对象
	public HTMLDocument handleURL(String url, HTTPSession session, HTTPRequestHeader header) throws WebMailException,
			ServletException {

		HTMLDocument content;

		if (url.equals("/")) {
			// content=new HTMLLoginScreen(parent,parent.getStorage(),false);
			XMLGenericModel model = parent.getStorage().createXMLGenericModel();

			AuthDisplayMngr adm = parent.getStorage().getAuthenticator().getAuthDisplayMngr();

			if (header.isContentSet("login")) {
				model.setStateVar("invalid password", "yes");
			}

			// Let the authenticator setup the loginscreen
			adm.setLoginScreenVars(model);

			/**
			 * Show login screen depending on WebMailServer's default locale.
			 */
			/*
			 * content = new XHTMLDocument(model.getRoot(),
			 * parent.getStorage().getStylesheet(adm.getLoginScreenFile(),
			 * Locale.getDefault(),"default"));
			 */
			/** 把XML输出查看 */
			/**	
			try {
				Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test.xml"), "GBK");
				OutputFormat format = OutputFormat.createPrettyPrint();
				// 指定XML编码
				format.setEncoding("GBK");

				XMLWriter writer = new XMLWriter(out, format);
				writer.write(model.getRoot());
				out.flush();
				out.close();
			} catch (Exception ex) {

			}
			*/

			String basePath = parent.getProperty("webmail.template.path") + System.getProperty("file.separator");
			String stylePath = basePath + adm.getLoginScreenFile();

			/**
			 * content = new XHTMLDocument(model.getRoot(), parent.getStorage()
			 * .getStylesheet(adm.getLoginScreenFile(),
			 * parent.getDefaultLocale(),
			 * parent.getProperty("webmail.default.theme")));
			 */
			content = new XHTMLDocument(model.getRoot(), stylePath);

		} else if (url.equals("/login")) {

			WebMailSession sess = (WebMailSession) session;
			UserData user = sess.getUser();

			/** 把XML输出查看 */
			/**
			try {
				Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"), "GBK");
				OutputFormat format = OutputFormat.createPrettyPrint();
				// 指定XML编码
				format.setEncoding("GBK");

				XMLWriter writer = new XMLWriter(out, format);
				writer.write(session.getModel());
				out.flush();
				out.close();
			} catch (Exception ex) {

			}*/

			String basePath = parent.getProperty("webmail.template.path") + System.getProperty("file.separator");
			String stylePath = basePath + "login.xsl";
			content = new XHTMLDocument(session.getModel(), stylePath);
		} else {
			/* Let the plugins handle it */

			URLHandler uh = urlhandlers.getHandler(url);

			if (uh != null && uh != this) {
				// System.err.println("Handler: "+uh.getName()+"
				// ("+uh.getURL()+")");
				String suburl = url.substring(uh.getURL().length(), url.length());
				content = uh.handleURL(suburl, session, header);
			} else {
				throw new DocumentNotFoundException(url + " was not found on this server");
			}
		}
		
		/**
		try {
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"), "GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			// 指定XML编码
			format.setEncoding("GBK");

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(session.getModel());
			out.flush();
			out.close();
		} catch (Exception ex) {

		}*/

		return content;
	}

}
