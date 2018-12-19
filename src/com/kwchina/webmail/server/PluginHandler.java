package com.kwchina.webmail.server;

import java.io.File;
import java.io.FilenameFilter;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.util.Vector;

import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.misc.Queue;
import com.kwchina.webmail.storage.Storage;

/**
 * PluginHandler.java
 */

public class PluginHandler {

	WebMailServer parent;

	String plugin_list;

	Vector plugins;

	public PluginHandler(WebMailServer parent) throws WebMailException {
		this.parent = parent;
		this.plugin_list = parent.getProperty("webmail.plugins");
		if (plugin_list == null) {
			throw new WebMailException(
					"Error: No Plugins defined (Property webmail.plugins).");
		}
		plugins = new Vector();
		
		//登记
		registerPlugins();
	}

	/**
	 * Initialize and register WebMail Plugins.
	 */
	public void registerPlugins() {
		parent.getStorage().log(Storage.LOG_INFO,
				"Initializing WebMail Plugins ...");

		StringTokenizer tok = new StringTokenizer(plugin_list, ":;, ");
		Class plugin_class = null;
		try {
			plugin_class = Class.forName("com.huizhi.webmail.server.Plugin");
		} catch (ClassNotFoundException ex) {
			parent.getStorage().log(Storage.LOG_CRIT,
					"===> Could not find interface 'Plugin'!!");
			System.exit(1);
		}

		//Plugin Tree
		PluginDependencyTree pt = new PluginDependencyTree("");
		Queue q = new Queue();

		int count = 0;
		while (tok.hasMoreTokens()) {
			String name = (String) tok.nextToken();
			try {
				Class c = Class.forName(name);
				if (plugin_class.isAssignableFrom(c)) {
					Plugin p = (Plugin) c.newInstance();
					q.queue(p);
					
					plugins.addElement(p);
					// System.err.print(p.getName()+" ");
					// System.err.flush();
					count++;
				}
			} catch (Exception ex) {
				parent.getStorage().log(Storage.LOG_ERR,
						"could not register plugin \"" + name + "\"!");
				ex.printStackTrace();
			}
		}

		parent.getStorage().log(Storage.LOG_INFO,count + " plugins loaded correctly.");

		count = 0;
		while (!q.isEmpty()) {
			Plugin p = (Plugin) q.next();
			
			//如果该plugin没有加入到PluginDependenceTree，则需要先保留，知道其父加入其中
			if (!pt.addPlugin(p)) {
				q.queue(p);
			}
		}
		
		pt.register(parent);
		parent.getStorage().log(Storage.LOG_INFO,count + " plugins initialized.");
	};

	public Enumeration getPlugins() {
		return plugins.elements();
	}

	/**
	 * A filter to find WebMail Plugins.
	 */
	class FFilter implements FilenameFilter {
		FFilter() {
		}

		public boolean accept(File f, String s) {
			if (s.endsWith(".class")) {
				return true;
			} else {
				return false;
			}
		}
	}

} 
