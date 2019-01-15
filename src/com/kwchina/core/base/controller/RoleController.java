package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.DataRightInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.base.service.ViewLogicRightManager;
import com.kwchina.core.base.vo.RoleInforVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;

@Controller
@RequestMapping("core/roleInfor.do")
public class RoleController extends BasicController{

	@Resource
	private RoleManager roleManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private ViewLogicRightManager viewLogicRightManager;
	
	@Autowired
	private DataRightInforManager dataRightInforManager;
	
	
	//显示所有角色
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//构造查询语句
		String[] queryString = this.roleManager.generateQueryString("RoleInfor", "roleId", getSearchParams(request));

		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.roleManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		rows = convert.modelCollect2JSONArray(list, new ArrayList());
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	//新增或者修改角色信息
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, RoleInforVo vo, Model model) throws Exception {

		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setRoleId(Integer.valueOf(rowId));
		}
		Integer roleId = vo.getRoleId();
		
		//修改
		if (roleId != null && roleId.intValue() != 0) {
			
			//判断是否有编辑权限
			this.dataRightInforManager.haveDataRight(request, response, "roleId", roleId, "edit");	
			
			RoleInfor roleInfor = (RoleInfor)this.roleManager.get(roleId);						
			
			//属性,从model到vo
			BeanUtils.copyProperties(vo, roleInfor);
			
			//系统用户
			List users = this.systemUserManager.getAll();
			int[] personIds = new int[users.size()];	
			Set roleUsers = roleInfor.getUsers();
			
			for (int i = 0; i < users.size(); i++) {
				SystemUserInfor tempUser = (SystemUserInfor) users.get(i);
				int tempPersonId = tempUser.getPersonId().intValue();
				
				for (Iterator it = roleUsers.iterator(); it.hasNext();) {
					SystemUserInfor rUser = (SystemUserInfor) it.next();
					int rPersonId = rUser.getPersonId().intValue();
					
					if (tempPersonId == rPersonId) {
						//该用户属于该角色
						personIds[i] = tempPersonId;
						break;
					}
				}
			}
			vo.setPersonIds(personIds);
			model.addAttribute("_PersonIds", personIds);
		}		
		

		//根据职级获取用户
		List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
		model.addAttribute("_Users", users);
		
		//获取职级大于一定值的用户
		List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
		model.addAttribute("_OtherUsers", otherUsers);
		
		//全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);

		return "base/editRole";
	}
	
	//保存角色信息
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, RoleInforVo vo) throws Exception {

		RoleInfor roleInfor = new RoleInfor();
		
		//角色类型(0-自定义;1-全体用户)
		int roleType = vo.getRoleType();
					
		BeanUtils.copyProperties(roleInfor, vo);
		
		Set users = new HashSet();
		if (roleType == 0) {
			/** 获得用户对应的用户 */
			int[] personIds = vo.getPersonIds();		
			if (personIds != null) {
				for (int k = 0; k < personIds.length; k++) {
					SystemUserInfor user = (SystemUserInfor)this.systemUserManager.get(personIds[k]);				
					users.add(user);
				}
			}
		}else {
			List<SystemUserInfor> validUsers = this.systemUserManager.getAllValid();
			if (validUsers != null && validUsers.size() > 0) {
				for (SystemUserInfor user:validUsers) {
					users.add(user);
				}
			}
		}
		roleInfor.setUsers(users);
		this.roleManager.save(roleInfor);
		return "success";
	}

	
	//删除角色信息
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, RoleInforVo vo) throws Exception {
		
		String detleteId = request.getParameter("rowId");
		if (detleteId != null && detleteId.length() > 0) {
			Integer roleId = Integer.valueOf(detleteId);
			
			//判断是否有删除权限
			this.dataRightInforManager.haveDataRight(request, response, "roleId", roleId, "delete");
			
			this.roleManager.remove(roleId);
		}
	}
	
}
