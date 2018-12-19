package com.kwchina.core.util.multisearch;

import java.io.UnsupportedEncodingException;

import com.kwchina.core.sys.CoreConstant;


public class ConditionUtils {
	/**
	 * 根据参数获取查询HQL的语句（如果是日期字段可能会出问题）
	 * @param sField 字段名称
	 * @param sOper  操作名称
	 * @param sValue 值
	 * @return
	 */
	public static String getCondition(String sField, String sOper, String sValue){
		if(sValue == null || sValue.trim().length() == 0) {
			return "";
		}else {
			try {
				sValue = new String(sValue.getBytes("utf-8"), CoreConstant.ENCODING);
			} catch (UnsupportedEncodingException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String result = "";
		if(sOper.trim().equals("eq")) {
			//等于
			result = sField + " = '" + sValue +"' ";
		}else if(sOper.trim().equals("ne")) {
			//不等于
			result = sField + " != '"+ sValue+"' ";
		}else if(sOper.trim().equals("lt"))	{
			//小于
			result = sField + " < '"+ sValue+"' ";
		}else if(sOper.trim().equals("le")) {
			//小于等于
			result = sField + " <= '"+ sValue+"' ";
		}else if(sOper.trim().equals("gt")) {
			//大于
			result = sField + " > '"+ sValue+"' ";
		}else if(sOper.trim().equals("ge")) {
			//大于等于
			result = sField + " >= '"+ sValue+"' ";
		}else if(sOper.trim().equals("bw")) {
			//开始于
			result = sField + " like '"+ sValue+"%' ";
		}else if(sOper.trim().equals("bn")) {
			//不开始于
			result = sField + " not like '"+ sValue+"%' ";
		}else if(sOper.trim().equals("in")) {
			//属于
			String[] sv = sValue.split(",");
			String svString = "";
			for(int i=0;i<sv.length;i++){
				if (sv[i].length() > 0) {
					svString += "'" + sv[i] + "'";
					if (i != sv.length-1) {
						svString += ",";
					}
				}
			}
			result = sField + " in (" + svString + ") ";
		}else if(sOper.trim().equals("ni")) {
			//不属于
			String[] sv = sValue.split(",");
			String svString = "";
			for(int i=0;i<sv.length;i++){
				if (sv[i].length() > 0) {
					svString += "'" + sv[i] + "'";
					if (i != sv.length-1) {
						svString += ",";
					}
				}
			}
			result = sField + " not in (" + svString + ") ";
		}else if(sOper.trim().equals("ew")) {
			//结束于
			result = sField + " like '%"+ sValue+"' ";
		}else if(sOper.trim().equals("en")) {
			//不结束于
			result = sField + " not like '%"+ sValue+"' ";
		}else if(sOper.trim().equals("cn")) {
			//包含
			result = sField + " like '%"+ sValue+"%' ";
		}else if(sOper.trim().equals("nc")) {
			//不包含
			result = sField + " not like '%"+ sValue+"%' ";
		}
		return result;
	}
}