package com.kwchina.webmail.xml;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.w3c.dom.NodeList;

/*
 * XMLMessage.java
 *
 */

/**
 * Represents an XML message object (part of the XMLUserModel)
 * 
 * @see XMLUserModel
 */
public class XMLMessage extends XMLMessagePart {

	protected Element message;

	public XMLMessage(Element message) {
		super(message);
		this.message = message;
		
		
	}

	public Element getMessageElement() {
		return message;
	}

	public boolean messageCompletelyCached() {
		// NodeList nl = message.getElementsByTagName("PART");
		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls, message, "PART");

		return ls.size() > 0;
	}

	// 设置"回复"邮件的相关内容
	public void prepareReply(String prefix_subject, String postfix_subject, String prefix_message, String postfix_message) {

		String subject = getHeader("SUBJECT");
		// Test whether this is already a reply (prefixed with RE or AW)
		if (!isReply(subject)) {
			subject = prefix_subject + " " + getHeader("SUBJECT") + " " + postfix_subject;
		}
		setHeader("SUBJECT", subject);

		XMLMessagePart firstpart = new XMLMessagePart(getFirstMessageTextPart(message));

		//firstpart.quoteContent();
		
		//前缀
		//firstpart.insertContent(prefix_message + "\n", 0);
		//后缀
		//firstpart.addContent(postfix_message, 0);

		removeAllParts();

		XMLMessagePart newmainpart = createPart("multi");

		newmainpart.appendPart(firstpart);
	}

	// 设置"转发"邮件的相关功能
	public void prepareForward(String prefix_subject, String postfix_subject, String prefix_message, String postfix_message) {

		String subject = getHeader("SUBJECT");
		subject = prefix_subject + " " + getHeader("SUBJECT") + " " + postfix_subject;
		setHeader("SUBJECT", subject);

		/**
		XMLMessagePart mainpart = null;
		List ls = message.elements();
		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element elem = (Element) it.next();
			if (elem.getName().equals("PART")) {
				mainpart = new XMLMessagePart(elem);
				break;
			}
		}*/	
		
		
		
		XMLMessagePart firstpart = new XMLMessagePart(getFirstMessageTextPart(message));
		
		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls,message, "PART");		
		
		removeAllParts();
		//firstpart.insertContent(prefix_message + "\n", 0);
		//firstpart.addContent(postfix_message, 0);

		XMLMessagePart newmainpart = createPart("multi");
		newmainpart.appendPart(firstpart);

		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element elem = (Element) it.next();
			if (elem.getName().equals("PART")) {
				String eleType = elem.attributeValue("type");
				if(eleType.toUpperCase().equals("BINARY") || eleType.toUpperCase().equals("IMAGE")){	
					newmainpart.appendPart(new XMLMessagePart(elem));
				}				
			}
		}
		
		
		
		/**把XML输出查看*/	
		/**
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_3.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(message);
			out.flush();
			out.close();
		}catch(Exception ex){
			
		}*/
		
		
		
		/**
		if (mainpart != null) {
			Enumeration enum1 = mainpart.getParts();
			while (enum1.hasMoreElements()) {
				newmainpart.appendPart((XMLMessagePart) enum1.nextElement());
			}
		}*/
	}

	//
	public Element getHeader() {
		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls, message, "HEADER");
		Element header = ls.size() == 0 ? null : (Element) ls.get(0);

		if (header == null) {
			header = new DefaultElement("HEADER");
			message.add(header);
		}
		return header;
	}

	public String getHeader(String header) {
		Element xml_header = getHeader();

		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls, xml_header, header);

		Element element;
		if (ls.size() == 0) {
			element = new DefaultElement(header);
			xml_header.add(element);
		} else {
			element = (Element) ls.get(0);
		}

		return XMLCommon.getElementTextValue(element);
	}

	public void setHeader(String header, String value) {
		Element xml_header = getHeader();

		ArrayList ls = new ArrayList();
		XMLCommon.getElementsByName(ls, xml_header, header);

		Element element;
		if (ls.size() == 0) {
			element = new DefaultElement(header);
			xml_header.add(element);
		} else {
			element = (Element) ls.get(0);
		}

		XMLCommon.setElementTextValue(element, value);
	}

	protected boolean isReply(String subject) {
		String s = subject.toUpperCase();
		return s.startsWith("RE") || s.startsWith("AW");
	}

	protected Element getFirstMessageTextPart(Element parent) {
		List ls = parent.elements();
		// NodeList nl = parent.getChildNodes();
		/**
		 * Maybe here we should modify the algorithm: If no appropriate
		 * text/plain is found, try to search for text/html.
		 */

		for (int i = 0; i < ls.size(); i++) {
			Element elem = (Element) ls.get(i);
			if (elem.getName().equals("PART")) {
				if (elem.attributeValue("type").equals("multi")) {
					Element retval = getFirstMessageTextPart(elem);
					if (retval != null) {
						return retval;
					}
				} else if (elem.attributeValue("type").equals("html")) {
					return elem;
				}
			}
		}

		for (int i = 0; i < ls.size(); i++) {
			Element elem = (Element) ls.get(i);

			if (elem.getName().equals("PART")) {
				if (elem.attributeValue("type").equals("multi")) {
					Element retval = getFirstMessageTextPart(elem);
					if (retval != null) {
						return retval;
					}
				} else if (elem.attributeValue("type").equals("text")) {
					return elem;
				}
			}
		}

		return null;
	}

	public XMLMessagePart getFirstMessageTextPart() {
		return new XMLMessagePart(getFirstMessageTextPart(message));
	}

	public XMLMessagePart getFirstMessageMultiPart() {
		List ls = message.elements();
		for (int i = 0; i < ls.size(); i++) {
			Element elem = (Element) ls.get(i);
			if (elem.getName().equals("PART")) {
				if (elem.attributeValue("type").equals("multi")) {
					return new XMLMessagePart(elem);
				}
			}
		}

		return null;
	}

} // XMLMessage
