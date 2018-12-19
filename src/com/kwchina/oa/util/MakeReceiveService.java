package com.kwchina.oa.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.activation.DataHandler;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.axis.transport.http.AxisServlet;
import org.apache.cxf.transport.servlet.ServletController;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.sys.MyApplicationContextUtil;
import com.kwchina.oa.workflow.customfields.service.CommonManager;
import com.kwchina.oa.workflow.customfields.util.CnToSpell;
import com.kwchina.oa.workflow.customfields.util.FlowConstant;
import com.kwchina.oa.workflow.entity.FlowDefinition;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.service.FlowDefinitionManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;

import freemarker.template.Template;

public class MakeReceiveService {
	
	/*private ServletContext context = getRequest().getSession().getServletContext();    

	public void setContext(ServletContext context){
		this.context = context;
	}*/
	/*private ServletContext context; 
	private WebApplicationContext ctx;
	
	public void init() throws ServletException {            
		super.init();  
		context = this.getServletContext();    
		ctx = WebApplicationContextUtils.getWebApplicationContext(context);  
	}     
*/
	
	public void doMakeReceive(Map<String, Object[]> formDataMap,DataHandler[] dhs,String[] attachNames){  
		try {
			
			/*//从容器中得到注入的bean 
			ServletContext context = this.getServletContext();    
			WebApplicationContext ctx = WebApplicationContextUtils.getWebApplicationContext(context);  */
//			WebApplicationContext applicationContext = WebApplicationContextUtils.getWebApplicationContext(context);
			
//			ApplicationContext ac = new FileSystemXmlApplicationContext("WEB-INF/config/applicationContext.xml");
			/*ServletContext context = null;
			ApplicationContext ac = WebApplicationContextUtils.getRequiredWebApplicationContext(context);*/
//			ApplicationContext ac = MyApplicationContextUtil.getContext();
//			ClassPathXmlApplicationContext cp = new ClassPathXmlApplicationContext("applicationContext.xml");
//			BeanFactory cp = new ClassPathXmlApplicationContext("classpath:/applicationContext*.xml");  

			String attachPaths = "";
			//处理附件
			for (int i = 0; i < dhs.length; ++i){
			    //file = new File(CoreConstant.Context_Real_Path + CoreConstant.Attachment_Path + "workflow-r" + savePath + "/" + recDH.getName());
				FileInputStream fileInput = null;
				FileOutputStream fileOutput = null;
				try {
					DataHandler recDH = dhs[i];
					
					long current = System.currentTimeMillis();
					String savePath = "/" + current;
					String filePath = CoreConstant.Context_Real_Path + CoreConstant.Attachment_Path;
					
					File file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					filePath += "workflow-r";
					file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					filePath += savePath + "/";
					file = new File(filePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					fileInput = new FileInputStream(String.valueOf(recDH.getName())); 
					filePath += attachNames[i];
					fileOutput = new FileOutputStream(filePath);
					int bytesRead = 0;
					byte[] buffer = new byte[8192];
					while ((bytesRead = fileInput.read(buffer, 0, 8192)) != -1) {
						fileOutput.write(buffer, 0, bytesRead);
					}
					
					attachPaths += filePath.substring(filePath.indexOf(CoreConstant.Attachment_Path)) + "|";
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}finally{
					fileOutput.flush();
					fileOutput.close();
					fileInput.close();
				}
//			    java.io.File receivedFile = new java.io.File(recDH.getName()); //文件生成
//			    System.out.println(com.kwchina.core.util.File.getFileName(recDH.getName()+"++++"+receivedFile.getAbsolutePath())+"==================");
			}
			
			if (!attachPaths.equals("")){
				attachPaths = attachPaths.substring(0, attachPaths.length()-1);
			}
			
			SystemUserManager systemUserManager = (SystemUserManager)MyApplicationContextUtil.getBean("systemUserManager");
			FlowDefinitionManager flowManager = (FlowDefinitionManager)MyApplicationContextUtil.getBean("flowManager");
			CommonManager commonManager = (CommonManager)MyApplicationContextUtil.getBean("commonManager");
			FlowInstanceManager flowInstanceManager = (FlowInstanceManager)MyApplicationContextUtil.getBean("flowInstanceManager");

			int flowId = Integer.valueOf(formDataMap.get("flowId")[0].toString());
			FlowInstanceInfor instance = new FlowInstanceInfor();
			FlowDefinition flow = (FlowDefinition)flowManager.get(Integer.valueOf(flowId));
			
			//标题
			instance.setInstanceTitle(formDataMap.get("instanceTitle")[0].toString());
			
			instance.setOldInstanceId(0);
			
			//创建时间
			Timestamp sysTime = new Timestamp(System.currentTimeMillis());
			instance.setUpdateTime(sysTime);
			
			//申请人、所属部门
			SystemUserInfor applier = (SystemUserInfor)systemUserManager.get(CoreConstant.User_Receive);
			instance.setApplier(applier);
			instance.setDepartment(applier.getPerson().getDepartment());
			
			//所属流程
			instance.setFlowDefinition(flow);
			
			//主办人
			instance.setCharger(flow.getCharger());
			
			//附件
			instance.setAttach(attachPaths);
			
			//收文号
			String documentNo = "";
			SimpleDateFormat sfY = new SimpleDateFormat("yyyy");
			
			String serialNo = "";
			
			String tableName = "Customize_shouwenguanli";
			String fieldName = "serialNo";
			String fieldYear = "reportYear";
			String reportYear = sfY.format(new Date());
			String sql = "select " + fieldName + " from " + tableName + " where " + fieldYear + " = '" + reportYear + "' and " + fieldName + " is not null ";
				   sql+=" and instanceId not in(select instanceId from Workflow_InstanceInfor where deleteFlag = 1) group by " + fieldName + " order by " + fieldName;
			List maxSerialNos = flowInstanceManager.getResultBySQLQuery(sql);
			if (maxSerialNos != null && maxSerialNos.size() > 0) {
				String maxSerialNo = (String)maxSerialNos.get(maxSerialNos.size()-1);
				
				Integer maxSerialNoInt = 1;
				if(maxSerialNo != null && !maxSerialNo.equals("")){
					maxSerialNoInt = Integer.valueOf(maxSerialNo)+1;
				}
				
				String zero = "";
				for(int i=0;i<3-maxSerialNoInt.toString().length();i++) {
					zero += "0";
				}
				serialNo = zero + maxSerialNoInt.toString();
			}
			documentNo = "HT-SW-"+reportYear+"-"+serialNo;
			formDataMap.put("serialNo", new String[]{serialNo});
			formDataMap.put("documentNo", new String[]{documentNo});
			
			
			//部门审核人
			SystemUserInfor manager = (SystemUserInfor)systemUserManager.get(CoreConstant.User_Receive_Manager);
			instance.setManager(manager);
			instance.setManagerChecked(false);
			instance.setManagerWord(null);
			instance.setCheckTime(null);
			instance.setManagerAttachment(null);
			
			//附件
//			DefaultMultipartHttpServletRequest multipartRequest = (DefaultMultipartHttpServletRequest)formDataMap.get("attachment");
//			BasicController basicController = new BasicController();
//			String attachment = basicController.uploadAttachment(multipartRequest, "workflow");
//			instance.setAttach(attachment);
			
			//收文相关字段
			/** 通过自定义表单模板生成html */
			String templateName = null;
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				//模板路径
				String[] templateArray = flow.getTemplate().split("/");
				templateName = templateArray[templateArray.length-1];
				Template template = commonManager.getTemplate(templateName, FlowConstant.Flow_FormTemplate_Path, true);
					
				//根据时间定义文件名
				Calendar calendar = Calendar.getInstance();
				String fileName = String.valueOf(calendar.getTimeInMillis()) + ".html";
				
				//删除原来的html页面
				String oldHtmlPath = CoreConstant.Context_Real_Path + instance.getContentPath();
				java.io.File htmlFile = new java.io.File(oldHtmlPath);
				if(htmlFile.exists()) {
					htmlFile.delete();
				}
			
				
				//保存新的html文件
				String filePath = CoreConstant.Context_Real_Path + FlowConstant.Flow_Html_Path;	//生成的html文件保存路径
				com.kwchina.core.util.File fileOperator = new com.kwchina.core.util.File();
				File ioFile = new File(filePath);
				fileOperator.createFilePath(ioFile);
				String htmlPath = filePath + "/" + fileName;
				FileOutputStream fos = new FileOutputStream(htmlPath);
				Writer wOut = new OutputStreamWriter(fos, CoreConstant.ENCODING);
				Map map = new HashMap();
				map.put("base", CoreConstant.Context_Real_Path);	//工程所在路径
				map.put("tagValue", false);	//在模板中用于区分是新增/修改还是生成html
				
				formDataMap.remove("instanceId");
				formDataMap.remove("flowId");
				formDataMap.remove("instanceTitle");
				map.put("formValues", formDataMap);	//从页面取得的值(键值对的方式)
				map.put("_GLOBAL_PERSON", applier);
				map.put("_InstanceId", 0);
				template.process(map, wOut);
				wOut.flush();
				wOut.close();
				instance.setContentPath(FlowConstant.Flow_Html_Path +  "/" + fileName);
			}
			
			instance = (FlowInstanceInfor)flowInstanceManager.save(instance);
			
			if (flow.getTemplate() != null && flow.getTemplate().length() > 0) {
				//根据模板获取自定义表单数据
				List fieldNames = commonManager.getFieldNames(templateName);
				String[] fieldValues = new String[fieldNames.size()];
				for (int i=0;i<fieldNames.size();i++) {
//					Object[] tmpArray = formDataMap.get(fieldNames.get(i).toString());
					fieldValues[i] = formDataMap.get(fieldNames.get(i).toString())[0].toString();
					System.out.println("fieldValues["+i+"]======"+fieldValues[i]);
				}
				
				//将汉字的流程名处理为拼音
				String flowName = CnToSpell.getFullSpell(flow.getFlowName());

				//新增或修改自定义表单数据
				//新增
				commonManager.updateFormData(flowName, fieldNames, fieldValues, instance.getInstanceId(), 0);
			}
			System.out.println("=========创建收文成功！========");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("=========创建收文失败！========");
			e.printStackTrace();
		} 
		
		
		//return "我说呢";
	} 

}
