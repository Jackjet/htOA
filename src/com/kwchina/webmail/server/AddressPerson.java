package com.kwchina.webmail.server;

public class AddressPerson {
	private String id;

	private String name;

	private String email;

	private String nickName;

	private String mobile;

	private String job;

	private String imgoogle;

	private String immsn;

	private String imqq;

	private String imskype;

	private String homeTel;

	private String homeAddress;

	private String homeCity;

	private String homeState;

	private String homeZip;

	private String homeCountry;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getHomeAddress() {
		return homeAddress;
	}

	public void setHomeAddress(String homeAddress) {
		this.homeAddress = homeAddress;
	}

	public String getHomeCity() {
		return homeCity;
	}

	public void setHomeCity(String homeCity) {
		this.homeCity = homeCity;
	}

	public String getHomeCountry() {
		return homeCountry;
	}

	public void setHomeCountry(String homeCountry) {
		this.homeCountry = homeCountry;
	}

	public String getHomeState() {
		return homeState;
	}

	public void setHomeState(String homeState) {
		this.homeState = homeState;
	}

	public String getHomeTel() {
		return homeTel;
	}

	public void setHomeTel(String homeTel) {
		this.homeTel = homeTel;
	}

	public String getHomeZip() {
		return homeZip;
	}

	public void setHomeZip(String homeZip) {
		this.homeZip = homeZip;
	}

	/**
     * The unique ID of this person
     */
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getImgoogle() {
		return imgoogle;
	}

	public void setImgoogle(String imgoogle) {
		this.imgoogle = imgoogle;
	}

	public String getImmsn() {
		return immsn;
	}

	public void setImmsn(String immsn) {
		this.immsn = immsn;
	}

	public String getImqq() {
		return imqq;
	}

	public void setImqq(String imqq) {
		this.imqq = imqq;
	}

	public String getImskype() {
		return imskype;
	}

	public void setImskype(String imskype) {
		this.imskype = imskype;
	}

	public String getJob() {
		return job;
	}

	public void setJob(String job) {
		this.job = job;
	}

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	 /**
     * 人员姓名
     * @return Value of 人员姓名.
     */
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNickName() {
		return nickName;
	}

	public void setNickName(String nickName) {
		this.nickName = nickName;
	}

}
