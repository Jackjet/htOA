package com.kwchina.oa.submit.controller;

import java.io.IOException;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.submit.util.SubmitConstant;
import com.kwchina.oa.workflow.controller.WorkflowBaseController;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.service.FlowInstanceManager;

@Controller
@RequestMapping("/workflow/submit.do")
public class SubmitController extends WorkflowBaseController {
	
	@Resource
	private FlowInstanceManager flowInstanceManager;
	
	//获取最大流水号
	@RequestMapping(params = "method=getSerialNo")
	public void getSerialNo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		JSONObject jsonObj = new JSONObject();
		String serialNo = "001";
		//String serialNo = "1";
		//response.setCharacterEncoding("utf-8");
		//request.setCharacterEncoding("utf-8");
		String tableName = request.getParameter("tableName");
		String fieldName = request.getParameter("fieldName");
		String categoryName =request.getParameter("categoryName");
		
		//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
		String fieldYear = request.getParameter("fieldYear");
		String reportYear = request.getParameter("reportYear");
		String sql = "select " + fieldName + " from " + tableName + " where " + fieldYear + " = '" + reportYear + "' and " + fieldName + " is not null ";
		if(categoryName != null){	   
		sql+="and category='"+categoryName+"'";
		}
			   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by " + fieldName + " order by " + fieldName;
		List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
		if (maxSerialNos != null && maxSerialNos.size() > 0) {
			String maxSerialNo = (String)maxSerialNos.get(maxSerialNos.size()-1);
			Integer maxSerialNoInt = Integer.valueOf(maxSerialNo)+1;
			String zero = "";
			for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
				zero += "0";
			}
			serialNo = zero + maxSerialNoInt.toString();
			//serialNo = maxSerialNoInt.toString();
		}
		jsonObj.put("serialNo", serialNo);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	//获取最大流水号
	@RequestMapping(params = "method=getHTSerialNo")
	public void getHTSerialNo(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		JSONObject jsonObj = new JSONObject();
		String serialNo = "001";
		//String serialNo = "1";
		//response.setCharacterEncoding("utf-8");
		//request.setCharacterEncoding("utf-8");
		String tableName = request.getParameter("tableName");
		String fieldName = "indexNo";
		String categoryName =request.getParameter("categoryName");
		
		//String categoryName = new String(request.getParameter("categoryName").getBytes("ISO-8859-1"), "utf-8");
		String fieldYear = request.getParameter("fieldYear");
		String reportYear = request.getParameter("reportYear");
		if(categoryName.equals("物流")){
			categoryName="WL";
		}else if (categoryName.equals("码头")){
			categoryName="MT";
		}
		String indexNo=categoryName+reportYear;
		String sql = "select " + fieldName + " from " + tableName + " where indexNo like '%" + indexNo + "%' and indexNo is not null ";
	
			   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by indexNo order by indexNo desc";
		List maxSerialNos = this.flowInstanceManager.getResultBySQLQuery(sql);
		if (maxSerialNos != null && maxSerialNos.size() > 0) {
			String maxSerialNo = (String)maxSerialNos.get(0);
			
			String documentLastNo = maxSerialNo.substring(6, 9);
			Integer maxSerialNoInt = Integer.valueOf(documentLastNo)+1;
			String zero = "";
			for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
				zero += "0";
			}
			serialNo = zero + maxSerialNoInt.toString();
			//serialNo = maxSerialNoInt.toString();
		}
		jsonObj.put("serialNo", serialNo);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	//打印公文
	@RequestMapping(params = "method=printSubmit")
	public String printSubmit(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String returnPage = "/submit/printSubmit";
		
		//获取审核实例相关信息
		String instanceId = request.getParameter("instanceId");
		FlowInstanceInfor instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
		
		FlowDefinition flow = instance.getFlowDefinition();
		
		if(flow.getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Contract){
			String sqlc ="select contractNo from Customize_Hetongshenpi where instanceId="+instanceId;
			List nowListc = this.flowInstanceManager.getResultBySQLQuery(sqlc);
			
			String contractNo =(String)nowListc.get(0);
			request.setAttribute("_ContractNo", contractNo);
		}
		
		getProcessInfors(request, response, instance);
		
		//董事会文件
		if (instance.getFlowDefinition().getFlowId().intValue() == SubmitConstant.SubmitFlow_Report_Board) {
			returnPage = "/submit/printReportBoard";
		}
		
		return returnPage;
		
	}
	
}