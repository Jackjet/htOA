package com.kwchina.extend.supervise.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.extend.supervise.dao.TaskCategoryDAO;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;
import com.kwchina.extend.supervise.service.TaskCategoryManager;
import com.kwchina.extend.supervise.service.TaskCategoryRightManager;
import com.kwchina.extend.supervise.util.TaskConstant;
import com.kwchina.extend.supervise.vo.TaskCategoryVo;

@Service
public class TaskCategoryManagerImpl extends BasicManagerImpl<TaskCategory> implements TaskCategoryManager {

	private TaskCategoryDAO taskCategoryDAO;

	@Resource
	private TaskCategoryRightManager taskCategoryRightManager;
	
	@Resource
	private SystemUserManager systemUserManager;

	//注入的方法
	@Autowired
	public void setTaskCategoryDAO(TaskCategoryDAO taskCategoryDAO) {
		this.taskCategoryDAO = taskCategoryDAO;
		super.setDao(taskCategoryDAO);
	}
	
	//把某个分类的所有子类id按照1,2,3的格式组合
	public String getChildIds(Integer categoryId){
		String ids = "";
		
		ArrayList ls = getCategoryAsTree(categoryId);
		for(Iterator it = ls.iterator();it.hasNext();){
			TaskCategory category = (TaskCategory)it.next();
			if(it.hasNext()){
				ids = ids + category.getCategoryId() + ",";
			}else{
				ids = ids + category.getCategoryId();
			}
		}
		
		return ids;
	}

	//获取全部叶文档分类
	public List getAllLeafCategory() {
		List ls = new ArrayList();

		List categorys = this.taskCategoryDAO.getAll();
		for (Iterator it = categorys.iterator(); it.hasNext();) {
			TaskCategory category = (TaskCategory) it.next();
			if (category.isLeaf()) {
				ls.add(category);
			}
		}

		return ls;
	}

	//获取某个层次的文档分类
	public List getLayerCategorys(int layer) {
		return this.taskCategoryDAO.getLayerCategorys(layer);
	}

	//获取所有父分类
	public List getParentCategory() {
		return this.taskCategoryDAO.getParentCategory();
	};


	//判断用户对某个分类是否具有操作权限
	public boolean hasRight(TaskCategory category, SystemUserInfor systemUser) {
		boolean hasRight = false;

		int userId = systemUser.getPersonId().intValue();

		Set rights = category.getRights();
		if (rights == null || rights.size() == 0) {
			//未设置任何权限
			hasRight = true;
		} else {
			for (Iterator it = rights.iterator(); it.hasNext();) {
				TaskCategoryRight categoryRight = (TaskCategoryRight) it.next();
				int tempUserId = categoryRight.getUser().getPersonId().intValue();
				if (userId == tempUserId) {
					hasRight = true;
					break;
				}
			}
		}

		return hasRight;
	}

	//获取列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId) {
		ArrayList arrayCategory = new ArrayList();

		List allCategorys = this.taskCategoryDAO.getAll();
		TaskCategory category = (TaskCategory) this.taskCategoryDAO.get(categoryId);

		if (category != null) {
			arrayCategory.add(category);
			addSubCategoryToArray(arrayCategory, category, null,allCategorys);
		}
		return arrayCategory;
	}

	private void addSubCategoryToArray(ArrayList array, TaskCategory category, SystemUserInfor user,List allCategorys) {
		List childs = new ArrayList(category.getChilds());
		//Collections.sort(childs, new BeanComparator("categoryId"));
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			TaskCategory subCategory = (TaskCategory) it.next();

			if (user != null) {				
				//判断是否有浏览权限
				boolean viewRight = this.taskCategoryRightManager.hasRight(subCategory, user, TaskCategoryRight._Right_View);
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
				TaskCategory tempCategory = (TaskCategory)itCategory.next();
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
			//List allCategorys = this.taskCategoryDAO.getAll();
			int subSum = getSumOfSubCategory(subCategory,allCategorys,user);	
			int rightIndex = leftIndex+subSum*2+1;
			subCategory.setRightIndex(rightIndex);
			
			taskCategoryDAO.save(category);
						
			array.add(subCategory);
			addSubCategoryToArray(array, subCategory, user,allCategorys);
		}
	}
	
	

	// 获取用户有权限看到的分类列表，树状显示
	public ArrayList getCategoryAsTree(Integer categoryId, SystemUserInfor user) {
		ArrayList arrayCategory = new ArrayList();
		List allCategorys = this.taskCategoryDAO.getAll();

		TaskCategory category = (TaskCategory) this.taskCategoryDAO.get(categoryId);

		if (category != null) {
			int subSum = getSumOfSubCategory(category,allCategorys,user);
			
			category.setLeftIndex(1);			
			category.setRightIndex(1+subSum*2+1);
			arrayCategory.add(category);			
			
			taskCategoryDAO.save(category);
			
			addSubCategoryToArray(arrayCategory, category, user,allCategorys);
		}

		return arrayCategory;
	}
	
	
	//根据用户获取其有权限看到某个分类下的分类数量
	private int getSumOfSubCategory(TaskCategory category,List allCategorys,SystemUserInfor user){
		int number = 0;
		
		List childs = new ArrayList(category.getChilds());		
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			TaskCategory subCategory = (TaskCategory) it.next();

			if (user == null) {
				number += 1;
				number += getSumOfSubCategory(subCategory, allCategorys,user);
			} else {
				//判断是否有浏览权限
				boolean viewRight = this.taskCategoryRightManager.hasRight(subCategory, user, TaskCategoryRight._Right_View);
				if (viewRight) {
					number += 1;
					number += getSumOfSubCategory(subCategory, allCategorys,user);
				}
			}
		} 	
		
		return number;		
	}
	
	

	/**
	 * 找到某个分类所有父分类信息(从最顶层到该层的序列)
	 */
	public ArrayList getAllParentCategory(TaskCategory category) {
		ArrayList tempList = new ArrayList();
		tempList.add(category);

		TaskCategory parent = category.getParent();
		while (parent != null && parent.getCategoryId().intValue() != TaskConstant._Root_Category_Id) {
			tempList.add(parent);

			parent = parent.getParent();
		}

		ArrayList reList = new ArrayList();
		for (int k = tempList.size() - 1; k >= 0; k--) {
			reList.add(tempList.get(k));
		}
		return reList;
	}

	/**
	 * 获取全部 某个分类下所有分类的Id,并构造为(1,2,3)的格式
	 * 
	 */
	public String getSubCategoryIds(SystemUserInfor user, int categoryId) {
		String ids = "";
		TaskCategory category = (TaskCategory) this.taskCategoryDAO.get(categoryId);

		if (category != null) {
			ids += "," + category.getCategoryId();
			String aaa = addSubCategoryId(user, category);
			ids += aaa;
		}

		return ids;
	}

	private String addSubCategoryId(SystemUserInfor user, TaskCategory category) {
		List childs = new ArrayList(category.getChilds());
		Iterator it = childs.iterator();

		String ids = "";
		while (it.hasNext()) {
			TaskCategory tempCategory = (TaskCategory) it.next();

			// if (!tempCategory.equals(tempCategory.getParent())) {
			// 判断用户权限,管理员具有全部权限,否则需要判断权限
			// boolean hasRight = false;
			// hasRight = hasRight(category,user);

			// if(hasRight){
			ids = ids + "," + tempCategory.getCategoryId();
			ids += addSubCategoryId(user, tempCategory);

			// }
			// }
		}

		return ids;
	}
	
	//保存分类信息(包括权限信息)
	public void saveCategory(TaskCategory category, TaskCategoryVo vo) {
		
		Set oldRights = category.getRights();
		category.getRights().removeAll(oldRights);

		//获取权限信息
		int[] createIds = vo.getCreateIds();
		int[] deleteIds = vo.getDeleteIds();
		int[] viewIds = vo.getViewIds();

		//创建
		if (createIds != null) {
			for (int k = 0; k < createIds.length; k++) {
				int userId = createIds[k];
				SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);

				TaskCategoryRight right = new TaskCategoryRight();
				right.setCategory(category);
				right.setOperateRight(1);
				right.setUser(user);

				category.getRights().add(right);
			}
		}

		//删除
		if (deleteIds != null) {
			for (int k = 0; k < deleteIds.length; k++) {
				Set rights = category.getRights();
				int userId = deleteIds[k];
				boolean has = false;
				
				for (Iterator it = rights.iterator(); it.hasNext();) {
					TaskCategoryRight right = (TaskCategoryRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 2);

						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);

					TaskCategoryRight right = new TaskCategoryRight();
					right.setCategory(category);
					right.setOperateRight(2);
					right.setUser(user);

					category.getRights().add(right);
				}
			}
		}

		//浏览
		if (viewIds != null) {
			for (int k = 0; k < viewIds.length; k++) {
				Set rights = category.getRights();
				int userId = viewIds[k];
				boolean has = false;

				for (Iterator it = rights.iterator(); it.hasNext();) {
					TaskCategoryRight right = (TaskCategoryRight) it.next();
					if (right.getUser().getPersonId().intValue() == userId) {
						right.setOperateRight(right.getOperateRight() + 4);

						has = true;
						break;
					}
				}

				if (!has) {
					SystemUserInfor user = (SystemUserInfor) this.systemUserManager.get(userId);

					TaskCategoryRight right = new TaskCategoryRight();
					right.setCategory(category);
					right.setOperateRight(4);
					right.setUser(user);

					category.getRights().add(right);
				}
			}
		}

		this.taskCategoryDAO.save(category);
		
	}
	
	//获取分类名称
	public List<TaskCategory> getCategoryName() {
		List<TaskCategory> returnLs = new ArrayList<TaskCategory>();
 		String hql = "from TaskCategory";
		returnLs = this.taskCategoryDAO.getResultByQueryString(hql); 		

 		return returnLs;
	}


}
