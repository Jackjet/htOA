package com.kwchina.oa.bbs.service.impl;

import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.kwchina.core.common.page.Pages;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.oa.bbs.dao.CommentDAO;
import com.kwchina.oa.bbs.entity.CommentInfor;
import com.kwchina.oa.bbs.service.CommentInforManager;
import com.kwchina.oa.bbs.vo.CommentInforVo;
import com.kwchina.oa.personal.schedule.dao.ScheduleJobDAO;
import com.kwchina.oa.sys.SystemConstant;

@Service
public class CommentInforManagerImpl extends BasicManagerImpl<CommentInfor> implements CommentInforManager {

	private CommentDAO commentDAO;

	@Autowired
	public void setCommentDAO(CommentDAO commentDAO) {
		this.commentDAO = commentDAO;
		super.setDao(commentDAO);
	}
	
	public String createHql(CommentInforVo vo, Pages pages, String method, int hqlType) {

    	String fileName = "commentInfor" + SystemConstant.FILEPREFIX + "?method="+method+"&page=" + vo.getPage();

        int thesisId = vo.getThesisId();
        int commentAuthor=vo.getAuthor();
        String thesisTitle=vo.getThesisTitle();
        String nickName = vo.getNickName();
        String replyContent=vo.getReplyContent();
        int replayDateAsc=vo.getReplyDateAsc();

        StringBuffer sb = new StringBuffer();
        switch (hqlType) {
            case 0:
                sb.append("from CommentInfor commentInfor where 1=1");
                break;
            case 1:
                sb.append("select count(*) from CommentInfor commentInfor where 1=1");
                break;
        }
        if (thesisId != 0) {
            sb.append(" and commentInfor.thesisInfor.thesisId =" + thesisId);
            if (hqlType == 0) {
                fileName += "&thesisId=" + thesisId;
            }
        }

        if (commentAuthor != 0) {
            sb.append(" and commentInfor.replyMan.personId =" + commentAuthor);
            if (hqlType == 0) {
                fileName += "&author=" + thesisId;
            }
        }

        if (thesisTitle != null && thesisTitle.length() > 0) {
            sb.append(" and commentInfor.thesisInfor.title like '%" + thesisTitle.trim() + "%'");
            if (hqlType == 0) {
                fileName += "&thesisTitle=" + thesisTitle.trim();
            }
        }

        if (replyContent != null && replyContent.length() > 0) {
            sb.append(" and replyContent like '%" + replyContent.trim() + "%'");
            if (hqlType == 0) {
                fileName += "&replyContent=" + replyContent.trim();
            }
        }

        if (nickName != null && nickName.length() > 0) {
            sb.append(" and nickName like '%" + nickName.trim() + "%'");
            if (hqlType == 0) {
                fileName += "&nickName=" + nickName.trim();
            }
        }


		if (hqlType == 0) {
			sb.append(replayDateAsc==1?" order by replyDate asc":" order by replyDate desc");
			fileName += "&replyDateAsc=" + replayDateAsc;
			pages.setFileName(fileName);
		}

        return sb.toString();
    }

	// 转化CommentInfor为CommentInforVo
	public CommentInforVo transPOToVO(CommentInfor comment) {
		CommentInforVo commentVo = new CommentInforVo();

		try {
			BeanUtils.copyProperties(commentVo, comment);
			
//			commentVo.setComments(comment.getCommentinfor().size());
//			
//			SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//			Date updateDateOld = (Date)comment.getUpdateDate();
//			String updateDateNew = sf.format(updateDateOld);
//			
//			commentVo.setUpdateDate(updateDateNew);
		} catch (Exception ex) {

		}

		return commentVo;
	}
}
