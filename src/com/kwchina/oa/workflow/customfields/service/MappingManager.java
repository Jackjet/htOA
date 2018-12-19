package com.kwchina.oa.workflow.customfields.service;

import java.util.Iterator;

import org.hibernate.mapping.Column;
import org.hibernate.mapping.Property;
import org.hibernate.type.Type;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;
import com.kwchina.oa.workflow.customfields.util.XMLUtil;


public class MappingManager {
	
	/** 更新类映射 
	 * @param entityManager 自定义实体方法接口
	 * @param hbmName hbm文件名
	 * */
	public static void updateClassMapping(CustomizableEntityManager entityManager, String hbmName) {
		
		try {
			/*Session session = HibernateUtil.getInstance().getCurrentSession();
			Class<? extends CustomizableEntity> entityClass = entityManager.getEntityClass();
			String file = entityClass.getResource(entityClass.getSimpleName() + ".hbm.xml").getPath();*/
			String file = CoreConstant.Context_Real_Path + FlowConstant.Flow_Hbm_Path + "/" + hbmName;
			//String file = "D:/tomcat55/webapps/ROOT" + FlowConstant.Flow_Hbm_Path + "/" + hbmName;

			Document document = XMLUtil.loadDocument(file);
			NodeList componentTags = document.getElementsByTagName("dynamic-component");
			Node node = componentTags.item(0);
			XMLUtil.removeChildren(node);

			Iterator propertyIterator = entityManager.getCustomProperties().getPropertyIterator();
			while (propertyIterator.hasNext()) {
				Property property = (Property) propertyIterator.next();
				Element element = createPropertyElement(document, property);
				node.appendChild(element);
			}

			XMLUtil.saveDocument(document, file);
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		
	}

	/** 创建属性元素 
	 * @param document xml文档
	 * @param property hibernate属性映射
	 * */
	private static Element createPropertyElement(Document document, Property property) {
		Element element = document.createElement("property");
		Type type = property.getType();

		element.setAttribute("name", property.getName());
		element.setAttribute("column", ((Column) property.getColumnIterator().next()).getName());
		element.setAttribute("type", type.getReturnedClass().getName());
		element.setAttribute("not-null", String.valueOf(false));

		return element;
	}
}
