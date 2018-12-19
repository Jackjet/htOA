package com.kwchina.oa.workflow.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;

@Controller
@RequestMapping("/workflow/customizeForm.do")
public class CustomizeFormController {
	
	@Resource
	private FlowDefinitionManager flowManager;
	
	@Resource
	private CommonManager commonManager;
	
	
	//获取流程对应的自定义表单
	@RequestMapping(params = "method=getCustomizeForm")
	public String getCustomizeForm(HttpServletRequest request, HttpServletResponse response, Model model) {
		
		String flowId = request.getParameter("flowId");
		String returnFtl = null;
		if (flowId != null && flowId.length() > 0) {
			FlowDefinition flow = (FlowDefinition)this.flowManager.get(Integer.valueOf(flowId));
			
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				model.addAttribute("base", request.getContextPath());	//工程所在路径
				model.addAttribute("tagValue", true);	//在模板中用于区分是新增/修改还是生成html
				String instanceId = request.getParameter("instanceId");
				model.addAttribute("_InstanceId", instanceId);
				
				if (instanceId != null && instanceId.length() > 0) {
					//根据流程名获取表列名
					String flowName = CnToSpell.getFullSpell(flow.getFlowName());
					List cols = this.commonManager.getColumnNames(flowName);
					
					//获取审核实例的相关数据返回到修改页面
					List formDatas = this.commonManager.getFormData(flowName, Integer.valueOf(instanceId));
					if (formDatas != null && formDatas.size() > 0) {
						Map<Object, Object> map = new HashMap<Object, Object>();
						Object[] obj = (Object[]) formDatas.get(0);
						for (int i=0;i<obj.length;i++) {
							map.put(cols.get(i), obj[i]);
						}
						model.addAttribute("map", map);	//从数据库取得的值
					}
				}
				
				//跳转模板
				String[] templateArray = flow.getTemplate().split("/");
				String templateName = templateArray[templateArray.length-1];
				returnFtl = templateName.replace(".ftl", "");
			}		
		}
		
		return returnFtl;
	}
	
	
}