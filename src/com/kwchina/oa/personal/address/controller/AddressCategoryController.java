package com.kwchina.oa.personal.address.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.personal.address.entity.AddressCategory;
import com.kwchina.oa.personal.address.service.AddressCategoryManager;
import com.kwchina.oa.personal.address.vo.AddressCategoryVo;
import com.kwchina.oa.util.SysCommonMethod;

/**
 * 个人通讯录分类
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/personal/address/AddressCategory.do")
public class AddressCategoryController extends BasicController  {
	
	@Resource
	private AddressCategoryManager addressCategoryManager;

	
	
	/****
	 * 显示通讯录分类
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=getCategoryTree")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//构造查询语句
//		String[] queryString = this.addressCategoryManager.generateQueryString("AddressCategory", "categoryId", getSearchParams(request));
		
		//得到当前登录用户ID
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		String personId = user.getPersonId().toString();
		
		String[] searchParams = getSearchParams(request);
		
		//构造自定义查询语句
		String[] basicQueryString = new String[2];
		basicQueryString[0] = "from AddressCategory where personId = "+personId;
		basicQueryString[1] = "select count(categoryId) from AddressCategory where personId = "+personId;
		
		String[] queryString = this.addressCategoryManager.generateQueryString(basicQueryString,searchParams);
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		
		PageList pl = this.addressCategoryManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		/*JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("category");
		rows = convert.modelCollect2JSONArray(list, new ArrayList());*/
        
		Iterator it = list.iterator();
		while(it.hasNext()){
			AddressCategory category = (AddressCategory)it.next();
			JSONObject obj = new JSONObject();
			obj.put("categoryId", category.getCategoryId());
			obj.put("categoryName", category.getCategoryName());
			obj.put("orderNo", category.getOrderNo());
			obj.put("level", 1);
            obj.put("leaf", true);
            rows.add(obj);
		}
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 获取查询条件数据(分类名称信息)
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=getCategoryName")
	public void getCategoryName(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//当前登录用户ID 
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int personId = user.getPersonId();
		
		//分类信息
		JSONArray categoryArray = new JSONArray();
		List categoryNames = this.addressCategoryManager.getCategoryName(personId);
		categoryArray = convert.modelCollect2JSONArray(categoryNames, new ArrayList());
		jsonObj.put("_CategoryNames", categoryArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	/**
	 * 新增或者修改个人通讯录分类
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, AddressCategoryVo vo, Model model) throws Exception {
		//得到ID
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setCategoryId(Integer.valueOf(rowId));
		}
		
		//当前登录用户ID
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int personId = user.getPersonId();
		
		List categoryNames = this.addressCategoryManager.getCategoryName(personId);
		model.addAttribute("_CategoryNames", categoryNames);
		
		getCategoryInfor(request, response, vo, model);
		
		return "editAddressCategory";
	}
	
	/**
	 * 加载对应详细信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @throws Exception
	 */
	private void getCategoryInfor(HttpServletRequest request, HttpServletResponse response, AddressCategoryVo vo, Model model) throws Exception {
		
		Integer categoryId = vo.getCategoryId();
		
		if (categoryId != null && categoryId.intValue() != 0) {
			//修改时
			AddressCategory addressCategory = (AddressCategory)this.addressCategoryManager.get(categoryId);
			model.addAttribute("_AddressCategory", addressCategory);
		
			vo.setCategoryId(addressCategory.getCategoryId());
			vo.setCategoryName(addressCategory.getCategoryName());
			vo.setOrderNo(addressCategory.getOrderNo());
			vo.setPersonId(addressCategory.getPerson().getPersonId());
		}
		
		List categorys = this.addressCategoryManager.getCategoryOrderById();
	
		request.setAttribute("_AddressCategorys",categorys);
	}
	
	
	/**
	 * 保存分类
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, AddressCategoryVo vo) throws Exception {

		//得到ID
		Integer categoryId = vo.getCategoryId();
		AddressCategory category = new AddressCategory();
		//SystemUserInfor person = new SystemUserInfor();
		
		if (categoryId != null && categoryId.intValue() != 0) {
			
			/*category.setCategoryName(vo.getCategoryName());
			category.setOrderNo(vo.getOrderNo());
			
			person.setPersonId(vo.getPersonId());
			
			category.setPerson(person);*/
			
			category = (AddressCategory)this.addressCategoryManager.get(categoryId);
		}else{
			/*category.setCategoryId(vo.getCategoryId());
			category.setCategoryName(vo.getCategoryName());
			category.setOrderNo(vo.getOrderNo());
			
			person.setPersonId(vo.getPersonId());
			
			category.setPerson(person);*/
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			category.setPerson(systemUser);
		}
		
		//属性从vo到model
		BeanUtils.copyProperties(category, vo);
		
		this.addressCategoryManager.save(category);
		
	}
	
	/*****
	 * 批量删除
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
					Integer categoryId = Integer.valueOf(detleteIds[i]);
					
					this.addressCategoryManager.remove(categoryId);
				}
			}
		}
	}
}
