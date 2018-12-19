package com.kwchina.oa.customer.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.oa.customer.entity.ProjectRight;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectCategoryRight;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.service.ProjectCategoryManager;
import com.kwchina.oa.customer.service.ProjectCategoryRightManager;
import com.kwchina.oa.customer.service.ProjectInforManager;
import com.kwchina.oa.customer.vo.ProjectCategoryVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/projectCategory.do")
public class ProjectCategoryController extends BasicController {

	private static Log log = LogFactory.getLog(ProjectInforController.class);

	@Resource
	private ProjectCategoryRightManager projectCategoryRightManager;

	@Resource
	private ProjectCategoryManager projectCategoryManager;

	@Autowired
	private SystemUserManager systemUserManager;

	@Resource
	private OrganizeManager organizeManager;

	@Resource
	private ProjectInforManager projectInforManager;

	/**
	 * 新增或者修改信息
	 * 
	 * @param request
	 * @param response
	 * @param model
	 * @param vo
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("projectCategoryVo")
	ProjectCategoryVo projectCategoryVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectCategoryAction.edit' method...");
		}

		// 获取用户信息
		List users = this.systemUserManager.getAll();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer categoryId = projectCategoryVo.getCategoryId();
		ProjectCategory projectCategory = new ProjectCategory();
		if (categoryId != null && categoryId != 0) {
			
			//由于防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都是加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			List list = this.projectInforManager.getResultByQueryString(hql);
			int maxProjectInforId = 0;
			if (list.size() != 0) {
				maxProjectInforId = Integer.parseInt(list.get(0).toString());
			}
			categoryId -= maxProjectInforId;
			projectCategory = (ProjectCategory) this.projectCategoryManager.get(categoryId);
			projectCategoryVo.setSetRight(0);  //默认可以设置权限

			// 判断当前用户是否有权限修改分类信息
			// 首先判断用户是否为项目添加者
			if (!(systemUser.getPersonId() == projectCategory.getCreator().getPersonId()||systemUser.getUserType() == SystemUserInfor._Type_Admin)) {
				projectCategoryVo.setSetRight(1);  //如果不是分类的创建者，则无权进行权限的设置
				if(!this.projectCategoryManager.hasRight(projectCategory, systemUser, ProjectCategoryRight._Right_Edit)){
					request.setAttribute("_ErrorMessage", "对不起,您无权对分类进行编辑,请联系管理员!");
					return "/common/error";
				}
			}
			
			BeanUtils.copyProperties(projectCategoryVo, projectCategory);
			projectCategoryVo.setProjectId(projectCategory.getProjectInfor().getProjectId());
			projectCategoryVo.setCreatorId(projectCategory.getCreator().getPersonId());
		}else {
			// projectId为空，则为新增信息,添加创建者
			projectCategoryVo.setCreatorId(systemUser.getPersonId());
		}

		// 创建时间
		String createTime = new Date(System.currentTimeMillis()).toString();

		// 分类权限
		int[] createIds = new int[users.size()]; // 创建项目分类权限
		int[] deleteIds = new int[users.size()]; // 删除权限
		int[] editIds = new int[users.size()]; // 编辑项目权限
		if (projectCategory != null) {
			int k = 0;
			Set rights = projectCategory.getCategoryRights();
			for (Iterator it = rights.iterator(); it.hasNext();) {
				ProjectCategoryRight right = (ProjectCategoryRight) it.next();
				int userId = right.getUser().getPersonId().intValue();

				// 创建项目分类权限
				if (this.projectCategoryRightManager.hasRight(right, ProjectCategoryRight._Right_Create)) {
					createIds[k] = userId;
				}

				// 删除权限
				if (this.projectCategoryRightManager.hasRight(right, ProjectCategoryRight._Right_Delete)) {
					deleteIds[k] = userId;
				}

				// 项目修改权限
				if (this.projectCategoryRightManager.hasRight(right, ProjectCategoryRight._Right_Edit)) {
					editIds[k] = userId;
				}

				k += 1;
			}
		}
		request.setAttribute("_CreateIds", createIds);
		request.setAttribute("_DeleteIds", deleteIds);
		request.setAttribute("_EditIds", editIds);

		// 所有项目
		List allProjectInfor = this.projectInforManager.getAll();
		request.setAttribute("_AllProjectInfors", allProjectInfor);

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);

		// 获取部门信息
		List organizes = this.organizeManager.getDepartments();
		request.setAttribute("_Organizes", organizes);

		request.setAttribute("_CreateTime", createTime);
		request.setAttribute("_Users", users);
		return "editProjectCategory";

	}

	/***************************************************************************
	 * 保存分类信息
	 * 
	 * @param request
	 * @param response
	 * @param projectInforInforVo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, ProjectCategoryVo projectCategoryVo, Model model) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer categoryId = projectCategoryVo.getCategoryId();
		ProjectCategory projectCategory = new ProjectCategory();
		if (categoryId != null && categoryId.intValue() != 0) {
			projectCategory = (ProjectCategory) this.projectCategoryManager.get(categoryId);
		}else{
			projectCategory.setCreator(systemUser);
		}
		BeanUtils.copyProperties(projectCategory, projectCategoryVo);

		// 添加所属项目
		ProjectInfor projectInfor = new ProjectInfor();
		Integer projectId = projectCategoryVo.getProjectId();
		if (projectId != null && projectId.intValue() != 0) {
			projectInfor = (ProjectInfor) this.projectInforManager.get(projectId);
			if(!this.projectInforManager.hasRight(projectInfor, systemUser, ProjectRight._Right_Create)){
				request.setAttribute("_Message", "对不起,您没有该项目的分类添加权限,请与管理员联系!");
				return "";
			}
			projectCategory.setProjectInfor(projectInfor);
		} 
		
		this.projectCategoryManager.saveProjectCategroy(projectCategory, projectCategoryVo);
		request.setAttribute("_Message", "分类添加成功!");
		return "";
	}

	/***************************************************************************
	 * 修改分类名称
	 * 
	 * @param request
	 * @param response
	 * @param projectInforInforVo
	 * @throws Exception
	 */
	@RequestMapping(params = "method=rename")
	public void save(HttpServletRequest request, HttpServletResponse response, ProjectCategoryVo projectCategoryVo) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		ProjectCategory projectCategory = new ProjectCategory();
		Integer categoryId = projectCategoryVo.getCategoryId();
		if (categoryId != null && categoryId != 0) {
			
			//由于防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都是加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			List list = this.projectInforManager.getResultByQueryString(hql);
			int maxProjectInforId = 0;
			if (list.size() != 0) {
				maxProjectInforId = Integer.parseInt(list.get(0).toString());
			}
			categoryId -= maxProjectInforId;
			projectCategory = (ProjectCategory) this.projectCategoryManager.get(categoryId.intValue());
			projectCategory.setCategoryName(request.getParameter("categoryName"));
		}
		
		//判断用户是否有修改项目的权利
		if ((systemUser.getPersonId() == projectCategory.getCreator().getPersonId())||this.projectCategoryManager.hasRight(projectCategory, systemUser, ProjectCategoryRight._Right_Edit)||systemUser.getUserType() == SystemUserInfor._Type_Admin) {
			this.projectCategoryManager.save(projectCategory);
		}
	}

	/***************************************************************************
	 * 删除指定分类
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteCategory")
	public void deleteCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer categoryId = Integer.valueOf(request.getParameter("categoryId"));
		// 判断用户是否有删除项目的权限

		if (categoryId != null && categoryId.intValue() > 0) {
			//由于防止树形图中项目和分类的主键冲突，树形结构中的分类的Id都是加上项目信息的最大Id的，所以作如下处理
			String hql = "select max(infor.projectId) from ProjectInfor infor";
			List list = this.projectInforManager.getResultByQueryString(hql);
			int maxProjectInforId = 0;
			if (list.size() != 0) {
				maxProjectInforId = Integer.parseInt(list.get(0).toString());
			}
			categoryId -= maxProjectInforId;
			ProjectCategory projectCategory = (ProjectCategory) this.projectCategoryManager.get(categoryId.intValue());
			if(this.projectCategoryManager.hasRight(projectCategory, systemUser, ProjectCategoryRight._Right_Delete)||projectCategory.getCreator().getPersonId()==systemUser.getPersonId()){
				this.projectCategoryManager.remove(projectCategory);
			}
		}
	}

}
