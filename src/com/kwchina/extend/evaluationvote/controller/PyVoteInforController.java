package com.kwchina.extend.evaluationvote.controller;

import java.sql.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.extend.evaluationvote.entity.PyItemInfor;
import com.kwchina.extend.evaluationvote.entity.PyPersonInfor;
import com.kwchina.extend.evaluationvote.entity.PyTopicInfor;
import com.kwchina.extend.evaluationvote.entity.PyVoteInfor;
import com.kwchina.extend.evaluationvote.entity.PyVoteItemInfor;
import com.kwchina.extend.evaluationvote.service.PyPersonInforManager;
import com.kwchina.extend.evaluationvote.service.PyTopicInforManager;
import com.kwchina.extend.evaluationvote.service.PyVoteInforManager;
import com.kwchina.extend.evaluationvote.service.PyVoteItemInforManager;
import com.kwchina.extend.evaluationvote.vo.PyVoteInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/extend/pyVoteInfor.do")
public class PyVoteInforController extends BasicController{
	
	@Resource
	private PyVoteInforManager pyVoteInforManager;
	
	@Resource
	private PyTopicInforManager pyTopicInforManager;
	
	@Resource
	private PyVoteItemInforManager pyVoteItemInforManager;
	
	@Resource
	private PyPersonInforManager pyPersonInforManager;
	
	@Resource
	private PersonInforManager personInforManager;
	
	
	/**
	 * 批量删除评优主题
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewpy")
		public String viewpy(HttpServletRequest request, HttpServletResponse response, PyVoteInforVo vo, Model model) throws Exception {
		String rowId = request.getParameter("rowId");
		vo.setTopicId(Integer.valueOf(rowId));
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		PersonInfor person = (PersonInfor)this.personInforManager.get(user.getPersonId());
		model.addAttribute("_Person", person);
		PyTopicInfor topic = new PyTopicInfor();
		if (rowId != null && rowId.length() > 0) {

					 topic = (PyTopicInfor) this.pyTopicInforManager.get(Integer.valueOf(rowId));
					model.addAttribute("_Topic", topic);
			}
		
		Date sysDate = new Date(System.currentTimeMillis());
		Date startTime = topic.getStartTime();
		Date endTime = topic.getEndTime();
		boolean isInDate = false;
		if(sysDate.getTime()>=startTime.getTime() && sysDate.getTime()<= endTime.getTime()){
			isInDate= true;
			
			request.setAttribute("_IsInDate", isInDate);
			
		}
		String voteInforSql = "from PyVoteInfor voteInfor where voteInfor.person.personId="+user.getPersonId()+" and voteInfor.topicInfor.topicId="+Integer.valueOf(rowId);
		List voteInforList = this.pyVoteInforManager.getResultByQueryString(voteInforSql);
		if(voteInforList.size() > 0){
			request.setAttribute("_VoteInfor", voteInforList.get(0));
			request.setAttribute("_IsVote", true);
		}
		
		return "evaluationvote/viewPy";
	}
	
	/***
	 * 保存投票信息
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, PyVoteInforVo vo) throws Exception {

		PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(vo.getTopicId());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		/**************首先保存voteInfor**************/
		PyVoteInfor voteInfor = new PyVoteInfor();
		
		// 创建时间默认为当前时间
		Date createDate = new Date(System.currentTimeMillis());
		voteInfor.setVoteTime(createDate);
		voteInfor.setPerson(systemUser);
		voteInfor.setTopicInfor(topic);
		//分数在最后保存
		PyVoteInfor newVoteInfor = (PyVoteInfor)this.pyVoteInforManager.save(voteInfor);
		
	
		/**************再保存详细条目信息**************/
		Set<PyPersonInfor> persons = topic.getPersonInfors();
		Set<PyItemInfor> items = topic.getItemInfors();
		
		for(PyPersonInfor person : persons){
			int pId = person.getPId();
			//PyPersonInfor personInfor = (PyPersonInfor)this.pyPersonInforManager.get(pId);
			for(PyItemInfor item : items){
				int itemId = item.getItemId();
				
				PyVoteItemInfor voteItemInfor = new PyVoteItemInfor();
				voteItemInfor.setVoteInfor(newVoteInfor);
				voteItemInfor.setItemInfor(item);
				voteItemInfor.setPersonInfor(person);
				
				if(item.getItemType() == 0){//单选
					String selectId = request.getParameter("option_"+itemId+"_"+pId);
					if (selectId == null ){
						continue;
					}
					voteItemInfor.setVoteValue(selectId);
					
				
				}else if(item.getItemType() == 1){//多选
					String[] selectIds = request.getParameterValues("option_"+itemId+"_"+pId);
					String ids = "";
					if (selectIds == null ){
						continue;
					}
					for(String id : selectIds){
						ids += id+",";
	
					}
					voteItemInfor.setVoteValue(ids);
					
				}
				
				this.pyVoteItemInforManager.save(voteItemInfor);
			}

		}
		return "redirect:/extend/pyVoteInfor.do?method=viewpy&rowId=" + vo.getTopicId();
	}
	
	
	@RequestMapping(params="method=checkPy")
	@ResponseBody
	public Map<String, Object> checkPy(HttpServletRequest request, HttpServletResponse response , PyVoteInforVo vo) {
		String rowId = request.getParameter("rowId");
		vo.setTopicId(Integer.valueOf(rowId));
		Map<String, Object> map = new HashMap<String, Object>();
		//PyTopicInfor topic = (PyTopicInfor)this.pyTopicInforManager.get(vo.getTopicId());
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		
		List list = this.pyVoteInforManager.getVoteByPerson(vo.getTopicId(),systemUser.getPersonId().intValue());
	if(list.size()> 0 ){
		map.put("flag", false);
		map.put("message", "您已经投过票了!");
	}else{
		map.put("flag", true);
		map.put("message", "评优投票成功!");
	}
		
		return map;
	}
	
}