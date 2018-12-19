package com.kwchina.core.util;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.DecimalFormat;

import com.kwchina.core.sys.CoreConstant;

public class File {

	/**
	 * 获取文件后缀
	 */
	public static String getFileSurfix(String fileName) {
		if (fileName != null) {
			int dot = fileName.lastIndexOf(".");
			String surfix = fileName.substring(dot);
			return surfix;
		} else {
			return "";
		}
	}

	// 获取文件名
	public static String getFileName(String filePath) {
		int pos = filePath.lastIndexOf("/");
		String fileName = filePath.substring(pos + 1);

		return fileName;
	}

	// 创建文件(夹)
	public static void createFilePath(java.io.File ioFile) {
		if (ioFile != null) {
			java.io.File parentFile = ioFile.getParentFile();
			if (parentFile != null && !parentFile.exists()) {
				createFilePath(parentFile);
				parentFile.mkdir();
			}
			ioFile.mkdir();
		}
	}

	// 拷贝文件
	public static void copy(String file1, String file2) {
		try {

			java.io.File file_in = new java.io.File(file1);
			java.io.File file_out = new java.io.File(file2);
			if (file_in != null) {
				FileInputStream in1 = new FileInputStream(file_in);
				FileOutputStream out1 = new FileOutputStream(file_out);

				byte[] bytes = new byte[1024];
				int c;
				while ((c = in1.read(bytes)) != -1)
					out1.write(bytes, 0, c);
				in1.close();
				out1.close();
			}
		}

		catch (Exception e) {
			System.out.println("Error!" + e.toString());
		}
	}

	// 根据附件信息,显示在页面
	// 有相应的taglib
	public String displayAttachment(String attachment) {
		String displayStr = "";

		if (attachment != null && !attachment.equals("")) {
			attachment = attachment.trim();

			String[] filePaths = attachment.split("\\|");
			for (int k = 0; k < filePaths.length; k++) {
				String tempFile = filePaths[k];
				displayStr += "<a href=\"" + CoreConstant.Context_Name + "/common/download.jsp?filepath=";
				displayStr += tempFile;
				displayStr += " \"><font color=\"red\">";

				String fileName = "";
				int pos = tempFile.lastIndexOf("/");
				if (pos > 0) {
					fileName = tempFile.substring(pos + 1);
				}
				displayStr += fileName;
				displayStr += "</font></a><br>";
			}
		}

		return displayStr;
	}
	
	/**
	 * 获取文件大小
	 * @param fileS:通过File的length()方法数值传入
	 */
	public String getFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");   
        String fileSizeString = "";   
        if (fileS < 1024) {   
            fileSizeString = df.format((double) fileS) + "B";   
        } else if (fileS < 1048576) {   
            fileSizeString = df.format((double) fileS / 1024) + "K";   
        } else if (fileS < 1073741824) {   
            fileSizeString = df.format((double) fileS / 1048576) + "M";   
        } else {   
            fileSizeString = df.format((double) fileS / 1073741824) + "G";   
        }   
        return fileSizeString;   
    }
	
	/** 获取文件夹大小 
	 * @param file 文件
	 * */
	public long getFloderSize(java.io.File file) {
		long floderSize = 0;
		
		if(file.isDirectory()) {
			java.io.File[] childs = file.listFiles();
			for(int i=0; i<childs.length; i++) {
				if (childs[i].isDirectory()) {
					floderSize += getFloderSize(childs[i]);
				}else {
					floderSize += childs[i].length();
				}
			}
		}else {
			floderSize += file.length();
		}
		return floderSize;
	}

}
