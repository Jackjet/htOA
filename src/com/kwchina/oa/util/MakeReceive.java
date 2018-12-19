package com.kwchina.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.xml.namespace.QName;


import org.apache.axis.attachments.AttachmentPart;
import org.apache.axis.client.Call;
import org.apache.axis.client.Service;

import com.kwchina.core.sys.CoreConstant;
import com.sun.xml.internal.ws.util.ByteArrayDataSource;

public class MakeReceive {
	public void doMakeReceive(Map<String, Object[]> formDataMap,String attachPaths,String ip)throws Exception{
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
	    
	    Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL("http://"+ip+"/MakeReceiveService.jws?wsdl"));
		call.setOperationName("doMakeReceive");
		
		//return res;
		
		
		// 接口调用
//		call.setOperationName(new QName("TestService", "sendMessage"));


		// 此处省略其他请求参数

		// 由于我获得的是内容的byte[],下面模拟下
		// DataHandler dh = new DataHandler(new FileDataSource(fileName));
		
		//附件
		String attachs[] = attachPaths.split("\\|");
		DataHandler[] dhs = new DataHandler[]{};
		String[] attachNames = new String[]{};
		
		if(attachPaths != null && !attachPaths.equals("")){
			dhs = new DataHandler[attachs.length];
			attachNames = new String[attachs.length];
//			DataHandler[] dhs = new DataHandler[attachs.length];
//			String[] attachNames = new String[attachs.length];
			for(int i=0;i<attachs.length;i++){
				String attach = attachs[i];
				
				// 获得的是byte[]
				File file = new File(CoreConstant.Context_Real_Path + attach);
				/*byte[] bs = null;
				if (file != null) {
					FileInputStream fis = new FileInputStream(file);
					if (fis != null) {
						int len = fis.available();
						bs = new byte[len];
						fis.read(bs);
					}
				}*/
				
				// 由byte[]转换为DataHandler
//				DataHandler dh = new DataHandler(new ByteArrayDataSource(bs, "application/octet-stream"));
				DataHandler dh = new DataHandler(new FileDataSource(file));
				dhs[i] = dh;
				attachNames[i] = file.getName();
//				AttachmentPart ap= new AttachmentPart(dh);
//				ap.setContentId("content");
//				call.addAttachmentPart(ap);
			}
			//CoreConstant.Context_Real_Path
			
			
			
		}
		call.invoke(new Object[] {formDataMap,dhs,attachNames});

	}
	
	public static void main(String[] args) {
		try{
			/*instanceId	874
			oldInstanceId	0
			flowId	84
			instanceTitle	收文测试一一一
			receiveDate	2014-10-22
			documentNo	HT-SW-2014-001
			reportYear	2014
			serialNo	001
			receiverId	8
			reportNo	沪港务工程发（2014）0194号
			fileDate	2014-10-22
			fileNum	
			secretName	
			urgencyName	
			unitName	
			selUnitName	安全监督部
			managerId	33
			viceManagerId	0
			attachment	*/
			
			/*Map<String, Object[]> formDataMap = new HashMap<String, Object[]>();
			MakeReceiveService makeReceive = new MakeReceiveService();
			makeReceive.doMakeReceive(formDataMap);*/
			
			String test = "abc";
			String ts[] = test.split("\\|");
			for(int i=0;i<ts.length;i++){
				System.out.println(ts[i]);
			}
			System.out.println(ts.length);
			
//			MakeLocalReceive test = new MakeLocalReceive();
//			test.doMakeReceive(formDataMap);
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
