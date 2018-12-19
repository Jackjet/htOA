
package com.kwchina.webmail.server;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.net.InetAddress;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;
import java.util.Vector;

import javax.mail.Provider;
import javax.mail.Session;

import com.kwchina.webmail.config.ConfigScheme;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.storage.Storage;

/*
 * WebMailServer.java
 * */
/**
 * This is WebMails main server. From here most parts will be administered. 
 */

public abstract class WebMailServer {

	//protected ConnectionTimer timer;
	public ConnectionTimer timer;

	protected AuthenticatorHandler ahandler;

	//protected PluginHandler phandler;

	//protected ToplevelURLHandler uhandler;

	//protected Hashtable sessions;
	public Hashtable sessions;
	

	protected static boolean debug = true;

	public static final String VERSION = "@version@";

	protected static Provider[] possible_providers;

	protected static Provider[] store_providers;

	protected static Provider[] transport_providers;

	private long start_time;

	protected static Storage storage;

	protected ConfigScheme config_scheme;

	protected static WebMailServer server;

	protected Properties config;

	// Modified by exce, start.
	/**
	 * Webmail default locale setting.
	 */
	protected static Locale defaultLocale = null;

	// Modified by exce, end.

	protected static String defaultTheme = null;

	public WebMailServer() {
	}

	/**
	 * 如果调试模式，则把调试信息输出到STDERR. 
	 * @param msg  The message
	 */
	public static void debugOut(String msg) {
		if (getDebug()) {
			System.err.println("DBG: " + msg);
		}
	}

	/**
	 * 如果调试模式，把错误信息，以及Exception输出到 STDERR. 
	 * @param msg
	 *            The explanatory message
	 * @param ex
	 *            The exception
	 */
	public void debugOut(String msg, Exception ex) {
		if (getDebug()) {
			System.err.println("DBG: " + msg);
			ex.printStackTrace();
		}
	}

	public static boolean getDebug() {
		return debug;
	}

	public static void setDebug(boolean b) {
		debug = b;
	}

	protected void doInit() throws WebMailException {
		server = this;
		System.err.println("\n\nWebMail/Java Server v" + VERSION + " going up...");
		System.err.println("=====================================\n");
		System.err.println("Initalizing...");

		//检测jdk,操作系统,服务器用户等
		new SystemCheck(this);

		//各类配置信息，如Session,admin的password等
		initConfig();
		
		
		/**
		 * 初始化Locale(语言),目前仅支持中文
		 */
		/**
		if ((config.getProperty("webmail.default.locale.language") == null)
				|| (config.getProperty("webmail.default.locale.country") == null))
			defaultLocale = Locale.getDefault();
		else
			defaultLocale = new Locale(config
					.getProperty("webmail.default.locale.language"), config
					.getProperty("webmail.default.locale.country"));
		System.err.println("- Default Locale: "
				+ defaultLocale.getDisplayName());
		*/

		//界面主题
		/**
		if (config.getProperty("webmail.default.theme") == null) {
			defaultTheme = "bibop";
		} else {
			defaultTheme = config.getProperty("webmail.default.theme");
		}
		System.err.println("- Default Theme: " + defaultTheme);
		*/

		//把web.xml中配置的授权信息，存储到ConfigScheme，并且初始化AuthenticatorHandler对象
		ahandler = new AuthenticatorHandler(this);

		//获取webmail系统信息(webmail.xml)
		System.err.println("- Storage API ("
				+ System.getProperty("webmail.storage")
				+ ") and Configuration ... ");
		initStorage();
		storage.log(Storage.LOG_CRIT,"=============================== cut ===============================");
		storage.log(Storage.LOG_CRIT, "Storage initialized.");

		/** zlb:多线程 存儲用户信息 这里初始化ConnectionTimer对象*/
		timer = new ConnectionTimer();
		
		sessions = new Hashtable();
		
		System.err.println("  --done---!");

		/**
		//URL处理器(一个树状的URL集合)
		uhandler = new ToplevelURLHandler(this);
		storage.log(Storage.LOG_CRIT, "URLHandler initialized.");

		//所有URL及其处理器（plugin）对应关系
		phandler = new PluginHandler(this);
		storage.log(Storage.LOG_CRIT, "Plugins initialized.");
		*/
		
		//imap/pop3等协议
		initProviders();

		/**@todo:目前为空 */
		initServers();

		//使用默认的或者设置的参数，初始化配置文件XMLSystemData（Storage.initConfigKeys)	
		storage.initConfigKeys();

		storage
				.log(Storage.LOG_CRIT,
						"=============================== cut ===============================");
		storage.log(Storage.LOG_CRIT, "WebMail/Java Server " + VERSION
				+ " initialization completed.");
		
		System.err.println("- Webmail System Initalization complete.");
		
		start_time = System.currentTimeMillis();
	}

	
	//
	protected void initStorage() {
		/* Storage API */
		try {
			//web.xml中配置的storage信息：SimpleStorage
			Class storage_api = Class.forName(config.getProperty("webmail.storage"));

			Class[] tmp = new Class[1];
			tmp[0] = Class.forName("com.kwchina.webmail.server.WebMailServer");
			Constructor cons = storage_api.getConstructor(tmp);

			Object[] sargs = new Object[1];
			sargs[0] = this;

			/** zlb: new SimpleStorage */
			storage = (Storage) cons.newInstance(sargs);

		} catch (InvocationTargetException e) {
			Throwable t = e.getTargetException();
			System.err.println("Nested exception: ");
			t.printStackTrace();
			System.err
					.println("Fatal error. Could not initialize. Exiting now!");
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err
					.println("Fatal error. Could not initialize. Exiting now!");
			System.exit(1);
		}
	}

	
	//initConfig,存储Session时间，admin的password等
	protected void initConfig() {
		config_scheme = new ConfigScheme();
		config_scheme.configRegisterIntegerKey("SESSION TIMEOUT", "3600000",
						"Timeout in milliseconds after which a WebMailSession is closed automatically.");
		
		//config_scheme.configRegisterCryptedStringKey("ADMIN PASSWORD",
		//		"Secret", "Password for administrator connections. Shown encrypted, but enter"	+ " plain password to change.");
		
		config_scheme.configRegisterStringKey("ADMIN PASSWORD",
				"Secret", "Password for administrator connections. Shown encrypted, but enter"	+ " plain password to change.");
		
	}

	protected void initProviders() {
		possible_providers = Session.getDefaultInstance(System.getProperties(),
				null).getProviders();
		System.err.println("- Mail providers:");
		config_scheme.configRegisterChoiceKey("DEFAULT PROTOCOL","Protocol to be used as default");
		int p_transport = 0;
		int p_store = 0;
		for (int i = 0; i < possible_providers.length; i++) {
			System.err.println("  * " + possible_providers[i].getProtocol()
					+ " from " + possible_providers[i].getVendor());
			if (possible_providers[i].getType() == Provider.Type.STORE) {
				p_store++;
				config_scheme.configAddChoice("DEFAULT PROTOCOL",
						possible_providers[i].getProtocol(), "Use "
								+ possible_providers[i].getProtocol()
								+ " from " + possible_providers[i].getVendor());
				config_scheme.configRegisterYesNoKey("ENABLE "
						+ possible_providers[i].getProtocol().toUpperCase(),
						"Enable " + possible_providers[i].getProtocol()
								+ " from " + possible_providers[i].getVendor());
			} else {
				p_transport++;
			}
		}
		store_providers = new Provider[p_store];
		transport_providers = new Provider[p_transport];
		
		p_store = 0;
		p_transport = 0;
		for (int i = 0; i < possible_providers.length; i++) {
			if (possible_providers[i].getType() == Provider.Type.STORE) {
				store_providers[p_store] = possible_providers[i];
				p_store++;
			} else {
				transport_providers[p_transport] = possible_providers[i];
				p_transport++;
			}
		}
		/*
		 * We want to use IMAP as default, since this is the most useful
		 * protocol for WebMail
		 */
		config_scheme.setDefaultValue("DEFAULT PROTOCOL", "imap");
	}

	/**
	 * Init possible servers of this main class
	 */
	protected abstract void initServers();

	protected abstract void shutdownServers();

	public abstract Object getServer(String ID);

	public abstract Enumeration getServers();

	public String getBasePath() {
		return "";
	}

	public String getImageBasePath() {
		return "";
	}

	public abstract void reinitServer(String ID);

	public String getBaseURI(HTTPRequestHeader header) {
		String host = header.getHeader("Host");
		StringTokenizer tok = new StringTokenizer(host, ":");
		String hostname = tok.nextToken();
		int port = 80;
		if (tok.hasMoreElements()) {
			try {
				port = Integer.parseInt(tok.nextToken());
			} catch (NumberFormatException e) {
			}
		}
		int ssl_port = 443;
		try {
			ssl_port = Integer.parseInt(storage.getConfig("ssl port"));
		} catch (NumberFormatException e) {
		}
		int http_port = 80;
		try {
			http_port = Integer.parseInt(storage.getConfig("http port"));
		} catch (NumberFormatException e) {
		}
		String protocol = "http";
		if (port == ssl_port)
			protocol = "https";
		else if (port == http_port)
			protocol = "http";
		return protocol + "://" + host;
	}

	public Provider[] getStoreProviders() {
		Vector v = new Vector();
		for (int i = 0; i < store_providers.length; i++) {
			if (storage.getConfig(
					"ENABLE " + store_providers[i].getProtocol().toUpperCase())
					.equals("YES")) {
				v.addElement(store_providers[i]);
			}
		}
		Provider[] retval = new Provider[v.size()];
		v.copyInto(retval);
		return retval;
	}

	public Provider[] getTransportProviders() {
		return transport_providers;
	}

	public ConnectionTimer getConnectionTimer() {
		return timer;
	}

	public static Storage getStorage() {
		return storage;
	}

	/**
	public PluginHandler getPluginHandler() {
		return phandler;
	}

	public ToplevelURLHandler getURLHandler() {
		return uhandler;
	}*/
	
	public AuthenticatorHandler getAuthenticatorHandler() {
		return ahandler;
	}	

	public ConfigScheme getConfigScheme() {
		return config_scheme;
	}

	public String getProperty(String name) {
		return config.getProperty(name);
	}

	public static String getDefaultTheme() {
		return defaultTheme;
	}

	// Modified by exce, start
	/**
	 * Return default locale.
	 * 
	 * Related code: 1. login screen: server/TopLevelHandler.java line #110. 2.
	 * webmail.css: plugins/PassThroughPlugin.java line #77. 3. user's default
	 * locale setting: xml/XMLUserData.java line #82.
	 * 
	 * @return default locale.
	 */
	public static Locale getDefaultLocale() {
		return defaultLocale;
	}

	// Modified by exce, end

	public void setProperty(String name, String value) {
		config.put(name, value);
	}

	/**
	 * @deprecated Use StorageAPI instead
	 */
	public static String getConfig(String key) {
		return storage.getConfig(key);
	}

	public void restart() {
		System.err.println("Initiating shutdown for child processes:");
		Enumeration e = sessions.keys();
		System.err.print("- Removing active WebMail sessions ... ");
		while (e.hasMoreElements()) {
			HTTPSession w = (HTTPSession) sessions.get(e.nextElement());
			removeSession(w);
		}
		System.err.println("done!");
		shutdownServers();
		try {
			Thread.sleep(5000);
		} catch (Exception ex) {
		}
		storage.log(Storage.LOG_CRIT,
				"Shutdown completed successfully. Restarting.");
		storage.shutdown();
		System.err.println("Garbage collecting ...");
		System.gc();
		try {
			doInit();
		} catch (WebMailException ex) {
			ex.printStackTrace();
			System.exit(1);
		}
	}

	public void shutdown() {
		System.err.println("Initiating shutdown for child processes:");
		Enumeration e = sessions.keys();
		System.err.print("- Removing active WebMail sessions ... ");
		while (e.hasMoreElements()) {
			HTTPSession w = (HTTPSession) sessions.get(e.nextElement());
			removeSession(w);
		}
		System.err.println("done!");
		shutdownServers();
		storage.log(Storage.LOG_CRIT,
				"Shutdown completed successfully. Terminating.");
		storage.shutdown();
		System.err.println("Shutdown complete! Will return to console now.");
		System.exit(0);
	}

	public long getUptime() {
		return System.currentTimeMillis() - start_time;
	}

	public static String getVersion() {
		return "WebMail/Java v" + VERSION + ", built with JDK @java-version@";
	}

	public static String getCopyright() {
		return "(c)1999-@year@ Sebastian Schaffert and others";
	}

	public static WebMailServer getServer() {
		return server;
	}

	public static String generateMessageID(String user) {
		long time = System.currentTimeMillis();
		String msgid = Long.toHexString(time) + ".JavaWebMail." + VERSION + "."
				+ user;
		try {
			msgid += "@" + InetAddress.getLocalHost().getHostName();
		} catch (Exception ex) {
		}
		return msgid;
	}

	public void removeSession(HTTPSession w) {
		storage
				.log(Storage.LOG_INFO, "Removing session: "
						+ w.getSessionCode());
		//timer.removeTimeableConnection(w);
		sessions.remove(w.getSessionCode());
		
		if (!w.isLoggedOut()) {
			w.logout();
		}
	}

	public HTTPSession getSession(String key) {
		return (HTTPSession) sessions.get(key);
	}

	public Enumeration getSessions() {
		return sessions.keys();
	}
	

} // WebMailServer
