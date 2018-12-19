package com.kwchina.core.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

public class ArrayUtil {
	/**  
	*@描  述：去掉重复元素方法  
	*@return List  
	*/  
	    @SuppressWarnings("unchecked")   
	    public static List removeDuplicateWithOrder(List list) {   
	       //定义Set :元素不重复   
	        Set set = new HashSet();   
	        //定义List 存放不重复的元素   
	        List newList = new ArrayList();   
	            //得到list的迭代器   
	        for (Iterator iter = list.iterator(); iter.hasNext();) {   
	            //获得每一个元素   
	            Object element = iter.next();   
	          //如果 set 中尚未存在指定的元素，则添加此元素,返回true    
	          //如果此 set 已经包含该元素，则该调用不改变此 set 返回 false。    
	            if (set.add(element))   
	              //添加元素   
	                newList.add(element);   
	  
	        }   
	        return newList;   
	  
	    }  
	    
	    
	    /**
	     * 将map按照key排序
	     * @param map
	     * @return
	     */
        public static Map sort(Map map) {
            Map<Object, Object> mapVK = new TreeMap<Object, Object>(
                new Comparator<Object>() {
                    public int compare(Object obj1, Object obj2) {
                        String v1 = (String)obj1;
                        String v2 = (String)obj2;
                        int s = v2.compareTo(v1);
                        return s;
                    }
                }
            );

            Set col = map.keySet();
            Iterator iter = col.iterator();
            while (iter.hasNext()) {
                String key = (String) iter.next();
                String value = (String) map.get(key);
                mapVK.put(key, value);
            }
            return mapVK;
        }
	   
        /**
         * 将map按value排序(升)
         * @param map
         * @return
         */
        public static Map sortByValue(Map map) {
            List list = new LinkedList(map.entrySet());
            Collections.sort(list, new Comparator(){
                   public int compare(Object o1, Object o2) {
                             return ((Comparable) ((Map.Entry)o1).getValue())
                                .compareTo(((Map.Entry)o2).getValue());
                   }
            });
            Map result = new LinkedHashMap();

            for (Iterator it = list.iterator(); it.hasNext();) {
                  Map.Entry entry = (Map .Entry) it.next();
                  result.put(entry.getKey(), entry.getValue());
            }
            return result;

        }

        /**
         * map 按value降序
         * @param map
         * @param reverse
         * @return
         */
        public static Map sortByValue(Map map, final boolean reverse) {
            List list = new LinkedList(map.entrySet());
            Collections .sort(list, new Comparator() {
                public int compare(Object o1, Object o2) {
                    if (reverse) {
                        return -((Comparable) ((Map .Entry)o1).getValue())
                                .compareTo(((Map .Entry)o2).getValue());
                    }
                    return ((Comparable) ((Map .Entry)o1).getValue())
                            .compareTo(((Map .Entry)o2).getValue());
                }
            });

            Map result = new LinkedHashMap();
            for (Iterator it = list.iterator(); it.hasNext();) {
                Map.Entry entry = (Map.Entry) it.next();
                result.put(entry.getKey(), entry.getValue());
            }
            return result;
    }



}
