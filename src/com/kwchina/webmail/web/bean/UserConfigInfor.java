package com.kwchina.webmail.web.bean;

public class UserConfigInfor {
	private String login;
	private String password;

	private String fullName;
	private String email;
	private String signature;
	private boolean saveSent;
	private boolean saveAddress;
	private String sendFolder;

	private int maxShow;
	private boolean directDelete;

	public boolean isDirectDelete() {
		return directDelete;
	}
	public void setDirectDelete(boolean directDelete) {
		this.directDelete = directDelete;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getFullName() {
		return fullName;
	}
	public void setFullName(String fullName) {
		this.fullName = fullName;
	}
	public String getLogin() {
		return login;
	}
	public void setLogin(String login) {
		this.login = login;
	}
	public int getMaxShow() {
		return maxShow;
	}
	public void setMaxShow(int maxShow) {
		this.maxShow = maxShow;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public boolean isSaveAddress() {
		return saveAddress;
	}
	public void setSaveAddress(boolean saveAddress) {
		this.saveAddress = saveAddress;
	}
	public boolean isSaveSent() {
		return saveSent;
	}
	public void setSaveSent(boolean saveSent) {
		this.saveSent = saveSent;
	}
	public String getSignature() {
		return signature;
	}
	public void setSignature(String signature) {
		this.signature = signature;
	}
	public String getSendFolder() {
		return sendFolder;
	}
	public void setSendFolder(String sendFolder) {
		this.sendFolder = sendFolder;
	}

}
