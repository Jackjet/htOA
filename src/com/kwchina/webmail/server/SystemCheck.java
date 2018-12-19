package com.kwchina.webmail.server;

import java.io.File;
import java.io.IOException;

import com.kwchina.webmail.exception.WebMailException;

/**
 * SystemCheck.java*  
 */


public class SystemCheck {

	public SystemCheck(WebMailServer parent) throws WebMailException {
		System.err.println("- Checking Java Virtual Machine ... ");
		System.err.print("  * Version: " + System.getProperty("java.version") + " ... ");

		/* 检测jdk版本 */
		if (System.getProperty("java.version").compareTo("1.4") >= 0) {
			System.err.println("ok.");
		} else {
			System.err.println("warn. At least Java 1.4 is required for WebMail.");
			//如果jdk版本不对，则退出系统
			System.exit(1);
		}

		/* 检测操作系统 */
		/**
		System.err.print("  * Operating System: "
				+ System.getProperty("os.name") + "/"
				+ System.getProperty("os.arch") + " "
				+ System.getProperty("os.version") + " ... ");
		if (System.getProperty("os.name").equals("SunOS")
				|| System.getProperty("os.name").equals("Solaris")
				|| System.getProperty("os.name").equals("Linux")) {
			System.err.println("ok.");
		} else {
			System.err
					.println("warning. WebMail was only tested\n   on Solaris, HP-UX and Linux and may cause problems on your platform.");
		}
		*/

		/* 检测是否以管理员身份登录服务器 */
		try {
			System.err.print("  * User name: "	+ System.getProperty("user.name") + " ... ");
			if (!System.getProperty("user.name").equals("root")) {
				System.err.println("ok.");
			} else {
				System.err
						.println("warning. You are running WebMail as root. This may be a potential security problem.");
			}
		} catch (Exception ex) {
			// Security restriction prohibit reading the username, then we do
			// not need to
			// check for root anyway
		}

		/* 检测webmail相关系统属性定义 */
		System.err.print("  * WebMail System Properties: ");
		// checkPathProperty(parent,"webmail.plugin.path");
		// checkPathProperty(parent,"webmail.auth.path");
		checkPathProperty(parent, "webmail.data.person");
		checkPathProperty(parent, "webmail.lib.path");
		checkPathProperty(parent, "webmail.template.path");
		checkPathProperty(parent, "webmail.data.path");
		checkPathProperty(parent, "webmail.xml.path");
		
		System.err.println("ok!");

		/*webmail.xml文件的格式验证 */
		/**
		System.err.print("  * Setting DTD-path in webmail.xml ... ");
		File f1 = new File(parent.getProperty("webmail.data.path")
				+ System.getProperty("file.separator") + "webmail.xml");

		File f2 = new File(parent.getProperty("webmail.data.path")
				+ System.getProperty("file.separator") + "webmail.xml."
				+ Long.toHexString(System.currentTimeMillis()));

		try {
			Pattern regexp = Pattern
					.compile("<!DOCTYPE SYSDATA SYSTEM \".*\">");

			BufferedReader file1 = new BufferedReader(new FileReader(f1));
			PrintWriter file2 = new PrintWriter(new FileWriter(f2));
			try {
				String line = file1.readLine();
				while (line != null) {
					Matcher m = regexp.matcher(line);
					
					 // String s = m.replaceAll("<!DOCTYPE SYSDATA SYSTEM
					 // \"file://"+ parent.getProperty("webmail.xml.path")+
					 // System.getProperty("file.separator")+
					 //"sysdata.dtd"+"\">");
					

					String s = m.replaceAll("<!DOCTYPE SYSDATA SYSTEM \""
							+ parent.getProperty("webmail.xml.path")
							+ System.getProperty("file.separator")
							+ "sysdata.dtd" + "\">");

					// String s=regexp.substituteAll(line,"<!DOCTYPE SYSDATA
					// SYSTEM \"file://"+
					// parent.getProperty("webmail.xml.path")+
					// System.getProperty("file.separator")+
					// "sysdata.dtd"+"\">");
					// System.err.println(s);
					file2.println(s);
					line = file1.readLine();
				}
			} catch (EOFException ex) {
			}
			file2.close();
			file1.close();
		} catch (Exception ex) {
			// ex.printStackTrace();
			// throw new WebMailException("Error parsing webmail.xml!");
			throw new WebMailException(ex);
		}
		
		f2.renameTo(f1);
		System.err.println("done!");
		*/
	}

	protected static void checkPathProperty(WebMailServer parent, String property) throws WebMailException {
		if (parent.getProperty(property) == null
				|| parent.getProperty(property).equals("")) {
			System.err.println("fatal error. " + property + " not defined.");
			throw new WebMailException();
		} 		
		else {
			String rootPath  = parent.getProperty("webmail.rootpath");
			String propertyValue = rootPath + parent.getProperty(property);
			
			parent.setProperty(property,propertyValue);
			
			/**
			try {				
				File f = new File(parent.getProperty(property));
				//在web.xml中一般配置为"../bin//webapps/webmail/lib",需要使用getCanonicalPath方法
				parent.setProperty(property, f.getCanonicalPath());								
			} catch (IOException ex) {
				throw new WebMailException(ex.getMessage());
			}*/
		}
	}

} 
