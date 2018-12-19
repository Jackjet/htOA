package com.kwchina.sms.dao;

import static com.kwchina.sms.misc.SysConstants.msDriver;
import static com.kwchina.sms.misc.SysConstants.msPassword;
import static com.kwchina.sms.misc.SysConstants.msURL;
import static com.kwchina.sms.misc.SysConstants.msUser;
import static com.kwchina.sms.misc.SysConstants.oracleDriver;
import static com.kwchina.sms.misc.SysConstants.oraclePwd;
import static com.kwchina.sms.misc.SysConstants.oracleURL;
import static com.kwchina.sms.misc.SysConstants.oracleUser;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.BeanListHandler;


@SuppressWarnings("unchecked")
public class BaseDAO<T> {

	protected Class<T> entityClass;

	/**
	 * 继承DAO子类的情况
	 */
	public BaseDAO() {
		this.entityClass = getSuperClassGenricType(getClass(),0);
	}
	
	/**
	 * 直接创建使用的情况
	 */
	public BaseDAO(Class clazz) {
		this.entityClass = clazz;
	}
	
	/**
	 * 获取数据库连接
	 */
	public Connection getConnection(boolean isOracle) {
		
		Connection conn = null;
		String jdbcURL = isOracle ? oracleURL : msURL;
		String user = isOracle ? oracleUser : msUser;
		String password = isOracle ? oraclePwd : msPassword;

		try {
			DbUtils.loadDriver(isOracle ? oracleDriver : msDriver);
			conn = DriverManager.getConnection(jdbcURL, user, password);
		} catch (SQLException e) {
			//logger.error(e.toString());
			//logger.error("Database connection lost...");
		} 
		
		return conn;
	}

	/**
	 * 查找多个对象
	 */
	public List<T> query(String sqlString, Object params[], boolean isOracle) {

		List<T> beans = null;
		Connection conn = getConnection(isOracle);
		if (conn != null)
		try {
			QueryRunner qRunner = new QueryRunner();
			beans = (List<T>) qRunner.query(conn, sqlString, params, new BeanListHandler(this.entityClass));
		} catch (SQLException e) {
			//logger.error(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return beans;
	}

	/**
	 * 查找单个对象
	 */
	public T get(String sqlString, Object params[], boolean isOracle) {

		Object obj = null;
		Connection conn = getConnection(isOracle);
		
		if (conn != null)
		try {
			QueryRunner qRunner = new QueryRunner();
			obj = qRunner.query(conn, sqlString, params, new BeanHandler(
					this.entityClass));
		} catch (SQLException e) {
			//logger.error(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return (T) obj;
	}
	
	/**
	 * 执行更新的sql语句,插入,修改,删除
	 */
	public boolean update(String sqlString, Object params[], boolean isOracle) {

		Connection conn = getConnection(isOracle);
		boolean flag = false;
		
		if (conn != null)
		try {
			QueryRunner qRunner = new QueryRunner();
			int i = qRunner.update(conn, sqlString, params);
			if (i > 0) {
				flag = true;
			}
		} catch (SQLException e) {
			//logger.error(e.getMessage());
		} finally {
			DbUtils.closeQuietly(conn);
		}

		return flag;
	}
	
	/**
	 * 通过反射机制获取泛型对应的实体类的类型
	 */
	protected Class getSuperClassGenricType(Class clazz, int index)
			throws IndexOutOfBoundsException {
		
		Type genType = clazz.getGenericSuperclass();

		if (!(genType instanceof ParameterizedType)) {
			return Object.class;
		}

		Type[] params = ((ParameterizedType) genType).getActualTypeArguments();

		if (index >= params.length || index < 0) {
			return Object.class;
		}
		if (!(params[index] instanceof Class)) {
			return Object.class;
		}
		
		return (Class) params[index];
	}
}