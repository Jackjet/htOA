package com.kwchina.core.cms.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Date;
import java.text.SimpleDateFormat;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.cms.util.InforConstant;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;

@Controller
@RequestMapping("/cms/inforTemplate.do")
public class InforTemplateController {


	//树状显示模板信息
	@RequestMapping(params = "method=listTree")
	public void listTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String templateStyle = request.getParameter("templateStyle");
		String templatePath = InforConstant.Cms_Template_Path;
		if (("workflow").equals(templateStyle)) {
			templatePath = FlowConstant.Flow_FormTemplate_Path;
		}
		
		File file = new File(CoreConstant.Context_Real_Path + "/" + templatePath);
		JSONArray fileArray = new JSONArray();
		JSONObject jsonObj = new JSONObject();
		fileArray = getFilesAsTree(fileArray, file, 1);
		jsonObj.put("rows", fileArray);
		
		//设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	public JSONArray getFilesAsTree(JSONArray array, File file, int level) {
		
		File[] childs = file.listFiles();
		int fileLevel = level + 1;
		for(int i=0; i<childs.length; i++) {
			//仅显示除img,svn外的文件夹和ftl,js,css,html,txt文件
			if ((childs[i].isDirectory() && childs[i].getName().indexOf("img")<=-1 && !childs[i].getName().endsWith(".svn")) || childs[i].getName().endsWith(".ftl") 
					|| childs[i].getName().endsWith(".js") || childs[i].getName().endsWith(".css")
					|| childs[i].getName().endsWith(".html") || childs[i].getName().endsWith(".txt")) {
				JSONObject obj = new JSONObject();
	            obj.put("id",array.size()+1);
				obj.put("fileName", childs[i].getName());
	            obj.put("path", childs[i].getPath().replaceAll("\\\\", "/"));//将右斜杠替换为斜杠,避免页面上以path传参时右斜杠被过滤
	            obj.put("level", fileLevel);
	            if(childs[i].isDirectory()) {
	            	obj.put("leaf", false);
	            }else {
	            	obj.put("leaf", true);
	            }
				array.add(obj);
				
				//存在子文件时继续调用
				if(childs[i].isDirectory()) {
					getFilesAsTree(array, childs[i], fileLevel);
	            }
			}
        }
		return array;
	}
	
	//显示指定文件夹下的文件
	@RequestMapping(params = "method=listFile")
	public void listFile(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String path = request.getParameter("path");
		String templateStyle = request.getParameter("templateStyle");
		String templatePath = InforConstant.Cms_Template_Path;
		if (("workflow").equals(templateStyle)) {
			templatePath = FlowConstant.Flow_FormTemplate_Path;
		}
		
		//无参数path时,默认显示根目录下的文件
		if (path == null || path.length() == 0) {
			path = CoreConstant.Context_Real_Path + "/" + templatePath;
		}
		
		File file = new File(path);
		File[] childs = file.listFiles();
		JSONArray fileArray = new JSONArray();
		com.kwchina.core.util.File tmpFile = new com.kwchina.core.util.File();
		for(int i=0; i<childs.length; i++) {
			//仅显示除img,svn外的文件夹和ftl,js,css,html,txt文件
			if ((childs[i].isDirectory() && childs[i].getName().indexOf("img")<=-1 && !childs[i].getName().endsWith(".svn")) || childs[i].getName().endsWith(".ftl") 
					|| childs[i].getName().endsWith(".js") || childs[i].getName().endsWith(".css")
					|| childs[i].getName().endsWith(".html") || childs[i].getName().endsWith(".txt")) {
				JSONObject obj = new JSONObject();
				obj.put("id", i);
				obj.put("path", childs[i].getPath());
				obj.put("fileName", childs[i].getName());
				long floderSize = tmpFile.getFloderSize(childs[i]);
	            String fileSize = tmpFile.getFileSize(floderSize);
				obj.put("fileSize", fileSize);
				long lastM = childs[i].lastModified();
				SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	            obj.put("updateDate", sdf.format(new Date(lastM)));
	            fileArray.add(obj);
			}
        }
		
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("rows", fileArray);
		
		//设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}


	//编辑
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String path = request.getParameter("path");
		File file = new File(path);
		String fileContent = FileUtils.readFileToString(file, CoreConstant.ENCODING);
		request.setAttribute("_FileContent", fileContent);
		
		return "editInforTemplate";
	}

	//保存
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String path = request.getParameter("path");
		String fileContent = request.getParameter("fileContent");
		File file = new File(path);
		FileUtils.writeStringToFile(file, fileContent, CoreConstant.ENCODING);
		
		String templateStyle = request.getParameter("templateStyle");
		
		return "redirect:templateBaseList.jsp?templateStyle="+templateStyle;
		
	}
	
	
	//创建文件
	@RequestMapping(params = "method=addFile")
	public void addFile(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String path = request.getParameter("path");
		String templateStyle = request.getParameter("templateStyle");
		String templatePath = InforConstant.Cms_Template_Path;
		if (("workflow").equals(templateStyle)) {
			templatePath = FlowConstant.Flow_FormTemplate_Path + "/";
		}
		
		//无参数path时,默认根目录下的文件
		if (path == null || path.length() == 0) {
			path = CoreConstant.Context_Real_Path + templatePath;
		}else {
			path += "/";
		}
		
		//根据所选文件类型进行创建
		String fileType = request.getParameter("fileType");
		String fileName = request.getParameter("fileName");
		String filePath = path + fileName;
		if (("floder").equals(fileType)) {
			File file = new File(filePath);
			if(!file.exists()){
				file.mkdir();
			}
		}else {
			if (("ftl").equals(fileType)) {
				filePath += ".ftl";
			}else if (("js").equals(fileType)) {
				filePath += ".js";
			}else if (("css").equals(fileType)) {
				filePath += ".css";
			}else if (("html").equals(fileType)) {
				filePath += ".html";
			}else if (("txt").equals(fileType)) {
				filePath += ".txt";
			}
			
			FileOutputStream fos = new FileOutputStream(filePath);
			Writer out = new OutputStreamWriter(fos, CoreConstant.ENCODING);
			out.flush();
			out.close();
		}
		
	}
	
	
	//删除
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {
					//删除文件
					String filePath = new String(detleteIds[i].getBytes("ISO-8859-1"), "gbk");
					java.io.File file = new java.io.File(filePath);
					if(file.exists()) {
						deleteFile(file);
					}
				}
			}
		}
	}
	
	//删除文件及其子文件
	private void deleteFile(File file) {
		if (file.isDirectory()) {
			File[] childs = file.listFiles();
			for (int i=0;i<childs.length;i++) {
				if (childs[i].isDirectory()) {
					deleteFile(childs[i]);
				}else {
					childs[i].delete();
				}
			}
		}
		file.delete();
	}
	
}
