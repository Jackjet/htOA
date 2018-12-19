package com.kwchina.webmail.authenticator;

import javax.mail.MessagingException;
import javax.mail.NoSuchProviderException;
import javax.mail.Session;
import javax.mail.Store;

import com.kwchina.webmail.config.ConfigScheme;
import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.server.WebMailVirtualDomain;
import com.kwchina.webmail.storage.Storage;


/**
 * IMAPAuthenticator.java
*/

public class POPAuthenticator extends com.kwchina.webmail.authenticator.Authenticator {

	public final String VERSION = "1.2";

	private Store st;

	private Storage storage;

	public POPAuthenticator() {
		super();
	}

	public String getVersion() {
		return VERSION;
	}

	//�����Ȩ��ȡ�����session,�Լ�����Store����
	public void init(Storage store) {
		storage = store;
		Session session = Session.getDefaultInstance(System.getProperties(),null);
		try {
			//st = session.getStore("imap");
			st = session.getStore("pop3");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	public void register(ConfigScheme store) {
		key = "POP3";
		store.configAddChoice(
						"AUTH",
						key,
						"Authenticate against an POP3 server on the net. Does not allow password change.");
	}

	
	//�ж��û��û��������Ƿ���ȷ
	public void authenticatePreUserData(String user, String domain,
			String passwd) throws InvalidPasswordException {
		//
		super.authenticatePreUserData(user, domain, passwd);
		
		System.out.println("--POP3:authenticatePreUserData--");
		
		//
		WebMailVirtualDomain vd = storage.getVirtualDomain(domain);
		String authhost = vd.getAuthenticationHost();
		
		try {
			st.connect(authhost, user, passwd);			
			st.close();
			storage.log(Storage.LOG_INFO, "POPAuthenticator: user " + user
					+ " authenticated successfully (imap host: " + authhost
					+ ").");
		} catch (MessagingException e) {
			storage.log(Storage.LOG_WARN, "POPAuthenticator: user " + user
					+ " authentication failed (imap host: " + authhost + ").");
			// e.printStackTrace();
			throw new InvalidPasswordException("POP3 authentication failed!");
		}
	}

	public boolean canChangePassword() {
		return false;
	}

}