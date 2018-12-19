package com.kwchina.webmail.server;

import java.util.Hashtable;
import java.util.StringTokenizer;

import com.kwchina.webmail.authenticator.Authenticator;
import com.kwchina.webmail.exception.WebMailException;

/**
 * AuthenticatorHandler.java
 */

public class AuthenticatorHandler {

	WebMailServer parent;

	Hashtable authenticators;

	String authenticator_list;
	

	public AuthenticatorHandler(WebMailServer parent) throws WebMailException {
		this.parent = parent;

		authenticator_list = parent.getProperty("webmail.authenticators");
		if (authenticator_list == null) {
			throw new WebMailException(
					"No Authenticators defined (parameter: webmail.authenticators)");
		}

		parent.getConfigScheme().configRegisterChoiceKey("AUTH","Authentication method to use.");		
		// used for remote authentication (e.g. for IMAP,POP3)");
		registerAuthenticators();
		
		/** zlb:  ����Ĭ�ϵ�Э��*/
		parent.getConfigScheme().setDefaultValue("AUTH", "IMAP");
	}

	/**
	 * Initialize and register WebMail Authenticators.
	 */
	public void registerAuthenticators() {
		System.err.println("- Initializing WebMail Authenticator Plugins ...");

		StringTokenizer tok = new StringTokenizer(authenticator_list, ":;, ");

		authenticators = new Hashtable();
		while (tok.hasMoreTokens()) {
			String name = (String) tok.nextToken();
			try {
				Class c = Class.forName(name);
				Authenticator a = (Authenticator) c.newInstance();
				
				a.register(parent.getConfigScheme());
				authenticators.put(a.getKey(), a);
				
				System.err.println("  * registered authenticator plugin \""	+ c.getName() + "\"");
			} catch (Exception ex) {
				System.err.println("  * Error: could not register \"" + name
						+ "\" (" + ex.getMessage() + ")!");
				// ex.printStackTrace();
			}
		}
		System.err.println("  done!");
	}

	public Authenticator getAuthenticator(String key) {
		return (Authenticator) authenticators.get(key);
	}

}
