package com.kwchina.core.cms.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.dao.InforCategoryDAO;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforCategoryRightManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service("inforCategoryManager")
public class InforCategoryManagerImpl extends BasicManagerImpl<InforCategory> implements InforCategoryManager{
	 
	private InforCategoryDAO inforCategoryDAO;
	
	@Resource
	private InforCategoryRightManager inforCategoryRightManager;
	
    //注入的方法
	@Autowired
    public void setInforCategoryDAO(InforCategoryDAO inforCategoryDAO){
   	 	this.inforCategoryDAO = inforCategoryDAO;
        super.setDao(inforCategoryDAO);
    }
    
    /**
	 * 按照树状结构组织InforCategory信息 
	 * @param categoryId:分类Id
	 */
	public ArrayList getCategoryAsTree(Integer categoryId){
		ArrayList array = new ArrayList();

		List allCategorys = this.inforCategoryDAO.getAll();
		InforCategory category = (InforCategory) this.inforCategoryDAO.get(categoryId);
		
		if (category != null) {
			array.add(category);
			addSubCategoryToArray(array, category, null, allCategorys);
		}

		return array;
	}
	
	
	private void addSubCategoryToArray(ArrayList array, InforCategory category, SystemUserInfor user, List allCategorys) {
		List<InforCategory> childs = new ArrayList<InforCategory>(category.getChilds());
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			InforCategory subCategory = (InforCategory) it.next();

			if (user != null) {				
				//判断是否有浏览权限
				boolean viewRight = this.inforCategoryRightManager.hasRight(subCategory, user, InforCategoryRight._Right_View);
				if (!viewRight) {
					continue;
				}
			}
			
			/**
			 * leftIndex:如果已经有同一个父分类的，则为同层最大Category的rightIndex+1
			 *           如果没有同一个父分类的，则为parent的leftIndex+1
			 */
			int leftIndex = 0;
			boolean hasBrother = false;
			int maxRightIndex = 0;
			for(Iterator itCategory = array.iterator();itCategory.hasNext();){
				InforCategory tempCategory = (InforCategory)itCategory.next();
				if(tempCategory.getParent()!=null && subCategory.getParent()!=null && tempCategory.getParent().getCategoryId()==subCategory.getParent().getCategoryId()){
					hasBrother = true;
					
					if(tempCategory.getRightIndex()>maxRightIndex)
						maxRightIndex = tempCategory.getRightIndex();
				}
			}
			
			if(hasBrother){
				leftIndex = (maxRightIndex+1);
			}else{
				if(subCategory.getParent()!=null)
					leftIndex = (subCategory.getParent().getLeftIndex()+1);
				else
					leftIndex =1;
			}
			subCategory.setLeftIndex(leftIndex);
			
			//rightIndex
			int subSum = getSumOfSubCategory(subCategory,allCategorys,user);	
			int rightIndex = leftIndex+subSum*2+1;
			subCategory.setRightIndex(rightIndex);
			inforCategoryDAO.save(category);
						
			array.add(subCategory);
			addSubCategoryToArray(array, subCategory, user,allCategorys);
		}
	}
	
	

	// 获取用户有权限看到的分类列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId, SystemUserInfor user) {
		ArrayList arrayCategory = new ArrayList();
		List allCategorys = this.inforCategoryDAO.getAll();

		InforCategory category = (InforCategory) this.inforCategoryDAO.get(categoryId);

		if (category != null) {
			int subSum = getSumOfSubCategory(category,allCategorys,user);
			
			category.setLeftIndex(1);			
			category.setRightIndex(1+subSum*2+1);
			arrayCategory.add(category);			
			
			inforCategoryDAO.save(category);
			
			addSubCategoryToArray(arrayCategory, category, user,allCategorys);
		}

		return arrayCategory;
	}
	
	
	//根据用户获取其有权限看到某个分类下的分类数量
	private int getSumOfSubCategory(InforCategory category, List allCategorys, SystemUserInfor user){
		int number = 0;
		
		List childs = new ArrayList(category.getChilds());		
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			InforCategory subCategory = (InforCategory) it.next();
			
			//if(!subCategory.isDeleted()){
				if (user == null) {
					number += 1;
					number += getSumOfSubCategory(subCategory, allCategorys,user);
				} else {
					//判断是否有浏览权限
					boolean viewRight = this.inforCategoryRightManager.hasRight(subCategory, user, InforCategoryRight._Right_View);
					if (viewRight) {
						number += 1;
						number += getSumOfSubCategory(subCategory, allCategorys,user);
					}
				}
			//}

			
		} 	
		
		return number;		
	}
	
	
	//获取所有叶分类
	public ArrayList getLeafCategory(){
		ArrayList returnLs = new ArrayList();
		
		List allCategory = this.getAll();
		for(Iterator it = allCategory.iterator();it.hasNext();){
			InforCategory category = (InforCategory)it.next();
			if(category.isLeaf()) returnLs.add(category);
		}
		
		
		return returnLs;
	}
	
	//把某个分类的所有子类id按照1,2,3的格式组合
	public String getChildIds(Integer categoryId){
		String ids = "";
		
		ArrayList ls = getCategoryAsTree(categoryId);
		for(Iterator it = ls.iterator();it.hasNext();){
			InforCategory category = (InforCategory)it.next();
			if(it.hasNext()){
				ids = ids + category.getCategoryId() + ",";
			}else{
				ids = ids + category.getCategoryId();
			}
		}
		
		return ids;
	}
	
	
	//获取某个分类的父分类信息(从根开始)
	public ArrayList getParents(Integer categoryId){
		InforCategory category = (InforCategory) this.inforCategoryDAO.get(categoryId);
		String fullPath = category.getFullPath();//.replaceAll("\\|", ",");

		ArrayList ls = new ArrayList();
	    if(fullPath!=null && fullPath.length()>0){
	    	String sql = " from InforCategory category where category.categoryId in (" + fullPath + ")";
	    	ls = (ArrayList)inforCategoryDAO.getResultByQueryString(sql);
	    }

		return (ArrayList) ls;
	}
	
	public void save(InforCategory category){
			
		//设置fullPath
		String fullPath = "";
			
		ArrayList ls = new ArrayList();
		InforCategory parent = category.getParent();
		if(parent!=null){
			Integer parentId = parent.getCategoryId();
			ls = getParentsAndSelf(parentId);
			for(Iterator it=ls.iterator();it.hasNext();){
				InforCategory tempCategory = (InforCategory)it.next();
				fullPath = fullPath + tempCategory.getCategoryId()+",";					
			}
		}
			
		if(fullPath!=null && fullPath.length()>0) fullPath = fullPath.substring(0, fullPath.length()-1);
		category.setFullPath(fullPath);
			
		//设置其层级
		category.setLayer(ls.size());
			
		//保存
		this.inforCategoryDAO.save(category);
	}
	
	//某个分类的父分类+自身
	public ArrayList getParentsAndSelf(Integer categoryId){
		ArrayList ls = getParents(categoryId);
		
		InforCategory self = (InforCategory)this.inforCategoryDAO.get(categoryId);
		ls.add(self);
		
		return ls;
		
	}
	
}
