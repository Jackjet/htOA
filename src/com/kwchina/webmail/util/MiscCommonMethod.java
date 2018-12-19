package com.kwchina.webmail.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public  class MiscCommonMethod {
	
	//格式化时间
	public static String formatDate(long date) {
		TimeZone tz = TimeZone.getDefault();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.DEFAULT);
		df.setTimeZone(tz);
		String now = df.format(new Date(date));
		return now;
	}

	//改变字符编码
	public static String stringEncoding(String encodingString,String oldCoding,String newCoding) {
		
		String newString = "";
		try{
			newString = new String(encodingString.getBytes(newCoding),oldCoding);
		}catch(UnsupportedEncodingException ex){
			newString = encodingString;
		}
		
		return newString;
	}
}
