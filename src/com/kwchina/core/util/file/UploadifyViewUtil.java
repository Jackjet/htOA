package com.kwchina.core.util.file;

import com.kwchina.core.sys.CoreConstant;

public class UploadifyViewUtil {
	/**
	 * 由uploadify上传的附件的查看方法
	 * @param attachmentStr
	 * attachment[0]--路径
	 * attachment[1]--附件名称
	 * attachment[2]--大小
	 * @return
	 */
	public static String[][] viewAttachment(String attachmentStr)throws Exception{
		String[] attachmentPaths = attachmentStr.split(";");
		String[][] attachment = new String[3][];
		attachment[0] = new String[attachmentPaths.length];
		attachment[1] = new String[attachmentPaths.length];
		attachment[2] = new String[attachmentPaths.length];
		for(int i=0;i<attachmentPaths.length;i++){
			String tmpAttachPath = attachmentPaths[i];
			if(tmpAttachPath != null && !tmpAttachPath.equals("")){
				int first = tmpAttachPath.indexOf("|");
				int second = tmpAttachPath.lastIndexOf("|");
				String uPath = tmpAttachPath.substring(0,first);
				String fileName = tmpAttachPath.substring(first+1,second);
				String size = tmpAttachPath.substring(second+1);
				String t = uPath + "/" + fileName;
				attachment[0][i] = t;//java.net.URLEncoder.encode(t,CoreConstant.ENCODING);
				attachment[1][i] = fileName;
				attachment[2][i] = size;
			}
		}
		return attachment;
	}
}
