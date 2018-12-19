package com.kwchina.oa.document.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.entity.DocumentCategoryRight;
import com.kwchina.oa.document.service.CategoryRightManager;
import com.kwchina.oa.document.service.DocumentCategoryManager;
import com.kwchina.oa.document.service.DocumentInforManager;
import com.kwchina.oa.document.util.DocumentConstant;
import com.kwchina.oa.document.vo.DocumentCategoryVo;

@Controller
@RequestMapping("/document/documentCategory.do")
public class DocumentCategoryController {

	@Resource
	private DocumentCategoryManager documentCategoryManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private CategoryRightManager documentCategoryRightManager;

	@Resource
	private OrganizeManager organizeManager;

	@Resource
	private DocumentInforManager documentInforManager;


	/*//列表供维护
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String parentId = request.getParameter("parentId");
		List returnLs = new ArrayList();
		
		if (parentId != null && parentId.length() > 0) {
			DocumentCategory documentCategory = (DocumentCategory) this.documentCategoryManager.get(Integer.valueOf(parentId));
			returnLs.addAll(documentCategory.getChilds());
		} else {
			returnLs = this.documentCategoryManager.getAll();
		}

		request.setAttribute("_Categorys", returnLs);

		return "document/list";
	}*/

	//浏览文档分类
	@RequestMapping(params = "method=list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
		request.setAttribute("_TREE", returnArray);
		
		//getLeftCategory(form, request, 1);
		
		return "listCategory";
	}


	/*private void getLeftCategory(ActionForm form, HttpServletRequest request, int categoryType) {
		//当前用户
		SystemUserInfor systemUser = (SystemUserInfor) request.getSession().getAttribute("_SYSTEM_USER");
		
		//全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
		request.setAttribute("_TREE", returnArray);

		//当前分类
		DocumentCategoryForm categoryForm = (DocumentCategoryForm) form;
		Integer categoryId = categoryForm.getCategoryId();
		if (categoryId != null && categoryId.intValue() != 0) {
			DocumentCategory category = (DocumentCategory) this.documentCategoryManager.get(categoryId.intValue());
			request.setAttribute("_Category", category);
		}
	}*/

	//编辑
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, DocumentCategoryVo vo) throws Exception {

		Integer categoryId = vo.getCategoryId();

		//获取用户信息
		List users = this.systemUserManager.getAll();
		
		DocumentCategory category = null;
		//修改
		if (categoryId != null && categoryId.intValue() > 0) {
			category = (DocumentCategory) this.documentCategoryManager.get(categoryId);

			BeanUtils.copyProperties(vo, category);
			
			//父分类
			if (category.getParent() != null) {
				vo.setParentId(category.getParent().getCategoryId().intValue());
			}
			//是否叶子分类
			if (category.isLeaf())
				vo.setLeaf(true);
			else
				vo.setLeaf(false);

			//分类权限
			int[] createIds = new int[users.size()];
			int[] deleteIds = new int[users.size()];
			int[] viewIds = new int[users.size()];

			int k = 0;
			Set rights = category.getRights();
			for (Iterator it = rights.iterator(); it.hasNext();) {
				DocumentCategoryRight right = (DocumentCategoryRight) it.next();
				int userId = right.getUser().getPersonId().intValue();

				//创建权限
				if (this.documentCategoryRightManager.hasRight(right, DocumentCategoryRight._Right_Create)) {
					createIds[k] = userId;
				}

				//删除权限
				if (this.documentCategoryRightManager.hasRight(right, DocumentCategoryRight._Right_Delete)) {
					deleteIds[k] = userId;
				}

				//浏览权限
				if (this.documentCategoryRightManager.hasRight(right, DocumentCategoryRight._Right_View)) {
					viewIds[k] = userId;
				}

				k += 1;
			}

			request.setAttribute("_CreateIds", createIds);
			request.setAttribute("_DeleteIds", deleteIds);
			request.setAttribute("_ViewIds", viewIds);

		}

		//全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.documentCategoryManager.getCategoryAsTree(DocumentConstant._Root_Category_Id);
		request.setAttribute("_TREE", returnArray);
		
		//根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
		request.setAttribute("_Persons", persons);
		
		//获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
		request.setAttribute("_OtherPersons", otherPersons);

		//获取部门信息
		List organizes = this.organizeManager.getDepartments();
		request.setAttribute("_Organizes", organizes);
		request.setAttribute("_Users", users);
		request.setAttribute("_Category", category);

		return "editCategory";
	}

	//保存
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, DocumentCategoryVo vo) throws Exception {

		DocumentCategory category = new DocumentCategory();
		Integer categoryId = vo.getCategoryId();

		if (categoryId != null && categoryId.intValue() != 0) {
			category = (DocumentCategory) this.documentCategoryManager.get(categoryId.intValue());
		}
		if (vo.getParentId() != 0) {
			DocumentCategory parent = (DocumentCategory) this.documentCategoryManager.get(vo.getParentId());
			category.setParent(parent);
			vo.setLayer(parent.getLayer()+1);
		}
		//是否叶分类
		category.setLeaf(vo.isLeaf());
		
		BeanUtils.copyProperties(category, vo);
		
		this.documentCategoryManager.saveCategory(category, vo);

	}

	//删除分类操作
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, DocumentCategoryVo vo) throws Exception {
		
		Integer categoryId = vo.getCategoryId();
		if (categoryId != null && categoryId.intValue() > 0) {
			DocumentCategory category = (DocumentCategory)this.documentCategoryManager.get(categoryId);
			deleteChildren(category);
			
		}
	}
	private void deleteChildren(DocumentCategory category){
		Set childs = category.getChilds();
		
		if(childs!=null && childs.size()>0){
			List tmpList = new ArrayList(childs);
			for(Iterator it = tmpList.iterator();it.hasNext();){
				DocumentCategory tpS = (DocumentCategory)it.next();
				
				//从父对象移除
				childs.remove(tpS);
				
				deleteChildren(tpS);
			}			
		}
		this.documentCategoryManager.remove(category);
	}
	
}
