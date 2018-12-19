package test.linux;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

//Test_Exec.java
public class Test_Exec {
	public static void main(String[] args)
	{
		try{
			Runtime rt=Runtime.getRuntime();
			//String str[]={"/bin/sh","-c","/opt/oa/check.sh zhoulb"};
			String str[]={"/bin/sh","/opt/oa/check.sh",args[0]};
			Process pcs=rt.exec(str);
			BufferedReader br = new BufferedReader(new InputStreamReader(pcs.getInputStream()));
			String line=new String();
			
			System.out.println("===begin===");
			while ((line = br.readLine()) != null){
				//line = br.readLine();
				System.out.println("===line==="+line);
				
			}
			System.out.println("===exit===");
			
			try{
				pcs.waitFor();
			} catch(InterruptedException e){
				System.err.println("processes was interrupted");
			}
			
			br.close();
			int ret=pcs.exitValue();
			System.out.println(ret);
		}catch(IOException ex){
			System.out.println(ex.toString());
		}

	}
}
