package com.kwchina.oa.bbs.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.bbs.dao.ThesisDAO;
import com.kwchina.oa.bbs.entity.ThesisInfor;
import com.kwchina.oa.bbs.service.ThesisInforManager;
import com.kwchina.oa.bbs.vo.ThesisInforVo;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobDAO;
import com.kwchina.oa.sys.SystemConstant;

@Service
public class ThesisInforManagerImpl extends BasicManagerImpl<ThesisInfor> implements ThesisInforManager {

	private ThesisDAO thesisDAO;

	@Autowired
	public void setThesisDAO(ThesisDAO thesisDAO) {
		this.thesisDAO = thesisDAO;
		super.setDao(thesisDAO);
	}

	// 转化ThesisInfor为ThesisInforVo
	public ThesisInforVo transPOToVO(ThesisInfor thesis) {
		ThesisInforVo thesisVo = new ThesisInforVo();

		try {
			BeanUtils.copyProperties(thesisVo, thesis);
			
			thesisVo.setComments(thesis.getCommentinfor().size());
			
			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
			Date updateDateOld = (Date)thesis.getUpdateDate();
			String updateDateNew = sf.format(updateDateOld);
			
			thesisVo.setUpdateDate(updateDateNew);
		} catch (Exception ex) {

		}

		return thesisVo;
	}
	
	//判断权限
	public boolean judgeRight(InforDocument inforDocument,SystemUserInfor user){
		boolean hasRight = false;
		int userId = user.getPersonId().intValue();
		int authorId = inforDocument.getAuthor().getPersonId().intValue();
			
		if (userId == authorId || user.getUserType() == SystemConstant._User_Type_Admin) {
			//只有作者和管理员才有权限
			hasRight = true;
		}	
			
		return hasRight;
	}
}
