package com.kwchina.webmail.servlet;

import java.io.File;
import java.net.MalformedURLException;
import java.util.Enumeration;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.exception.UserDataException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.HTTPSession;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.web.action.MailDispatchAction;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.xml.XMLCommon;

public class WebMailLogin extends MailDispatchAction {

	/**
	 * 邮件用户登录 return: true成功，false失败 userName：存储用户邮件信息的部分文件名
	 */
	public boolean newUserSession(HttpServletRequest request, String userName, boolean newLogin) {
		boolean loginSuccess = false;

		// 页面参数
		HTTPRequestHeader http_header = this.getRequestHeader(request);
		http_header.setContent("_path", userName);

		// HTTPRequestHeader http_header = new HTTPRequestHeader();
		/**
		 * Enumeration en = request.getHeaderNames(); while
		 * (en.hasMoreElements()) { String s = (String) en.nextElement();
		 * http_header.setHeader(s, request.getHeader(s)); }
		 */

		try {
			if (!newLogin) {

				// 第一步:根据登陆用户名，获取用户的邮件帐户信息
				ServletContext context = request.getSession().getServletContext();// getServlet().getServletContext();
				WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
				WebMailInit webmailServer = (WebMailInit) webContext.getBean("webmailServer");

				// 存储用户信息的文件(userName.xml)
				String filename = webmailServer.getProperty("webmail.data.person") + System.getProperty("file.separator")
						+ userName + ".xml";
				File f = new File(filename);

				/**
				 * 如下获得邮件用户登录信息 如果第一次登录，从页面传递 否则，从文件中读取
				 */
				if (f.exists() && f.canRead()) {
					// 获取用户名，密码及domain
					SAXReader reader = new SAXReader();
					Document root = reader.read(f);
					Element userdata = root.getRootElement();

					String loginName = XMLCommon.getTagValue(userdata, "LOGIN"); // XMLCommon.getTagValue(userdata,
																					// "FULL_NAME");
					String password = XMLCommon.getTagValue(userdata, "PASSWORD");
					// password = Helper.decryptTEA(password);
					String domain = XMLCommon.getTagValue(userdata, "USER_DOMAIN");

					// 转换
					loginName = MailBasicService.transCodeToTag(loginName);
					password = MailBasicService.transCodeToTag(password);
					domain = MailBasicService.transCodeToTag(domain);

					// 把用户名，密码，域名放到HTTPRequestHeader
					http_header.setContent("username", loginName);
					http_header.setContent("password", password);
					http_header.setContent("domain", domain);
				} else {
					// 文件不存在，必须先设置相关邮件信息
				}
			}

			HTTPSession sess = null;
			sess = request.getSession(false) == null ? null : (HTTPSession) request.getSession(false).getAttribute(
					"webmail.session");
			if (sess == null) {
				// 普通用户登录
				try {
					sess = newSession(request, http_header);

					/** 把XML输出查看 */
					/**
					 * try { Writer out = new OutputStreamWriter(new
					 * FileOutputStream("c:\\test_1.xml"), "GBK"); OutputFormat
					 * format = OutputFormat.createPrettyPrint(); // 指定XML编码
					 * format.setEncoding("GBK");
					 * 
					 * XMLWriter writer = new XMLWriter(out, format);
					 * writer.write(sess.getModel()); out.flush(); out.close(); }
					 * catch (Exception ex) { }
					 */

					// 返回值
					loginSuccess = true;
				} catch (InvalidPasswordException ex) {
					// ex.printStackTrace();
					System.out.println(ex.toString());
				} catch (Exception ex) {
					// ex.printStackTrace();
					System.out.println(ex.toString());
				}
			}

		} catch (DocumentException ex) {
			//
			ex.printStackTrace();
		}
		/*
		 * catch (MalformedURLException ex) { // }
		 */

		return loginSuccess;
	}

	// 普通用户登录，构建Session
	public WebMailSession newSession(HttpServletRequest req, HTTPRequestHeader h) throws UserDataException,
			InvalidPasswordException, WebMailException {
		HttpSession sess = req.getSession(true);

		// 获取WemMailInit对象 (Servlet)
		// WebMailInit webmailServer =
		// (WebMailInit)sess.getServletContext().getServlet("WebMail");

		ServletContext context = this.getServlet().getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		WebMailInit webmailServer = (WebMailInit) webContext.getBean("webmailServer");

		if (sess.getAttribute("webmail.session") == null) {
			WebMailSession n = new WebMailSession(webmailServer, req, h);

			// 加入到ConnectionTimer Thread (超时断开)
			// webmailServer.timer.addTimeableConnection(n);

			n.login();

			sess.setAttribute("webmail.session", n);

			// 把WebMailSession放入sessions
			webmailServer.sessions.put(sess.getId(), n);

			webmailServer.debugOut("Created new Session: " + sess.getId());
			return n;
		} else {
			Object tmp = sess.getAttribute("webmail.session");
			if (tmp instanceof WebMailSession) {
				WebMailSession n = (WebMailSession) tmp;
				n.login();

				webmailServer.debugOut("Using old Session: " + sess.getId());
				return n;
			} else {
				/*
				 * 如果已有的session是管理员,则去除，创建新的session
				 */
				sess.setAttribute("webmail.session", null);
				webmailServer.debugOut("Reusing old AdminSession: " + sess.getId());
				return newSession(req, h);
			}
		}
	}
}
