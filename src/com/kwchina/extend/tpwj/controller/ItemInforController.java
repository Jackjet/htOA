package com.kwchina.extend.tpwj.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.tpwj.entity.ItemInfor;
import com.kwchina.extend.tpwj.entity.OptionInfor;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.service.ItemInforManager;
import com.kwchina.extend.tpwj.service.OptionInforManager;
import com.kwchina.extend.tpwj.service.TopicInforManager;
import com.kwchina.extend.tpwj.vo.ItemInforVo;
import com.kwchina.extend.tpwj.vo.OptionInforVo;
import com.kwchina.extend.tpwj.vo.TopicInforVo;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/tpwj/itemInfor.do")
public class ItemInforController extends BasicController{

	@Resource
	private TopicInforManager topicInforManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private RoleManager roleManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private ItemInforManager itemInforManager;
	
	@Resource
	private OptionInforManager optionInforManager;
	
	
	
	/***
	 * 修改或新增条目信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, ItemInforVo vo, Model model) throws Exception {
		//首先将此主题下的所有条目及选项找出，列在编辑表单页面
		int topicId = vo.getTopicId();
		TopicInfor topicInfor = new TopicInfor();
		if(topicId > 0){
			topicInfor = (TopicInfor)this.topicInforManager.get(topicId);
		}
		request.setAttribute("_TopicInfor", topicInfor);
		
		
		String rowId = request.getParameter("rowId");
		if (rowId != null && rowId.length() > 0) {
			vo.setItemId(Integer.valueOf(rowId));
		}
		Integer itemId = vo.getItemId();
		
		//修改
		if (itemId != null && itemId.intValue() != 0) {
			
			ItemInfor itemInfor = (ItemInfor) this.itemInforManager.get(itemId.intValue());

			BeanUtils.copyProperties(vo, itemInfor);
//			vo.setTopicId(itemInfor.getTopic().getTopicId());
						
			// 对附件信息进行处理
			String attachmentFile = itemInfor.getPicPath();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}
		
		//得到当前topic下所有item的categoryName，以供选择使用
		String categoryNameSql = "select distinct(categoryName) from ItemInfor item where item.topic.topicId="+topicId;
		List categoryList = this.itemInforManager.getResultByQueryString(categoryNameSql);
		request.setAttribute("_CategoryList", categoryList);
		
		
		//判断是否是在题目前插入,如果是,则把此次新增的displayOrder设为此,把大于等于此值的条目的displayOrder都 加1
		String displayOrder = request.getParameter("orderNo");
		if(StringUtil.isNotEmpty(displayOrder)){
			vo.setDisplayOrder(Integer.valueOf(displayOrder));
			vo.setCategoryName(request.getParameter("category"));
			
			String queryString = "from ItemInfor item where item.displayOrder >= " + Integer.valueOf(displayOrder);
			List infors = this.itemInforManager.getResultByQueryString(queryString);
			for(Iterator it = infors.iterator();it.hasNext();){
				ItemInfor tmpItem = (ItemInfor)it.next();
				
				tmpItem.setDisplayOrder(tmpItem.getDisplayOrder() + 1);
				this.itemInforManager.save(tmpItem);
			}
			
			request.setAttribute("_Insert", true);
		}else {
			request.setAttribute("_Insert", false);
		}

		
		return "/extend/tpwj/editItemInfor";
	}
	
	/***
	 * 保存条目信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, ItemInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		boolean isAdd = true;
		ItemInfor itemInfor = new ItemInfor();
		String oldFiles = "";
		if (vo.getItemId() != null && vo.getItemId().intValue() != 0) {
			itemInfor = (ItemInfor) this.itemInforManager.get(vo.getItemId());
			isAdd = false;
			
			// 修改信息时,对附件进行修改
			String filePaths = itemInfor.getPicPath();
			oldFiles = deleteOldFile(request, filePaths, "picAttach");
		}
		// 上传附件
		String attachment = this.uploadAttachment(multipartRequest, "item");
		
		BeanUtils.copyProperties(itemInfor, vo);
		
		TopicInfor topic = (TopicInfor)this.topicInforManager.get(vo.getTopicId());
		itemInfor.setTopic(topic);
		

		// 对附件信息的判断
		if (attachment == null || attachment.equals("")) {
			attachment = oldFiles;
		} else {
			if (oldFiles == null || oldFiles.equals("")) {
				// attachment = attachment;
			} else {
				//attachment = attachment + "|" + oldFiles;
			}
		}

		// 保存附件
		itemInfor.setPicPath(attachment);

		ItemInfor newItemInfor = (ItemInfor)this.itemInforManager.save(itemInfor);
		

		//在新增item时，如果是文本或者段落型时，直接保存其一个option
		if(isAdd && (vo.getItemType() == 2 || vo.getItemType() == 3)){
			OptionInfor optionInfor = new OptionInfor();
			optionInfor.setDisplayOrder(1);
			optionInfor.setItem(newItemInfor);
			this.optionInforManager.save(optionInfor);
		}
		
		return "redirect:itemInfor.do?method=edit&topicId="+vo.getTopicId();
	}
	
	/**
	 * 删除条目信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String detleteId = request.getParameter("itemId");
		if (detleteId != null && detleteId.length() > 0) {

			Integer itemId = Integer.valueOf(detleteId);
			ItemInfor itemInfor = (ItemInfor) this.itemInforManager.get(itemId);

			// 删除附件
			String filePaths = itemInfor.getPicPath();
			deleteFiles(filePaths);

			this.itemInforManager.remove(itemInfor);
		}
		
		return "redirect:itemInfor.do?method=edit&topicId="+request.getParameter("topicId");
	}
	
	/***
	 * 修改或新增选项信息
	 * @param request
	 * @param response
	 * @param vo
	 * @param model
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params="method=editOption")
	public String editOption(HttpServletRequest request, HttpServletResponse response, OptionInforVo vo, Model model) throws Exception {
		
		int itemId = vo.getItemId();
		ItemInfor itemInfor = new ItemInfor();
		if(itemId > 0){
			itemInfor = (ItemInfor)this.itemInforManager.get(itemId);
		}
		request.setAttribute("_ItemInfor", itemInfor);
		
		
		
		String rowId = request.getParameter("optionId");
		if (rowId != null && rowId.length() > 0) {
			vo.setOptionId(Integer.valueOf(rowId));
		}
		Integer optionId = vo.getOptionId();
		
		//修改
		if (optionId != null && optionId.intValue() != 0) {
			
			OptionInfor optionInfor = (OptionInfor) this.optionInforManager.get(optionId.intValue());

			BeanUtils.copyProperties(vo, optionInfor);
			
			// 对附件信息进行处理
			String attachmentFile = optionInfor.getPicPath();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
						
		}
		

		
		return "/extend/tpwj/editOptionInfor";
	}
	
	/***
	 * 保存选项信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=saveOption")
	public void saveOption(HttpServletRequest request, HttpServletResponse response, OptionInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		OptionInfor optionInfor = new OptionInfor();
		String oldFiles = "";
		if (vo.getOptionId() != null && vo.getOptionId().intValue() != 0) {
			optionInfor = (OptionInfor) this.optionInforManager.get(vo.getOptionId());
			
			// 修改信息时,对附件进行修改
			String filePaths = optionInfor.getPicPath();
			oldFiles = deleteOldFile(request, filePaths, "picAttach");
		}
		// 上传附件
		String attachment = this.uploadAttachment(multipartRequest, "option");
		
		BeanUtils.copyProperties(optionInfor, vo);
		
		ItemInfor item = (ItemInfor)this.itemInforManager.get(vo.getItemId());
		optionInfor.setItem(item);

		// 对附件信息的判断
		if (attachment == null || attachment.equals("")) {
			attachment = oldFiles;
		} else {
			if (oldFiles == null || oldFiles.equals("")) {
				
			} else {
				//attachment = attachment + "|" + oldFiles;
			}
		}

		// 保存附件
		optionInfor.setPicPath(attachment);

		this.optionInforManager.save(optionInfor);
		
		//return "redirect:itemInfor.do?method=editOption&topicId="+vo.getTopicId();
	}
	
	/**
	 * 保存选项复制源
	 * @param request
	 * @param response
	 * @return
	 */
	@RequestMapping(params="method=saveCopy")
	public void saveCopy(HttpServletRequest request, HttpServletResponse response) {
		try {
			String sourceIdStr = request.getParameter("sourceId");
			String targetIdStr = request.getParameter("targetId");
			
			if(sourceIdStr != null && !sourceIdStr.equals("") && targetIdStr != null && !targetIdStr.equals("")){
				ItemInfor sourceItem = (ItemInfor)this.itemInforManager.get(Integer.valueOf(sourceIdStr));
				ItemInfor targetItem = (ItemInfor)this.itemInforManager.get(Integer.valueOf(targetIdStr));
				Set<OptionInfor> sourceOptions = sourceItem.getOptions();
				
				for(OptionInfor sourceOp : sourceOptions){
					OptionInfor targetOp = new OptionInfor();
					targetOp.setOptionName(sourceOp.getOptionName());
					targetOp.setOptionValue(sourceOp.getOptionValue());
					targetOp.setDisplayOrder(sourceOp.getDisplayOrder());
					targetOp.setScore(sourceOp.getScore());
					targetOp.setPicPath(sourceOp.getPicPath());
					targetOp.setItem(targetItem);
					this.optionInforManager.save(targetOp);
				}
				
				response.getWriter().print("success");
			}else {
				response.getWriter().print("fail");
			}
		} catch (Exception e) {
			try {
				response.getWriter().print("fail");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
		
	}
	
	/**
	 * 删除选项信息
	 * 
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteOption")
	public String deleteOption(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String detleteId = request.getParameter("optionId");
		if (detleteId != null && detleteId.length() > 0) {

			Integer optionId = Integer.valueOf(detleteId);
			OptionInfor optionInfor = (OptionInfor) this.optionInforManager.get(optionId);

			// 删除附件
			String filePaths = optionInfor.getPicPath();
			deleteFiles(filePaths);

			this.optionInforManager.remove(optionInfor);
		}
		return "redirect:itemInfor.do?method=edit&topicId="+request.getParameter("topicId");
	}
	

}
