package test.linux;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;

//Test_CreateUser.java
public class Test_CreateUser {
	public static void main(String[] args)
	{
		try{
			//Runtime rt=Runtime.getRuntime();
			
			//写入用户名密码到/opt/oa/newuser
			//FileWriter fw = new FileWriter("/opt/oa/newuser"); 
			//PrintWriter out=new PrintWriter(fw); 
			//out.write(args[0]+":"+args[1]);
			
			//out.flush(); 
			//fw.close(); 
			//out.close();
			
			Runtime rt=Runtime.getRuntime();
			String str[]={"/bin/sh","/opt/oa/useradd.sh"};
			Process pcs=rt.exec(str);
			
			try{
				pcs.waitFor();
				int ret=pcs.exitValue();
			} catch(InterruptedException e){
				System.err.println("processes was interrupted");
			}		
			
		}catch(Exception ex){
			System.out.println(ex.toString());
		}
	}
}
