package com.kwchina.extend.tpwj.controller;

import java.text.DecimalFormat;
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
import com.kwchina.extend.tpwj.entity.ItemInfor;
import com.kwchina.extend.tpwj.entity.OptionInfor;
import com.kwchina.extend.tpwj.entity.TopicInfor;
import com.kwchina.extend.tpwj.entity.VoteInfor;
import com.kwchina.extend.tpwj.entity.VoteItemInfor;
import com.kwchina.extend.tpwj.service.ItemInforManager;
import com.kwchina.extend.tpwj.service.OptionInforManager;
import com.kwchina.extend.tpwj.service.TopicInforManager;
import com.kwchina.extend.tpwj.service.VoteInforManager;
import com.kwchina.extend.tpwj.service.VoteItemInforManager;
import com.kwchina.extend.tpwj.vo.ItemInforVo;
import com.kwchina.extend.tpwj.vo.OptionInforVo;
import com.kwchina.extend.tpwj.vo.TopicInforVo;
import com.kwchina.extend.tpwj.vo.VoteInforVo;
import com.kwchina.oa.document.entity.DocumentInfor;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/tpwj/voteInfor.do")
public class VoteInforController extends BasicController{

	@Resource
	private TopicInforManager topicInforManager;
	
	@Resource
	private VoteInforManager voteInforManager;
	
	@Resource
	private VoteItemInforManager voteItemInforManager;
	
	@Resource
	private ItemInforManager itemInforManager;
	
	@Resource
	private OptionInforManager optionInforManager;
	
	
	
	/***
	 * 保存投票信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response, VoteInforVo vo) throws Exception {

		TopicInfor topic = (TopicInfor)this.topicInforManager.get(vo.getTopicId());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		/**************首先保存voteInfor**************/
		VoteInfor voteInfor = new VoteInfor();
		
		// 创建时间默认为当前时间
		Date createDate = new Date(System.currentTimeMillis());
		voteInfor.setVoteTime(createDate);
		voteInfor.setVoter(systemUser);
		voteInfor.setTopic(topic);
		//分数在最后保存
		VoteInfor newVoteInfor = (VoteInfor)this.voteInforManager.save(voteInfor);
		
		float score = 0f;
		/**************再保存详细条目信息**************/
		Set<ItemInfor> items = topic.getItems();
		for(ItemInfor item : items){
			int itemId = item.getItemId();
			
			VoteItemInfor voteItemInfor = new VoteItemInfor();
			voteItemInfor.setVoteInfor(newVoteInfor);
			voteItemInfor.setItem(item);
			
			if(item.getItemType() == 0){//单选
				String selectId = request.getParameter("item_"+itemId);
				
				
				//保存option的值
				if(selectId != null && !selectId.equals("")){
					voteItemInfor.setVoteValue(selectId+",");
					OptionInfor optionInfor = (OptionInfor)this.optionInforManager.get(Integer.valueOf(selectId));
					voteItemInfor.setVoteContent(optionInfor.getOptionValue()+",");
				}
				
				//计算分数
				if(item.getScore() > 0 && selectId != null && !selectId.equals("")){
					OptionInfor option = (OptionInfor)this.optionInforManager.get(Integer.valueOf(selectId));
					//score += item.getScore()*option.getRation();
					score += option.getScore();
				}
			}else if(item.getItemType() == 1 || item.getItemType() == 4){//多选(包括图片型)
				String[] selectIds = request.getParameterValues("item_"+itemId);
				String ids = "";
				String contents = "";
				if(selectIds != null && selectIds.length > 0){
					for(int i=0;i<selectIds.length;i++){
						String id = selectIds[i];
						ids += id +",";
						
						//保存option的值
						if(id != null && !id.equals("")){
							OptionInfor optionInfor = (OptionInfor)this.optionInforManager.get(Integer.valueOf(id));
							contents += optionInfor.getOptionValue()+",";
						}
						
						//计算分数
						if(item.getScore() > 0 && id != null && !id.equals("")){
							OptionInfor option = (OptionInfor)this.optionInforManager.get(Integer.valueOf(id));
							//score += item.getScore()*option.getRation();
							score += option.getScore();
						}
					}
				}
				
				voteItemInfor.setVoteValue(ids);
				voteItemInfor.setVoteContent(contents);
			}else if(item.getItemType() == 2 || item.getItemType() == 3){//文本或者段落
				String textValue = request.getParameter("item_"+itemId);
				voteItemInfor.setVoteText(textValue);
			}else if(item.getItemType() == 5){//列表型
				
			}
			
			this.voteItemInforManager.save(voteItemInfor);
		}

		DecimalFormat df = new DecimalFormat("#.00");
		//String scoreString = df.format(score);
		score = Float.valueOf(df.format(score));
		newVoteInfor.setVoteScore(score);
		this.voteInforManager.save(newVoteInfor);
		
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
				attachment = attachment + "|" + oldFiles;
			}
		}

		// 保存附件
		optionInfor.setPicPath(attachment);

		this.optionInforManager.save(optionInfor);
		
		//return "redirect:itemInfor.do?method=editOption&topicId="+vo.getTopicId();
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
