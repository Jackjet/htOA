package com.kwchina.core.config.controller;

import java.io.FileInputStream;
import java.io.FileOutputStream;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {         
//		// TODO Auto-generated// method stub //思路
//		// 先把图片读入到内存 // 再写入到某个文件
//		// //因为是二进制文件，因此只能用字节流完成
//		 FileInputStream fis = null; //输出流
//		 FileOutputStream fos = null; 
//		 try { 
//			 fis = new FileInputStream("d:\\a.jpg");
//			 fos = new FileOutputStream("e:\\a.jpg");
//			 byte buf[] = new byte[1024]; 
//			 int n=0;//记录实际读取到的字节数
//			 //循环读取
//			 while((n=fis.read(buf))!=-1){
//				 fos.write(buf); 
//			 } 
//		 }catch (Exception e) {
//			 // TODO Auto-generated catch block
//			 e.printStackTrace();
//		 }finally{ 
//			 try {
//				 fis.close();
//				 fos.close(); 
//			 } catch(Exception e) { 
//			     //TODO Auto-generated catch block
//				 e.printStackTrace();
//			 } 
//		 } 
		
		String i = "3";
		
		boolean flag = Boolean.valueOf(i);
		System.out.println(flag);
	   }
	}
