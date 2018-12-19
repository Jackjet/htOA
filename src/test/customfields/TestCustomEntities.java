package test.customfields;

//import org.hibernate.Session;
//import org.hibernate.Transaction;

import com.kwchina.oa.workflow.customfields.domain.CustomizableEntity;
import com.kwchina.oa.workflow.customfields.service.CustomizableEntityManager;
import com.kwchina.oa.workflow.customfields.service.impl.CustomizableEntityManagerImpl;
import com.kwchina.oa.workflow.customfields.util.HibernateUtil;

//测试自定义实体
public class TestCustomEntities {
	
	private static final String TEST_FIELD_NAME = "nodeId7";
	private static final String TEST_VALUE = "test@test.com";

	public static void main(String[] args) {
		
		HibernateUtil hibernateUtil = HibernateUtil.getInstance();
		hibernateUtil.setHbmName("CustomizableEntity.hbm.xml");
		//启动Configuration配置,并注入hbm文件
		hibernateUtil.getCurrentSession();

		CustomizableEntityManager contactEntityManager = new CustomizableEntityManagerImpl(CustomizableEntity.class);
		contactEntityManager.addCustomField(TEST_FIELD_NAME, Integer.class, "CustomizableEntity.hbm.xml");
		
		//重新启动Configuration配置,使之前对hbm文件的修改同步到数据库
		hibernateUtil.getCurrentSession();
		hibernateUtil.reset();

		/*Session session = hibernateUtil.getCurrentSession();
		Transaction tx = session.beginTransaction();
		try {

			CustomizableEntity customizableEntity = new CustomizableEntity();
			customizableEntity.setValueOfCustomField(TEST_FIELD_NAME, TEST_VALUE);
			Serializable id = session.save(customizableEntity);
			tx.commit();

			customizableEntity = (CustomizableEntity) session.get(CustomizableEntity.class, id);
			Object value = customizableEntity.getValueOfCustomField(TEST_FIELD_NAME);
			System.out.println("value = " + value);

		} catch (Exception e) {
			tx.rollback();
			System.out.println("e = " + e);
		}*/
	}
}
