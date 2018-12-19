package com.kwchina.core.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Timestamp;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTPClient;

//int port 为FTP的端口号   默认为21  

public class FtpUtils {

	/**
	 * ftp上传单个文件
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            上传至ftp的路径名不包括ftp地址
	 * @param srcFileName
	 *            要上传的文件全路径名
	 * @param destName
	 *            上传至ftp后存储的文件名
	 * @throws IOException
	 */
	public static boolean upload(String logSeq, String ftpUrl, String userName,
			int port, String password, String directory, String srcFileName,
			String destName) throws IOException {
		FTPClient ftpClient = new FTPClient();
		FileInputStream fis = null;
		boolean result = false;
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			File srcFile = new File(srcFileName);
			fis = new FileInputStream(srcFile);
			// 设置上传目录
			
			//1.首先进入ftp的根目录
			ftpClient.changeWorkingDirectory("/");
			String[] dirs = directory.split("/");
			for (String dir : dirs) {
				//2.创建并进入不存在的目录
				if (!ftpClient.changeWorkingDirectory(dir)) {
					ftpClient.mkd(dir);
					ftpClient.changeWorkingDirectory(dir);
					//System.out.println("进入目录成功:"+dir);
				}
			}
			
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.setBufferSize(1024);
			ftpClient.setControlEncoding("gbk");
			// 设置文件类型（二进制）
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);
			result = ftpClient.storeFile(new String(destName.getBytes("gbk"),"iso-8859-1"), fis);
			return result;
		} catch (NumberFormatException e) {
			System.out.println("FTP端口配置错误:不是数字:");
			throw e;
		} catch (FileNotFoundException e) {
			System.out.println("找不到源文件！");
			throw new FileNotFoundException();
		} catch (IOException e) {
			System.out.println("读取错误！");
			throw new IOException(e);
		} finally {
			IOUtils.closeQuietly(fis);
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * FTP单个文件下载
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要下载的文件所在ftp的路径名不包含ftp地址
	 * @param destFileName
	 *            要下载的文件名
	 * @param downloadName
	 *            下载后锁存储的文件名全路径
	 */
	public static boolean download(String logSeq, String ftpUrl,
			String userName, int port, String password, String directory,
			String destFileName, String downloadName) throws IOException {
		FTPClient ftpClient = new FTPClient();
		boolean result = false;
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setBufferSize(1024);
			// 设置文件类型（二进制）
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.setFileType(FTPClient.BINARY_FILE_TYPE);

			System.out.println("destFileName:" + destFileName
					+ ",downloadName:" + downloadName);
			result = ftpClient.retrieveFile(destFileName, new FileOutputStream(
					downloadName));
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (FileNotFoundException e) {
			throw new FileNotFoundException();
		} catch (IOException e) {
			throw new IOException(e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要重命名的文件所在ftp的路径名不包含ftp地址
	 * @param oldFileName
	 *            要重命名的文件名
	 * @param newFileName
	 *            重命名后的文件名
	 * @throws IOException
	 */
	public static boolean rename(String logSeq, String ftpUrl, String userName,
			int port, String password, String directory, String oldFileName,
			String newFileName) throws IOException {
		/**
		 * 判断远程文件是否重命名成功，如果成功返回true，否则返回false
		 */
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(directory);
			result = ftpClient.rename(oldFileName, newFileName);// 重命名远程文件
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要删除的文件所在ftp的路径名不包含ftp地址
	 * @param fileName
	 *            要删除的文件名
	 * @return
	 * @throws IOException
	 */
	public static boolean remove(String logSeq, String ftpUrl, String userName,
			int port, String password, String directory, String fileName)
			throws IOException {
		/**
		 * 判断远程文件是否移除成功，如果成功返回true，否则返回false
		 */
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(directory);
			result = ftpClient.deleteFile(fileName);// 删除远程文件
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要创建的目录所在ftp的路径名不包含ftp地址
	 * @param newDirectory
	 *            要创建的新目录名
	 * @return
	 * @throws IOException
	 */
	public static boolean makeDirecotory(String logSeq, String ftpUrl,
			String userName, int port, String password, String directory,
			String newDirectory) throws IOException {
		/**
		 * 判断远程文件是否移除成功，如果成功返回true，否则返回false
		 */
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(directory);
			result = ftpClient.makeDirectory(new String(newDirectory.getBytes("gbk"),"iso-8859-1"));// 创建新目录
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要重命名的目录所在ftp的路径名不包含ftp地址
	 * @param oldDirectory
	 *            要重命名的旧目录名
	 * @param newDirectory
	 *            重命名后的新目录
	 * @return
	 * @throws IOException
	 */
	public static boolean renameDirecotory(String logSeq, String ftpUrl,
			String userName, int port, String password, String directory,
			String oldDirectory, String newDirectory) throws IOException {
		/**
		 * 判断远程文件是否移除成功，如果成功返回true，否则返回false
		 */
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(directory);
			result = ftpClient.rename(oldDirectory, newDirectory);// 重命名目录
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 *            ftp地址
	 * @param userName
	 *            ftp的用户名
	 * @param password
	 *            ftp的密码
	 * @param directory
	 *            要重命名的目录所在ftp的路径名不包含ftp地址
	 * @param deldirectory
	 *            要删除的目录名
	 * @return
	 * @throws IOException
	 */
	public static boolean removeDirecotory(String logSeq, String ftpUrl,
			String userName, int port, String password, String directory,
			String deldirectory) throws IOException {
		/**
		 * 判断远程文件是否移除成功，如果成功返回true，否则返回false
		 */
		boolean result = false;
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.changeWorkingDirectory(directory);
			result = ftpClient.removeDirectory(deldirectory);// 删除目录
			return result;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}

	/**
	 * 
	 * @param ftpUrl
	 * @param userName
	 * @param password
	 * @param directory
	 * @return
	 * @throws IOException
	 */
	public static String[] list(String logSeq, String ftpUrl, String userName,
			int port, String password, String directory) throws IOException {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect(ftpUrl, port);
			ftpClient.login(userName, password);
			ftpClient.enterLocalPassiveMode();
			ftpClient.setControlEncoding("gbk");
			ftpClient.changeWorkingDirectory(directory);
			ftpClient.enterLocalPassiveMode();
			String[] list = ftpClient.listNames();// 删除目录
			return list;
		} catch (NumberFormatException e) {
			throw e;
		} catch (IOException e) {
			throw new IOException("连接ftp服务器失败！", e);
		} finally {
			try {
				ftpClient.disconnect();
			} catch (IOException e) {
				throw new RuntimeException("关闭FTP连接发生异常！", e);
			}
		}
	}
	
	public static void main(String[] args)throws Exception {
		System.out.println("before:----" + new Timestamp(System.currentTimeMillis()));
//		boolean a = upload("", "192.168.1.99", "archive", 21, "archive", "test", "D:\\apache-tomcat-6.0.20.rar", "apache-tomcat-6.0.20.rar");
		boolean a = upload("", "192.168.1.99", "archive", 21, "archive", "attach/oa/1498795634965", "D:\\网易企业邮箱集成接口技术白皮书（华东服务器）v1.0.HZ.doc", "a.doc");
		System.out.println("after:----" +a+ new Timestamp(System.currentTimeMillis()));
	}

}