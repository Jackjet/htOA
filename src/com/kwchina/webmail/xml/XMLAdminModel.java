package com.kwchina.webmail.xml;

import java.util.List;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.kwchina.webmail.server.WebMailServer;

/*
 * XMLAdminModel.java
 * 
 *
 */

public class XMLAdminModel extends XMLGenericModel {

	public XMLAdminModel(WebMailServer parent, Element rsysdata)
			throws ParserConfigurationException {
		super(parent, rsysdata);
	}

	public synchronized Element addStateElement(String tag) {
		Element elem = new DefaultElement(tag);
		
		statedata.add(elem);
		return elem;
	}

	public synchronized Element createElement(String tag) {
		return new DefaultElement(tag);
	}

	public synchronized Element createTextElement(String tag, String value) {
		Element elem = new DefaultElement(tag);
		
		XMLCommon.setElementTextValue(elem, value);
		return elem;
	}

	public synchronized void importUserData(Element userdata) {
		
		//把userdata放到usermodel
		/**
		List ls = root.getRootElement().selectNodes("USERDATA");
		if(!ls.isEmpty()){
			Element ele = (Element)ls.get(0);
			root.getRootElement().remove(ele);
		}*/
			
		//先删除
		XMLCommon.genericRemoveAll(statedata, "USERDATA");
		
		Element copySys = userdata.createCopy();
		statedata.add(copySys);
	}

	public synchronized void clearUserData() {
		XMLCommon.genericRemoveAll(statedata, "USERDATA");
	}

} 
