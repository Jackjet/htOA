package com.kwchina.webmail.servlet;

import java.util.Enumeration;
import java.util.Properties;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;

import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.xml.XMLGenericModel;

/*
 * WebMailInit.java 
 */
public class WebMailInit extends WebMailServer implements InitializingBean, DisposableBean {	
	// ServletConfig srvlt_config;

	/** Size of the chunks that are sent. Must not be greater than 65536 */
	// private static final int chunk_size = 8192;protected
	protected String rootpath;
	
	protected String basepath;

	protected String imgbase;

	protected String authenticators;

	protected String language;

	protected String country;

	protected String storage;
	
	
	
	public void setRootpath(String rootpath) {
		this.rootpath = rootpath;
	}

	public void setBasepath(String basepath) {
		this.basepath = basepath;
	}

	public void setImgbase(String imgbase) {
		this.imgbase = imgbase;
	}

	public void setAuthenticators(String authenticators) {
		this.authenticators = authenticators;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public void setCountry(String country) {
		this.country = country;
	}
	
	public void setStorage(String storage) {
		this.storage = storage;
	}
	
	

	public String getBasePath() {
		return basepath;
	}

	public String getImageBasePath() {
		return imgbase;
	}
	
	
	public WebMailInit(){
		 //System.out.println("--------||　构造方法在调用..."); 	       
	     //System.out.println(this); 	   
	}
	
	
	/**
	public WebMailInit(String basepath,String imgbase,String authenticators,String language,String country,String storage) {
		this.setBasepath(basepath);
		this.setImgbase(imgbase);
		this.setAuthenticators(authenticators);
		this.setLanguage(language);
		this.setCountry(country);
		this.setStorage(storage);
		
		init();
	}
	*/ 
	
	public void afterPropertiesSet() throws Exception { 
        //System.out.println("--------||　afterPropertiesSet()正在调用...");        
        //System.out.println(this); 
        //System.out.println(this.basepath);
        
        init();
    }
    
	
	public void init () {
		//System.out.println("--------||　init()正在调用...");
		// 初始化
		System.err.println("Mail System Init");

		// srvlt_config = config;
		/*
		 * 如果没有配置 webmail.basepath 以及 webmail.imagebase，则提示
		 */
		if(rootpath == null){
			rootpath = "";
		}
		
		if (basepath == null) {
			// config.getServletContext().log(
			// "Warning: init param webmail.basepath should be set to the
			// WebMail Servlet's base path");
			basepath = "";
		}

		if (imgbase == null) {
			// config.getServletContext().log(
			// "Error: init param webmail.basepath should be set to the WebMail
			// Servlet's base path");
			imgbase = "";
		}
		
		//从配置中读取参数，存储在config(WebMailServer.config)中		
		this.config = new Properties();		
		this.config.put("webmail.rootpath", rootpath);
		this.config.put("webmail.basepath", basepath);
		this.config.put("webmail.imagebase", imgbase);
		this.config.put("webmail.authenticators", authenticators);
		this.config.put("webmail.default.locale.language", language);
		this.config.put("webmail.default.locale.country", country);
		this.config.put("webmail.storage", storage);
		
		
		/*
		 * 如果未在Servlet中配置如下参数，则设置为默认的路径 
		 * webmail.data.person: 个人邮件配置信息
		 * webmail.data.path 
		 * webmail.lib.path
		 * webmail.template.path 
		 * webmail.xml.path
		 */
		/**
		 * this.config.put("webmail.data.path",
		 * getServletContext().getRealPath("/mail/data"));
		 * this.config.put("webmail.lib.path",
		 * getServletContext().getRealPath("/mail/lib"));
		 * this.config.put("webmail.lib.path",
		 * getServletContext().getRealPath("/mail/lib"));
		 * this.config.put("webmail.template.path",
		 * getServletContext().getRealPath("/mail/lib/templates"));
		 * this.config.put("webmail.xml.path",
		 * getServletContext().getRealPath("/mail/lib/xml"));
		 */
		this.config.put("webmail.data.path", "/webmailhz/data");
		this.config.put("webmail.data.person", "/webmailhz/data/person");
		this.config.put("webmail.lib.path", "/webmailhz/lib");		
		this.config.put("webmail.template.path", "/webmailhz/lib/templates");
		this.config.put("webmail.xml.path", "/webmailhz/lib/xml");

		
		try {
			//Call the WebMailServer's initialization method
			doInit();
			
			//初始化(XMLGenericModel:SYSDATA/STATEDATA
			XMLGenericModel model = this.getStorage().createXMLGenericModel();
			
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
				ex.printStackTrace();
			}
			*/
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * public void debugOut(String msg, Exception ex) { if (getDebug()) {
	 * srvlt_config.getServletContext().log(msg, ex); } }
	 * 
	 * public ServletConfig getServletConfig() { return srvlt_config; }
	 * 
	 * public ServletContext getServletContext() { return
	 * srvlt_config.getServletContext(); }
	 */

	public String getServletInfo() {
		return getVersion()
				+ "\n(c)2009 by Huizhi company\nThis software is distributed @2009 (GPL)";
	}

	public void destroy() {
		shutdown();
	}

	/**
	 * Init possible servers of this main class
	 */
	protected void initServers() {
	}

	protected void shutdownServers() {
	}


	public Enumeration getServers() {
		return new Enumeration() {
			public boolean hasMoreElements() {
				return false;
			}

			public Object nextElement() {
				return null;
			}
		};
	}

	public Object getServer(String ID) {
		return null;
	}

	public void reinitServer(String ID) {
	}


}
