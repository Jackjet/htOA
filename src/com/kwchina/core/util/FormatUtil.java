/**
 * 
 */
package com.kwchina.core.util;

import java.math.BigDecimal;
import java.text.NumberFormat;




/**
 * @author Administrator
 */
public class FormatUtil {

	//四舍五入的处理
	public static double round(double v,int scale){ 
        if(scale<0) { 
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        } 
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1"); 
        return b.divide(one,scale,BigDecimal.ROUND_HALF_UP).doubleValue(); 
    } 	
	
    public static float formatNumber(float number) {
        NumberFormat format = NumberFormat.getInstance();

        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(false);
        String str = format.format(number);
        float num = Float.parseFloat(str);
        return num;
    }
    
    public static String formatNumberStr(float number) {
        NumberFormat format = NumberFormat.getInstance();
        format.setMaximumFractionDigits(2);
        format.setGroupingUsed(false);
        return format.format(number);
    }

    public static float formatNumber(float number, int n) {
        NumberFormat format = NumberFormat.getInstance();

        format.setMaximumFractionDigits(n);
        format.setGroupingUsed(false);
        String str = format.format(number);
        float num = Float.parseFloat(str);
        return num;
    }

  


    public static String ISOtoGB(String convertStr) {
        String gb;
        try {
            if (convertStr.equals("") || convertStr == null) {
                return "";
            } else {
                convertStr = convertStr.trim();
                gb = new String(convertStr.getBytes("ISO-8859-1"), "GB2312");
                return gb;
            }
        } catch (Exception e) {
            System.err.print("编码转换错误：" + e.getMessage());
            return "";
        }
    }
    
    // 判断是否为数字
    public static boolean isNumber(String str){
    	int point = 0;
    	if(str == null ||str.length() == 0){
    		return false;
    	}
    	for(int i = 0 ; i<str.length();i++){
    		if(!Character.isDigit(str.charAt(i))){
    			if(point == 0 && str.charAt(i) == '.'){
    				point ++ ;
    				continue;
    			}
    			return false;
    		}
    	}
    	return true;
    }
}

