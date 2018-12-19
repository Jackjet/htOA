package com.kwchina.core.common.service;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.dao.DataAccessException;

import com.kwchina.core.common.dao.BasicDao;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.util.jmesa.PageForMesa;
import com.kwchina.core.util.multisearch.ConditionUtils;


public class BasicManagerImpl<T> implements BasicManager {
	 private BasicDao basicDAO;
	 
	//注入的方法
	public void setDao(BasicDao basicDAO){ 
		this.basicDAO = basicDAO; 
	}	 
	
	@SuppressWarnings("unchecked")
	public T get(Serializable id) throws DataAccessException {
		return (T) this.basicDAO.get(id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		return this.basicDAO.getAll();
	}

	public void add(Object o) {
		this.basicDAO.add(o);
	}

	public void remove(Object o) {
		this.basicDAO.remove(o);
	}

	public void remove(String id) {
		this.basicDAO.remove(id);
	}

	public void remove(Integer id) {
		this.basicDAO.remove(id);
	}

	public Object save(Object o) {
		Object obj = this.basicDAO.save(o);
		return obj;
	}

	public List getResultByQueryString(String queryString, boolean isPageAble, int firstResult, int maxResults) {
		return this.basicDAO.getResultByQueryString(queryString, isPageAble, firstResult, maxResults);
	}

	public int getResultNumByQueryString(String queryString) {
		return this.basicDAO.getResultNumByQueryString(queryString);
	}

	@SuppressWarnings("unchecked")
	public List<T> getResultByQueryString(String queryString) {
		return this.basicDAO.getResultByQueryString(queryString);
	}

	@SuppressWarnings("unchecked")
	public List<String> getResultBySQLQuery(String sql) {
		return this.basicDAO.getResultBySQLQuery(sql);
	}
	
	//按属性过滤条件列表分页查找对象.
	@SuppressWarnings("unchecked")
	public PageForMesa<T> findByPage(PageForMesa page, List filters, Map alias) {
		return this.basicDAO.findByPage(page, filters, alias);
	}
	
	//根据查询串SQL查找相应结果(分页显示部分-1)
	public PageList getResultByQueryString(String querySQL, String countSQL, boolean isPageAble, Pages pages){
		PageList pl = new PageList();

		if (isPageAble) {
			if (pages.getTotals() == -1) {
				pages.setTotals(this.basicDAO.getResultNumByQueryString(countSQL));
			}
			pages.doPageBreak();
		}

		List l = this.basicDAO.getResultByQueryString(querySQL, isPageAble, pages.getSpage(), pages.getPerPageNum());

		pl.setObjectList(l);
		pl.setPageShowString(pages.getListPageBreak());
		pl.setPages(pages);
		return pl;
	}
	
	/**
	 * 基本查询(仅限查询单个表)
	 * @param objName  对象名
	 * @param primykey 主键名
	 * @param params   查询的相关参数:params[0]-sidx;params[1]-sord;params[2]-_search;params[3]-filters.
	 */
	public String[] generateQueryString(String objName, String primykey, String[] params) {
		String[] queryString = new String[2];
		
		queryString[0] = " from " + objName + " where 1=1";
		queryString[1] = " select count(" + primykey + ") from " + objName + " where 1=1";
		
		queryString = generateQueryString(queryString, params);
		
		return queryString;
	}
	public String[] generateQueryString1(String objName, String primykey, String[] params) {
		String[] queryString = new String[2];

		queryString[0] = " from " + objName + " where 1=1 and valid=1";
		queryString[1] = " select count(" + primykey + ") from " + objName + " where 1=1 and valid=1";

		queryString = generateQueryString(queryString, params);

		return queryString;
	}
	
	/**
	 * 自定义查询(可从外部传入hql语句进行组装)
	 * @param queryString 从外部传入的查询语句:queryString[0]-queryHQL;queryString[1]-countHQL.
	 * @param params   查询的相关参数:params[0]-sidx;params[1]-sord;params[2]-_search;params[3]-filters.
	 */
	public String[] generateQueryString(String[] queryString, String[] params) {
		
		//构造查询条件
		String conditions = generateCondition(params[3]);

		//查询操作时,加入查询条件
		if (params[2].equals("true") && conditions != null && conditions.length() > 0) {
			queryString[0] += " and (" + conditions + ")";
			queryString[1] += " and (" + conditions + ")";
		}
		queryString[0] += " order by " + params[0] + " " + params[1];
		
		return queryString;
	}
	
	/**
	 * 构造查询条件
	 * @param filters 前台获取的查询条件数据,主要包括:查询字段,查询条件,查询数据.
	 */
	public String generateCondition(String filters) {
		
		StringBuffer conditions = new StringBuffer();
		
		if (filters != null && filters.length() > 0) {
			JSONObject filter = JSONObject.fromObject(filters);
			String groupOp = filter.getString("groupOp");		//取数据中的匹配方式:与,或
			JSONArray rules = filter.getJSONArray("rules");		//取数据中的查询信息:查询字段,查询条件,查询数据
			if (rules != null && rules.size() > 0) {
				for (int i=0;i<rules.size();i++) {
					JSONObject tmpObj = (JSONObject)rules.get(i);
					String fieldValue = tmpObj.getString("field");	//查询字段
					String opValue = tmpObj.getString("op");		//查询条件:大于,等于,小于..
					String dataValue = tmpObj.getString("data");	//查询数据
					String condition = ConditionUtils.getCondition(fieldValue, opValue, dataValue);
					if (i == rules.size()-1 || rules.size() == 1) {
						conditions.append(condition);
					}else {
						conditions.append(condition + groupOp.toLowerCase() + " ");
					}
				}
			}
		}
		
		if (conditions != null && conditions.length() > 0) {
			return conditions.toString();
		}
		return null;
	}
	
}
