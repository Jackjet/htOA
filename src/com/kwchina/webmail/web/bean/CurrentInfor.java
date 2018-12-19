package com.kwchina.webmail.web.bean;

public class CurrentInfor {
	private String type;

	private String id;
	
	

	// Folder的信息
	private int firstMessage;

	private int lastMessage;

	private int listPart;

	private int allMessage;

	private int allPart;

	
	

	public int getAllMessage() {
		return allMessage;
	}

	public void setAllMessage(int allMessage) {
		this.allMessage = allMessage;
	}

	public int getAllPart() {
		return allPart;
	}

	public void setAllPart(int allPart) {
		this.allPart = allPart;
	}

	public int getFirstMessage() {
		return firstMessage;
	}

	public void setFirstMessage(int firstMessage) {
		this.firstMessage = firstMessage;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getLastMessage() {
		return lastMessage;
	}

	public void setLastMessage(int lastMessage) {
		this.lastMessage = lastMessage;
	}

	public int getListPart() {
		return listPart;
	}

	public void setListPart(int listPart) {
		this.listPart = listPart;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

}
