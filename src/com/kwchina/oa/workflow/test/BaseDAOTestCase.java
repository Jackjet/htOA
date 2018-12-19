package com.kwchina.oa.workflow.test;

import junit.framework.TestCase;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.beans.factory.xml.XmlBeanDefinitionReader;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

public class BaseDAOTestCase extends TestCase {
	// protected final static XmlBeanFactory factory;
	protected final static BeanFactory factory;
	static {
		// String file = getClass().getResource("resource.xml").getFile();
		// XmlBeanFactory xbf = new XmlBeanFactory(new
		// FileSystemResource(file));

		// Resource resource=new
		// FileSystemResource("E:\\myWorkSpace\\xinhuaHR\\web\\WEB-INF\\applicationContext.xml,E:\\myWorkSpace\\xinhuaHR\\web\\WEB-INF\\applicationContext-attendance.xml");
		// Resource rs1 = new ClassPathResource("applicationContext.xml");
		// factory = new XmlBeanFactory(resource);

		BeanDefinitionRegistry reg = new DefaultListableBeanFactory();

		XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(reg);
		Resource resource = new FileSystemResource("E:\\myWorkSpace\\oa\\web\\WEB-INF\\applicationContext.xml");
		reader.loadBeanDefinitions(resource);
		resource = new FileSystemResource("E:\\myWorkSpace\\oa\\web\\WEB-INF\\applicationContext-core.xml");
		reader.loadBeanDefinitions(resource);
		resource = new FileSystemResource("E:\\myWorkSpace\\oa\\web\\WEB-INF\\applicationContext-base.xml");
		reader.loadBeanDefinitions(resource);
		resource = new FileSystemResource("E:\\myWorkSpace\\oa\\web\\WEB-INF\\applicationContext-workflow.xml");
		reader.loadBeanDefinitions(resource);
		
		factory = (BeanFactory) reg;
	}

	public BaseDAOTestCase(String arg0) {
		super(arg0);
	}

	
	/**
	 * �ӳټ��س���ͨ����дTestCase���е�setUp��tearDown����4ʵ�����Ҫ��
	 */
	private SessionFactory sessionFactory;
	private Session session;

	public void setUp() throws Exception {
		super.setUp();
		SessionFactory sessionFactory = (SessionFactory) getBean("sessionFactory");
		session = SessionFactoryUtils.getSession(sessionFactory, true);
		Session s = sessionFactory.openSession();
		TransactionSynchronizationManager.bindResource(sessionFactory, new SessionHolder(s));
	}

	protected Object getBean(String beanName) {
		// Code to get objects from Spring application context
		return factory.getBean(beanName);
	}

	public void tearDown() throws Exception {
		super.tearDown();
		SessionHolder holder = (SessionHolder) TransactionSynchronizationManager.getResource(sessionFactory);
		Session s = holder.getSession();
		s.flush();
		TransactionSynchronizationManager.unbindResource(sessionFactory);
		//SessionFactoryUtils.closeSessionIfNecessary(s, sessionFactory);
	}

}
