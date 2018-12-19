package com.kwchina.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.sql.Date;
import java.util.HashMap;
import java.util.Map;

import javax.activation.DataHandler;

import org.apache.commons.beanutils.BeanUtils;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.cms.util.DocumentConverter;
import com.kwchina.core.cms.vo.InforDocumentVo;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.BeanToMapUtil;
import com.kwchina.oa.sys.MyApplicationContextUtil;

public class SendInforDocumentService {
	
	public void sendInforDocument(Map<String, Object> dataMap,DataHandler[] dhs,String[] attachNames){  
		try {
			System.out.println("进来了！");
			//从容器中得到注入的bean 

			SystemUserManager systemUserManager = (SystemUserManager)MyApplicationContextUtil.getBean("systemUserManager");
			InforDocumentManager inforDocumentManager = (InforDocumentManager)MyApplicationContextUtil.getBean("inforDocumentManager");
			InforCategoryManager inforCategoryManager = (InforCategoryManager)MyApplicationContextUtil.getBean("inforCategoryManager");
			
			
			InforDocument inforDocument = new InforDocument();
			
			String attachPaths = "";
			//处理附件
			for (int i = 0; i < dhs.length; ++i){
				System.out.println("++++++开始处理附件+++++"+dhs.length);
			    //file = new File(CoreConstant.Context_Real_Path + CoreConstant.Attachment_Path + "workflow-r" + savePath + "/" + recDH.getName());
				FileInputStream fileInput = null;
				FileOutputStream fileOutput = null;
				try {
					DataHandler recDH = dhs[i];
					
					long current = System.currentTimeMillis();
					String savePath = "/" + current;
					String filePath = CoreConstant.Context_Real_Path + CoreConstant.Attachment_Path;
					
					File file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					filePath += "cms/attach-r";
					file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					filePath += savePath + "/";
					file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					fileInput = new FileInputStream(String.valueOf(recDH.getName())); 
					filePath += attachNames[i];
					fileOutput = new FileOutputStream(filePath);
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = fileInput.read(buffer, 0, 8192)) != -1) {
						fileOutput.write(buffer, 0, bytesRead);
					}
					
					attachPaths += filePath.substring(filePath.indexOf(CoreConstant.Attachment_Path)) + "|";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					fileOutput.flush();
					fileOutput.close();
					fileInput.close();
				}
			}
			
			if (!attachPaths.equals("")){
				attachPaths = attachPaths.substring(0, attachPaths.length()-1);
			}
			/*for(Map.Entry<String, Object> entry : dataMap.entrySet()) {
				//System.out.print(entry.getKey() + ":" + entry.getValue() + "\t");
			}*/
			
			InforDocumentVo vo = (InforDocumentVo)BeanToMapUtil.convertMap(InforDocumentVo.class, dataMap);
			
			BeanUtils.copyProperties(inforDocument, vo);
			
			inforDocument.setInforId(null);
			//时间类
			if(vo.getStartDateStr() != null && !vo.getStartDateStr().equals("")){
				inforDocument.setStartDate(Date.valueOf(vo.getStartDateStr()));
			}
			if(vo.getEndDateStr() != null && !vo.getEndDateStr().equals("")){
				inforDocument.setEndDate(Date.valueOf(vo.getEndDateStr()));
			}
			if(vo.getCreateTimeStr() != null && !vo.getCreateTimeStr().equals("")){
				inforDocument.setCreateTime(Date.valueOf(vo.getCreateTimeStr()));
			}
			if(vo.getInforTimeStr() != null && !vo.getInforTimeStr().equals("")){
				inforDocument.setInforTime(Date.valueOf(vo.getInforTimeStr()));
			}
			if(vo.getDefDate1Str() != null && !vo.getDefDate1Str().equals("")){
				inforDocument.setDefDate1(Date.valueOf(vo.getDefDate1Str()));
			}
			if(vo.getDefDate2Str() != null && !vo.getDefDate2Str().equals("")){
				inforDocument.setDefDate2(Date.valueOf(vo.getDefDate2Str()));
			}
			
			inforDocument.setDefDec1(BigDecimal.valueOf(vo.getDefDec1D()));
			
			//分类
			InforCategory category = (InforCategory)inforCategoryManager.get(vo.getCategoryId());
			inforDocument.setCategory(category);
			
			//默认的作者
			SystemUserInfor author = (SystemUserInfor)systemUserManager.get(CoreConstant.User_Infor_Author);
			inforDocument.setAuthor(author);
			
			//是否接收到的
			inforDocument.setReceived(true);
			
			//将“互通”设为false
			inforDocument.setHandOut(false);
			
			//附件
			inforDocument.setAttachment(attachPaths);
			
			//自动生成html静态页面并返回html文件保存路径
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("_InforDocument", inforDocument);
			
			//附件信息
			String attachmentFile = attachPaths;
			if (attachmentFile != null && attachmentFile.length() > 0) {
				String[] arrayFiles = attachmentFile.split("\\|");
				//字符串转换为utf-8编码
				String[] newArrayFiles = new String[arrayFiles.length];
				for (int i=0;i<arrayFiles.length;i++) {
					newArrayFiles[i] = java.net.URLEncoder.encode(arrayFiles[i],CoreConstant.ENCODING);
				}
				map.put("_ArrayFiles", newArrayFiles);

				String[] attachmentNames = new String[arrayFiles.length];
				for (int k = 0; k < arrayFiles.length; k++) {
					attachmentNames[k] = com.kwchina.core.util.File.getFileName(arrayFiles[k]);
				}
				map.put("_AttachmentNames", attachmentNames);
			}
			
			/*//图片附件
			String defaultPicUrl = inforDocument.getDefaultPicUrl();
			if (defaultPicUrl != null && defaultPicUrl.length() > 0) {
				String[] arrayPics = defaultPicUrl.split("\\|");
				//字符串转换为utf-8编码
				String[] newArrayPics = new String[arrayPics.length];
				for (int i=0;i<arrayPics.length;i++) {
					newArrayPics[i] = java.net.URLEncoder.encode(arrayPics[i],CoreConstant.ENCODING);
				}
				map.put("_ArrayPics", newArrayPics);

				String[] picNames = new String[arrayPics.length];
				for (int k = 0; k < arrayPics.length; k++) {
					picNames[k] = com.kwchina.core.util.File.getFileName(arrayPics[k]);
				}
				map.put("_PicNames", picNames);
			}*/

			//this.inforDocumentManager.save(inforDocument);
		
//			map.put("base", CoreConstant.Context_Real_Path);
			map.put("base", "");
			
			
			String templateName = inforDocument.getCategory().getContentTemplate();
			String pagePath = inforDocument.getCategory().getPagePath();
			if (templateName != null && templateName.length() > 0 && pagePath != null && pagePath.length() > 0) {
				templateName = templateName.split("/")[templateName.split("/").length-1];
				String htmlPath = DocumentConverter.createHtml(templateName, pagePath, map);
				inforDocument.setHtmlFilePath(htmlPath);
				//String ss = DocumentConverter.createHtml(templateName, pagePath, map);
			}
			
			inforDocumentManager.save(inforDocument);
			
			System.out.println("=========信息发布类互通成功！========");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("=========信息发布类互通失败！========");
			e.printStackTrace();
		} 
		
		
		//return "我说呢";
	} 

}
