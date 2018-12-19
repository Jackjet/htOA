package com.kwchina.oa.meeting.util;

import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;
import java.util.UUID;

import javax.activation.DataHandler;
import javax.mail.Address;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.mail.util.ByteArrayDataSource;

import com.sun.org.apache.bcel.internal.generic.NEW;

public class EmailUtil {
//	private static String EMAIL_CONFIG = "email_config.properties";
//	private Properties emailProp = new Properties();
//	public EmailUtil() {
//		InputStream is = getClass().getResourceAsStream("/"+EMAIL_CONFIG);
//		try {
//			emailProp.load(is);
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//	}
	private String userId;
	private String password;
	
	public EmailUtil(String userId,String password) {
		this.userId = userId;
		this.password = password;
	}

	private class EmailAuthenticator extends Authenticator {
		protected PasswordAuthentication getPasswordAuthentication() {
//			String userId = emailProp.getProperty("userId", "testoa1@haitongauto.com");
//			String password = emailProp.getProperty("password", "HTpassword1234");
			return new PasswordAuthentication(userId, password);
		}
	}
		
		
	/*Authenticator auth = new Authenticator() {
	@Override
	   protected PasswordAuthentication getPasswordAuthentication() {
	       String username = "yin.zhang@highteam.com";    //大多数是你邮件@前面的部分
	       String pwd = "binglan212";
	       return new PasswordAuthentication(username, pwd);
	   }
	};*/

	
	public void sendMeetingRemind(String fromEmail,String toEmails,String subject,
			String startTime,String endTime,String location,String category,String content,String summary) throws Exception {

		try {
//			String fromEmail = emailProp.getProperty("fromEmail", "");
//			String toEmail=emailProp.getProperty("toEmail", ""); 
//			String fromEmail = "testoa1@haitongauto.com";
//			String toEmail = "testoa2@haitongauto.com";
			Properties props = new Properties();
			try {
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.host", "192.168.61.70");
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl", "true");

			} catch (Exception e) {
				e.printStackTrace();
			}

			Session session;
			Authenticator authenticator = new EmailAuthenticator();
			session = Session.getInstance(props, authenticator);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));//发件人
			
			//收件人为多个时
			Address address[] = new Address[toEmails.split(";").length];
			for(int i=0;i<toEmails.split(";").length;i++){
				address[i] = new InternetAddress(toEmails.split(";")[i]);
			}
//			Address address[] = {new InternetAddress(toEmail),new InternetAddress(fromEmail)}; 

			message.addRecipients(Message.RecipientType.TO, address);//收件人
			message.setSubject(subject);//主题
			StringBuffer buffer = new StringBuffer();
			buffer.append("BEGIN:VCALENDAR\n"
					+ "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
					+ "VERSION:2.0\n"
					+ "METHOD:REQUEST\n"
					+ "BEGIN:VEVENT\n"
					+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+address+"\n"
					+ "ORGANIZER:MAILTO:"+fromEmail+"\n"
//					+ "DTSTART:20120302T060000Z\n"
//					+ "DTEND:20120302T070000Z\n"
					+ "DTSTART;TZID=US-Eastern:"+startTime+"\n"
					+ "DTEND;TZID=US-Eastern:"+endTime+"\n"
					+ "LOCATION:"+location+"\n"
					+ "UID:"+UUID.randomUUID().toString()+"\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。
					+ "CATEGORIES:"+category+"\n"
					+ "DESCRIPTION:"+content+"\n"
					+ "SUMMARY:"+summary+"\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
					+ "TRIGGER:-PT15M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n"
					+ "END:VEVENT\n" + "END:VCALENDAR");
			BodyPart messageBodyPart = new MimeBodyPart();
			// 测试下来如果不这么转换的话，会以纯文本的形式发送过去，
			//如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求
			messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), 
					"text/calendar;method=REQUEST;charset=\"UTF-8\"")));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			
			
			 /*javax.mail.Transport transport = s.getTransport("smtp");     
			 transport.connect(host, (null == domainUser) ? username : domainUser, password);     
			 transport.sendMessage(message, message.getAllRecipients());     
			 transport.close();     */

			
			Transport.send(message);
		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	public void send() throws Exception {

		try {
//			String fromEmail = emailProp.getProperty("fromEmail", "");
//			String toEmail=emailProp.getProperty("toEmail", ""); 
			String fromEmail = "testoa1@haitongauto.com";
			String toEmail = "testoa2@haitongauto.com";
			Properties props = new Properties();
			try {
				props.put("mail.smtp.port", "25");
				props.put("mail.smtp.host", "192.168.61.70");
				props.put("mail.transport.protocol", "smtp");
				props.put("mail.smtp.auth", "true");
				props.put("mail.smtp.starttls.enable", "true");
				props.put("mail.smtp.ssl", "true");

			} catch (Exception e) {
				e.printStackTrace();
			}

			Session session;
			Authenticator authenticator = new EmailAuthenticator();
			session = Session.getInstance(props, authenticator);
			MimeMessage message = new MimeMessage(session);
			message.setFrom(new InternetAddress(fromEmail));//发件人
			
			//收件人为多个时
			Address address[] = {new InternetAddress(toEmail),new InternetAddress(fromEmail)}; 

			message.addRecipients(Message.RecipientType.TO, address);//收件人
			message.setSubject("测试-使用JavaMail发送Outlook会议邀请");//主题
			StringBuffer buffer = new StringBuffer();
			buffer.append("BEGIN:VCALENDAR\n"
					+ "PRODID:-//Microsoft Corporation//Outlook 9.0 MIMEDIR//EN\n"
					+ "VERSION:2.0\n"
					+ "METHOD:REQUEST\n"
					+ "BEGIN:VEVENT\n"
					+ "ATTENDEE;ROLE=REQ-PARTICIPANT;RSVP=TRUE:MAILTO:"+address+"\n"
					+ "ORGANIZER:MAILTO:"+fromEmail+"\n"
//					+ "DTSTART:20120302T060000Z\n"
//					+ "DTEND:20120302T070000Z\n"
					+ "DTSTART;TZID=US-Eastern:20140912T143000\n"
					+ "DTEND;TZID=US-Eastern:20140912T174500\n"
					+ "LOCATION:大会议室\n"
					+ "UID:"+UUID.randomUUID().toString()+"\n"//如果id相同的话，outlook会认为是同一个会议请求，所以使用uuid。
					+ "CATEGORIES:紧急会议\n"
					+ "DESCRIPTION:这里是会议内容\n"
					+ "SUMMARY:会议邀请测试\n" + "PRIORITY:5\n"
					+ "CLASS:PUBLIC\n" + "BEGIN:VALARM\n"
					+ "TRIGGER:-PT15M\n" + "ACTION:DISPLAY\n"
					+ "DESCRIPTION:Reminder\n" + "END:VALARM\n"
					+ "END:VEVENT\n" + "END:VCALENDAR");
			BodyPart messageBodyPart = new MimeBodyPart();
			// 测试下来如果不这么转换的话，会以纯文本的形式发送过去，
			//如果没有method=REQUEST;charset=\"UTF-8\"，outlook会议附件的形式存在，而不是直接打开就是一个会议请求
			messageBodyPart.setDataHandler(new DataHandler(new ByteArrayDataSource(buffer.toString(), 
					"text/calendar;method=REQUEST;charset=\"UTF-8\"")));
			Multipart multipart = new MimeMultipart();
			multipart.addBodyPart(messageBodyPart);
			message.setContent(multipart);
			Transport.send(message);
		} catch (MessagingException me) {
			me.printStackTrace();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		try {
			EmailUtil email = new EmailUtil("testoa1@haitongauto.com","HTpassword1234");
//			email.send();
			email.sendMeetingRemind("testoa1@haitongauto.com", "testoa2@haitongauto.com;", "发送测试主题", "20140913T081500", "20140913T113500", "大会议室", "会议类别", "这里是会议内容", "这里是会议概要");
//			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
//			System.out.println(sdf.format(sdf.parse("2014-09-13")));
//			System.out.println("2014-09-14".replace("-", ""));
			System.out.println("success");
//			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd");
//			System.out.println(sf.format(new Date()).toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
