package com.kwchina.core.util.json;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.hibernate.collection.PersistentSet;
import org.hibernate.proxy.LazyInitializer;

import com.kwchina.core.util.annotation.ObjectId;


public class JSONConvert {

    public static final Logger log = Logger.getLogger(JSONConvert.class);

    //private DictionaryFactory dictionaryFactory;
    
    public JSONArray modelCollect2JSONArray(Collection collect) {
        return modelCollect2JSONArray(collect, null);
    }
    
    public JSONArray modelCollect2JSONArray(Collection collect, Collection jsonAwareCollect) {
        JSONArray jsonArray = new JSONArray();
        for (Iterator iter = collect.iterator(); iter.hasNext();) {
            Object object = iter.next();
            if (object instanceof Collection) {
                object = modelCollect2JSONArray(collect, jsonAwareCollect);
            } else if (object instanceof Map ||
                object instanceof CharSequence ||
                object instanceof Number
                    ) {
                JsonConfig jsonConfig = new JsonConfig();
                jsonConfig.setExcludes(new String[]{"personModules"});
//                jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);

                object = JSONObject.fromObject(object,jsonConfig);
            } else {
                object = model2JSON(object, jsonAwareCollect);

            }
//            JsonConfig
            jsonArray.add(object);
        }
        return jsonArray;
    }

    public JSONObject model2JSON(Object model) {
        return model2JSON(model, null);
    }
    
    
    /**
    public static ClassMetadata getClassMetadata(Class<?> cls) {   
    	       //return getSessionFactory().getClassMetadata(cls);
    	        *  ClassMetadata meta = super.getSessionFactory().getClassMetadata(clazz);         
    	        String entityName = meta.getEntityName();      
    	         String pkName = meta.getIdentifierPropertyName();
    }
    
    public static SessionFactory getSessionFactory() {   
    	      
    	       return (SessionFactory) applicationContext.getBean("sessionFactory");   
     } */  
    
    /**
     * 把model转换为JSON对象，凡是实现JSONNotAware的不引入，按照get方法得到属性信息
     * 向量的判断暂时只有Collection，也就是说会对Collection中所有JSONNotAware的不引入，
     * 目的是减少不必要的延迟加载，需要引入的话把名字通过jsonAwareArray传入
     * @param model 需要转换的对象
     * @param jsonAwareArray 需要引入的变量
     * @return
     */
    public JSONObject model2JSON(Object model, Collection jsonAwareCollect) {
        JSONObject jsonObject = new JSONObject();
        Method[] methods = model.getClass().getMethods();
        
        for (int i = 0; i < methods.length; i++) {
            Method method = methods[i];
            
            /*BaseModel中不需要getClass*/
            if (method.getName().equals("getClass")) {
                continue;
            }
            if (!ModelUtils.isGetMethod(method)) {
                continue;
            }
            
            Class returnType = method.getReturnType();
            /*不需要转换的类型*/
            if (returnType == Document.class || 
                returnType == byte[].class ||
                returnType == Logger.class ||
                returnType == LazyInitializer.class
               ) {
                continue;
            }
            
            String propertyName = ModelUtils.getPropertyName(method.getName());
            Object getObj = ModelUtils.invokeGetMethod(model, method);
            if (getObj == null) {
                continue;
            }
            
            //对象信息中,需要获取对象的
            Collection subCollect = getAwareSubList(jsonAwareCollect, propertyName);
            
            
            if (getObj instanceof LazyInitializer) {
                continue;
            } else if(getObj instanceof PersistentSet){
            	continue;
            } else if (getObj instanceof Calendar) {        
            	getObj = new java.util.Date(((Calendar)getObj).getTimeInMillis());
            } else if (getObj instanceof Timestamp) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            	getObj = sdf.format(getObj);
            } else if (getObj instanceof Date) {
            	SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            	getObj = sdf.format(getObj);
            } else if (getObj instanceof Enumeration) {
                //数据字典转换，已知code得到name
                Enumeration tempEnum = (Enumeration) getObj;
                //dictionaryFactory.setName(tempEnum);
                //if (tempEnum.getName() == null) {
                	//log.info("●●●●●●●●●●●●●●●method:" + model.getClass().getName() + "." + method.getName());
                //}
            } else if (getObj instanceof JSONNotAware) {            	            	
            	/*判断的时候，jsonAwareCollect优先级高于JSONNotAware*/
                if (subCollect == null) {
                	String getId = "";
                	
                	try{
    	            	Class clazz = getObj.getClass();
    	            	
    	            	String className = clazz.getName();
    	            	int pos = className.indexOf("_");
    	            	if(pos>0)
    	            		className = className.substring(0, pos);
    	            	 java.lang.Class   myclass   =   java.lang.Class.forName(className);   
    	            	 //Annotation[] annotations = myclass.getAnnotations();
    	            	 Annotation annotation = myclass.getAnnotation(ObjectId.class);
    	            	 if(annotation!=null) {
    	            		 ObjectId objId = (ObjectId)annotation;
    	            		 getId = objId.id();
    	            	 }
    	            	 
    	            	 /**
    	            	 Object obj = clazz.newInstance();
    	            	 Annotation[] as = obj.getClass().getAnnotations();
    	            	
    	            	Annotation annotation = clazz.getAnnotation(ObjectId.class);
    	            	Annotation[] annos = clazz.getDeclaredAnnotations();
    	            	//ObjectId objId = (ObjectId)clazz.getAnnotations(ObjectId.class);
    	            	
    	            	SystemUserInfor user = new SystemUserInfor();
    	            	Annotation[] ans = user.getClass().getAnnotations();
    	            	
    	            	
    	            	Method mt = clazz.getMethod("getObjectId");   
    	            	ObjectId objId = mt.getAnnotation(ObjectId.class);   
    	            	
    	            	System.out.println(objId);
    	            	*/
                	}catch(Exception ex){
                		//ex.printStackTrace();
                	}
                	
                	
                    /*只有多对一的情况下会到此逻辑，如果没有指定加载这个对象，只加载idStr*/
                	if(!getId.equals("")){
	                    Class clazz = getObj.getClass();
	                    Method getIdStrMothod = ModelUtils.getMethod(clazz,ModelUtils.getGetMehodName(getId), new Class[0]);
	                                         
	                    JSONObject tempJSONObject = new JSONObject();
	                    tempJSONObject.put(getId, ModelUtils.invokeGetMethod(getObj, getIdStrMothod));
	                    getObj = tempJSONObject;
                	}
                } else {
                    getObj = model2JSON(getObj, subCollect);
                }
            } /*else if (getObj instanceof AbstractPersistentCollection) {
            	//Hibernate的List，可能触发延迟加载
            	
                if (subCollect == null) {
                    continue;
                }
                
                JSONArray jsonArray = new JSONArray();
                for (Iterator iter = ((Collection) getObj).iterator(); iter.hasNext();) {
                    jsonArray.add(model2JSON(iter.next(), subCollect));
                }
                getObj = jsonArray;
            }*/
            
            try {            	
                jsonObject.put(propertyName, getObj);
            } catch (Exception ex) {
                log.info("propertyName:" + propertyName + ",ex:" + ex.getMessage());
//                ex.printStackTrace();
            }
        }
        
        return jsonObject;
    }
    
    
    /**从原始的jsonAwareList中查找前缀为name的，去除前缀后，放到新的List中返回
     * 例，user.group，去除前缀后就是group，返回的结果主要用于递归下一层的需要
     * @param jsonAwareCollect 原始jsonAwareList
     * @param name 需要判断的属性名称
     * @return 如果连名字符合的都没有返回null，如果有符合的但是没有subList返回new LinkedList()
     */
    private static Collection getAwareSubList(Collection jsonAwareCollect, String name) {
        if (jsonAwareCollect == null) {
            return null;
        }
        if (StringUtils.isEmpty(name)) {
            throw new IllegalArgumentException("name is empty");
        }
        
        boolean needAware = false;
        Collection awareSubCollect = new LinkedList();
        for (Iterator iter = jsonAwareCollect.iterator(); iter.hasNext();) {
            String element = (String) iter.next();
            if (StringUtils.isEmpty(element)) {
                throw new IllegalArgumentException("element is empty");
            }
            if (element.startsWith(name + ".")) {
                String subStr = element.substring(name.length() + 1);
                if (StringUtils.isEmpty(subStr)) {
                    throw new IllegalArgumentException("subStr is empty, element:" + element);
                }
                awareSubCollect.add(subStr);
                needAware = true;
            } else if (element.startsWith(name)) {
                needAware = true;
            }
        }
        if (!needAware) {
            return null;
        }
        
        return awareSubCollect;
    }
    
    /**
    public void setDictionaryFactory(DictionaryFactory dictionaryFactory) {
        this.dictionaryFactory = dictionaryFactory;
    }
	*/
}
