package com.kwchina.oa.personal.address.controller;

import java.sql.Date;
import java.util.ArrayList;
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
import com.kwchina.oa.personal.address.entity.PersonalAddress;
import com.kwchina.oa.personal.address.service.AddressCategoryManager;
import com.kwchina.oa.personal.address.service.PersonalAddressManager;
import com.kwchina.oa.personal.address.vo.PersonalAddressVo;
import com.kwchina.oa.util.SysCommonMethod;

/**
 * 个人通讯录
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/personal/address/personalAddressInfor.do")
public class PersonalAddressController extends BasicController  {
	
	@Resource
	private PersonalAddressManager personalAddressManager;
	
	@Resource
	private AddressCategoryManager addressCategoryManager;

	
	
	/****
	 * 显示个人全部通讯录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//构造查询语句
		//String[] queryString = this.personalAddressManager.generateQueryString("PersonalAddress", "personId", getSearchParams(request));
		
		//当前登录用户ID
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		String personId = user.getPersonId().toString();
		String[] searchParams = getSearchParams(request);
		
		//构造自定义查询语句
		String[] basicQueryString = new String[2];
		
		basicQueryString[0] = "from PersonalAddress address where address.category.person.personId = "+personId;
		basicQueryString[1] = "select count(address.personId) from PersonalAddress address where address.category.person.personId = "+personId;
		
		String[] queryString = this.personalAddressManager.generateQueryString(basicQueryString,searchParams);
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.personalAddressManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("category");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	/****
	 * 分类显示通讯录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=categoryList")
	public void categoryList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//当前登录用户ID
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		String personId = user.getPersonId().toString();
		String[] searchParams = getSearchParams(request);
		
		//得到点击的分类ID
		String categoryId = request.getParameter("categoryId");
		
		//构造自定义查询语句
		String[] basicQueryString = new String[2];
		
		basicQueryString[0] = "from PersonalAddress address where address.category.person.personId = "+personId+" and address.category.categoryId="+categoryId;
		basicQueryString[1] = "select count(address.personId) from PersonalAddress address where address.category.person.personId = "+personId+" and address.category.categoryId="+categoryId;
		
		String[] queryString = this.personalAddressManager.generateQueryString(basicQueryString,searchParams);
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.personalAddressManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("category");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	/******
	 * 查看选中的通讯录详细信息
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewAddress")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int personId = Integer.parseInt(request.getParameter("rowId"));
//		PersonalAddress address = this.personalAddressManager.seeAddress(personId);
		PersonalAddress address = (PersonalAddress)this.personalAddressManager.get(personId);
		request.setAttribute("_message", address);
		return "personalAddressView";
	}
	
	/**
	 * 新增或者修改个人通讯录
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, PersonalAddressVo vo, Model model) throws Exception {
		
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			//vo.setPersonId(Integer.valueOf(rowId));
			PersonalAddress address = (PersonalAddress)this.personalAddressManager.get(Integer.valueOf(rowId));
			
			BeanUtils.copyProperties(vo, address);
			
			//所属分类
			vo.setCategoryId(address.getCategory().getCategoryId());
			
			//出生日期
			request.setAttribute("_Birthday", address.getBirthday());
		}
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int personId = user.getPersonId();
		
		//得到所登录用户所拥有的分类
		List categorys = this.addressCategoryManager.getCategoryName(personId);
		model.addAttribute("_Categorys", categorys);
		
		//getAddressInfor(request, response, vo, model);
		
		return "editPersonalAddress";
	}
	
	/**
	 * 加载对应详细信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @throws Exception
	 */
	/*private void getAddressInfor(HttpServletRequest request, HttpServletResponse response, PersonalAddressVo vo, Model model) throws Exception {
		
		Integer personId = vo.getPersonId();
		
		if (personId != null && personId.intValue() != 0) {
			//修改时
			PersonalAddress address = (PersonalAddress)this.personalAddressManager.get(personId);
			model.addAttribute("_PersonalAddress", address);
			
			vo.setPersonId(address.getPersonId());
			vo.setCategoryId(address.getCategory().getCategoryId());
			vo.setPersonName(address.getPersonName());
			vo.setPosition(address.getPosition());
			vo.setMobile(address.getMobile());
			vo.setEmail(address.getEmail());
			vo.setGender(address.getGender());
			
			Date bir = address.getBirthday();
			if(bir==null){
				vo.setBirthday(null);
			}else{
				vo.setBirthday(bir.toString());
			}
			
			vo.setMemo(address.getMemo());
			vo.setOfficeAddress(address.getOfficeAddress());
			vo.setOfficePhone(address.getOfficePhone());
			vo.setOfficeCode(address.getOfficeCode());
			vo.setHomeAddress(address.getHomeAddress());
			vo.setHomePhone(address.getHomePhone());
			vo.setPostCode(address.getPostCode());
		}
		
		List addresses = this.personalAddressManager.getAddressOrderById();
		request.setAttribute("_PersonalAddresses",addresses);
	}*/
	
	
	/**
	 * 保存个人通讯录
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, PersonalAddressVo vo) throws Exception {

		//得到ID
		//Integer personId = vo.getPersonId();
		String rowId = request.getParameter("rowId");
		PersonalAddress address = new PersonalAddress();
		//AddressCategory category = new AddressCategory();
		
		//if (personId != null && personId.intValue() != 0) {
		if (rowId != null && rowId.length() > 0) {
			
			address = (PersonalAddress)this.personalAddressManager.get(Integer.valueOf(rowId));
			vo.setPersonId(address.getPersonId());
			
			/*category.setCategoryId(vo.getCategoryId());
			address.setCategory(category);
			
			address.setPersonName(vo.getPersonName());
			address.setPosition(vo.getPosition());
			address.setMobile(vo.getMobile());
			address.setEmail(vo.getEmail());
			address.setGender(vo.getGender());
			
			String birthday = vo.getBirthday();
			if(birthday == null || birthday.length() == 0){
				address.setBirthday(null);
			}else{
				Date date = Date.valueOf(birthday);
				address.setBirthday(date);
			}
			
			address.setMemo(vo.getMemo());
			address.setOfficeAddress(vo.getOfficeAddress());
			address.setOfficePhone(vo.getOfficePhone());
			address.setOfficeCode(vo.getOfficeCode());
			address.setHomeAddress(vo.getHomeAddress());
			address.setHomePhone(vo.getHomePhone());
			address.setPostCode(vo.getPostCode());*/
		}else{
			/*address.setPersonId(vo.getPersonId());
			category.setCategoryId(vo.getCategoryId());
			address.setCategory(category);
			
			address.setPersonName(vo.getPersonName());
			address.setPosition(vo.getPosition());
			address.setMobile(vo.getMobile());
			address.setEmail(vo.getEmail());
			address.setGender(vo.getGender());
			
			String birthday = vo.getBirthday();
			if(birthday == null || birthday.length() == 0){
				address.setBirthday(null);
			}else{
				Date date = Date.valueOf(birthday);
				address.setBirthday(date);
			}
			
			address.setMemo(vo.getMemo());
			address.setOfficeAddress(vo.getOfficeAddress());
			address.setOfficePhone(vo.getOfficePhone());
			address.setOfficeCode(vo.getOfficeCode());
			address.setHomeAddress(vo.getHomeAddress());
			address.setHomePhone(vo.getHomePhone());
			address.setPostCode(vo.getPostCode());*/
		}
		
		//属性从vo到model
		BeanUtils.copyProperties(address, vo);
		
		//所属分类
		AddressCategory category = (AddressCategory)this.addressCategoryManager.get(vo.getCategoryId());
		address.setCategory(category);
		
		//出生日期
		String birthday = request.getParameter("birthday");
		if(birthday != null && birthday.length() > 0){
			Date date = Date.valueOf(birthday);
			address.setBirthday(date);
		}
		
		this.personalAddressManager.save(address);
		
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
					Integer personId = Integer.valueOf(detleteIds[i]);
					
					this.personalAddressManager.remove(personId);
				}
			}
		}
	}

}
