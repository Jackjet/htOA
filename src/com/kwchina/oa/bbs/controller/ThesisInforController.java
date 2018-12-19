package com.kwchina.oa.bbs.controller;

import java.io.PrintWriter;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.file.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.bbs.entity.ThesisInfor;
import com.kwchina.oa.bbs.service.ThesisInforManager;
import com.kwchina.oa.bbs.vo.CommentInforVo;
import com.kwchina.oa.bbs.vo.ThesisInforVo;
import com.kwchina.oa.sys.SystemConstant;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/bbs/thesisInfor.do")
public class ThesisInforController extends BasicController{

	@Resource
	private ThesisInforManager thesisInforManager;
	
	
	@RequestMapping(params = "method=list")
	public void list(@ModelAttribute("thesisInforVo")
			ThesisInforVo thesisInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		//SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		//from ThesisInfor thesisInfor where 1=1 order by 
		//thesisInfor.topThesis desc,thesisInfor.essence desc,thesisInfor.updateDate  desc,
		//thesisInfor.commentinforsByThesisId.size desc
		queryString[0] = "from ThesisInfor thesisInfor where 1=1 ";
		
		queryString[1] = "select count(thesisId) from ThesisInfor  where 1=1 ";
		
//		condition += "order by thesisInfor.topThesis desc,thesisInfor.essence desc," +
//				"thesisInfor.updateDate  desc,thesisInfor.commentinfor.size desc ";
		
		queryString = this.thesisInforManager.generateQueryString(queryString, getSearchParams(request));
		queryString[0] += condition+" order by thesisInfor.topThesis desc,thesisInfor.essence desc," +
		"thesisInfor.updateDate  desc,thesisInfor.commentinfor.size desc ";;
		queryString[1] += condition;
		
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.thesisInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();


		// 把查询到的结果转化为VO
		List thesisVos = new ArrayList();
		if (list.size() > 0) {

			for (Iterator it = list.iterator(); it.hasNext();) {
				ThesisInfor thesis = (ThesisInfor) it.next();

				// 把查询到的结果转化为VO
				thesisInforVo = this.thesisInforManager.transPOToVO(thesis);
				thesisVos.add(thesisInforVo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		// awareObject.add("sender.person");
		rows = convert.modelCollect2JSONArray(thesisVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	
	@RequestMapping(params = "method=myList")
	public void myList(@ModelAttribute("thesisInforVo")
			ThesisInforVo thesisInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		//from ThesisInfor thesisInfor where 1=1 order by 
		//thesisInfor.topThesis desc,thesisInfor.essence desc,thesisInfor.updateDate  desc,
		//thesisInfor.commentinforsByThesisId.size desc
		queryString[0] = "from ThesisInfor thesisInfor where nickName = '"+user.getUserName()+"' ";
		
		queryString[1] = "select count(thesisId) from ThesisInfor where nickName = '"+user.getUserName()+"' ";
		
//		condition += "order by thesisInfor.topThesis desc,thesisInfor.essence desc," +
//				"thesisInfor.updateDate  desc,thesisInfor.commentinfor.size desc ";
		
		queryString = this.thesisInforManager.generateQueryString(queryString, getSearchParams(request));
		queryString[0] += condition+" order by thesisInfor.topThesis desc,thesisInfor.essence desc," +
		"thesisInfor.updateDate  desc,thesisInfor.commentinfor.size desc ";;
		queryString[1] += condition;
		
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.thesisInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();


		// 把查询到的结果转化为VO
		List thesisVos = new ArrayList();
		if (list.size() > 0) {

			for (Iterator it = list.iterator(); it.hasNext();) {
				ThesisInfor thesis = (ThesisInfor) it.next();

				// 把查询到的结果转化为VO
				thesisInforVo = this.thesisInforManager.transPOToVO(thesis);
				thesisVos.add(thesisInforVo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		// awareObject.add("sender.person");
		rows = convert.modelCollect2JSONArray(thesisVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("thesisInforVo")
			ThesisInforVo thesisInforVo, Model model,  HttpServletRequest request, HttpServletResponse response) throws Exception {

		ThesisInfor thesis = new ThesisInfor();
		
		Integer thesisId = thesisInforVo.getThesisId();
		
		String attachStr = "";
		String imgAttachStr = "";
		
		if (thesisId != null) {
			thesis = (ThesisInfor) this.thesisInforManager.get(thesisId);
			
			attachStr = thesis.getAttachment();
			imgAttachStr = thesis.getImgAttachment();
			
			BeanUtils.copyProperties(thesisInforVo, thesis);
			
			/*//对图片附件进行处理，保存到ThesisInforVo信息中
			String attachmentFile = thesis.getImgAttachment();
			if (attachmentFile != null && (!attachmentFile.equals(""))) {
				//把多余的空白附件过滤掉
				attachmentFile = attachmentFile.replace("||", "|");
				String[] arrayFiles = attachmentFile.split("\\|");
				thesisInforVo.setImgAttachmentArray(arrayFiles);
				
				Map imgMap = new HashMap();
				
				String[] attachmentNames = new String[arrayFiles.length];
				for (int k = 0; k < arrayFiles.length; k++) {
					attachmentNames[k] = File.getFileName(arrayFiles[k]);
					imgMap.put(arrayFiles[k], attachmentNames[k]);
				}
//				Arrays.sort(attachmentNames);
				request.setAttribute("_Attachment_Names", attachmentNames);
				request.setAttribute("_Attachment_Files", arrayFiles);
				request.setAttribute("_ImgMap", imgMap);
			}*/
		}
		request.setAttribute("_AttachStrs", attachStr);
		request.setAttribute("_ImgAttachStrs", imgAttachStr);
		
		request.setAttribute("_Thesis",thesis);
		return "editThesis";
	}
	
	
	@RequestMapping(params = "method=save")
    public void save(ThesisInforVo thesisInforVo, HttpServletRequest request, 
			HttpServletResponse response,DefaultMultipartHttpServletRequest multipartRequest) throws Exception{
		String s1 = request.getParameter("title");
		String s2 = request.getParameter("content");
		ThesisInforVo vo = (ThesisInforVo) thesisInforVo;
		ThesisInfor thesis = new ThesisInfor();
		Integer thesisId = vo.getThesisId();
		long nowTime = System.currentTimeMillis();
		String oldFiles = "";
		//修改
		if (thesisId != null) {
			thesis = (ThesisInfor) this.thesisInforManager.get(thesisId);

			/*//修改信息时，对图片附件进行修改
			String filePaths = thesis.getImgAttachment();
			oldFiles = deleteOldFile(request, filePaths, "imgAttachmentArray");*/
			
		}
		
		String encoding = request.getCharacterEncoding();
		if ((encoding != null) && (encoding.equalsIgnoreCase("utf-8"))) {
		response.setContentType("text/html; charset=gb2312");
		}
		
		/*//页面中已存在的附件
		String oldAtts = vo.getAttachmentArray();
//		thesis.setAttachment(vo.getAttachmentArray()==null ? null : vo.getAttachmentArray());
		if(oldAtts==null || oldAtts.equals("")){
			oldAtts = null;
		}else{
			thesis.setAttachment(oldAtts);
		}
		
		//保存附件
		String attch = request.getParameter("hideAttachment");
//		String[] attchArray = {attch};
		List attchArray = new ArrayList();
		
		if(!"".equals(attch) && null!=attch){
			attchArray.add(attch);
		}
		
		//判断非空
		String attchmentSave = "";
		if(attchArray.size() == 0 || attchArray == null){
			attchmentSave = null;
		}else{
			attchmentSave = saveAttchment(attchArray, SystemConstant.Attch_path ,false);
		}
		
		//判断非空
		if(attchmentSave == null || attchmentSave.equals("")){
			if(oldAtts==null || oldAtts.equals("")){
				attchmentSave = null;
			}else{
				attchmentSave = oldAtts;
			}
		}else{
			if(oldAtts==null || oldAtts.equals("")){
				
			}else{
				attchmentSave = oldAtts + attchmentSave + ",";
			}
		}*/
		
		String uploadifyAttachment = request.getParameter("uploadifyAttachment");
		thesis.setAttachment(uploadifyAttachment);
		
		thesis.setContent(vo.getContent());
		thesis.setTitle(vo.getTitle());
		
		//新增
//		if (thesisId == 0) {
			SystemUserInfor sysUser = SysCommonMethod.getSystemUser(request);
			thesis.setAuthor(sysUser);
			Timestamp updateDate = new java.sql.Timestamp(nowTime);
			thesis.setUpdateDate(updateDate);
			//昵称(保存用户名)
			thesis.setNickName(sysUser.getUserName());
//		}
		
		
		/*//获取图片附件
		String folder = SystemConstant.Img_path ;
		
		
		*//** 图片附件处理 *//*
		List picAttachs = new ArrayList();
		
		String tempImgAttchments = request.getParameter("hideImg");
		if(!"".equals(tempImgAttchments) && tempImgAttchments != null){
			String arg[] = tempImgAttchments.split(",");

			for(int i=0;i<arg.length;i++){
				int ind = tempImgAttchments.indexOf(",");
				String tempPath = "";
				if(ind!=-1){
					tempPath = tempImgAttchments.substring(0, ind);
					if(!"".equals(tempPath)){
						picAttachs.add(tempPath);
					}
					
					tempImgAttchments = tempImgAttchments.substring(ind+1,tempImgAttchments.length());
				}else if(ind==-1){
					
				}
			}
		}
		
		
		
		String imgAttachment = "";
		if(picAttachs.size() == 0 || picAttachs == null){
			imgAttachment = null;
		}else{
			imgAttachment = saveAttchment(picAttachs, folder, false);
		}
		
		
		//String attachment = this.dealAttachmentFile(form, contextPath, folder);
		if(imgAttachment==null || imgAttachment.equals("")){
			if(oldFiles==null || oldFiles.equals("")){
				imgAttachment = null;
			}else{
				imgAttachment = oldFiles;
			}
		}else{
			if(oldFiles==null || oldFiles.equals("")){
				
			}else{
				imgAttachment = imgAttachment+ "|" + oldFiles;
			}				
		}*/
		String uploadifyImgAttachment = request.getParameter("uploadifyImgAttachment");
		thesis.setImgAttachment(uploadifyImgAttachment);
		
		this.thesisInforManager.save(thesis);
		
		PrintWriter out = response.getWriter();
		out.print("<script language='javascript'>");
		out.print("var returnArray = [\"refresh\",\"\"];");
		out.print("window.returnValue = returnArray;");
		out.print("window.close();");
		out.print("</script>");
	}
	
	//删除相关信息
	@RequestMapping(params = "method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {

					Integer thesisId = Integer.valueOf(detleteIds[i]);
					ThesisInfor thesisInfor = (ThesisInfor)this.thesisInforManager.get(thesisId);
					
					//判断是否有删除权限
//					boolean hasRight = this.inforDocumentRightManager.hasRight(inforDocument, systemUser, InforDocumentRight._Right_Delete);
						
					/*//删除该论题下的评论下的图片
	                Collection<CommentInfor> comments = thesisInfor.getCommentinforsByThesisId();
	                for (Iterator it=comments.iterator();it.hasNext();) {
	                	CommentInfor comment = (CommentInfor)it.next();
	                	String commentImgs = comment.getImgAttachment();
	                	deleteFiles(commentImgs);
	                }*/
					
					//删除图片附件
					String defaultPicUrls = thesisInfor.getImgAttachment();
					if(defaultPicUrls == null || defaultPicUrls.equals("")){
						
					}else{
						deleteFiles(defaultPicUrls);
					}
					
					this.thesisInforManager.remove(thesisInfor);
				}
			}
		}
	}
	
	
	//设置论题为置顶
	@RequestMapping(params = "method=setTop")
    public void setTop(HttpServletRequest request, HttpServletResponse response) throws Exception{

    	String rowIds = request.getParameter("rowIds");
    	String topThesisStr = request.getParameter("topThesis");
    	boolean topThesis = false;
    	if (topThesisStr.equals("true")) {
    		topThesis = true;
    	}
    	
    	
    	
//    	if(hasRight){
    		if (rowIds != null && rowIds.length() > 0) {
    			String[] detleteIds = rowIds.split(",");
    			if (detleteIds.length > 0) {
    				for (int i = 0; i < detleteIds.length; i++) {

    					Integer thesisId = Integer.valueOf(detleteIds[i]);
    					ThesisInfor thesisInfor = (ThesisInfor)this.thesisInforManager.get(thesisId);
    					
    					thesisInfor.setTopThesis(topThesis);
    	                this.thesisInforManager.save(thesisInfor);
    				}
    			}
        	}
//    	}else{
//    		response.getWriter().write("您无权进行此操作，请联系管理员！");
//    	}
    	
    	
	}
    
    //为论题加精
	@RequestMapping(params = "method=setEssence")
    public void setEssence(HttpServletRequest request, HttpServletResponse response) throws Exception{

		String rowIds = request.getParameter("rowIds");
		String essenceStr = request.getParameter("essence");
    	boolean essence = false;
    	if (essenceStr.equals("true")) {
    		essence = true;
    	}
    	
    	
    	if (rowIds != null && rowIds.length() > 0) {
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i = 0; i < detleteIds.length; i++) {

					Integer thesisId = Integer.valueOf(detleteIds[i]);
					ThesisInfor thesisInfor = (ThesisInfor)this.thesisInforManager.get(thesisId);
					
					thesisInfor.setEssence(essence);
	                this.thesisInforManager.save(thesisInfor);
				}
			}
    	}
	}
	
	/**
	 * 判断是否是论坛版主，即管理员，用于显示置顶、加精、删除等操作
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=judgeRight")
	public void judgeRight(HttpServletRequest request, HttpServletResponse response) throws Exception{
		SystemUserInfor systemUserInfor = SysCommonMethod.getSystemUser(request);
    	int hasRight = 0;
    	
    	if(systemUserInfor.getUserType() == SystemConstant._User_Type_Admin){
    		hasRight = 1;
    	}
    	
    	response.getWriter().println(hasRight);
	}

}
