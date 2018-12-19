package com.kwchina.core.base.controller;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.AppImg;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.AppImgManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.base.vo.AppImgVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping(value="/core/app.do")
public class AppImgController extends BasicController {

	
	@Autowired
	private AppImgManager appImgManager;

	@Autowired
	private SystemUserManager systemUserManager;
	

	//显示所有
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//构造查询语句
		//String[] queryString = this.personManager.generateQueryString("PersonInfor", "personId", getSearchParams(request));
		
		String[] queryString = new String[2];
		queryString[0] = "from AppImg appImg where 1=1";
		queryString[1] = "select count(imgId) from AppImg appImg where 1=1";

		queryString = this.appImgManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.appImgManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
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
		awareObject.add("author");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);

	}
	
	
	
	
	/**
	 * 编辑
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, AppImgVo vo, Model model) throws Exception {

		Integer imgId = vo.getImgId();
		AppImg appImg = new AppImg();
		
		if (imgId != null && imgId.intValue() > 0) {
			//编辑
			appImg = (AppImg)this.appImgManager.get(imgId);	
			
			//属性从model到vo
			BeanUtils.copyProperties(vo, appImg);			
			
			
			// 对附件信息进行处理
			String attachmentFile = appImg.getAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}
		
		
		return "base/editAppImg";
	}
	
	/**
	 * 保存
	 * @param request
	 * @param response
	 * @param vo
	 * @param multipartRequest
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, AppImgVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		try {
			Integer imgId = vo.getImgId();
			AppImg appImg = new AppImg();
			long nowTime = System.currentTimeMillis();
			
			String oldFiles = "";
			
			if (imgId != null && imgId.intValue() != 0) {
				//修改
				appImg = (AppImg)this.appImgManager.get(imgId);		
				
				// 修改信息时,对附件进行修改
				String filePaths = appImg.getAttachment();
				oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			}else {
				//创建日期
				appImg.setCreateDate(new Date(nowTime));
			}
			
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "app");
			
			BeanUtils.copyProperties(appImg, vo);	
			
			//作者
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
			appImg.setAuthor(systemUser);
			
			//更新日期
			appImg.setUpdateDate(new Date(nowTime));
			
			// 对附件信息的判断
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
//					attachment = attachment + "|" + oldFiles;
					attachment = attachment;
				}
			}

			// 保存附件
			appImg.setAttachment(attachment);
			
			this.appImgManager.save(appImg);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
	
	
	/**
	 * 删除
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i=0;i<detleteIds.length;i++) {
					Integer imgId = Integer.valueOf(detleteIds[i]);
					AppImg appImg = (AppImg)this.appImgManager.get(imgId);
					//删除
					this.appImgManager.remove(appImg);
				}
			}
		}
		
	}
	

}
