package com.kwchina.oa.personal.address.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.personal.address.service.AddressManager;
import com.kwchina.oa.util.HtmlParser;

/**
 * 公共通讯录
 * 
 * @author Administrator
 * 
 */
@Controller
@RequestMapping(value = "/address.do")
public class AddressController extends BasicController {

	@Resource
	private AddressManager addressManager;

	/**
	 * 显示所有人员通信录
	 */ 
	@RequestMapping(params = "method=listAddress")
	public void list(HttpServletRequest request, HttpServletResponse response)throws Exception {
		// 构造查询语句
		String[] queryString = this.addressManager.generateQueryString("PersonInfor", "personId", getSearchParams(request));

		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.addressManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
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
		awareObject.add("department");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	/**
	 * 公司通讯录链接
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=listCompanyAddress")
	public String listCompanyAddress(HttpServletRequest request, HttpServletResponse response)throws Exception {
//		JSONObject jsonObj = new JSONObject();
//		jsonObj.put("_AddressURL", "http://192.168.61.5/txl/htxls_mt.htm");
//		
//		//设置字符编码
//        response.setContentType(CoreConstant.CONTENT_TYPE);
//        response.getWriter().print(jsonObj);
//		String srcStr = HtmlParser.getHtmlContent("http://192.168.61.5/txl/htxls_mt.htm","gb2312");
//		request.setAttribute("_SrcStr", srcStr);
		
		//return "redirect:http://192.168.61.5/txl/htxls_mt.htm";
		//return "redirect:/companyAdr.htm";
		return "redirect:/personal/address/companyAddress.htm";
	}
}
