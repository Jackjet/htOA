package com.kwchina.oa.workflow.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ArrayUtil;
import com.kwchina.oa.workflow.entity.FlowInstanceInfor;
import com.kwchina.oa.workflow.entity.InstanceCheckInfor;
import com.kwchina.oa.workflow.entity.InstanceLayerInfor;
import com.kwchina.oa.workflow.service.FlowCheckInforManager;
import com.kwchina.oa.workflow.service.FlowInstanceManager;
import com.kwchina.oa.workflow.service.FlowLayerInforManager;
import com.kwchina.oa.workflow.vo.InstanceLayerInforVo;

@Controller
@RequestMapping("/workflow/layerInfor.do")
public class LayerInforController extends WorkflowBaseController {
	
	@Resource
	private FlowLayerInforManager flowLayerInforManager;
	
	@Resource
	private FlowCheckInforManager flowCheckInforManager;
	
	@Resource
	private FlowInstanceManager flowInstanceManager;
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private RoleManager roleManager;
	
	
	
	//编辑审核层信息
	@RequestMapping(params = "method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, InstanceLayerInforVo vo) throws Exception {
		
		Integer layerId = vo.getLayerId();
		FlowInstanceInfor instance = null;
		boolean partUser = true;	//用于判断:显示可选审核人时是部分显示还是全部显示
		InstanceLayerInfor layerInfor = new InstanceLayerInfor();
		if (layerId != null && layerId.intValue() > 0) {
			//修改
			layerInfor = (InstanceLayerInfor)this.flowLayerInforManager.get(layerId);

			BeanUtils.copyProperties(vo, layerInfor);
			
			//获取审核人信息
			Set checkInfors = layerInfor.getCheckInfors();
			int[] checkerIds = new int[checkInfors.size()];
			int i=0;
			int otherUsrNum = 0;
			for (Iterator it=checkInfors.iterator();it.hasNext();) {
				InstanceCheckInfor checkInfor = (InstanceCheckInfor)it.next();
				checkerIds[i] = checkInfor.getChecker().getPersonId().intValue();
				i++;

				//判断是否存在常用审核角色之外的用户
				if (otherUsrNum == 0) {
					Set roles = checkInfor.getChecker().getRoles();
					if (roles == null || roles.size() == 0) {
						otherUsrNum++;
					}else {
						boolean notHave = true;
						for (Iterator itR=roles.iterator();itR.hasNext();) {
							RoleInfor role = (RoleInfor)itR.next();
							Integer roleId = role.getRoleId();
							if (roleId == CoreConstant.Role_Checker_Leader || roleId == CoreConstant.Role_Checker_DepManager
									|| roleId == CoreConstant.Role_Checker_Party || roleId == CoreConstant.Role_Checker_HR
									|| roleId == CoreConstant.Role_Checker_BackupOne || roleId == CoreConstant.Role_Checker_BackupTwo
									|| roleId == CoreConstant.Role_Checker_FileManager || roleId == CoreConstant.Role_Checker_QDManager
									|| roleId == CoreConstant.Role_Checker_DivisionLeader || roleId == CoreConstant.Role_Checker_LeaderFile ) {
								notHave = false;
							}
						}
						if (notHave) {
							otherUsrNum++;
						}
					}
				}
			}
			vo.setPersonIds(checkerIds);
			request.setAttribute("_CheckerIds", checkerIds);
			
			if (otherUsrNum > 0) {
				//如果存在常用审核角色之外的用户,则将partUser置为false
				partUser = false;
			}
			
			//获取分叉的父层
			InstanceLayerInfor forkedLayer = layerInfor.getForkedLayer();
			request.setAttribute("_ForkedLayer", forkedLayer);
			
			instance = layerInfor.getInstance();
			
		}else {
			//新增
			String instanceId = request.getParameter("instanceId");
			if (instanceId != null && instanceId.length() > 0) {
				instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			}
			
			//获取所有可以选择的FromLayer
			List fromLayers = this.flowLayerInforManager.getFromLayers(instance);
			request.setAttribute("_FromLayers", fromLayers);
		}
		
		//获取审核实例相关信息
		getProcessInfors(request, response, instance);
		
		//获取部门信息
		List departments = this.organizeManager.getDepartments();
		request.setAttribute("_Departments", departments);
		
		//根据职级获取用户
		List users = this.systemUserManager.getUserByPosition(PersonInfor._Position_Tag);
		request.setAttribute("_Users", users);

		//获取职级大于一定值的用户
		List otherUsers = this.systemUserManager.getOtherPositionUser(PersonInfor._Position_Tag);
		request.setAttribute("_OtherUsers", otherUsers);
		
		/** 如果设置了常用审核角色,则取该角色下的用户,否则取全部用户信息 */
		int flowId = instance.getFlowDefinition().getFlowId().intValue();
		String roleIds = "";
		if((flowId>89 && flowId<101)) {
			roleIds = CoreConstant.Role_Checker_FileManager + "," + CoreConstant.Role_Checker_DepManager + "," + CoreConstant.Role_Checker_QDManager + ","
					+ CoreConstant.Role_Checker_DivisionLeader + "," + CoreConstant.Role_Checker_LeaderFile;
		}else if (flowId == 86){
			roleIds = CoreConstant.Role_Checker_DepManager  + ","
					+ CoreConstant.Role_Checker_DivisionLeader + "," + CoreConstant.Role_Checker_LeaderFile + "," + CoreConstant.Role_Checker_Law;
		}else {
			roleIds = CoreConstant.Role_Checker_Leader + "," + CoreConstant.Role_Checker_DepManager + "," + CoreConstant.Role_Checker_HR + ","
					+ CoreConstant.Role_Checker_BackupOne + "," + CoreConstant.Role_Checker_BackupTwo+ "," + CoreConstant.Role_Checker_Law;
		}

		List roles = this.roleManager.getRoles(roleIds);

		//如果审核层为公司总经理或者副总经理  只显示公司领导这个角色
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getFlowNode().getNodeId()==169 || layerInfor.getFlowNode().getNodeId()==174){
					if((flowId>89 && flowId<101) || flowId == 86) {
						String roleIds_Leader = CoreConstant.Role_Checker_LeaderFile + "";
						roles = this.roleManager.getRoles(roleIds_Leader);
					}else {
						String roleIds_Leader = CoreConstant.Role_Checker_Leader + "";
						roles = this.roleManager.getRoles(roleIds_Leader);
					}
				}
			}
		}
		//如果审核层为分管领导  只显示分管领导这个角色
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getFlowNode().getNodeId()==168 ){
					String roleIds_Leader = CoreConstant.Role_Checker_DivisionLeader+"";
					roles = this.roleManager.getRoles(roleIds_Leader);
				}
			}
		}
		String layerName =layerInfor.getLayerName();
/*---------------------------  ---体系文件审核人-------------------  ------------*/
		//如果审核层为文件流转审核人
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getLayerName().equals("文件流转审核人")){
					String roleIds_File = CoreConstant.Role_Checker_FileManager+"";
					roles = this.roleManager.getRoles(roleIds_File);
				}
			}
		}
		//如果审核层为会签层
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getLayerName().equals("会签层")){
					String roleIds_File = CoreConstant.Role_Checker_DepManager+"";
					roles = this.roleManager.getRoles(roleIds_File);
				}
			}
		}
		//如果审核层为安质部审核
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getLayerName().equals("安质部审核")){
					String roleIds_File = CoreConstant.Role_Checker_QDManager+"";
					roles = this.roleManager.getRoles(roleIds_File);
				}
			}
		}
		//如果审核层为分管领导
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getLayerName().equals("分管领导")){
					String roleIds_File = CoreConstant.Role_Checker_DivisionLeader+"";
					roles = this.roleManager.getRoles(roleIds_File);
				}
			}
		}
		//如果审核层为公司领导
		if (layerId != null && layerId.intValue() > 0) {
			if(layerInfor.getFlowNode()!=null){
				if(layerInfor.getLayerName().equals("公司领导")){
					String roleIds_File = CoreConstant.Role_Checker_LeaderFile+"";
					roles = this.roleManager.getRoles(roleIds_File);
				}
			}
		}
/*------------------------------体系文件审核人------------------------------------*/
		request.setAttribute("_Roles", roles);
		request.setAttribute("_PartUser", partUser);
		request.setAttribute("_LayerName",layerName);
		
		return "editLayerInfor";
	}
	
	//保存审核层信息
	@RequestMapping(params = "method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, InstanceLayerInforVo vo) throws Exception {

		Integer layerId = vo.getLayerId();
		InstanceLayerInfor layerInfor = new InstanceLayerInfor();
		FlowInstanceInfor instance = null;
		String instanceId = null;
		
		if (layerId != null && layerId.intValue() > 0) {
			layerInfor = (InstanceLayerInfor)this.flowLayerInforManager.get(layerId);
			instance = layerInfor.getInstance();
			instanceId = instance.getInstanceId().toString();
		}else {
			//新增
			instanceId = request.getParameter("instanceId");
			if (instanceId != null && instanceId.length() > 0) {
				instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
				layerInfor.setInstance(instance);
			}
		}
		
		BeanUtils.copyProperties(layerInfor, vo);
		
		//所选父审核层
		int[] fromLayerIds = vo.getFromLayerIds();
		List fromLayers = new ArrayList();
		if (fromLayerIds != null && fromLayerIds.length > 0) {
			for (int i=0;i<fromLayerIds.length;i++) {
				InstanceLayerInfor fromLayer = (InstanceLayerInfor)this.flowLayerInforManager.get(fromLayerIds[i]);
				fromLayers.add(fromLayer);
			}
		}
		
		//审核人信息
		int[] checkerIds = vo.getPersonIds();
		//去重复
		/*List<Integer> list = new LinkedList<Integer>();  
	    for(int i = 0; i < checkerIds.length; i++) {  
	        if(!list.contains(checkerIds[i])) {  
	            list.add(checkerIds[i]);  
	        }  
	    }  
	    checkerIds = (Integer[])list.toArray(new Integer[list.size()]);  */
	    
	    boolean[] repeat=new boolean[checkerIds.length];//初始化都是false 
        for(int i=0; i<checkerIds.length; i++) { 
            for(int j=0; j<checkerIds.length; j++) { 
                if(i>j && checkerIds[i]==checkerIds[j] ) repeat[j]=true; 
            } 
        } 
        int counter=0; 
        for(int i=0; i<repeat.length; i++) { 
            if(repeat[i]==false) counter++; 
        } 
        int[] arrayNoRepeat=new int[counter]; 
        int n=0; 
        for(int i=0; i<checkerIds.length; i++) { 
            if(repeat[i]==false) { 
                arrayNoRepeat[n]=checkerIds[i]; 
                n++; 
            } 
        }
        checkerIds = arrayNoRepeat;
        
		
		this.flowLayerInforManager.saveInstanceLayer(layerInfor, fromLayers, checkerIds, false, false);
		
		return "redirect:instanceInfor.do?method=view&instanceId=" + instanceId;
		
	}
	
	
	//验证所选父审核层是否合理
	@RequestMapping(params="method=validate")
	@ResponseBody
	public Map<String, Object> validate(HttpServletRequest request, HttpServletResponse response, InstanceLayerInforVo vo) { 
		
		Map<String, Object> map = new HashMap<String, Object>();
		
		Integer layerId = vo.getLayerId();
		FlowInstanceInfor instance = null;
		
		boolean canChoose = true;
		String warningStr = null;
		
		if (layerId == null || layerId.intValue() == 0) {
			
			String instanceId = request.getParameter("instanceId");
			if (instanceId != null && instanceId.length() > 0) {
				instance = (FlowInstanceInfor)this.flowInstanceManager.get(Integer.valueOf(instanceId));
			}
			
			//所选父审核层
			int[] fromLayerIds = vo.getFromLayerIds();
			
			//如果fromLayerIds有多个，说明是聚合层, 聚合层必须来自于同一个分叉，并且要完整
			List processLayers = this.flowInstanceManager.getCurrentProcessLayers(instance);
			if (fromLayerIds != null && fromLayerIds.length > 0) {

				if (fromLayerIds.length > 1) {
					int k = 0;
					int forkedLayerId = 0;
					int forkSize = 0;
					
					for (int i=0;i<fromLayerIds.length;i++) {
						InstanceLayerInfor tempLayer = (InstanceLayerInfor)this.flowLayerInforManager.get(fromLayerIds[i]);
						if (tempLayer.getForkedType() != InstanceLayerInfor.Layer_Forked_ForkInner) {
							canChoose = false;
							warningStr = "形成聚合层时，所选父审核层必须都为分叉内层次！";
							break;
						}

						forkSize += 1;
						k += 1;
						
						if (k > 1 && forkedLayerId != tempLayer.getForkedLayer().getLayerId()) {
							canChoose = false;
							warningStr = "形成聚合层时，所选父审核层的父层必须是同一个！";
							break;
						}else {
							forkedLayerId = tempLayer.getForkedLayer().getLayerId();
						}
						
					}
					
					//完整性判断,获取有几个分叉，然后与forkSize比较
					int processInnerLayer = 0;
					for (Iterator it=processLayers.iterator();it.hasNext();) {
						//获取当前分叉个数
						InstanceLayerInfor tempLayer = (InstanceLayerInfor)it.next();
						if (tempLayer.getForkedType() == InstanceLayerInfor.Layer_Forked_ForkInner) {
							processInnerLayer++;
						}
					}
					if(canChoose && (processLayers.isEmpty() || processInnerLayer != forkSize)) {
						canChoose = false;
						warningStr = "形成聚合层时，当前分叉层必须全部选中！";
					}
				}else {
					//完整性判断,看是否存在当前处理层
					if(processLayers == null || processLayers.isEmpty()) {
						canChoose = false;
						warningStr = "当前处理层不能为空！";
					}
				}
				
			}else {
				/** 如果fromLayerIds为空,存在三种情况：
				 * A.此时为审核刚开始,没有可选审核层(可以设定审核层);
				 * B.在审核过程中,没有可选审核层(不能设定审核层);
				 * C.在审核过程中,有可选审核层,但不做任何选择(可以设定审核层,并判断是否存在处理中的普通或聚合审核层,不存在则抛出异常);
				 * */
				if (processLayers != null && !processLayers.isEmpty()) {
					//获取所有可以选择的FromLayer
					List fromLayers = this.flowLayerInforManager.getFromLayers(instance);
					
					if (fromLayers == null || fromLayers.size() == 0) {
						//情况B
						canChoose = false;
						warningStr = "必须至少有一个审核层结束才能设置审核层！";
					}else {
						//情况C
						boolean hasLayer = false;
						for (Iterator it=fromLayers.iterator();it.hasNext();) {
							InstanceLayerInfor layerInfor = (InstanceLayerInfor)it.next();
							if ((layerInfor.getForkedType() == InstanceLayerInfor.Layer_Forked_Normal || layerInfor.getForkedType() == InstanceLayerInfor.Layer_Forked_Join)
									&& this.flowLayerInforManager.finishedCheck(layerInfor)) {
								hasLayer = true;
								break;
							}
						}
						if (!hasLayer) {
							canChoose = false;
							warningStr = "不存在普通或聚合审核层,必须手动选定父审核层！";
						}
					}
				}
			}
			
		}
		
		map.put("canChoose", canChoose);
		map.put("warningStr", warningStr);
		
		return map;
	}
	
	
	//删除审核层信息
	@RequestMapping(params = "method=delete")
	@ResponseBody
	public Map<String, Object> delete(HttpServletRequest request, HttpServletResponse response, InstanceLayerInforVo vo) {
		
		Map<String, Object> map = new HashMap<String, Object>();
		Integer layerId = vo.getLayerId();
		
		if (layerId != null && layerId.intValue() > 0) {
			InstanceLayerInfor layerInfor = (InstanceLayerInfor)this.flowLayerInforManager.get(layerId);
			
			try {
				this.flowLayerInforManager.deleteInstanceLayer(layerInfor);
				map.put("message", "该审核层已被删除！");
			} catch (Exception e) {
				map.put("message", "该审核层后存在信息,不能删除！");
				//e.printStackTrace();
			}
		}
		
		return map;
		
	}
	
	
}