package com.kwchina.webmail.storage;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FilenameFilter;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.Hashtable;

import org.dom4j.Document;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;

import com.kwchina.webmail.exception.CreateUserDataException;
import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.exception.UserDataException;
import com.kwchina.webmail.misc.ExpireableCache;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.server.WebMailVirtualDomain;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.xml.XMLCommon;
import com.kwchina.webmail.xml.XMLSystemData;
import com.kwchina.webmail.xml.XMLUserData;

/**
 * SimpleStorage.java 
 */

/**
 * This is the SimpleStorage class for the non-enterprise edition of WebMail. It
 * provides means of getting and storing data in ZIPFiles and ResourceBundles.
 * 
 * @see Storage
 */
public class SimpleStorage extends FileStorage {

	public static final String user_domain_separator = "|";

	protected Hashtable resources;

	protected Hashtable vdoms;

	protected ExpireableCache user_cache;

	protected int user_cache_size = 100;

	private static String dataXml;

	// 初始化SimpleStorage,获取webmail.xml中的配置信息
	public SimpleStorage(WebMailServer parent) {
		// 初始化系统信息
		super(parent);

		// 根据初始化信息，保存到XML文件
		saveXMLSysData();
	}

	// 从webmail.xml中获取配置信息
	protected void initConfig() {
		System.err.print("  * Configuration ... ");

		// 保存相关信息的webmail.xml
		dataXml = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + "webmail.xml";

		loadXMLSysData();

		System.err.println("  * successfully parsed XML configuration file.");
	}

	// 获取XMLSystemData，从webmail.xml中获取
	protected void loadXMLSysData() {
		// String datapath = parent.getProperty("webmail.data.path");
		// String file = datapath + System.getProperty("file.separator") +
		// "webmail.xml";
		// String
		// file=datapath+System.getProperty("file.separator")+"webmail.xml";
		// bug fixed by Christian Senet
		Document root;
		try {
			SAXReader reader = new SAXReader();
			root = reader.read(new File(dataXml));
			/**
			 * DocumentBuilder parser =
			 * DocumentBuilderFactory.newInstance().newDocumentBuilder(); root =
			 * parser.parse(file);
			 */
			if (debug)
				System.err.println("\n  * Configuration file parsed, document: " + root);

			sysdata = new XMLSystemData(root, cs);

			log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration loaded.");
		} catch (Exception ex) {
			log(Storage.LOG_ERR, "SimpleStorage: Failed to load WebMail configuration file. Reason: " + ex.getMessage());
			ex.printStackTrace();
			System.exit(0);
		}
	}

	protected void saveXMLSysData() {
		try {
			Document d = sysdata.getRoot();
			OutputStream cfg_out = new FileOutputStream(dataXml);

			// 把document的内容写入文件
			String sysDataDtd = parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "sysdata.dtd";
			XMLCommon.writeXML(d, cfg_out, sysDataDtd);

			cfg_out.flush();
			cfg_out.close();

			sysdata.setLoadTime(System.currentTimeMillis());

			log(Storage.LOG_DEBUG, "SimpleStorage: WebMail configuration saved.");
		} catch (Exception ex) {
			log(Storage.LOG_ERR, "SimpleStorage: Error while trying to save WebMail configuration (" + ex.getMessage() + ").");
			ex.printStackTrace();
		}
	}

	// 相关cache信息(这里指user_cache)
	protected void initCache() {
		// Initialize the file cache from FileStorage
		super.initCache();

		// Now initialize the user cache
		cs.configRegisterIntegerKey(this, "CACHE SIZE USER", "100", "Size of the user cache");
		try {
			// Default value 100, if parsing fails.
			user_cache_size = 100;
			user_cache_size = Integer.parseInt(getConfig("CACHE SIZE USER"));
		} catch (NumberFormatException e) {
		}
		if (user_cache == null) {
			user_cache = new ExpireableCache(user_cache_size);
		} else {
			user_cache.setCapacity(user_cache_size);
		}
	}

	public Enumeration getUsers(String domain) {
		String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain
				+ System.getProperty("file.separator");

		File f = new File(path);
		if (f.canRead() && f.isDirectory()) {
			final String[] files = f.list(new FilenameFilter() {
				public boolean accept(File file, String s) {
					if (s.endsWith(".xml")) {
						return true;
					} else {
						return false;
					}
				}
			});
			return new Enumeration() {
				int i = 0;

				public boolean hasMoreElements() {
					return i < files.length;
				}

				public Object nextElement() {
					int cur = i++;
					return files[cur].substring(0, files[cur].length() - 4);
				}
			};
		} else {
			log(Storage.LOG_WARN, "SimpleStorage: Could not list files in directory " + path);
			return new Enumeration() {
				public boolean hasMoreElements() {
					return false;
				}

				public Object nextElement() {
					return null;
				}
			};
		}
	}

	// 把用户相关信息，放入XMLUserData (用户第一次登录）
	public XMLUserData createUserData(String user, String domain, String password) throws CreateUserDataException {

		XMLUserData data;
		// 用户信息模板文件
		String template = parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "userdata.xml";

		File f = new File(template);
		if (!f.exists()) {
			log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") doesn't exist!");
			throw new CreateUserDataException("User configuration template (" + template + ") doesn't exist!", user, domain);
		} else if (!f.canRead()) {
			log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") is not readable!");
			throw new CreateUserDataException("User configuration template (" + template + ") is not readable!", user, domain);
		}

		Document root;
		try {
			/**
			 * DocumentBuilder parser = DocumentBuilderFactory.newInstance()
			 * .newDocumentBuilder(); // root = parser.parse("file://" +
			 * template); root = parser.parse(template);
			 */

			SAXReader reader = new SAXReader();
			root = reader.read(f);

			data = new XMLUserData(root);
			// 初始化用户信息
			data.init(user, domain, password);

			if (getConfig("SHOW ADVERTISEMENTS").toUpperCase().equals("YES")) {
				data.setSignature(user + "\n\n" + getConfig("ADVERTISEMENT MESSAGE"));
				/**
				 * if (domain.equals("")) { data.setSignature(user + "\n\n" +
				 * getConfig("ADVERTISEMENT MESSAGE")); } else {
				 * data.setSignature(user + "@" + domain + "\n\n" +
				 * getConfig("ADVERTISEMENT MESSAGE")); }
				 */
			} else {
				data.setSignature(user);
				/**
				 * if (domain.equals("")) { data.setSignature(user); } else {
				 * data.setSignature(user + "@" + domain); }
				 */
			}

			data.setTheme(parent.getDefaultTheme());

			WebMailVirtualDomain vdom = getVirtualDomain(domain);

			// XMLUserData 添加mailhost
			// data.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://"
			// + vdom.getDefaultServer(), user, password);
			data.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://" + vdom.getAuthenticationHost(), user, password);

		} catch (Exception ex) {
			log(Storage.LOG_WARN, "SimpleStorage: User configuration template (" + template + ") exists but could not be parsed");
			if (debug)
				ex.printStackTrace();
			throw new CreateUserDataException("User configuration template (" + template + ") exists but could not be parsed",user, domain);
		}

		// 把XMLUserData打印到文件
		/**
		 * try{ Writer out = new OutputStreamWriter(new
		 * FileOutputStream("c:\\test_1.xml"),"GBK"); OutputFormat format =
		 * OutputFormat.createPrettyPrint(); //指定XML编码
		 * format.setEncoding("GBK");
		 * 
		 * XMLWriter writer = new XMLWriter(out, format);
		 * writer.write(data.getRoot()); out.flush(); out.close();
		 * }catch(Exception ex){ }
		 */

		return data;
	}

	/**
	 * @see net.wastl.webmail.server.Storage.getUserData() 获取用户信息(XMLUserData)
	 */
	public XMLUserData getUserData(String xmlPath, String user, String domain, String password, boolean authenticate)
			throws UserDataException, InvalidPasswordException {
		// 处理登录名
		if (!(user.indexOf("@") > 0)) {
			user = user + "@" + domain;
		}

		if (authenticate) {
			// 需要判定用户是否正确
			System.out.println("---------");
			// System.out.println("---" + auth.toString() + "----");
			auth.authenticatePreUserData(user, domain, password);
		}

		if (user.equals("")) {
			return null;
		}

		String userCacheKey = user; // + user_domain_separator + domain;
		XMLUserData data = (XMLUserData) user_cache.get(userCacheKey);

		if (data == null) {
			/** @todo */
			user_cache.miss();

			boolean error = true;

			// 得到用户信息存储的文件 zhoulb.xml
			String filename = parent.getProperty("webmail.data.person") + System.getProperty("file.separator") + xmlPath + ".xml";
			File f = new File(filename);
			if (f.exists() && f.canRead()) {
				log(Storage.LOG_INFO, "SimpleStorage: Reading user configuration (" + f.getAbsolutePath() + ") for " + user);

				long t_start = System.currentTimeMillis();
				try {
					/**
					 * DocumentBuilder parser = DocumentBuilderFactory
					 * .newInstance().newDocumentBuilder(); Document root =
					 * parser.parse(new InputSource(new InputStreamReader(new
					 * FileInputStream(filename), "UTF-8")));
					 */

					SAXReader reader = new SAXReader();
					Document root = reader.read(f);

					// 创建XMLUserData，获取用户信息（包括用户个人信息，个人配置信息，以及通讯录等）
					data = new XMLUserData(root);
					
					//更新其用户名密码，对user,domain,password进行处理，防止出现"<"符号等；
					user = MailBasicService.transTagToCode(user);
					domain = MailBasicService.transTagToCode(domain);
					password = MailBasicService.transTagToCode(password);					
					data.setUserName(user);
					data.setDomain(domain);
					//data.setFullName(user);
					data.setEmail(user);
					try {
						data.setPassword(password, password);
					} catch (InvalidPasswordException ex) {
					}
					
					//更新其MailHost的数据,先去除，再添加
					data.removeMailHost("Default");				
					WebMailVirtualDomain vdom = getVirtualDomain(domain);				
					data.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://" + vdom.getAuthenticationHost(), user,
							password);

					if (debug)
						System.err.println("SimpleStorage: Parsed Document " + root);
					error = false;
				} catch (Exception ex) {
					log(Storage.LOG_WARN, "SimpleStorage: User configuration for " + user + " exists but could not be parsed ("
							+ ex.getMessage() + ")");
					if (debug)
						ex.printStackTrace();
					error = true;
				}

				long t_end = System.currentTimeMillis();
				log(Storage.LOG_DEBUG, "SimpleStorage: Parsing of XML userdata for " + user + ", domain " + domain + " took "
						+ (t_end - t_start) + "ms.");

				// 验证授权信息
				if (authenticate) {
					auth.authenticatePostUserData(data, domain, password);
				}				
			}

			if (error && !f.exists()) {
				// 如果用户信息文件不存在
				log(Storage.LOG_INFO, "UserConfig: Creating user configuration for " + user);
				// 创建该文件
				data = createUserData(user, domain, password);
				// if (authenticate) {
				// auth.authenticatePostUserData(data, domain, password);
				// }
			} 
			
			/**else {
				// 如果存在,则需要更新其用户名，密码等信息，防止用户更改密码后的登录，不能保存这些信息
				data.setUserName(MailBasicService.transTagToCode(user));
				// data.setPasswordData(password);
				if(password!="" && !password.equals("")){
					password = MailBasicService.transTagToCode(password);
					data.setPassword(password, password);
				}
				
				data.setDomain(MailBasicService.transTagToCode(domain));
			}
			error = false;

			if (error) {
				log(Storage.LOG_ERR, "UserConfig: Could not read userdata for " + user + "!");
				throw new UserDataException("Could not read userdata!", user, domain);
			}*/

			user_cache.put(user, data);
		} else {
			/** @todo ?? */
			user_cache.hit();			
		
			// if (authenticate) {
			// auth.authenticatePostUserData(data, domain, password);
			// }
		}

		return data;
	}

	// 保存用户数据
	public void saveUserData(String xmlPath, String user, String domain,String password) {
		try {
			// data\kwchina.com\路径
			// String path = parent.getProperty("webmail.data.path") +
			// System.getProperty("file.separator") + domain;
			String path = parent.getProperty("webmail.data.person");
			// String filename =
			// webmailServer.getProperty("webmail.data.person") +
			// System.getProperty("file.separator") + userName + ".xml";

			File p = new File(path);
			if ((p.exists() && p.isDirectory()) || p.mkdirs()) {
				File f = new File(path + System.getProperty("file.separator") + xmlPath + ".xml");
				if ((!f.exists() && p.canWrite()) || f.canWrite()) {

					XMLUserData userdata = getUserData(xmlPath, user, domain, "", false);
					
					//更新其MailHost的数据,先去除，再添加
					userdata.removeMailHost("Default");
					
					WebMailVirtualDomain vdom = getVirtualDomain(domain);				
					userdata.addMailHost("Default", getConfig("DEFAULT PROTOCOL") + "://" + vdom.getAuthenticationHost(), user,
							password);
					
					Document d = userdata.getRoot();

					long t_start = System.currentTimeMillis();

					FileOutputStream out = new FileOutputStream(f);
					// XMLCommon.writeXML(d,out,parent.getProperty("webmail.xml.path")+
					// System.getProperty("file.separator")+"userdata.dtd");

					XMLCommon.writeXML(d, out, parent.getProperty("webmail.xml.path") + System.getProperty("file.separator")
							+ "userdata.dtd");
					out.flush();
					out.close();

					long t_end = System.currentTimeMillis();
					log(Storage.LOG_DEBUG, "SimpleStorage: Serializing userdata for " + user + ", domain " + domain + " took "
							+ (t_end - t_start) + "ms.");
				} else {
					log(Storage.LOG_WARN, "SimpleStorage: Could not write userdata (" + f.getAbsolutePath() + ") for user "
							+ user);
				}
			} else {
				log(Storage.LOG_ERR, "SimpleStorage: Could not create path " + path + ". Aborting with user " + user);
			}
		} catch (Exception ex) {
			log(Storage.LOG_ERR, "SimpleStorage: Unexpected error while trying to save user configuration " + "for user " + user
					+ "(" + ex.getMessage() + ").");
			if (debug)
				ex.printStackTrace();
		}
	}

	/**
	 * Delete a WebMail user
	 * 
	 * @param user
	 *            Name of the user to delete
	 */
	public void deleteUserData(String xmlPath, String user, String domain) {
		//String path = parent.getProperty("webmail.data.path") + System.getProperty("file.separator") + domain
		//		+ System.getProperty("file.separator") + user + ".xml";
		
		String path = parent.getProperty("webmail.data.person") + System.getProperty("file.separator") + xmlPath + ".xml";
		
		File f = new File(path);
		if (!f.canWrite() || !f.delete()) {
			log(Storage.LOG_ERR, "SimpleStorage: Could not delete user " + user + " (" + path + ")!");
		} else {
			log(Storage.LOG_INFO, "SimpleStorage: Deleted user " + user + "!");
		}

		// user_cache.remove(user + user_domain_separator + domain);
		user_cache.remove(user);
	}
	
	//从Cache中去除
	public void removeUserData(String user){
		XMLUserData data = (XMLUserData) user_cache.get(user);
		if(data != null)
			user_cache.remove(user);
	}

	public String toString() {
		String s = "SimpleStorage:\n" + super.toString();
		s += " - user cache:  Capacity " + user_cache.getCapacity() + ", Usage " + user_cache.getUsage();
		s += ", " + user_cache.getHits() + " hits, " + user_cache.getMisses() + " misses\n";
		return s;
	}

}
