package com.kwchina.oa.util;

import java.io.File;
import java.net.URL;
import java.util.Map;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;

import org.apache.axis.client.Call;

import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.sys.CoreConstant;

public class SendInforDocument {
	public void sendInforDocument(Map<String, Object> dataMap,String attachPaths,String ip)throws Exception{
		org.apache.axis.client.Service service = new org.apache.axis.client.Service();
	    
	    Call call = (Call) service.createCall();
		call.setTargetEndpointAddress(new URL("http://"+ip+"/SendInforDocumentService.jws?wsdl"));
		call.setOperationName("sendInforDocument");
		
		//附件
		String attachs[] = attachPaths.split("\\|");
		DataHandler[] dhs = new DataHandler[]{};
		String[] attachNames = new String[]{};
		
		if(attachPaths != null && !attachPaths.equals("")){
			dhs = new DataHandler[attachs.length];
			attachNames = new String[attachs.length];
			for(int i=0;i<attachs.length;i++){
				String attach = attachs[i];
				
				// 获得的是byte[]
				File file = new File(CoreConstant.Context_Real_Path + attach);
				// 由byte[]转换为DataHandler
//				DataHandler dh = new DataHandler(new ByteArrayDataSource(bs, "application/octet-stream"));
				DataHandler dh = new DataHandler(new FileDataSource(file));
				dhs[i] = dh;
				attachNames[i] = file.getName();
			}
		}
		
		call.invoke(new Object[] {dataMap,dhs,attachNames});
		//return res;
	}
	
	public static void main(String[] args) {
		try{
			MakeReceiveService makeReceive = new MakeReceiveService();
//			makeReceive.doMakeReceive(formDataMap);
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
}
