package com.kwchina.core.cms.controller;

import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.CategoryRoleRight;
import com.kwchina.core.cms.entity.CategoryUserRight;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.service.InforCategoryRightManager;
import com.kwchina.core.cms.util.InforConstant;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;

@Controller
@RequestMapping("/cms/inforCategory.do")
public class InforCategoryController {

	@Resource
	private InforCategoryManager inforCategoryManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private InforCategoryRightManager inforCategoryRightManager;


	//浏览咨询分类
	@RequestMapping(params = "method=list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//全部分类树状显示
		ArrayList returnArray = this.inforCategoryManager.getCategoryAsTree(CoreConstant.InforCategory_Begin_Id);
		
		//清除系统固定分类
		List tmpArray = new ArrayList(returnArray);
		for (Iterator it=tmpArray.iterator();it.hasNext();) {
			InforCategory category = (InforCategory)it.next();
			if (category.getCategoryId() == InforConstant.Cms_Category_Annouce ||
					category.getCategoryId() == InforConstant.Cms_Category_Companynews ||
					category.getCategoryId() == InforConstant.Cms_Category_Important) {
				returnArray.remove(category);
			}
		}
		
		request.setAttribute("_TREE", returnArray);
		
		return "listInforCategory";
	}


	//编辑
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String categoryId = request.getParameter("categoryId");
		
		//修改
		if (categoryId != null && categoryId.length() > 0) {
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			request.setAttribute("_Category", category);
			
			//内容模板
			if (category.getContentTemplate() != null && category.getContentTemplate().length() > 0) {
				String[] templateArray = category.getContentTemplate().split("/");
				String templateName = templateArray[templateArray.length-1];
				request.setAttribute("_TemplateName", templateName);
			}
		}
		
		//所有分类信息
		List treeCategorys = this.inforCategoryManager.getCategoryAsTree(CoreConstant.InforCategory_Begin_Id);
		request.setAttribute("_TreeCategorys", treeCategorys);
		
		return "editInforCategory";
	}

	//保存
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String categoryId = request.getParameter("categoryId");
		String categoryName = request.getParameter("categoryName");
		String orderNo = request.getParameter("orderNo");
		String deleted = request.getParameter("deleted");
		String leaf = request.getParameter("leaf");
		String listTemplate = request.getParameter("listTemplate");
		String contentTemplate = request.getParameter("contentTemplate");
		String parentId = request.getParameter("parentId");
		String inherit = request.getParameter("inherit");
		String urlPath = request.getParameter("urlPath");
		String attchmentPath = InforConstant.Cms_Attachment_Path + urlPath;
		if (request.getParameter("attchmentPath") != null && request.getParameter("attchmentPath").length() > 0) {
			attchmentPath = request.getParameter("attchmentPath");
		}
		InforCategory category = new InforCategory();
		InforCategory parent = null;
		
		if (categoryId != null && categoryId.length() > 0) {
			//修改
			category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));		
		}
		category.setCategoryName(categoryName);
		if (orderNo != null && orderNo.length() > 0) {
			category.setOrderNo(Integer.valueOf(orderNo));
		}
		category.setDeleted(Boolean.valueOf(deleted));
		category.setLeaf(Boolean.valueOf(leaf));
		category.setListTemplate(listTemplate);
		if(parentId != null && parentId.length() > 0){
			parent = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(parentId));
			category.setParent(parent);
		}
		category.setAttchmentPath(attchmentPath);
		category.setPagePath(urlPath);
		category.setUrlPath(urlPath);
		category.setInherit(Boolean.valueOf(inherit));
		
		//保存该分类下需要显示的字段情况
		String[] fieldNames = request.getParameterValues("fieldName");			//字段名
		String[] displayTitles = request.getParameterValues("displayTitle");	//显示标题
		String[] displayeds = request.getParameterValues("displayed");			//添加时是否显示
		String[] displayOrders = request.getParameterValues("displayOrder");	//添加时的排序
		String[] listDisplayeds = request.getParameterValues("listDisplayed");	//列表里是否显示
		String[] listOrders = request.getParameterValues("listOrder");			//列表里的排序
		
		Set fields = category.getFields();
		category.getFields().removeAll(fields);
		for (int i=0;i<fieldNames.length;i++) {
			InforField field = new InforField();
			field.setCategory(category);
			field.setDisplayTitle(displayTitles[i]);
			field.setFieldName(fieldNames[i]);
			field.setDisplayOrder(Integer.valueOf(displayOrders[i]));
			field.setListOrder(Integer.valueOf(listOrders[i]));
			//判断添加时是否显示该字段
			if (displayeds != null && displayeds.length > 0) {
				for (int j=0;j<displayeds.length;j++) {
					if (Integer.valueOf(displayeds[j])-1 == i) {
						field.setDisplayed(true);
						break;
					}
				}
			}else {
				field.setDisplayed(false);
			}
			//判断列表里是否显示该字段
			if (listDisplayeds != null && listDisplayeds.length > 0) {
				for (int j=0;j<listDisplayeds.length;j++) {
					if (Integer.valueOf(listDisplayeds[j])-1 == i) {
						field.setListDisplayed(true);
						break;
					}
				}
			}else {
				field.setListDisplayed(false);
			}
			category.getFields().add(field);
		}
		
		//保存内容模板路径
		if (contentTemplate != null && contentTemplate.length() > 0) {
			category.setContentTemplate(InforConstant.Cms_Template_Path + contentTemplate);
		}
		
		this.inforCategoryManager.save(category);
	}
	
	//获取父类的相关信息(模板,字段,附件路径)
	@RequestMapping(params="method=getParentInfor")
	public void getParentInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String parentId = request.getParameter("parentId");
		
		//父类信息
		if (parentId != null && parentId.length() > 0) {
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			JSONArray fieldArray = new JSONArray();
			
			InforCategory parent = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(parentId));
			InforCategory tmpObj = new InforCategory();
			tmpObj.setContentTemplate(parent.getContentTemplate());
			tmpObj.setAttchmentPath(parent.getAttchmentPath());
			jsonObj.put("_Parent", tmpObj);
			
			Set fields = parent.getFields();
			fieldArray = convert.modelCollect2JSONArray(fields, new ArrayList());
			jsonObj.put("_Fields", fieldArray);
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}
        
	}

	//删除分类操作
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String categoryId = request.getParameter("categoryId");
		if (categoryId != null && categoryId.length() > 0) {
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			deleteChildren(category);
			
		}
	}
	private void deleteChildren(InforCategory category){
		Set childs = category.getChilds();
		
		if(childs!=null && childs.size()>0){
			List tmpList = new ArrayList(childs);
			for(Iterator it = tmpList.iterator();it.hasNext();){
				InforCategory tpS = (InforCategory)it.next();
				
				//从父对象移除
				childs.remove(tpS);
				
				deleteChildren(tpS);
			}			
		}
		this.inforCategoryManager.remove(category);
	}
	
	//编辑分类权限
	@RequestMapping(params = "method=editCategoryRight")
	public String editCategoryRight(HttpServletRequest request, HttpServletResponse response) {
		
		String categoryId = request.getParameter("categoryId");
		if (categoryId != null && categoryId.length() > 0) {
			InforCategory category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			request.setAttribute("_Category", category);
			
			//根据职级获取用户
			List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
			request.setAttribute("_Persons", persons);
			
			//获取职级大于一定值的用户
			List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
			request.setAttribute("_OtherPersons", otherPersons);

			//获取部门信息
			List organizes = this.organizeManager.getDepartments();
			request.setAttribute("_Organizes", organizes);
			
			//获取角色信息
			List roles = this.roleManager.getAll(); 
			request.setAttribute("_Roles", roles);
			
			//获取用户信息
			List users = this.systemUserManager.getAll();
			request.setAttribute("_Users", users);
			
			//权限信息
			int[] editUserIds = new int[users.size()];
			int[] deleteUserIds = new int[users.size()];
			int[] viewUserIds = new int[users.size()];
			int[] editRoleIds = new int[roles.size()];
			int[] deleteRoleIds = new int[roles.size()];
			int[] viewRoleIds = new int[roles.size()];
			int rightType = 0;

			int k = 0;
			Set rights = category.getRights();
			for (Iterator it = rights.iterator(); it.hasNext();) {
				InforCategoryRight right = (InforCategoryRight) it.next();
				if (right instanceof CategoryUserRight) {
					rightType = 1;
					/** 用户权限 */
					CategoryUserRight userRight = (CategoryUserRight)right;
					int userId = userRight.getSystemUser().getPersonId().intValue();
					
					//编辑权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_Edit)) {
						editUserIds[k] = userId;
					}

					//删除权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_Delete)) {
						deleteUserIds[k] = userId;
					}

					//浏览权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_View)) {
						viewUserIds[k] = userId;
					}
				}else if (right instanceof CategoryRoleRight) {
					/** 角色权限 */
					CategoryRoleRight roleRight = (CategoryRoleRight)right;
					int roleId = roleRight.getRole().getRoleId().intValue();

					//编辑权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_Edit)) {
						editRoleIds[k] = roleId;
					}

					//删除权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_Delete)) {
						deleteRoleIds[k] = roleId;
					}

					//浏览权限
					if (this.inforCategoryRightManager.hasRight(right, InforCategoryRight._Right_View)) {
						viewRoleIds[k] = roleId;
					}
				}
				k += 1;
			}

			request.setAttribute("_EditUserIds", editUserIds);
			request.setAttribute("_DeleteUserIds", deleteUserIds);
			request.setAttribute("_ViewUserIds", viewUserIds);
			request.setAttribute("_EditRoleIds", editRoleIds);
			request.setAttribute("_DeleteRoleIds", deleteRoleIds);
			request.setAttribute("_ViewRoleIds", viewRoleIds);
			request.setAttribute("_RightType", rightType);
		}
		
		return "editInforCategoryRight";
	}
	
	//保存分类权限
	@RequestMapping(params = "method=saveCategoryRight")
	public void saveCategoryRight(HttpServletRequest request, HttpServletResponse response) {
		
		String categoryId = request.getParameter("categoryId");
		String rightType = request.getParameter("rightType");
		InforCategory category = new InforCategory();
		
		if (categoryId != null && categoryId.length() > 0) {
			category = (InforCategory)this.inforCategoryManager.get(Integer.valueOf(categoryId));
			
			Set rights = category.getRights();
			category.getRights().removeAll(rights);
			
			if (rightType.equals("0")) {
				/** 为角色权限时 */
				String[] editRoleIds = request.getParameterValues("editRoleIds");
				String[] deleteRoleIds = request.getParameterValues("deleteRoleIds");
				String[] viewRoleIds = request.getParameterValues("viewRoleIds");
				
				//新增,修改
				if (editRoleIds != null && editRoleIds.length > 0) {
					for (int i=0;i<editRoleIds.length;i++) {
						RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(editRoleIds[i]));
						CategoryRoleRight roleRight = new CategoryRoleRight();
						roleRight.setCategory(category);
						roleRight.setOperateRight(1);
						roleRight.setRole(role);
						category.getRights().add(roleRight);
					}
				}
				//删除
				if (deleteRoleIds != null && deleteRoleIds.length > 0) {
					Set oldRights = category.getRights();
					for (int i=0;i<deleteRoleIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							CategoryRoleRight roleRight = (CategoryRoleRight)it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(deleteRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 2);
								
								has = true;
								break;
							}
						}
						
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(deleteRoleIds[i]));
							CategoryRoleRight roleRight = new CategoryRoleRight();
							roleRight.setCategory(category);
							roleRight.setOperateRight(2);
							roleRight.setRole(role);
							category.getRights().add(roleRight);
						}
					}
				}
				//浏览
				if (viewRoleIds != null && viewRoleIds.length > 0) {
					Set oldRights = category.getRights();
					for (int i=0;i<viewRoleIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							CategoryRoleRight roleRight = (CategoryRoleRight)it.next();
							if (roleRight.getRole().getRoleId().intValue() == Integer.valueOf(viewRoleIds[i])) {
								roleRight.setOperateRight(roleRight.getOperateRight() + 4);
								
								has = true;
								break;
							}
						}
						
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							RoleInfor role = (RoleInfor)this.roleManager.get(Integer.valueOf(viewRoleIds[i]));
							CategoryRoleRight roleRight = new CategoryRoleRight();
							roleRight.setCategory(category);
							roleRight.setOperateRight(4);
							roleRight.setRole(role);
							category.getRights().add(roleRight);
						}
					}
				}
			}else {
				/** 为用户权限时 */
				String[] editUserIds = request.getParameterValues("editUserIds");
				String[] deleteUserIds = request.getParameterValues("deleteUserIds");
				String[] viewUserIds = request.getParameterValues("viewUserIds");
				
				//新增,修改
				if (editUserIds != null && editUserIds.length > 0) {
					for (int i=0;i<editUserIds.length;i++) {
						SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(editUserIds[i]));
						CategoryUserRight userRight = new CategoryUserRight();
						userRight.setCategory(category);
						userRight.setOperateRight(1);
						userRight.setSystemUser(user);
						category.getRights().add(userRight);
					}
				}
				//删除
				if (deleteUserIds != null && deleteUserIds.length > 0) {
					Set oldRights = category.getRights();
					for (int i=0;i<deleteUserIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							CategoryUserRight userRight = (CategoryUserRight)it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(deleteUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 2);
								
								has = true;
								break;
							}
						}
						
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(deleteUserIds[i]));
							CategoryUserRight userRight = new CategoryUserRight();
							userRight.setCategory(category);
							userRight.setOperateRight(2);
							userRight.setSystemUser(user);
							category.getRights().add(userRight);
						}
					}
				}
				//浏览
				if (viewUserIds != null && viewUserIds.length > 0) {
					Set oldRights = category.getRights();
					for (int i=0;i<viewUserIds.length;i++) {
						boolean has = false;
						for (Iterator it=oldRights.iterator();it.hasNext();) {
							//假如前面的权限操作中已包含该角色权限,则需要在此基础上进行修改
							CategoryUserRight userRight = (CategoryUserRight)it.next();
							if (userRight.getSystemUser().getPersonId().intValue() == Integer.valueOf(viewUserIds[i])) {
								userRight.setOperateRight(userRight.getOperateRight() + 4);
								
								has = true;
								break;
							}
						}
						
						if (!has) {
							//假如前面的权限操作中不包含该角色权限,则创建该权限
							SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(Integer.valueOf(viewUserIds[i]));
							CategoryUserRight userRight = new CategoryUserRight();
							userRight.setCategory(category);
							userRight.setOperateRight(4);
							userRight.setSystemUser(user);
							category.getRights().add(userRight);
						}
					}
				}
				
			}
			this.inforCategoryManager.save(category);
		}
	}
	
	
	//获取所有内容模板
	@RequestMapping(params = "method=getCmsTemplates")
	public void getCmsTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONArray fileArray = new JSONArray();
		File file = new File(CoreConstant.Context_Real_Path + InforConstant.Cms_Template_Path);
		
		File[] childs = file.listFiles();
		for(int i=0; i<childs.length; i++) {
			//仅显示ftl文件
			if (childs[i].getName().endsWith(".ftl")) {
				JSONObject obj = new JSONObject();
				obj.put("templateName", childs[i].getName());
	            fileArray.add(obj);
			}
        }
		jsonObj.put("templates", fileArray);
		
		//设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	
}
