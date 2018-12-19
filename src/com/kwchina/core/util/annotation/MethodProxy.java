package com.kwchina.core.util.annotation;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodProxy {		
		private Class clazz;	//对象所属的类
		private Object target;	//目标对象
		private Method method;	//目标方法
		private Object[] params;	//参数数组
		
		@SuppressWarnings("unchecked")
		public MethodProxy(Object target, String methodName, Object ... params) {
			rebindTarget(target, methodName, params);	//设置目标对象与方法
		}
		
		/**
		 * 重新设置目标对象与方法
		 * @param target
		 * @param methodName
		 * @param params
		 */
		public void rebindTarget(Object target, String methodName, Object ... params) {
			this.target = target;
			this.clazz = target.getClass();
			rebindMethod(methodName, params);	//设置目标方法
		}
		
		/**
		 * 重新设置目标方法
		 * @param methodName
		 * @param params
		 */
		public void rebindMethod(String methodName, Object ...params) {
			this.params = params;
			int paramLength = params.length;
			Class[] paramTypes = new Class[paramLength];
			for(int i = 0 ; i < paramLength ; i ++ ) {
				paramTypes[i] = params[i].getClass();
			}
			try {
				this.method = clazz.getMethod(methodName, paramTypes);
			} catch (SecurityException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			}
		}
		
		/**
		 * 动态调用已绑定的方法
		 */
		public void doMethod() {
			try {
				this.method.invoke(target, params);
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			}
		}
		
		
		/**
		 * 获取方法上的注解
		 * @param anClazz 注解类
		 * @return
		 */
		public Annotation getAnnotation(Class anClazz) {
			//return this.method.getAnnotation(anClazz);
			return this.getAnnotation(anClazz);
		}
	}
