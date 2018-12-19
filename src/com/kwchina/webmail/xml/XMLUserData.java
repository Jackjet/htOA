package com.kwchina.webmail.xml;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;

import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.misc.Helper;
import com.kwchina.webmail.server.MailHostData;
import com.kwchina.webmail.server.UserData;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.util.MiscCommonMethod;
import com.kwchina.webmail.web.service.MailBasicService;

/**
 * XMLUserData.java
 *
 */

public class XMLUserData implements UserData {

	protected Document root;

	protected Element userdata;

	protected XMLAddressData address;

	protected boolean debug;

	protected long login_time;

	protected boolean logged_in;

	public XMLUserData() {

	}

	public XMLUserData(Document d) {
		debug = WebMailServer.getDebug();

		root = d;
		userdata = root.getRootElement();

		if (userdata == null) {
			System.err.println("UserData was null ???");

			userdata = root.addElement("USERDATA");
			root.add(userdata);
		}

		// 初始化通讯录信息
		address = new XMLAddressData(userdata);
	}

	public void init(String user, String domain, String password) {

		//对user,domain,password进行处理，防止出现"<"符号等；
		user = MailBasicService.transTagToCode(user);
		domain = MailBasicService.transTagToCode(domain);
		password = MailBasicService.transTagToCode(password);

		setUserName(user);
		setDomain(domain);
		setFullName(user);
		setEmail(user);
		/**
		 * if (domain.equals("")) { // This is a special case when the user
		 * already contains the domain // e.g. QMail setEmail(user); } else {
		 * setEmail(user + "@" + domain); }
		 */

		try {
			setPassword(password, password);
		} catch (InvalidPasswordException ex) {
		}

		/**
		 * Set user's locale to WebMailServer's default locale.
		 */
		// setPreferredLocale(Locale.getDefault().toString());
		// setPreferredLocale(WebMailServer.getDefaultLocale().toString());

		// setTheme(WebMailServer.getDefaultTheme());
		// 是否自動保存地址
		setBoolVar("auto save address", false);
		// 是否保存发送的邮件
		setBoolVar("save sent messages", false);
		// 删除时直接删除与否
		setBoolVar("direct delete message", true);
		// 第一次登陆时间
		setIntVar("first login", System.currentTimeMillis());
		// 最后一次登陆时间
		setIntVar("last login", System.currentTimeMillis());
		setIntVar("login count", 0);
		// 每页显示邮件数量(初始化为20)
		setIntVar("max show messages", 10);
		//
		setIntVar("icon size", 48);
		//
		setIntVar("max line length", 79);
	}

	public Document getRoot() {
		return root;
	}

	public Element getUserData() {
		return userdata;
	}

	/**
	 * public DocumentFragment getDocumentFragment() { DocumentFragment df =
	 * root.createDocumentFragment(); df.appendChild(userdata); return df; }
	 */

	// 相关参数及其值设定(<INTVAR name="login count" value="16"/>)
	protected void ensureElement(String tag, String attribute, String att_value) {
		List ls = userdata.elements(tag);
		boolean flag = false;

		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element e = (Element) it.next();
			if (attribute == null) {
				// No attribute required
				flag = true;
				break;
			} else if (att_value == null) {
				if (e.attribute(attribute) != null) {
					// Attribute exists, value is not requested
					flag = true;
					break;
				}
			} else if (e.attributeValue(attribute).equals(att_value)) {
				flag = true;
				break;
			}
		}

		if (!flag) {
			// root.addElement(tag);

			Element elem = userdata.addElement(tag);
			if (attribute != null) {
				// 为该element添加一个属性，并赋值
				elem.addAttribute(attribute, att_value == null ? "" : att_value);
			}

		}
	}

	// 设置该用户最后一次登录时间，以及总登录次数
	public void login() {
		// Increase login count and last login pointer
		// setIntVar("last login",System.currentTimeMillis());
		if (!logged_in) {
			setIntVar("login count", getIntVar("login count") + 1);

			login_time = System.currentTimeMillis();
			logged_in = true;
		} else {
			System.err.println("Err: Trying to log in a second time for user " + getLogin());
		}
	}

	// log out,set last login time
	public void logout() {
		if (logged_in) {
			setIntVar("last login", login_time);
			// Modified by exce, start
			logged_in = false;
			// Modified by exce, end
		} else {
			System.err.println("Err: Logging out a user that wasn't logged in.");
		}
	}

	// 添加MAILHOST
	public void addMailHost(String name, String host, String login, String password) {
		// 如果有同名的mailhost,则先删除
		try {
			// System.err.println("Adding mailhost "+name);
			if (getMailHost(name) != null) {
				removeMailHost(name);
			}

			Element mailhost = userdata.addElement("MAILHOST");

			mailhost.addAttribute("name", name);
			mailhost.addAttribute("id", Long.toHexString(Math.abs(name.hashCode()))
					+ Long.toHexString(System.currentTimeMillis()));

			Element mh_login = mailhost.addElement("MH_LOGIN");
			XMLCommon.setElementTextValue(mh_login, login);
			// mailhost.add(mh_login);

			Element mh_pass = mailhost.addElement("MH_PASSWORD");
			//XMLCommon.setElementTextValue(mh_pass, Helper.encryptTEA(password));
			XMLCommon.setElementTextValue(mh_pass, password);
			// mailhost.add(mh_pass);

			Element mh_uri = mailhost.addElement("MH_URI");
			XMLCommon.setElementTextValue(mh_uri, host);
			// mailhost.add(mh_uri);

			// userdata.add(mailhost);
			// System.err.println("Done mailhost "+name);
			// XMLCommon.writeXML(root,System.err,"");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void removeMailHost(String id) {
		Element n = XMLCommon.getElementByAttribute(userdata, "MAILHOST", "name", id);
		if (n != null) {
			userdata.remove(n);
		}
	}

	// 得到指定名称的MailHost
	public MailHostData getMailHost(String id) {
		final Element mailhost = XMLCommon.getElementByAttribute(userdata, "MAILHOST", "id", id);

		return new MailHostData() {

			public String getPassword() {
				//return Helper.decryptTEA(XMLCommon.getTagValue(mailhost, "MH_PASSWORD"));
				return XMLCommon.getTagValue(mailhost, "MH_PASSWORD");
			}

			public void setPassword(String s) {
				//XMLCommon.setTagValue(mailhost, "MH_PASSWORD", Helper.encryptTEA(s));
				XMLCommon.setTagValue(mailhost, "MH_PASSWORD", s);
			}

			public String getLogin() {
				return XMLCommon.getTagValue(mailhost, "MH_LOGIN");
			}

			public String getName() {
				return mailhost.attributeValue("name");
			}

			public void setLogin(String s) {
				XMLCommon.setTagValue(mailhost, "MH_LOGIN", s);
			}

			public void setName(String s) {
				mailhost.attributeValue("name", s);
			}

			public String getHostURL() {
				return XMLCommon.getTagValue(mailhost, "MH_URI");
			}

			public void setHostURL(String s) {
				XMLCommon.setTagValue(mailhost, "MH_URI", s);
			}

			public String getID() {
				return mailhost.attributeValue("id");
			}
		};
	}

	public Enumeration mailHosts() {
		final List nl = userdata.elements("MAILHOST");
		// final NodeList nl = userdata.getElementsByTagName("MAILHOST");

		/** @todo:Enumeration */
		return new Enumeration() {
			int i = 0;

			public boolean hasMoreElements() {
				return i < nl.size();
			}

			public Object nextElement() {
				Element e = (Element) nl.get(i++);
				return e.attributeValue("id");
			}
		};
	}

	// 每页邮件数量
	public int getMaxShowMessages() {
		int retval = (int) getIntVarWrapper("max show messages");
		return retval == 0 ? 20 : retval;
	}

	public void setMaxShowMessages(int i) {
		setIntVarWrapper("max show messages", i);
	}

	/**
	 * As of WebMail 0.7.0 this is different from the username, because it
	 * consists of the username and the domain.
	 *
	 * @see getUserName()
	 */
	public String getLogin() {
		String userName = getUserName();
		/** @todo change it */
		if (userName.indexOf("@") < 0) {
			userName += "@" + getDomain();
		}
		return userName;
	}

	// FullName
	public String getFullName() {
		return XMLCommon.getTagValue(userdata, "FULL_NAME");
	}

	public void setFullName(String s) {
		XMLCommon.setTagValue(userdata, "FULL_NAME", s);
	}

	// 个人签名
	public String getSignature() {

		return XMLCommon.getTagValue(userdata, "SIGNATURE");
	}

	public void setSignature(String s) {
		XMLCommon.setTagValue(userdata, "SIGNATURE", s, true);
	}

	// 个人Email地址
	public String getEmail() {
		return XMLCommon.getTagValue(userdata, "EMAIL");
	}

	public void setEmail(String s) {
		XMLCommon.setTagValue(userdata, "EMAIL", s);
	}

	//
	public Locale getPreferredLocale() {
		/**
		 * String loc = XMLCommon.getTagValue(userdata, "LOCALE");
		 * StringTokenizer t = new StringTokenizer(loc, "_"); String language =
		 * t.nextToken().toLowerCase(); String country = ""; if
		 * (t.hasMoreTokens()) { country = t.nextToken().toUpperCase(); }
		 *
		 * return new Locale(language, country);
		 */

		return Locale.getDefault();
	}

	public void setPreferredLocale(String newloc) {
		XMLCommon.setTagValue(userdata, "LOCALE", newloc);
	}

	public String getTheme() {
		String retval = XMLCommon.getTagValue(userdata, "THEME");
		if (retval.equals("")) {
			return WebMailServer.getDefaultTheme();
		} else {
			return retval;
		}
	}

	public void setTheme(String theme) {
		XMLCommon.setTagValue(userdata, "THEME", theme);
	}

	/**
	 * private String formatDate(long date) { TimeZone tz =
	 * TimeZone.getDefault(); /@TODO:getPreferredLocale 没有语言地区的设置/ //DateFormat
	 * df = DateFormat.getDateTimeInstance(DateFormat.LONG, //
	 * DateFormat.DEFAULT, getPreferredLocale()); DateFormat df =
	 * DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT);
	 * df.setTimeZone(tz); String now = df.format(new Date(date)); return now; }
	 */

	public String getFirstLogin() {
		long date = getIntVarWrapper("first login");
		return MiscCommonMethod.formatDate(date);
	}

	public String getLastLogin() {
		long date = getIntVarWrapper("last login");
		return MiscCommonMethod.formatDate(date);
	}

	public String getLoginCount() {
		return getIntVarWrapper("login count") + "";
	}

	public boolean checkPassword(String s) {
		String password = XMLCommon.getTagValue(userdata, "PASSWORD");
		if (password.startsWith(">")) {
			password = password.substring(1);
		}
		return password.equals(Helper.crypt(password, s));
	}

	public void setPassword(String newpasswd, String verify) throws InvalidPasswordException {
		if (newpasswd.equals(verify)) {
			/**
			 * Random r = new Random(); // Generate the crypted password; avoid
			 * problems with XML parsing String crypted = ">"; while
			 * (crypted.lastIndexOf(">") >= 0 || crypted.lastIndexOf("<") >= 0) { //
			 * This has to be some integer between 46 and 127 for the Helper //
			 * class String seed = (char) (r.nextInt(80) + 46) + "" + (char)
			 * (r.nextInt(80) + 46); System.err.println("Seed: " + seed);
			 * crypted = Helper.crypt(seed, newpasswd); }
			 * XMLCommon.setTagValue(userdata, "PASSWORD", crypted);
			 */

			XMLCommon.setTagValue(userdata, "PASSWORD", newpasswd);
		} else {
			throw new InvalidPasswordException("The passwords did not match!");
		}
	}

	public String getPassword() {
		return XMLCommon.getTagValue(userdata, "PASSWORD");
	}

	public void setPasswordData(String data) {
		XMLCommon.setTagValue(userdata, "PASSDATA", data);
	}

	public String getPasswordData() {
		return XMLCommon.getTagValue(userdata, "PASSDATA");
	}

	public int getMaxLineLength() {
		int retval = (int) getIntVarWrapper("max line length");
		return retval == 0 ? 79 : retval;
	}

	public void setMaxLineLength(int i) {
		setIntVarWrapper("max line length", i);
	}

	/**
	 * public boolean wantsBreakLines() { return getBoolVarWrapper("break
	 * lines"); }
	 *
	 * public void setBreakLines(boolean b) { setBoolVarWrapper("break lines",
	 * b); }
	 */

	public boolean wantsShowImages() {
		return getBoolVarWrapper("show images");
	}

	public void setShowImages(boolean b) {
		setBoolVarWrapper("show images", b);
	}

	public boolean wantsShowFancy() {
		return getBoolVarWrapper("show fancy");
	}

	public void setShowFancy(boolean b) {
		setBoolVarWrapper("show fancy", b);
	}

	// 邮件设定标志（删除/查看过/最新/已回复/草稿)
	public boolean wantsSetFlags() {
		return getBoolVarWrapper("set message flags");
	}

	public void setSetFlags(boolean b) {
		setBoolVarWrapper("set message flags", b);
	}

	public void setSaveSent(boolean b) {
		setBoolVarWrapper("save sent messages", b);
	}

	public boolean wantsSaveSent() {
		return getBoolVarWrapper("save sent messages");
	}

	public String getSentFolder() {
		return XMLCommon.getTagValue(userdata, "SENT_FOLDER");
	}

	public void setSentFolder(String s) {
		XMLCommon.setTagValue(userdata, "SENT_FOLDER", s);
	}

	public String getDomain() {
		return XMLCommon.getTagValue(userdata, "USER_DOMAIN");
	}

	public void setDomain(String s) {
		XMLCommon.setTagValue(userdata, "USER_DOMAIN", s);
	}

	/**
	 * Return the username without the domain (in contrast to getLogin()).
	 *
	 * @see getLogin()
	 */
	public String getUserName() {
		return XMLCommon.getTagValue(userdata, "LOGIN");
	}

	public void setUserName(String s) {
		XMLCommon.setTagValue(userdata, "LOGIN", s);
	}

	public void setIntVar(String var, long value) {
		setIntVarWrapper(var, value);
	}

	public long getIntVar(String var) {
		return getIntVarWrapper(var);
	}

	public void setBoolVar(String var, boolean value) {
		setBoolVarWrapper(var, value);
	}

	public boolean getBoolVar(String var) {
		return getBoolVarWrapper(var);
	}

	/**
	 * Wrapper method for setting all bool vars
	 */
	protected void setIntVarWrapper(String var, long value) {
		ensureElement("INTVAR", "name", var);

		Element e = XMLCommon.getElementByAttribute(userdata, "INTVAR", "name", var);
		Attribute attr = e.attribute("value");
		if (attr == null) {
			e.addAttribute("value", String.valueOf(value));
		} else {
			// e.attributeValue("value", value + "");
			attr.setValue(value + "");
		}

		if (debug)
			System.err.println("XMLUserData (" + getUserName() + "): Setting '" + var + "' to '" + value + "'");
		// System.err.println("XMLUserData (" + getUserName() + "@" +
		// getDomain() + "): Setting '" + var + "' to '" + value + "'");
	}

	protected long getIntVarWrapper(String var) {
		ensureElement("INTVAR", "name", var);
		Element e = XMLCommon.getElementByAttribute(userdata, "INTVAR", "name", var);
		long r = 0;
		try {
			r = Long.parseLong(e.attributeValue("value"));
		} catch (NumberFormatException ex) {
			System.err.println("Warning: Not a valid number in '" + var + "' for user " + getUserName() + "@"
					+ getDomain());
		}
		return r;
	}

	/**
	 * Wrapper method for setting all bool vars
	 */
	protected void setBoolVarWrapper(String var, boolean value) {
		ensureElement("BOOLVAR", "name", var);
		Element e = XMLCommon.getElementByAttribute(userdata, "BOOLVAR", "name", var);

		// 默认设置其value属性
		Attribute attr = e.attribute("value");
		if (attr == null) {
			e.addAttribute("value", value ? "yes" : "no");
		} else {
			// e.attributeValue("value", );
			attr.setValue(value ? "yes" : "no");
		}
		// e.attributeValue("value", value ? "yes" : "no");

		if (debug)
			System.err.println("XMLUserData (" + getUserName() + "): Setting '" + var + "' to '" + value + "'");
	}

	protected boolean getBoolVarWrapper(String var) {
		ensureElement("BOOLVAR", "name", var);

		Element e = XMLCommon.getElementByAttribute(userdata, "BOOLVAR", "name", var);

		boolean rValue = false;
		String value = e.attributeValue("value");
		if(value==null || value.equals("")){
			e.addAttribute("value", "no");
		}else{
			rValue = (value.toUpperCase().equals("YES") || value.toUpperCase().equals("TRUE"));
		}

		return rValue;
	}

	/**
	 * Set all boolvars to "false".
	 *
	 */
	public void resetBoolVars() {
		ArrayList ls = XMLCommon.getElementsByName(userdata, "BOOLVAR");
		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element elem = (Element) it.next();
			elem.attributeValue("value", "no");
		}

		/**
		 * NodeList nl = root.getElementsByTagName("BOOLVAR"); for (int i = 0; i <
		 * nl.getLength(); i++) { Element elem = (Element) nl.item(i);
		 * elem.setAttribute("value", "no"); }
		 */
	}

	// ----------------------如下为用户通讯录部分----------------------------------
	public XMLAddressData getAddress() {
		return address;
	}

}
