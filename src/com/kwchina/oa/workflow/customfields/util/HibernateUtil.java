package com.kwchina.oa.workflow.customfields.util;

import java.net.URL;

import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.mapping.PersistentClass;

import com.kwchina.core.sys.CoreConstant;

public class HibernateUtil {

	private static HibernateUtil instance;
	private Configuration configuration;
	private SessionFactory sessionFactory;
	private Session session;
	private String hbmName;		//hbm文件名
	
	
	public void setHbmName(String hbmName) {
		this.hbmName = hbmName;
	}
	
	//获取HibernateUtil实例
	public synchronized static HibernateUtil getInstance() {
		if (instance == null) {
			instance = new HibernateUtil();
		}
		return instance;
	}

	//获取session工厂
	private synchronized SessionFactory getSessionFactory() {
		if (sessionFactory == null) {
			sessionFactory = getConfiguration().buildSessionFactory();
		}
		return sessionFactory;
	}

	//获取当前session
	public synchronized Session getCurrentSession() {
		if (session == null) {
			session = getSessionFactory().openSession();
			session.setFlushMode(FlushMode.COMMIT);
			System.out.println("session opened.");
		}
		return session;
	}

	//加载hibernate配置文件,并加载相关映射hbm文件
	private synchronized Configuration getConfiguration() {
		if (configuration == null) {
			System.out.print("configuring Hibernate ... ");
			try {
				URL url = this.getClass().getResource("/com/kwchina/oa/workflow/customfields/hibernate.cfg.xml"); 
				configuration = new Configuration().configure(url);
				//configuration.addClass(CustomizableEntity.class);
				if (hbmName != null && hbmName.length() > 0) {
					configuration.addFile(CoreConstant.Context_Real_Path + FlowConstant.Flow_Hbm_Path + "/" + hbmName);
					//configuration.addFile("D:/tomcat55/webapps/ROOT" + FlowConstant.Flow_Hbm_Path + "/" + hbmName);
				}
				System.out.println("ok");
			}
			catch (HibernateException e) {
				System.out.println("failure");
				e.printStackTrace();
			}
		}
		return configuration;
	}

	//关闭清空所有配置和session
	public void reset() {
		Session session = getCurrentSession();
		if (session != null) {
			session.flush();
			if (session.isOpen()) {
				System.out.print("closing session ... ");
				session.close();
				System.out.println("ok");
			}
		}
		SessionFactory sf = getSessionFactory();
		if (sf != null) {
			System.out.print("closing session factory ... ");
			sf.close();
			System.out.println("ok");
		}
		this.configuration = null;
		this.sessionFactory = null;
		this.session = null;
	}

	/** 获取类映射 
	 * @param entityClass 实体类
	 * */
	public PersistentClass getClassMapping(Class entityClass) {
		return getConfiguration().getClassMapping(entityClass.getName());
	}

}
