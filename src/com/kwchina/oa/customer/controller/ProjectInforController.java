package com.kwchina.oa.customer.controller;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

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
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.customer.entity.ProjectCategory;
import com.kwchina.oa.customer.entity.ProjectInfor;
import com.kwchina.oa.customer.entity.ProjectRight;
import com.kwchina.oa.customer.service.ProjectCategoryManager;
import com.kwchina.oa.customer.service.ProjectCategoryRightManager;
import com.kwchina.oa.customer.service.ProjectInforManager;
import com.kwchina.oa.customer.service.ProjectRightManager;
import com.kwchina.oa.customer.vo.ProjectInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/projectInfor.do")
public class ProjectInforController extends BasicController {

	private static Log log = LogFactory.getLog(ProjectInforController.class);

	@Resource
	private ProjectInforManager projectInforManager;

	@Resource
	private ProjectRightManager projectRightManager;

	@Resource
	private ProjectCategoryManager projectCategoryManager;

	@Resource
	private ProjectCategoryRightManager projectCategoryRightManager;

	@Autowired
	private SystemUserManager systemUserManager;

	@Resource
	private OrganizeManager organizeManager;

	/**
	 * 按照树状结构获取项目和项目分类信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void listTree(@ModelAttribute("projectInforVo")
	ProjectInforVo projectInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.listTree' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);

		// 获取该用户有权限的所有项目信息
		List projectInforList = this.projectInforManager.getProjectInfor(systemUser);

		int sumOfSubProjectInfor = 0;
		for (int i = 0; i < projectInforList.size(); i++) {
			ProjectInfor projectInfor = (ProjectInfor) projectInforList.get(i);
			sumOfSubProjectInfor += 2 * (projectInfor.getProjectCategorys().size() + 1);
		}
		// 将set集置空
		Iterator projectInforIt = projectInforList.iterator();
		List projectInforVos = new ArrayList();

		// 首先设置根节点
		ProjectInforVo projectInforVoBase = new ProjectInforVo();
		projectInforVoBase.setProjectName("全部项目");
		projectInforVoBase.setProjectId(0);
		projectInforVoBase.setLayer(0);
		projectInforVoBase.setLeaf(false);
		projectInforVoBase.setLeftIndex(1);
		projectInforVoBase.setRightIndex(sumOfSubProjectInfor + 2);
		projectInforVos.add(projectInforVoBase);
		int leftIndex = 2;
		int rightIndex = 3;
		while (projectInforIt.hasNext()) {
			ProjectInfor projectInfor = (ProjectInfor) projectInforIt.next();
			ProjectInforVo projectInforVoParent = this.projectInforManager.transPOToVO(projectInfor, null, 0, leftIndex, leftIndex + projectInfor.getProjectCategorys().size() * 2 + 1);
			projectInforVos.add(projectInforVoParent);
			if (projectInfor.getProjectCategorys().size() != 0) {
				List projectCategoryList = new ArrayList(projectInfor.getProjectCategorys());
				Iterator projectCategoryIt = projectCategoryList.iterator();
				while (projectCategoryIt.hasNext()) {
					ProjectCategory projectCategory = (ProjectCategory) projectCategoryIt.next();
					ProjectInforVo projectInforVoChild = this.projectInforManager.transPOToVO(null, projectCategory, 1, leftIndex, rightIndex);
					projectInforVos.add(projectInforVoChild);
					rightIndex += 2;
				}
			}
			leftIndex = rightIndex + 1;
			rightIndex += 2;
		}

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();

		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(projectInforVos, new ArrayList());
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	/***************************************************************************
	 * 查询项目信息列表
	 * 
	 * @param projectInforVo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listProject")
	public void listProject(@ModelAttribute("projectInforVo")
	ProjectInforVo projectInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.listProject' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int userId = systemUser.getPersonId().intValue();

		// 构造查询语句
		String[] queryString = new String[2];
		queryString[0] = " from ProjectInfor projectInfor where 1=1";
		queryString[1] = " select count(projectInfor.projectId) from ProjectInfor projectInfor where 1=1";
		String condition = "";

		// 判断该用户的权限

		// 判断是否为项目的创建者或项目经理
		condition += " and (projectInfor.creator.personId='" + userId + "' or projectInfor.manager.personId='" + userId + "'";

		// 判断是否有项目权限
		condition += " or (projectInfor.projectId in (select  projectRight.projectInfor.projectId from ProjectRight projectRight where projectRight.user.personId = '" + userId + "'))";

		// 判断是否有分类权限
		condition += " or (projectInfor.projectId in (select projectCategorya.projectInfor.projectId from ProjectCategory projectCategorya where projectCategorya.categoryId in (select projectCategoryRight.projectCategory.categoryId from ProjectCategoryRight projectCategoryRight where projectCategoryRight.user.personId = '"+userId+"') ))";

		// 判断是否为任务的审核人、签核人执行人
		condition +=
			" or (projectInfor.projectId in (select projectCategory.projectInfor.projectId from ProjectCategory projectCategory where projectCategory.categoryId in(select taskInfor.projectCategory.categoryId from TaskInfor taskInfor where 1=1 and( taskInfor.checker.personId = '"
				+ userId + "' or taskInfor.signer.personId = '" + userId + "' or taskInfor.taskId in (select executorInfor.taskInfor.taskId from ExecutorInfor executorInfor where executorInfor.executor.personId = '" + userId + "')))))" +
				")))";

		if (systemUser.getUserType() != 1) {
			queryString[0] += condition;
			queryString[1] += condition;
		}

		// 构造查询条件
		queryString = this.projectInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		// 获取该用户有权限的所有项目信息
		PageList pl = this.projectInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List projectInforList = pl.getObjectList();
		int sumOfSubProjectInfor = 0;
		for (int i = 0; i < projectInforList.size(); i++) {
			ProjectInfor projectInfor = (ProjectInfor) projectInforList.get(i);
			sumOfSubProjectInfor += 2 * (projectInfor.getProjectCategorys().size() + 1);
		}
		// 将set集置空
		Iterator projectInforIt = projectInforList.iterator();
		List projectInforVos = new ArrayList();
		while (projectInforIt.hasNext()) {
			ProjectInfor projectInfor = (ProjectInfor) projectInforIt.next();
			ProjectInforVo projectInforVoAdd = this.projectInforManager.transPOToVO(projectInfor);
			projectInforVos.add(projectInforVoAdd);
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();

		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(projectInforVos, new ArrayList());
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

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
	public String edit(@ModelAttribute("projectInforVo")
	ProjectInforVo projectInforVo, HttpServletRequest request, HttpServletResponse response) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.edit' method...");
		}

		// 获取用户信息
		List users = this.systemUserManager.getAll();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		Integer projectId = projectInforVo.getProjectId();
		ProjectInfor projectInfor = new ProjectInfor();
		if (projectId != null && projectId != 0) {
			// projectId不为空，则为修改信息
			projectInfor = (ProjectInfor) this.projectInforManager.get(projectId);
			projectInforVo.setSetRight(0); // 默认有权限设置权限

			// 判断当前用户是否有权限修改项目信息
			// 首先判断用户是否为项目经理活项目添加者
			if (!(systemUser.getPersonId() == projectInfor.getCreator().getPersonId() || systemUser.getPersonId() == projectInfor.getManager().getPersonId() || systemUser.getUserType() == SystemUserInfor._Type_Admin)) {
				projectInforVo.setSetRight(1); // 非项目经理或项目创建者，则无权限
				if (!this.projectInforManager.hasRight(projectInfor, systemUser, ProjectRight._Right_Edit)) {
					request.setAttribute("_ErrorMessage", "对不起,您无权对项目进行修改,请联系管理员!");
					return "/common/error";
				}
			}

			BeanUtils.copyProperties(projectInforVo, projectInfor);
			projectInforVo.setManagerId(projectInfor.getManager().getPersonId());
			projectInforVo.setCreatorId(projectInfor.getCreator().getPersonId());
		} else {
			// projectId为空，则为新增信息,添加创建者
			projectInforVo.setCreatorId(systemUser.getPersonId());
		}

		// 分类权限
		int[] createIds = new int[users.size()]; // 创建项目分类权限
		int[] deleteIds = new int[users.size()]; // 删除权限
		int[] editIds = new int[users.size()]; // 编辑项目权限
		if (projectInfor != null) {
			int k = 0;
			Set rights = projectInfor.getProjectRights();
			for (Iterator it = rights.iterator(); it.hasNext();) {
				ProjectRight right = (ProjectRight) it.next();
				int userId = right.getUser().getPersonId().intValue();

				// 创建项目分类权限
				if (this.projectRightManager.hasRight(right, ProjectRight._Right_Create)) {
					createIds[k] = userId;
				}

				// 删除权限
				if (this.projectRightManager.hasRight(right, ProjectRight._Right_Delete)) {
					deleteIds[k] = userId;
				}

				// 项目修改权限
				if (this.projectRightManager.hasRight(right, ProjectRight._Right_Edit)) {
					editIds[k] = userId;
				}

				k += 1;
			}
		}
		request.setAttribute("_CreateIds", createIds);
		request.setAttribute("_DeleteIds", deleteIds);
		request.setAttribute("_EditIds", editIds);

		// 所有用户
		List allSystemUsers = systemUserManager.findUserOrderByPersonName();
		request.setAttribute("_AllSystemUsers", allSystemUsers);

		// 根据职级获取用户
		List persons = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Persons", persons);

		// 获取职级大于一定值的用户
		List otherPersons = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherPersons", otherPersons);

		// 获取部门信息
		List organizes = this.organizeManager.getDepartments();
		request.setAttribute("_Organizes", organizes);

		request.setAttribute("_Users", users);
		return "editProjectInfor";

	}

	/***************************************************************************
	 * 保存项目信息
	 * 
	 * @param request
	 * @param response
	 * @param projectInforInforVo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, ProjectInforVo projectInforInforVo, Model model) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		ProjectInfor projectInfor = new ProjectInfor();
		Integer projectId = projectInforInforVo.getProjectId();
		if (projectId != null && projectId.intValue() != 0) {
			projectInfor = (ProjectInfor) this.projectInforManager.get(projectId);
		} else {
			// 添加创建人
			SystemUserInfor creator = (SystemUserInfor) this.systemUserManager.get(projectInforInforVo.getCreatorId());
			projectInfor.setCreator(creator);

			// 添加创建时间
			Date nowdate = new Date(System.currentTimeMillis());
			projectInfor.setUpdateDate(nowdate);
		}

		BeanUtils.copyProperties(projectInfor, projectInforInforVo);

		// 添加项目经理
		SystemUserInfor manager = (SystemUserInfor) this.systemUserManager.get(projectInforInforVo.getManagerId());
		projectInfor.setManager(manager);

		this.projectInforManager.saveProject(projectInfor, projectInforInforVo);
	}

	/***************************************************************************
	 * 修改项目名称
	 * 
	 * @param request
	 * @param response
	 * @param projectInforInforVo
	 * @throws Exception
	 */
	@RequestMapping(params = "method=rename")
	public void save(HttpServletRequest request, HttpServletResponse response, ProjectInforVo projectInforInforVo) throws Exception {
		if (log.isDebugEnabled()) {
			log.debug("entering 'ProjectInforAction.save' method...");
		}

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		ProjectInfor projectInfor = new ProjectInfor();
		Integer projectId = projectInforInforVo.getProjectId();
		if (projectId != null && projectId != 0) {
			projectInfor = (ProjectInfor) this.projectInforManager.get(projectId.intValue());
			projectInfor.setProjectName(request.getParameter("projectName"));
		}

		// 判断用户是否有修改项目的权利
		if ((systemUser.getPersonId() == projectInfor.getCreator().getPersonId()) || (systemUser.getPersonId() == projectInfor.getManager().getPersonId()) || this.projectInforManager.hasRight(projectInfor, systemUser, ProjectRight._Right_Edit)
				|| systemUser.getUserType() == SystemUserInfor._Type_Admin) {
			this.projectInforManager.save(projectInfor);
		}
	}

	/***************************************************************************
	 * 删除项目
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteProject")
	public String deleteProjectName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String projectIdStr = request.getParameter("projectId");
		String rowIds = request.getParameter("rowIds");
		if (projectIdStr != null && !projectIdStr.equals("")) {
			Integer projectId = Integer.parseInt(projectIdStr);
			ProjectInfor projectInfor = (ProjectInfor) this.projectInforManager.get(projectId.intValue());

			// 判断用户是否有删除项目的权限
			if (this.projectInforManager.hasRight(projectInfor, systemUser, ProjectRight._Right_Delete) || projectInfor.getCreator().getPersonId() == systemUser.getPersonId() || systemUser.getUserType() == SystemUserInfor._Type_Admin) {
				this.projectInforManager.remove(projectInfor);
			}
		} else if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					Integer prcId = Integer.parseInt(deleteIds[i]);
					if (prcId != null && prcId.intValue() > 0) {
						ProjectInfor projectInfor = (ProjectInfor) this.projectInforManager.get(prcId.intValue());
						if (this.projectInforManager.hasRight(projectInfor, systemUser, ProjectRight._Right_Delete) || projectInfor.getCreator().getPersonId() == systemUser.getPersonId()) {
							this.projectInforManager.remove(projectInfor);
						}
					}
				}
			}
		}
		return null;
	}

	/***************************************************************************
	 * 查看项目信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=view")
	public String viewProjectInfor(HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer projectId = Integer.parseInt(request.getParameter("projectId"));
		ProjectInfor projectInfor = new ProjectInfor();
		if (projectId != null && projectId != 0) {
			projectInfor = (ProjectInfor) this.projectInforManager.get(projectId);
			request.setAttribute("_ProjectInfor", projectInfor);
		}
		return "viewProjectInfor";
	}

}
