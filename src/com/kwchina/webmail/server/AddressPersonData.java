package com.kwchina.webmail.server;

public interface AddressPersonData {
	/**
     * The unique ID of this mailbox
     */
	public String getID(); 
	
	 /**
     * 人员姓名
     * @return Value of 人员姓名.
     */
	public String getPersonName();
	
	
	  /**
     * 设定人员姓名.
     * @param personName  Value to assign to PersonName.
     */
	public void setPersonName(String personName);
}
