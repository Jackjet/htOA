package com.kwchina.core.util.file;

import java.io.File;
import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class DeleteFileServlet extends HttpServlet {
	public void init() throws ServletException {
	}

	// Process the HTTP Get request
	public void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	// Process the HTTP Post request
	public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String savePath = this.getServletConfig().getServletContext().getRealPath("");
		String folder = request.getParameter("folder");
		savePath += folder;

		File file = new File(savePath);

		// 判断目录或文件是否存在
		if (file.exists()) {
			com.kwchina.core.util.file.File fileUtil = new com.kwchina.core.util.file.File();
			// 判断是否为文件
			if (file.isFile()) { // 为文件时调用删除文件方法
				fileUtil.deleteFile(savePath);
			} else { // 为目录时调用删除目录方法
				fileUtil.deleteDirectory(savePath);
			}
		}
	}

	// Clean up resources
	public void destroy() {
	}

}
