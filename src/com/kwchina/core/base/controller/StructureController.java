package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.service.StructureManager;
import com.kwchina.core.sys.CoreConstant;
@Controller
@RequestMapping("core/structureInfor.do")
public class StructureController {

	@Resource
	private StructureManager structureManager;

	
	//按照树状结构获取企业岗位信息
	@RequestMapping(params="method=list")
	public String listTree(HttpServletRequest request, HttpServletResponse response) throws Exception {

		ArrayList returnArray = this.structureManager.getStructureAsTree(CoreConstant.Structure_Begin_Id);
		request.setAttribute("_StructureTree", returnArray);

		return "base/listStructure";
	}

	/*//列表岗位信息
	@RequestMapping(params="method=list")
	public String list(HttpServletRequest request, HttpServletResponse response) throws Exception {

		Integer parentId = structureForm.getParentId();

		List returnLs = new ArrayList();
		if (parentId != null && parentId.intValue() != 0) {
			StructureInfor structure = (StructureInfor) this.structureManager.get(parentId);
			returnLs.addAll(structure.getChilds());
		} else {
			returnLs = this.structureManager.getAll();
		}

		request.setAttribute("_Structures", returnLs);

		return "base/listStructure";
	}*/
	
	
	//编辑岗位
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String structureId = request.getParameter("structureId");
		//String parentId = request.getParameter("parentId");
		
		//修改
		if (structureId != null && structureId.length() > 0) {
			StructureInfor structure = (StructureInfor)this.structureManager.get(Integer.valueOf(structureId));
			request.setAttribute("_Structure", structure);
		}
		
		//所有岗位信息
		List treeStructures = this.structureManager.getStructureAsTree(CoreConstant.Structure_Begin_Id);
		request.setAttribute("_TreeStructures", treeStructures);
		
		/*//父岗位
		if(parentId != null && parentId.length() > 0){
			ArrayList parentsAndSelf = this.structureManager.getParentsAndSelf(Integer.valueOf(parentId));			
			request.setAttribute("_Parent", parentsAndSelf);
		}*/
		
		return "base/editStructure";
	}
	
	
	//保存岗位
	@RequestMapping(params="method=save")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String structureId = request.getParameter("structureId");
		String structureName = request.getParameter("structureName");
		String orderNo = request.getParameter("orderNo");
		String parentId = request.getParameter("parentId");
		StructureInfor structure = new StructureInfor();
		
		//修改
		if (structureId != null && structureId.length() > 0) {
			structure = (StructureInfor)this.structureManager.get(Integer.valueOf(structureId));		
		}
		structure.setStructureName(structureName);
		if (orderNo != null && orderNo.length() > 0) {
			structure.setOrderNo(Integer.valueOf(orderNo));
		}
		if(parentId != null && parentId.length() > 0){
			StructureInfor parent= (StructureInfor)this.structureManager.get(Integer.valueOf(parentId));
			structure.setParent(parent);
		}
		
		this.structureManager.save(structure);
		
	}
	
	
	//删除岗位
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String structureId = request.getParameter("structureId");
		if (structureId != null && structureId.length() > 0) {
			StructureInfor structure  = (StructureInfor)this.structureManager.get(Integer.parseInt(structureId));
			deleteChildren(structure);
		}
	}
	
	private void deleteChildren(StructureInfor structure){
		Set childs = structure.getChilds();
		
		if(childs!=null && childs.size()>0){
			for(Iterator it = childs.iterator();it.hasNext();){
				StructureInfor tpS = (StructureInfor)it.next();
				
				//从父对象移除
				structure.getChilds().remove(tpS);
				
				deleteChildren(tpS);
			}			
		}
		
		this.structureManager.remove(structure);
	}

}
