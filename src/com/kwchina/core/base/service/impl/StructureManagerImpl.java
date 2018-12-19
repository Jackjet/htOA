package com.kwchina.core.base.service.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.beanutils.BeanComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.StructureInforDAO;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.StructureManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class StructureManagerImpl extends BasicManagerImpl<StructureInfor> implements StructureManager {
	
	private StructureInforDAO structureDAO;
	
	@Autowired
	private PersonInforManager personManager;

	@Autowired
	public void setStructureDAO(StructureInforDAO structureDAO) {
		this.structureDAO = structureDAO;
		super.setDao(structureDAO);
	}

	
	public void save(StructureInfor structure){
		/*Integer structureId = structure.getStructureId();
		if(structureId==null || structureId.intValue()==0){*/
			//新增，设置其fullPath
			String fullPath = "";
			
			ArrayList ls = new ArrayList();
			StructureInfor parent = structure.getParent();
			if(parent!=null){
				Integer parentId = parent.getStructureId();
				ls = getParentsAndSelf(parentId);
				for(Iterator it=ls.iterator();it.hasNext();){
					StructureInfor tempStructure = (StructureInfor)it.next();
					fullPath = fullPath + tempStructure.getStructureId()+",";					
				}
			}
			
			if(fullPath!=null && fullPath.length()>0) fullPath = fullPath.substring(0, fullPath.length()-1);
			structure.setFullPath(fullPath);
			
			//设置其层级
			structure.setLayer(ls.size());
			
		/*}*/
		
		//保存
		this.structureDAO.save(structure);
	}
	
	//删除岗位
	public void remove(Object o){
		StructureInfor tempStructure = (StructureInfor)o;
		Integer structureId = tempStructure.getStructureId();
		
		List ls = this.personManager.getStructurePersons(structureId);
		if(ls==null || ls.size()==0){
			this.structureDAO.remove(o);
		}
	}

	// 获取的岗位信息通过orderNo排序
	public List getStructureOrderBy() {
		/**
		 * Comparator mycmp = ComparableComparator.getInstance(); mycmp =
		 * ComparatorUtils.nullLowComparator(mycmp); //允许null //mycmp =
		 * ComparatorUtils.reversedComparator(mycmp); //逆序 Comparator cmp = new
		 * BeanComparator("id", mycmp); Collections.sort(returnLs, cmp);
		 */
		List ls = this.structureDAO.getAll();
		// 按照orderNo排序
		Collections.sort(ls, new BeanComparator("orderNo"));
		return ls;
	}

	/**
	 * 按照树状结构组织StructureInfor信息
	 * 
	 * @param structrueId:
	 *            根分类Id
	 */
	public ArrayList getStructureAsTree(Integer structureId) {
		ArrayList arrayStructrue = new ArrayList();

		StructureInfor structure = (StructureInfor) this.structureDAO.get(structureId);
		if (structure != null) {
			arrayStructrue.add(structure);
			
			//get sub depart
			addSubStructrueToArray(arrayStructrue, structure);
		}

		return arrayStructrue;
	}

	private void addSubStructrueToArray(ArrayList array, StructureInfor structure) {
		List<StructureInfor> childs = new ArrayList<StructureInfor>(structure.getChilds());
		// 按照orderNo排序
		//Collections.sort(childs, new BeanComparator("orderNo"));
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			StructureInfor subStructure = (StructureInfor) it.next();
			array.add(subStructure);
			
			addSubStructrueToArray(array, subStructure);
		}
	}

	// 获取某个岗位的父岗位信息(从根开始)
	public ArrayList getParents(Integer structrueId) {
		// ArrayList ls = new ArrayList();

		// 根据StructureInfor的fullPath去获取所有父岗位
		StructureInfor structure = (StructureInfor) this.structureDAO.get(structrueId);
		String fullPath = structure.getFullPath();//.replaceAll("\\|", ",");

		ArrayList ls = new ArrayList();
	    if(fullPath!=null && fullPath.length()>0){
	    	String sql = " from StructureInfor structure where structure.structureId in (" + fullPath + ")";
	    	ls = (ArrayList)structureDAO.getResultByQueryString(sql);
	    }

		return (ArrayList) ls;
	}
	
	//某个岗位的父岗位信息+自身
	public ArrayList getParentsAndSelf(Integer structureId){
		ArrayList ls = getParents(structureId);
		
		StructureInfor self = (StructureInfor)this.structureDAO.get(structureId);
		ls.add(self);
		
		return ls;
		
	}
}
