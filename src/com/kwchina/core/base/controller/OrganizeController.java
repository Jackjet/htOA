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

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.DirAndSupInfor;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.service.DirAndSupInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;


@Controller
@RequestMapping("/core/organizeInfor.do")
public class OrganizeController extends BasicController {
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private PersonInforManager personInforManager;
	
	@Resource
	private DirAndSupInforManager dirAndSupInforManager;
	

	//按照树状结构获取组织结构信息
	@RequestMapping(params="method=list")
	public String listTree(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		ArrayList returnArray = organizeManager.getOrganizeAsTree(CoreConstant.Organize_Begin_Id);
		request.setAttribute("_OrganizeTree", returnArray);
		
		//构造查询语句
		/*String[] queryString = this.organizeManager.generateQueryString("OrganizeInfor", "organizeId", getSearchParams(request));

		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.organizeManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
        Iterator it = list.iterator();
		while(it.hasNext()){
			OrganizeInfor organize = (OrganizeInfor)it.next();
			JSONObject obj = new JSONObject();
			obj.put("organizeId", organize.getOrganizeId());
			obj.put("organizeName", organize.getOrganizeName());
			obj.put("shortName", organize.getShortName());
			obj.put("organizeNo", organize.getOrganizeNo());
			String director = "";
			if (organize.getDirector() != null) {
				director = organize.getDirector().getPersonName();
			}
			obj.put("director", director);
			obj.put("orderNo", organize.getOrderNo());
			obj.put("level", 1);
			if (organize.getParent() != null) {
				obj.put("leaf", true);
			}else {
				obj.put("leaf", false);
			}
            rows.add(obj);
		}
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);*/
		
		return "base/listOrganize";
	}

	
	//获取查询条件数据(部门信息)
	@RequestMapping(params="method=getDepartments")
	public void getDepartments(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//部门信息
		JSONArray departmentArray = new JSONArray();
		List departments = this.organizeManager.getDepartments();
		departmentArray = convert.modelCollect2JSONArray(departments, new ArrayList());
		jsonObj.put("_Departments", departmentArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//获取查询条件数据(班组信息)
	@RequestMapping(params="method=getGroups")
	public void getGroups(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//班组信息
		String departmentId = request.getParameter("departmentId");
		JSONArray groupArray = new JSONArray();
		
		List groups = new ArrayList();
		if (departmentId != null && departmentId.length() > 0 && !("0").equals(departmentId)) {
			//若departmentId不为空,则取该部门下的班组信息
			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentId));
			groups = new ArrayList(department.getChilds());
		}else {
			//若departmentId为空,则取该所有班组信息
			groups = this.organizeManager.getGroups();
		}
		groupArray = convert.modelCollect2JSONArray(groups, new ArrayList());
		jsonObj.put("_Groups", groupArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	//获取部门名称(用于工作流自定义标签)
	@RequestMapping(params="method=getOrganizeName")
	public void getOrganizeName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		//部门名称
		String departmentId = request.getParameter("departmentId");
		String organizeName = "";
		if (departmentId != null && departmentId.length() > 0) {
			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentId));
			if (department != null) {
				organizeName = department.getOrganizeName();
			}
		}
		jsonObj.put("_OrganizeName", organizeName);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//获取部门简称(用于公文生成发文号、收文号)
	@RequestMapping(params="method=getShortName")
	public void getShortName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		
		//部门名称
		String departmentId = request.getParameter("departmentId");
		String shortName = "";
		if (departmentId != null && departmentId.length() > 0) {
			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(departmentId));
			if (department != null) {
				shortName = department.getShortName();
			}
		}
		jsonObj.put("_ShortName", shortName);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//编辑
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String organizeId = request.getParameter("organizeId");
		
		//修改
		if (organizeId != null && organizeId.length() > 0) {
			OrganizeInfor organize = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(organizeId));
			request.setAttribute("_Organize", organize);
			
			//若是投资公司,则发送董事、监事信息到页面
			if (organize.getLevelId() == 4 && organize.getDirAndSups() != null && organize.getDirAndSups().size() > 0) {
				Set dirAndSups = organize.getDirAndSups();
				List list = new ArrayList(dirAndSups);
				DirAndSupInfor dirAndSup = (DirAndSupInfor)list.get(0);
				request.setAttribute("_DirAndSup", dirAndSup);
			}
		}
		
		//所有部门班组信息
		List treeOrganizes = this.organizeManager.getOrganizeAsTree(CoreConstant.Organize_Begin_Id);
		request.setAttribute("_TreeOrganizes", treeOrganizes);
		
		//人员信息
		List persons = this.personInforManager.getPersonOrderByName();
		request.setAttribute("_Persons", persons);
		
		return "base/editOrganize";
	}
	
	
	//保存
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String organizeId = request.getParameter("organizeId");
		String organizeName = request.getParameter("organizeName");
		String shortName = request.getParameter("shortName");
		String organizeNo = request.getParameter("organizeNo");
		String orderNo = request.getParameter("orderNo");
		String levelId = request.getParameter("levelId");
		String parentId = request.getParameter("parentId");
		String directorId = request.getParameter("directorId");
		String guikou = request.getParameter("guikou");
		OrganizeInfor organize = new OrganizeInfor();
		
		//修改
		if (organizeId != null && organizeId.length() > 0) {
			organize = (OrganizeInfor)this.organizeManager.get(Integer.valueOf(organizeId));		
		}
		//部门名称、简称、编号、组织层级、排序编号、父部门或公司、经理/主管
		organize.setOrganizeName(organizeName);
		organize.setGuikou(guikou);
		organize.setShortName(shortName);
		organize.setOrganizeNo(organizeNo);
		if (orderNo != null && orderNo.length() > 0) {
			organize.setOrderNo(Integer.valueOf(orderNo));
		}
		if (levelId != null && levelId.length() > 0) {
			organize.setLevelId(Integer.valueOf(levelId));
			if (levelId.equals("2")) {
				//为班组信息时层级设为2
				organize.setLayer(2);
			}else if (levelId.equals("4")){
				//为投资公司时层级设为1,且保存相关董事、监事信息
				organize.setLayer(1);
				String directors = request.getParameter("directors");
				String supervisors = request.getParameter("supervisors");
				if ((directors != null && directors.length() > 0) || (supervisors != null && supervisors.length() > 0)) {
					Set oldDirAndSups = organize.getDirAndSups();
					List tmpList = new ArrayList(oldDirAndSups);
					//删除以前的信息
					if (oldDirAndSups != null && oldDirAndSups.size() > 0) {
						for (Iterator it=tmpList.iterator();it.hasNext();) {
							DirAndSupInfor dirAndSup = (DirAndSupInfor)it.next();
							oldDirAndSups.remove(dirAndSup);
							this.dirAndSupInforManager.remove(dirAndSup.getDirAndSupId());
						}
					}
					//添加新信息
					Set dirAndSups = new HashSet();
					DirAndSupInfor dirAndSup = new DirAndSupInfor();
					dirAndSup.setDirectors(directors);
					dirAndSup.setSupervisors(supervisors);
					dirAndSup.setOrganize(organize);
					dirAndSups.add(dirAndSup);
					organize.setDirAndSups(dirAndSups);
				}
			}else {
				//为部门、分公司时层级设为1
				organize.setLayer(1);
			}
		}
		if(parentId != null && parentId.length() > 0){
			OrganizeInfor parent= (OrganizeInfor)this.organizeManager.get(Integer.valueOf(parentId));
			organize.setParent(parent);
		}
		if(directorId != null && directorId.length() > 0){
			PersonInfor director = (PersonInfor)this.personInforManager.get(Integer.valueOf(directorId));
			organize.setDirector(director);
		}
		
		this.organizeManager.save(organize);
		return "success";
	}
	
	
	//删除
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String organizeId = request.getParameter("organizeId");
		if (organizeId != null && organizeId.length() > 0) {
			OrganizeInfor organize  = (OrganizeInfor)this.organizeManager.get(Integer.parseInt(organizeId));
			deleteChildren(organize);
		}
	}
	//获取当前部门
	@RequestMapping(params="method=getCurDep")
	public void getCurDep(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String organizeId = request.getParameter("organizeId");
		if (organizeId != null && organizeId.length() > 0) {
			OrganizeInfor organize  = (OrganizeInfor)this.organizeManager.get(Integer.parseInt(organizeId));
			deleteChildren(organize);
		}
	}

	private void deleteChildren(OrganizeInfor organize){
		Set childs = organize.getChilds();
		
		if(childs!=null && childs.size()>0){
			List tmpList = new ArrayList(childs);
			for(Iterator it = tmpList.iterator();it.hasNext();){
				OrganizeInfor tpS = (OrganizeInfor)it.next();
				
				//从父对象移除
				childs.remove(tpS);
				
				deleteChildren(tpS);
			}			
		}
		
		this.organizeManager.remove(organize);
	}
	
}
