package com.kwchina.webmail.util;

import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.util.Date;
import java.util.TimeZone;

public  class MiscCommonMethod {
	
	//��ʽ��ʱ��
	public static String formatDate(long date) {
		TimeZone tz = TimeZone.getDefault();
		DateFormat df = DateFormat.getDateTimeInstance(DateFormat.LONG,
				DateFormat.DEFAULT);
		df.setTimeZone(tz);
		String now = df.format(new Date(date));
		return now;
	}

	//�ı��ַ�����
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
