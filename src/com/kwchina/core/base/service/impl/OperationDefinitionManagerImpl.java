package com.kwchina.core.base.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Property;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.OperationDefinitionDAO;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class OperationDefinitionManagerImpl extends BasicManagerImpl<OperationDefinition> implements OperationDefinitionManager {
	
	private OperationDefinitionDAO operationDefinitionDAO;

	@Autowired
	public void setOperationDefinitionDAO(OperationDefinitionDAO operationDefinitionDAO) {
		this.operationDefinitionDAO = operationDefinitionDAO;
		super.setDao(operationDefinitionDAO);
	}

	//通过方法名获取操作定义
	public OperationDefinition getOperationByMethod(String method) {
		OperationDefinition od = null;
		
		DetachedCriteria inforDC = DetachedCriteria.forClass(OperationDefinition.class);
		inforDC.add(Property.forName("methodName").eq(method));
		List list = this.operationDefinitionDAO.getInforByDetachedCriteria(inforDC);
		
		if (list.size() > 0) {
			od = (OperationDefinition)list.get(0);
		}
		
		return od;
	}
	
	//按照树状结构组织操作信息
	public ArrayList getOperationAsTree(Integer operationId) {
		ArrayList arrayOperation = new ArrayList();

		if (operationId == null || operationId.intValue() == 0) {
			//取得顶层operation
			List allOperations = this.getAll();
			for (Iterator it = allOperations.iterator(); it.hasNext();) {
				OperationDefinition tempOperation = (OperationDefinition) it.next();
				if (tempOperation.getParent() == null) {
					arrayOperation.add(tempOperation);

					//get sub
					addSubOperationToArray(arrayOperation, tempOperation);
				}
			}
		} else {
			OperationDefinition operation = (OperationDefinition) this.operationDefinitionDAO.get(operationId);
			if (operation != null) {
				arrayOperation.add(operation);

				//get sub
				addSubOperationToArray(arrayOperation, operation);
			}
		}

		return arrayOperation;
	}

	private void addSubOperationToArray(ArrayList array, OperationDefinition operation) {
		Set childs = operation.getChilds();
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			OperationDefinition subOperation = (OperationDefinition) it.next();
			array.add(subOperation);

			addSubOperationToArray(array, subOperation);
		}
	}

}
