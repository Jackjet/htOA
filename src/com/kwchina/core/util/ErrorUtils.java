package com.kwchina.core.util;

import java.util.Iterator;
import java.util.List;

import org.springframework.validation.ObjectError;


public class ErrorUtils {
	
	public static String getErrorStr(List errors) {
		String errorMsg = "错误信息如下:<br>";
		
		for(Iterator it=errors.iterator();it.hasNext();){
			ObjectError error = (ObjectError) it.next();
			errorMsg += error.getDefaultMessage() + "<br>";
		}
		
		return errorMsg;
	}
}