package test.customfields;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;

import com.kwchina.oa.workflow.customfields.domain.CustomizableEntity;
import com.kwchina.oa.workflow.customfields.service.CustomizableEntityManager;
import com.kwchina.oa.workflow.customfields.util.HibernateUtil;

//测试查找自定义字段数据
public class TestQueryCustomFields {
	public static void main(String[] args) {
		Session session = HibernateUtil.getInstance().getCurrentSession();
		Criteria criteria = session.createCriteria(CustomizableEntity.class);
		criteria.add(Restrictions.eq(CustomizableEntityManager.CUSTOM_COMPONENT_NAME + ".email", "test@test.com"));
		List list = criteria.list();
		System.out.println("list.size() = " + list.size());
	}
}
