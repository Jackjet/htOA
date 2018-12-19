package com.kwchina.extend.supervise.controller;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.extend.supervise.entity.TaskCategory;
import com.kwchina.extend.supervise.entity.TaskCategoryRight;
import com.kwchina.extend.supervise.service.TaskCategoryManager;
import com.kwchina.extend.supervise.service.TaskCategoryRightManager;
import com.kwchina.extend.supervise.util.TaskConstant;
import com.kwchina.extend.supervise.vo.TaskCategoryVo;
import com.kwchina.oa.customer.service.TaskInforManager;
import com.kwchina.oa.document.entity.DocumentCategory;
import com.kwchina.oa.document.util.DocumentConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/supervise/taskCategory.do")
public class TaskCategoryController {

	@Resource
	private TaskCategoryManager taskCategoryManager;

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private TaskCategoryRightManager taskCategoryRightManager;

	@Resource
	private OrganizeManager organizeManager;


	/**
	 * 浏览任务分类
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.taskCategoryManager.getCategoryAsTree(TaskConstant._Root_Category_Id);
		request.setAttribute("_TREE", returnArray);
		
		//getLeftCategory(form, request, 1);
		
		return "listTaskCategory";
	}
	
	/**
	 * 跳转到基本页面
	 * @param inforPath
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listBase")
	public String listBase(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		boolean isParty = false;
		
		//根据path获取categoryId
		String categoryTypeStr = request.getParameter("categoryType");
		int categoryId = TaskConstant._Root_Category_Id;
		if(categoryTypeStr != null && !categoryTypeStr.equals("")){
			if(categoryTypeStr.equals("1")){
				categoryId = TaskConstant._Root_Admin_Category_Id;
			}
			if(categoryTypeStr.equals("2")){
				categoryId = TaskConstant._Root_Party_Category_Id;
				isParty = true;
			}
			if(categoryTypeStr.equals("3")){
				categoryId = TaskConstant._Root_Department_Category_Id;
			}
			if(categoryTypeStr.equals("4")){
				categoryId = TaskConstant._Root_Inside_Category_Id;
			}
		}
		
		//假如该分类下包含子分类,则跳转到包含左边树状分类的inforBaseList页面
		TaskCategory category = (TaskCategory)this.taskCategoryManager.get(Integer.valueOf(categoryId));
		request.setAttribute("_Category", category);
		
		//找到所有部门
		List departments = this.organizeManager.getAll();
		
		//去掉特殊部门
		List<String> validDeparts = new ArrayList<String>();
		validDeparts.add("总经理办公室");
		validDeparts.add("党群工作部");
		validDeparts.add("人力资源部");
		validDeparts.add("计划财务部");
		validDeparts.add("技术规划部");
		validDeparts.add("安全质量部");
		validDeparts.add("信息技术部");
		validDeparts.add("市场营销部");
		validDeparts.add("码头运营部");
		validDeparts.add("滚装业务部");
		//validDeparts.add("整车服务中心");
		//validDeparts.add("整车物流部");
		validDeparts.add("整车物流部");
		validDeparts.add("零部件物流部");
		validDeparts.add("采购部");
		validDeparts.add("数据业务部");
		
		if(isParty){
			validDeparts.add("洋山码头");
			validDeparts.add("太仓码头");
		}
		
		List actualDeparts = new ArrayList();
		
		//是“督办查看人员”角色的，可看全部部门，其余只能看本部门
		boolean canSeeAll = false;
		boolean canSeeXz = false;     //可查看所有方针目录（原行政类）
		boolean canSeeDq = false;     //可查看所有党群
		
		boolean canSeeBmjs = false;   //可查看所有部门建设类
		boolean canSeeNk = false;     //可查看所有内控类
		
		SystemUserInfor userInfor = SysCommonMethod.getSystemUser(request);
		Set<RoleInfor> roles = userInfor.getRoles();
		for(RoleInfor role : roles){
			if(role.getRoleName().contains("查看所有工作跟踪")){
				canSeeAll = true;
				break;
			}
			
			if(role.getRoleName().contains("行政督办查看人员")){
				canSeeXz = true;
				//break;
			}
			
			if(role.getRoleName().contains("党群督办查看人员")){
				canSeeDq = true;
				//break;
			}
			
			if(role.getRoleName().contains("部门建设督办查看人员")){
				canSeeBmjs = true;
				//break;
			}
			
			if(role.getRoleName().contains("内控督办查看人员")){
				canSeeNk = true;
				//break;
			}
		}
		
//		if(canSeeAll){
//			for(String depart : validDeparts){
//				for(int i=0;i<departments.size();i++){
//					OrganizeInfor department = (OrganizeInfor)departments.get(i);
//					if(department.getOrganizeName().equals(depart)){
//						actualDeparts.add(department);
//					}
//				}
//			}
//		}
		//公司领导可看到全部  或者 行政、党群督办查看人员
		if(canSeeAll || userInfor.getPerson().getDepartment().getOrganizeId() == 75 || (categoryTypeStr.equals("1") && canSeeXz) 
				|| (categoryTypeStr.equals("2") && canSeeDq) ||(categoryTypeStr.equals("3") && canSeeBmjs) 
				|| (categoryTypeStr.equals("4") && canSeeNk)){
			for(String depart : validDeparts){
				for(int i=0;i<departments.size();i++){
					OrganizeInfor department = (OrganizeInfor)departments.get(i);
					if(department.getOrganizeName().equals(depart)){
						actualDeparts.add(department);
					}
				}
			}
		}else {
			OrganizeInfor depart = (OrganizeInfor)this.organizeManager.get(userInfor.getPerson().getDepartment().getOrganizeId());
			actualDeparts.add(depart);
		}
		
		request.setAttribute("_Departs", actualDeparts);
		
		return "taskBaseList";
	}

	/**
	 * 按照树状结构获取督办分类信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getCategoryTree")
	public void getCategoryTree(HttpServletRequest request, HttpServletResponse response) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String categoryId = request.getParameter("categoryId");
		
		if (categoryId != null && categoryId.length() > 0) {
			ArrayList returnArray = this.taskCategoryManager.getCategoryAsTree(Integer.valueOf(categoryId), systemUser);
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			
			JSONArray array = new JSONArray();
			array = convert.modelCollect2JSONArray(returnArray, new ArrayList());
			jsonObj.put("rows", array);
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
		}
		

//		ArrayList returnArray = this.taskCategoryManager.getCategoryAsTree(TaskConstant._Root_Category_Id, systemUser);
//
//		// 将set集置空
//		Iterator it = returnArray.iterator();
//		List returnList = new ArrayList();
//		while (it.hasNext()) {
//			TaskCategory tc = (TaskCategory) it.next();
//			tc.setChilds(null);
//			tc.setRights(null);
//			tc.setTasks(null);
//			returnList.add(tc);
//		}
//
//		String page = request.getParameter("page"); // 当前页
//		String rowsNum = request.getParameter("rows"); // 每页显示的行数
//		Pages pages = new Pages(request);
//		pages.setPage(Integer.valueOf(page));
//		pages.setPerPageNum(Integer.valueOf(rowsNum));
//
//		// 定义返回的数据类型：json，使用了json-lib
//		JSONObject jsonObj = new JSONObject();
//
//		// 定义rows，存放数据
//		JSONArray rows = new JSONArray();
//
//		JSONConvert convert = new JSONConvert();
//		rows = convert.modelCollect2JSONArray(returnList, new ArrayList());
//		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)
//
//		// 设置字符编码
//		response.setContentType(CoreConstant.CONTENT_TYPE);
//		response.getWriter().print(jsonObj);
	}
	
	/**
	 * 获取查询条件数据(分类名称信息)
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getCategoryName")
	public void getCategoryName(HttpServletRequest request, HttpServletResponse response) throws Exception {

		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();

		// 当前登录用户ID
		// SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// int personId = user.getPersonId();

		// 分类信息
		JSONArray categoryArray = new JSONArray();
		List categoryNames = this.taskCategoryManager.getCategoryName();
		categoryArray = convert.modelCollect2JSONArray(categoryNames, new ArrayList());
		jsonObj.put("_CategoryNames", categoryArray);

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}

	//编辑
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, TaskCategoryVo vo) throws Exception {

		Integer categoryId = vo.getCategoryId();

		//获取用户信息
		List users = this.systemUserManager.getAll();
		
		TaskCategory category = null;
		//修改
		if (categoryId != null && categoryId.intValue() > 0) {
			category = (TaskCategory) this.taskCategoryManager.get(categoryId);

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
				TaskCategoryRight right = (TaskCategoryRight) it.next();
				int userId = right.getUser().getPersonId().intValue();

				//创建权限
				if (this.taskCategoryRightManager.hasRight(right, TaskCategoryRight._Right_Edit)) {
					createIds[k] = userId;
				}

				//删除权限
				if (this.taskCategoryRightManager.hasRight(right, TaskCategoryRight._Right_Delete)) {
					deleteIds[k] = userId;
				}

				//浏览权限
				if (this.taskCategoryRightManager.hasRight(right, TaskCategoryRight._Right_View)) {
					viewIds[k] = userId;
				}

				k += 1;
			}

			request.setAttribute("_CreateIds", createIds);
			request.setAttribute("_DeleteIds", deleteIds);
			request.setAttribute("_ViewIds", viewIds);

		}

		//全部分类树状显示(需要判断分类的权限)
		ArrayList returnArray = this.taskCategoryManager.getCategoryAsTree(TaskConstant._Root_Category_Id);
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

		return "editTaskCategory";
	}

	//保存
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, TaskCategoryVo vo) throws Exception {

		TaskCategory category = new TaskCategory();
		Integer categoryId = vo.getCategoryId();

		if (categoryId != null && categoryId.intValue() != 0) {
			category = (TaskCategory) this.taskCategoryManager.get(categoryId.intValue());
		}
		
		if (vo.getParentId() != 0) {
			TaskCategory parent = (TaskCategory) this.taskCategoryManager.get(vo.getParentId());
			category.setParent(parent);
			vo.setLayer(parent.getLayer()+1);
			//把其父分类的isLeaf改为false
			parent.setLeaf(false);
			this.taskCategoryManager.save(parent);
		}
		//是否叶分类
		category.setLeaf(vo.isLeaf());
		
		
		BeanUtils.copyProperties(category, vo);
		
		this.taskCategoryManager.saveCategory(category, vo);

	}

	//删除分类操作
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, TaskCategoryVo vo) throws Exception {
		
		Integer categoryId = vo.getCategoryId();
		if (categoryId != null && categoryId.intValue() > 0) {
			TaskCategory category = (TaskCategory)this.taskCategoryManager.get(categoryId);
			deleteChildren(category);
			
		}
	}
	private void deleteChildren(TaskCategory category){
		Set childs = category.getChilds();
		
		if(childs!=null && childs.size()>0){
			List tmpList = new ArrayList(childs);
			for(Iterator it = tmpList.iterator();it.hasNext();){
				TaskCategory tpS = (TaskCategory)it.next();
				
				//从父对象移除
				childs.remove(tpS);
				
				deleteChildren(tpS);
			}			
		}
		this.taskCategoryManager.remove(category);
	}
	
}
