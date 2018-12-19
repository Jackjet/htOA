package com.kwchina.core.base.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.OrganizeInforDAO;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.sys.CoreConstant;

@Service("organizeManager")
public class OrganizeManagerImpl extends BasicManagerImpl<OrganizeInfor> implements OrganizeManager{
	 
	private OrganizeInforDAO organizeInforDAO;
	 
	@Autowired
    public void setOrganizeDAO(OrganizeInforDAO organizeInforDAO){
    	this.organizeInforDAO = organizeInforDAO;
        super.setDao(organizeInforDAO);    
    }
     
     
    //获取部门信息
 	public List getDepartments(){
 		List<OrganizeInfor> returnLs = new ArrayList<OrganizeInfor>();
 		
 		List allOrganize = this.organizeInforDAO.getAll(); 		
 		for (Iterator it = allOrganize.iterator(); it.hasNext();) {
 			OrganizeInfor organize = (OrganizeInfor)it.next();
 			if(organize.getLevelId() == CoreConstant.Core_Organize_Level_Department) {
 				/*OrganizeInfor tmpObj = new OrganizeInfor();
 				organize.setParent(null);
 				organize.setChilds(null);
 				try {
					BeanUtils.copyProperties(tmpObj, organize);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
 				returnLs.add(organize);
 			}
 		}
 		
 		//按照orderNo排序
 		//Collections.sort(returnLs, new BeanComparator("orderNo"));    
 		
 		//上面的排序遇到属性为null就会抛出异常, 也不能设定升序还是降序。    
        //可借助commons-collections包的ComparatorUtils,BeanComparator，ComparableComparator和ComparatorChain都是实现了Comparator这个接口    
        /**
 		Comparator mycmp = ComparableComparator.getInstance();       
        mycmp = ComparatorUtils.nullLowComparator(mycmp);  //允许null       
        //mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序       
        Comparator cmp = new BeanComparator("id", mycmp);    
        Collections.sort(returnLs, cmp);    
		*/
 		
 		return returnLs;
 	}
 	
 	//获取班组信息
 	public List getGroups(){
 		List<OrganizeInfor> returnLs = new ArrayList<OrganizeInfor>();
 		
 		List allOrganize = this.organizeInforDAO.getAll(); 		
 		for (Iterator it = allOrganize.iterator(); it.hasNext();) {
 			OrganizeInfor organize = (OrganizeInfor)it.next();
 			if(organize.getLevelId() == CoreConstant.Core_Organize_Level_Group) {
 				/*OrganizeInfor tmpObj = new OrganizeInfor();
 				organize.setParent(null);
 				organize.setChilds(null);
 				try {
					BeanUtils.copyProperties(tmpObj, organize);
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}*/
 				returnLs.add(organize);
 			}
 		}
 		
 		//按照orderNo排序
 		//Collections.sort(returnLs, new BeanComparator("orderNo"));  
 		
 		return returnLs;
 	}
 	
 	//获取未删除的所有组织结构信息
 	public List getUndeleted(){
 		List<OrganizeInfor> returnLs = new ArrayList<OrganizeInfor>();
 		
 		List allOrganize = this.organizeInforDAO.getAll(); 		
 		for (Iterator it = allOrganize.iterator(); it.hasNext();) {
 			OrganizeInfor organize = (OrganizeInfor)it.next();
 			if(!organize.isDeleted())
 				returnLs.add(organize);
 		}
 		
 		//按照orderNo排序
 		//Collections.sort(returnLs, new BeanComparator("orderNo"));  
 		
 		return returnLs;
 	}
 	
 	
 	/**
	 * 按照树状结构组织OrganizeInfor信息 
	 * @param organizeId:
	 * 根分类Id
	 */
	public ArrayList getOrganizeAsTree(Integer organizeId){
		ArrayList arrayOrganize = new ArrayList();

		OrganizeInfor organize = (OrganizeInfor) this.organizeInforDAO.get(organizeId);
		if (organize != null) {
			arrayOrganize.add(organize);
			
			//get sub depart
			addSubOrganizeToArray(arrayOrganize, organize);
		}

		return arrayOrganize;
	}
	
	private void addSubOrganizeToArray(ArrayList array, OrganizeInfor organize) {
		List<OrganizeInfor> childs = new ArrayList<OrganizeInfor>(organize.getChilds());
		//按照orderNo排序
		//Collections.sort(childs, new BeanComparator("orderNo"));
		Iterator it = childs.iterator();

		while (it.hasNext()) {
			OrganizeInfor subOrganize = (OrganizeInfor) it.next();
			array.add(subOrganize);
			
			addSubOrganizeToArray(array, subOrganize);
		}
	}
	
	//获取指定用户的部门信息
	public List getDepFromUsers(List users) {
		
		List returnDeps = new ArrayList();
		
		if (users != null && users.size() > 0) {
			for (Iterator it=users.iterator();it.hasNext();) {
				SystemUserInfor user = (SystemUserInfor)it.next();
				if (user.getPerson().getDepartment() != null) {
					if (!returnDeps.contains(user.getPerson().getDepartment())) {
						returnDeps.add(user.getPerson().getDepartment());
					}
				}
			}
		}
		
		return returnDeps;
	}

	//根据名称获取部门信息
	public OrganizeInfor findByOrganizeName(String organizeName){
		//OrganizeInfor organizeInfor = new OrganizeInfor();
		String queryString = "from OrganizeInfor department where department.organizeName = '" + organizeName + "'";
		List list = this.organizeInforDAO.getResultByQueryString(queryString);

		if (list != null && list.size() > 0 && list.get(0) != null) {
			return (OrganizeInfor) list.get(0);
		} else {
			return null;
		}
		//return organizeInfor;
	}

}
