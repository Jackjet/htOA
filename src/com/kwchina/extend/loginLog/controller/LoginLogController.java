package com.kwchina.extend.loginLog.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanComparator;
import org.apache.commons.collections.ComparatorUtils;
import org.apache.commons.collections.comparators.ComparableComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.entity.LoginLog;
import com.kwchina.extend.loginLog.service.LoginLogManager;
import com.kwchina.extend.loginLog.vo.LoginLogFormed;
import com.kwchina.extend.loginLog.vo.LoginLogVo;

@Controller
@RequestMapping(value="/extend/loginLog.do")
public class LoginLogController extends BasicController {

	
	@Autowired
	private LoginLogManager loginLogManager;

	@Autowired
	private SystemUserManager systemUserManager;
	
	
	public JSONObject getInstances(HttpServletRequest request, HttpServletResponse response, boolean isExcel){
		JSONObject jsonObj = new JSONObject();
		
		//构造查询语句
		//String[] queryString = this.personManager.generateQueryString("PersonInfor", "personId", getSearchParams(request));
		
		String[] queryString = new String[2];
		queryString[0] = "from LoginLog log where 1=1";
		queryString[1] = "select count(logId) from LoginLog log where 1=1";
		
		
		queryString = this.loginLogManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.loginLogManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        
                  
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
		
		return jsonObj;
	}
	

	//显示所有
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = getInstances(request, response, false);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 导出excel
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=expertExcel")
	public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			JSONObject jsonObj = getInstances(request, response, false);
			
			JSONArray listArray = jsonObj.getJSONArray("rows");
			
			List<LoginLogFormed> infors = (List)JSONArray.toCollection(listArray, LoginLogFormed.class);
			
			//转为以用户名为索引，计数总次数
			List<LoginLogVo> vos = new ArrayList<LoginLogVo>();
			
			//用户名
			Set<String> userNameSet = new HashSet<String>();
			for(LoginLogFormed tmpLog : infors){
				if(StringUtil.isNotEmpty(tmpLog.getUserName())){
					userNameSet.add(tmpLog.getUserName());
				}
				
			}
			
			for(String tmpUserName : userNameSet){
				LoginLogVo vo = new LoginLogVo();
				vo.setUserName(tmpUserName);
				
				int eachCount = 0;
				
				for(LoginLogFormed tmpLog : infors){
					if(StringUtil.isNotEmpty(tmpLog.getUserName())){
						if(tmpLog.getUserName().equals(tmpUserName) && tmpLog.getSucTag() == 1){
							eachCount++;
						}
					}
					
				}
				
				vo.setLogCount(eachCount);
				
				vos.add(vo);
			}
			
			//按照orderNo排序
	 		//Collections.sort(vos, new BeanComparator("logCount"));
			Comparator mycmp = ComparableComparator.getInstance();       
	        mycmp = ComparatorUtils.nullLowComparator(mycmp);  //允许null       
	        mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序       
	        Comparator cmp = new BeanComparator("logCount", mycmp);    
	        Collections.sort(vos, cmp);
			
			
			/******************导出Excel********************/
			long time = System.currentTimeMillis();
//			String filePath = SystemConstant.Submit_Path + time + "/";
			String filePath = "/"+CoreConstant.Attachment_Path + "loginLog/";
			String fileTitle = "登录日志";

			ExcelObject object = new ExcelObject();
			object.setFilePath(filePath);
			object.setFileName(fileTitle);
			object.setTitle(fileTitle);

			List rowName = new ArrayList();
			String[][] data = new String[4][vos.size()];
			int k = 0;// 列数

			rowName.add("序号");
			rowName.add("登录用户");
			rowName.add("次数");
			k = 4;

			for (int i = 0; i < vos.size(); i++) {
				LoginLogVo tmpVo = (LoginLogVo)vos.get(i);

				data[0][i] = String.valueOf(i + 1);
				data[1][i] = tmpVo.getUserName();
				data[2][i] = String.valueOf(tmpVo.getLogCount());
				
			}

			for (int i = 0; i < k; i++) {
				object.addContentListByList(data[i]);
			}
			object.setRowName(rowName);
			ExcelOperate operate = new ExcelOperate();
			try {
				operate.exportExcel(object, vos.size(), true, request);
			} catch (IOException e) {
				e.printStackTrace();
			}

			filePath = filePath + fileTitle + ".xls";
//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path_login");
			request.getSession().setAttribute("_File_Path_login", filePath);
			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download_login";
	}
	
	
	
//	/**
//	 * 编辑
//	 * @param request
//	 * @param response
//	 * @param vo
//	 * @param model
//	 * @return
//	 * @throws Exception
//	 */
//	@RequestMapping(params="method=edit")
//	public String edit(HttpServletRequest request, HttpServletResponse response, AppImgVo vo, Model model) throws Exception {
//
//		Integer imgId = vo.getImgId();
//		AppImg appImg = new AppImg();
//		
//		if (imgId != null && imgId.intValue() > 0) {
//			//编辑
//			appImg = (AppImg)this.appImgManager.get(imgId);	
//			
//			//属性从model到vo
//			BeanUtils.copyProperties(vo, appImg);			
//			
//			
//			// 对附件信息进行处理
//			String attachmentFile = appImg.getAttachment();
//			if (attachmentFile != null && !attachmentFile.equals("")) {
//				String[][] attachment = processFile(attachmentFile);
//				request.setAttribute("_Attachment_Names", attachment[1]);
//				request.setAttribute("_Attachments", attachment[0]);
//			}
//		}
//		
//		
//		return "base/editAppImg";
//	}
//	
//	/**
//	 * 保存
//	 * @param request
//	 * @param response
//	 * @param vo
//	 * @param multipartRequest
//	 * @throws Exception
//	 */
//	@RequestMapping(params="method=save")
//	public void save(HttpServletRequest request, HttpServletResponse response, AppImgVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
//
//		try {
//			Integer imgId = vo.getImgId();
//			AppImg appImg = new AppImg();
//			long nowTime = System.currentTimeMillis();
//			
//			String oldFiles = "";
//			
//			if (imgId != null && imgId.intValue() != 0) {
//				//修改
//				appImg = (AppImg)this.appImgManager.get(imgId);		
//				
//				// 修改信息时,对附件进行修改
//				String filePaths = appImg.getAttachment();
//				oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
//			}else {
//				//创建日期
//				appImg.setCreateDate(new Date(nowTime));
//			}
//			
//			// 上传附件
//			String attachment = this.uploadAttachment(multipartRequest, "app");
//			
//			BeanUtils.copyProperties(appImg, vo);	
//			
//			//作者
//			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
//			appImg.setAuthor(systemUser);
//			
//			//更新日期
//			appImg.setUpdateDate(new Date(nowTime));
//			
//			// 对附件信息的判断
//			if (attachment == null || attachment.equals("")) {
//				attachment = oldFiles;
//			} else {
//				if (oldFiles == null || oldFiles.equals("")) {
//					// attachment = attachment;
//				} else {
////					attachment = attachment + "|" + oldFiles;
//					attachment = attachment;
//				}
//			}
//
//			// 保存附件
//			appImg.setAttachment(attachment);
//			
//			this.appImgManager.save(appImg);
//		} catch (RuntimeException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//	}
//	
//	
//	/**
//	 * 删除
//	 * @param request
//	 * @param response
//	 * @throws Exception
//	 */
//	@RequestMapping(params="method=delete")
//	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
//		
//		String rowIds = request.getParameter("rowIds");
//		if (rowIds != null && rowIds.length() > 0) {
//			
//			String[] detleteIds = rowIds.split(",");
//			if (detleteIds.length > 0) {
//				for (int i=0;i<detleteIds.length;i++) {
//					Integer imgId = Integer.valueOf(detleteIds[i]);
//					AppImg appImg = (AppImg)this.appImgManager.get(imgId);
//					//删除
//					this.appImgManager.remove(appImg);
//				}
//			}
//		}
//		
//	}
	

}
