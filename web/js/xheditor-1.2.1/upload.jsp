<%@ page contentType="text/html;charset=UTF-8" %>
<%@page import="org.apache.commons.lang.StringUtils"%>
<%@page import="java.io.*"%>
<%@page import="org.apache.commons.fileupload.DiskFileUpload"%>
<%@page import="java.util.List"%>
<%@page import="org.apache.commons.fileupload.FileItem"%>
<%@page import="java.util.Map"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.UUID"%>

<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	response.setHeader("Cache-Control","no-store");
	response.setHeader("Pragma","no-cache");
	response.setDateHeader("Expires",0);
	
	response.setContentType("text/html; charset=UTF-8");
%>
<%
		String baseFileDir = File.separator + "upload/xheditor" + File.separator;//上传文件存储目录
		String baseURLDir = "/upload/xheditor/";//上传文件目录URL
		String fileExt = "jpg,jpeg,bmp,gif,png";
		Long maxSize = 0l;
		
		// 0:不建目录 1:按天存入目录 2:按月存入目录 3:按扩展名存目录 建议使用按天存
		String dirType = "1";
	/*获取文件上传存储的相当路径*/
		if (!StringUtils.isBlank(this.getInitParameter("baseDir"))){
			baseFileDir = this.getInitParameter("baseDir");
		}
		System.out.println(this.getInitParameter("baseDir"));
		String realBaseDir = this.getServletConfig().getServletContext().getRealPath(baseFileDir);
		System.out.println(realBaseDir);
		File baseFile = new File(realBaseDir);
		if (!baseFile.exists()) {
			baseFile.mkdir();
		}

		/*获取	文件类型参数*/
		fileExt = this.getInitParameter("fileExt");
		System.out.println(fileExt);
		if (StringUtils.isBlank(fileExt)) fileExt = "jpg,jpeg,gif,bmp,png,swf,avi";

		/*获取文件大小参数*/
		String maxSize_str = this.getInitParameter("maxSize");
		System.out.println(maxSize_str);
		if (StringUtils.isNotBlank(maxSize_str)) maxSize = new Long(maxSize_str);
		
		/*获取文件目录类型参数*/
		dirType = this.getInitParameter("dirType");
		System.out.println(dirType);
		if (StringUtils.isBlank(dirType))
			dirType = "1";
		if (",0,1,2,3,".indexOf("," + dirType + ",") < 0)
			dirType = "1";
		
		
		
		String err = "";
		String newFileName = "";

		DiskFileUpload upload = new DiskFileUpload();
		try {
			List<FileItem> items = upload.parseRequest(request);
			Map<String, Serializable> fields = new HashMap<String, Serializable>();
			Iterator<FileItem> iter = items.iterator();
			
			while (iter.hasNext()) {
				FileItem item = (FileItem) iter.next();
				if (item.isFormField())
					fields.put(item.getFieldName(), item.getString());
				else
					fields.put(item.getFieldName(), item);
			}
			PrintWriter outWriter = response.getWriter();
			/*获取表单的上传文件*/
			FileItem uploadFile = (FileItem)fields.get("filedata");
			
			/*获取文件上传路径名称*/
			String fileNameLong = uploadFile.getName();
			System.out.println("fileNameLong:" + fileNameLong);
			
			/*获取文件扩展名*/
			/*索引加1的效果是只取xxx.jpg的jpg*/
			String extensionName = fileNameLong.substring(fileNameLong.lastIndexOf(".") + 1);
			System.out.println("extensionName:" + extensionName);
			
			/*检查文件类型*/
			if (("," + fileExt.toLowerCase() + ",").indexOf("," + extensionName.toLowerCase() + ",") < 0){
				out.println("{\"err\":\"不允许上传此类型的文件\",\"msg\":\"" + newFileName + "\"}");
				return;
			}
			/*文件是否为空*/
			if (uploadFile.getSize() == 0){
				out.println("{\"err\":\"上传文件不能为空\",\"msg\":\"" + newFileName + "\"}");
				return;
			}
			/*检查文件大小*/
			if (maxSize > 0 && uploadFile.getSize() > maxSize){
				out.println("{\"err\":\"上传文件的大小超出限制\",\"msg\":\"" + newFileName + "\"}");
				return;
			}
			
			//0:不建目录, 1:按天存入目录, 2:按月存入目录, 3:按扩展名存目录.建议使用按天存.
			String fileFolder = "";
			if (dirType.equalsIgnoreCase("1"))
				fileFolder = new SimpleDateFormat("yyyyMMdd").format(new Date());;
			if (dirType.equalsIgnoreCase("2"))
				fileFolder = new SimpleDateFormat("yyyyMM").format(new Date());
			if (dirType.equalsIgnoreCase("3"))
				fileFolder = extensionName.toLowerCase();
			
			/*文件存储的相对路径*/
			String saveDirPath = baseFileDir + fileFolder + "/";
			System.out.println("saveDirPath:" + saveDirPath);
			
			/*文件存储在容器中的绝对路径*/
			String pathExtension = "";
			if("swf".equals(extensionName)){
				pathExtension = "swf";
			}else if("avi".equals(extensionName)){
				pathExtension = "avi";
			}else{
				pathExtension = "image";
			}
			String saveFilePath = this.getServletConfig().getServletContext().getRealPath("") + baseFileDir + fileFolder + "/" + pathExtension + "/";
			System.out.println("----------saveFilePath:" + saveFilePath);
			
			/*构建文件目录以及目录文件*/
			File fileDir = new File(saveFilePath);
			if (!fileDir.exists()) {fileDir.mkdirs();}
			
			/*重命名文件*/
			String filename = UUID.randomUUID().toString();
			File savefile = new File(saveFilePath + filename + "." + extensionName);
			
			/*存储上传文件*/
			System.out.println(upload == null);
			uploadFile.write(savefile);
			
			String projectBasePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
			newFileName = projectBasePath + baseURLDir + fileFolder + "/" +pathExtension+ "/" + filename + "." + extensionName;		
			System.out.println("newFileName:" + newFileName);
			out.println("{\"err\":\"\",\"msg\":\"" + newFileName + "\"}");
		} catch (Exception ex) {
			System.out.println(ex.getMessage());
			newFileName = "";
			out.println("{\"err\":\"错误:"+ex.getMessage()+"\",\"msg\":\"" + newFileName + "\"}");
		}
%>

