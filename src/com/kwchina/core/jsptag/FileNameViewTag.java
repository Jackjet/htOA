package com.kwchina.core.jsptag;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.ServletContext;
import javax.servlet.jsp.JspWriter;
import javax.servlet.jsp.tagext.BodyTagSupport;

import com.kwchina.core.sys.CoreConstant;

public class FileNameViewTag extends BodyTagSupport {
	public FileNameViewTag() {
	}

	public int doStartTag() {
		return 2;
	}

	public int doEndTag() {
		return 6; 
	}
	
	public void setContextPath(String path)  {
        setValue("contextPath", path);
    }

	
	public int doAfterBody() {
		String printStr = "";
		String contextPath = (String)getValue("contextPath");
		
		String bcString = getBodyContent().getString();
		if (bcString!=null && !bcString.equals("")){
			bcString = bcString.trim();
			
			String[] filePaths = bcString.split("\\|");
			for(int k =0; k<filePaths.length;k++){
				if (k!=0) printStr += "<br>";
				
				String tempFile = filePaths[k];
				printStr += "<a href=\"" + contextPath + "/common/download.jsp?filepath=";
				String filepath = tempFile;
				try {
					filepath = java.net.URLEncoder.encode(tempFile,CoreConstant.ENCODING);
				} catch (UnsupportedEncodingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				printStr += filepath;
				printStr += " \">";
					
				String fileName = "";				
				int pos = tempFile.lastIndexOf("/");
				if (pos>0){
					fileName = tempFile.substring(pos+1);
				}
				printStr += fileName;
				printStr += "</a>";				
			}
			
			
			//ServletContext context = super.pageContext.getServletContext();
			//ServletRequest request = pageContext.getRequest();
			//String contextPath = pageContext();			
		}
		
		JspWriter out = getBodyContent().getEnclosingWriter();
		try {
			out.print(printStr);
			//out.flush();
		} catch (IOException e) {
			ServletContext sc = super.pageContext.getServletContext();
			sc.log("[EXCEPTION] while FilePathViewTag outputing content...", e);
		}
		return 0;
	}

}
