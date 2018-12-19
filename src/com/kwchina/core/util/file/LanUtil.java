package com.kwchina.core.util.file;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Timestamp;
import java.util.UUID;

import jcifs.smb.SmbFile;
import jcifs.smb.SmbFileInputStream;

/**
 * 局域网拷贝文件
 * @author suguan
 *
 */
public class LanUtil {
	public static void LANCopyFiles(String host,String username,String password,
			String remoteUrl,String localPath)throws Exception{
//		String host = "192.168.1.140";
//		String username = "gkadm1n";
//		String password = "gki23456";
//		String path = "/picture/";
//		String local_path = "d:/bak/picture/";
		
		String full_path = "smb://" + username + ":" + password + "@" + host + remoteUrl + (remoteUrl.endsWith("/") ? "" : "/");
//        System.out.println("\u6E90\u76EE\u5F55\uFF1A" + full_path);
//        System.out.println("\u76EE\u6807\u76EE\u5F55\uFF1A" + local_path);
        SmbFile dir = new SmbFile(full_path);
        SmbFile files[] = dir.listFiles(new SelectFile()); 
       
        for(int i = 0; i < files.length; i++) 
        {  
          
         
//         BufferedReader  in   =   new   BufferedReader(new   InputStreamReader(new SmbFileInputStream(files[i]))); 
            SmbFileInputStream in = new SmbFileInputStream(files[i]);
//         String str=in.readLine();
            String localFileName = files[i].getName();
            System.out.println(files[i].getName());
//            BufferedWriter fos =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(new File(local_path + (path.endsWith("/") ? "" : "/") + localFileName)),"gb2312"));
            FileOutputStream fos = new FileOutputStream(new File(localPath + (remoteUrl.endsWith("/") ? "" : "/") + localFileName));
            
            
            byte[] buffer = new byte[2048];
			while(in.read(buffer)!=-1){
				fos.write(buffer);
				buffer = new byte[1024];
			}
            
            
//           int count = 1;
//            while(str!=null)
//                { 
//             System.out.println(count+"行："+str);
//                fos.write(str);
//                fos.newLine();
//                fos.flush();
//                str= in.readLine();
//                count+=1;
//                }
           in.close();
           fos.flush();
           fos.close();
        }
	}
	
	public static void main(String[] args) throws Exception{
//		String host = "192.168.1.99";
//		String username = "kwchina";
//		String password = "kwchina";
//		String remoteUrl = "/soft/";
//		String localPath = "d:/new.gif";
//		System.out.println("before:----" + new Timestamp(System.currentTimeMillis()));
//		LANCopyFiles(host, username, password, remoteUrl, localPath);
//		System.out.println("after:----" + new Timestamp(System.currentTimeMillis()));
		
		System.out.println("before:----" + new Timestamp(System.currentTimeMillis()));
		String srcPath = "d:\\data.zip";
        File parentDir = new File("\\\\192.168.1.99\\soft");
        File targetPath = new File(parentDir, 
                UUID.randomUUID().toString().replaceAll("-", "") + ".zip");
        InputStream in = new FileInputStream(srcPath);
        OutputStream out = new FileOutputStream(targetPath);
        try {
            byte[] bs = new byte[1024];
            int len = -1;
            while((len = in.read(bs)) != -1) {
                out.write(bs, 0, len);
            }
        } finally {
            try {
                out.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                in.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("上传成功！！");
        System.out.println("after:----" + new Timestamp(System.currentTimeMillis()));
	}
}
