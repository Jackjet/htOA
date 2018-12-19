package com.kwchina.webmail.xml;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;

import org.dom4j.Element;
import org.dom4j.tree.DefaultElement;

import com.kwchina.webmail.server.AddressPerson;
import com.kwchina.webmail.util.MiscCommonMethod;

public class XMLAddressData  {
	
	//用户配置文件中的USERDATA下的ADDRESS
	protected Element address;
	
	//用户配置文件中的USERDATA
	protected Element userdata;

	//GROUPLIST
	private Element grouplist;
	
	//PERSONLIST
	private Element personlist;
	
	
	public XMLAddressData(Element udata) {
		this.userdata = udata;
		
		//查看USERDATA下是否有ADDRESS元素
		ArrayList lsAddress= XMLCommon.getElementsByName(udata, "ADDRESS");
		if(lsAddress.isEmpty()){
			address = new DefaultElement("ADDRESS");
			grouplist = address.addElement("GROUPLIST");
			personlist = address.addElement("PERSONLIST");
			
			udata.add(address);			
		}else{
			address = (Element)lsAddress.get(0);
			
			grouplist = address.element("GROUPLIST");
			personlist = address.element("PERSONLIST");
		}		
	}
	
	
	//保存分组信息
	public void saveAddressGroup(String groupName,String groupId,String personIds) throws UnsupportedEncodingException{
		//如果已经存在
		if (groupId!=null && !groupId.equals("")){
			//group = getAddressGroup(groupId);
			removeGroup(groupId);
		}	
		
		
		Element group = new DefaultElement("GROUP");	
		String name = MiscCommonMethod.stringEncoding(groupName,"UTF-8","ISO8859_1");			
		group.addAttribute("name",name);
		String codeId = Long.toHexString(Math.abs(groupName.hashCode())) + Long.toHexString(System.currentTimeMillis());
		group.addAttribute("id",codeId);
		
		if(personIds!=null && !personIds.equals("")){
			String[] ids = personIds.split("\\|");
			for(int k = 0;k<ids.length;k++){
				String id = ids[k];
				
				Element groupPerson = new DefaultElement("PERSON");
				groupPerson.addAttribute("id",id);
				
				group.add(groupPerson);
			}
		}		

		grouplist.add(group);
		
	}
	
	//根据Id，删除Group
	public void removeGroup(String groupId) {
		Element group = getAddressGroup(groupId);
		ArrayList groupList = XMLCommon.getElementsByName(userdata,"GROUPLIST");	
		if (group != null) {
			group.detach();
			
			//Element eleList = (Element)groupList.get(0);
			//eleList.remove(group);
			
			grouplist.remove(group);
		}
	}
	
	//根据组Id获取组信息
	public Element getAddressGroup(String groupId){
		Element group = XMLCommon.getElementByAttribute(userdata,"GROUP", "id", groupId);
		return group;
	}
	
	
	//保存通讯录人员信息
	public void savePerson(AddressPerson person) throws UnsupportedEncodingException{
		String pId = person.getId();
		
		//如果已经存在
		if (pId!=null && !pId.equals("")){
			//修改人员信息
			Element elePerson = XMLCommon.getElementByAttribute(userdata,"PERSON", "id", pId);
			
			Element ele = elePerson.element("AB_NAME");
			ele.setText(person.getName());
			
			ele = elePerson.element("AB_NAME");
			ele.setText(person.getName());
			
			ele = elePerson.element("AB_EMAIL");
			ele.setText(person.getEmail());
			
			ele = elePerson.element("AB_NICKNAME");
			ele.setText(person.getNickName());
			
			ele = elePerson.element("AB_MOBILE");
			ele.setText(person.getMobile());
			
			ele = elePerson.element("AB_JOB");
			ele.setText(person.getJob());
			
			ele = elePerson.element("AB_IMGOOGLE");
			ele.setText(person.getImgoogle());
			
			ele = elePerson.element("AB_IMMSN");
			ele.setText(person.getImmsn());
			
			ele = elePerson.element("AB_IMQQ");
			ele.setText(person.getImqq());
			
			ele = elePerson.element("AB_IMSKYPE");
			ele.setText(person.getImskype());
			
			
			ele = elePerson.element("AB_HOMETEL");
			ele.setText(person.getHomeTel());
			
			ele = elePerson.element("AB_HOMEADDRESS");
			ele.setText(person.getHomeAddress());
			
			ele = elePerson.element("AB_HOMECITY");
			ele.setText(person.getHomeCity());
			
			ele = elePerson.element("AB_HOMESTATE");
			ele.setText(person.getHomeState());
			
			ele = elePerson.element("AB_HOMEZIP");
			ele.setText(person.getHomeZip());
			
			ele = elePerson.element("AB_HOMECOUNTRY");
			ele.setText(person.getHomeCountry());			
			
		}else{
			//新增人员信息
			Element elePerson = new DefaultElement("PERSON");
			String codeId = Long.toHexString(Math.abs(person.getName().hashCode())) + Long.toHexString(System.currentTimeMillis());
			elePerson.addAttribute("id",codeId);
			
			Element ele = new DefaultElement("AB_NAME");
			ele.addText(person.getName());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_EMAIL");
			ele.addText(person.getEmail());
			elePerson.add(ele);
					
			ele = new DefaultElement("AB_NICKNAME");
			ele.addText(person.getNickName());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_MOBILE");
			ele.addText(person.getMobile());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_JOB");
			ele.addText(person.getJob());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_IMGOOGLE");
			ele.addText(person.getImgoogle());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_IMMSN");
			ele.addText(person.getImmsn());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_IMQQ");
			ele.addText(person.getImqq());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_IMSKYPE");
			ele.addText(person.getImskype());
			elePerson.add(ele);
			
			
			//
			ele = new DefaultElement("AB_HOMETEL");
			ele.addText(person.getHomeTel());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_HOMEADDRESS");
			ele.addText(person.getHomeAddress());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_HOMECITY");
			ele.addText(person.getHomeCity());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_HOMESTATE");
			ele.addText(person.getHomeState());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_HOMEZIP");
			ele.addText(person.getHomeZip());
			elePerson.add(ele);
			
			ele = new DefaultElement("AB_HOMECOUNTRY");
			ele.addText(person.getHomeCountry());
			elePerson.add(ele);			
				
			personlist.add(elePerson);			
		}
	}
	
	//根据组Id获取人员信息
	public Element getAddressPerson(String personId){
		ArrayList personList = XMLCommon.getElementsByName(userdata,"PERSONLIST");	
		if(! personList.isEmpty()){
			Element eleList = (Element)personList.get(0); 
			
			Element person = XMLCommon.getElementByAttribute(eleList,"PERSON", "id", personId);
			return person;
		}else{
			return null;
		}		
	}
	
	
	//根据Id，删除Person
	public void removePerson(String personId) {
		Element person = getAddressPerson(personId);
		ArrayList personList = XMLCommon.getElementsByName(userdata,"PERSONLIST");	
		if (person != null) {
			person.detach();
			
			Element eleList = (Element)personList.get(0);
			eleList.remove(person);	
			
			//删除其所属的组别
			//找到GROUPLIST下面所有的PERSON节点，如果id相等，则去掉
			ArrayList groupList = XMLCommon.getElementsByName(userdata,"GROUPLIST");
			Element eleGroupList = (Element)groupList.get(0);
			ArrayList groupPersons = XMLCommon.getElementsByName(eleGroupList,"PERSON");
			for(Iterator it=groupPersons.iterator();it.hasNext();){
				Element eleGroupPerson = (Element)it.next();
				String tempId = eleGroupPerson.attributeValue("id");
				if(tempId.equals(personId)){
					Element group = eleGroupPerson.getParent();
					
					eleGroupPerson.detach();					
					group.remove(eleGroupPerson);	
				}				
			}
		}
	}
		
	
}
