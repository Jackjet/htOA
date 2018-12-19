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
import com.kwchina.extend.evaluationvote.entity.PyPersonInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.service.PyItemInforManager;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;
import com.kwchina.extend.evaluationvote.vo.PyItemInforVo;
import com.kwchina.extend.evaluationvote.vo.PyPersonInforVo;

@Controller
@RequestMapping("/extend/pyItemInfor.do")
public class PyItemInforController extends BasicController{
	
	@Resource
	private PyItemInforManager pyItemInforManager;
	@Resource
	private SystemUserManager systemUserManager;
	@Resource
	private PyTopicInforManager pyTopicInforManager;
	
	
	
	
	/**
	 * 编辑条目信息
	 * @param pyPersonInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("pyItemInforVo")
			PyItemInforVo pyItemInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rowId = request.getParameter("rowId");
		
		
		
		Integer topicId = Integer.valueOf(rowId);
		
		pyItemInforVo.setTopicId(topicId);
		
		Integer itemId = pyItemInforVo.getItemId();
	
		if (itemId != null && itemId.intValue() != 0) {
			PyItemInfor item = (PyItemInfor)this.pyItemInforManager.get(itemId);	
			model.addAttribute("_Item", item);
			BeanUtils.copyProperties(pyItemInforVo, item);
			
		}

		//修改
		if (topicId != null && topicId.intValue() != 0) {
			
			PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
			
			model.addAttribute("_Topic", topic);
			
		}
		
		List items= this.pyItemInforManager.getItemOrderBy();
		model.addAttribute("_Items", items);
		
	
		return "evaluationvote/editItemInfor";

	
	}
	
	
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("pyItemInforVo")
			PyItemInforVo pyItemInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		PyItemInfor item = new PyItemInfor();
		
	
		
		Integer topicId = pyItemInforVo.getTopicId();
		
		Integer itemId = pyItemInforVo.getItemId();
		
		if (topicId != null && topicId.intValue() != 0) {
			
			PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
	
		}
		
		
		//修改
		if (itemId != null && itemId.intValue() != 0) {
			item = (PyItemInfor)this.pyItemInforManager.get(itemId);
			BeanUtils.copyProperties(item, pyItemInforVo);
			
			this.pyItemInforManager.save(item);
			//pId = person.getPId();
		}else {
		
			item.setItemName(pyItemInforVo.getItemName());
			item.setItemType(pyItemInforVo.getItemType());
			item.setDisplayOrder(pyItemInforVo.getDisplayOrder());
			
			PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
			item.setTopicInfor(topic);
						
			this.pyItemInforManager.save(item);
					
						//if (i == 0) {
							//parmWorkId = tmpWork.getId();
						//}
				
			}

		return "redirect:/extend/pyItemInfor.do?method=edit&rowId=" + topicId;
	
	}
	
	
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("itemId");
		String rowId = request.getParameter("rowId");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					// 获取被删除讯息
					PyItemInfor item = (PyItemInfor) this.pyItemInforManager.get(deleteId);
				
					this.pyItemInforManager.remove(item);
				}
			}
		}
		return "redirect:/extend/pyItemInfor.do?method=edit&rowId=" + rowId;
	}
}