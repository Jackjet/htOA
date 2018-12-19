package com.kwchina.extend.evaluationvote.controller;

import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.extend.evaluationvote.entity.PyItemInfor;
import com.kwchina.extend.evaluationvote.entity.PyOptionInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.service.PyItemInforManager;
import com.kwchina.extend.evaluationvote.service.PyOptionInforManager;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;
import com.kwchina.extend.evaluationvote.vo.PyItemInforVo;
import com.kwchina.extend.evaluationvote.vo.PyOptionInforVo;

@Controller
@RequestMapping("/extend/pyOptionInfor.do")
public class PyOptionInforController extends BasicController{
	
	@Resource
	private PyOptionInforManager pyOptionInforManager;
	@Resource
	private SystemUserManager systemUserManager;
	@Resource
	private PyTopicInforManager pyTopicInforManager;
	@Resource
	private PyItemInforManager pyItemInforManager;
	
	

	
	
	
	/**
	 * 编辑选项信息
	 * @param pyPersonInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("pyOptionInforVo")
			PyOptionInforVo pyOptionInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rowId = request.getParameter("rowId");
		Integer topicId = Integer.valueOf(rowId);
		
		//String itemIdStr = request.getParameter("rowId");
		//Integer itemId = Integer.valueOf(itemIdStr);
		
		//pyOptionInforVo.setTopicId(topicId);
		
		Integer itemId = pyOptionInforVo.getItemId();
		Integer optionId = pyOptionInforVo.getOptionId();
		
		PyItemInfor item = (PyItemInfor)this.pyItemInforManager.get(itemId);	
		model.addAttribute("_Item", item);
	
		if (optionId != null && optionId.intValue() != 0) {
			PyOptionInfor option = (PyOptionInfor)this.pyOptionInforManager.get(optionId);	
			model.addAttribute("_Option", option);
			BeanUtils.copyProperties(pyOptionInforVo, option);
			
		}

		if (topicId != null && topicId.intValue() != 0) {
			
			PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
			
			model.addAttribute("_Topic", topic);
			
		}
		
		List items= this.pyItemInforManager.getItemOrderBy();
		model.addAttribute("_Items", items);
		
	
		return "evaluationvote/editOptionInfor";

	}

	
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("pyOptionInforVo")
			PyOptionInforVo pyOptionInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		PyOptionInfor option = new PyOptionInfor();
		
	
		
		Integer topicId = Integer.valueOf(request.getParameter("rowId"));
		
		Integer itemId = pyOptionInforVo.getItemId();
		
		Integer optionId = pyOptionInforVo.getOptionId();
		
		if (itemId != null && itemId.intValue() != 0) {
			
			PyItemInfor item = (PyItemInfor)this.pyItemInforManager.get(itemId);
	
		}
		
		
		//修改
		if (optionId != null && optionId.intValue() != 0) {
			option = (PyOptionInfor)this.pyOptionInforManager.get(optionId);
			BeanUtils.copyProperties(option, pyOptionInforVo);
			
			this.pyOptionInforManager.save(option);
			//pId = person.getPId();
		}else {
		
			option.setOptionName(pyOptionInforVo.getOptionName());
			option.setDisplayOrder(pyOptionInforVo.getDisplayOrder());
			option.setSelectNum(pyOptionInforVo.getSelectNum());
			
			PyItemInfor item = (PyItemInfor)this.pyItemInforManager.get(itemId);
			option.setItemInfor(item);
						
			this.pyItemInforManager.save(option);
					
						//if (i == 0) {
							//parmWorkId = tmpWork.getId();
						//}
				
			}

		return "redirect:/extend/pyItemInfor.do?method=edit&rowId=" + topicId;
	
	}
	
	
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("OptionId");
		String rowId = request.getParameter("rowId");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					// 获取被删除讯息
					PyOptionInfor option = (PyOptionInfor) this.pyOptionInforManager.get(deleteId);
				
					this.pyOptionInforManager.remove(option);
				}
			}
		}
		return "redirect:/extend/pyOptionInfor.do?method=edit&rowId=" + rowId;
	}
	
}