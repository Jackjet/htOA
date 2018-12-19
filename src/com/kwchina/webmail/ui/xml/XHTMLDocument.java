package com.kwchina.webmail.ui.xml;

import java.io.File;
import java.io.StringWriter;

import javax.xml.transform.Templates;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.dom4j.Document;
import org.dom4j.io.DocumentResult;
import org.dom4j.io.DocumentSource;
import org.dom4j.io.OutputFormat;

import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.storage.Storage;

/**
 * XHTMLDocument.java
 *
 */

/**
 * Constructs HTML-Documents using a Stylesheet and a XML Document.
 * 利用样式表和XML文件，生成一个HTML文档
 */

public class XHTMLDocument extends com.kwchina.webmail.ui.html.HTMLDocument {

	public XHTMLDocument(Document xml, String xsl) throws WebMailException {
		
		/**@todo:重点查看如下代码，包括注释的*/
		Document transformedDoc = null;
		long start_t = System.currentTimeMillis();

		StringWriter writer = new StringWriter();
		try {
			
			TransformerFactory tFactory = TransformerFactory.newInstance();

			StreamSource msg_xsl = new StreamSource(xsl);
            Transformer transformer = tFactory.newTransformer(msg_xsl);
            transformer.setOutputProperty("encoding","gb2312");
            
            DocumentSource source = new DocumentSource(xml);
            //StreamSource source = new StreamSource();
            
            StreamResult result = new StreamResult(writer);
            transformer.transform(source,result);                     
            
            /**            
			TransformerFactory factory = TransformerFactory.newInstance();			
			Transformer transformer = factory.newTransformer(msg_xsl);
			
			Properties props = transformer.getOutputProperties();    
            props.setProperty(OutputKeys.ENCODING,"GBK");    
            props.setProperty(OutputKeys.METHOD,"html");    
            props.setProperty(OutputKeys.VERSION,"4.0");    
                
            //props.list(System.out);    
            transformer.setOutputProperties(props); 
            
			 transformer.setOutputProperty("encoding","gb2312");

            
			// now lets style the given document
			DocumentSource source = new DocumentSource(xml);
			
			DocumentResult result = new DocumentResult();

			transformer.transform(source, result);

			// return the transformed document
			transformedDoc = result.getDocument();
			*/		
			
			/**
			DOMSource msg_xml = new DOMSource((Node) xml);
			// StreamSource msg_xsl=new StreamSource("file://"+xsl);

			StreamSource msg_xsl = new StreamSource(xsl);
			StreamResult msg_result = new StreamResult(writer);

			TransformerFactory factory = TransformerFactory.newInstance();

			Transformer processor = factory.newTransformer(msg_xsl);

			// processor.setDiagnosticsOutput(System.err);
			processor.transform(msg_xml, msg_result);
			*/
			
		} catch (Exception ex) {
			System.err.println("Error transforming XML with " + xsl
					+ " to XHTML.");
			WebMailServer.getStorage().log(Storage.LOG_INFO,
					"Error transforming XML with " + xsl + " to XHTML.");
			throw new WebMailException(ex.getMessage());
		}

		long end_t = System.currentTimeMillis();
		// System.err.println("Transformation XML --> XHTML took
		// "+(end_t-start_t)+" ms.");
		WebMailServer.getStorage().log(
				Storage.LOG_DEBUG,
				"Transformation XML --> XHTML took " + (end_t - start_t)
						+ " ms.");
		content = writer.toString();
		
		/**
		try{
			StringWriter sw = new StringWriter(); 
			OutputFormat format = OutputFormat.createPrettyPrint(); 
	        //These are the default formats from createPrettyPrint, so you needn't set them: 
	        //  format.setNewlines(true); 
	        //  format.setTrimText(true); 
	        //format.setXHTML(true);  //Default is false, this produces XHTML 
	        format.setEncoding("GBK");//
	        
	        org.dom4j.io.HTMLWriter writer = new org.dom4j.io.HTMLWriter(sw, format);         
	        writer.write(transformedDoc); 
	        writer.flush(); 
	        content = sw.toString(); 
		}catch(Exception ex){
			
		}
		*/
		//if (transformedDoc != null) {
			//content = transformedDoc.asXML();
			/**@todo 下面语句输出为空 */
			//content = transformedDoc.getText();
		//}
	}

	public XHTMLDocument(Document xml, Templates stylesheet)
			throws WebMailException {

		Document transformedDoc = null;
		long start_t = System.currentTimeMillis();
		try {

			/**
			 * DocumentBuilderFactory domfac =
			 * DocumentBuilderFactory.newInstance(); DocumentBuilder dombuilder =
			 * domfac.newDocumentBuilder(); String content = msg_xml.toString();
			 * String xmlContent = xml.toString();
			 * 
			 * Element root = xml.getDocumentElement();
			 * System.out.println(root.toString());
			 */

			/** log:Author:zlb */
			// 把内存中的Document保存为xml文件
			/**
			 * TransformerFactory tFactory=TransformerFactory.newInstance();
			 * Transformer transformer=tFactory.newTransformer();
			 * //设置输出的encoding为改变gb2312
			 * transformer.setOutputProperty("encoding","gb2312"); StreamResult
			 * result = new StreamResult("c:\\test.xml");
			 * transformer.transform(msg_xml,result);
			 */	

			/**
			 * StringWriter writer = new StringWriter(); DOMSource msg_xml = new
			 * DOMSource((Node) xml); StreamResult msg_result = new
			 * StreamResult(writer); Transformer processor =
			 * stylesheet.newTransformer(); processor.transform(msg_xml,
			 * msg_result); content = writer.toString();
			 */

			Transformer processor = stylesheet.newTransformer();

			// now lets style the given document
			DocumentSource source = new DocumentSource(xml);
			DocumentResult result = new DocumentResult();

			processor.transform(source, result);

			// return the transformed document
			transformedDoc = result.getDocument();

		} catch (Exception ex) {
			System.err.println("Error transforming XML to XHTML.");
			ex.printStackTrace();
			throw new WebMailException(ex.toString());
		}

		long end_t = System.currentTimeMillis();
		// System.err.println("Transformation (with precompiled stylesheet) XML
		// --> XHTML took "+(end_t-start_t)+" ms.");
		WebMailServer.getStorage().log(
				Storage.LOG_DEBUG,
				"Transformation (with precompiled stylesheet) XML --> XHTML took "
						+ (end_t - start_t) + " ms.");

		if (transformedDoc != null) {
			content = transformedDoc.toString();
		}
	}

	public String toString() {
		return content;
	}

	public int length() {
		return content.length();
	}

}
