package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.jmesa.facade.TableFacade;
import org.jmesa.limit.Limit;
import org.jmesa.util.ItemUtils;
import org.jmesa.view.component.Row;
import org.jmesa.view.component.Table;
import org.jmesa.view.editor.CellEditor;
import org.jmesa.view.html.HtmlBuilder;
import org.jmesa.view.html.HtmlComponentFactory;
import org.jmesa.view.html.component.HtmlColumn;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.DataRightInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.StructureManager;
import com.kwchina.core.base.service.ViewLogicRightManager;
import com.kwchina.core.base.vo.PersonInforVo;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.jmesa.HibernateWebUtils;
import com.kwchina.core.util.jmesa.JmesaUtils;
import com.kwchina.core.util.jmesa.PageForMesa;
import com.kwchina.core.util.jmesa.PropertyFilter;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping(value="/core/personInfor_bak.do")
public class PersonController_bak {

	/*@Resource
	private Validator validator;*/
	
	@Autowired
	private PersonInforManager personManager;

	@Autowired
	private OrganizeManager organizeManager;

	@Autowired
	private StructureManager structureManager;
	
	@Autowired
	private ViewLogicRightManager viewLogicRightManager;
	
	@Autowired
	private DataRightInforManager dataRightInforManager;
	
	/*@InitBinder
	public void initBinder(WebDataBinder binder) {
	   SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
	   dateFormat.setLenient(false);
	   binder.registerCustomEditor(Date.class, new CustomDateEditor(dateFormat, false));
	}*/

	//显示所有人员
	@RequestMapping(params="method=list")
	public String list(HttpServletRequest request, HttpServletResponse response, @ModelAttribute("personInforVo") PersonInforVo vo, Model model) throws Exception {
		
		this.viewLogicRightManager.getViewRightSql(request, "list", null);
		
		//分页对象
		PageForMesa<PersonInfor> page = new PageForMesa<PersonInfor>(10);//每页10条记录
		
		//分页参数
//		page.setPageNo(vo.getPageNo() == null ? 1 : vo.getPageNo());
//		page.setOrder(vo.getOrder());
//		page.setPageSize(vo.getPageSize() == null ? 10 : vo.getPageSize());
//		page.setOrderBy(vo.getOrderBy());
		/*if ((page.getOrder() == null || page.getOrder().length() == 0)
				&& (page.getOrderBy() != null || page.getOrderBy().length() > 0)) {
			page.setOrder("desc");
			page.setOrderBy("personId");
		}*/

		//通过分析Request，从页面返回信息中提取带有Jmesa特定的Prefix的参数，并组成PropertyFilter
		List<PropertyFilter> filters = HibernateWebUtils.buildJmesaFilters(request, "personId", PersonInfor.class.getName());
		//不显示被逻辑删除的信息
		/*PropertyFilter filter = new PropertyFilter("deleted", MatchType.EQ, 0);
		filters.add(filter);*/
		//为了保证可以对关联表中的信息进行无差别查询，需要为关联表建立别名
		Map<String, String> alias = new HashMap<String, String>();
		/*alias.put("department", "department");
		alias.put("structure", "structure");*/
		//根据PropertyFilter参数对获取列表中的信息
		page = this.personManager.findByPage(page, filters, alias);
		//根据页面参数构成新的页面的列表对象
		TableFacade facade = JmesaUtils.buildTableFacade(page, request, response);
		//定义当前的列表标题及配对从数据库中返回的信息的字段
		facade = this.ornamentFacade(facade, "personId");
		//获取控制导出功能的对象
		Limit limit = facade.getLimit();
		if (limit.isExported()) {
			//导出查询所得的信息
			facade.render();
			return null;
		} else {
			//facade = JmesaUtils.reOrnamentFacade(facade, "personId");
			//列表对象facade生成一串HTML代码返回到页面
			model.addAttribute("jmesaHtml", facade.render());
		}
		
		return "base/listPerson";
	}
	
	
	//编辑人员
	@RequestMapping(params="method=edit")
	@ResponseBody
	public Map<String,Object> edit(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		Integer personId = vo.getPersonId();
		PersonInfor person = new PersonInfor();
		
		if (personId != null && personId.intValue() != 0) {
			//判断是否有编辑权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "edit");
			//编辑
			person = (PersonInfor)this.personManager.get(personId);	
			//属性从model到vo
			BeanUtils.copyProperties(vo, person);			
			
			if(person.getDepartment()!=null){
				vo.setDepartmentId(person.getDepartment().getOrganizeId());
			}
			if(person.getGroup()!=null){
				vo.setGroupId(person.getGroup().getOrganizeId());
			}
			if(person.getStructure()!=null){
				vo.setStructureId(person.getStructure().getStructureId());
			}
		}
		
		//所有部门，班组以及按照树状结构组织的岗位信息
		List departments = this.organizeManager.getDepartments();
		List groups = this.organizeManager.getGroups();
		List treeStructures = this.structureManager.getStructureAsTree(CoreConstant.Structure_Begin_Id);
		
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("person", vo);
		List list=new ArrayList();
	    OrganizeInfor department = new OrganizeInfor(); 
	    department.setOrganizeId(1);
	    department.setOrganizeName("上港物流");
		list.add(department);
		map.put("departments", list);
		map.put("groups", null);
		
		
		/*List list=new ArrayList();
	      list.add("11");
	      list.add("22");
	      list.add("33");
	      JSONArray jsonArray = JSONArray.fromObject(list);
		JSONObject object = new JSONObject();
		object.put("users", jsonArray);
		object.put("status", "1");*/
		return map;
	}
	
	//保存人员
	@RequestMapping(params="method=save")
	//@ResponseBody
	public void save(HttpServletRequest request, HttpServletResponse response,
			@ModelAttribute("personInforVo") PersonInforVo vo, BindingResult result, Model model) throws Exception {

		//Map<String,Object> map = new HashMap<String,Object>();
		
		//表单验证的方法
		/*validator.validate(vo, result);
		if (result.hasErrors()) {
			//如果验证不通过
			map.put("status", "0");
			List errors = result.getAllErrors();
			String errorMsg = ErrorUtils.getErrorStr(errors);
			map.put("errorMs",errorMsg);			
			return map;
		}*/
		
		Integer personId = vo.getPersonId();
		
		PersonInfor person = new PersonInfor();
		if (personId != null && personId.intValue() != 0) {
			//修改保存
			person = (PersonInfor)this.personManager.get(personId);			
		}
		//属性从vo到model
		BeanUtils.copyProperties(person, vo);			
		
		//保存部门、班组、岗位信息
		Integer departmentId = vo.getDepartmentId();
		Integer groupId = vo.getGroupId();
		Integer structureId = vo.getStructureId();
		if(departmentId != null && departmentId.intValue() != 0){
			OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(departmentId);
			person.setDepartment(department);
		}
		
		if(groupId != null && groupId.intValue() != 0){
			OrganizeInfor group = (OrganizeInfor)this.organizeManager.get(groupId);
			person.setGroup(group);
		}
		
		if(structureId != null && structureId.intValue() != 0){
			StructureInfor structure = (StructureInfor)this.structureManager.get(structureId);
			person.setStructure(structure);
		}
		this.personManager.save(person);
		
		//return map;
	}
	
	
	//删除人员信息
	@RequestMapping(params="method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		/*SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String[] deleteIds = request.getParameterValues("toBeDeletedId");
		if (deleteIds != null) {
			for (int i = 0; i < deleteIds.length; i++) {
				PersonInfor person  = (PersonInfor)this.personManager.get(Integer.parseInt(deleteIds[i]));*/
				
				Integer personId = vo.getPersonId();
				if (personId != null && personId.intValue() != 0) {
					//判断是否有删除权限
					this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "delete");
					
					//PersonInfor person  = (PersonInfor)this.personManager.get(personId);
					this.personManager.remove(personId);
				}
			/*}
		}*/

		return list(request, response, vo, model);
	}
	
	//注销或恢复人员
	@RequestMapping(params="method=cancelOrResume")
	public String cancelOrResume(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		Integer personId = vo.getPersonId();
		if (personId != null && personId.intValue() != 0) {
			//判断是否有注销或恢复权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "cancelOrResume");
			
			PersonInfor person = (PersonInfor)this.personManager.get(personId);
			if (person != null) {
				if (person.isDeleted()) {
					//若已注销,则执行恢复操作
					person.setDeleted(false);
				}else {
					//若正常,则执行注销操作
					person.setDeleted(true);
				}
			}
			this.personManager.save(person);
		}

		return list(request, response, vo, model);
	}
	
	//编辑个人信息
	@RequestMapping(params="method=editPersonalInfor")
	public String editPersonInfor(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int personId = systemUser.getPersonId().intValue();
		PersonInfor person = (PersonInfor)this.personManager.get(personId);
		
		//属性从model到vo
		BeanUtils.copyProperties(vo, person);
		
		model.addAttribute("_PersonInfor", person);
		
		return "base/editPersonalInfor";
	}
	

	/**
	 * Jmesa表格标题、列名等属性的设置.
	 */
	private TableFacade ornamentFacade(TableFacade facade, final String id) {
		
		HtmlComponentFactory factory = new HtmlComponentFactory(facade.getWebContext(), facade.getCoreContext());
		Row row = factory.createRow();
		Table table = factory.createTable();
		
		HtmlColumn nameColumn = factory.createColumn("");
		nameColumn.setTitle("姓名");
		nameColumn.setSortable(true);
		nameColumn.setFilterable(true);
		nameColumn.setProperty("personName");
		row.addColumn(nameColumn);
		
		HtmlColumn genderColumn = factory.createColumn("");
		genderColumn.setTitle("性别");
		genderColumn.setProperty("gender");
		genderColumn.setSortable(true);
		genderColumn.setFilterable(true);
		CellEditor nameEditor = new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				int gender = (Integer)ItemUtils.getItemValue(item, "gender");
				String genderStr = "";
				if (gender == 0) genderStr = "男";
				if (gender == 1) genderStr = "女";
				
				HtmlBuilder html = new HtmlBuilder();				
				html.append(genderStr);
				html.aEnd();						
				
				return html.toString();
			}
		};
		genderColumn.getCellRenderer().setCellEditor(nameEditor);
		row.addColumn(genderColumn);
		
		HtmlColumn personNoColumn = factory.createColumn("");
		personNoColumn.setTitle("编号");
		personNoColumn.setSortable(true);
		personNoColumn.setFilterable(true);
		personNoColumn.setProperty("personNo");
		row.addColumn(personNoColumn);
		
		HtmlColumn departmentColumn = factory.createColumn("");
		departmentColumn.setTitle("部门[班组]");
		departmentColumn.setSortable(true);
		departmentColumn.setFilterable(true);
		departmentColumn.setProperty("department.organizeName");
		row.addColumn(departmentColumn);
		
		HtmlColumn structureColumn = factory.createColumn("");
		structureColumn.setTitle("岗位");
		structureColumn.setSortable(true);
		structureColumn.setFilterable(true);
		structureColumn.setProperty("structure.structureName");
		row.addColumn(structureColumn);
		
		HtmlColumn mobileColumn = factory.createColumn("");
		mobileColumn.setTitle("手机");
		mobileColumn.setSortable(true);
		mobileColumn.setFilterable(true);
		mobileColumn.setProperty("mobile");
		row.addColumn(mobileColumn);
		
		HtmlColumn emailColumn = factory.createColumn("");
		emailColumn.setTitle("email");
		emailColumn.setSortable(true);
		emailColumn.setFilterable(true);
		emailColumn.setProperty("email");
		row.addColumn(emailColumn);
		
		HtmlColumn statusColumn = factory.createColumn("");
		statusColumn.setTitle("状态");
		statusColumn.setProperty("deleted");
		statusColumn.setSortable(true);
		statusColumn.setFilterable(true);
		CellEditor statusEditor = new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				boolean status = (Boolean)ItemUtils.getItemValue(item, "deleted");
				String statusStr = "";
				if (!status) statusStr = "正常";
				if (status) statusStr = "已注销";
				
				HtmlBuilder html = new HtmlBuilder();				
				html.append(statusStr);
				html.aEnd();						
				
				return html.toString();
			}
		};
		statusColumn.getCellRenderer().setCellEditor(statusEditor);
		row.addColumn(statusColumn);
		
		//操作列
		HtmlColumn column = factory.createColumn("");
		column.setTitle("操作");
		column.setSortable(false);
		column.setFilterable(false);		
		
		CellEditor cellEditor = new CellEditor() {
			public Object getValue(Object item, String property, int rowcount) {
				Object theId = ItemUtils.getItemValue(item, id);
				boolean status = (Boolean)ItemUtils.getItemValue(item, "deleted");
				String modifyJS = "javascript:openUpdate(" + theId + ")";
				String removeJS = "javascript:if(confirm('删除后不可恢复,确定要删除该信息么?')){remove(" + theId + ")}";
				String cancelOrResumeJS = "javascript:if(confirm('确定要注销该人员么?')){cancelOrResume(" + theId + ")}";
				if (status) {
					cancelOrResumeJS = "javascript:if(confirm('确定要恢复该人员么?')){cancelOrResume(" + theId + ")}";
				}
				
				HtmlBuilder html = new HtmlBuilder();
				html.a().href().quote().append(modifyJS).quote().close();
				html.append("[修改]");
				html.aEnd();
				
				html.append(" ").a().href().quote().append(removeJS).quote().close();
                html.append("[删除]");
				html.aEnd();
				
				html.append(" ").a().href().quote().append(cancelOrResumeJS).quote().close();
				if (status) {
					html.append("[恢复]");
				}else {
					html.append("[注销]");
				}
				html.aEnd();
				
				return html.toString();
			}
		};
		column.getCellRenderer().setCellEditor(cellEditor);		
		row.addColumn(column);
		
		table.setRow(row);
		table.setCaption("");
		
		//设置标题		
		facade.setTable(table);
		
		return facade;
	}
	
}
