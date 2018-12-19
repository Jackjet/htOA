package com.kwchina.core.util.json;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * 反射一个对象所有属性和属性值和将一个对象的反射结果封装成jsons格式 
 * @author Administrator
 * 
 */


public class ObjectListToJSON {

	/**
	 * 对象的全部属性和属性值。用于填写json的{}内数据 生成后的格式类似 "属性1":"属性值" 将这些属性名和属性值写入字符串列表返回
	 */
	private List<String> GetObjectProperty(Object object) {

		List<String> propertys = new ArrayList();

		Class clazz = object.getClass();// 获取集合中的对象类型
		Field[] fds = clazz.getDeclaredFields();// 获取他的字段数组
		
		

		// 遍历该数组
		for (Field field : fds) {

			try {
				// 得到字段名
				String fdName = field.getName();

				// 根据字段名找到对应的get方法，null表示无参数
				Method metd = clazz.getMethod("get" + changeChar(fdName), null);

				// 获得字段值
				if (metd != null) {
					Object value = metd.invoke(object, null);

					propertys.add("\"" + fdName + "\":\"" + value + "\"");
				}
			} catch (NoSuchMethodException ex) {

			} catch (IllegalArgumentException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}// 调用该字段的get方法

		}
		
		return propertys;
	}
	
	/**
	 * 将一个对象的所有属性和属性值按json的格式要求输入为一个封装后的结果。
	 * 
	 * 返回值类似{"属性1":"属性1值","属性2":"属性2值","属性3":"属性3值"}
	 * 
	 */
	 private String OneObjectToJSON(Object object) {
		 String result = "{";
		 List<String> ls_propertys = new ArrayList<String>();
		 
		 ls_propertys = GetObjectProperty(object);
		 for (String str_property : ls_propertys) {
			 if (result.equals("{")) {
				 result = result + str_property;
			 } else {
				 result = result + "," + str_property + "";
			 }
		 }
		 
		 return result + "}";
	 }
	 
	 /**
	  * 把对象列表转换成json串	 
	  * @param objlist
	  * @return
	  */
	 public String toJSON(List<Object> objlist) {
		 return toJSON(objlist, "");
	 }
		
	 public String toJSON(List<Object> objList, String className) {
		 String result = "{";
		 
		 if(objList.size()>0){
			 
			 if (className.equals("")) {
				 //如果没有给定类的名称，设定一个
				 Object obj = objList.get(0);
				 className = obj.getClass().getName();
			 }	 
		 
			 result += "\"" + className + "\":[";
			
			 boolean firstLine = true;	// 处理第一行前面不加","号
			 
			 for (Object tempObj : objList) {
				 if (!firstLine) {
					 result = result + "," + OneObjectToJSON(tempObj);
				 } else {
					 result = result + OneObjectToJSON(tempObj) + "";
					 firstLine = false;
				 }
			 }			 
		 }
		 
		 return result + "]}";
	 }
	 
	 
	/**
	 * @param src
	 *            源字符串
	 * @return 字符串，将src的第一个字母转换为大写，src为空时返回null
	 */
	public static String changeChar(String src) {
		if (src != null) {
			StringBuffer sb = new StringBuffer(src);
			sb.setCharAt(0, Character.toUpperCase(sb.charAt(0)));
			return sb.toString();
		} else {
			return null;
		}
	}
}
