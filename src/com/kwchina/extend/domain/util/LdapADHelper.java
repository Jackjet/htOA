package com.kwchina.extend.domain.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.Attribute;
import javax.naming.directory.Attributes;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

import org.aspectj.weaver.patterns.ThisOrTargetAnnotationPointcut;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
import com.sun.org.apache.xerces.internal.util.DraconianErrorHandler;

public class LdapADHelper {
	public LdapADHelper() {
	}

	private String host, url, adminName, adminPassword;
	private LdapContext ctx = null;
	
	public void opLdap(){
		String userName = "testadmin"; // 用户名称
		String password = "HTpassword1234"; // 密码
		String host = "192.168.61.1"; // AD服务器
		String port = "389"; // 端口
		String domain = "@haitongauto.com"; // 邮箱的后缀名
		String url = new String("ldap://" + host + ":" + port);
		String user = userName.indexOf(domain) > 0 ? userName : userName + domain;
		Hashtable env = new Hashtable();
		DirContext ctx;
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		env.put(Context.SECURITY_PRINCIPAL, user); // 不带邮箱后缀名的话，会报错，具体原因还未探究。高手可以解释分享。
		env.put(Context.SECURITY_CREDENTIALS, password);
		env.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
		env.put(Context.PROVIDER_URL, url);
		try {
			ctx = new InitialDirContext(env);
			ctx.close();
			System.out.println("验证成功！");
		} catch (NamingException err) {
			err.printStackTrace();
			System.out.println("验证失败！");
		}
	}

	/**
	 * 初始化ldap
	 */
	public void initLdap() {
		// ad服务器
		this.host = "192.168.61.1:389"; // AD服务器
		this.url = new String("ldap://" + host);// 默认端口为80的可以不用填写，其他端口需要填写，如ldap://xxx.com:8080
		this.adminName = "testadmin@haitongauto.com";// 注意用户名的写法：domain\User 或
											// User@domain.com
		this.adminPassword = "HTpassword1234";
		Hashtable HashEnv = new Hashtable();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
		HashEnv.put(Context.SECURITY_PRINCIPAL, adminName); // AD User
		HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword); // AD
																	// Password
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put(Context.PROVIDER_URL, url);
		try {
			ctx = new InitialLdapContext(HashEnv, null);
			System.out.println("初始化ldap成功！");
		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}
	}
	
	public static LdapContext getCtx(){
		LdapContext ctxUtil = null;
		// ad服务器
		String host = "192.168.61.1:389"; // AD服务器
		String url = new String("ldap://" + host);// 默认端口为80的可以不用填写，其他端口需要填写，如ldap://xxx.com:8080
		String adminName = "testadmin@haitongauto.com";// 注意用户名的写法：domain\User 或
											// User@domain.com
		String adminPassword = "HTpassword1234";
		Hashtable HashEnv = new Hashtable();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
		HashEnv.put(Context.SECURITY_PRINCIPAL, adminName); // AD User
		HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword); // AD
																	// Password
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put(Context.PROVIDER_URL, url);
		try {
			ctxUtil = new InitialLdapContext(HashEnv, null);
			System.out.println("初始化ldap成功！");
		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}
		
		return ctxUtil;
	}

	/**
	 * 关闭ldap
	 */
	public void closeLdap() {
		try {
			this.ctx.close();
		} catch (NamingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * 得到所有域用户
	 * OU=海通国际汽车物流有限公司
	 * OU=海通国际汽车码头有限公司
	 * OU=公司领导
	 * 不要了OU=客户&供应商
	 */
	public void getAllADInfo(String ouPath) {
		try {
			// LdapContext ctx = new InitialLdapContext(HashEnv, null);
			SearchControls searchCtls = new SearchControls(); // Create the search controls
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Specify the search scope

			String searchFilter = "objectClass=User"; // specify the LDAP search filter
			// searchFilter =
			// "objectClass=organizationalUnit";//当然organizationalUnit表示组织结构下的组织结构

			// 这里解释一下 OU DC CN的意思
			// 因为ldap访问的是树结构的数据库 所有既然是数据库 那就必须要有 数据库名称也就是域名 这里就是用DC表示
			// 比如域名是Test.bomb那么必须按照顺序来写 如下 DC=Test,DC=bomb
			// dc下面对应的创建文件夹就是一个组织结构 用OU来表示 OU必须DC的前面
			// CN表示组织结构下的用户名称 CN必须卸载OU的前面
			// 主义OU必须是完整的路径 比如hongan是最上面的 下面有个a组织 a下面有个b组织
			// 不能直接写OU=b,OU=a,DC=Test,DC=bomb
			// 必须要指明完整的要这样写OU=b,OU=a,OU=hongan，DC=Test,DC=bomb
			// 否则会报错

			String searchBase = "OU="+ouPath+",OU=HaiTong AUTO,DC=haitongauto,DC=com"; // Specify the Base for the search//搜索域节点

			int totalResults = 0;
			// String returnedAtts[] = {"memberOf"};//定制返回属性
			String returnedAtts[] = {"DisplayName", "container","url", "whenChanged", "employeeID",
					"name", "userPrincipalName", "physicalDeliveryOfficeName",
					"departmentNumber", "telephoneNumber", "homePhone",
					"mobile", "department", "sAMAccountName", "whenChanged",
					"mail" ,"description","userAccountControl"}; // 定制返回属性

			searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集

			// Search for objects using the filter
			NamingEnumeration answer = this.ctx.search(searchBase, searchFilter,searchCtls);

			int numbers = 0;
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				System.out.println("************************************************");
				System.out.println(sr.getName());
				
				if(sr.getName() != null && !sr.getName().equals("")){
					if(sr.getName().contains("OU=")){
						String oaDepartment = sr.getName().substring(sr.getName().lastIndexOf("OU=")+3,sr.getName().length());
						System.out.println("---"+oaDepartment+"---");
					}
				}

				Attributes Attrs = sr.getAttributes();
				if (Attrs != null) {
					try {
						for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
							Attribute Attr = (Attribute) ne.next();

							System.out.println(" AttributeID=" + Attr.getID().toString());

							// 读取属性值
							for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
								System.out.println("    AttributeValues=" + e.next().toString());
							}
							System.out.println("    ---------------");

//							// 读取属性值
//							Enumeration values = Attr.getAll();
//							if (values != null) { // 迭代
//								while (values.hasMoreElements()) {
//									System.out.println("    AttributeValues=" + values.nextElement());
//								}
//							}
//							System.out.println("    ---------------");
						}
					} catch (NamingException e) {
						System.err.println("Throw Exception : " + e);
					}
				}
				numbers += 1;
			}
			System.out.println("TotalResults: " + totalResults);
			System.out.println("Number: " + totalResults);
			this.ctx.close();
		}

		catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}

	}
	
	
	/**
	 * 同步所有域用户
	 * OU=海通国际汽车物流有限公司
	 * OU=海通国际汽车码头有限公司
	 * OU=公司领导  不要了
	 * OU=客户&供应商
	 */
	public List<Map<String, String>> getSyncADUserInfo(String ouPath) {
		List<Map<String, String>> returnList = new ArrayList<Map<String,String>>();
		LdapContext ctxUtil = getCtx();
		try {
			SearchControls searchCtls = new SearchControls(); // Create the search controls
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Specify the search scope

			String searchFilter = "objectClass=User"; // specify the LDAP search filter
			String searchBase = "OU="+ouPath+",OU=HaiTong AUTO,DC=haitongauto,DC=com"; // Specify the Base for the search//搜索域节点

			int totalResults = 0;
			String returnedAtts[] = {"DisplayName", "container","url", "whenChanged", "employeeID",
					"name", "userPrincipalName", "physicalDeliveryOfficeName",
					"departmentNumber", "telephoneNumber", "homePhone",
					"mobile", "department", "sAMAccountName", "whenChanged",
					"mail" ,"description","userAccountControl"}; // 定制返回属性
			searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集

			// Search for objects using the filter
			NamingEnumeration answer = ctxUtil.search(searchBase, searchFilter,searchCtls);

			int numbers = 0;
			while (answer.hasMoreElements()) {
				SearchResult sr = (SearchResult) answer.next();
				System.out.println("************************************************");
				System.out.println(sr.getName());
				
				//取到的用户名、姓名、邮件、部门
				//String displayName = "";   
				//定为当mail不为空时，则同步为OA用户
				String oaPersonName = "";
				String oaUserName = "";
				String oaEmail = "";
				String oaDepartment = "公司领导";
				boolean isValid = true;
				if(sr.getName() != null && !sr.getName().equals("")){
					if(sr.getName().contains("OU=")){
						oaDepartment = sr.getName().substring(sr.getName().lastIndexOf("OU=")+3,sr.getName().length()).trim();
					}
				}

				Attributes Attrs = sr.getAttributes();
				if (Attrs != null) {
					try {
						for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
							Attribute Attr = (Attribute) ne.next();

							//姓名
							if("name".equals(Attr.getID())){
								for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
									oaPersonName = e.next().toString();
								}
							}
							//用户名
							if("sAMAccountName".equals(Attr.getID())){
								for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
									oaUserName = e.next().toString();
//									System.out.println("oaUserName=============" + oaUserName);
								}
							}
							//邮件
							if("mail".equals(Attr.getID())){
								for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
									oaEmail = e.next().toString();
								}
							}
							
							//"userAccountControl"  为66050时，是禁用状态
							if("userAccountControl".equals(Attr.getID())){
								for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
									String tmpControl = e.next().toString();
									System.out.println("control=============" + tmpControl);
									if(tmpControl != null && !tmpControl.equals("")){
										if(tmpControl.equals("66050") || tmpControl.equals("514")){
											isValid = false;
										}
									}
								}
							}

						}
					} catch (NamingException e) {
						System.err.println("Throw Exception : " + e);
					}
				}
				numbers += 1;
				//如果displayName不为空时，则同步，其它不同步
				if(oaEmail != null && !oaEmail.equals("")){
					Map<String, String> personMap = new HashMap<String, String>();
					personMap.put("userName", oaUserName.trim());
					personMap.put("personName", oaPersonName.trim());
					personMap.put("email", oaEmail.trim());
					personMap.put("department", oaDepartment.trim());
					personMap.put("isValid", isValid ? "true" : "false");
					returnList.add(personMap);
				}
				
			}
			System.out.println("TotalResults: " + totalResults);
			System.out.println("Number: " + totalResults);
			ctxUtil.close();
		}

		catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}

		return returnList;
	}
	

	/**
	 * 根据条件得到信息
	 * @param type
	 *            organizationalUnit:组织架构 group：用户组 user|person：用户
	 * @param name
	 * @return
	 */
	public String GetADInfo(String type, String filter, String name) {

		String userName = name; // 用户名称
		if (userName == null) {
			userName = "";
		}
		String company = "";
		String result = "";
		try {
			// 域节点
			String searchBase = "DC=haitong,DC=com";
			// LDAP搜索过滤器类
			// cn=*name*模糊查询 cn=name 精确查询
			// String searchFilter = "(objectClass="+type+")";
			String searchFilter = "(&(objectClass=" + type + ")(" + filter + "=*" + name + "*))";
			// 创建搜索控制器
			SearchControls searchCtls = new SearchControls();
			// 设置搜索范围
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE);
			// String returnedAtts[] = { "memberOf" }; // 定制返回属性
			// searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集
			// 不设置则返回所有属性
			// 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
			NamingEnumeration answer = ctx.search(searchBase, searchFilter,searchCtls);// Search for objects using the filter
			// 初始化搜索结果数为0
			int totalResults = 0;// Specify the attributes to return
			int rows = 0;
			while (answer.hasMoreElements()) {// 遍历结果集
				SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
				++rows;
				String dn = sr.getName();
				System.out.println(dn);
				Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集
				if (Attrs != null) {
					try {
						for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
							Attribute Attr = (Attribute) ne.next();// 得到下一个属性
							System.out.println(" AttributeID=属性名：" + Attr.getID().toString());
							// 读取属性值
							for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
								company = e.next().toString();
								System.out.println("    AttributeValues=属性值：" + company);
							}
							System.out.println("    ---------------");

						}
					} catch (NamingException e) {
						System.err.println("Throw Exception : " + e);
					}
				}// if
			}// while
			System.out.println("************************************************");
			System.out.println("Number: " + totalResults);
			System.out.println("总共用户数：" + rows);
		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}
		return result;
	}
	
	/**
	 * 将明文密码跟ldap中的用户密码进行匹配判断
	 * @param ldappw
	 * @param inputpw
	 * @return
	 * @throws NoSuchAlgorithmException
	 */
	public static boolean verifySHA(String ldappw, String inputpw) throws NoSuchAlgorithmException {

		// MessageDigest 提供了消息摘要算法，如 MD5 或 SHA，的功能，这里LDAP使用的是SHA-1
		MessageDigest md = MessageDigest.getInstance("SHA-1");
		
		// 取出加密字符
		if (ldappw.startsWith("{SSHA}")) {
		    ldappw = ldappw.substring(6);
		} else if (ldappw.startsWith("{SHA}")) {
		    ldappw = ldappw.substring(5);
		}
		
		// 解码BASE64
		byte[] ldappwbyte = Base64.decode(ldappw);
		byte[] shacode;
		byte[] salt;
		
		// 前20位是SHA-1加密段，20位后是最初加密时的随机明文
		if (ldappwbyte.length <= 20) {
		    shacode = ldappwbyte;
		    salt = new byte[0];
		} else {
		    shacode = new byte[20];
		    salt = new byte[ldappwbyte.length - 20];
		    System.arraycopy(ldappwbyte, 0, shacode, 0, 20);
		    System.arraycopy(ldappwbyte, 20, salt, 0, salt.length);
		}
		
		// 把用户输入的密码添加到摘要计算信息
		md.update(inputpw.getBytes());
		// 把随机明文添加到摘要计算信息
		md.update(salt);
		
		// 按SSHA把当前用户密码进行计算
		byte[] inputpwbyte = md.digest();
		
		// 返回校验结果
		return MessageDigest.isEqual(shacode, inputpwbyte);
	}
	
	
	/*************以下得到当前登录域用户相关方法*************/
	public static String getFormatOU(String ou) {
		String[] splt = ou.split(",");
		String realFormat = "";
		for (int i = splt.length - 1; i >= 0; i--) {
			realFormat = realFormat + "OU=" + splt[i] + ",";
		}
		if (',' == realFormat.charAt(realFormat.length() - 1)) {
			realFormat = realFormat.substring(0, realFormat.length() - 1);
		}
		return realFormat;
	}
		 
	public static String getFormatDoamin(String domainName) {
	String[] splt = domainName.split("//.");
	String realFormat = "";
	for (int i = 0; i < splt.length; i++) {
		if (!"".equals(splt[i]))
			realFormat += "DC=" + splt[i] + ",";
		}
		if (',' == realFormat.charAt(realFormat.length() - 1)) {
			realFormat = realFormat.substring(0, realFormat.length() - 1);
		}
		return realFormat;
	}
		 
	public String GetRemoteDomainUser(LdapContext ctx, String ou, String domainName) throws NamingException {
		//ctx = this.ctx;
		String xml = "";
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		String searchFilter = "objectClass=User";
		String searchBase = "";
		searchBase = ou + "," + getFormatDoamin(domainName);
		String returnedAtts[] = { "name", "telephoneNumber", "mobile", "mail" };
		searchCtls.setReturningAttributes(returnedAtts);
		 
		NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
		 
		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult) answer.next();
			int oulenth = 0;
			Attributes Attrs = sr.getAttributes();
			if (Attrs != null) {
				try {
					xml += "<User ";
					for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
						Attribute Attr = (Attribute) ne.next();
		 
						if ("name".equals(Attr.getID())) {
							xml += "name=";
						}
						if ("telephoneNumber".equals(Attr.getID())) {
							xml += "tel=";
						}
						if ("mobile".equals(Attr.getID())) {
							xml += "mobile=";
						}
						if ("mail".equals(Attr.getID())) {
							xml += "email=";
						}
						Enumeration values = Attr.getAll();
						if (values != null) {
							while (values.hasMoreElements()) {
								xml += "\"" + values.nextElement() + "\" ";
								oulenth = oulenth + 1;
							}
						}
					}
					xml += "/>";
				} catch (NamingException e) {
					System.err.println("Throw Exception : " + e);
				}
			}
		}
		return xml;
	}
		 
	public String GetRemoteDomainGroupDie(LdapContext ctx, String ou, String domainName) throws NamingException{
		String xml="";
		SearchControls searchCtls = new SearchControls();
		searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
		String searchFilter = "objectClass=organizationalUnit";
		String searchBase = "";
		searchBase =ou + "," + getFormatDoamin(domainName);
		String returnedAtts[] = { "name" };
		 
		searchCtls.setReturningAttributes(returnedAtts);
		 
		NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
		while (answer.hasMoreElements()) {
			SearchResult sr = (SearchResult) answer.next();
			String ouName = sr.getName();
			//System.out.println(ouName);
			if (ouName != null && !"".equals(ouName)) {
				Attributes Attrs = sr.getAttributes();
				if (Attrs != null) {
					try {
						for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
							Attribute Attr = (Attribute) ne.next();
							if ("name".equals(Attr.getID())) {
								Enumeration values = Attr.getAll();
								if (values != null) { // 迭代
									while (values.hasMoreElements()) {
										String v=(String)values.nextElement();
										xml+="<Group name=\""+v+"\">";
										xml += GetRemoteDomainUser(ctx, "OU="+v+","+ou, domainName);
										xml+=GetRemoteDomainGroupDie(ctx,"OU="+v+","+ou,domainName);
										xml+="</Group>";
									}
								}
		 
							}
		 
						}
					} catch (NamingException e) {
						e.printStackTrace();
					}
				}
			}else{
				xml += GetRemoteDomainUser(ctx,
						getFormatOU(ou), domainName);
			}
		}
		return xml;
	}
	
	
	public String GetRemoteDomainGroup(String ip, String port, String adminName, String adminPassword, String domainName, String ou) {
		String xml = "<?xml version=\"1.0\" encoding=\"gbk\" ?>";
		Hashtable<String, String> HashEnv = new Hashtable<String, String>();
		String rport = port;
		if (port == null || "".equals(port))
			rport = "389";
			String LDAP_URL = "ldap://" + ip + ":" + rport;
			adminName = adminName + "@" + domainName;
			HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple");
			HashEnv.put(Context.SECURITY_PRINCIPAL, adminName);
			HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword);
			// Password
			HashEnv.put(Context.INITIAL_CONTEXT_FACTORY, "com.sun.jndi.ldap.LdapCtxFactory");
			HashEnv.put(Context.PROVIDER_URL, LDAP_URL);
		 
			try {
				LdapContext ctx = new InitialLdapContext(HashEnv, null);
				SearchControls searchCtls = new SearchControls();
				searchCtls.setSearchScope(SearchControls.ONELEVEL_SCOPE);
				String searchFilter = "objectClass=organizationalUnit";
				String searchBase = "";
				searchBase = getFormatOU(ou) + "," + getFormatDoamin(domainName);
				String returnedAtts[] = { "name" };
		 
				searchCtls.setReturningAttributes(returnedAtts);
		 
				NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);
				int oulenth = 0;
				String[] splt = ou.split(",");
				for (int j = 0; j < splt.length; j++) {
					if (!"".equals(splt[j])) {
						xml = xml + "<Group name=\"" + splt[j] + "\">";
						oulenth = oulenth + 1;
					}
				}
				xml += GetRemoteDomainUser(ctx, getFormatOU(ou), domainName);
				xml+=GetRemoteDomainGroupDie(ctx, getFormatOU(ou), domainName);
				for (int i = 0; i < oulenth; i++) {
					xml += "</Group>";
				}
				ctx.close();
			}catch (NamingException e) {
				e.printStackTrace();
			}
			return xml;
	}
	
	//获取AD域用户信息
	public String GetADInfo(String name ) {
		String userName = name; // 用户名称
		if(userName==null){
			userName = "";
		}
		String company = "";
		String host = "192.168.61.1"; // AD服务器
		String port = "389"; // 端口
		String url = new String("ldap://" + host + ":" + port);
		Hashtable HashEnv = new Hashtable();
		// String adminName ="CN=oyxiaoyuanxy,CN=Users,DC=Hebmc,DC=com";//AD的用户名
		String adminName = "haitongauto\\testadmin"; // 注意用户名的写法：domain\User 
		String adminPassword = "HTpassword1234"; // 密码
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别
		HashEnv.put(Context.SECURITY_PRINCIPAL, adminName); // AD User
		HashEnv.put(Context.SECURITY_CREDENTIALS, adminPassword); // AD Password
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put(Context.PROVIDER_URL, url);
		try {
			LdapContext ctx = new InitialLdapContext(HashEnv, null);
			// 域节点
			String searchBase = "OU=HaiTong AUTO,DC=haitongauto,DC=com";
			// LDAP搜索过滤器类
			String searchFilter = "objectClass=User";
			// 搜索控制器
			SearchControls searchCtls = new SearchControls(); // Create the
			// search
			// controls
			// 创建搜索控制器
			searchCtls.setSearchScope(SearchControls.SUBTREE_SCOPE); // Specify
			// the
			// search
			// scope
			// 设置搜索范围
			// searchCtls.setSearchScope(SearchControls.OBJECT_SCOPE); //
			// Specify the search scope 设置搜索范围
			//   String returnedAtts[] = { "memberOf", "distinguishedName",
			//			     "Pwd-Last-Set", "User-Password", "cn" };// 定制返回属性
			String returnedAtts[] = { "company" };// 定制返回属性
   
			// String returnedAtts[] = { "url", "whenChanged", "employeeID",
			// "name", "userPrincipalName", "physicalDeliveryOfficeName",
			// "departmentNumber", "telephoneNumber", "homePhone",
			// "mobile", "department", "sAMAccountName", "whenChanged",
			// "mail" }; // 定制返回属性
			searchCtls.setReturningAttributes(returnedAtts); // 设置返回属性集
			// 根据设置的域节点、过滤器类和搜索控制器搜索LDAP得到结果
			NamingEnumeration answer = ctx.search(searchBase, searchFilter, searchCtls);// Search for objects using the filter
			// 初始化搜索结果数为0
			int totalResults = 0;// Specify the attributes to return
			int rows = 0;
			while (answer.hasMoreElements()) {// 遍历结果集
				SearchResult sr = (SearchResult) answer.next();// 得到符合搜索条件的DN
				System.out.println(++rows + "************************************************");

				String dn = sr.getName();
				System.out.println(dn);
				String match = dn.split("CN=")[1].split(",")[0];//返回格式一般是CN=ptyh,OU=专卖
				System.out.println(match);
				if(userName.equals(match)){
					Attributes Attrs = sr.getAttributes();// 得到符合条件的属性集
					if (Attrs != null) {
						try {
							for (NamingEnumeration ne = Attrs.getAll(); ne.hasMore();) {
								Attribute Attr = (Attribute) ne.next();// 得到下一个属性
								System.out.println(" AttributeID=属性名："+ Attr.getID().toString());
								// 读取属性值
								for (NamingEnumeration e = Attr.getAll(); e.hasMore(); totalResults++) {
									company =  e.next().toString();
									System.out.println("    AttributeValues=属性值：" + company);
								}
								System.out.println("    ---------------");
   
							}
						} catch (NamingException e) {
							System.err.println("Throw Exception : " + e);
						}
					}//if
				}
			}//while
			System.out.println("************************************************");
			System.out.println("Number: " + totalResults);
			ctx.close();
		} catch (NamingException e) {
			e.printStackTrace();
			System.err.println("Throw Exception : " + e);
		}
		return company;
		}
	
	public boolean userLogin(String userName, String password) {  //新建用户验证。            
		Hashtable envi = new Hashtable();            
		try {                
			envi.put("java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");                
			envi.put("java.naming.provider.url", url.toString());                
			envi.put(Context.SECURITY_AUTHENTICATION, "simple");                
			envi.put("java.naming.security.principal", userName);                
			envi.put("java.naming.security.credentials", password);                
			InitialContext iCnt = new InitialContext(envi);                
			return true;            
		} catch (Exception e) {                
			//e.printStackTrace();                
			return false;            
		} finally {                
			try {                   
				ctx.close();                
			} catch (Exception ie) {
				
			}            
		}        
	} 
		
	
	
	/*************************/

	public static void main(String args[])throws Exception {
		try {
			// 实例化
			LdapADHelper ad = new LdapADHelper();
			ad.initLdap();
			//ad.opLdap();
			/*ad.GetADInfo("user", "cn", "李XX");// 查找用户
			ad.GetADInfo("organizationalUnit", "ou", "工程");// 查找组织架构
			ad.GetADInfo("group", "cn", "福建xxx");// 查找用户组
			ad.closeLdap();*/
			
			//ad.GetADInfo("testadmin");
			
			/*
			 * OU=海通国际汽车物流有限公司
				OU=海通国际汽车码头有限公司
				OU=公司领导
				OU=客户&供应商
			 */
			ad.getAllADInfo("海通国际汽车码头有限公司");
			//System.out.println(ad.GetRemoteDomainUser(ad.ctx, "ou=HaiTong AUTO", "haitongauto//.com"));
			
			/***测试当前登录相关***/
			//System.out.println(ad.GetRemoteDomainGroup("192.168.61.1", "389", "testadmin", "HTpassword1234", "haitongauto.com", "HaiTong AUTO"));
			/*
			 * 输出结果：
			 * <?xml version="1.0" encoding="gbk" ?><Group name="hongan"><Group name="rtrt"><User name="small" /><Group name="ggd"><Group name="343"><Group name="6677"></Group></Group></Group><Group name="gggg"><Group name="111"><Group name="ggg"></Group><Group name="iiii"></Group></Group><Group name="222"><Group name="lllk"></Group></Group></Group></Group></Group>
			 */
			/*********************/
			
			/*String user = System.getProperty("user.name"); 
			System.out.println(user);*/
			
			/*//获取本机信息
			Map<String, String> map = System.getenv();	    
			String userName = map.get("USERNAME");// 获取用户名	    
			String computerName = map.get("COMPUTERNAME");// 获取计算机名	    
			String userDomain = map.get("USERDOMAIN");// 获取计算机域名	    
			System.out.println(userName);	    
			System.out.println(computerName);	    
			System.out.println(userDomain);*/
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}