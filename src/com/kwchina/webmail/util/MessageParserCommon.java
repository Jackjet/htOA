package com.kwchina.webmail.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;

import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Part;
import javax.mail.internet.MimeUtility;

public class MessageParserCommon {

	/** */
	/**
	 * 判断一个邮件体是否是附件<br>
	 * 核心代码主要是解析其头部的disposition,一般设置了是disposition是attachment的才是附件
	 * 
	 * @return:如果是附件的话,那么返回true;<br>
	 *                           否则/异常false;
	 */
	public static boolean isAttachment(Part part) {
		boolean attachflag = false;

		try {
			String disposition = part.getDisposition();
			String contentType = part.getContentType().toUpperCase();
			if ((disposition != null) && ((disposition.equals(Part.ATTACHMENT)) || (disposition.equals(Part.INLINE))))
				attachflag = true;
			
			//String aa = part.getFileName();
			//InputStream input = part.getInputStream();

			// 图片（某些邮件服务器发送图片，不设置disposition，例如Qmail（不确定）
			if (contentType.startsWith("IMAGE/JPG") || contentType.startsWith("IMAGE/JPEG")
					|| contentType.startsWith("IMAGE/GIF") || contentType.startsWith("IMAGE/PNG")) {
				attachflag = true;
			}
			
			if(part.getFileName()==null || part.getFileName().equals(""))
				attachflag = false;
		} catch (Exception ex) {
			/**
			 * 上面的方法只是适合于附件不是中文，或者中文名较短的情况,<br>
			 * 附件中文名过长时就不能用了，因为javamail Api中的part.getDisposition()根本就不能得到正确的数据了<br>
			 * 捕获异常后,分别再详细判断处理
			 */
			attachflag = false;
			try {
				String contentType = part.getContentType();
				if (contentType.startsWith("application/octet-stream"))
					attachflag = true;
			} catch (MessagingException e1) {
				// return false;
			}
		}

		return attachflag;
	}

	public static boolean isContainAttach(Part part) {
		boolean attachflag = false;

		try {
			if (part.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) part.getContent();
				for (int i = 0; i < mp.getCount(); i++) {
					BodyPart mpart = mp.getBodyPart(i);

					attachflag = isContainAttach(mpart);
				}
			} else {
				attachflag = isAttachment(part);
			}

		} catch (Exception e) {
			attachflag = false;
		}

		return attachflag;
	}

	/** */
	/**
	 * 判断一封邮件是否含有附件<br>
	 * 首先要满足:msg的mimetype是multipart/*类型的,然后要有某bodyPart的disposition是attachment
	 * 
	 * @param msg:要判断是否含有附件的邮件
	 * @author M.Liang Liu
	 * @version 1.0
	 * @see public static boolean isAttachment(Part part)
	 * @return true:含有附件<br>
	 *         false:不含有附件
	 */
	public static boolean hasAttachment(Message msg) {
		Part part = (Part) msg;
		try {
			if (part.isMimeType("multipart/*")) {
				Multipart mp = (Multipart) part.getContent();
				int count = mp.getCount();
				for (int i = 0; i < count; i++) {
					Part tmp = mp.getBodyPart(i);
					if (isAttachment(tmp))
						return true;
				}
			}
		} catch (Exception e) {
			return false;
		}
		return false;
	}

	//
	public static String getFileName(Part p) throws MessagingException, UnsupportedEncodingException {

		// System.out.println("filename:");

		String filename = p.getFileName();

		if (filename == null)
			return null;

		filename = new String(filename.getBytes("ISO-8859-1"), "GBK");

		int indexStart = filename.toUpperCase().indexOf("=?GB");

		if (indexStart > -1) {

			filename = filename.substring(0, indexStart) + MimeUtility.decodeText(filename.substring(indexStart));

		}

		return filename;

	}

	public static String getISOFileName(Part body) {
		String fileName = "";

		// 设置一个标志，判断文件名从Content-Disposition中获取还是从Content-Type中获取
		boolean flag = true;
		if (body == null) {
			return null;
		}

		String[] cdis;
		try {
			cdis = body.getHeader("Content-Disposition");
		} catch (Exception e) {
			return null;
		}

		if (cdis == null) {
			flag = false;
		}

		if (!flag) {
			try {
				cdis = body.getHeader("Content-Type");
			} catch (Exception e) {
				return null;
			}
		}

		if (cdis == null) {
			return null;
		}
		if (cdis[0] == null) {
			return null;
		}

		// 从Content-Disposition中获取文件名
		if (flag) {
			int pos = cdis[0].indexOf("filename=");
			if (pos < 0) {
				return null;
			}
			// 如果文件名带引号
			if (cdis[0].charAt(cdis[0].length() - 1) == '"') {
				fileName = cdis[0].substring(pos + 10, cdis[0].length() - 1);
			} else {
				fileName = cdis[0].substring(pos + 9, cdis[0].length());
			}
		} else {
			int pos = cdis[0].indexOf("name=");
			if (pos < 0) {
				return null;
			}
			// 如果文件名带引号
			if (cdis[0].charAt(cdis[0].length() - 1) == '"') {
				fileName = cdis[0].substring(pos + 6, cdis[0].length() - 1);
			} else {
				fileName = cdis[0].substring(pos + 5, cdis[0].length());
			}
		}

		/**
		 * try { fileName = new String(fileName.getBytes("ISO-8859-1"), "GBK"); }
		 * catch (Exception ex) {
		 *  }
		 */

		return fileName;

	}

	/**
	 * 处理相关信息 某些服务器会把编码处理为x-unkown 将编码方式的信息由x-unkown改为gbk
	 */
	public static String dealUnkownEncoding(String content) {
		// 将编码方式的信息由x-unkown改为gbk
		if (content != null) {
			if (content.indexOf("=?x-unknown?") >= 0) {
				// 将编码方式的信息由x-unkown改为gbk
				content = content.replaceAll("x-unknown", "gbk");
			}
		} else {
			content = "";
		}

		return content;
	}

	public static String dealSubject(String subject) throws UnsupportedEncodingException,IOException {
		if (subject != null && !subject.equals("")) {
			
			String newSubject = subject;
			if (newSubject.indexOf("=?GB2312?B?") > 0) {
				int pos = newSubject.indexOf("=?GB2312?B?");

				int k = 0;
				subject = "";
				String encode = "";

				while (pos >= 0) {
					k += 1;

					int pos2 = newSubject.indexOf("?=");
					if (k > 1 && pos != 0) {
						sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
						byte[] b = decoder.decodeBuffer(encode);
						String aa = (new String(b));
						subject += aa;

						encode = "";
					}

					if (pos2 > 0)
						encode += newSubject.substring(pos + 11, pos2);
					else
						encode += newSubject.substring(pos + 11);

					// System.out.println(aa);
					subject += newSubject.substring(0, pos);

					if (pos2 > 0)
						newSubject = newSubject.substring(pos2 + 2);
					else
						newSubject = "";

					pos = newSubject.indexOf("=?GB2312?B?");
				}

				if (!encode.equals("")) {
					sun.misc.BASE64Decoder decoder = new sun.misc.BASE64Decoder();
					byte[] b = decoder.decodeBuffer(encode);
					String aa = (new String(b));
					subject += aa;
				}

				if (newSubject != "")
					subject += newSubject;
			} else {
				subject = MimeUtility.decodeText(subject);
			}
		}
		
		return subject;
	}

}
