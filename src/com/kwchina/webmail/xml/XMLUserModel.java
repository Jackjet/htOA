package com.kwchina.webmail.xml;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.w3c.dom.DOMException;

import com.kwchina.webmail.server.WebMailServer;

/*
 * XMLUserModel.java
 * 
 */

/**
 * 
 * Mainly consists of a DOM that represents all of the data in the user's
 * session. On subtrees, there are the SYSDATA and the USERDATA DOM trees (among
 * other stuff like folder list, message list, etc)
 * 
 * Many methods here are synchronized but that shouldn't hurt performance too
 * much since the cases where several Threads access the model are rare anyway
 * 
 * DOM,存储了javax.mail.Session中所有的用户数据 该DOM包含了如下信息： 1. Webmail Sysem Data 2. User
 * Data 3. State Data 4. MAILHOST_MODEL
 */

public class XMLUserModel extends XMLGenericModel {

	protected Element usermodel;

	protected Element userdata;

	public XMLUserModel(WebMailServer parent, Element rsysdata, Element ruserdata) throws ParserConfigurationException {

		super(parent, rsysdata);

		usermodel = root.getRootElement();

		this.userdata = ruserdata;

		update();
	}

	protected void initRoot() {
		// Create a new usermodel from the template file
		try {
			root = reader.read(
			// "file://"+
					parent.getProperty("webmail.xml.path") + System.getProperty("file.separator") + "usermodel_template.xml");
		} catch (Exception ex) {
			System.err.println("Error parsing WebMail UserModel template " + ex.getMessage());
			ex.printStackTrace();
		}
	}

	// 把系统信息、用户信息加入到XMLUserModel
	public synchronized void update() {
		// Insert the sysdata and userdata objects into the usermodel tree
		super.update();

		try {
			/** @todo:一个插入另外一个文件 */
			List ls = usermodel.elements("USERDATA");
			if (!ls.isEmpty()) {
				Element ele = (Element) ls.get(0);
				usermodel.remove(ele);
			}

			// 复制USERDATA到usermodel
			Element copySys = userdata.createCopy();
			usermodel.add(copySys);

			/**
			 * Element ele = (Element)ls.get(0);
			 * 
			 * List ls_1 = usermodel.selectNodes("USERDATA");
			 * usermodel.remove((Element)ls_1.get(0));
			 * 
			 * usermodel.add(ele);
			 */

			/** 把XML输出查看 */
			/**
			 * try{ Writer out = new OutputStreamWriter(new
			 * FileOutputStream("c:\\test.xml"),"GBK"); OutputFormat format =
			 * OutputFormat.createPrettyPrint(); //指定XML编码
			 * format.setEncoding("GBK");
			 * 
			 * XMLWriter writer = new XMLWriter(out, format);
			 * writer.write(usermodel); out.flush(); out.close();
			 * }catch(Exception ex){
			 *  }
			 */

			// NodeList nl = root.getElementsByTagName("USERDATA");
			// usermodel.replaceChild(root.importNode(userdata, true),
			// nl.item(0));
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.err.println("The WebMail UserModel template file didn't contain a USERDATA tag.");
		} catch (DOMException ex) {
			System.err.println("Something went wrong with the XML user model.");
		}
	}

	// 在UserModel中创建FOLDER元素
	public synchronized Element createFolder(String id, String name, boolean holds_folders, boolean holds_messages) {
		Element folder = new DefaultElement("FOLDER");

		folder.addAttribute("id", id);
		folder.addAttribute("name", name);
		folder.addAttribute("holds_folders", holds_folders + "");
		folder.addAttribute("holds_messages", holds_messages + "");

		return folder;
	}

	// 根据Folder的Id 获得Floder
	public synchronized Element getFolder(String id) {
		return XMLCommon.getElementByAttribute(usermodel, "FOLDER", "id", id);
	}

	// 根据

	public synchronized Element createMessageList() {
		Element messagelist = new DefaultElement("MESSAGELIST");
		return messagelist;
	}

	// 获取USERDATA的Element
	public synchronized Element getUserData() {
		return this.userdata;
	}

	/**
	 * Get messagelist for folder. Create if necessary.
	 */
	public synchronized Element getMessageList(Element folder) {
		List ls = folder.elements();
		Element messageList = null;

		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element tmp = (Element) it.next();
			if (tmp.getName().equals("MESSAGELIST")) {
				messageList = tmp;
				break;
			}
		}

		/**
		 * NodeList nl = folder.getChildNodes(); Element messagelist = null; for
		 * (int i = 0; i < nl.getLength(); i++) { Element tmp = (Element)
		 * nl.item(i); if (tmp.getTagName().equals("MESSAGELIST")) { messagelist =
		 * tmp; break; } } if (messagelist == null) { messagelist =
		 * createMessageList(); folder.appendChild(messagelist); }
		 */

		return messageList;
	}

	// 刪除Folder下的MESSAGELIST(<MESSAGELIST new="0" total="0"/>)
	public synchronized void removeMessageList(Element folder) {
		XMLCommon.genericRemoveAll(folder, "MESSAGELIST");
	}

	/**
	 * Check whether we already fetched this message. This can save a lot of
	 * time and CPU.
	 */
	/**
	 * public synchronized boolean messageCached(Element folder, String msgid) {
	 * NodeList nl = folder.getElementsByTagName("MESSAGE"); Element message =
	 * null; for (int i = 0; i < nl.getLength(); i++) { Element test = (Element)
	 * nl.item(i); if (test.getAttribute("msgid").equals(msgid)) { message =
	 * test; break; } } return message != null; }
	 */

	public synchronized XMLMessage getMessage(Element folder, String msgnr, String msgid) {
		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls, folder, "MESSAGE");

		Element message = null;
		for (int i = 0; i < ls.size(); i++) {
			Element test = (Element) ls.get(i);
			if (test.attributeValue("msgid").equals(msgid)) {
				message = test;
				break;
			}
		}

		if (message == null) {
			message = new DefaultElement("MESSAGE");
			message.addAttribute("msgid", msgid);

			Element msglist = getMessageList(folder);
			msglist.add(message);
		}

		if (msgnr != null && !msgnr.equals(""))
			XMLCommon.setAttributeValue(message, "msgnr", msgnr);

		return new XMLMessage(message);
	}

	/**
	 * Return the WORK element that stores messages that are currently edited.
	 */
	public synchronized XMLMessage getWorkMessage() {
		ArrayList ls = XMLCommon.getElementsByName(usermodel, "WORK");
		Element work;
		if (ls.size() > 0) {
			work = (Element) ls.get(0);
		} else {
			work = new DefaultElement("WORK");
			usermodel.add(work);
		}

		ls = XMLCommon.getElementsByName(work, "MESSAGE");
		XMLMessage message;
		if (ls.size() > 0) {
			message = new XMLMessage((Element) ls.get(0));
		} else {
			message = new XMLMessage(new DefaultElement("MESSAGE"));
			work.add(message.getMessageElement());

			message.setAttribute("msgnr", "0");
			message.setAttribute("msgid", WebMailServer.generateMessageID("webmailuser"));

			XMLMessagePart multipart = message.createPart("multi");
			multipart.createPart("text");
		}

		return message;
	}

	public synchronized void clearWork() {
		// NodeList nl = usermodel.getElementsByTagName("WORK");
		ArrayList ls = XMLCommon.getElementsByName(usermodel, "WORK");
		if (ls.size() > 0) {
			Element work = (Element) ls.get(0);

			List childs = work.elements();
			Vector v = new Vector();
			for (int i = 0; i < childs.size(); i++) {
				v.addElement(childs.get(i));
			}

			Enumeration enum1 = v.elements();
			while (enum1.hasMoreElements()) {
				work.remove((Element) enum1.nextElement());
			}
		}

		/**
		 * if (nl.getLength() > 0) { Element work = (Element) nl.item(0);
		 * NodeList nl2 = work.getChildNodes(); Vector v = new Vector(); for
		 * (int i = 0; i < nl2.getLength(); i++) { v.addElement(nl2.item(i)); }
		 * Enumeration enum1 = v.elements(); while (enum1.hasMoreElements()) {
		 * work.removeChild((Node) enum1.nextElement()); } }
		 */
	}

	/**
	 * Set the current work message (for forwarding and replying). Note that
	 * this method uses importNode, therefore the newly cloned message element
	 * is returned by this method.
	 */
	public synchronized XMLMessage setWorkMessage(XMLMessage message) {
		clearWork();

		ArrayList ls = XMLCommon.getElementsByName(usermodel, "WORK");
		Element work;
		if (ls.size() > 0) {
			work = (Element) ls.get(0);
		} else {
			work = new DefaultElement("WORK");
			usermodel.add(work);
		}

		// 把message.getMessageElement()复制到WORK下
		Element copyMessage = message.getMessageElement().createCopy();
		/**
		 * Element newmessage = (Element)
		 * root.importNode(message.getMessageElement(), true);
		 */
				
		work.add(copyMessage);

		return new XMLMessage(copyMessage);
	}

	// 在UserModel中创建该用户mailhost的相关信息（MAILHOST_MODEL部分）
	public synchronized Element createMailhost(String name, String id, String url) {
		// Element mh = new DefaultElement("MAILHOST_MODEL");
		// usermodel.add(mh);
		Element mh = usermodel.addElement("MAILHOST_MODEL");

		mh.addAttribute("name", name);
		mh.addAttribute("id", id);
		if (url != null) {
			mh.addAttribute("url", url);
		}

		return mh;
	}

	// 添加Mailhost部分到usermodel,Mailhose中可能已经包含所有的Folder信息
	public synchronized void addMailhost(Element mh) {
		String name = mh.attributeValue("name");

		Element elem = XMLCommon.getElementByAttribute(usermodel, "MAILHOST_MODEL", "name", name);
		if (elem == null) {
			usermodel.add(mh);
		} else {
			// 如果找到，用现在的内容去替换
			elem.detach();
			usermodel.remove(elem);

			/** @todo */
			if (mh.getParent() == null || (mh.getParent() != null && !mh.getParent().equals(usermodel))) {
				usermodel.add(mh);
			}
			// usermodel.replaceChild(mh, elem);
		}
	}

	// 根据MAILHOST_MODEL的name 获得MailHost部分
	public synchronized Element getMailhost(String name) {
		return XMLCommon.getElementByAttribute(usermodel, "MAILHOST_MODEL", "name", name);
	}

	// 添加Quato以及相关信息
	public void addMailHostQuato(String mailhostId, Element quato) {
		// 先找到该MailHost
		Element elem = XMLCommon.getElementByAttribute(usermodel, "MAILHOST_MODEL", "id", mailhostId);
		if (elem != null) {
			ArrayList ls = XMLCommon.getElementsByName(elem, "quota");
			if (!ls.isEmpty()) {
				// ls中有相关Quota
				Element quotaEle = (Element) ls.get(0);
				quotaEle.detach();
				elem.remove(quotaEle);

				elem.add(quato);
			} else {
				// 没有
				elem.add(quato);
			}
		}
	}

	protected synchronized Element setCurrent(String type, String id) {
		List ls = usermodel.elements("CURRENT");
		Element current = null;
		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element ele = (Element) it.next();
			if (ele.attributeValue("type").equals(type)) {
				current = ele;
				break;
			}
		}

		if (current == null) {
			current = new DefaultElement("CURRENT");
			current.addAttribute("type", type);
			usermodel.add(current);
		}

		/**
		 * NodeList nl = usermodel.getElementsByTagName("CURRENT"); Element
		 * current = null; for (int i = 0; i < nl.getLength(); i++) { if
		 * (((Element) nl.item(i)).getAttribute("type").equals(type)) { current =
		 * (Element) nl.item(i); break; } } if (current == null) { current =
		 * root.createElement("CURRENT"); current.setAttribute("type", type);
		 * usermodel.appendChild(current); }
		 */

		XMLCommon.setAttributeValue(current, "id", id);
		// current.attributeValue("id", id);
		return current;
	}

	// 取得当前的信息
	protected synchronized Element getCurrent(String type, String id) {
		ArrayList nl = XMLCommon.getElementsByName(usermodel, "CURRENT");

		Element current = null;
		for (int i = 0; i < nl.size(); i++) {
			if (((Element) nl.get(i)).attributeValue("type").equals(type)) {
				current = (Element) nl.get(i);
				break;
			}
		}
		return current;
	}

	// -----------
	public Element setCurrentMessage(String id) {
		return setCurrent("message", id);
	}

	public Element setCurrentFolder(String id) {
		return setCurrent("folder", id);
	}

	public Element setCurrentGroup(String id) {
		return setCurrent("group", id);
	}

	public Element setCurrentPerson(String id) {
		return setCurrent("person", id);
	}

	public Element setBrowseGroup(String id) {
		return setCurrent("groupbrowse", id);
	}
	// -------------------

	public void removeBrowseGroup() {
		Element ele = getBrowseGroup("");
		if (ele != null) {
			ele.detach();
			usermodel.remove(ele);
		}
	}

	public Element getCurrentMessage(String id) {
		return getCurrent("message", id);
	}

	public Element getCurrentFolder(String id) {
		return getCurrent("folder", id);
	}

	public Element getBrowseGroup(String id) {
		return getCurrent("groupbrowse", id);
	}

} // XMLUserModel
