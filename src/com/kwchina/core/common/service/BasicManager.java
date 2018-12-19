package com.kwchina.core.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.springframework.dao.DataAccessException;

import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.util.jmesa.PageForMesa;

public interface BasicManager<T> {

	public T get(Serializable id) throws DataAccessException;
	
	public List<T> getAll();

	public void add(Object o);

	public void remove(Object o);

    public void remove(String id);

	public void remove(Integer id);

	public Object save(Object o);

	public List getResultByQueryString(String queryString, boolean isPageAble, int firstResult, int maxResults);

	public int getResultNumByQueryString(String queryString);

	public List<T> getResultByQueryString(String queryString);

	public List<String> getResultBySQLQuery(String sql);
	
	//按属性过滤条件列表分页查找对象.
	public PageForMesa<T> findByPage(PageForMesa page, List filters, Map alias);
	
	//根据查询串SQL查找相应结果(分页显示部分-1)
	public PageList getResultByQueryString(String querySQL, String countSQL, boolean isPageAble, Pages pages);
	
	/**
	 * 基本查询(仅限查询单个表)
	 * @param objName  对象名
	 * @param primykey 主键名
	 * @param params   查询的相关参数:params[0]-sidx;params[1]-sord;params[2]-_search;params[3]-filters.
	 */
	public String[] generateQueryString(String objName, String primykey, String[] params);
	public String[] generateQueryString1(String objName, String primykey, String[] params);

	/**
	 * 自定义查询(可从外部传入hql语句进行组装)
	 * @param queryString 从外部传入的查询语句:queryString[0]-queryHQL;queryString[1]-countHQL.
	 * @param params   查询的相关参数:params[0]-sidx;params[1]-sord;params[2]-_search;params[3]-filters.
	 */
	public String[] generateQueryString(String[] queryString, String[] params);
	
}
