package com.kwchina.webmail.xml;

import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;
import java.util.Vector;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Text;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.dom4j.tree.DefaultText;

/*
 * XMLMessagePart.java
 *
 */

/**
 * A message part object for an XML message
 */
public class XMLMessagePart {

	protected Document root;

	protected Element part;

	/**
	 * Create a new part for the given root document. Creates the necessary
	 * Element.
	 */
	public XMLMessagePart(Document root) {
		//this.part = root.createElement("PART");
		this.part = new DefaultElement("PART");
				
		this.root = root;
	}

	/**
	 * Return a new part for a given part element
	 */
	public XMLMessagePart(Element part) {
		this.part = part;
		//this.root = part.getOwnerDocument();
		this.root = part.getDocument();
	}

	public Element getPartElement() {
		return part;
	}

	public void setAttribute(String key, String value) {
		XMLCommon.setAttributeValue(part,key,value);		
		//part.setAttribute(key, value);
	}

	public String getAttribute(String key) {
		//return part.getAttribute(key);
		return part.attributeValue(key);
	}

	public void quoteContent() {
		StringBuffer text = new StringBuffer();
		
		List ls = part.elements();
		for(Iterator it = ls.iterator();it.hasNext();){
			Element elem = (Element) it.next();
			
			String value = XMLCommon.getElementTextValue(elem);
			StringTokenizer tok = new StringTokenizer(value, "\n");
			while (tok.hasMoreTokens()) {
				text.append("> ").append(tok.nextToken()).append("\n");
			}
		}
		
		
		/**
		NodeList nl = part.getChildNodes();
		StringBuffer text = new StringBuffer();
		for (int i = 0; i < nl.getLength(); i++) {
			Element elem = (Element) nl.item(i);
			if (elem.getNodeName().equals("CONTENT")) {
				String value = XMLCommon.getElementTextValue(elem);
				StringTokenizer tok = new StringTokenizer(value, "\n");
				while (tok.hasMoreTokens()) {
					text.append("> ").append(tok.nextToken()).append("\n");
				}
			}
		}
		*/
		removeAllContent();

		addContent(text.toString(), 0);
	}

	/**
	 * This method is designed for content that already is in DOM format, like
	 * HTML messages.
	 */
	public void addContent(Document content) {
		Element content_elem = new DefaultElement("CONTENT");
		content_elem.addAttribute("quotelevel", "0");
		
		//Element content_elem = root.createElement("CONTENT");
		//content_elem.setAttribute("quotelevel", "0");

		/* Find all <BODY> elements and add the child nodes to the content */
		//for (int count = 0; count < 2; count++) {
			//String bodyName = count == 0 ? "BODY" : "body";
			/**@todo check upping sentence */
			String bodyName = "BODY";
			Element rootEle = content.getRootElement();
			
			//content_elem.add(rootEle.createCopy());
			
			
			ArrayList ls = XMLCommon.getElementsByName(rootEle,bodyName);	
			
			System.err.println("While parsing HTML content: Found "	+ ls.size() + " body elements");
			for(Iterator it = ls.iterator();it.hasNext();){
				Element tempBody = (Element)it.next();
				List childs = tempBody.content();
				
				//List childs = ((Element)it.next()).elements();
				System.err.println("While parsing HTML content: Found " + childs.size() + " child elements");
								
				for(Iterator it2 = childs.iterator();it2.hasNext();){
					//把content中的信息复制到content_elem下面
					Object obj = it2.next();
					if(obj instanceof DefaultText){
						DefaultText tpEl = (DefaultText)obj;
						
						content_elem.add((Text)tpEl.clone());
					}else if (obj instanceof DefaultElement){
						Element tpEl = (Element)obj;
						content_elem.add(tpEl.createCopy());
					}					
				}
			}
			
			/**
			for (int i = 0; i < nl.getLength(); i++) {
				NodeList nl2 = nl.item(i).getChildNodes();
				System.err.println("While parsing HTML content: Found "
						+ nl2.getLength() + " child elements");
				for (int j = 0; j < nl2.getLength(); j++) {
					System.err.println("Element: " + j);
					
					content_elem.appendChild(XMLCommon.importNode(root, nl2
							.item(j), true));
				}
			}*/
		//}

		//part.appendChild(content_elem);
		part.add(content_elem);

		// XMLCommon.debugXML(root);
	}

	public void addContent(String content, int quotelevel) {
		/**
		Element content_elem = root.createElement("CONTENT");
		content_elem.setAttribute("quotelevel", quotelevel + "");
		XMLCommon.setElementTextValue(content_elem, content, true);
		part.appendChild(content_elem);
		*/
		
		Element content_elem = new DefaultElement("CONTENT");
		content_elem.addAttribute("quotelevel", quotelevel + "");
		
		
		//XMLCommon.setElementTextValue(content_elem, content, true);
		XMLCommon.setElementTextValue(content_elem, content, false);
		
		part.add(content_elem);
	}

	public void insertContent(String content, int quotelevel) {
		/**		 
		Element content_elem = root.createElement("CONTENT");
		content_elem.setAttribute("quotelevel", quotelevel + "");
		XMLCommon.setElementTextValue(content_elem, content, true);
		Node first = part.getFirstChild();
		part.insertBefore(content_elem, first);
		*/
		Element content_elem = new DefaultElement("CONTENT");
		content_elem.addAttribute("quotelevel", quotelevel + "");
		
		XMLCommon.setElementTextValue(content_elem, content, true);
		
		
		Element first = (Element)part.elements().get(0);
		
		List partElements = part.elements();
		partElements.add(partElements.indexOf(first),content_elem);
				
		//Node first = part.getFirstChild();
		//part.insertBefore(content_elem, first);
	}

	public void addJavaScript(String content) {
		Element javascript_elem = new DefaultElement("JAVASCRIPT");
		XMLCommon.setElementTextValue(javascript_elem, content, true);
		part.add(javascript_elem);
	}

	public void removeAllContent() {
		XMLCommon.genericRemoveAll(part, "CONTENT");
	}

	public XMLMessagePart createPart(String type) {
		XMLMessagePart newpart = new XMLMessagePart(root);
		newpart.setAttribute("type", type);
		appendPart(newpart);
		
		return newpart;
	}

	public void insertPart(XMLMessagePart childpart) {
		Element first = (Element)part.elements().get(0);
		
		List partElements = part.elements();
		partElements.add(partElements.indexOf(first),childpart.getPartElement());
		/**
		Node first = part.getFirstChild();
		part.insertBefore(childpart.getPartElement(), first);
		*/
	}

	public void appendPart(XMLMessagePart childpart) {
		//part.appendChild(childpart.getPartElement());
				
		Element ele = childpart.getPartElement();
		if(ele.getParent()!=null){
			Element copyPart = ele.createCopy();
			part.add(copyPart);
		}else{
			part.add(childpart.getPartElement());
		}
		
	}

	public Enumeration getParts() {
		// Sucking NodeList needs a Vector to store Elements that will be
		// removed!
		Vector v = new Vector();
		List parts = part.elements();
		
		/**
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_3.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(part);
			out.flush();
			out.close();
		}catch(Exception ex){
			
		}
		*/		
		
		for (int j = 0; j < parts.size(); j++) {
			Element elem = (Element) parts.get(j);
			if (elem.getName().equals("PART")) {
				v.addElement(new XMLMessagePart(elem));
			}
		}
		return v.elements();
	}

	public void removePart(XMLMessagePart childpart) {
		//part.removeChild(childpart.getPartElement());
		part.remove(childpart.getPartElement());
	}

	public void removeAllParts() {
		XMLCommon.genericRemoveAll(part, "PART");
	}

} 
