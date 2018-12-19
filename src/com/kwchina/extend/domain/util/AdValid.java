package com.kwchina.extend.domain.util;
import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.directory.DirContext;
import javax.naming.directory.InitialDirContext;

/**
 * 使用java连接AD域,验证账号密码是否正确
 * @author Herman.Xiong
 * @date 2014-12-23 下午02:07:26
 * @version V3.0
 * @since jdk 1.6,tomcat 6.0
 */
public class AdValid {
	
	private static String DOMAINNAME = "@haitongauto.com";
	private static String DOMAINHOST = "192.168.61.1";
	private static String DOMAINPORT = "389";
	
	/**
	 * 使用java连接AD域
	 * @author Herman.Xiong
	 * @date 2014-12-23 下午02:24:04
	 * @return void  
	 * @throws 异常说明
	 * @param host 连接AD域服务器的ip
	 * @param post AD域服务器的端口
	 * @param username 用户名
	 * @param password 密码
	 */
	public static boolean connect(String username,String password) {
		boolean valid = false;
		
		DirContext ctx=null;
		Hashtable HashEnv = new Hashtable();
		HashEnv.put(Context.SECURITY_AUTHENTICATION, "simple"); // LDAP访问安全级别(none,simple,strong)
		HashEnv.put(Context.SECURITY_PRINCIPAL, username + DOMAINNAME); //AD的用户名
		HashEnv.put(Context.SECURITY_CREDENTIALS, password); //AD的密码
		HashEnv.put(Context.INITIAL_CONTEXT_FACTORY,"com.sun.jndi.ldap.LdapCtxFactory"); // LDAP工厂类
		HashEnv.put("com.sun.jndi.ldap.connect.timeout", "3000");//连接超时设置为3秒
		HashEnv.put(Context.PROVIDER_URL, "ldap://" + DOMAINHOST + ":" + DOMAINPORT);// 默认端口389
		try {
			ctx = new InitialDirContext(HashEnv);// 初始化上下文
			valid = true; 
			//System.out.println("身份验证成功!");
		} catch (AuthenticationException e) {
			//System.out.println("身份验证失败!");
			valid = false;
			//e.printStackTrace();
		} catch (javax.naming.CommunicationException e) {
			valid = false;
			System.out.println("AD域连接失败!");
			//e.printStackTrace();
		} catch (Exception e) {
			valid = false;
			System.out.println("身份验证未知异常!");
			//e.printStackTrace();
		} finally{
			//if(null!=ctx){
				try {
					ctx.close();
					ctx=null;
				} catch (Exception e) {
					valid = false;
					//e.printStackTrace();
				}
			//}
		}
		
		return valid;
	}
	
	public static void main(String[] args) {
		System.out.println(AdValid.connect("pengfb", "051220leona"));
	}
}