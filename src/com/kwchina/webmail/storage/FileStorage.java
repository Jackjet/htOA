package com.kwchina.webmail.storage;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.StringTokenizer;
import java.util.TimeZone;

import javax.xml.transform.Templates;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamSource;

import com.kwchina.webmail.authenticator.Authenticator;
import com.kwchina.webmail.config.ConfigurationListener;
import com.kwchina.webmail.exception.BinaryNotFoundException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.logger.FileLogger;
import com.kwchina.webmail.logger.Logger;
import com.kwchina.webmail.logger.ServletLogger;
import com.kwchina.webmail.misc.AttributedExpireableCache;
import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.misc.ExpireableCache;
import com.kwchina.webmail.server.WebMailServer;

/**
 * FileStorage.java 
 */

/**
 * 从ZIP以及资源中，存储或得到信息，例如HTML模板，二进制文件以及MIME类型
 */
public abstract class FileStorage extends Storage implements ConfigurationListener {

	protected Hashtable resources;

	protected Hashtable file_resources;

	protected Hashtable stylesheet_cache;

	protected Hashtable binary_cache;

	protected Authenticator auth;

	protected static Hashtable mime_types;

	protected Logger logger;

	protected static DateFormat df = null;

	private boolean init_complete = false;

	protected int file_cache_size = 40;

	/**
	 * 初始化，获取配置信息
	 */
	public FileStorage(WebMailServer parent) {
		super(parent);

		// loadXMLSysData
		initConfig();

		cs.addConfigurationListener("AUTH", this);
		cs.configRegisterStringKey(this, "MIME TYPES", parent.getProperty("webmail.lib.path")
				+ System.getProperty("file.separator") + "mime.types", "File with mime type information.");

		cs.configRegisterYesNoKey("SHOW ADVERTISEMENTS",
						"Whether or not to include the WebMail advertisement messages in default user signatures and HTTP response headers");

		cs.configRegisterStringKey("ADVERTISEMENT MESSAGE", "HZ JWebMail 1.0",	"");

		// 最大附件大小
		cs.configRegisterIntegerKey("MAX ATTACH SIZE", "1000000", "Maximum size of attachments in bytes");

		resources = new Hashtable();
		file_resources = new Hashtable();

		/** @todo :user_cache初始化，保存用户信息的cache */
		// ------------------
		initCache();

		// log初始化
		initLog();

		// Now included in configuration:
		// initVirtualDomains();

		/**
		 * Multipurpose Internet Email Extension
		 * MIME类型就是设定某种扩展名的文件用一种应用程序来打开的方式类型，当该扩展名文件被访问的时候，浏览器会自动使用指定应用程序来打开
		 * 从mime.types中读取
		 */
		initMIME();

		// 初始化授权方式，如IMAP/POP3等
		initAuth();

		// 语言信息
		// initLanguages();

		init_complete = true;
	}

	/**
	 * initialize XMLSystemData sysdata
	 */
	protected abstract void initConfig();

	// 相关cache信息（file_cache_size,stylesheet_cache,binary_cache,user_cache)
	protected void initCache() {
		// Initialize the file cache
		cs.configRegisterIntegerKey(this, "CACHE SIZE FILE", "40", "Size for the file system cache for every locale");

		try {
			file_cache_size = Integer.parseInt("CACHE SIZE FILE");
		} catch (NumberFormatException e) {
		}

		// Now the same for the stylesheet cache
		if (stylesheet_cache == null) {
			stylesheet_cache = new Hashtable(10);
		}

		Enumeration enum2 = stylesheet_cache.keys();
		while (enum2.hasMoreElements()) {
			ExpireableCache ec = (ExpireableCache) stylesheet_cache.get(enum2.nextElement());
			ec.setCapacity(file_cache_size);
		}

		// And for binary files
		if (binary_cache == null) {
			binary_cache = new Hashtable(10);
		}
		Enumeration enum3 = binary_cache.keys();
		while (enum3.hasMoreElements()) {
			ExpireableCache ec = (ExpireableCache) binary_cache.get(enum3.nextElement());
			ec.setCapacity(file_cache_size);
		}
	}

	protected void initLog() {
		if (false) {
			try {
				Class logger_class = Class.forName(parent.getProperty("webmail.log.facility"));
				Class[] argtypes = {Class.forName("net.wastl.webmail.server.WebMailServer"),
						Class.forName("net.wastl.webmail.server.Storage")};
				Object[] args = {parent, this};
				logger = (Logger) logger_class.getConstructor(argtypes).newInstance(args);
			} catch (Exception ex) {
				// Print a warning!
				logger = new FileLogger(parent, this);
			}
		} else {
			try {
				logger = new ServletLogger(parent, this);
			} catch (Exception ex) {
				// Print a warning!
				logger = new FileLogger(parent, this);
			}

		}
	}

	// 根据定义的授权类型(POP3/IMAP),初始化javax.mail.Session，javax.mail.Store
	protected void initAuth() {
		System.err.print("  * Authenticator ... ");
		Authenticator a = parent.getAuthenticatorHandler().getAuthenticator(getConfig("AUTH"));
		if (a != null) {
			// IMAP level authentication
			auth = a;
			auth.init(this);
			System.err.println("ok. Using " + auth.getClass().getName() + " (v" + auth.getVersion() + ") for authentication.");
		} else {
			System.err.println("error. Could not initalize any authenticator. Users will not be able to log on.");
			auth = null;
		}
	}

	protected void initMIME() {
		System.err.print("  * MIME types ... ");
		String config = getConfig("mime types");
		if (config != null) {
			try {
				File f = new File(config);
				if (f.exists() && f.canRead()) {
					mime_types = new Hashtable();
					BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(f)));
					String line = in.readLine();
					while (line != null) {
						if (!line.startsWith("#")) {
							StringTokenizer tok = new StringTokenizer(line);
							if (tok.hasMoreTokens()) {
								String type = tok.nextToken();
								while (tok.hasMoreTokens()) {
									String key = tok.nextToken();
									mime_types.put(key, type);
									// System.err.println(key+" -> "+type);
								}
							}
						}
						line = in.readLine();
					}
					in.close();
					System.err.println(" loaded from " + getConfig("mime types") + ".");
				} else {
					System.err.println(" could not find " + config + ". Will use standard MIME types.");
				}
			} catch (IOException ex) {
				System.err.println(" could not find " + getConfig("mime types") + ". Will use standard MIME types.");
			}
		} else {
			System.err.println(" not configured. Will use standard MIME types.");
		}
	}

	/**
	 * protected void initLanguages() { System.err.print(" * Available languages
	 * ... "); File f = new File(parent.getProperty("webmail.template.path") +
	 * System.getProperty("file.separator")); String[] flist = f.list(new
	 * FilenameFilter() { public boolean accept(File myf, String s) { if
	 * (myf.isDirectory() && s.equals(s.toLowerCase()) && (s.length() == 2 ||
	 * s.equals("default"))) { return true; } else { return false; } } });
	 * 
	 * File cached = new File(parent.getProperty("webmail.data.path") +
	 * System.getProperty("file.separator") + "locales.cache"); Locale[]
	 * available1 = null;
	 * 
	 * 
	 * Now we try to cache the Locale list since it takes really long to gather
	 * it!
	 * 
	 * boolean exists = cached.exists(); if (exists) { try { ObjectInputStream
	 * in = new ObjectInputStream( new FileInputStream(cached)); available1 =
	 * (Locale[]) in.readObject(); in.close(); System.err.print(" using disk
	 * cache ... "); } catch (Exception ex) { exists = false; } } if (!exists) { //
	 * We should cache this on disk since it is so slow! available1 =
	 * Collator.getAvailableLocales(); try { ObjectOutputStream os = new
	 * ObjectOutputStream( new FileOutputStream(cached));
	 * os.writeObject(available1); os.close(); } catch (Exception ex) {
	 * ex.printStackTrace(); } }
	 *  // Do this manually, as it is not JDK 1.1 compatible ... // Vector
	 * available=new Vector(Arrays.asList(available1)); Vector available = new
	 * Vector(available1.length); for (int i = 0; i < available1.length; i++) {
	 * available.addElement(available1[i]); } String s = ""; int count = 0; for
	 * (int i = 0; i < flist.length; i++) { String cur_lang = flist[i]; Locale
	 * loc = new Locale(cur_lang, "", ""); Enumeration enum1 =
	 * available.elements(); boolean added = false; while
	 * (enum1.hasMoreElements()) { Locale l = (Locale) enum1.nextElement(); if
	 * (l.getLanguage().equals(loc.getLanguage())) { s += l.toString() + " ";
	 * count++; added = true; } } if (!added) { s += loc.toString() + " ";
	 * count++; } } System.err.println(count + " languages initialized.");
	 * cs.configRegisterStringKey(this, "LANGUAGES", s, "Languages available in
	 * WebMail"); setConfig("LANGUAGES", s);
	 * 
	 * 
	 * Setup list of themes for each language
	 * 
	 * for (int j = 0; j < flist.length; j++) { File themes = new
	 * File(parent.getProperty("webmail.template.path") +
	 * System.getProperty("file.separator") + flist[j] +
	 * System.getProperty("file.separator")); String[] themelist =
	 * themes.list(new FilenameFilter() { public boolean accept(File myf, String
	 * s3) { if (myf.isDirectory() && !s3.equals("CVS")) { return true; } else {
	 * return false; } } }); String s2 = ""; for (int k = 0; k <
	 * themelist.length; k++) { s2 += themelist[k] + " "; }
	 * cs.configRegisterStringKey(this, "THEMES_" + flist[j].toUpperCase(), s2,
	 * "Themes for language " + flist[j]); setConfig("THEMES_" +
	 * flist[j].toUpperCase(), s2); } }
	 */

	/**
	 * Get the String for key and the specified locale.
	 * 
	 * @param key
	 *            Identifier for the String
	 * @param locale
	 *            locale of the String to fetch
	 */
	public String getStringResource(String key, Locale locale) {
		if (resources.get(locale.getLanguage()) != null) {
			String s = ((ResourceBundle) resources.get(locale.getLanguage())).getString(key);
			return ((ResourceBundle) resources.get(locale.getLanguage())).getString(key);
		} else {
			try {
				// Modified by exce, start.
				// ResourceBundle
				// rc=XMLResourceBundle.getBundle("resources",locale,null);
				/**
				 * System.err.println("Loading locale"); ResourceBundle rc =
				 * ResourceBundle.getBundle(
				 * "org.bulbul.webmail.xmlresource.Resources", locale); //
				 * Modified by exce, end. resources.put(locale.getLanguage(),
				 * rc); return rc.getString(key);
				 */
				return key;
			} catch (Exception e) {
				e.printStackTrace();
				return "";
			}
		}
	}

	/**
	 * Return the requested Stylesheet, precompiled and fitting to the locale
	 * and theme
	 */
	public Templates getStylesheet(String name, Locale locale, String theme) throws WebMailException {
		// stylesheet_cache中,保存根据语言和地区的Cache
		/**
		 * String key = locale.getLanguage() + "/" + theme;
		 * 
		 * AttributedExpireableCache cache = (AttributedExpireableCache)
		 * stylesheet_cache.get(key);
		 * 
		 * if (cache == null) { cache = new
		 * AttributedExpireableCache(file_cache_size); stylesheet_cache.put(key,
		 * cache); }
		 * 
		 * 
		 * 
		 * Templates stylesheet = null; String basepath = getBasePath(locale,
		 * theme);
		 * 
		 * File f = new File(basepath + name); if (!f.exists()) { throw new
		 * StylesheetNotFoundException("The requested stylesheet " + name + "
		 * could not be found (path tried: " + basepath + "."); }
		 * 
		 * 
		 * if (cache.get(name) != null && ((Long)
		 * cache.getAttributes(name)).longValue() >= f.lastModified()) { //
		 * 如果该模板没有被修改过 cache.hit(); return (Templates) cache.get(name); } else {
		 * try { StreamSource msg_xsl = new StreamSource(basepath + name);
		 * TransformerFactory factory = TransformerFactory.newInstance();
		 * 
		 * stylesheet = factory.newTemplates(msg_xsl);
		 * 
		 * //如果修改过，则需要重新放入cache cache.put(name, stylesheet, new
		 * Long(f.lastModified())); cache.miss(); } catch (Exception ex) { //
		 * System.err.println("Error while compiling stylesheet // "+name+",
		 * language="+locale.getLanguage()+", // theme="+theme+"."); throw new
		 * WebMailException("Error while compiling stylesheet " + name + ",
		 * language=" + locale.getLanguage() + ", theme=" + theme + ":\n" +
		 * ex.toString()); }
		 * 
		 * return stylesheet; }
		 */

		Templates stylesheet = null;
		try {
			// String basepath = getBasePath(locale, theme);
			String basepath = parent.getProperty("webmail.template.path") + System.getProperty("file.separator");

			StreamSource msg_xsl = new StreamSource(basepath + name);
			TransformerFactory factory = TransformerFactory.newInstance();

			stylesheet = factory.newTemplates(msg_xsl);

		} catch (Exception ex) {
			// System.err.println("Error while compiling stylesheet
			// "+name+", language="+locale.getLanguage()+",
			// theme="+theme+".");
			throw new WebMailException("Error while compiling stylesheet " + name + ", language=" + locale.getLanguage()
					+ ", theme=" + theme + ":\n" + ex.toString());
		}

		return stylesheet;
	}

	/**
	 * Get a binary file for the specified locale.
	 * 
	 * @param key
	 *            Identifier for the String
	 * @param locale
	 *            locale of the String to fetch
	 */
	public synchronized byte[] getBinaryFile(String name, Locale locale, String theme) throws BinaryNotFoundException {
		String key = locale.getLanguage() + "/" + theme;

		AttributedExpireableCache cache = (AttributedExpireableCache) binary_cache.get(key);

		if (cache == null) {
			cache = new AttributedExpireableCache(file_cache_size);
			binary_cache.put(key, cache);
		}

		ByteStore bs = null;

		String basepath = getBasePath(locale, theme);
		File f = new File(basepath + name);
		if (!f.exists()) {
			throw new BinaryNotFoundException("The file " + name + " could not be found!");
		}

		if (cache.get(name) != null && ((Long) cache.getAttributes(name)).longValue() >= f.lastModified()) {
			// Keep statistics :-)
			cache.hit();
			return ((ByteStore) cache.get(name)).getBytes();
		} else {
			try {
				bs = ByteStore.getBinaryFromIS(new FileInputStream(f), (int) f.length());
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			cache.put(name, bs, new Long(f.lastModified()));

			if (bs != null) {
				return bs.getBytes();
			} else {
				return new byte[1];
			}
		}
	}

	public Authenticator getAuthenticator() {
		return auth;
	}

	/**
	 * Send a message to the logging facility.
	 * 
	 * @param level
	 *            severity level of the message
	 * @param message
	 *            the message
	 */
	public synchronized void log(int level, String message) {
		if (logger != null) {
			logger.log(level, message);
		} else {
			System.err.println("LOG(" + level + "): " + message);
		}
	}

	/**
	 * Send a message to the logging facility.
	 * 
	 * @param level
	 *            severity level of the message
	 * @param message
	 *            the message
	 */
	public synchronized void log(int level, Exception ex) {
		if (logger != null) {
			logger.log(level, ex);
		} else {
			System.err.println("LOG(" + level + "): ");
			ex.printStackTrace();
		}
	}

	protected String formatDate(long date) {
		if (df == null) {
			TimeZone tz = TimeZone.getDefault();
			df = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.DEFAULT, Locale.getDefault());
			df.setTimeZone(tz);
		}
		String now = df.format(new Date(date));
		return now;
	}

	public void shutdown() {
		logger.shutdown();
	}

	public String getMimeType(String name) {
		if (mime_types == null) {
			return super.getMimeType(name);
		} else {
			if (name != null) {
				String type = "application/unknown";
				Enumeration enum1 = mime_types.keys();
				while (enum1.hasMoreElements()) {
					String s = (String) enum1.nextElement();
					if (name.toLowerCase().endsWith(s)) {
						type = (String) mime_types.get(s);
					}
				}
				return type;
			} else {
				return "UNKNOWN";
			}
		}
	}

	public void notifyConfigurationChange(String key) {
		log(Storage.LOG_DEBUG, "FileStorage: Configuration change notify for key " + key + ".");
		System.err.println("- Configuration changed: ");
		if (key.toUpperCase().startsWith("AUTH")) {
			initAuth();
		} else if (key.toUpperCase().startsWith("MIME")) {
			initMIME();
		}
	}

	public String toString() {
		String s = "";
		Enumeration enum1 = stylesheet_cache.keys();
		while (enum1.hasMoreElements()) {
			String name = (String) enum1.nextElement();
			ExpireableCache cache = (ExpireableCache) stylesheet_cache.get(name);
			s += " - stylesheet cache for " + name + ": Capacity " + cache.getCapacity() + ", Usage " + cache.getUsage();
			s += ", " + cache.getHits() + " hits, " + cache.getMisses() + " misses\n";
		}
		enum1 = binary_cache.keys();
		while (enum1.hasMoreElements()) {
			String name = (String) enum1.nextElement();
			ExpireableCache cache = (ExpireableCache) binary_cache.get(name);
			s += " - binary cache for " + name + ": Capacity " + cache.getCapacity() + ", Usage " + cache.getUsage();
			s += ", " + cache.getHits() + " hits, " + cache.getMisses() + " misses\n";
		}
		return s;
	}

}
