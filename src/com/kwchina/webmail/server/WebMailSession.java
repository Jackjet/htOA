package com.kwchina.webmail.server;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.io.Writer;
import java.net.InetAddress;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.TimeZone;
import java.util.Vector;

import javax.mail.AuthenticationFailedException;
import javax.mail.BodyPart;
import javax.mail.FetchProfile;
import javax.mail.Flags;
import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Part;
import javax.mail.Provider;
import javax.mail.Quota;
import javax.mail.SendFailedException;
import javax.mail.Session;
import javax.mail.Store;
import javax.mail.URLName;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.internet.MimeUtility;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.DOMReader;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.XMLWriter;
import org.dom4j.tree.DefaultElement;
import org.xml.sax.InputSource;

import com.kwchina.webmail.exception.InvalidPasswordException;
import com.kwchina.webmail.exception.NoSuchFolderException;
import com.kwchina.webmail.exception.UserDataException;
import com.kwchina.webmail.exception.WebMailException;
import com.kwchina.webmail.misc.ByteStore;
import com.kwchina.webmail.misc.Helper;
import com.kwchina.webmail.misc.JavaScriptCleaner;
import com.kwchina.webmail.misc.MD5;
import com.kwchina.webmail.misc.Queue;
import com.kwchina.webmail.server.http.HTTPRequestHeader;
import com.kwchina.webmail.storage.Storage;
import com.kwchina.webmail.ui.html.Fancyfier;
import com.kwchina.webmail.util.MessageParserCommon;
import com.kwchina.webmail.util.MiscCommonMethod;
import com.kwchina.webmail.web.service.MailBasicService;
import com.kwchina.webmail.xml.XMLCommon;
import com.kwchina.webmail.xml.XMLMessage;
import com.kwchina.webmail.xml.XMLMessagePart;
import com.kwchina.webmail.xml.XMLUserData;
import com.kwchina.webmail.xml.XMLUserModel;
import com.sun.mail.imap.IMAPStore;

/*
 * WebMailSession.java
 *  
 */

/**
 * 用户Session,从相关配置中读取（每个用户均有配置文件） *
 * 
 */

public class WebMailSession implements HTTPSession {

	/** When has the session been last accessed? */
	private long last_access;

	/** The session-ID for this session */
	private String session_code;

	/** Parent WebMailServer */
	private WebMailServer parent;

	/** State of the current users configuration */
	private XMLUserData user;

	private XMLUserModel model;

	/** Connections to Mailboxes */
	private Hashtable connections;

	/** Connections to hosts */
	private Hashtable stores;

	/** javax.mail Mailsession */
	private Session mailsession;

	private InetAddress remote;

	/*
	 * Files attached to messages will be stored here. We will have to take care
	 * of possible memory problems!
	 */
	private Hashtable mime_parts_decoded;

	private boolean sent;

	private String remote_agent;

	private String remote_accepts;

	private int attachments_size = 0;

	private String last_login;

	/**
	 * Save the login password. It will be used for the second try password if
	 * opening a folder fails.
	 */
	/** @todo 从private改为protected */
	private String login_password;

	// 用户文件路径，如zhoulb.xml的zhoulb部分
	private String xml_path;

	/** @tood */
	private String currentMailHostName;

	private Object sess = null;

	private Hashtable folders;

	protected Vector need_expunge_folders;

	protected boolean is_logged_out = false;

	public WebMailSession(WebMailServer parent, Object parm, HTTPRequestHeader h) throws UserDataException,
			InvalidPasswordException, WebMailException {
		try {
			Class srvltreq = Class.forName("javax.servlet.http.HttpServletRequest");
			if (srvltreq.isInstance(parm)) {
				javax.servlet.http.HttpServletRequest req = (javax.servlet.http.HttpServletRequest) parm;

				// Session Id
				this.sess = req.getSession(false);
				session_code = ((javax.servlet.http.HttpSession) sess).getId();

				try {
					remote = InetAddress.getByName(req.getRemoteHost());
				} catch (UnknownHostException e) {
					try {
						remote = InetAddress.getByName(req.getRemoteAddr());
					} catch (Exception ex) {
						try {
							remote = InetAddress.getByName("localhost");
						} catch (Exception ex2) {
						}
					}
				}
			} else {
				throw new Exception("Servlet class found but not running as servlet");
			}
		} catch (Throwable t) {
			this.remote = (InetAddress) parm;

			// 计算SessionId
			session_code = Helper.calcSessionCode(remote, h);
		}

		// 初始化操作
		doInit(parent, h);
	}

	/**
	 * 用户数据初始化
	 */
	protected void doInit(WebMailServer parent, HTTPRequestHeader h) throws UserDataException, InvalidPasswordException,
			WebMailException {
		// 设置最后一次登录时间
		setLastAccess();

		this.parent = parent;
		remote_agent = h.getHeader("User-Agent").replace('\n', ' ');
		remote_accepts = h.getHeader("Accept").replace('\n', ' ');
		parent.getStorage().log(Storage.LOG_INFO, "WebMail: New Session (" + session_code + ")");

		// 获取用户信息（XMLUserData)
		String username = h.getContent("username");
		String password = h.getContent("password");
		String domain = h.getContent("domain");
		String path = h.getContent("_path");

		// 创建XMLUserData，获取用户信息（包括用户个人信息，个人配置信息，以及通讯录等）
		user = WebMailServer.getStorage().getUserData(path, username, domain, password, true);

		last_login = user.getLastLogin();
		user.login();

		// 登录密码
		login_password = password;

		// 用户文件路径
		this.xml_path = path;

		// XMLUserModel (包含了STATEDATA，SYSDATA以及USERDATA)
		model = parent.getStorage().createXMLUserModel(user);

		connections = new Hashtable();
		stores = new Hashtable();
		folders = new Hashtable();

		// javax.mail.Session
		mailsession = Session.getDefaultInstance(System.getProperties(), null);

		/* If the user logs in for the first time we want all folders subscribed */
		if (user.getLoginCount().equals("1")) {
			Enumeration enum1 = user.mailHosts();
			while (enum1.hasMoreElements()) {
				String id = (String) enum1.nextElement();
				if (user.getMailHost(id).getName().equals("Default")) {
					try {
						setSubscribedAll(id, true);
					} catch (MessagingException ex) {
						ex.printStackTrace();
					}
					break;
				}
			}
		}

		// 设置相关配置参数
		setEnv();
	}

	public XMLUserModel getUserModel() {
		return model;
	}

	public Document getModel() {
		return model.getRoot();
	}

	/**
	 * Calculate session-ID for a session.
	 * 
	 * @param a
	 *            Adress of the remote host
	 * @param h
	 *            Requestheader of the remote user agent
	 * @returns Session-ID
	 */
	public String calcCode(InetAddress a, HTTPRequestHeader h) {
		if (sess == null) {
			return Helper.calcSessionCode(a, h);
		} else {
			try {
				Class srvltreq = Class.forName("javax.servlet.http.HttpSession");
				if (srvltreq.isInstance(sess)) {
					return ((javax.servlet.http.HttpSession) sess).getId();
				} else {
					return "error";
				}
			} catch (Throwable t) {
				return "error";
			}
		}
	}

	/**
	 * Login to this session. Establishes connections to a user磗 Mailhosts
	 * 
	 * @param h
	 *            RequestHeader with content from Login-POST operation.
	 * @deprecated Use login() instead, no need for parameters and exception
	 *             handling
	 */
	public void login(HTTPRequestHeader h) throws InvalidPasswordException {
		// user.login(h.getContent("password"));
		login();
	}

	/**
	 * Login this session.
	 * 
	 * Updates access time, sets initial environment and connects all configured
	 * mailboxes.
	 */
	public void login() {
		setLastAccess();
		setEnv();

		// log on, get the folderInfor
		connectAll();
	}

	/**
	 * Return a locale-specific string resource
	 */
	public String getStringResource(String key) {
		return parent.getStorage().getStringResource(key, user.getPreferredLocale());
	}

	/**
	 * Create a Message List. Fetches a list of headers in folder foldername for
	 * part list_part. The messagelist will be stored in the "MESSAGES"
	 * environment.
	 * 
	 * @param folderhash
	 *            folderId for which a message list should be built
	 * @param list_part
	 *            part of list to display (1 = last xx messages, 2 = total-2*xx -
	 *            total-xx messages)
	 */
	public void createMessageList(String folderhash, int list_part) throws NoSuchFolderException {

		long time_start = System.currentTimeMillis();
		TimeZone tz = TimeZone.getDefault();

		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, user.getPreferredLocale());
		df.setTimeZone(tz);

		try {
			Folder folder = getFolder(folderhash);

			if (folder == null) {
				throw new NoSuchFolderException("Could not find folder " + folderhash + "!");
			}

			// 更新该Folder所属Store的Quato信息
			Store st = folder.getStore();
			addMailHostQuato(st, currentMailHostName);

			Element xml_folder = model.getFolder(folderhash);
			Element xml_current = model.setCurrentFolder(folderhash);
			Element xml_messagelist = model.getMessageList(xml_folder);

			if (folder == null) {
				throw new NoSuchFolderException(folderhash);
			}

			long fetch_start = System.currentTimeMillis();

			if (!folder.isOpen()) {				
				if (!folder.getStore().isConnected())
					folder.getStore().connect();
				folder.open(Folder.READ_ONLY);
			} else {
				folder.close(false);
				folder.open(Folder.READ_ONLY);
			}

			/** Calculate first and last message to show */
			int total_messages = folder.getMessageCount();
			int new_messages = folder.getUnreadMessageCount();
			int show_msgs = user.getMaxShowMessages();

			XMLCommon.setAttributeValue(xml_messagelist, "total", total_messages + "");
			XMLCommon.setAttributeValue(xml_messagelist, "new", new_messages + "");
			/**
			 * xml_messagelist.attributeValue("total", total_messages + "");
			 * xml_messagelist.setAttribute("new", new_messages + "");
			 */

			// ---------如下信息用于分页等-----------------------------------
			// System.err.println("Total: "+total_messages);
			// 总页数
			int partNum = 0;
			/** Handle small messagelists correctly */
			if (total_messages < show_msgs) {
				show_msgs = total_messages;
				partNum = 1;
			} else {
				if ((total_messages % show_msgs) == 0) {
					partNum = (total_messages / show_msgs);
				} else {
					partNum = (total_messages / show_msgs) + 1;
				}
			}

			/** Don't accept list-parts smaller than 1 */
			if (list_part < 1) {
				list_part = 1;
			} else if (list_part > partNum) {
				list_part = partNum;
			}

			int first = 0;
			int last = 0;
			// int start = list_part * show_msgs;
			int start = total_messages;
			for (int k = 0; k < list_part; k++) {
				start -= show_msgs;
			}

			if (start < 0) {
				first = 1;
				// last = start + show_msgs;
			} else {
				first = start + 1;
				// last = start + show_msgs;
			}
			// int first = start + 1;
			last = start + show_msgs;

			/** Set environment variable */
			setEnv();

			// 显示的第一个Message
			XMLCommon.setAttributeValue(xml_current, "first_msg", first + "");
			// 显示的最后一个Message
			XMLCommon.setAttributeValue(xml_current, "last_msg", last + "");
			// 当前显示的页
			XMLCommon.setAttributeValue(xml_current, "list_part", list_part + "");
			// 总Message
			XMLCommon.setAttributeValue(xml_current, "all_msg", total_messages + "");
			// 总part
			XMLCommon.setAttributeValue(xml_current, "all_part", partNum + "");

			/**
			 * xml_current.setAttribute("first_msg", first + "");
			 * xml_current.setAttribute("last_msg", last + "");
			 * xml_current.setAttribute("list_part", list_part + "");
			 */

			/** Fetch headers */
			FetchProfile fp = new FetchProfile();
			fp.add(FetchProfile.Item.ENVELOPE);
			fp.add(FetchProfile.Item.FLAGS);
			//fp.add(FetchProfile.Item.CONTENT_INFO);
			fp.add("X-Mailer");

			// System.err.println("Last: "+last+", first: "+first);
			Message[] msgs = folder.getMessages(first, last);
			// System.err.println(msgs.length + " messages fetching...");
			folder.fetch(msgs, fp);

			long fetch_stop = System.currentTimeMillis();

			// Hashtable header = new Hashtable(15);

			Flags.Flag[] sf;
			String from, to, cc, bcc, replyto, subject;
			String messageid;

			// for (int i = 0; i <msgs.length; i++) {
			for (int i = msgs.length - 1; i >= 0; i--) {
				// if(((MimeMessage)msgs[i]).getMessageID() == null) {
				// folder.close(false);
				// folder.open(Folder.READ_WRITE);
				// ((MimeMessage)msgs[i]).setHeader("Message-ID","<"+user.getLogin()+"."+System.currentTimeMillis()+".jwebmail@"+user.getDomain()+">");
				// ((MimeMessage)msgs[i]).saveChanges();
				// folder.close(false);
				// folder.open(Folder.READ_ONLY);
				// }

				//int messageNumber = ((MimeMessage) msgs[i]).getMessageNumber();
				//String messageId = ((MimeMessage) msgs[i]).getMessageID();

				try {
					StringTokenizer tok = new StringTokenizer(((MimeMessage) msgs[i]).getMessageID(), "<>");
					messageid = tok.nextToken();
					// } catch (NullPointerException ex) {
				} catch (Exception ex) {
					// For mail servers that don't generate a Message-ID
					// (Outlook et al)
					messageid = user.getLogin() + "." + i + ".jwebmail@" + user.getDomain();
				}

				XMLMessage xml_message = model.getMessage(xml_folder, msgs[i].getMessageNumber() + "", messageid);

				/** Addresses */
				from = "";
				replyto = "";
				to = "";
				cc = "";
				bcc = "";
				try {
					from = MimeUtility.decodeText(Helper.joinAddress(msgs[i].getFrom()));
					replyto = MimeUtility.decodeText(Helper.joinAddress(msgs[i].getReplyTo()));
					to = MimeUtility.decodeText(Helper.joinAddress(msgs[i].getRecipients(Message.RecipientType.TO)));
					cc = MimeUtility.decodeText(Helper.joinAddress(msgs[i].getRecipients(Message.RecipientType.CC)));
					bcc = MimeUtility.decodeText(Helper.joinAddress(msgs[i].getRecipients(Message.RecipientType.BCC)));
				} catch (UnsupportedEncodingException e) {
					parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding: " + e.getMessage());
				}

				if (from == "")
					from = getStringResource("unknown sender");

				if (to == "")
					to = getStringResource("unknown recipient");


				sf = msgs[i].getFlags().getSystemFlags();
				// String basepath = parent.getBasePath();

				for (int j = 0; j < sf.length; j++) {
					if (sf[j] == Flags.Flag.RECENT)
						xml_message.setAttribute("recent", "true");
					if (sf[j] == Flags.Flag.SEEN)
						xml_message.setAttribute("seen", "true");
					if (sf[j] == Flags.Flag.DELETED)
						xml_message.setAttribute("deleted", "true");
					if (sf[j] == Flags.Flag.ANSWERED)
						xml_message.setAttribute("answered", "true");
					if (sf[j] == Flags.Flag.DRAFT)
						xml_message.setAttribute("draft", "true");
					if (sf[j] == Flags.Flag.FLAGGED)
						xml_message.setAttribute("flagged", "true");
					if (sf[j] == Flags.Flag.USER)
						xml_message.setAttribute("user", "true");
				}

				// 判断是否有附件
				if (msgs[i] instanceof MimeMessage) {
					try {
						boolean hasAttach = MessageParserCommon.isContainAttach((MimeMessage) msgs[i]);
						if (hasAttach)
							xml_message.setAttribute("attachment", "true");
					} catch (Exception ex) {
						xml_message.setAttribute("attachment", "false");
					}
				}

				/**
				 * if (msgs[i] instanceof MimeMessage && ((MimeMessage)
				 * msgs[i]).getContentType()
				 * .toUpperCase().startsWith("MULTIPART/")) {
				 * xml_message.setAttribute("attachment", "true"); }
				 */

				/** message Size */
				if (msgs[i] instanceof MimeMessage) {
					int size = ((MimeMessage) msgs[i]).getSize();
					size /= 1024;
					xml_message.setAttribute("size", (size > 0 ? size + "" : "<1") + " kB");
				}

				/** Subject */	
				subject = msgs[i].getSubject();
				subject = MessageParserCommon.dealUnkownEncoding(subject);
				if (subject != null && !subject.equals("")) {
					try {	
						subject = MessageParserCommon.dealSubject(subject);								
					} catch (UnsupportedEncodingException ex) {
						parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding: " + ex.getMessage());
					} catch(IOException ex){
						parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding: " + ex.getMessage());
					}
				}
				if (subject == null || subject.equals("")) {
					/** @todo check the following sentence */
					// subject = getStringResource("no subject");
					subject = "无主题";
				}

				/** Set all of what we found into the DOM */
				xml_message.setHeader("FROM", from);
				xml_message.setHeader("SUBJECT", subject);				
				xml_message.setHeader("TO", to);
				xml_message.setHeader("CC", cc);
				xml_message.setHeader("BCC", bcc);
				xml_message.setHeader("REPLY-TO", replyto);

				/** Date */
				Date d = msgs[i].getSentDate();
				String ds = "";
				if (d != null) {
					ds = df.format(d);
				}
				xml_message.setHeader("DATE", ds);
			}
			long time_stop = System.currentTimeMillis();
			// try {
			// XMLCommon.writeXML(model.getRoot(),new
			// FileOutputStream("/tmp/wmdebug"),"");
			// } catch(IOException ex) {}

			parent.getStorage().log(
					Storage.LOG_DEBUG,
					"Construction of message list took " + (time_stop - time_start) + " ms. Time for IMAP transfer was "
							+ (fetch_stop - fetch_start) + " ms.");
			
			if (folder.isOpen()) 		
				folder.close(false);
		} catch (NullPointerException e) {
			e.printStackTrace();
			// throw new NoSuchFolderException(folderhash);
			System.out.println("--Get Mail Error:" + e.toString());
		} catch (MessagingException ex) {
			// ex.printStackTrace();
			System.out.println("--Get Mail Error:" + ex.toString());
		} 
	}

	/**
	 * This indicates standard getMessage behaviour: Fetch the message from the
	 * IMAP server and store it in the current UserModel.
	 * 
	 * @see getMessage(String,int,int)
	 */
	public static final int GETMESSAGE_MODE_STANDARD = 0;

	/**
	 * Set this mode in getMessage to indicate that the message is requested to
	 * generate a reply message and should therefore be set as the current
	 * "work" message.
	 * 
	 * @see getMessage(String,int,int)
	 */
	public static final int GETMESSAGE_MODE_REPLY = 1;

	/**
	 * Set this mode in getMessage to indicate that the message is to be
	 * forwarded to someone else and a "work" message should be generated.
	 * 
	 * @see getMessage(String,int,int)
	 */
	public static final int GETMESSAGE_MODE_FORWARD = 2;

	/**
	 * This is a wrapper to call getMessage with standard mode.
	 * 
	 * @see getMessage(String,int,int)
	 */
	public void getMessage(String folderhash, int msgnum) throws NoSuchFolderException {
		getMessage(folderhash, msgnum, GETMESSAGE_MODE_STANDARD);
	}

	/**
	 * Fetch a message from a folder. Will put the messages parameters in the
	 * sessions environment
	 * 
	 * @param folderhash
	 *            Id of the folder were the message should be fetched from
	 * @param msgnum
	 *            Number of the message to fetch
	 * @param mode
	 *            there are three different modes: standard, reply and forward.
	 *            reply and forward will enter the message into the current work
	 *            element of the user and set some additional flags on the
	 *            message if the user has enabled this option.
	 * @see com.kwchina.webmail.server.WebMailSession.GETMESSAGE_MODE_STANDARD
	 * @see com.kwchina.webmail.server.WebMailSession.GETMESSAGE_MODE_REPLY
	 * @see com.kwchina.webmail.server.WebMailSession.GETMESSAGE_MODE_FORWARD
	 */
	public void getMessage(String folderhash, int msgnum, int mode) throws NoSuchFolderException {
		try {
			long time_start = System.currentTimeMillis();
			TimeZone tz = TimeZone.getDefault();

			DateFormat df = DateFormat.getDateTimeInstance(DateFormat.DEFAULT, DateFormat.SHORT, user.getPreferredLocale());
			df.setTimeZone(tz);

			Folder folder = getFolder(folderhash);
			Element xml_folder = model.getFolder(folderhash);

			if (folder == null) {
				throw new NoSuchFolderException("No such folder: " + folderhash);
			} else {
				/** @todo need reConnect? */
				if (!folder.getStore().isConnected()) {
					folder.getStore().connect();
				}
			}

			// 打开Folder,并设置为READ_ONLY状态
			if (folder.isOpen() && folder.getMode() == Folder.READ_WRITE) {
				folder.close(false);
				folder.open(Folder.READ_ONLY);
			} else if (!folder.isOpen()) {
				folder.open(Folder.READ_ONLY);
			}

			MimeMessage m = (MimeMessage) folder.getMessage(msgnum);
			// boolean aa =m.isMimeType("text/plain");

			String messageid;
			try {
				StringTokenizer tok = new StringTokenizer(m.getMessageID(), "<>");
				messageid = tok.nextToken();
				// } catch (NullPointerException ex) {
			} catch (Exception ex) {
				// For mail servers that don't generate a Message-ID (Outlook et
				// al)

				/** @todo following sentence */
				// messageid = user.getLogin() + "." + msgnum + ".jwebmail@" +
				// user.getDomain();
				messageid = user.getLogin() + "." + msgnum + ".webmail@" + user.getDomain();
			}

			// 在XMLUserModel中创建CurrentMessage的Element
			Element xml_current = model.setCurrentMessage(messageid);

			XMLMessage xml_message = model.getMessage(xml_folder, m.getMessageNumber() + "", messageid);

			/**
			 * Check whether we already cached this message (not only headers
			 * but complete) If we cached the message, we don't need to fetch it
			 * again
			 */
			boolean cached = xml_message.messageCompletelyCached();
			if (!cached) {
				// Element xml_header=model.getHeader(xml_message);

				try {
					String from = MimeUtility.decodeText(Helper.joinAddress(m.getFrom()));
					String replyto = MimeUtility.decodeText(Helper.joinAddress(m.getReplyTo()));
					String to = MimeUtility.decodeText(Helper.joinAddress(m.getRecipients(Message.RecipientType.TO)));
					String cc = MimeUtility.decodeText(Helper.joinAddress(m.getRecipients(Message.RecipientType.CC)));
					String bcc = MimeUtility.decodeText(Helper.joinAddress(m.getRecipients(Message.RecipientType.BCC)));

					Date date_orig = m.getSentDate();

					/** @todo check the following sentence */
					// String date = getStringResource("no date");
					String date = "无日期";
					if (date_orig != null) {
						date = df.format(date_orig);
					}

					// 主题
					String subject = "";
					if (m.getSubject() != null) {
						subject = MessageParserCommon.dealUnkownEncoding(m.getSubject());
						
						//String encode = TranscodeUtil.getEncoding(subject);						
						//try{
							//String aaa = TranscodeUtil.decodeQP("_=CB=D5=B4=F2=B7=DB");
							//System.out.println(aaa);
						//String decodeQP = TranscodeUtil.decodeQP(subject);
						//System.out.println(decodeQP);
						//}catch(Exception ex){
							//System.out.print(ex.toString());
						//}
						
						// String aa   =   MimeUtility.encodeText(new   String(subject.getBytes(),"GB2312"),   "GB2312",   "quoted-printable");   
						//String aaa= MimeUtility.decode(subject,"quoted-printable");   
						
						//subject = MimeUtility.decodeText(subject);
						
						
						if (subject != null) {
							try {	
								subject = MessageParserCommon.dealSubject(subject);								
							} catch (UnsupportedEncodingException ex) {
								parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding: " + ex.getMessage());
							} catch(IOException ex){
								parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding: " + ex.getMessage());
							}
						}
						
						//System.out.println(subject);
						//System.out.println(subject);
						//subject = MimeUtility.decodeText(subject);
					}
					if (subject == null || subject.equals("")) {
						subject = getStringResource("no subject");
					}

					try {
						Flags.Flag[] sf = m.getFlags().getSystemFlags();
						for (int j = 0; j < sf.length; j++) {
							if (sf[j] == Flags.Flag.RECENT)
								xml_message.setAttribute("recent", "true");
							if (sf[j] == Flags.Flag.SEEN)
								xml_message.setAttribute("seen", "true");
							if (sf[j] == Flags.Flag.DELETED)
								xml_message.setAttribute("deleted", "true");
							if (sf[j] == Flags.Flag.ANSWERED)
								xml_message.setAttribute("answered", "true");
							if (sf[j] == Flags.Flag.DRAFT)
								xml_message.setAttribute("draft", "true");
							if (sf[j] == Flags.Flag.FLAGGED)
								xml_message.setAttribute("flagged", "true");
							if (sf[j] == Flags.Flag.USER)
								xml_message.setAttribute("user", "true");
						}
					} catch (NullPointerException ex) {
						System.out.println("Message Flags:" + ex.toString());
					}

					// 先设定没有附件，解析各部分再判断是否有附件
					boolean hasAttach = MessageParserCommon.isContainAttach(m);
					xml_message.setAttribute("attachment", hasAttach ? "true" : "false");

					// 邮件大小
					int size = m.getSize();
					size /= 1024;
					xml_message.setAttribute("size", (size > 0 ? size + "" : "<1") + " kB");

					/** Set all of what we found into the DOM * */
					xml_message.setHeader("FROM", from);
					// xml_message.setHeader("SUBJECT",
					// MimeUtility.decodeText(subject));
					
					xml_message.setHeader("SUBJECT", Fancyfier.apply(subject));
					xml_message.setHeader("TO", to);
					xml_message.setHeader("CC", cc);
					xml_message.setHeader("BCC", bcc);
					xml_message.setHeader("REPLY-TO", replyto);
					xml_message.setHeader("DATE", date);

					/** Decode MIME contents recursively */
					xml_message.removeAllParts();

					// 解析邮件各部分内容
					int mimeCount = 0;
					parseMIMEContent(m, xml_message, messageid, mimeCount);

				} catch (UnsupportedEncodingException e) {
					parent.getStorage().log(Storage.LOG_WARN, "Unsupported Encoding in parseMIMEContent: " + e.getMessage());
					System.err.println("Unsupported Encoding in parseMIMEContent: " + e.getMessage());
				}
			}

			/** Set seen flag (Maybe make that threaded to improve performance) */
			// 设置邮件Flag,必须通过READ_WRITE打开
			if (user.wantsSetFlags()) {
				if (folder.isOpen() && folder.getMode() == Folder.READ_ONLY) {
					folder.close(false);
					folder.open(Folder.READ_WRITE);
				} else if (!folder.isOpen()) {
					folder.open(Folder.READ_WRITE);
				}

				// 设置邮件为已读邮件
				folder.setFlags(msgnum, msgnum, new Flags(Flags.Flag.SEEN), true);
				xml_message.setAttribute("seen", "true");

				// Examine ALL system flags for this message
				/**
				 * Flags flags = m.getFlags(); Flags.Flag[] sf =
				 * flags.getSystemFlags(); for (int i = 0; i < sf.length; i++) {
				 * System.out.println(sf[i].toString()); if (sf[i] ==
				 * Flags.Flag.DELETED) { // System.out.println("DELETED
				 * message"); } else if (sf[i] == Flags.Flag.SEEN) {
				 * folder.setFlags(msgnum, msgnum, new Flags(Flags.Flag.SEEN),
				 * true); } else if (sf[i] == Flags.Flag.RECENT) {
				 * folder.setFlags(msgnum, msgnum, new Flags(Flags.Flag.RECENT),
				 * false); } }
				 */

				if ((mode & GETMESSAGE_MODE_REPLY) == GETMESSAGE_MODE_REPLY) {
					folder.setFlags(msgnum, msgnum, new Flags(Flags.Flag.ANSWERED), true);
				}
			}
			folder.close(false);

			/**
			 * In this part we determine whether the message was requested so
			 * that it may be used for further editing (replying or forwarding).
			 * In this case we set the current "work" message to the message we
			 * just fetched and then modifiy it a little (quote, add a "Re" to
			 * the subject, etc).
			 */
			XMLMessage work = null;
			if ((mode & GETMESSAGE_MODE_REPLY) == GETMESSAGE_MODE_REPLY
					|| (mode & GETMESSAGE_MODE_FORWARD) == GETMESSAGE_MODE_FORWARD) {
				// System.err.println("Setting work message!");
				work = model.setWorkMessage(xml_message);

				String newmsgid = WebMailServer.generateMessageID(user.getUserName());

				if (work != null && (mode & GETMESSAGE_MODE_REPLY) == GETMESSAGE_MODE_REPLY) {
					String from = work.getHeader("FROM");
					work.setHeader("FROM", user.getEmail());
					work.setHeader("TO", from);
					/**
					 * work.prepareReply( getStringResource("reply subject
					 * prefix"), getStringResource("reply subject postfix"),
					 * getStringResource("reply message prefix"),
					 * getStringResource("reply message postfix"));
					 */
					work.prepareReply("RE:", "", "", "");
				} else if (work != null && (mode & GETMESSAGE_MODE_FORWARD) == GETMESSAGE_MODE_FORWARD) {
					//String from = work.getHeader("FROM");
					work.setHeader("FROM", user.getEmail());
					work.setHeader("TO", "");
					work.setHeader("CC", "");
					/**
					 * work.prepareForward( getStringResource("forward subject
					 * prefix"), getStringResource("forward subject postfix"),
					 * getStringResource("forward message prefix"),
					 * getStringResource("forward message postfix"));
					 */

					work.prepareForward("Fwd: ", "", "", "");

					/** Copy all references to MIME parts to the new message id */
					Enumeration attids = getMimeParts(work.getAttribute("msgid"));
					while (attids.hasMoreElements()) {
						String key = (String) attids.nextElement();
						StringTokenizer tok2 = new StringTokenizer(key, "/");
						tok2.nextToken();
						String newkey = tok2.nextToken();
						
						mime_parts_decoded.put(newmsgid + "/" + newkey, mime_parts_decoded.get(key));
					}
				}

				/** Clear the msgnr and msgid fields at last */
				work.setAttribute("msgnr", "0");
				work.setAttribute("msgid", newmsgid);

				// 自动加上签名信息
				prepareCompose();
				
				try{ Writer out = new OutputStreamWriter(new
						FileOutputStream("c:\\testxx.xml"),"GBK"); OutputFormat
						format = OutputFormat.createPrettyPrint(); //指定XML编码
						format.setEncoding("GBK");
						
						 XMLWriter writer = new XMLWriter(out, format);
						writer.write(model.getRoot()); out.flush(); out.close();
					 }catch(Exception ex){ }
			}

		} catch (MessagingException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Use depth-first search to go through MIME-Parts recursively.
	 * 
	 * @param p
	 *            Part to begin with
	 */
	protected int parseMIMEContent(Part p, XMLMessagePart parent_part, String msgid, int mimeCount) throws MessagingException {
		StringBuffer content = new StringBuffer(1000);

		XMLMessagePart xml_part;

		try {

			if (MessageParserCommon.isAttachment(p)) {
				// 如果是附件，则

				// 设定是否有附件
				// parent_part.setAttribute("attachment", "true");
				InputStream in = null;
				String type = "";
				if (p.getContentType().toUpperCase().startsWith("IMAGE/JPG")
						|| p.getContentType().toUpperCase().startsWith("IMAGE/JPEG")) {
					type = "jpg";
					xml_part = parent_part.createPart("image");
				} else if (p.getContentType().toUpperCase().startsWith("IMAGE/GIF")) {
					type = "gif";
					xml_part = parent_part.createPart("image");
				} else if (p.getContentType().toUpperCase().startsWith("IMAGE/PNG")) {
					type = "png";
					xml_part = parent_part.createPart("image");
				} else {
					xml_part = parent_part.createPart("binary");
				}

				int size = p.getSize();
				if (p instanceof MimeBodyPart) {
					MimeBodyPart mpb = (MimeBodyPart) p;
					System.err.println("MIME Body part (image), Encoding: " + mpb.getEncoding());

					/**
					 * InputStream is = mpb.getInputStream();
					 * 
					 * Workaround for Java or Javamail Bug in = new
					 * BufferedInputStream(is); ByteStore ba =
					 * ByteStore.getBinaryFromIS(in, size); in = new
					 * ByteArrayInputStream(ba.getBytes()); End of workaround
					 * size = in.available();
					 */
				} else {
					System.err.println("*** No MIME Body part!! ***");
					// in = p.getInputStream();
				}

				String name = MessageParserCommon.getISOFileName(p);		
				

				// 通过MimeUtility去decode附件文件名称
				try {
					name= new String(name.getBytes("ISO-8859-1"),"gb2312");
					name = MimeUtility.decodeText(name);
				} catch (Exception e) {
					System.err.println(e);
				}

				xml_part.setAttribute("filename", name);
				// Transcode name into UTF-8 bytes then make a new ISO8859_1
				// string to encode URL.
				xml_part.setAttribute("hrefFileName", URLEncoder.encode(new String(name.getBytes("UTF-8"), "ISO8859_1")));

				String afterSize = "";
				DecimalFormat df2 = new DecimalFormat("###.0");
				if (size > 1 * 1000 * 1000) {
					afterSize = df2.format(size / (1000 * 1000)) + "M";
				} else if (size > 1 * 1000) {
					afterSize = df2.format(size / (1000)) + "K";
				} else {
					afterSize = size + "Byte";
				}

				xml_part.setAttribute("size", afterSize);
				String description = p.getDescription() == null ? "" : p.getDescription();
				xml_part.setAttribute("description", description);

				StringTokenizer tok = new StringTokenizer(p.getContentType(), ";");
				xml_part.setAttribute("content-type", tok.nextToken().toLowerCase());

				mimeCount += 1;
				xml_part.setAttribute("part-count", mimeCount + "");

			} else {

				String contentType = p.getContentType().toUpperCase();
				if (contentType.startsWith("TEXT/HTML")) {

					xml_part = parent_part.createPart("html");					

					String encoding = "GB2312";
					int pos =contentType.indexOf("CHARSET");
					if(pos>0)
						encoding = contentType.substring(pos+8);
					if(encoding==null || encoding.equals(""))
						encoding = "GB2312";
					
					//System.out.println(encoding);
					
					/* Here we create a DOM tree. */
					org.cyberneko.html.parsers.DOMParser parser = new org.cyberneko.html.parsers.DOMParser();
					parser.setProperty("http://cyberneko.org/html/properties/default-encoding", encoding);
					parser.setFeature("http://cyberneko.org/html/features/augmentations",true);
					parser.setProperty("http://cyberneko.org/html/properties/names/elems","lower");
					

					InputStream stream = p.getInputStream();				
					parser.parse(new InputSource(stream));

					/** @ todo here org.w3c.dom.Document --> org.dom4j.Document */
					org.w3c.dom.Document doc = parser.getDocument();
					// 去掉Javascript以及其它标签内容
					new JavaScriptCleaner(doc);

					DOMReader xmlReader = new DOMReader();					
					Document htmldoc = xmlReader.read(doc);

					
					/** 把XML输出查看 */		
					/**
					try{ Writer out = new OutputStreamWriter(new
						FileOutputStream("c:\\test.xml"),"GBK"); OutputFormat
						format = OutputFormat.createPrettyPrint(); //指定XML编码
						format.setEncoding("GBK");
						
						 XMLWriter writer = new XMLWriter(out, format);
						writer.write(htmldoc); out.flush(); out.close();
					 }catch(Exception ex){ }					
					*/
					xml_part.addContent(htmldoc);

				} else if (p.getContentType().toUpperCase().startsWith("TEXT")
						|| p.getContentType().toUpperCase().startsWith("MESSAGE")) {
					// else if (p.isMimeType("TEXT/PLAIN")) {
					/*
					 * The part is a standard message part in some incarnation
					 * of text (html or plain). We should decode it and take
					 * care of some extra issues like recognize quoted parts,
					 * filter JavaScript parts and replace smileys with
					 * smiley-icons if the user has set wantsFancy()
					 */

					xml_part = parent_part.createPart("text");
					
					BufferedReader in;
					if (p instanceof MimeBodyPart) {
						int size = p.getSize();
						MimeBodyPart mpb = (MimeBodyPart) p;
						InputStream is = mpb.getInputStream();

						/* Workaround for Java or Javamail Bug */
						is = new BufferedInputStream(is);
						ByteStore ba = ByteStore.getBinaryFromIS(is, size);
						in = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(ba.getBytes())));
						/* End of workaround */
						size = is.available();

						String charset = "GBK";
						// Check whether the part contained a charset in the
						// content-type header

						StringTokenizer tok2 = new StringTokenizer(p.getContentType(), ";=");
						String blah = tok2.nextToken();
						if (tok2.hasMoreTokens()) {
							blah = tok2.nextToken().trim();
							if (blah.toLowerCase().equals("charset") && tok2.hasMoreTokens()) {
								charset = tok2.nextToken().trim();
							}
						}

						String token = "";
						int quote_level = 0, old_quotelevel = 0;
						boolean javascript_mode = false;
						/* Read in the message part line by line */
						while ((token = in.readLine()) != null) {
							try {
								token = new String(token.getBytes(), charset);
							} catch (UnsupportedEncodingException ex1) {
								parent.getStorage().log(Storage.LOG_INFO,
										"Java Engine does not support charset " + charset + ". Trying to convert from MIME ...");

								try {
									charset = MimeUtility.javaCharset(charset);
									token = new String(token.getBytes(), charset);

								} catch (UnsupportedEncodingException ex) {
									parent.getStorage().log(
											Storage.LOG_WARN,
											"Converted charset (" + charset
													+ ") does not work. Using default charset (ouput may contain errors)");
									token = new String(token.getBytes());
								}
							}

							/*
							 * Here we figure out which quote level this line
							 * has, simply by counting how many ">" are in front
							 * of the line, ignoring all whitespaces.
							 */
							int current_quotelevel = Helper.getQuoteLevel(token);

							/*
							 * When we are in a different quote level than the
							 * last line, we append all we got so far to the
							 * part with the old quotelevel and begin with a
							 * clean String buffer
							 */
							if (current_quotelevel != old_quotelevel) {
								xml_part.addContent(content.toString(), old_quotelevel);
								old_quotelevel = current_quotelevel;

								content = new StringBuffer(1000);
							}

							/**
							 * if (user.wantsBreakLines()) { Enumeration enum1 =
							 * Helper.breakLine(token, user .getMaxLineLength(),
							 * current_quotelevel);
							 * 
							 * while (enum1.hasMoreElements()) { String s =
							 * (String) enum1.nextElement(); if
							 * (user.wantsShowFancy()) {
							 * content.append(Fancyfier.apply(s)).append("\n"); }
							 * else { content.append(s).append("\n"); } } } else {
							 */
							if (user.wantsShowFancy()) {
								content.append(Fancyfier.apply(token)).append("\n\r");
							} else {
								content.append(token).append("\n\r");
							}
							// }
						}

						String contentStr = content.toString();
						xml_part.addContent(contentStr, 0);
					} else {
						// in = new BufferedReader(new
						// InputStreamReader(p.getInputStream()));
						String contentStr = (String) p.getContent();
						xml_part.addContent(contentStr, 0);
					}

					// System.err.println("Content-Type: "+p.getContentType());

					/* First decode all language and MIME dependant stuff */
					// Default to ISO-8859-1 (Western Latin 1)
					/** @todo */
					// String charset = "ISO-8859-1";
					// Modified by exce, start
					// Why the following code???
					// content = new StringBuffer(1000);
					// Modified by exce, end.
				} else if (p.getContentType().toUpperCase().startsWith("MULTIPART/ALTERNATIVE")) {
					MimeMultipart m = (MimeMultipart) p.getContent();
					String[] preferred = {"TEXT/HTML", "TEXT"};
					boolean found = false;
					int alt = 0;

					/**	
					 * Here is our policy for alternative part: 1. Displays HTML
					 * but hides TEXT. 2. When replying this mail, try to quote
					 * TEXT part. If no TEXT part exists, quote HTML in best
					 * effort(use XMLMessagePart.quoteContent() by Sebastian
					 * Schaffert.)
					 */
					while (alt < preferred.length) {
						for (int i = 0; i < m.getCount(); i++) {
							Part p2 = m.getBodyPart(i);
							if (p2.getContentType().toUpperCase().startsWith(preferred[alt])) {
								System.err.println("Processing: " + p2.getContentType());

								int currentPart = parseMIMEContent(p2, parent_part, msgid, mimeCount);
								mimeCount = currentPart;

								found = true;
								break;
							}
						}
						/**
						 * If we've selected HTML part from alternative part,
						 * the TEXT part should be hidden from display but
						 * keeping in XML for later quoting operation.						
						 */
						if (found && (alt == 1)) {
							// 最后一个节点
							List ls = parent_part.getPartElement().elements();
							Element textPart = (Element) ls.get(ls.size() - 1);
							List attributes = textPart.attributes();
							for (int i = 0; i < attributes.size(); ++i) {
								Attribute attr = (Attribute) attributes.get(i);
								if (attr.getName().toUpperCase().equals("TYPE") && attr.getValue().toUpperCase().equals("TEXT")) {
									textPart.addAttribute("hidden", "true");
								}
							}
						}

						alt++;
					}

					if (!found) {
						int currentPart = parseMIMEContent(m.getBodyPart(0), parent_part, msgid, mimeCount);
						mimeCount = currentPart;
					}

				} else if (p.getContentType().toUpperCase().startsWith("MULTIPART/")) {
					/*
					 * This is a standard multipart message. We should
					 * recursively walk thorugh all of the parts and decode
					 * them, appending as children to the current part
					 */

					xml_part = parent_part.createPart("multi");

					MimeMultipart m = (MimeMultipart) p.getContent();
					for (int i = 0; i < m.getCount(); i++) {
						int currentPart = parseMIMEContent(m.getBodyPart(i), xml_part, msgid, mimeCount);
						mimeCount = currentPart;
					}
				}
			}
		} catch (java.io.IOException ex) {
			ex.printStackTrace();
		} catch (MessagingException ex) {
			throw ex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return mimeCount;
	}

	/**
	 * 
	 * @param folderhash:Folder
	 * @param msgnr:第几个消息
	 * @param partNum：消息的第几个BodyPart
	 * @return
	 * @throws MessagingException
	 * @throws IOException
	 */
	public BodyPart getMIMEPart(String folderhash, int msgnr, int partNum) throws NoSuchFolderException, MessagingException,
			IOException {
		Folder folder = getFolder(folderhash);
		// 打开Folder,并设置为READ_ONLY状态
		if (folder.isOpen() && folder.getMode() == Folder.READ_WRITE) {
			folder.close(false);
			folder.open(Folder.READ_ONLY);
		} else if (!folder.isOpen()) {
			folder.open(Folder.READ_ONLY);
		}

		MimeMessage m = (MimeMessage) folder.getMessage(msgnr);
		MimeMultipart mPart = (MimeMultipart) m.getContent();

		// 两层次?
		BodyPart returnPart = null;
		int tempPart = 0;
		int count = mPart.getCount();
		for (int k1 = 0; k1 < count; k1++) {
			BodyPart part = mPart.getBodyPart(k1);
			Object obj = part.getContent();

			if (obj instanceof MimeMultipart) {
				MimeMultipart subMPart = (MimeMultipart) obj;

				int subCount = subMPart.getCount();
				for (int k2 = 0; k2 < subCount; k2++) {
					BodyPart partb = subMPart.getBodyPart(k2);

					//如果是附件
					if (MessageParserCommon.isAttachment(partb)) {
						tempPart += 1;

						if (tempPart == partNum)
							returnPart = partb;
					}
				}

				/**
				 * tempPart += 1; if(tempPart==partNum) returnPart = part;
				 */
			} else {
				// BodyPart partb = (BodyPart)obj;
				if (MessageParserCommon.isAttachment(part)) {
					tempPart += 1;
					if (tempPart == partNum)
						returnPart = part;
				}
			}
		}

		// int count = mPart.getCount();

		// BodyPart part = mPart.getBodyPart(0);
		// MimeMultipart mm = (MimeMultipart) part.getContent();
		// BodyPart partm = mm.getBodyPart(2);
		return returnPart;

		// BodyPart part = mPart.getBodyPart(partNum);

		// return part;
	}

	public ByteStore getMIMEPart(String msgid, String name) {
		if (mime_parts_decoded != null) {
			return (ByteStore) mime_parts_decoded.get(msgid + "/" + name);
		} else {
			return null;
		}
	}

	public Enumeration getMimeParts(String msgid) {
		if (mime_parts_decoded == null) {
			mime_parts_decoded = new Hashtable();
		}

		Enumeration enum1 = mime_parts_decoded.keys();
		Vector v = new Vector();
		while (enum1.hasMoreElements()) {
			String key = (String) enum1.nextElement();
			if (key.startsWith(msgid)) {
				v.addElement(key);
			}
		}

		return v.elements();
	}

	public void clearWork() {
		clearAttachments();
		model.clearWork();
	}

	public void prepareCompose() {
		// model.getWorkMessage().getFirstMessageTextPart().addContent("\n--\n",
		// 0);
		
		String signature = user.getSignature();
		
		String brBt = String.valueOf((char) 10);
		
		//String a = "289";
		signature = signature.replaceAll(brBt, "<br>");
				
		model.getWorkMessage().getFirstMessageTextPart().addContent(signature, 0);
	}

	/**
	 * This method removes all of the attachments of the current "work" message
	 */
	public void clearAttachments() {
		attachments_size = 0;

		XMLMessage xml_message = model.getWorkMessage();

		String msgid = xml_message.getAttribute("msgid");

		Enumeration enum1 = getMimeParts(msgid);
		attachments_size = 0;
		while (enum1.hasMoreElements()) {
			mime_parts_decoded.remove((String) enum1.nextElement());
		}
	}

	/**
	 * This method returns a table of attachments for the current "work" message
	 */
	public Hashtable getAttachments() {
		Hashtable hash = new Hashtable();
		XMLMessage xml_message = model.getWorkMessage();

		String msgid = xml_message.getAttribute("msgid");

		Enumeration enum1 = getMimeParts(msgid);
		while (enum1.hasMoreElements()) {
			String key = (String) enum1.nextElement();
			String filename = key.substring(msgid.length() + 1);
			hash.put(filename, mime_parts_decoded.get(key));
		}

		return hash;
	}

	/**
	 * This method returns the attachment with the given name of the current
	 * "work" message
	 */
	public ByteStore getAttachment(String key) {
		XMLMessage xml_message = model.getWorkMessage();
		String msgid = xml_message.getAttribute("msgid");

		return getMIMEPart(msgid, key);
	}

	/**
	 * Add an attachment to the current work message.
	 * 
	 * @param name
	 *            Name of the attachment (e.g. filename)
	 * @param bs
	 *            The contents of the attachment, as a ByteStore object
	 * @param description
	 *            A short description of the contents (will be used as the
	 *            "Description:" header
	 */
	public void addWorkAttachment(String name, ByteStore bs, String description) throws WebMailException {
		XMLMessage xml_message = model.getWorkMessage();
		XMLMessagePart xml_multipart = xml_message.getFirstMessageMultiPart();

		String msgid = xml_message.getAttribute("msgid");

		bs.setDescription(description);

		Enumeration enum1 = getMimeParts(msgid);
		attachments_size = 0;
		while (enum1.hasMoreElements()) {
			ByteStore b = (ByteStore) mime_parts_decoded.get((String) enum1.nextElement());
			attachments_size += b.getSize();
		}

		int max_size = 0;
		try {
			max_size = Integer.parseInt(parent.getStorage().getConfig("MAX ATTACH SIZE"));
		} catch (NumberFormatException e) {
			parent.getStorage().log(Storage.LOG_WARN, "Invalid setting for parameter \"MAX ATTACH SIZE\". Must be a number!");
		}

		if (attachments_size + bs.getSize() > max_size) {
			throw new WebMailException("Attachments are too big. The sum of the sizes may not exceed " + max_size + " bytes.");
		} else {
			mime_parts_decoded.put(msgid + "/" + name, bs);
			attachments_size += bs.getSize();
			XMLMessagePart xml_part = xml_multipart.createPart("binary");

			xml_part.setAttribute("filename", name);
			xml_part.setAttribute("size", bs.getSize() + "");
			xml_part.setAttribute("description", description);
			xml_part.setAttribute("content-type", bs.getContentType().toLowerCase());
		}

		// setEnv();
		// XMLCommon.debugXML(model.getRoot());
	}

	/**
	 * Remove the attachment with the given name from the current work message.
	 */
	/**
	 * public void removeWorkAttachment(String name) { XMLMessage xml_message =
	 * model.getWorkMessage(); XMLMessagePart xml_multipart =
	 * xml_message.getFirstMessageMultiPart();
	 * 
	 * String msgid = xml_message.getAttribute("msgid");
	 * 
	 * mime_parts_decoded.remove(msgid + "/" + name);
	 * 
	 * Enumeration enum1 = getMimeParts(msgid); attachments_size = 0; while
	 * (enum1.hasMoreElements()) { ByteStore b = (ByteStore)
	 * mime_parts_decoded.get((String) enum1 .nextElement()); attachments_size +=
	 * b.getSize(); }
	 * 
	 * enum1 = xml_multipart.getParts(); XMLMessagePart oldpart = null; while
	 * (enum1.hasMoreElements()) { XMLMessagePart tmp = (XMLMessagePart)
	 * enum1.nextElement(); if (tmp.getAttribute("filename") != null &&
	 * tmp.getAttribute("filename").equals(name)) { oldpart = tmp; break; } } if
	 * (oldpart != null) { xml_multipart.removePart(oldpart); } setEnv(); //
	 * XMLCommon.debugXML(model.getRoot()); }
	 */

	/**
	 * Store a message in the environment for further processing.
	 */
	public void storeMessage(HTTPRequestHeader head) {
		XMLMessage xml_message = model.getWorkMessage();
		XMLMessagePart xml_textpart = xml_message.getFirstMessageTextPart();

		/* Store the already typed message if necessary/possible */
		if (head.isContentSet("BODY")) {
			StringBuffer content = new StringBuffer();

			/**
			 * Because the data transfered through HTTP should be ISO8859_1,
			 * HTTPRequestHeader is also ISO8859_1 encoded. Furthermore, the
			 * string we used in brwoser is UTF-8 encoded, hence we have to
			 * transcode the stored variables from ISO8859_1 to UTF-8 so that
			 * the client browser displays correctly.
			 */
			String bodyString;
			try {
				bodyString = new String(head.getContent("BODY").getBytes("ISO8859_1"), "UTF-8");
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				bodyString = head.getContent("BODY");
			}

			/**
			 * //If the user enabled "break line", then do it! if
			 * (user.wantsBreakLines()) { // StringTokenizer tok=new //
			 * StringTokenizer(head.getContent("BODY"),"\n"); StringTokenizer
			 * tok = new StringTokenizer(bodyString, "\n"); while
			 * (tok.hasMoreTokens()) { String line = tok.nextToken();
			 * Enumeration enum1 = Helper.breakLine(line, user
			 * .getMaxLineLength(), Helper.getQuoteLevel(line)); while
			 * (enum1.hasMoreElements()) { content.append((String)
			 * enum1.nextElement()).append( '\n'); } } } else { //
			 * content.append(head.getContent("BODY"));
			 * content.append(bodyString); // Modified by exce, end }
			 */

			content.append(bodyString);
			xml_textpart.removeAllContent();
			xml_textpart.addContent(content.toString(), 0);
		}

		if (head.isContentSet("TO")) {
			// xml_message.setHeader("TO",head.getContent("TO"));
			try {
				xml_message.setHeader("TO", new String(head.getContent("TO").getBytes("ISO8859_1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				xml_message.setHeader("TO", head.getContent("TO"));
			}
		}

		if (head.isContentSet("CC")) {
			// xml_message.setHeader("CC",head.getContent("CC"));
			try {
				xml_message.setHeader("CC", new String(head.getContent("CC").getBytes("ISO8859_1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				xml_message.setHeader("CC", head.getContent("CC"));
			}
		}

		if (head.isContentSet("BCC")) {
			// xml_message.setHeader("BCC",head.getContent("BCC"));
			try {
				xml_message.setHeader("BCC", new String(head.getContent("BCC").getBytes("ISO8859_1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				xml_message.setHeader("BCC", head.getContent("BCC"));
			}
		}

		if (head.isContentSet("REPLY-TO")) {
			// xml_message.setHeader("REPLY-TO",head.getContent("REPLY-TO"));
			try {
				xml_message.setHeader("REPLY-TO", new String(head.getContent("REPLY-TO").getBytes("ISO8859_1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				xml_message.setHeader("REPLY-TO", head.getContent("REPLY-TO"));
			}
		}

		if (head.isContentSet("SUBJECT")) {
			// xml_message.setHeader("SUBJECT",head.getContent("SUBJECT"));
			try {
				xml_message.setHeader("SUBJECT", new String(head.getContent("SUBJECT").getBytes("ISO8859_1"), "UTF-8"));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				xml_message.setHeader("SUBJECT", head.getContent("SUBJECT"));
			}
		}

		// 附件的处理
		try {
			Enumeration em = head.getContentKeys();
			while (em.hasMoreElements()) {
				String key = (String) em.nextElement();
				if (key.length() > 5 && key.substring(0, 5).equalsIgnoreCase("file_")) {
					// 为附件

					ByteStore bs = (ByteStore) head.getObjContent(key);
					String fileName = bs.getName();

					// Transcode file name
					if (!((fileName == null) || fileName.equals(""))) {
						int offset = fileName.lastIndexOf("\\"); // This is
						// no
						// effect.
						// It seems
						// that
						// MimeBodyPart.getFileName()
						// filters
						// '\'
						// character.
						fileName = fileName.substring(offset + 1);
						fileName = new String(fileName.getBytes("ISO8859_1"), "UTF-8");
						bs.setName(fileName);
					}

					if (bs != null && bs.getSize() > 0) {
						addWorkAttachment(bs.getName(), bs, bs.getName());
					}

				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			// throw new DocumentNotFoundException("Could not attach file.
			// (Reason: "+e.getMessage()+")");
		}

		/** @todo why need setEnv() */
		// setEnv();
	}

	/**
	 * Connect to all Mailhosts
	 * 
	 * @deprecated Should use refreshFolderInformation now.
	 */
	public void connectAll() {
		refreshFolderInformation();
	}

	/**
	 * Get a childfolder of a rootfolder for a specified hash value
	 */
	public Folder getChildFolder(Folder root, String folderhash) {
		return getFolder(folderhash);
	}

	/**
	 * Get the folder with the given hashvalue.
	 * 
	 * @returns Folder with the given hashvalue
	 */
	public Folder getFolder(String folderhash) {
		return (Folder) folders.get(folderhash);
	}

	/**
	 * This method tries to generate a unique folder identifier for the given
	 * folder. This method generates an MD5 sum over the complete folder URL, if
	 * possible. 对给定的Folder,产生一个唯一的标识
	 */
	protected String generateFolderHash(Folder folder) {
		String id = Integer.toHexString(folder.hashCode());
		// If possible, use the MD5-Sum for the folder ID because it is
		// persistant over sessions
		try {
			MD5 md5 = new MD5(folder.getURLName());

			id = md5.asHex();
		} catch (MessagingException ex) {
		}

		return id;
	}

	/**
	 * Construct the folder subtree for the given folder and append it to
	 * xml_parent. 根据给定的Folder,构建其所有子文件夹的subTree.
	 * 
	 * @param folder
	 *            the folder where we begin
	 * @param xml_parent
	 *            the XML Element where the gathered information will be
	 *            appended
	 * @param subscribed_only
	 *            Only list subscribed folders
	 * @returns maximum depth of the folder tree (needed to calculate the
	 *          necessary columns in a table)
	 */
	protected int getFolderTree(Folder folder, Element xml_parent, boolean subscribed_only) {
		int depth = 1;

		String id = generateFolderHash(folder);

		boolean holds_folders = false, holds_messages = false;
		Element xml_folder;
		try {
			holds_folders = (folder.getType() & Folder.HOLDS_FOLDERS) == Folder.HOLDS_FOLDERS;
			holds_messages = (folder.getType() & Folder.HOLDS_MESSAGES) == Folder.HOLDS_MESSAGES;

			// 创建该Element
			xml_folder = model.createFolder(id, folder.getName(), holds_folders, holds_messages);

			if (folder.isSubscribed()) {
				xml_folder.addAttribute("subscribed", "true");
			} else {
				xml_folder.addAttribute("subscribed", "false");
			}
		} catch (MessagingException ex) {
			xml_folder = model.createFolder(id, folder.getName(), holds_folders, holds_messages);
			xml_folder.attributeValue("error", ex.getMessage());
		}

		folders.put(id, folder);

		try {
			/*
			 * This folder can contain messages
			 * 如果该folder可以存储Message,则需要计算Message的数量
			 */
			if (holds_messages) {
				// 在FOLDER节点加入MESSAGELIST
				Element messagelist = model.createMessageList();

				// 总邮件
				int total_messages = folder.getMessageCount();
				// 未读邮件
				int new_messages = folder.getUnreadMessageCount();

				if ((total_messages == -1 || new_messages == -1) || !folder.isOpen()) {
					folder.open(Folder.READ_ONLY);
					total_messages = folder.getMessageCount();
					new_messages = folder.getUnreadMessageCount();
				}
				folder.close(false);

				messagelist.addAttribute("total", total_messages + "");
				messagelist.addAttribute("new", new_messages + "");

				xml_folder.add(messagelist);
			}
		} catch (MessagingException ex) {
			xml_folder.attributeValue("error", ex.getMessage());
		}

		try {
			/*
			 * There are subfolders, get them! 如果该Folder有子Folder，则需要获取
			 */
			if (holds_folders) {
				Folder[] subfolders;

				/*
				 * If the user only wanted to see subscribed folders, call
				 * listSubscribed otherwise call list()
				 */
				if (subscribed_only) {
					try {
						subfolders = folder.listSubscribed();
					} catch (MessagingException ex) {
						System.err.println("Subscribe not supported");
						subfolders = folder.list();
					}
				} else {
					subfolders = folder.list();
				}

				int max_tree_depth = 0;
				/*
				 * Recursiveley add subfolders to the XML model 递归把子folder加入XML
				 */
				for (int i = 0; i < subfolders.length; i++) {
					int tree_depth = getFolderTree(subfolders[i], xml_folder, subscribed_only);
					if (tree_depth > max_tree_depth) {
						max_tree_depth = tree_depth;
					}
				}

				depth += max_tree_depth;
			}
		} catch (MessagingException ex) {
			xml_folder.attributeValue("error", ex.getMessage());
		}

		xml_parent.add(xml_folder);

		return depth;
	}

	public void refreshFolderInformation() {
		refreshFolderInformation(false);
	}

	/**
	 * Refresh Information about folders. Tries to connect folders that are not
	 * yet connected. 获取Folders的信息
	 */
	public void refreshFolderInformation(boolean subscribed_only) {
		// Env设置
		setEnv();

		if (folders == null)
			folders = new Hashtable();

		Folder cur_folder = null;
		String cur_mh_id = "";
		Enumeration mailhosts = user.mailHosts();

		int max_depth = 0;
		while (mailhosts.hasMoreElements()) {
			cur_mh_id = (String) mailhosts.nextElement();

			/** @todo */
			// /---------------------------
			currentMailHostName = cur_mh_id;
			// -----------------------

			MailHostData mhd = user.getMailHost(cur_mh_id);

			URLName url = new URLName(mhd.getHostURL());

			Element mailhost = model.createMailhost(mhd.getName(), mhd.getID(), url.toString());

			int depth = 0;
			try {
				// 取得根目录
				cur_folder = getRootFolder(cur_mh_id);

				/* Cannot unsubscribe root folder! */
				try {
					cur_folder.setSubscribed(true);
				} catch (MessagingException ex) {
					// Only IMAP supports subscription
				}

				/*
				 * Here we try to determine the remote IMAP or POP host. There
				 * is no problem if this fails (it will most likely for POP3),
				 * so the exception is caught and not handled
				 * 
				 * getFolderTree:得到该mailhost下的所有Folder
				 */
				try {
					if (cur_folder.getFolder("~" + mhd.getLogin() + "/mail").exists()) {
						/*
						 * Washington University stores user mailboxes as
						 * ~user/mail/...
						 */
						depth = getFolderTree(cur_folder.getFolder("INBOX"), mailhost, subscribed_only);
						if (depth > max_depth) {
							max_depth = depth;
						}
						depth = getFolderTree(cur_folder.getFolder("~" + mhd.getLogin() + "/mail"), mailhost, subscribed_only);
					} else if (cur_folder.getFolder("INBOX").exists()) {
						/**
						 * 对于Domino邮件服务器，其Folder结构为getDefaultFolder-->(INBOX,Trash等)
						 * 对于其它，其Folder结构为getDefaultFolder-->INBOX-->Trash等
						 */
						if (cur_folder.getFolder("INBOX").list().length == 0) {
							depth = getFolderTree(cur_folder, mailhost, subscribed_only);
						} else {
							depth = getFolderTree(cur_folder.getFolder("INBOX"), mailhost, subscribed_only);
						}
					}
				} /*
					 * If it didn't work it failed in the "if" statement, since
					 * "getFolderTree" doesn't throw exceptions so what we want
					 * to do is to simply construct the folder tree for INBOX
					 */
				catch (MessagingException ex) {
					depth = getFolderTree(cur_folder.getFolder("INBOX"), mailhost, subscribed_only);
				}
			} catch (MessagingException ex) {
				// Here a more serious exception has been caught (Connection
				// failed)
				System.out.println(ex.getMessage());
				mailhost.attributeValue("error", ex.getMessage());
				
				//parent.getStorage().log(Storage.LOG_WARN,
				//		"Error connecting to mailhost (" + url.toString() + "): " + ex.getMessage());
			}

			if (depth > max_depth) {
				max_depth = depth;
			}

			model.addMailhost(mailhost);

		}

		model.setStateVar("max folder depth", (1 + max_depth) + "");
	}

	// 更新并获取邮件Folder的信息
	public void refreshFolderInformation(String folderhash) {
		Folder folder = getFolder(folderhash);
		Element xml_folder = model.getFolder(folderhash);

		if (xml_folder.attributeValue("holds_messages").toLowerCase().equals("true")) {
			try {
				Element messagelist = model.createMessageList();

				int total_messages = folder.getMessageCount();
				int new_messages = folder.getUnreadMessageCount();

				if ((total_messages == -1 || new_messages == -1) && !folder.isOpen()) {
					// 先open
					folder.open(Folder.READ_ONLY);

					total_messages = folder.getMessageCount();
					new_messages = folder.getUnreadMessageCount();
				}

				if (folder.isOpen())
					folder.close(false);

				messagelist.addAttribute("total", total_messages + "");
				messagelist.addAttribute("new", new_messages + "");

				// 从XMLUserModel中，先刪除MESSAGELIST
				model.removeMessageList(xml_folder);

				// 再添加MESSAGELIST
				xml_folder.add(messagelist);

			} catch (MessagingException ex) {
				Attribute attr = xml_folder.attribute("error");
				if (attr == null) {
					xml_folder.addAttribute("error", ex.getMessage());
				} else {
					xml_folder.attributeValue(ex.getMessage());
				}
			}
		}
	}

	/**
	 * Try to subscribe to a folder (i.e. unhide it)
	 */
	public void subscribeFolder(String folderhash) {
		Folder folder = getFolder(folderhash);

		// Only IMAP supports subscription...
		try {
			folder.setSubscribed(true);
		} catch (MessagingException ex) {
			// System.err.println("Folder subscription not supported");
		}
	}

	/**
	 * Try to unsubscribe from a folder (i.e. hide it)
	 */
	public void unsubscribeFolder(String folderhash) {
		Folder folder = getFolder(folderhash);

		// Only IMAP supports subscription...
		try {
			folder.setSubscribed(false);
		} catch (MessagingException ex) {
			// System.err.println("Folder subscription not supported");
		}
	}

	/**
	 * Subscribe all folders for a Mailhost Do it the non-recursive way: Uses a
	 * simple Queue :-) 使用Queue,非递归的方式
	 */
	public void setSubscribedAll(String id, boolean subscribed) throws MessagingException {
		// 取得邮件服务器的Root Folder(即default folder)
		Folder folder = getRootFolder(id);

		Queue q = new Queue();
		q.queue(folder);

		// Only IMAP supports subscription...
		try {
			while (!q.isEmpty()) {
				folder = (Folder) q.next();
				folder.setSubscribed(subscribed);

				Folder[] list = folder.list();
				for (int i = 0; i < list.length; i++) {
					q.queue(list[i]);
				}
			}
		} catch (MessagingException ex) {
		}
	}

	/**
	 * Disconnect from all Mailhosts
	 */
	public void disconnectAll() {
		Enumeration e = user.mailHosts();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			disconnect(name);
		}

		e = stores.keys();
		while (e.hasMoreElements()) {
			String name = (String) e.nextElement();
			Store st = (Store) stores.get(name);
			try {
				st.close();
				parent.getStorage().log(Storage.LOG_INFO, "Mail: Connection to " + st.toString() + " closed.");
			} catch (Exception ex) {
				parent.getStorage().log(Storage.LOG_WARN,
						"Mail: Failed to close connection to " + st.toString() + ". Reason: " + ex.getMessage());
			}

			stores.remove(name);
		}

		folders = null;
	}

	// 取得邮箱的Root Folder
	public Folder getRootFolder(String name) throws MessagingException {
		if (connections != null && connections.containsKey(name)) {
			return (Folder) connections.get(name);
		} else {
			return connect(name);
		}
	}

	// 根据host,login,password，链接mail服务器
	protected Store connectStore(String host, String protocol, String login, String password) throws MessagingException {
		/* Check whether the domain of this user allows to connect to the host */
		WebMailVirtualDomain vdom = parent.getStorage().getVirtualDomain(user.getDomain());
		if (!vdom.isAllowedHost(host)) {
			throw new MessagingException("You are not allowed to connect to this host");
		}

		/*
		 * Check if this host is already connected. Use connection if true,
		 * create a new one if false.
		 */
		Store st = (Store) stores.get(host + "-" + protocol);
		if (st == null) {
			st = mailsession.getStore(protocol);

			// 连接后放入stores
			stores.put(host + "-" + protocol, st);
		}

		/*
		 * Maybe this is a new store or this store has been disconnected.
		 * Reconnect if this is the case.
		 */
		if (!st.isConnected()) {
			try {
				// 连接Store
				st.connect(host, login, password);

				parent.getStorage().log(Storage.LOG_INFO, "Mail: Connection to " + st.toString() + ".");
			} catch (AuthenticationFailedException ex) {
				/* If login fails, try the login_password */
				if (!login_password.equals(password)
						&& parent.getStorage().getConfig("FOLDER TRY LOGIN PASSWORD").toUpperCase().equals("YES")) {
					st.connect(host, login, login_password);
					parent.getStorage().log(Storage.LOG_INFO,
							"Mail: Connection to " + st.toString() + ", second attempt with login password succeeded.");

				} else {
					throw ex;
				}
			}
		}
		return st;
	}

	/**
	 * Connect to mailhost "name" 连接到该MailHost,得到Default Folder
	 */
	public Folder connect(String name) throws MessagingException {
		// 根据名称,取得MailHostData
		MailHostData m = user.getMailHost(name);

		// m.getHostURL()：得到配置文件中该MailHost的MH_URI参数，如“imap://mail.kwchina.com”
		URLName url = new URLName(m.getHostURL());

		Store st = connectStore(url.getHost(), url.getProtocol(), m.getLogin(), m.getPassword());

		// System.err.println("Default folder:
		// "+st.getDefaultFolder().toString());

		Folder f = st.getDefaultFolder();

		// 如果该Store为IMAPStore,则取得其定额,及其使用的百分比
		addMailHostQuato(st, name);

		connections.put(name, f);

		parent.getStorage().log(Storage.LOG_INFO, "Mail: Folder " + f.toString() + " opened at store " + st.toString() + ".");
		return f;
	}

	// 添加相关MailHost的Quota信息
	public void addMailHostQuato(Store st, String mailhostName) {
		// 如果该Store为IMAPStore,则取得其定额,及其使用的百分比
		if (st instanceof IMAPStore) {
			try {
				Quota[] quotas;
				try {
					quotas = ((IMAPStore) st).getQuota("INBOX");
				} catch (MessagingException me) {
					quotas = null;
				}

				if (quotas != null) {
					for (int i = 0; i < quotas.length; i++) {
						Element quota = new DefaultElement("quota");
						quota.addAttribute("root", quotas[i].quotaRoot);

						Quota.Resource[] resources = quotas[i].resources;
						for (int j = 0; j < resources.length; j++) {

							Element res = new DefaultElement("resource");
							res.addAttribute("name", resources[j].name);
							res.addAttribute("limit", String.valueOf(resources[j].limit));
							res.addAttribute("usage", String.valueOf(resources[j].usage));
							res.addAttribute("limitkb", String.valueOf(resources[j].limit / 1024));
							res.addAttribute("usagekb", String.valueOf(resources[j].usage / 1024));
							res.addAttribute("usagepct", String.valueOf(Math.round(100 * resources[j].usage
									/ (double) resources[j].limit)));

							quota.add(res);
						}

						// 添加到XMLUserModel
						model.addMailHostQuato(mailhostName, quota);

					}
				}
			} catch (Exception e) {
				parent.getStorage().log(Storage.LOG_INFO, "Get MailHost- " + mailhostName + " quato error.");
			}

		}
	}

	/**
	 * Disconnect from mailhost "name"
	 */
	public void disconnect(String name) {
		try {
			Folder f = (Folder) connections.get(name);
			if (f != null && f.isOpen()) {
				f.close(true);
				Store st = ((Folder) connections.get(name)).getStore();
				// st.close();
				parent.getStorage().log(Storage.LOG_INFO,
						"Mail: Disconnected from folder " + f.toString() + " at store " + st.toString() + ".");
			} else {
				parent.getStorage().log(Storage.LOG_WARN, "Mail: Folder " + name + " was null???.");
			}
		} catch (MessagingException ex) {
			// Should not happen
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			// This happens when deleting a folder with an error
			ex.printStackTrace();
		} finally {
			connections.remove(name);
		}
	}

	/**
	 * Terminate this session. 用户退出，清除相关信息
	 * 
	 * This will expunge deleted messages, close all mailbox connections, save
	 * the user data and then remove this session from the session list,
	 * effectively destroying this session.
	 */
	public void logout() {
		if (!is_logged_out) {
			is_logged_out = true;
			
			// Expunge all folders
			expungeFolders();

			// Disconnect from all Mailhosts
			disconnectAll();

			// log out,set last login time
			user.logout();

			// Save User Data
			saveData();
			
			//从Storage的user_cache中清除
			parent.getStorage().removeUserData(user.getUserName());
			
			//parent.getStorage().deleteUserData(this.xml_path, user.getUserName(), user.getDomain());

			parent.getStorage().log(Storage.LOG_INFO, "WebMail: Session " + getSessionCode() + " logout.");

			// Make sure the session is invalidated
			if (sess != null) {
				try {
					Class srvltreq = Class.forName("javax.servlet.http.HttpSession");
					if (srvltreq.isInstance(sess)) {
						((javax.servlet.http.HttpSession) sess).invalidate();
					}
				} catch (Throwable t) {
				}
			}

			if (parent.getSession(getSessionCode()) != null) {
				parent.removeSession(this);
			}
		} else {
			System.err.println("WARNING: Session was already logged out. Ignoring logout request.");
		}
	}

	/**
	 * Check whether this session is already logged out. Useful to avoid loops.
	 */
	public boolean isLoggedOut() {
		return is_logged_out;
	}

	/**
	 * Return the session id that was generated for this session.
	 */
	public String getSessionCode() {
		return session_code;
	}

	/**
	 * Return the last access time of this session
	 * 
	 * @see TimeableConnection
	 */
	public long getLastAccess() {
		return last_access;
	}

	/**
	 * Update the last access time. Sets the last access time to the current
	 * time.
	 * 
	 * @see TimeableConnection
	 */
	public void setLastAccess() {
		last_access = System.currentTimeMillis();
		// System.err.println("Setting last access to session: "+last_access);
	}

	/**
	 * Handle a timeout for this session. This calls the logout method,
	 * effectively terminating this session.
	 * 
	 * @see TimeableConnection
	 * @see logout()
	 */
	public void timeoutOccured() {
		parent.getStorage().log(Storage.LOG_WARN, "WebMail: Session " + getSessionCode() + " timeout.");
		logout();
	}

	public long getTimeout() {
		long i = 600000;
		try {
			i = Long.parseLong(parent.getStorage().getConfig("session timeout"));
		} catch (NumberFormatException ex) {
			ex.printStackTrace();
		}
		return i;
	}

	public Locale getLocale() {
		return user.getPreferredLocale();
	}

	// 保存邮件用户信息
	public void saveData() {
		parent.getStorage().saveUserData(this.xml_path, user.getUserName(), user.getDomain(),user.getPassword());
	}

	protected static int[] getSelectedMessages(HTTPRequestHeader head, int max) {
		// System.err.print(" - select messages...");

		Enumeration e = head.getContent().keys();
		int _msgs[] = new int[max];
		int j = 0;

		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();

			if (s.toUpperCase().startsWith("MSGNR") && head.getContent(s).equals("on")) {
				try {
					_msgs[j] = Integer.parseInt(s.substring(6));
					// System.err.print(_msgs[j]+" ");
					j++;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		// System.err.println();

		int msgs[] = new int[j];
		for (int i = 0; i < j; i++) {
			msgs[i] = _msgs[i];
		}

		return msgs;
	}

	// 转发时，获取选中的需要转发的附件
	public int[] getForwardAttach(HTTPRequestHeader head, String folderhash) {
		Enumeration e = head.getContent().keys();
		int _parts[] = new int[10];
		int j = 0;

		while (e.hasMoreElements()) {
			String s = (String) e.nextElement();

			if (s.toUpperCase().startsWith("REMOVE") && head.getContent(s).equals("on")) {
				try {
					_parts[j] = Integer.parseInt(s.substring(7));
					// System.err.print(_msgs[j]+" ");
					j++;
				} catch (NumberFormatException ex) {
					ex.printStackTrace();
				}
			}
		}
		// System.err.println();

		int parts[] = new int[j];
		for (int i = 0; i < j; i++) {
			parts[i] = _parts[i];
		}

		return parts;
	}

	/**
	 * Expunge all folders that have messages waiting to be deleted
	 */
	public void expungeFolders() {
		if (need_expunge_folders != null) {
			Enumeration enum1 = need_expunge_folders.elements();
			while (enum1.hasMoreElements()) {
				String hash = (String) enum1.nextElement();
				if (user.wantsSetFlags()) {
					Folder f = getFolder(hash);
					try {
						if (f.isOpen()) {
							f.close(false);
						}

						f.open(Folder.READ_WRITE);
						// POP3 doesn't support expunge!
						try {
							f.expunge();
						} catch (MessagingException ex) {
						}
						f.close(true);
					} catch (MessagingException ex) {
						// XXXX
						ex.printStackTrace();
					}
				}
			}
		}
	}

	/**
	 * Change the Flags of the messages the user selected.
	 * 
	 */
	public void setFlags(String folderhash, HTTPRequestHeader head) throws MessagingException {
		String method = head.getContent("method");

		if (head.isContentSet("method") && method != null) {

			if (method.toUpperCase().equals("MOVE")) {
				// 邮件移动等
				copyMoveMessage(folderhash, head.getContent("TO"), head, true);
			} else if (method.toUpperCase().equals("DELETE")) {
				System.err.println("delete message");
				Folder folder = getFolder(folderhash);

				System.err.println("Processing Request Header...");

				/** Get selected messages */
				int msgs[] = getSelectedMessages(head, folder.getMessageCount());

				/** Get selected flags */
				Flags fl = new Flags(Flags.Flag.USER);
				// 删除的邮件
				fl = new Flags(Flags.Flag.DELETED);
				/**
				 * if (need_expunge_folders == null) { need_expunge_folders =
				 * new Vector(); } need_expunge_folders.addElement(folderhash);
				 */
				// }
				/**
				 * else if (head.getContent("MESSAGE FLAG").equals("SEEN")) { //
				 * 查看的邮件 fl = new Flags(Flags.Flag.SEEN); } else if
				 * (head.getContent("MESSAGE FLAG").equals("RECENT")) { // 最新邮件
				 * fl = new Flags(Flags.Flag.RECENT); } else if
				 * (head.getContent("MESSAGE FLAG").equals("ANSWERED")) { //
				 * 已回复邮件 fl = new Flags(Flags.Flag.ANSWERED); } else if
				 * (head.getContent("MESSAGE FLAG").equals("DRAFT")) { // 草稿 fl =
				 * new Flags(Flags.Flag.DRAFT); }
				 */

				/**
				 * boolean value = true; if
				 * (head.getContent("MARK").equals("UNMARK")) { value = false; }
				 */

				if (user.wantsSetFlags()) {
					// 如果用户需要设置邮件的标识(默认为true)
					if (folder.isOpen() && folder.getMode() == Folder.READ_ONLY) {
						folder.close(false);
						folder.open(Folder.READ_WRITE);
					} else if (!folder.isOpen()) {
						folder.open(Folder.READ_WRITE);
					}

					folder.setFlags(msgs, fl, true);

					/** @todo autoexpunge */
					folder.close(true);
					/**
					 * if (user.getBoolVar("autoexpunge")) { folder.close(true);
					 * if (need_expunge_folders != null) {
					 * need_expunge_folders.removeElement(folderhash); } } else {
					 * folder.close(false); }
					 */
				}

				refreshFolderInformation(folderhash);

			} else if (method.toUpperCase().equals("COPY")) {
				// 邮件复制等
				// copyMoveMessage(folderhash, head.getContent("TO"), head,
				// true);
			}
		}

	}

	/**
	 * Copy or move the selected messages from folder fromfolder to folder
	 * tofolder.
	 */
	public void copyMoveMessage(String fromfolder, String tofolder,
			HTTPRequestHeader head, boolean move) throws MessagingException {
		Folder from = getFolder(fromfolder);
		Folder to = getFolder(tofolder);
		
		if (user.wantsSetFlags()) {
			if (from.isOpen() && from.getMode() == Folder.READ_ONLY) {
				from.close(false);
				from.open(Folder.READ_WRITE);
			} else if (!from.isOpen()) {
				from.open(Folder.READ_WRITE);
			}
			if (to.isOpen() && to.getMode() == Folder.READ_ONLY) {
				to.close(false);
				to.open(Folder.READ_WRITE);
			} else if (!to.isOpen()) {
				to.open(Folder.READ_WRITE);
			}
		} else {
			if (!from.isOpen()) {
				from.open(Folder.READ_ONLY);
			}
			if (to.isOpen() && to.getMode() == Folder.READ_ONLY) {
				to.close(false);
				to.open(Folder.READ_WRITE);
			} else if (!to.isOpen()) {
				to.open(Folder.READ_WRITE);
			}
		}
		
		try{
			int m[] = getSelectedMessages(head, from.getMessageCount());
			Message msgs[] = from.getMessages(m);		
			from.copyMessages(msgs, to);
			
			if (move && user.wantsSetFlags()) {
				from.setFlags(m, new Flags(Flags.Flag.DELETED), true);
				//if (user.getBoolVar("autoexpunge")) {
				if(true){
					from.close(true);
					to.close(true);
				} else {
					if (need_expunge_folders == null) {
						need_expunge_folders = new Vector();
					}
					need_expunge_folders.addElement(fromfolder);
					from.close(false);
					to.close(false);
				}
			} else {
				from.close(false);
				
				//user.getBoolVar("autoexpunge")
				if (true) {
					to.close(true);
				} else {
					to.close(false);
				}
			}
		}catch(Exception ex){
			throw new MessagingException("-Mail Server Error: 删除邮件到垃圾箱时发生错误,请查看您是否超出邮件限额!");
		}
		
		refreshFolderInformation(fromfolder);
		refreshFolderInformation(tofolder);
	}

	/**
	 * Change a user's configuration. Header fields given in the requestheader
	 * are parsed and turned into user options (probably should not be in
	 * WebMailSession but in a plugin or something; this is very hacky).
	 */
	public void changeSetup(HTTPRequestHeader head) throws WebMailException {
		// 先把所有Boolean参数设置为false
		user.resetBoolVars();

		// 设置INTVAR,BOOLVAR参数值
		Enumeration contentkeys = head.getContentKeys();
		while (contentkeys.hasMoreElements()) {
			String key = ((String) contentkeys.nextElement()).toLowerCase();
			if (key.startsWith("intvar")) {
				try {
					// 页面传递过来的参数以"intvar%","boolvar%"等开头
					long value = Long.parseLong(head.getContent(key));
					user.setIntVar(key.substring(7), value);
				} catch (NumberFormatException ex) {
					System.err.println("Warning: Remote provided illegal intvar in request header: \n(" + key + ","
							+ head.getContent(key) + ")");
				}
			} else if (key.startsWith("boolvar")) {
				boolean value = head.getContent(key).toUpperCase().equals("ON");
				user.setBoolVar(key.substring(8), value);
			}
		}

		// 个性化签名，个人姓名		
		String signature = head.getContent("SIGNATURE");
		if(signature!=null)
			user.setSignature(MailBasicService.transTagToCode(signature));

		String fullName = head.getContent("FULLNAME");
		if(fullName!=null)
			user.setFullName(MailBasicService.transTagToCode(fullName));
	
		
		/**
		String signature = head.getContent("SIGNATURE");
		String fullName = head.getContent("FULLNAME");
		try { 
			//signature = new String(signature.getBytes("GBK"),"iso8859-1");
			user.setSignature(signature); 
			
			//fullName = new String(fullName.getBytes("GBK"), "iso8859-1");
			user.setFullName(fullName); 
		} catch (UnsupportedEncodingException e) { 
			//e.printStackTrace();
			user.setSignature(signature);
			user.setFullName(fullName); 
		}*/
		

		// Email Address
		String email = head.getContent("EMAIL");
		if(email!=null)
			user.setEmail(MailBasicService.transTagToCode(email));

		/** 把XML输出查看 */
		/**
		 * try { Writer out = new OutputStreamWriter(new FileOutputStream(
		 * "c:\\test_1.xml"), "GBK"); OutputFormat format =
		 * OutputFormat.createPrettyPrint(); // 指定XML编码
		 * format.setEncoding("GBK");
		 * 
		 * XMLWriter writer = new XMLWriter(out, format);
		 * writer.write(user.getRoot()); out.flush(); out.close(); } catch
		 * (Exception ex) { }
		 */

		// if (!head.getContent("PASSWORD").equals("")) {
		// user.setPassword(head.getContent("PASSWORD"),
		// head.getContent("VERIFY"));
		/**
		 * com.kwchina.webmail.authenticator.Authenticator auth =
		 * parent.getStorage() .getAuthenticator(); if
		 * (auth.canChangePassword()) { auth.changePassword(user,
		 * head.getContent("PASSWORD"), head.getContent("VERIFY")); } else {
		 * throw new InvalidDataException( getStringResource("EX NO CHANGE
		 * PASSWORD")); }
		 */
		// }
		/**
		 * user.setPreferredLocale(head.getContent("LANGUAGE"));
		 * user.setTheme(head.getContent("THEME"));
		 */

		if (head.isContentSet("SENTFOLDER")) {
			// System.err.println("SENTFOLDER=" +
			// head.getContent("SENTFOLDER"));
			user.setSentFolder(head.getContent("SENTFOLDER"));
		}

		// Not sure if this is really necessary:
		// refreshFolderInformation(true);
		setEnv();

		// 把系统信息、用户信息加入到XMLUserModel
		model.update();
	}

	/**
	 * Add the mailbox with the given parameters to this user's configuration.
	 * Subscribe all folders on startup (the user can later unsubscribe them)
	 * and update the model.
	 * 
	 * @param name
	 *            Name for the mailbox (used for identification within the
	 *            session)
	 * @param protocol
	 *            The protocol used for this mailbox (most likely IMAP or POP3)
	 * @param host
	 *            The hostname of the host this mailbox lives on
	 * @param login
	 *            Login name the user provided for the host
	 * @param password
	 *            Password the user provided to the given login
	 */
	public void addMailbox(String name, String protocol, String host, String login, String password) throws MessagingException {
		disconnectAll();
		String host_url = protocol + "://" + host;
		user.addMailHost(name, host_url, login, password);
		Enumeration enum1 = user.mailHosts();
		while (enum1.hasMoreElements()) {
			String id = (String) enum1.nextElement();
			if (user.getMailHost(id).getName().equals(name)) {
				setSubscribedAll(id, true);
				break;
			}
		}
		model.update();
	}

	/**
	 * Remove the mailbox with the given name. Will first disconnect all
	 * mailboxes, remove the given mailbox and then update the model.
	 * 
	 * @param name
	 *    Name of the mailbox that is to be removed.
	 */
	public void removeMailbox(String name) {
		disconnectAll();
		user.removeMailHost(name);
		model.update();
		// Should be called from FolderSetup Plugin
		// refreshFolderInformation(true);
	}

	public void setAddToFolder(String id) {
		model.setStateVar("add to folder", id);
	}

	public void addFolder(String toid, String name, boolean holds_messages, boolean holds_folders) throws MessagingException {

		Folder parent = getFolder(toid);
		
		Folder folder = parent.getFolder(name);
		if (!folder.exists()) {
			int type = 0;
			if (holds_messages) {
				type += Folder.HOLDS_MESSAGES;
			}
			if (holds_folders) {
				type += Folder.HOLDS_FOLDERS;
			}
			
			folder.create(type);
		}
		
		// refreshFolderInformation();
	}
	
	public void editFolder(String toid, String name, String oldName) throws MessagingException {
		Folder parent = getFolder(toid);
		
		Folder newFolder = parent.getFolder(name);
		
		Folder folder = parent.getFolder(oldName);
		if (folder.exists()) {
			folder.renameTo(newFolder);
		}
	}
	

	public void removeFolder(String id, boolean recurse) throws MessagingException {
		
		Folder folder = getFolder(id);
		
		//如果该文件夹是打开的，则需要关闭
		if(folder.isOpen())
			folder.close(false);
		
		folder.delete(recurse);

		// Should be called from FolderSetup Plugin
		// refreshFolderInformation();
	}

	public String getEnv(String key) {
		return "";
	}

	public void setEnv(String key, String value) {
	}

	public void setException(Exception ex) {
		model.setException(ex);
	}

	// 在XMLUserModel中设置相关参数(设置相关STATEDATA参数)
	public void setEnv() {
		// This will soon replace "ENV":
		model.setStateVar("base uri", parent.getBasePath());
		model.setStateVar("img base uri", parent.getImageBasePath());
		// + "/" + parent.getDefaultLocale().getLanguage() + "/"
		// + parent.getDefaultTheme());
		model.setStateVar("webmail version", parent.getVersion());
		model.setStateVar("operating system", System.getProperty("os.name") + " " + System.getProperty("os.version") + "/"
				+ System.getProperty("os.arch"));
		model.setStateVar("java virtual machine", System.getProperty("java.vendor") + " " + System.getProperty("java.vm.name")
				+ " " + System.getProperty("java.version"));

		/**
		 * model.setStateVar("base uri", parent.getBasePath());
		 * model.setStateVar("img base uri", parent.getImageBasePath() + "/" +
		 * user.getPreferredLocale().getLanguage() + "/" + user.getTheme());
		 * 
		 * model.setStateVar("webmail version", parent.getVersion());
		 * model.setStateVar("operating system", System.getProperty("os.name") + " " +
		 * System.getProperty("os.version") + "/" +
		 * System.getProperty("os.arch")); model.setStateVar("java virtual
		 * machine", System .getProperty("java.vendor") + " " +
		 * System.getProperty("java.vm.name") + " " +
		 * System.getProperty("java.version"));
		 */

		model.setStateVar("last login", user.getLastLogin());
		model.setStateVar("first login", user.getFirstLogin());
		model.setStateVar("session id", session_code);
		model.setStateVar("date", MiscCommonMethod.formatDate(System.currentTimeMillis()));
		model.setStateVar("max attach size", parent.getStorage().getConfig("MAX ATTACH SIZE"));
		model.setStateVar("current attach size", "" + attachments_size);

		// Add all languages to the state
		/**
		 * model.removeAllStateVars("language"); String lang =
		 * parent.getConfig("languages"); StringTokenizer tok = new
		 * StringTokenizer(lang, " "); while (tok.hasMoreTokens()) { String t =
		 * tok.nextToken(); model.addStateVar("language", t);
		 * model.removeAllStateVars("themes_" + t); StringTokenizer tok2 = new
		 * StringTokenizer(parent .getConfig("THEMES_" + t.toUpperCase()), " ");
		 * while (tok2.hasMoreElements()) { model.addStateVar("themes_" + t,
		 * (String) tok2.nextToken()); } }
		 */

		model.removeAllStateVars("protocol");
		Provider[] stores = parent.getStoreProviders();
		for (int i = 0; i < stores.length; i++) {
			model.addStateVar("protocol", stores[i].getProtocol());
		}

		/** @todo: 语言和地区，不考虑 */
		/**
		 * model.setStateVar("themeset", "themes_" +
		 * user.getPreferredLocale().getLanguage().toLowerCase());
		 */
	}

	public UserData getUser() {
		return user;
	}

	public String getUserName() {
		return user.getLogin();
	}

	/** @tod */
	public String getPassword() {
		return this.login_password;
	}

	// 获取用户文件路径
	public String getXmlPath() {
		return this.xml_path;
	}

	/**
	 * @todo 当前工作的MailHost Name
	 */
	public String getCurrentMailHostName() {
		return this.currentMailHostName;
	}

	public InetAddress getRemoteAddress() {
		return remote;
	}

	public Hashtable getActiveConnections() {
		return connections;
	}

	public void setSent(boolean b) {
		sent = b;
	}

	public boolean isSent() {
		return sent;
	}

	/**
	 * private String formatDate(long date) { TimeZone tz =
	 * TimeZone.getDefault(); DateFormat df =
	 * DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.DEFAULT,
	 * getLocale()); df.setTimeZone(tz); String now = df.format(new Date(date));
	 * return now; }
	 */

	public void handleTransportException(SendFailedException e) {
		model.setStateVar("send status", e.getNextException().getMessage());
		model.setStateVar("valid sent addresses", Helper.joinAddress(e.getValidSentAddresses()));
		model.setStateVar("valid unsent addresses", Helper.joinAddress(e.getValidUnsentAddresses()));
		model.setStateVar("invalid addresses", Helper.joinAddress(e.getInvalidAddresses()));
		sent = true;
	}

	// ----------------------如下为关于用户通讯录信息---------------------------------

	// 添加通讯录组别
	public void addAddressGroup(HTTPRequestHeader head) throws UnsupportedEncodingException {
		// 分组名
		String groupName = head.getContent("grpname");
		String groupId = head.getContent("sid");
		String personIds = head.getContent("grpmember");

		user.getAddress().saveAddressGroup(groupName, groupId, personIds);

		// 编辑了组信息，需要更新XMLUserModel中的信息
		model.update();
	}

	// 设定当前操作组别
	public Element setCurrentGroup(String groupId) {
		Element xml_current = model.setCurrentGroup(groupId);

		// 显示的第一个Message
		// XMLCommon.setAttributeValue(xml_current, "first_msg", "");

		return xml_current;
	}

	// 删除组别信息
	public void deleteGroup(HTTPRequestHeader head) {
		String groupId = head.getContent("sid");
		if (groupId != null && !groupId.equals("")) {
			user.getAddress().removeGroup(groupId);
		}
	}

	// 添加修改通讯录人员
	public void addAddressPerson(HTTPRequestHeader head) throws UnsupportedEncodingException {
		// 从页面获取人员相关信息，通过AddressPerson传递保存
		String id = head.getContent("sid");
		String name = MiscCommonMethod.stringEncoding(head.getContent("ab_name"), "UTF-8", "ISO8859_1");
		String email = MiscCommonMethod.stringEncoding(head.getContent("ab_email"), "UTF-8", "ISO8859_1");
		String nickName = MiscCommonMethod.stringEncoding(head.getContent("ab_nickname"), "UTF-8", "ISO8859_1");
		String mobile = MiscCommonMethod.stringEncoding(head.getContent("ab_mobile"), "UTF-8", "ISO8859_1");
		String job = MiscCommonMethod.stringEncoding(head.getContent("ab_job"), "UTF-8", "ISO8859_1");
		String imgoogle = MiscCommonMethod.stringEncoding(head.getContent("ab_imgoogle"), "UTF-8", "ISO8859_1");
		String immsn = MiscCommonMethod.stringEncoding(head.getContent("ab_immsn"), "UTF-8", "ISO8859_1");
		String imqq = MiscCommonMethod.stringEncoding(head.getContent("ab_imqq"), "UTF-8", "ISO8859_1");
		String imskype = MiscCommonMethod.stringEncoding(head.getContent("ab_imskype"), "UTF-8", "ISO8859_1");

		String homeTel = MiscCommonMethod.stringEncoding(head.getContent("ab_hometel"), "UTF-8", "ISO8859_1");
		String homeAddress = MiscCommonMethod.stringEncoding(head.getContent("ab_homeaddress"), "UTF-8", "ISO8859_1");
		String homeCity = MiscCommonMethod.stringEncoding(head.getContent("ab_homecity"), "UTF-8", "ISO8859_1");
		String homeState = MiscCommonMethod.stringEncoding(head.getContent("ab_homestate"), "UTF-8", "ISO8859_1");
		String homeZip = MiscCommonMethod.stringEncoding(head.getContent("ab_homezip"), "UTF-8", "ISO8859_1");
		String homeCountry = MiscCommonMethod.stringEncoding(head.getContent("ab_homecountry"), "UTF-8", "ISO8859_1");

		AddressPerson person = new AddressPerson();
		person.setId(id);
		person.setName(name);
		person.setEmail(email);
		person.setNickName(nickName);
		person.setMobile(mobile);
		person.setJob(job);
		person.setImgoogle(imgoogle);
		person.setImmsn(immsn);
		person.setImqq(imqq);
		person.setImskype(imskype);

		person.setHomeTel(homeTel);
		person.setHomeAddress(homeAddress);
		person.setHomeCity(homeCity);
		person.setHomeState(homeState);
		person.setHomeZip(homeZip);
		person.setHomeCountry(homeCountry);

		user.getAddress().savePerson(person);

		// 编辑了人员信息，需要更新XMLUserModel中的信息
		model.update();
	}

	// 设定当前操作人员
	public Element setCurrentPerson(String personId) {
		Element xml_current = model.setCurrentPerson(personId);

		// 显示的第一个Message
		// XMLCommon.setAttributeValue(xml_current, "first_msg", "");

		return xml_current;
	}

	// 删除通讯录人员信息
	public void deletePerson(HTTPRequestHeader head) {
		// 总共数量
		String itemNum = head.getContent("itemNum");
		int allNum = Integer.parseInt(itemNum);
		for (int k = 1; k <= allNum; k++) {
			String key = "deleteId-" + k;
			String personId = head.getContent(key);
			if (personId != null && !personId.equals("")) {
				// 删除选择的人员
				user.getAddress().removePerson(personId);
			}
		}

		// 编辑了人员信息，需要更新XMLUserModel中的信息
		model.update();
	}

	// 设定当前浏览通讯录的组别
	public Element setBrowseGroup(String groupId) {
		Element xml_current = model.setBrowseGroup(groupId);

		// 显示的第一个Message
		// XMLCommon.setAttributeValue(xml_current, "first_msg", "");

		return xml_current;
	}

	// 消除当前浏览通讯组别信息
	public void removeBrowseGroup() {
		model.removeBrowseGroup();
	}

}
