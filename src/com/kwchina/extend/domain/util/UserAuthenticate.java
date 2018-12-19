package com.kwchina.extend.domain.util;

import java.util.Hashtable;

import javax.naming.AuthenticationException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingEnumeration;
import javax.naming.NamingException;
import javax.naming.directory.DirContext;
import javax.naming.directory.SearchControls;
import javax.naming.directory.SearchResult;
import javax.naming.ldap.Control;
import javax.naming.ldap.InitialLdapContext;
import javax.naming.ldap.LdapContext;

public class UserAuthenticate {
	//private String URL = "ldap://192.168.61.1:389/";
	private String URL = "ldap://www.haitongauto.com/";
	private String BASEDN = "ou=HaiTongAUTO,o=haitongauto.com";
	private String FACTORY = "com.sun.jndi.ldap.LdapCtxFactory";
	private LdapContext ctx = null;
	private Hashtable env = null;
	private Control[] connCtls = null;
	
	//StringBuffer url;   

	private void LDAP_connect() {
		env = new Hashtable();
		env.put(Context.INITIAL_CONTEXT_FACTORY, FACTORY);
		env.put(Context.PROVIDER_URL, URL + BASEDN);// LDAP server
		env.put(Context.SECURITY_AUTHENTICATION, "simple");
		// 此处若不指定用户名和密码,则自动转换为匿名登录

		try {
			ctx = new InitialLdapContext(env, connCtls);
		} catch (javax.naming.AuthenticationException e) {
			System.out.println("Authentication faild: " + e.toString());
		} catch (Exception e) {
			System.out.println("Something wrong while authenticating: "
					+ e.toString());
		}
	}

	private String getUserDN(String email) {
		String userDN = "";

		LDAP_connect();

		try {
			SearchControls constraints = new SearchControls();
			constraints.setSearchScope(SearchControls.SUBTREE_SCOPE);
			NamingEnumeration en = ctx.search("", "mail=" + email, constraints); // The  UID you are going to query,* means all nodes
			if (en == null) {
				System.out.println("Have no NamingEnumeration.");
			}
			if (!en.hasMoreElements()) {
				System.out.println("Have no element.");
			}
			while (en != null && en.hasMoreElements()) {// maybe more than one
														// element
				Object obj = en.nextElement();
				if (obj instanceof SearchResult) {
					SearchResult si = (SearchResult) obj;
					userDN += si.getName();
					userDN += "," + BASEDN;
				} else {
					System.out.println(obj);
				}
				System.out.println();
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Exception in search():" + e);
		}

		return userDN;
	}

	public boolean authenricate(String ID, String password) {
		boolean valide = false;
		String userDN = getUserDN(ID);

		try {
			ctx.addToEnvironment(Context.SECURITY_PRINCIPAL, userDN);
			ctx.addToEnvironment(Context.SECURITY_CREDENTIALS, password);
			ctx.reconnect(connCtls);
			System.out.println(userDN + " is authenticated");
			valide = true;
		} catch (AuthenticationException e) {
			System.out.println(userDN + " is not authenticated");
			System.out.println(e.toString());
			valide = false;
		} catch (NamingException e) {
			System.out.println(userDN + " is not authenticated");
			valide = false;
		}

		return valide;
	}
	
	public boolean userLogin(String userName, String password) {  //用户验证。            
		Hashtable envi = new Hashtable();            
		try {                
			envi.put("java.naming.factory.initial","com.sun.jndi.ldap.LdapCtxFactory");                
			envi.put("java.naming.provider.url", URL.toString());                
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
	
	public static void main(String[] args) {
		UserAuthenticate user = new UserAuthenticate();
		//user.getUserDN("testadmin@haitongauto.com");
		//System.out.println(user.authenricate("testoa1@haitongauto.com", "HTpassword1234HTpassword1234"));
		System.out.println(user.userLogin("pengfb@haitongauto.com", "051220leona"));
	}
}