package com.kwchina.webmail.xml;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

/*
 * XMLCommon.java 
 * 常用的XML操作方法
 */
public final class XMLCommon {

	// 查找某个节点下，某个名称的所有Element
	public static ArrayList getElementsByName(Element top, String name) {
		ArrayList ls = new ArrayList();

		if (top.getName().equalsIgnoreCase(name)) {
			ls.add(top);
		}

		Iterator iter = top.elementIterator();
		if (!iter.hasNext()) {
			return ls;
		} else {
			while (iter.hasNext()) {
				Element sub = (Element) iter.next();
				// if(sub.getName().equalsIgnoreCase(name)){
				// ls.add(sub);
				// }

				// ArrayList tpLs = getElementsByName(sub, name);
				ls.addAll(getElementsByName(sub, name));
			}
		}

		return ls;
	}

	// 查找某个节点下，某个名称的所有Element
	public static ArrayList getElementsByName(ArrayList elements, Element top, String name) {

		if (top.getName().equalsIgnoreCase(name)) {
			elements.add(top);
		}

		Iterator iter = top.elementIterator();
		if (!iter.hasNext()) {
			//return elements;
		} else {
			while (iter.hasNext()) {
				Element sub = (Element) iter.next();
				getElementsByName(elements, sub, name);
			}
		}
		
		return elements;
	}
	
	

	// 通过名称，属性及属性值查找Element
	public static Element getElementByAttribute(Element root, String tagname, String attribute, String att_value) {
		// 找到改名称的所有元素
		ArrayList elements = new ArrayList();
		elements = getElementsByName(elements, root, tagname);

		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();

			// 判断该Element该attribute的值
			Attribute attr = element.attribute(attribute);
			/** @todo attr有可能为Null equalsIgnoreCase的问题 */
			if (attr != null && attr.getValue().equalsIgnoreCase(att_value)) {
				return element;
			}
		}

		/**
		 * NodeList nl=root.getElementsByTagName(tagname); for(int i=0; i<nl.getLength();i++) {
		 * Element elem=(Element)nl.item(i);
		 * if(elem.getAttribute(attribute).equals(att_value)) { return elem; } }
		 */
		return null;
	}
	
	//通过名称查找下面所有的Element（各个层次）
	public static Element getElementByTag(Element root, String tagname) {
		// 找到该名称的所有元素
		ArrayList elements = new ArrayList();
		elements = getElementsByName(elements, root, tagname);

		for (Iterator iterator = elements.iterator(); iterator.hasNext();) {
			Element element = (Element) iterator.next();
			return element;
		}

		/**
		 * NodeList nl=root.getElementsByTagName(tagname); for(int i=0; i<nl.getLength();i++) {
		 * Element elem=(Element)nl.item(i);
		 * if(elem.getAttribute(attribute).equals(att_value)) { return elem; } }
		 */
		return null;
	}
	
	
	//通过名称查找下面的Element（第一层）
	public static Element getDirectElementByTag(Element root, String tagname) {
		
		Iterator iter = root.elementIterator();
		while (iter.hasNext()) {
			Element element = (Element) iter.next();
			
			if (element.getName().equalsIgnoreCase(tagname)) {
				return element;
			}	
		}		
		
		return null;
	}
	

	// 为Element设定某个属性值
	public static void setAttributeValue(Element e, String attrName, String value) {
		Attribute attr = e.attribute(attrName);
		if (attr == null) {
			e.addAttribute(attrName, value);
		} else {
			// e.attributeValue(value);
			attr.setValue(value);
		}
	}
	
	//获取Element值为boolean
	public static boolean getAttributeBooleanValue(Element e, String attrName){
		String attributeValue = e.attributeValue(attrName);		
		if(attributeValue==null || attributeValue.equals("")){
			return false;
		}else{
			return Boolean.valueOf(attributeValue);
		}
	}
	
	//获取Element值为int
	public static int getAttributeIntValue(Element e, String attrName){
		String attributeValue = e.attributeValue(attrName);
		if(attributeValue==null || attributeValue.equals("")){
			return 0;
		}else{
			return Integer.valueOf(attributeValue);
		}
	}
	

	public static String getElementTextValue(Element e) {
		if (e == null) {
			return "";
		} else {
			String value = e.getStringValue();
			return value.trim();
			//return e.getTextTrim();
		}

		/**
		 * NodeList nl=e.getChildNodes(); if(nl.getLength() <= 0) {
		 * System.err.println("Elements: "+nl.getLength()); return ""; } else {
		 * String s=""; for(int i=0;i<nl.getLength();i++) { if(nl.item(i)
		 * instanceof CharacterData) { s+=nl.item(i).getNodeValue(); } } return
		 * s.trim(); }
		 */
	}

	public static void setElementTextValue(Element e, String text) {
		setElementTextValue(e, text, false);

	}

	public static void setElementTextValue(Element e, String text, boolean cdata) {
		// Document root = e.getDocument();
		e.normalize();	
		
		
		List nl = e.elements();
		if(!nl.isEmpty()){ 
			int length = nl.size();		 
			
			Vector v=new Vector(nl.size()); 
			for(int i=0;i<length;i++) {
				v.add(nl.get(i)); }
		 
			 Enumeration enum1=v.elements(); 
			 while(enum1.hasMoreElements()) {
				 Element tempEle=(Element)enum1.nextElement(); 
				 e.remove(tempEle); 
			 } 
		 }
		
		e.setText(text);
		
		/** @todo cdata含义 */
		/**
		if (cdata) {
			//
			// e.setText(text);			
			e.setText("");
			e.addCDATA(text);
			//e.set
		} else {
			e.setText(text);
		}*/

		/**
		 * List nl = e.elements(); if(!nl.isEmpty()){ int length = nl.size();
		 * 
		 * Vector v=new Vector(nl.size()); for(int i=0;i<length;i++) {
		 * v.add(nl.get(i)); }
		 * 
		 * Enumeration enum1=v.elements(); while(enum1.hasMoreElements()) {
		 * Element tempEle=(Element)enum1.nextElement(); e.remove(tempEle); } }
		 * 
		 * if(cdata) { //e.appendChild(root.createCDATASection(text)); } else {
		 * //e.add(root.addElement(text)); e.addElement(text); }
		 */

		/**
		 * Document root=e.getOwnerDocument(); e.normalize();
		 * if(e.hasChildNodes()) { NodeList nl=e.getChildNodes();
		 * 
		 * This suxx: NodeList Object is changed when removing children !!! I
		 * will store all nodes that should be deleted in a Vector and delete
		 * them afterwards
		 * 
		 * int length=nl.getLength();
		 * 
		 * Vector v=new Vector(nl.getLength()); for(int i=0;i<length;i++) {
		 * if(nl.item(i) instanceof CharacterData) { v.addElement(nl.item(i)); } }
		 * Enumeration enum1=v.elements(); while(enum1.hasMoreElements()) { Node
		 * n=(Node)enum1.nextElement(); e.removeChild(n); } }
		 * 
		 * if(cdata) { e.appendChild(root.createCDATASection(text)); } else {
		 * e.appendChild(root.createTextNode(text)); }
		 */
	}

	//获取某个Element下某个Tag的内容
	public static String getTagValue(Element e, String tagName) {
		List namel = e.elements(tagName);
		if (namel.size() > 0) {
			return getElementTextValue((Element) namel.get(0));
		} else {
			return null;
		}

		/**
		 * NodeList namel=e.getElementsByTagName(tagname);
		 * if(namel.getLength()>0) { return
		 * getElementTextValue((Element)namel.item(0)); } else { return null; }
		 */
	}

	/**
	 * Set the value of the first tag below e with name tagname to text.
	 */

	public static void setTagValue(Element e, String tagname, String text) {
		setTagValue(e, tagname, text, false);
	}

	public static void setTagValue(Element e, String tagname, String text, boolean cdata) {
		try {
			setTagValue(e, tagname, text, false, "", cdata);
		} catch (Exception ex) {
		}
	}

	public static void setTagValue(Element e, String tagname, String text, boolean unique, String errormsg)
			throws Exception {
		setTagValue(e, tagname, text, unique, errormsg, false);
	}

	public static void setTagValue(Element e, String tagName, String text, boolean unique, String errormsg,
			boolean cdata) throws Exception {

		if (text == null || tagName == null)
			throw new NullPointerException("Text or Tagname may not be null!");

		// Document root = e.getDocument();

		/**
		 * if (unique) { // Check for double entries! List ls =
		 * e.elements(tagName);
		 * 
		 * if (!ls.isEmpty()) { String tempName = getElementTextValue((Element)
		 * ls.get(0)); if (tempName.equals(text)) { throw new
		 * Exception(errormsg); } } }
		 */

		List ls = e.elements(tagName);
		Element elem;
		if (ls.isEmpty()) {
			System.err.println("Creating Element " + tagName + "; will set to " + text);

			// elem = root.addElement(tagName);
			elem = e.addElement(tagName);
		} else {
			elem = (Element) ls.get(0);
		}

		/**
		 * NodeList namel=e.getElementsByTagName(tagname); Element elem;
		 * if(namel.getLength()<=0) { System.err.println("Creating Element
		 * "+tagname+"; will set to "+text); elem=root.createElement(tagname);
		 * e.appendChild(elem); } else { elem=(Element)namel.item(0); }
		 */

		// debugXML(root);
		setElementTextValue(elem, text, cdata);
	}

	public static void genericRemoveAll(Element parent, String tagname) {
		List ls = parent.elements();
		Vector parts = new Vector();
		for (Iterator it = ls.iterator(); it.hasNext();) {
			Element elem = (Element) it.next();
			if (elem.getName().equals(tagname)) {
				parts.add(elem);
			}
		}

		Enumeration enum1 = parts.elements();
		while (enum1.hasMoreElements()) {
			parent.remove((Element) enum1.nextElement());
		}

		/**
		 * NodeList nl=parent.getChildNodes(); Vector parts=new Vector();
		 * for(int i=0;i<nl.getLength();i++) { if(nl.item(i) instanceof
		 * Element) { Element elem=(Element)nl.item(i);
		 * if(elem.getTagName().equals(tagname)) { parts.addElement(elem); } } }
		 * Enumeration enum1=parts.elements(); while(enum1.hasMoreElements()) {
		 * parent.removeChild((Node)enum1.nextElement()); }
		 */
	}

	// 把xml内容写入到文件
	public static void writeXML(Document d, OutputStream os, String sysID) throws IOException {
		// Modified by exce, start
		
		/** 把XML输出查看 */
		/**
		 try { 
			 Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"), "GBK"); 
			 OutputFormat format = OutputFormat.createPrettyPrint(); 
			 // 指定XML编码
			 format.setEncoding("GBK");
		
			 XMLWriter writer = new XMLWriter(out, format);
			 writer.write(d); out.flush(); out.close(); 
		} catch  (Exception ex) { }
		*/

		/*
		 * To support i18n, we have to specify the encoding of output writer to
		 * UTF-8 when we writing the XML.
		 */
		PrintWriter out = new PrintWriter(new OutputStreamWriter(os, "GBK"));
		//out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		
		out.println("<?xml version=\"1.0\" encoding=\"gb2312\"?>");
		out.println();
		// out.println("<!DOCTYPE "+d.getDocType().getName()+" SYSTEM
		// \""+sysID+"\">");
		out.println("<!DOCTYPE SYSDATA " + " SYSTEM \"" + sysID + "\">");

		out.println();

		writeXMLwalkTree(d.getRootElement(), 0, out);
		out.flush();

		/**
		 * // PrintWriter out=new PrintWriter(os); PrintWriter out = new
		 * PrintWriter(new OutputStreamWriter(os, "UTF-8"));
		 * 
		 * out.println("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
		 * out.println(); out.println("<!DOCTYPE "+d.getDocType().getName()+"
		 * SYSTEM \""+sysID+"\">"); out.println();
		 * //d.getDocumentElement().normalize();
		 * writeXMLwalkTree(d.getDocumentElement(),0,out); out.flush();
		 */
	}

	// 递归把xml文档的内容写入文件
	protected static void writeXMLwalkTree(Element ele, int indent, PrintWriter out) {
		if (ele == null) {
			System.err.println("??? Element was null ???");
			return;
		}

		if (ele.elements().size() > 0) {
			out.print("\n");
			for (int j = 0; j < indent; j++) {
				out.print(" ");
			}

			// element的名称
			out.print("<" + ele.getName());

			// element的属性
			List ls = ele.attributes();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				Attribute attr = (Attribute) it.next();
				out.print(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
			}
			out.print(">");

			// element的子元素
			List childs = ele.elements();
			for (Iterator itChild = childs.iterator(); itChild.hasNext();) {
				Element child = (Element) itChild.next();
				writeXMLwalkTree(child, indent + 2, out);
			}

			// element名称后缀
			out.println("</" + ele.getName() + ">");

		} else {
			out.print("\n");
			for (int j = 0; j < indent; j++) {
				out.print(" ");
			}

			// element的名称
			out.print("<" + ele.getName());
			// element的属性
			List ls = ele.attributes();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				Attribute attr = (Attribute) it.next();
				out.print(" " + attr.getName() + "=\"" + attr.getValue() + "\"");
			}

			String text = ele.getText();
			if (text == null || text.equals("")) {
				// 空元素
				out.print("/>");
			} else {
				out.print(">");
				out.print(text);
				out.print("</" + ele.getName() + ">");
			}
		}
	}

	/**
	 * This is a helper function to deal with problems that occur when importing
	 * Nodes from JTidy Documents to Xerces Documents.
	 */

	/**
	 * public static Node importNode(Document d, Node n, boolean deep) { Node r =
	 * cloneNode(d, n); if (deep) { NodeList nl = n.getChildNodes(); for (int i =
	 * 0; i < nl.getLength(); i++) { Node n1 = importNode(d, nl.item(i), deep);
	 * r.appendChild(n1); } } return r; }
	 * 
	 * public static Node cloneNode(Document d, Node n) { Node r = null; switch
	 * (n.getNodeType()) { case Node.TEXT_NODE: r = d.createTextNode(((Text)
	 * n).getData()); break; case Node.CDATA_SECTION_NODE: r =
	 * d.createCDATASection(((CDATASection) n).getData()); break; case
	 * Node.ELEMENT_NODE: r = d.createElement(((Element) n).getTagName());
	 * NamedNodeMap map = n.getAttributes(); for (int i = 0; i <
	 * map.getLength(); i++) { ((Element) r).setAttribute(((Attr)
	 * map.item(i)).getName(), ((Attr) map.item(i)).getValue()); } break; }
	 * return r; }
	 */

	/**
	 * public static synchronized void debugXML(Document d) { try {
	 * FileOutputStream fout=new
	 * FileOutputStream("/tmp/webmail.xml."+System.currentTimeMillis());
	 * PrintWriter out=new PrintWriter(fout); out.println("Debugging XML:");
	 * out.println("==============================================================");
	 * 
	 * writeXML(d,fout,"test"); // OutputFormat of=new
	 * OutputFormat(Method.XML,"ISO-8859-1",true); // of.setIndenting(true); //
	 * of.setIndent(2); // of.setDoctype(null,d.getDoctype().getName()); //
	 * of.setStandalone(false); // System.err.println("Doctype
	 * system:"+of.getDoctypeSystem()); // XMLSerializer ser=new
	 * XMLSerializer(System.out,of); // ser.serialize(d.getDocumentElement());
	 * out.println("==============================================================");
	 * fout.flush(); fout.close(); } catch(Exception ex) { ex.printStackTrace(); } }
	 */
}
