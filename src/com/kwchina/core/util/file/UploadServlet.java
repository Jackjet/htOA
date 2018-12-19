package com.kwchina.core.util.file;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public class UploadServlet extends HttpServlet {
	 	public void init() throws ServletException {
	    }
	    
	    //Process the HTTP Get request
	    public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	        doPost(request,response);
	    }
	    
	    //Process the HTTP Post request
	    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	    	String savePath = this.getServletConfig().getServletContext().getRealPath("");
	    	String relativePath = this.getInitParameter("relativePath");
	    	String uPath = UUID.randomUUID().toString();
	    	savePath = savePath + relativePath +"bbs/" + uPath + "/";
	    	
	    	File f1 = new File(savePath);
			System.out.println(savePath);
			if (!f1.exists()) {
				f1.mkdirs();
			}

			String name = "";
			String extName = "";
			String fileSize = "";
			
			DiskFileItemFactory fac = new DiskFileItemFactory();
			ServletFileUpload upload = new ServletFileUpload(fac);
			upload.setHeaderEncoding("utf-8");
			List fileList = null;
			try {
				fileList = upload.parseRequest(request);
				
				if(fileList!=null) {
					Iterator<FileItem> it = fileList.iterator();
					
					while (it.hasNext()) {
						FileItem item = it.next();
						if (!item.isFormField()) {
							name = item.getName();
							long size = item.getSize();
							fileSize = com.kwchina.core.util.file.File.getFileSize(size);
							String type = item.getContentType();
							//System.out.println(size + " " + type);
							
							if (name == null || name.trim().equals("")) {
								continue;
							}
	
							// 扩展名格式：
							if (name.lastIndexOf(".") >= 0) {
								extName = name.substring(name.lastIndexOf("."));
							}
							
							/**
							File file = null;
							do {
								// 生成文件名：
								//name = UUID.randomUUID().toString();
								//file = new File(savePath + name + extName);
								//file=new File(savePath+name);
							} while (file.exists());
							*/	
							
							try {
								//File saveFile = new File(savePath + name + extName);
								File saveFile = new File(savePath + name);
								item.write(saveFile);
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			} catch (FileUploadException ex) {
				ex.toString();
			}
			
			//返回文件信息，其格式为”文件夹|文件名|大小";
			String back = "";
			back= relativePath +"bbs/" + uPath + "|" + name + "|" + fileSize;
			response.getWriter().print(back);
	    }

	    
	    //Clean up resources
	    public void destroy() {
	    }

}
