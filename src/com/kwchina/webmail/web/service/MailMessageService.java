package com.kwchina.webmail.web.service;

import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Address;
import javax.mail.BodyPart;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.InternetHeaders;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;

import com.kwchina.webmail.exception.DocumentNotFoundException;
import com.kwchina.webmail.exception.NoSuchFolderException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.misc.StreamConnector;
import com.kwchina.webmail.server.WebMailServer;
import com.kwchina.webmail.server.WebMailSession;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.storage.Storage;
import com.kwchina.webmail.util.MessageParserCommon;
import com.kwchina.webmail.util.TranscodeUtil;
import com.kwchina.webmail.web.bean.MailFolder;
import com.kwchina.webmail.web.bean.MailMessageInfor;
import com.kwchina.webmail.web.bean.MailMessagePart;
import com.kwchina.webmail.xml.XMLCommon;
import com.kwchina.webmail.xml.XMLMessage;
import com.kwchina.webmail.xml.XMLMessagePart;
import com.kwchina.webmail.xml.XMLUserModel;

public class MailMessageService extends MailBasicService {

	// 获取某个邮件的信息
	public static void getMailMessageInfor(HttpServletRequest request, HTTPRequestHeader header) {

		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");

		int nr = 1;
		try {
			nr = Integer.parseInt(header.getContent("serialNo"));
		} catch (Exception e) {
		}

		try {

			XMLUserModel userModel = mailSession.getUserModel();

			// 获取当前folder的Id
			Element currentFolder = userModel.getCurrentFolder("");
			String folderId = currentFolder.attributeValue("id");

			// 获取Message到XML
			mailSession.getMessage(folderId, nr);

			Element currentMessage = userModel.getCurrentMessage("");
			String messageId = currentMessage.attributeValue("id");

			// 获取当前的Message
			Element xml_folder = userModel.getFolder(folderId);
			XMLMessage xml_message = userModel.getMessage(xml_folder, "", messageId);
			Element eleMessage = xml_message.getMessageElement();
			int serialNo = XMLCommon.getAttributeIntValue(eleMessage, "msgnr");

			MailMessageInfor message = getMailMessage(eleMessage, true);
			request.setAttribute("_Mail_Message", message);

			// 递归获取Message各部分内容
			Enumeration emu = xml_message.getParts();
			ArrayList messageParts = new ArrayList();
			while (emu.hasMoreElements()) {
				XMLMessagePart xmlPart = (XMLMessagePart) emu.nextElement();
				getMessagePart(messageParts, xmlPart, null, serialNo);
			}
			request.setAttribute("_Message_Parts", messageParts);

			MailFolder mailFolder = getFolderInfor(request, folderId);
			request.setAttribute("_Mail_Folder", mailFolder);

		} catch (NoSuchFolderException e) {
			// throw new DocumentNotFoundException("Could not find folder " +
			// folderId + "!");
			e.printStackTrace();
		}

		/** 把XML输出查看 */
		/**
		try{ 
			Writer out = new OutputStreamWriter(new
			FileOutputStream("c:\\test_1.xml"),"GBK"); 
			OutputFormat format = OutputFormat.createPrettyPrint(); //指定XML编码
			format.setEncoding("GBK");
		  
			XMLWriter writer = new XMLWriter(out, format);
			writer.write(mailSession.getModel()); out.flush(); out.close();
		 }catch(Exception ex){
		 }
		 */				
	}

	/**
	 * 邮件回复
	 * param mode
	 * 	0- 编写
	 *  1- 回复
	 *  2- 转发
	 */ 
	public static void getWriteInfor(HttpServletRequest request, int mode) {

		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");

		try {
			XMLUserModel userModel = mailSession.getUserModel();

			if (mode == 0) {
				mailSession.clearAttachments();
				mailSession.clearWork();
				mailSession.prepareCompose();
				mailSession.setEnv();

				// 从WORK MESSAGE中获取相关转发的信息
				XMLMessage workMessage = userModel.getWorkMessage();
				// Element eleM = workMessage.getMessageElement();

				// 递归获取Message各部分内容
				Enumeration emu = workMessage.getParts();
				ArrayList messageParts = new ArrayList();
				while (emu.hasMoreElements()) {
					XMLMessagePart xmlPart = (XMLMessagePart) emu.nextElement();
					getMessagePart(messageParts, xmlPart, null, 0);
				}

				request.setAttribute("_Work_Parts", messageParts);
			} else {
				// 获取当前folder的Id
				Element currentFolder = userModel.getCurrentFolder("");
				String folderId = currentFolder.attributeValue("id");

				// 获取当前Message的id
				Element currentMessage = userModel.getCurrentMessage("");
				String messageId = currentMessage.attributeValue("id");

				// 获取当前的Message
				Element xml_folder = userModel.getFolder(folderId);
				XMLMessage xml_message = userModel.getMessage(xml_folder, "", messageId);
				Element eleMessage = xml_message.getMessageElement();
				//邮件的序号，不能使用Work下的msgnr,那下面的为0
				int serialNo = XMLCommon.getAttributeIntValue(eleMessage, "msgnr");

				MailMessageInfor message = getMailMessage(eleMessage, true);
				request.setAttribute("_Old_Message", message);

				mailSession.clearAttachments();
				mailSession.clearWork();

				// 获取Message到XML
				if (mode == 1)
					mailSession.getMessage(folderId, serialNo, mailSession.GETMESSAGE_MODE_REPLY);
				else
					mailSession.getMessage(folderId, serialNo, mailSession.GETMESSAGE_MODE_FORWARD);

				MailFolder mailFolder = getFolderInfor(request, folderId);
				request.setAttribute("_Mail_Folder", mailFolder);

				// 从WORK MESSAGE中获取相关转发的信息
				XMLMessage workMessage = userModel.getWorkMessage();
				Element eleM = workMessage.getMessageElement();

				MailMessageInfor reMessage = getMailMessage(eleM, true);
				request.setAttribute("_Work_Message", reMessage);

				// 递归获取Message各部分内容
				Enumeration emu = workMessage.getParts();
				ArrayList messageParts = new ArrayList();
				while (emu.hasMoreElements()) {
					XMLMessagePart xmlPart = (XMLMessagePart) emu.nextElement();

					getMessagePart(messageParts, xmlPart, null, serialNo);
				}
				request.setAttribute("_Work_Parts", messageParts);
			}

		} catch (NoSuchFolderException e) {
			// throw new DocumentNotFoundException("Could not find folder " +
			// folderId + "!");
			e.printStackTrace();
		}

		/** 把XML输出查看 */
		/**
		 * try{ Writer out = new OutputStreamWriter(new
		 * FileOutputStream("c:\\test_1.xml"),"GBK"); OutputFormat format =
		 * OutputFormat.createPrettyPrint(); //指定XML编码
		 * format.setEncoding("GBK");
		 * 
		 * XMLWriter writer = new XMLWriter(out, format);
		 * writer.write(mailSession.getModel()); out.flush(); out.close();
		 * }catch(Exception ex){
		 *  }
		 */
	}

	// 获取各个MessagePart的信息
	private static void getMessagePart(ArrayList parts, XMLMessagePart xmlPart, XMLMessagePart parentPart, int serialNo) {
		Element elePart = xmlPart.getPartElement();
		MailMessagePart messagePart = new MailMessagePart();

		String type = elePart.attributeValue("type");
		if (type.equals("multi")) {
			// multipart,递归
			Enumeration emu = xmlPart.getParts();
			while (emu.hasMoreElements()) {
				XMLMessagePart xml_part = (XMLMessagePart) emu.nextElement();
				
				getMessagePart(parts, xml_part, xmlPart, serialNo);
			}
		} else if (type.toUpperCase().equals("BINARY") || type.toUpperCase().equals("IMAGE")) {
			// 附件(文件+图片)
			messagePart.setFileName(elePart.attributeValue("filename"));
			messagePart.setHrefFileName(elePart.attributeValue("hrefFileName"));
			messagePart.setSize(elePart.attributeValue("size"));
			messagePart.setDescription(elePart.attributeValue("description"));
			messagePart.setBinaryType(elePart.attributeValue("content-type"));

			messagePart.setPartCount(XMLCommon.getAttributeIntValue(elePart, "part-count"));
			messagePart.setType(elePart.attributeValue("type"));

			parts.add(messagePart);
			
		} else {
			// text,html类型
			messagePart.setHidden(XMLCommon.getAttributeBooleanValue(elePart, "hidden"));
			messagePart.setPartCount(XMLCommon.getAttributeIntValue(elePart, "part-count"));
			messagePart.setType(elePart.attributeValue("type"));
			
			ArrayList contents = new ArrayList();
			XMLCommon.getElementsByName(contents, elePart, "CONTENT");

			String content = "";
			for (Iterator it = contents.iterator(); it.hasNext();) {
				Element ele_content = (Element) it.next();

				// 获取内容转换如&amp;为"&"符号
				String eleContent = ele_content.asXML();
				eleContent = transCodeToTag(eleContent);

				// 转换邮件中的内嵌图片信息,附件信息必然存储在父Part下的某个Part下,使用递归查找"src="cid:"的方式
				if (parentPart != null)
					eleContent = changeInsidePicPath(eleContent, parentPart, serialNo);				
				
				content += eleContent;
			}
			/**
			 * Element ele_content = XMLCommon.getDirectElementByTag(elePart,
			 * "CONTENT"); if(ele_content!=null){ String content =
			 * ele_content.asXML(); content = transCodeToTag(content);
			 * 
			 * messagePart.setContent(content); }
			 */
			messagePart.setContent(content);
			parts.add(messagePart);
		}
	}

	// 更换内嵌图片的路径
	private static String changeInsidePicPath(String content, XMLMessagePart parentPart, int mailSerialNo) {

		if (content != null && !content.equals("")) {
			// 找到parentPart下的所有附件形式的Part
			ArrayList picParts = new ArrayList();

			Enumeration emu = parentPart.getParts();
			while (emu.hasMoreElements()) {
				XMLMessagePart xmlPart = (XMLMessagePart) emu.nextElement();
				Element elePart = xmlPart.getPartElement();

				String type = elePart.attributeValue("type");
				if (type.toUpperCase().equals("BINARY") || type.toUpperCase().equals("IMAGE")) {
					picParts.add(elePart);
				}
			}

			// 找到所有内嵌图片
			// src="cid:CC93E20B3B6E4F0E9DBD3C082740A5B1@zhoulb"/>
			ArrayList picStrs = new ArrayList();
			String picAlias = "src=\"cid:";
			int pos = content.indexOf(picAlias);
			while (pos > 0) {
				int pos1 = content.indexOf("\"", pos);
				int pos2 = content.indexOf("\"", pos1 + 1);

				String picStr = content.substring(pos1 + 1, pos2);
				picStrs.add(picStr);

				pos = content.indexOf(picAlias, pos2);
			}

			// 更换内嵌图片
			try {
				int k = 0;
				for (Iterator it = picStrs.iterator(); it.hasNext();) {
					String picStr = (String) it.next();

					Element eleAttach = (Element) picParts.get(k);
					String changeStr = "showMime.do?method=list&part=";
					changeStr += XMLCommon.getAttributeIntValue(eleAttach, "part-count") + "&serialNo=" + mailSerialNo;

					// 更换
					content = content.replaceAll(picStr, changeStr);

					k += 1;
				}
			} catch (Exception ex) {
				System.out.println(ex.toString());
			}
		}

		return content;
	}

	// 发送邮件
	public static void sendMessage(HttpServletRequest request, HTTPRequestHeader header, WebMailServer webmailServer)
			throws WebMailException, ServletException {

		WebMailSession mailSession = (WebMailSession) request.getSession().getAttribute("webmail.session");

		// ----------------转发的附件处理---------------------
		boolean hasForwardAttach = false;
		Enumeration em = header.getContent().keys();
		while (em.hasMoreElements()) {
			String s = (String) em.nextElement();
			if (s.toUpperCase().startsWith("REMOVE") && header.getContent(s).equals("on")) {
				try {
					hasForwardAttach = true;
					break;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}

		if (hasForwardAttach) {
			XMLUserModel userModel = mailSession.getUserModel();

			Element currentFolder = userModel.getCurrentFolder("");
			String folderId = currentFolder.attributeValue("id");

			// 获取当前Message的id
			Element currentMessage = userModel.getCurrentMessage("");
			String messageId = currentMessage.attributeValue("id");
			// int messageSerialNo =
			// XMLCommon.getAttributeIntValue(currentMessage, "msgnr");
			Element xml_folder = userModel.getFolder(folderId);
			XMLMessage xml_message = userModel.getMessage(xml_folder, "", messageId);
			Element eleMessage = xml_message.getMessageElement();
			int serialNo = XMLCommon.getAttributeIntValue(eleMessage, "msgnr");

			int parts[] = mailSession.getForwardAttach(header, folderId);

			try {
				for (int k = 0; k < parts.length; k++) {
					BodyPart bodyPart = mailSession.getMIMEPart(folderId, serialNo, parts[k]);
					String name = MessageParserCommon.getISOFileName(bodyPart);
					
					/**
					try {
						name = MimeUtility.decodeText(name);
					} catch (Exception ex) {
						System.err.println(ex);
					}*/

					InputStream is = bodyPart.getInputStream();

					int size = Integer.parseInt(webmailServer.getStorage().getConfig("max attach size"));
					ByteStore bs = ByteStore.getBinaryFromIS(is, size);
					bs.setName(name);
					bs.setContentType(webmailServer.getStorage().getMimeType(name));

					java.util.Random r = new java.util.Random();
					header.setContent("file_1" + name + r.nextInt(), bs);
				}
			} catch (Exception ex) {

			}
		}
		// ----------------转发的附件处理-------------------

		/* Save message in case there is an error */
		mailSession.storeMessage(header);

		/** @todo */
		mailSession.setSent(false);

		try {
			Locale locale = Locale.getDefault();

			Session session;
			Properties props = new Properties();
			Storage store = webmailServer.getStorage();
			props.put("mail.smtp.host", store.getConfig("SMTP HOST"));
			props.put("mail.transport.protocol", "smtp");
			props.put("mail.smtp.auth", "true");
			session = Session.getInstance(props);
			session.setDebug(true);

			MimeMessage msg = new MimeMessage(session);

			Address from[] = new Address[1];
			try {
				// from[0] = new
				// InternetAddress(mailSession.getUser().getEmail(),mailSession.getUser().getFullName());
				from[0] = new InternetAddress(TranscodeUtil.transcodeThenEncodeByLocale(mailSession.getUser().getEmail(), "GBK",
						locale), TranscodeUtil.transcodeThenEncodeByLocale(mailSession.getUser().getFullName(), "GBK", locale));

			} catch (UnsupportedEncodingException e) {
				from[0] = new InternetAddress(mailSession.getUser().getEmail(), mailSession.getUser().getFullName());
			}

			// -----------TO-------------------------
			StringTokenizer t;
			try {
				t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(header.getContent("TO"), null, locale).trim(),
						",");
			} catch (UnsupportedEncodingException e) {
				t = new StringTokenizer(header.getContent("TO").trim(), ",;");
			}

			/* Check To: field, when empty, throw an exception */
			if (t.countTokens() < 1) {
				throw new MessagingException("The recipient field must not be empty!");
			}

			Address to[] = new Address[t.countTokens()];
			int i = 0;
			while (t.hasMoreTokens()) {
				to[i] = new InternetAddress(t.nextToken().trim());
				i++;
			}

			// -----------------CC------------------------
			try {
				t = new StringTokenizer(TranscodeUtil.transcodeThenEncodeByLocale(header.getContent("CC"), "GBK", locale).trim(),
						",");
			} catch (UnsupportedEncodingException e) {
				t = new StringTokenizer(header.getContent("CC").trim(), ",;");
			}
			Address cc[] = new Address[t.countTokens()];
			i = 0;
			while (t.hasMoreTokens()) {
				cc[i] = new InternetAddress(t.nextToken().trim());
				i++;
			}

			// ----------------BCC-------------------------------
			try {
				t = new StringTokenizer(
						TranscodeUtil.transcodeThenEncodeByLocale(header.getContent("BCC"), "GBK", locale).trim(), ",");
			} catch (UnsupportedEncodingException e) {
				t = new StringTokenizer(header.getContent("BCC").trim(), ",;");
			}
			Address bcc[] = new Address[t.countTokens()];
			i = 0;
			while (t.hasMoreTokens()) {
				bcc[i] = new InternetAddress(t.nextToken().trim());
				i++;
			}

			// ----------------SUBJECT---------------------------
			String subject = null;
			if (!header.isContentSet("SUBJECT") || header.getContent("SUBJECT").equals("")) {
				subject = "no subject";
			} else {
				// subject = header.getContent("SUBJECT");
				try {

					// subject=
					// MimeUtility.encodeText(header.getContent("SUBJECT"),"gb2312","B");
					// //B为base64方式

					subject = TranscodeUtil.transcodeThenEncodeByLocale(header.getContent("SUBJECT"), "GBK", locale);
					// subject = new
					// String(header.getContent("SUBJECT").getBytes("gbk"),"iso8859-1");
					// subject = MimeUtility.encodeText(subject);
				} catch (UnsupportedEncodingException e) {
					subject = header.getContent("SUBJECT");
				}
			}

			// 设定Message的From,To,CC,BCC等内容
			msg.addFrom(from);
			if (to.length > 0) {
				msg.addRecipients(Message.RecipientType.TO, to);
			}
			if (cc.length > 0) {
				msg.addRecipients(Message.RecipientType.CC, cc);
			}
			if (bcc.length > 0) {
				msg.addRecipients(Message.RecipientType.BCC, bcc);
			}
			// msg.addHeader("X-Mailer", WebMailServer.getVersion() + ",
			// SendMessage" + " plugin v1.8");

			msg.addHeader("Subject", subject);
			msg.setSentDate(new Date(System.currentTimeMillis()));

			// -----------------REPLY-TO--------------------
			if (header.isContentSet("REPLY-TO")) {
				// msg.addHeader("Reply-To",
				// TranscodeUtil.transcodeThenEncodeByLocale(header.getContent("REPLY-TO"),
				// "GBK", locale));
				msg.addHeader("Reply-To", header.getContent("REPLY-TO"));
			}

			MimeMultipart multipart = new MimeMultipart();

			BodyPart messageBodyPart = new MimeBodyPart();
			// Transcode to GBK
			String contnt = header.getContent("BODY");
			// String charset = "gbk";
			// contnt = new String(contnt.getBytes("ISO8859_1"), charset);
			// 设置e-mail内容
			// messageBodyPart.setText(contnt);
			// 建立第一部分：文本正文
			// 给BodyPart对象设置内容和格式/编码方式
			messageBodyPart.setContent(contnt, "text/html;charset=gbk");
			multipart.addBodyPart(messageBodyPart);

			// 附件处理
			Enumeration atts = mailSession.getAttachments().keys();
			while (atts.hasMoreElements()) {
				ByteStore bs = mailSession.getAttachment((String) atts.nextElement());

				InternetHeaders ih = new InternetHeaders();
				ih.addHeader("Content-Type", bs.getContentType());
				ih.addHeader("Content-Transfer-Encoding", "BASE64");

				PipedInputStream pin = new PipedInputStream();
				PipedOutputStream pout = new PipedOutputStream(pin);

				/**
				 * This is used to write to the Pipe asynchronously to avoid
				 * blocking
				 */
				StreamConnector sconn = new StreamConnector(pin, (int) (bs.getSize() * 1.6) + 1000);
				BufferedOutputStream encoder = new BufferedOutputStream(MimeUtility.encode(pout, "BASE64"));
				encoder.write(bs.getBytes());
				encoder.flush();
				encoder.close();

				// MimeBodyPart att1=sconn.getResult();
				MimeBodyPart mPart = new MimeBodyPart(ih, sconn.getResult().getBytes());

				mPart.addHeader("Content-Type", bs.getContentType());
				mPart.setDescription(bs.getDescription(), "utf-8");

				// As described in FileAttacher.java line #95, now we need to
				// encode the attachment file name.
				String fileName = bs.getName();
				//fileName = new String(fileName.getBytes("GB2312"), "iso-8859-1");
				//fileName  = MimeUtility.encodeText(fileName);
				//fileName = new String(fileName.getBytes("GB2312"), "iso-8859-1");
				// System.err.println("fileName: " + fileName);
				// if (locale.getLanguage().equals("zh") &&
				// locale.getCountry().equals("TW")) {
				// fileName = MimeUtility.encodeText(fileName, "Big5", null);
				// }
				mPart.setFileName(fileName);
				mPart.setDisposition(MimeBodyPart.ATTACHMENT);

				multipart.addBodyPart(mPart);
			}

			// -------------------
			msg.setContent(multipart);

			// ---
			msg.saveChanges();

			boolean savesuccess = true;

			msg.setHeader("Message-ID", mailSession.getUserModel().getWorkMessage().getAttribute("msgid"));

			// 如果发送时候，同时保存发送的邮件
			if (mailSession.getUser().wantsSaveSent()) {
				String folderhash = mailSession.getUser().getSentFolder();
				try {
					Folder folder = mailSession.getFolder(folderhash);
					Message[] m = new Message[1];
					m[0] = msg;
					folder.appendMessages(m);
				} catch (MessagingException e) {
					savesuccess = false;
				} catch (NullPointerException e) {
					// Invalid folder:
					savesuccess = false;
				}
			}

			boolean sendsuccess = false;
			try {

				Transport trans = session.getTransport();
				/** @todo */
				// 取得smtp host,user,password链接
				String smtpHost = store.getConfig("SMTP HOST");
				String password = mailSession.getPassword();
				String userName = mailSession.getUserName();
				trans.connect(smtpHost, userName, password);

				Address sent[] = new Address[to.length + cc.length + bcc.length];
				int c1 = 0;
				int c2 = 0;
				for (c1 = 0; c1 < to.length; c1++) {
					sent[c1] = to[c1];
				}
				for (c2 = 0; c2 < cc.length; c2++) {
					sent[c1 + c2] = cc[c2];
				}
				for (int c3 = 0; c3 < bcc.length; c3++) {
					sent[c1 + c2 + c3] = bcc[c3];
				}

				trans.sendMessage(msg, sent);

				trans.close();
				// Transport.send(msg);

				sendsuccess = true;
				// throw new SendFailedException("success", new
				// Exception("success"), sent, null, null);
			} catch (SendFailedException e) {
				mailSession.handleTransportException(e);

				// e.printStackTrace();
			}

			// 发送结束，把当前的work清除
			if (sendsuccess) {
				mailSession.clearWork();
				// 设置发送成功
				mailSession.getUserModel().setStateVar("send status", "Send Success");
			} else {
				mailSession.getUserModel().setStateVar("send status", "Send Fail");
			}
			mailSession.getUserModel().updateStateData();

			/** 把XML输出查看 */
			/**
			 * try{ Writer out = new OutputStreamWriter(new
			 * FileOutputStream("c:\\test_1.xml"),"GBK"); OutputFormat format =
			 * OutputFormat.createPrettyPrint(); //指定XML编码
			 * format.setEncoding("GBK");
			 * 
			 * XMLWriter writer = new XMLWriter(out, format);
			 * writer.write(mailSession.getModel()); out.flush(); out.close();
			 * }catch(Exception ex){
			 *  }
			 */

		} catch (Exception e) {
			e.printStackTrace();
			throw new DocumentNotFoundException("Could not send message. (Reason: " + e.getMessage() + ")");
		}
	}

}
