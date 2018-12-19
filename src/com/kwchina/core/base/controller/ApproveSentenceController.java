package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.ApproveSentence;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.ApproveSentenceManager;
import com.kwchina.core.base.vo.ApproveSentenceVo;
import com.kwchina.core.base.vo.RoleInforVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;

@Controller
@RequestMapping("/core/approveSentence.do")
public class  ApproveSentenceController  extends BasicController{
	@Resource
	private ApproveSentenceManager approveSentenceManager;
	//	显示所有惯用语
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		//构造查询语句
		String[] queryString = this.approveSentenceManager.generateQueryString("ApproveSentence", "sentenceId", getSearchParams(request));

		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.approveSentenceManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
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
	public String edit(HttpServletRequest request, HttpServletResponse response, ApproveSentenceVo vo, Model model) throws Exception {

		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setSentenceId(Integer.valueOf(rowId));
		}
		Integer sentenceId = vo.getSentenceId();
		
		//修改
		if (sentenceId != null && sentenceId.intValue() != 0) {
			

	
			ApproveSentence approveSentence = (ApproveSentence)this.approveSentenceManager.get(sentenceId);						
			
			//属性,从model到vo
			BeanUtils.copyProperties(vo, approveSentence);
			

		}		
		

		return "base/editApproveSentence";
	}
	
	//保存信息
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, ApproveSentenceVo vo) throws Exception {

		ApproveSentence approveSentence = new ApproveSentence();
		
					
		BeanUtils.copyProperties(approveSentence, vo);
		
	
		this.approveSentenceManager.save(approveSentence);
	}
	

	//删除信息
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response, ApproveSentenceVo vo) throws Exception {
		
		String detleteId = request.getParameter("rowId");
		if (detleteId != null && detleteId.length() > 0) {
			Integer sentenceId = Integer.valueOf(detleteId);


			
			this.approveSentenceManager.remove(sentenceId);
		}
	}
	
}
