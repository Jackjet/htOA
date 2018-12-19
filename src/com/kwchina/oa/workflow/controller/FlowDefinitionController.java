package com.kwchina.oa.workflow.controller;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.StructureManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowNode;
import com.kwchina.oa.workflow.entity.NodeCheckerPerson;
import com.kwchina.oa.workflow.entity.NodeCheckerRole;
import com.kwchina.oa.workflow.exception.NodeOperateException;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowNodeManager;
import com.kwchina.oa.workflow.service.NodeCheckerPersonManager;
import com.kwchina.oa.workflow.service.NodeCheckerRoleManager;
import com.kwchina.oa.workflow.vo.FlowDefinitionVo;
import com.kwchina.oa.workflow.vo.FlowNodeVo;


/**
 * 流程定义
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping("/workFlowDefinition.do")
public class FlowDefinitionController extends BasicController {

	@Autowired
	private FlowDefinitionManager flowManager;
	
	@Autowired
	private FlowNodeManager nodeManager;
	
	@Autowired
	private SystemUserManager systemUserManager;
	
	@Autowired
	private OrganizeManager organizeManager;
	
	@Autowired
	private StructureManager structureManager;
	
	@Autowired
	private RoleManager roleManager;
	
	@Autowired
	private NodeCheckerRoleManager nodeCheckerRoleManager; 
	
	@Autowired
	private NodeCheckerPersonManager nodeCheckerPersonManager;
	
	@Resource
	private CommonManager commonManager;

	/**
	 * 显示所有流程
	 */ 
	@RequestMapping(params = "method=listFlowDefinition")
	public void list(HttpServletRequest request, HttpServletResponse response)throws Exception {
		// 构造查询语句
		String[] queryString = this.flowManager.generateQueryString("FlowDefinition", "flowId", getSearchParams(request));

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.flowManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("charger.person");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 树状显示流程信息
	 */ 
	@RequestMapping(params = "method=listTree")
	public void listTree(HttpServletRequest request, HttpServletResponse response)throws Exception {
		
		// 构造查询语句
		String[] queryString = new String[2];
		queryString[0] = "from FlowDefinition flow where status=1";
		queryString[1] = "select count(flowId) from FlowDefinition flow where status=1";
		queryString = this.flowManager.generateQueryString(queryString, getSearchParams(request));

		List list = this.flowManager.getResultByQueryString(queryString[0]);
		
		JSONArray rows = new JSONArray();
		for (Iterator it=list.iterator();it.hasNext();) {
			FlowDefinition flow = (FlowDefinition)it.next();
			
			JSONObject obj = new JSONObject();
			obj.put("flowId", flow.getFlowId());
			obj.put("flowName", flow.getFlowName());
			obj.put("level", "1");
            obj.put("leaf", true);
            
            rows.add(obj);
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 新增或者修改
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, FlowDefinitionVo vo) throws Exception {
		
		Integer flowId = vo.getFlowId();
		FlowDefinition flow = new FlowDefinition();
		if (flowId != null && flowId.intValue() > 0) {
			//修改时,查出流程信息
			flow = (FlowDefinition)flowManager.get(flowId);
			request.setAttribute("_FlowDefinition", flow);
			
			BeanUtils.copyProperties(vo, flow);
			
			//主办人
			if (flow.getCharger() != null) {
				vo.setChargerId(flow.getCharger().getPersonId());
			}
			
			//归档角色
			if(flow.getFileRole() != null){
				vo.setFileRoleId(flow.getFileRole().getRoleId());
			}
			
			//自定义模板
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				String[] templateArray = flow.getTemplate().split("/");
				String templateName = templateArray[templateArray.length-1];
				request.setAttribute("_TemplateName", templateName);
			}
		}
		
		//所有用户
		List allSystemUsers = systemUserManager.findUserOrderByPersonName();
		request.setAttribute("_AllSystemUsers", allSystemUsers);

		//所有角色
		List allRoleInfor = roleManager.getAll();
		request.setAttribute("_AllRoles", allRoleInfor);
		
		String ac = request.getParameter("ac");
		if(ac.equals("view")){
			//标题链接查看时
			request.setAttribute("include_page", "view");
		}else if(ac.equals("edit")){
			//通知大页面将编辑页面INCLUDE进去 
			request.setAttribute("include_page", "edit");
		}
		
		return "workflow/inforFlowDefinition";
	}
	
	
	//获取所有流程自定义模板
	@RequestMapping(params = "method=getFlowTemplates")
	public void getFlowTemplates(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONArray fileArray = new JSONArray();
		File file = new File(CoreConstant.Context_Real_Path + "/" + FlowConstant.Flow_FormTemplate_Path);
		
		File[] childs = file.listFiles();
		for(int i=0; i<childs.length; i++) {
			//仅显示ftl文件
			if (childs[i].getName().endsWith(".ftl") && !childs[i].getName().equals("customizeTag.ftl")) {
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
	
	
	/**
	 * 保存信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, FlowDefinitionVo vo) throws Exception {

		FlowDefinition flow = new FlowDefinition();
		Integer flowId = vo.getFlowId();
		
		//修改保存时
		if (flowId != null && flowId.intValue() > 0) {
			flow = (FlowDefinition) this.flowManager.get(flowId);
		}
		
		BeanUtils.copyProperties(flow, vo);
		
		//保存主办人
		Integer chargerId = vo.getChargerId();
		if (chargerId != null && chargerId.intValue() > 0) {
			SystemUserInfor charger = (SystemUserInfor)this.systemUserManager.get(chargerId);
			flow.setCharger(charger);
		}
		
		//保存归档人类型和角色
		int filerType = vo.getFilerType();
		if(filerType == 0){
			flow.setFilerType(0);
			flow.setFileRole(null);
		}else if(filerType == 1){
			flow.setFilerType(1);
			Integer fileRoleId = vo.getFileRoleId();
			if (fileRoleId != null && fileRoleId.intValue() > 0) {
				RoleInfor role = (RoleInfor)this.roleManager.get(fileRoleId);
				flow.setFileRole(role);
			}
		}
		
		
		//保存自定义模板路径
		String flowTemplate = request.getParameter("flowTemplate");
		if (flowTemplate != null && flowTemplate.length() > 0) {
			flow.setTemplate(FlowConstant.Flow_FormTemplate_Path + "/" + flowTemplate);
		}else {
			flow.setTemplate(null);
		}
		
		flow = (FlowDefinition)this.flowManager.save(flow);
		request.setAttribute("_FlowDefinition", flow);
		
		/** 根据所选模板生成hbm文件并创建数据库表 */
		if (flowTemplate != null && flowTemplate.length() > 0) {
			//生成hbm文件
			String hbmName = this.commonManager.createHbm(CnToSpell.getFullSpell(flow.getFlowName()));
			
			//创建数据库表
			this.commonManager.createTable(hbmName, flowTemplate);
		}
		/** **** */
		
		//通知大页面将view页面INCLUDE进去 
		request.setAttribute("include_page", "view");
		
		return "workflow/inforFlowDefinition";

	}
	
	/*****
	 * 批量删除流程
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i=0;i<detleteIds.length;i++) {
					Integer flowId = Integer.valueOf(detleteIds[i]);
					
					//判断是否可以删除流程
					boolean canDelete = this.flowManager.canDeleteFlow(flowId);
					
					if (canDelete) {
						//删除节点
						List nodes = this.nodeManager.findFlowNodes(flowId);
						if (nodes != null && nodes.size() > 0) {
							for (Iterator it=nodes.iterator();it.hasNext();) {
								FlowNode node = (FlowNode)it.next();
								this.nodeManager.deleteFlowNode(node);
							}
						}
						
						//删除流程
						this.flowManager.remove(flowId);
					}
				}
			}
		}
	}
	
	/****
	 * 节点内容 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=nodeList")
	public void nodeList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		
		String flowId = request.getParameter("flowId");
		String[] basicQueryString = new String[2];
		
		if(flowId != null && flowId.length() > 0){
			//构造自定义查询语句
			basicQueryString[0] = "from FlowNode node where node.flowDefinition.flowId = "+flowId;
			basicQueryString[1] = "select count(node.nodeId) from FlowNode node where node.flowDefinition.flowId = "+flowId;
		
			String[] queryString = this.nodeManager.generateQueryString(basicQueryString,getSearchParams(request));
			
			String page = request.getParameter("page"); // 当前页
			String rowsNum = request.getParameter("rows"); // 每页显示的行数
			
			Pages pages = new Pages(request);
			pages.setPage(Integer.valueOf(page));
			pages.setPerPageNum(Integer.valueOf(rowsNum));

			PageList pl = this.nodeManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
			List list = pl.getObjectList();
			
			//将对象中包含的set集去掉
			List newList = new ArrayList();
			Iterator it=list.iterator();
			while(it.hasNext()) {
				FlowNode node = (FlowNode)it.next();
				node.setCheckerPersons(null);
				node.setCheckerRoles(null);
				newList.add(node);
			}

			// 定义返回的数据类型：json，使用了json-lib
			JSONObject jsonObj = new JSONObject();

			// 定义rows，存放数据
			JSONArray rows = new JSONArray();
			jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
			jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
			jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

			JSONConvert convert = new JSONConvert();
			//通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
			awareObject.add("flowDefinition");
			awareObject.add("forkedNode");
			awareObject.add("department");
			awareObject.add("structure");
			awareObject.add("user");
			rows = convert.modelCollect2JSONArray(newList, awareObject);
			jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

			// 设置字符编码
			response.setContentType(CoreConstant.CONTENT_TYPE);
			response.getWriter().print(jsonObj);
		}
	}
	
	/**
	 * 新增或者修改节点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=editNode")
	public String editNode(@ModelAttribute("flowNodeVo") FlowNodeVo vo,HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		Integer flowId = vo.getFlowId();
		Integer nodeId = vo.getNodeId();
		
		FlowNode node = new FlowNode();
		
		if (nodeId != null && nodeId.intValue() > 0) {
			//修改时,查出流程信息
			node = (FlowNode)nodeManager.get(nodeId);
			
			BeanUtils.copyProperties(vo, node);
			
			//节点名称
			vo.setFlowNodeName(node.getNodeName());
			
			//获取审核人信息
			if (node.getCheckerType() == FlowNode.Node_CheckerType_PreSet) {
				/** 自定义 */
				if (node.getCheckerPersons() != null && node.getCheckerPersons().size() > 0) {
					//自定义人员
					Set checkerPersons = node.getCheckerPersons();
					int[] userIds = new int[checkerPersons.size()];
					int i=0;
					for (Iterator it=checkerPersons.iterator();it.hasNext();) {
						NodeCheckerPerson checkerPerson = (NodeCheckerPerson)it.next();
						userIds[i] = checkerPerson.getUser().getPersonId();
						i++;
					}
					vo.setUserIds(userIds);
					request.setAttribute("_CheckerPerson", true);
				}else if (node.getCheckerRoles() != null && node.getCheckerRoles().size() > 0) {
					//自定义角色
					Set checkerRoles = node.getCheckerRoles();
					int[] roleIds = new int[checkerRoles.size()];
					int i=0;
					for (Iterator it=checkerRoles.iterator();it.hasNext();) {
						NodeCheckerRole checkerRole = (NodeCheckerRole)it.next();
						roleIds[i] = checkerRole.getRole().getRoleId();
						i++;
					}
					vo.setRoleIds(roleIds);
					request.setAttribute("_CheckerRole", true);
				}
			}else if (node.getCheckerType() == FlowNode.Node_CheckerType_Structrue) {
				/** 岗位 */
				if (node.getStructure() != null) {
					vo.setStructure_(node.getStructure().getStructureId());
				}
			}else if (node.getCheckerType() == FlowNode.Node_CheckerType_Department) {
				/** 部门 */
				if (node.getDepartment() != null) {
					vo.setDepartment_(node.getDepartment().getOrganizeId());
				}
			}else if (node.getCheckerType() == FlowNode.Node_CheckerType_Person) {
				/** 人员 */
				if (node.getUser() != null) {
					vo.setUser_(node.getUser().getPersonId());
				}
			}
			
		}else {
			//增加某个流程的节点时，获取可以作为fromNode的节点信息
			List fromNodes = this.nodeManager.getFromNodes(flowId);
			request.setAttribute("_FromNodes", fromNodes);
		}
		
		
		//获取用户信息
		List users = systemUserManager.findUserOrderByPersonName();
		request.setAttribute("_Users", users);
		
		//获取角色信息
		List roles = roleManager.getRoleOrderBy();
		request.setAttribute("_Roles", roles);
		
		//获取部门信息
		List organizes = this.organizeManager.getDepartments();
		request.setAttribute("_Departments", organizes);
		
		//获取岗位信息
		List structures = this.structureManager.getStructureOrderBy();
		request.setAttribute("_Structures", structures);
		
		FlowDefinition flow = (FlowDefinition)flowManager.get(flowId);
		request.setAttribute("_Flow", flow);
		
		
		return "workflow/editNode";
	}
	
	/**
	 * 查看节点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=viewNode")
	public String viewNode(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//String flowId = request.getParameter("flowId");
		String nodeId = request.getParameter("nodeId");
		
		//FlowDefinition flow = new FlowDefinition();
		FlowNode node = new FlowNode();
		
		if (nodeId != null && nodeId.length() > 0) {
			//修改时,查出流程信息
			node = (FlowNode)nodeManager.get(Integer.valueOf(nodeId));
			request.setAttribute("_FlowNode", node);
		}
		
		return "workflow/viewNode";
	}
	
	/**
	 * 保存节点信息
	 * @param request
	 * @param response
	 * @param multipartRequest
	 * @param vo
	 * @throws InvocationTargetException 
	 * @throws IllegalAccessException 
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveNode")
	@ResponseBody
	public Map<String, Object> saveNode(HttpServletRequest request, HttpServletResponse response,FlowNodeVo vo) throws IllegalAccessException, InvocationTargetException {

		Map<String, Object> map = new HashMap<String, Object>();
		
		FlowDefinition flow = new FlowDefinition();
		FlowNode node = new FlowNode();
		OrganizeInfor department = new OrganizeInfor();
		StructureInfor structure = new StructureInfor();
		SystemUserInfor user = new SystemUserInfor();
		
		Integer nodeId = vo.getNodeId();
		Integer flowId = vo.getFlowId();
		
		//修改保存时
		if (nodeId != null && nodeId.intValue() > 0) {
			node = (FlowNode) this.nodeManager.get(nodeId);
		}
		
		BeanUtils.copyProperties(node, vo);
		
		//节点名称
		node.setNodeName(vo.getFlowNodeName());
		
		//保存所属流程信息
		flow.setFlowId(flowId);
		node.setFlowDefinition(flow);
		
		//保存部门信息
		if(vo.getDepartment_() > 0){
			department.setOrganizeId(vo.getDepartment_());
			node.setDepartment(department);
		}
		
		//保存岗位信息
		if(vo.getStructure_() > 0){
			structure.setStructureId(vo.getStructure_());
			node.setStructure(structure);
		}
		
		//保存人员信息
		if(vo.getUser_() > 0){
			user = (SystemUserInfor)this.systemUserManager.get(vo.getUser_());
			node.setUser(user);
		}
		
		
		//所选来源节点
		int[] fromNodeIds = vo.getFromNodeIds();
		List fromNodes = new ArrayList();
		if (fromNodeIds != null && fromNodeIds.length > 0) {
			for (int i=0;i<fromNodeIds.length;i++) {
				FlowNode fromNode = (FlowNode)this.nodeManager.get(fromNodeIds[i]);
				fromNodes.add(fromNode);
			}
		}
		
		try {
			node = (FlowNode)this.nodeManager.saveFlowNode(node, fromNodes);
			
			//判断是哪种自定义审核方式
			String rightType = request.getParameter("rightType");
			
			if(rightType != null && rightType.length() > 0){

				if(rightType.equals("0")){
					/** 是角色时 */
					//删除已有用户信息
					Set checkerPersons = node.getCheckerPersons();
					if (checkerPersons != null && checkerPersons.size() > 0) {
						List tmpCheckerPersons = new ArrayList(checkerPersons);
						for (Iterator it=tmpCheckerPersons.iterator();it.hasNext();) {
							NodeCheckerPerson checkerPerson = (NodeCheckerPerson)it.next();
							this.nodeCheckerPersonManager.remove(checkerPerson);
						}
					}
					
					//保存角色信息
					int[] roleIds = vo.getRoleIds();
					Set checkerRoles = node.getCheckerRoles();
					List tmpCheckerRoles = new ArrayList(checkerRoles);
					
					//原来有,现在没有的删除
					for (Iterator it=tmpCheckerRoles.iterator();it.hasNext();) {
						NodeCheckerRole checkerRole = (NodeCheckerRole)it.next();
						
						RoleInfor tempRole = checkerRole.getRole();
						int rRoleId = tempRole.getRoleId().intValue();

						boolean hasThisRole = false;
						if (roleIds != null && roleIds.length != 0) {
							for (int i = 0; i < roleIds.length; i++) {
								if (rRoleId == roleIds[i]) {
									hasThisRole = true;
									break;
								}
							}
						}

						if (!hasThisRole) {
							node.getCheckerRoles().remove(checkerRole);
							//this.nodeCheckerRoleManager.remove(checkerRole);
						}
					}
					if (checkerRoles != null && checkerRoles.size() > 0) {
						node = (FlowNode)this.nodeManager.save(node);
					}
					
					//没有的加上
					if (roleIds != null && roleIds.length > 0) {
						for (int i=0;i<roleIds.length;i++) {
							boolean hasThisRole = false;

							for (Iterator it = checkerRoles.iterator(); it.hasNext();) {
								NodeCheckerRole checkerRole = (NodeCheckerRole)it.next();

								RoleInfor tempRole = checkerRole.getRole();
								int rRoleId = tempRole.getRoleId().intValue();
								if (rRoleId == roleIds[i]) {
									hasThisRole = true;
									break;
								}
							}

							if (!hasThisRole) {
								NodeCheckerRole checkerRole = new NodeCheckerRole();
								RoleInfor role = (RoleInfor)this.roleManager.get(roleIds[i]);
								checkerRole.setFlowNode(node);
								checkerRole.setRole(role);
								this.nodeCheckerRoleManager.save(checkerRole);
							}
						}
					}
					
				}else if(rightType.equals("1")){
					/** 是用户时 */
					//删除已有角色信息
					Set checkerRoles = node.getCheckerRoles();
					if (checkerRoles != null && checkerRoles.size() > 0) {
						node.getCheckerRoles().removeAll(checkerRoles);
						this.nodeManager.save(node);
					}
					
					int[] userIds = vo.getUserIds();
					Set checkerPersons = node.getCheckerPersons();
					
					//原来有,现在没有的删除
					for (Iterator it=checkerPersons.iterator();it.hasNext();) {
						NodeCheckerPerson checkerPerson = (NodeCheckerPerson)it.next();
						
						SystemUserInfor tempUser = checkerPerson.getUser();
						int rPersonId = tempUser.getPersonId().intValue();

						boolean hasThisUser = false;
						if (userIds != null && userIds.length != 0) {
							for (int i = 0; i < userIds.length; i++) {
								if (rPersonId == userIds[i]) {
									hasThisUser = true;
									break;
								}
							}
						}

						if (!hasThisUser) {
							this.nodeCheckerPersonManager.remove(checkerPerson);
						}
					}
					
					//没有的加上
					if (userIds != null && userIds.length > 0) {
						for (int i=0;i<userIds.length;i++) {
							boolean hasThisUser = false;

							for (Iterator it = checkerPersons.iterator(); it.hasNext();) {
								NodeCheckerPerson checkerPerson = (NodeCheckerPerson)it.next();

								SystemUserInfor tempUser = checkerPerson.getUser();
								int rPersonId = tempUser.getPersonId().intValue();
								if (rPersonId == userIds[i]) {
									hasThisUser = true;
									break;
								}
							}

							if (!hasThisUser) {
								NodeCheckerPerson checkerPerson = new NodeCheckerPerson();
								SystemUserInfor userInfor = (SystemUserInfor)this.systemUserManager.get(userIds[i]);
								checkerPerson.setFlowNode(node);
								checkerPerson.setUser(userInfor);
								this.nodeCheckerPersonManager.save(checkerPerson);
							}
						}
					}
				}
			}
		} catch (NodeOperateException e) {
			//e.printStackTrace();
			map.put("warningStr", "来源节点选择错误！");
		} 
		
		return map;
	}
	
	/**
	 * 删除节点
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteNode")
	public void deleteNode(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {
					Integer nodeId = Integer.valueOf(detleteIds[i]);
				
					FlowNode node = (FlowNode)this.nodeManager.get(Integer.valueOf(nodeId));
			
					nodeManager.deleteFlowNode(node);
				}
			}
		}
	}
}
