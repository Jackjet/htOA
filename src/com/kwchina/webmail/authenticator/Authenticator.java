package com.kwchina.webmail.authenticator;

import com.kwchina.webmail.config.ConfigScheme;
import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.server.UserData;
import com.kwchina.webmail.storage.Storage;


/**
 * Authenticator.java 
 */

public abstract class Authenticator {
	protected String key;

	public Authenticator() {
	}

	public String getKey() {
		return key;
	}

	public abstract String getVersion();

	/**
	 * Get a displamanager object for this class.
	 * 
	 * @see org.webengruven.webamil.auth.AuthDisplayMngr
	 * @return the AuthDisplayMngr apropriate for this class.
	 */	
	public AuthDisplayMngr getAuthDisplayMngr() {
		return new AuthDisplayMngr();
	}


	/**
	 * (Re-)Initialize this authenticator. Needed as we can't use the
	 * Constructor properly with the Plugin-style.
	 * 
	 * @param parent
	 *            Give the Storage to allow the authenticator to check certain
	 *            things.
	 */	
	public abstract void init(Storage store);
	

	/**
	 * Register this authenticator with WebMail.
	 */
	public abstract void register(ConfigScheme store);

	/**
	 * Authentication to be done *before* UserData is available. You may use a
	 * Unix login() for example to check whether a user is allowed to use
	 * WebMail in general Subclasses should override this. It simply does
	 * nothing in this implementation.
	 * 
	 * @param login
	 *            Login-name for the user
	 * @param domain
	 *            Domain name the user used to log on
	 * @param passwd
	 *            Password to verify
	 */
	//判断用户授权，这里仅为超类接口
	public void authenticatePreUserData(String login, String domain,
			String passwd) throws InvalidPasswordException {
		
		System.out.println("--authenticatePreUserData--");
		
		if (login.equals("") || passwd.equals("")) {
			throw new InvalidPasswordException();
		}
	}

	/**
	 * Authentication with available UserData. This usually should just check
	 * the password saved by the user, but may also be empty if you trust the
	 * pre-authentication (perhaps that was done against the Unix-login(), you
	 * can really trust in in that case. Subclasses should override this. It
	 * simply does nothing in this implementation.
	 * 
	 * @param udata
	 *            UserData for this user
	 * @param domain
	 *            Domain name the user used to log on
	 * @param passwd
	 *            Password to verify
	 */	
	public void authenticatePostUserData(UserData udata, String domain,
			String password) throws InvalidPasswordException {
	}

	/**
	 * Tell WebMail whether this authentication method allows users to change
	 * their passwords. A Password-change option is then shown in the
	 * Options-Dialog.
	 */
	public boolean canChangePassword() {
		return true;
	}


	public void changePassword(UserData udata, String newpassword, String verify)
			throws InvalidPasswordException {
	}
	

}
