package com.kwchina.core.common.dao;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import org.hibernate.Session;
import org.hibernate.criterion.DetachedCriteria;
import org.springframework.dao.DataAccessException;

import com.kwchina.core.util.jmesa.PageForMesa;
import com.kwchina.core.util.jmesa.PropertyFilter;

public interface BasicDao<T> {

	public Session openSession();
	
	public T get(Serializable id) throws DataAccessException;
	
	public List<T> getAll();

	public void add(Object o);

	public void remove(Object o);

    public void remove(String id);

	public void remove(Integer id);

	public Object save(Object o);
	public void saveOrUpdate(Object o);
	public List getResultByQueryString(String queryString, boolean isPageAble, int firstResult, int maxResults);

	public int getResultNumByQueryString(String queryString);

	public List<T> getResultByQueryString(String queryString);

	public void clean(Object o);

	public List<String> getResultBySQLQuery(String sql);

	public T getReference(int id);

    public boolean exit(long id);
    
	//依据DetachedCriteria的查询
	public List getInforByDetachedCriteria(DetachedCriteria detachedCriteria);
	
	//按属性过滤条件列表分页查找对象.
	public PageForMesa<T> findByPage(PageForMesa<T> page, List<PropertyFilter> filters, Map<String, String> alias);
}
