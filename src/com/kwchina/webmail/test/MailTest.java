package com.kwchina.webmail.test;

import java.util.Properties;

import javax.mail.Folder;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Store;

public class MailTest {

	public MailTest() {
		
	}

	public static void main(String[] args) {
		/**
		Properties prop = new Properties();
		prop.setProperty("mail.store.protocol", "pop3");
		prop.setProperty("mail.smtp.autu", "true");
		prop.setProperty("mail.pop3.host","pop.tom.com");
		
		Session session = Session.getDefaultInstance(System.getProperties(),null);
		session.setDebug(true);
		
		try{
			Store store = session.getStore("pop3");
			store.connect("mail.kwchina.com","zhoulb@kwchina.com","755105");
			
			 Folder folder = store.getDefaultFolder();// 默认父目录 
	         if (folder == null) { 
	                System.out.println("服务器不可用"); 
	                return; 	               
	         } 

	         System.out.println("默认信箱名:" + folder.getName()); 
	         Folder[] folders = folder.list();// 默认目录列表 
	         System.out.println("默认目录下的子目录数: " + folders.length); 

	         Folder popFolder = folder.getFolder("INBOX");// 获取收件箱 
	            //popFolder.open(Folder.READ_WRITE);// 可读邮件,可以删邮件的模式打开目录 
	         popFolder.open(Folder.READ_ONLY);
	            // 4. 列出来收件箱 下所有邮件 
	            Message[] messages = popFolder.getMessages(); 
	            // 取出来邮件数 
	            int msgCount = popFolder.getMessageCount(); 
	            System.out.println("共有邮件: " + msgCount + "封"); 

	            // 7. 关闭 Folder 会真正删除邮件, false 不删除 
	            popFolder.close(true); 
	            // 8. 关闭 store, 断开网络连接 
	            store.close(); 

			//Folder folder = store.getFolder("inbox");
			//folder.open(Folder.READ_WRITE);
			
			//store.close();
			
			
		}catch(Exception ex){
			ex.printStackTrace();
		}*/
		
		 sun.misc.BASE64Decoder   decoder   =   new   sun.misc.BASE64Decoder();
		 sun.misc.BASE64Encoder encoder = new  sun.misc.BASE64Encoder();
		 String a = "Fw: 现代物流报综合物流版--电子报";
		 byte[] b = a.getBytes();
		 String c = encoder.encode(b);
		 System.out.println(c);
		
	}

}
