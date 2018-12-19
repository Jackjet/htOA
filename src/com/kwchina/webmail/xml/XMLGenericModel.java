package com.kwchina.webmail.xml;

import java.io.CharArrayWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Enumeration;
import java.util.List;
import java.util.Vector;

import javax.xml.parsers.ParserConfigurationException;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.Node;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.w3c.dom.DOMException;

import com.kwchina.webmail.server.WebMailServer;


/*
 * XMLGenericModel.java
 */

/**
 * A generic representation of WebMail data. Contains mainly state information
 * and the system configuration
 * 
 */

public class XMLGenericModel {

	protected Document root;

	protected Element sysdata;

	protected Element statedata;

	protected WebMailServer parent;

	protected long current_id = 0;

	protected  SAXReader reader; 
	


	public XMLGenericModel(WebMailServer parent, Element rsysdata)
			throws ParserConfigurationException {		
		/**把XML输出查看*/
		/**
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test_1.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(rsysdata);
			out.flush();
			out.close();
		}catch(Exception ex){
			
		}	*/	
		
		//WebMailServer
		this.parent = parent;

		//SAX Parser
		reader = new SAXReader();		

		//获取到Root Element
		initRoot();

		//添加STATEDATA Element
		//statedata = new DefaultElement("STATEDATA");

		List ls = root.getRootElement().elements("STATEDATA");		
		//NodeList nl = root.getDocumentElement().getElementsByTagName("STATEDATA");
		if (!ls.isEmpty()) {
			//如果有该节点，则先替换掉
			/**@todo:更换节点*/
			System.err
					.println("*** Warning: Webmail usermodel template contained a STATEDATA tag, although this should only be created at runtime!");
			  List   elepar   =  root.getRootElement().content();   
		      elepar.set(elepar.indexOf(ls.get(0)),statedata);   
		      statedata = (Element)ls.get(0);

		      //root.getRootElement().replaceChild(statedata, ls.get(0));
		} else {			
			//root.getRootElement().add(statedata);
			statedata = root.getRootElement().addElement("STATEDATA");		
		}

		this.sysdata = rsysdata;
	}

	protected void initRoot() {
		// Create a new usermodel from the template file
		try {
			String file = // "file://"+
			parent.getProperty("webmail.xml.path")
					+ System.getProperty("file.separator")
					+ "generic_template.xml";
			
			root =reader.read(file);  				
		} catch (Exception ex) {
			System.err.println("Error parsing WebMail UserModel template "
					+ ex.getMessage());
			ex.printStackTrace();
		}
	}

	public Document getRoot() {
		return root;
	}

	public Element getStateData() {
		return statedata;
	}

	public void init() {
		
		setStateVar("base uri", parent.getBasePath());
		setStateVar("img base uri", parent.getImageBasePath()); 
				//+ "/" + parent.getDefaultLocale().getLanguage() + "/"
				//+ parent.getDefaultTheme());
		setStateVar("webmail version", parent.getVersion());
		setStateVar("operating system", System.getProperty("os.name") + " "
				+ System.getProperty("os.version") + "/"
				+ System.getProperty("os.arch"));
		setStateVar("java virtual machine", System.getProperty("java.vendor")
				+ " " + System.getProperty("java.vm.name") + " "
				+ System.getProperty("java.version"));						
	}

	//更新STATEDATA部分
	public void updateStateData(){
		List ls = root.getRootElement().selectNodes("STATEDATA");
		if(!ls.isEmpty()){
			Element ele = (Element)ls.get(0);
			root.getRootElement().remove(ele);
		}
		
		Element copySys = statedata.createCopy();
		root.getRootElement().add(copySys); 
	}
	
	
	//更新SYSDATA部分
	public void update() {		
		/**把XML输出查看*/	
		/**
		try{
			Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test.xml"),"GBK");
			OutputFormat format = OutputFormat.createPrettyPrint();
			//指定XML编码 
	        format.setEncoding("GBK");           

			XMLWriter writer = new XMLWriter(out, format);
			writer.write(root.getRootElement());
			out.flush();
			out.close();	
		}catch(Exception ex){
			
		}*/
		
		// Insert the sysdata and userdata objects into the usermodel tree
		try {
			//把sysdata放到usermodel
			List ls = root.getRootElement().selectNodes("SYSDATA");
			if(!ls.isEmpty()){
				Element ele = (Element)ls.get(0);
				root.getRootElement().remove(ele);
			}
						
			//List ls_1 = sysdata.selectNodes("SYSDATA");						
			//root.getRootElement().add((Element)ls_1.get(0));
			/**@todo：复制节点
			 *root.getRootElement().add(sysdata);
			 * (将一个Document的部分元素复制到另一个Document中，但dom4j中直接add会产生“The Node already has an existing parent”异常)*/
			//root.add(sysdata.detach()); //如果无需保留原文档对象
			Element copySys = sysdata.createCopy();
			root.getRootElement().add(copySys); //如果必需保留原对象
				
			/**
			try{
				Writer out = new OutputStreamWriter(new FileOutputStream("c:\\test.xml"),"GBK");
				OutputFormat format = OutputFormat.createPrettyPrint();
				//指定XML编码 
		        format.setEncoding("GBK");           

				XMLWriter writer = new XMLWriter(out, format);
				writer.write(root.getRootElement());
				out.flush();
				out.close();	
			}catch(Exception ex){
				
			}		
			*/			
			
			/**
			NodeList nl = root.getElementsByTagName("SYSDATA");
			root.getDocumentElement().replaceChild(
					root.importNode(sysdata, true), nl.item(0));
					*/
		} catch (ArrayIndexOutOfBoundsException ex) {
			System.err
					.println("The WebMail GenericModel template file didn't contain a SYSDATA tag.");
		} catch (DOMException ex) {
			System.err
					.println("Something went wrong with the XML generic model.");
		}
	}

	/**
	 * Create a unique ID. Important: This returns a new Unique ID within this
	 * session. It should be used to generate IDs for Folders and messages so
	 * that they can be easily referenced
	 */
	public String getNextID() {
		return Long.toHexString(++current_id).toUpperCase();
	}

	
	
	public synchronized void setException(Exception ex) {
		Element exception = new DefaultElement("EXCEPTION");
		Element ex_message = new DefaultElement("EX_MESSAGE");
		Element ex_stacktrace =  new DefaultElement("EX_STACKTRACE");
		
		exception.add(ex_message);
		exception.add(ex_stacktrace);
		
		ex_message.addText(ex.getMessage());

		/**
		Text msg = root.createTextNode(ex.getMessage());
		ex_message.appendChild(msg);
		*/

		String my_stack = "";
		CharArrayWriter cstream = new CharArrayWriter();
		ex.printStackTrace(new PrintWriter(cstream));
		my_stack = cstream.toString();
		ex_stacktrace.addText(my_stack);
		
		/**
		CDATASection stack = root.createCDATASection(my_stack);
		ex_stacktrace.appendChild(stack);
		*/

		List ls = statedata.elements("EXCEPTION");
		if (ls.size() > 0) {
			Element tempEl = (Element)ls.get(0);
			tempEl.detach();
			statedata.remove(tempEl);
			
			statedata.add(tempEl);
						
			//statedata.replaceChild(exception, nl.item(0));
		} else {
			statedata.add(exception);
		}

		// XMLCommon.debugXML(root);
	}
	

	/**
	 * We need to synchronize that to avoid problems, but this should be fast
	 * anyway
	 */	
	public synchronized void setStateVar(String name, String value) {		
		Element var = XMLCommon.getElementByAttribute(statedata, "VAR", "name",	name);
		if (var == null) {
			var = statedata.addElement("VAR");
			var.addAttribute("name", name);			
		}
		
		var.addAttribute("value", value);
	}

	public Element createStateVar(String name, String value) {
		Element var = new DefaultElement("VAR");
		var.addAttribute("name", name);
		var.addAttribute("value", value);
		
		return var;
	}

	public void addStateVar(String name, String value) {
		Element var = statedata.addElement("VAR");
		var.addAttribute("name", name);			
		
		/**
		statedata.appendChild(var);
		var.setAttribute("value", value);
		*/
	}

	/**
	 * We need to synchronize that because it can cause problems with multiple
	 * threads
	 */	
	public synchronized void removeAllStateVars(String name) {
		List nl = statedata.elements("VAR");

		/*
		 * This suxx: NodeList Object is changed when removing children !!! I
		 * will store all nodes that should be deleted in a Vector and delete
		 * them afterwards
		 */
		int length = nl.size();
		Vector v = new Vector(nl.size());
		for (int i = 0; i < length; i++) {
			if (((Element) nl.get(i)).attributeValue("name").equals(name)) {
				v.addElement(nl.get(i));
			}
		}
		
		Enumeration enum1 = v.elements();
		while (enum1.hasMoreElements()) {
			/**@todo Node和Element区别*/
			Node n = (Node) enum1.nextElement();
			statedata.remove(n);
		}
	}

	//从StateData中，根据Attribute名字获取value
	public String getStateVar(String name) {
		Element var = XMLCommon.getElementByAttribute(statedata, "VAR", "name",	name);
		if (var == null) {
			return "";
		} else {
			return var.attributeValue("value");
		}
	}

} 
