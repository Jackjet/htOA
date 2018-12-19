package com.kwchina.oa.util;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.util.ArrayList;
import java.util.List;

public class CommonLinuxMethod {
	/**
	 * 运行shell脚本
	 * 
	 * @param shell
	 *            需要运行的shell脚本
	 */
	public static void execShell(String shell) {
		try {
			Runtime rt = Runtime.getRuntime();
			rt.exec(shell);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 运行shell
	 * 
	 * @param shStr
	 *            需要执行的shell
	 * @return
	 * @throws IOException
	 */
	public static String runShell(String shStr[]) throws Exception {
		String returnStr = "";

		Process process;
		/**
		String shellComm[]={"/bin/sh","/opt/oa/check.sh"};
		if(params!=null) {
			for(int k=0;k<params.length;k++){
				shellComm[k+2] = params[k];
			}
		}*/
		
		process = Runtime.getRuntime().exec(shStr);
		InputStreamReader ir = new InputStreamReader(process.getInputStream());
		LineNumberReader input = new LineNumberReader(ir);
		String line;
		process.waitFor();
		while ((line = input.readLine()) != null) {
			returnStr += line;
		}

		return returnStr;
	}
}
