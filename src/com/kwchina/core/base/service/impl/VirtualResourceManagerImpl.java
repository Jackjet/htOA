package com.kwchina.core.base.service.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.dao.VirtualResourceDAO;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.VirtualResourceManager;
import com.kwchina.core.common.service.BasicManagerImpl;

@Service
public class VirtualResourceManagerImpl extends BasicManagerImpl<SystemUserInfor> implements VirtualResourceManager {

	private VirtualResourceDAO virtualResourceDAO;

	@Autowired
	public void setVirtualResourceDAO(VirtualResourceDAO virtualResourceDAO) {
		this.virtualResourceDAO = virtualResourceDAO;
		super.setDao(virtualResourceDAO);
	}

	public void save(VirtualResource resource) {
		Integer resourceId = resource.getResourceId();
		// if(resourceId==null || resourceId.intValue()==0){
		// 新增，设置其layer
		int layer = 1;

		VirtualResource parent = resource.getParent();
		if (parent != null) {
			layer = parent.getLayer() + 1;
		}

		// 设置其层级
		resource.setLayer(layer);
		// }

		// 保存
		this.virtualResourceDAO.save(resource);
	}
	
	//根据别名获取
	public VirtualResource  getResourceByAlias(String alias){
		VirtualResource resource = null;

		if (alias != null && !alias.equals("")) {
			List ls = this.getAll();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				VirtualResource tpResource = (VirtualResource) it.next();

				// 路径全部转化为小写
				if(tpResource.getAliasName()!=null){
					String aliasName = tpResource.getAliasName().toLowerCase();
					String aName = alias.toLowerCase();
	
					if(aliasName.equals(aName)){
						return tpResource;					
					}				
				}
			}
		}

		return resource;
	}

	//根据资源路径获取资源
	public VirtualResource getVirtualResource(String resourcePath) {
		VirtualResource resource = null;

		if (resourcePath != null && !resourcePath.equals("")) {
			List ls = this.getAll();
			for (Iterator it = ls.iterator(); it.hasNext();) {
				VirtualResource tpResource = (VirtualResource) it.next();

				// 路径全部转化为小写
				String path = tpResource.getResourcePath().toLowerCase();
				String rPath = resourcePath.toLowerCase();

				if(path.indexOf(rPath)>=0){
					return tpResource;
				}
				
				/**
				if (path.equals(resourcePath)) {
					return tpResource;
				}
				*/
			}
		}

		return resource;
	}

	/**
	 * 按照树状结构组织Resource信息
	 * 
	 * @param resourceId:根分类Id
	 */
	public ArrayList getResourceAsTree(Integer resourceId) {
		ArrayList arrayResource = new ArrayList();

		if (resourceId == null || resourceId.intValue() == 0) {
			// 取得顶层resource
			List allResources = this.getAll();
			for (Iterator it = allResources.iterator(); it.hasNext();) {
				VirtualResource tempResource = (VirtualResource) it.next();
				if (tempResource.getParent() == null) {
					arrayResource.add(tempResource);

					// get sub
					addSubResourceToArray(arrayResource, tempResource);
				}
			}
		} else {
			VirtualResource resource = (VirtualResource) this.virtualResourceDAO.get(resourceId);
			if (resource != null) {
				arrayResource.add(resource);

				// get sub
				addSubResourceToArray(arrayResource, resource);
			}
		}

		return arrayResource;
	}

	private void addSubResourceToArray(ArrayList array, VirtualResource resource) {
		/*List<VirtualResource> childs = new ArrayList<VirtualResource>(resource.getChilds());
		// 按照orderNo排序
		Collections.sort(childs, new BeanComparator("orderNo"));*/
		List<VirtualResource> childs = this.getActiveChilds(resource.getResourceId());
		Iterator it = childs.iterator();

		while(it.hasNext()){
			VirtualResource subResource = (VirtualResource) it.next();
			array.add(subResource);
			addSubResourceToArray(array, subResource);
		}
	}
	
	//获取所有属于OA的系统资源信息
	public List getAll () {
		String hql = "from VirtualResource virtualResource where virtualResource.virResourceType = 0 and virtualResource.hided = 0";
		return this.getResultByQueryString(hql);
	}
	
	//获取指定信息的子系统资源信息
	public List getActiveChilds(int parentId) {
		String hql = "from VirtualResource virtualResource where virtualResource.hided = 0 and virtualResource.parent.resourceId = " + parentId + " order by orderNo";
		return this.getResultByQueryString(hql);
	}
}
