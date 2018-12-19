package com.kwchina.webmail.web.bean;

public class MailFolder {

	private String folderId;

	private String folderName;

	private boolean hasFolders = false;

	private boolean holdMessage = false;

	private int totalMessage = 0;

	private int newMessage = 0;
	
	

	public String getFolderName() {
		return folderName;
	}

	public void setFloderName(String folderName) {
		this.folderName = folderName;
	}

	public String getFolderId() {
		return folderId;
	}

	public void setFolderId(String folderId) {
		this.folderId = folderId;
	}

	public boolean isHasFolders() {
		return hasFolders;
	}

	public void setHasFolders(boolean hasFolders) {
		this.hasFolders = hasFolders;
	}

	public boolean isHoldMessage() {
		return holdMessage;
	}

	public void setHoldMessage(boolean holdMessage) {
		this.holdMessage = holdMessage;
	}

	public int getNewMessage() {
		return newMessage;
	}

	public void setNewMessage(int newMessage) {
		this.newMessage = newMessage;
	}

	public int getTotalMessage() {
		return totalMessage;
	}

	public void setTotalMessage(int totalMessage) {
		this.totalMessage = totalMessage;
	}

}
