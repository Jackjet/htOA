<%@ page import="com.kwchina.core.base.entity.SystemUserInfor" %>
<%@ page import="com.kwchina.oa.util.SysCommonMethod" %>
<%@ page contentType="text/html; charset=utf-8"%>

<%
	  java.io.BufferedInputStream bis=null;
	  java.io.BufferedOutputStream  bos=null;
	  
	try{
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		if (systemUser != null) {
			String getpath = request.getParameter("filepath");
			//String getpath = java.net.URLEncoder.encode(request.getParameter("filepath"), "gbk");
			//String getpath = new String(request.getParameter("filepath").getBytes("ISO-8859-1"), "utf-8");
			String filepath = "/";
			if (getpath != null && !("").equals(getpath)) {
				filepath = filepath + getpath;
			} else {
				filepath = filepath + request.getSession().getAttribute("_File_Path");
			}
			//filename=new String(filename.getBytes("iso8859-1"),"gb2312");

			//response.setHeader("Content-disposition","attachment; filename="+new String(filename.getBytes("gb2312"),"iso8859-1"));

			//out.println(filepath);
			if (filepath.startsWith("/upload") || filepath.startsWith("//upload")) {
				int pos = filepath.lastIndexOf("/");
				String fileName = filepath.substring(pos + 1);
				//fileName = new String(fileName.getBytes("iso8859-1"),"gbk");
				filepath = filepath.substring(0, pos);


				//out.println(fileName);
				//out.println(filepath);
				//out.println(filepath+"/"+fileName);
				//filepath = filepath.substring(0,pos-1);
				//out.println(config.getServletContext().getRealPath(filepath+"/"+fileName));

				response.setContentType("application/x-msdownload");
				response.setHeader("Content-disposition", "attachment; filename=" + new String(fileName.getBytes("gbk"), "iso8859-1"));
				response.addHeader("Pragma", "public");
				//response.addHeader("Cache-Control", "max-age=0");

				response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
				response.setHeader("Pragma", "public");

				bis = new java.io.BufferedInputStream(new java.io.FileInputStream(config.getServletContext().getRealPath(filepath + "/" + fileName)));
				bos = new java.io.BufferedOutputStream(response.getOutputStream());
				byte[] buff = new byte[8192];
				int bytesRead;
				while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
					bos.write(buff, 0, bytesRead);
				}
			}
		}
		
	}catch(Exception e){
	 	e.printStackTrace();
	}finally {
		 if (bis != null)bis.close();
		 if (bos != null)bos.close();
		 out.clear();
		 out = pageContext.pushBody();
	}
%> 
