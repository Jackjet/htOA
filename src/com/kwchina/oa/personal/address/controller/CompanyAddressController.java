package com.kwchina.oa.personal.address.controller;

import java.sql.Date;
import java.sql.Timestamp;
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
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.AppModuleLog;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.oa.personal.address.entity.CompanyAddress;
import com.kwchina.oa.personal.address.service.CompanyAddressManager;
import com.kwchina.oa.personal.address.vo.CompanyAddressVo;
import com.kwchina.oa.util.SysCommonMethod;

/**
 * 公司通讯录
 * @author Administrator
 *
 */
@Controller
@RequestMapping("/personal/address/companyAddressInfor.do")
public class CompanyAddressController extends BasicController  {
	
	@Resource
	private CompanyAddressManager companyAddressManager;

	@Resource
	private AppModuleLogManager appModuleLogManager;
	
	
	/****
	 * 显示公司通讯录
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception{
		//构造查询语句
		//String[] queryString = this.companyAddressManager.generateQueryString("CompanyAddress", "personId", getSearchParams(request));
		
		//当前登录用户ID
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		String personId = user.getPersonId().toString();
		
		if(StringUtil.isNotEmpty(SysCommonMethod.getPlatform(request))){
			/************记录app模块使用日志************/ 
			AppModuleLog appModuleLog = new AppModuleLog();
			appModuleLog.setModuleName("通讯录");
			appModuleLog.setPlatform(SysCommonMethod.getPlatform(request));
			appModuleLog.setLogTime(new Timestamp(System.currentTimeMillis()));
			appModuleLog.setUserName(user.getUserName());
			this.appModuleLogManager.save(appModuleLog);
			/*****************************************/
		}
		
		
		String[] searchParams = getSearchParams(request);
		
		//构造自定义查询语句
		String[] basicQueryString = new String[2];
		
		basicQueryString[0] = "from CompanyAddress address where 1=1 ";
		basicQueryString[1] = "select count(address.personId) from CompanyAddress address where 1=1 ";
		
		String[] queryString = this.companyAddressManager.generateQueryString(basicQueryString,searchParams);
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.companyAddressManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
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
//		CompanyAddress address = this.companyAddressManager.seeAddress(personId);
		CompanyAddress address = (CompanyAddress)this.companyAddressManager.get(personId);
		request.setAttribute("_message", address);
		return "companyAddressView";
	}
	
	/**
	 * 新增或者修改公司通讯录
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, CompanyAddressVo vo, Model model) throws Exception {
		
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			//vo.setPersonId(Integer.valueOf(rowId));
			CompanyAddress address = (CompanyAddress)this.companyAddressManager.get(Integer.valueOf(rowId));
			
			BeanUtils.copyProperties(vo, address);
			
			
			//出生日期
			request.setAttribute("_Birthday", address.getBirthday());
		}
		
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		int personId = user.getPersonId();
		
		
		//getAddressInfor(request, response, vo, model);
		
		return "editCompanyAddress";
	}
	
	/**
	 * 加载对应详细信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @throws Exception
	 */
	/*private void getAddressInfor(HttpServletRequest request, HttpServletResponse response, CompanyAddressVo vo, Model model) throws Exception {
		
		Integer personId = vo.getPersonId();
		
		if (personId != null && personId.intValue() != 0) {
			//修改时
			CompanyAddress address = (CompanyAddress)this.companyAddressManager.get(personId);
			model.addAttribute("_CompanyAddress", address);
			
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
		
		List addresses = this.companyAddressManager.getAddressOrderById();
		request.setAttribute("_CompanyAddresses",addresses);
	}*/
	
	
	/**
	 * 保存公司通讯录
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, CompanyAddressVo vo) throws Exception {

		//得到ID
		//Integer personId = vo.getPersonId();
		String rowId = request.getParameter("rowId");
		CompanyAddress address = new CompanyAddress();
		//AddressCategory category = new AddressCategory();
		
		//if (personId != null && personId.intValue() != 0) {
		if (rowId != null && rowId.length() > 0) {
			
			address = (CompanyAddress)this.companyAddressManager.get(Integer.valueOf(rowId));
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
		
		//出生日期
		String birthday = request.getParameter("birthday");
		if(birthday != null && birthday.length() > 0){
			Date date = Date.valueOf(birthday);
			address.setBirthday(date);
		}
		
		this.companyAddressManager.save(address);
		
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
					
					this.companyAddressManager.remove(personId);
				}
			}
		}
	}

}
