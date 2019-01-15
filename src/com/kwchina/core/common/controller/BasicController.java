package com.kwchina.core.common.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.sys.CoreConstant;

public class BasicController {

	//普通附件上传
	public String saveAttchment(List attachFiles,String folder,boolean onlySaveName)throws Exception{
		String attachment = "";
		
		String savePath =  folder;
		
		//类似于 D:\tomcat55\webapps\ROOT/upload\message;
		File file = new File(CoreConstant.Context_Real_Path + savePath);
		if(!file.exists()){
			file.mkdir();
		}
		
		//在folder下面建立目录，以当前时间为目录
		long current = System.currentTimeMillis();
		savePath += "/" + current;
		file = new File(CoreConstant.Context_Real_Path + savePath);
		if(!file.exists()){
			file.mkdir();
		}
		
		FileInputStream fileInput = null;
		FileOutputStream fileOutput = null;
		try {
			if(attachFiles.size()>0){
				for(int i=0;i<attachFiles.size();i++){
					String filePath = CoreConstant.Context_Real_Path + savePath;
					file = new File(filePath);
					if (!file.exists()) {
						file.mkdirs();
					}

					File tempFile = new File(String.valueOf(attachFiles.get(i)));
					String fileName = tempFile.getName();
					//savePath += 
					filePath += "/"+fileName;
					
					fileInput = new FileInputStream(String.valueOf(attachFiles.get(i))); 
					fileOutput = new FileOutputStream(filePath);
					
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = fileInput.read(buffer, 0, 8192)) != -1) {
						fileOutput.write(buffer, 0, bytesRead);
					}
					
					
					//如果是只保存文件名的情况，
					if(onlySaveName){
						attachment = attachment + fileName;
					}else{
						if(i==attachFiles.size()-1){
							attachment = attachment + folder + current + "/" + fileName;
						}else{
							attachment = attachment + folder + current + "/" + fileName + "|";
						}
					}
					
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			try {
				fileOutput.flush();
				fileOutput.close();
				fileInput.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
			
		}
		
		
		return attachment;
	}
	
	//附件上传
	public String uploadAttachment(DefaultMultipartHttpServletRequest multipartRequest,String folder) {
		String attachment = "";

		try{
			if (multipartRequest != null) {
				Iterator iterator = multipartRequest.getFileNames();
				
				while (iterator.hasNext()) {
					MultipartFile multifile = multipartRequest.getFile((String) iterator.next());
	
					if (multifile != null && multifile.getSize() != 0) {						
						String savePath = CoreConstant.Attachment_Path + folder;
						
						//类似于 D:\tomcat55\webapps\ROOT/upload\message;
						File file = new File(CoreConstant.Context_Real_Path + savePath);
						if(!file.exists()){
							file.mkdir();
						}
						
						//在folder下面建立目录，以当前时间为目录
						long current = System.currentTimeMillis();
						savePath += "/" + current;
						file = new File(CoreConstant.Context_Real_Path + savePath);
						if(!file.exists()){
							file.mkdir();
						}
						
						//获取文件名，并保存到该目录下
						String fileName = multifile.getOriginalFilename();
						savePath +=  "/" + fileName;
						String filePath = CoreConstant.Context_Real_Path + savePath;
						file = new File(filePath);
						if (!file.exists()) {
							file.mkdirs();
						}
						
						multifile.transferTo(file);			
						
						//保存到数据库中的信息(仅记录Contextpath之后的路径)
						attachment = attachment + savePath + "|";					
					} 
				}
			}
		}catch(IOException ex){
			System.out.println("--File Upload Error--:" + ex.toString());
		}
		
		if (!attachment.equals(""))
			attachment = attachment.substring(0, attachment.length()-1);

		return attachment;		
	}
	
	/**
	 * 删除原来的附件,且返回删除后的附件路径
	 * @param filePaths:原附件路径
	 * @param delboxName:页面用于勾选要删除附件的控件名称
	 * */
	public String deleteOldFile(HttpServletRequest request, String filePaths, String delboxName) {
		
		String oldFiles = "";
		
		if(filePaths != null && filePaths.length() > 0){
			String[] paths = filePaths.split("\\|");
			
			//要删除的原附件信息
			String[] strFiles = request.getParameterValues(delboxName);
			for (int i = 0; i < paths.length; i++) {
				boolean isDelete = false;

				if(strFiles != null && strFiles.length > 0){
					for (int j = 0; j < strFiles.length; j++) {
						if (strFiles[j].equals(String.valueOf(i))) {
							isDelete = true;
							
							//删除附件文件
							String filePath = CoreConstant.Context_Real_Path + paths[i];
							File file = new File(filePath);
							if(file.exists()) {
								file.delete();
								file.getParentFile().delete();
							}							
							break;
						}
					}
				}

				if (!isDelete) {
					if(oldFiles == null || oldFiles.length() == 0) {
						oldFiles += paths[i];
					}else{
						oldFiles += "|" + paths[i];
					}
				}
			}
		}
		
		return oldFiles;
	}
	
	/**
	 * 删除附件
	 * @param filePaths:附件路径
	 * */
	public void deleteFiles(String filePaths) {
		
		if(filePaths != null && filePaths.length() > 0){
			String[] paths = filePaths.split("\\|");
			for (int i = 0; i < paths.length; i++) {
				//删除附件文件
				String attachPath = CoreConstant.Context_Real_Path + paths[i];
				File attachFile = new File(attachPath);
				if(attachFile.exists()) {
					attachFile.delete();
					attachFile.getParentFile().delete();
				}							
			}
		}
		
	}
	
	/**
	 * 修改信息时,对附件进行处理
	 * @param attachmentFile:原附件路径
	 * @return returnAttach:returnAttach[0]-附件路径;returnAttach[1]-附件名称.
	 * */
	public String[][] processFile(String attachmentFile) {
		
		String[][] returnAttach = new String[2][];
		
		if (attachmentFile != null && attachmentFile.length() > 0) {
			returnAttach[0] = attachmentFile.split("\\|");

			returnAttach[1] = new String[returnAttach[0].length];
			for (int k = 0; k < returnAttach[0].length; k++) {
				returnAttach[1][k] = com.kwchina.core.util.File.getFileName(returnAttach[0][k]);
				try {
					returnAttach[0][k] = java.net.URLEncoder.encode(returnAttach[0][k],CoreConstant.ENCODING);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		
		return returnAttach;
	}
	
	//获取查询中的参数信息
	public String[] getSearchParams(HttpServletRequest request) {
		
		String[] params = new String[4];
		//查询排序的条件
		String sidx = request.getParameter("sidx");
		//查询排序的方式:asc,desc
		String sord = request.getParameter("sord");
		//表示是否是查询:true,false
		String _search = request.getParameter("_search");
		//从页面取得的多条件查询数据
		String filters = request.getParameter("filters");

		params[0] = sidx;
		params[1] = sord;
		params[2] = _search;
		params[3] = filters;

		return params;
	}
	/**对文件隔开****/

	public String[] cutOffattach(String attachName){
		if (attachName!=null&&attachName!=""){
			return attachName.split("\\|");
		}
		return null;
	}
	
}
