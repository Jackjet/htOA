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

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.extend.evaluationvote.entity.PyPersonInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.service.PyPersonInforManager;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;
import com.kwchina.extend.evaluationvote.vo.PyPersonInforVo;


@Controller
@RequestMapping("/extend/pyPersonInfor.do")
public class PyPersonInforController extends BasicController{
	
	@Resource
	private PyPersonInforManager pyPersonInforManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private PyTopicInforManager pyTopicInforManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	
	/**
	 * 编辑评优人员
	 * @param pyPersonInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("pyPersonInforVo")
			PyPersonInforVo pyPersonInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rowId = request.getParameter("rowId");
		
		
		
		Integer topicId = Integer.valueOf(rowId);
		Integer pId = 0;
		if(request.getParameter("pId")!=null){
			String pIdStr = request.getParameter("pId");
			 pId = Integer.valueOf(pIdStr);
		}
		if (pId != null && pId.intValue() != 0) {
			PyPersonInfor person = (PyPersonInfor)this.pyPersonInforManager.get(pId);	
			model.addAttribute("_Person", person);
			BeanUtils.copyProperties(pyPersonInforVo, person);
			
		}

		//修改
		if (topicId != null && topicId.intValue() != 0) {
			
			PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
			
			model.addAttribute("_Topic", topic);
			
		}
		
		List persons= this.pyPersonInforManager.getPersonOrderBy();
		model.addAttribute("_Persons", persons);
		
		//根据职级获取用户
		List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);		
		model.addAttribute("_Users", users);
		
		//获取职级大于一定值的用户
		List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);		
		model.addAttribute("_OtherUsers", otherUsers);
		
		//全部部门信息
		List departments = this.organizeManager.getDepartments();
		model.addAttribute("_Departments", departments);

		return "evaluationvote/editPersonInfor";
		
			
	}
	
	@RequestMapping(params = "method=save")
	public String save(@ModelAttribute("pyPersonInforVo")
			PyPersonInforVo pyPersonInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		
		PyPersonInfor person = new PyPersonInfor();
		
		String rowId = request.getParameter("rowId");
		
		Integer topicId = Integer.valueOf(rowId);
		
		Integer pId = pyPersonInforVo.getPId();
		PyTopicInfor topic =new PyTopicInfor();
		if (topicId != null && topicId.intValue() != 0) {
			
			 topic = (PyTopicInfor)this.pyTopicInforManager.get(topicId);
	
		}
		
		
		//修改
		if (pId != null && pId.intValue() != 0) {
			person = (PyPersonInfor)this.pyPersonInforManager.get(pId);
			BeanUtils.copyProperties(person, pyPersonInforVo);
			
			
			pId = person.getPId();
		}else {
			person.setDisplayOrder(pyPersonInforVo.getDisplayOrder());
			person.setDepartment(pyPersonInforVo.getDepartment());
			person.setPersonName(pyPersonInforVo.getPersonName());
			person.setDescrip(pyPersonInforVo.getDescrip());
			person.setTopicInfor(topic);
		}
		this.pyPersonInforManager.save(person);
		
		
		return "redirect:/extend/pyPersonInfor.do?method=edit&rowId=" + rowId;
	
	}
	
	
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("pId");
		String rowId = request.getParameter("rowId");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);
					// 获取被删除讯息
					PyPersonInfor person = (PyPersonInfor) this.pyPersonInforManager.get(deleteId);
				
					this.pyTopicInforManager.remove(person);
				}
			}
		}
		return "redirect:/extend/pyPersonInfor.do?method=edit&rowId=" + rowId;
	}
	
	
}