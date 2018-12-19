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

public class IMAPAuthenticator extends com.kwchina.webmail.authenticator.Authenticator {

	public final String VERSION = "1.2";

	private Store st;

	private Storage storage;

	public IMAPAuthenticator() {
		super();
	}

	public String getVersion() {
		return VERSION;
	}

	// �����Ȩ��ȡ�����session,�Լ�����Store����
	public void init(Storage store) {
		storage = store;
		Session session = Session.getDefaultInstance(System.getProperties(), null);
		try {
			st = session.getStore("imap");
			// st = session.getStore("pop3");
		} catch (NoSuchProviderException e) {
			e.printStackTrace();
		}
	}

	public void register(ConfigScheme store) {
		key = "IMAP";
		store.configAddChoice("AUTH", key, "Authenticate against an IMAP server on the net. Does not allow password change.");
	}

	// �ж��û��û��������Ƿ���ȷ
	public void authenticatePreUserData(String user, String domain, String passwd) throws InvalidPasswordException {
		// �������ж��û��Ƿ���ȷ
		super.authenticatePreUserData(user, domain, passwd);

		System.out.println("--IMAP:authenticatePreUserData--");

		//
		WebMailVirtualDomain vd = storage.getVirtualDomain(domain);
		String authhost = vd.getAuthenticationHost();

		try {
			//st.connect("192.168.30.1","chenm","123456");
			st.connect(authhost, user, passwd);
			// st.connect(authhost,-1, user, passwd);
			st.close();
			storage.log(Storage.LOG_INFO, "IMAPAuthentication: user " + user + " authenticated successfully (imap host: "
					+ authhost + ").");
		} catch (MessagingException e) {
			storage.log(Storage.LOG_WARN, "IMAPAuthentication: user " + user + " authentication failed (imap host: " + authhost
					+ ").");
			// e.printStackTrace();
			System.out.println("--IMAP authentication failed:" + e.toString());

			throw new InvalidPasswordException("IMAP authentication failed!");
		}
	}

	public boolean canChangePassword() {
		return false;
	}

}