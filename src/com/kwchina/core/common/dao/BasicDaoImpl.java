package com.kwchina.core.common.dao;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.CriteriaSpecification;
import org.hibernate.criterion.Criterion;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projection;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.impl.CriteriaImpl;
import org.hibernate.transform.ResultTransformer;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.Assert;

import com.kwchina.core.util.jmesa.PageForMesa;
import com.kwchina.core.util.jmesa.PropertyFilter;
import com.kwchina.core.util.jmesa.ReflectionUtils;
import com.kwchina.core.util.jmesa.PropertyFilter.MatchType;

/**
 * Base class for Hibernate DAOs. This class defines common CRUD methods for
 * child classes to inherit. User Spring AOP Inteceptor
 */
@Transactional
public class BasicDaoImpl<T> implements BasicDao<T> {
	@PersistenceContext
	protected EntityManager entityManager;

	/**
	 * 获取Session对象
	 */
	public Session openSession() {
		return (Session) entityManager.getDelegate();
	}

	/**
	 * Logger for this class
	 */
	@SuppressWarnings("unused")
	private static final Logger logger = Logger.getLogger(BasicDaoImpl.class);

	private Class<T> entityClass;

	@SuppressWarnings("unchecked")
	public BasicDaoImpl() {
		this.entityClass = GenericsUtils.getSuperClassGenricType(getClass());
	}

	public T get(Serializable id) throws DataAccessException {
		return entityManager.find(entityClass, id);
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getAll() {
		Query query = this.entityManager.createQuery("from "+entityClass.getSimpleName());
		return query.getResultList();
	}

	public void add(Object o) {
		entityManager.persist(o);
	}

	public void remove(Object o) {
		entityManager.remove(o);
	}

    public void remove(String id) {
       entityManager.remove(entityManager.getReference(entityClass, id));
    }

    public void remove(Integer id) {
		entityManager.remove(entityManager.getReference(entityClass, id));
	}

	public Object save(Object o) {
		Object obj = entityManager.merge(o);
		//this.openSession().saveOrUpdate(o);
		return obj;
	}
	public void saveOrUpdate(Object o) {
//		Object obj = entityManager.merge(o);
		this.openSession().saveOrUpdate(o);
	}

	@SuppressWarnings("unchecked")
	public List<T> getResultByQueryString(final String queryString,
			final boolean isPageAble, final int firstResult,
			final int maxResults) {
		Query query = this.entityManager.createQuery(queryString);

		if (isPageAble) {
			query.setFirstResult(firstResult);
			query.setMaxResults(maxResults);
		}

		List<T> list = query.getResultList();
		return list;
	}

	public int getResultNumByQueryString(String queryString) {
		try {
			List l = this.entityManager.createQuery(queryString).getResultList();
			if (l != null && !l.isEmpty()) {
				Object obj = l.get(0);
				if (obj instanceof Long) {
					return ((Long) l.get(0)).intValue();
				} else {
					return ((Integer) l.get(0)).intValue();
				}
			} else {
				return 0;
			}
		} catch (DataAccessException ex) {
			return 0;
		}
	}

	@SuppressWarnings("unchecked")
	public List<T> getResultByQueryString(String queryString) {
		return this.entityManager.createQuery(queryString).getResultList();
	}

	public void clean(Object o) {
		entityManager.clear();
	}

	@SuppressWarnings("unchecked")
	public List getResultBySQLQuery(String sql) {
		Query query = this.entityManager.createNativeQuery(sql);
		return query.getResultList();
	}

	public T getReference(int id) {
		return entityManager.getReference(entityClass, id);
	}

    public boolean exit(long id) {
        T a=entityManager.find(entityClass, id);
        return !(a==null);
    }
    
    //依据DetachedCriteria的查询
	public List getInforByDetachedCriteria(DetachedCriteria detachedCriteria) {
		Criteria criteria = detachedCriteria.getExecutableCriteria(openSession());
		return criteria.list();
	}
	
	/****************分页(开始)****************/
	//按属性过滤条件列表分页查找对象.
	public PageForMesa<T> findByPage(final PageForMesa<T> page, final List<PropertyFilter> filters, Map<String, String> alias) {
		DetachedCriteria criterion = buildFilterCriterions(filters, alias);
		return find(page, criterion);
	}
	
	//按属性条件列表创建Criterion数组,辅助函数.
	private DetachedCriteria buildFilterCriterions(final List<PropertyFilter> filters, Map<String, String> alias) {
		DetachedCriteria dc = DetachedCriteria.forClass(this.entityClass);
		for (PropertyFilter filter : filters) {
			String propertyName = filter.getPropertyName();

			boolean multiProperty = StringUtils.contains(propertyName, PropertyFilter.OR_SEPARATOR);
			if (!multiProperty) { // properNameName中只有一个属性的情况.
				Criterion criterion = buildPropertyCriterion(propertyName, filter.getValue(), filter.getMatchType());
				if (criterion != null)
					dc.add(criterion);
			} else {// properName中包含多个属性的情况,进行or处理.
				Disjunction disjunction = Restrictions.disjunction();
				String[] params = StringUtils.split(propertyName, PropertyFilter.OR_SEPARATOR);

				for (String param : params) {
					Criterion criterion = buildPropertyCriterion(param, filter.getValue(), filter.getMatchType());
					if (criterion != null)
						disjunction.add(criterion);
				}
				dc.add(disjunction);
			}
		}
		if (alias != null)
			for (Map.Entry<String, String> entry : alias.entrySet()) {
				String key = entry.getKey();
				String value = entry.getValue();
				dc.createAlias(key, value);
			}
		return dc;
	}
	
	//按属性条件参数创建Criterion,辅助函数.
	private Criterion buildPropertyCriterion(final String propertyName, final Object value, final MatchType matchType) {
		Assert.hasText(propertyName, "propertyName不能为空");
		Criterion criterion = null;
		Field field = null;
		try{
			field = entityClass.getDeclaredField(propertyName);
		}catch(NoSuchFieldException nsfe){
			field = null;
		}
		try {
			if (MatchType.EQ.equals(matchType)) {
				if (field!=null && field.getGenericType()
						.toString().indexOf("java.sql.Date") >= 0) {
					criterion = Restrictions.eq(propertyName, Date
							.valueOf(value.toString()));
				} else {
					criterion = Restrictions.eq(propertyName, value);
				}
			}
			if (MatchType.GE.equals(matchType)) {
				if (field!=null && field.getGenericType()
						.toString().indexOf("java.sql.Date") >= 0) {
					criterion = Restrictions.ge(propertyName, Date
							.valueOf(value.toString()));
				} else {
					criterion = Restrictions.ge(propertyName, value);
				}
			}
			if (MatchType.LE.equals(matchType)) {
				if (field!=null && field.getGenericType()
						.toString().indexOf("java.sql.Date") >= 0) {
					criterion = Restrictions.le(propertyName, Date
							.valueOf(value.toString()));
				} else {
					criterion = Restrictions.le(propertyName, value);
				}
			}
			if (MatchType.LIKE.equals(matchType)) {
				if (field!=null && field.getGenericType()
						.toString().indexOf("java.sql.Date") >= 0) {
					criterion = Restrictions.eq(propertyName, Date
							.valueOf(value.toString()));
				} else {
					criterion = Restrictions.like(propertyName, (String) value,
							MatchMode.ANYWHERE);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return criterion;
	}
	
	/**
	 * 按Criteria分页查询.
	 * 
	 * @param page -
	 *            分页参数.支持pageSize、firstResult和orderBy、order、autoCount参数.
	 *            其中autoCount指定是否动态获取总结果数.
	 * @param dc-数量可变的DetachedCriteria
	 * .
	 * @return 分页查询结果.附带结果列表及所有查询时的参数.
	 */
	@SuppressWarnings("unchecked")
	private PageForMesa<T> find(final PageForMesa<T> page, final DetachedCriteria dc) {
		Assert.notNull(page, "page不能为空");

		if (page.getOrderBy() != null && page.getOrderBy().length() > 0) {
			String[] orderBy = page.getOrderBy().split(",");
			String[] order = page.getOrder().split(",");
			for (int i = 0; i < orderBy.length; i++) {
				if (order[i].equals("asc"))
					dc.addOrder(Order.asc(orderBy[i]));
				else
					dc.addOrder(Order.desc(orderBy[i]));
			}
		}
		Criteria c = dc.getExecutableCriteria(this.openSession());

		if (page.isAutoCount()) {
			int totalCount = countCriteriaResult(c);
			page.setTotalCount(totalCount);
		}

		setPageParameter(c, page);
		List all = c.list();
		page.setAll(all);
		// hibernate的firstResult的序号从0开始
		c.setFirstResult(page.getFirst() - 1);
		c.setMaxResults(page.getPageSize());
		List result = c.list();
		page.setResult(result);
		return page;
	}
	
	//执行count查询获得本次Criteria查询所能获得的对象总数.
	@SuppressWarnings("unchecked")
	private int countCriteriaResult(final Criteria c) {
		CriteriaImpl impl = (CriteriaImpl) c;

		//先把Projection、ResultTransformer、OrderBy取出来,清空三者后再执行Count操作
		Projection projection = impl.getProjection();
		ResultTransformer transformer = impl.getResultTransformer();

		List<CriteriaImpl.OrderEntry> orderEntries = null;
		try {
			orderEntries = (List) ReflectionUtils.getFieldValue(impl, "orderEntries");
			ReflectionUtils.setFieldValue(impl, "orderEntries", new ArrayList());
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}" + e.getMessage());
		}

		//执行Count查询
		int totalCount = (Integer) c.setProjection(Projections.rowCount()).uniqueResult();

		//将之前的Projection,ResultTransformer和OrderBy条件重新设回去
		c.setProjection(projection);

		if (projection == null) {
			c.setResultTransformer(CriteriaSpecification.ROOT_ENTITY);
		}
		if (transformer != null) {
			c.setResultTransformer(transformer);
		}
		try {
			ReflectionUtils.setFieldValue(impl, "orderEntries", orderEntries);
		} catch (Exception e) {
			logger.error("不可能抛出的异常:{}" + e.getMessage());
		}

		return totalCount;
	}
	
	//设置分页参数到Criteria对象,辅助函数.
	private Criteria setPageParameter(final Criteria c, final PageForMesa<T> page) {
		if (page.isOrderBySetted()) {
			String[] orderByArray = StringUtils.split(page.getOrderBy(), ',');
			String[] orderArray = StringUtils.split(page.getOrder(), ',');

			Assert.isTrue(orderByArray.length == orderArray.length,"分页多重排序参数中,排序字段与排序方向的个数不相等");

			// for (int i = 0; i < orderByArray.length; i++) {
			// if (Page.ASC.equals(orderArray[i])) {
			// c.addOrder(Order.asc(orderByArray[i]));
			// } else {
			// c.addOrder(Order.desc(orderByArray[i]));
			// }
			// }
		}
		return c;
	}
	/****************分页(结束)****************/
}
