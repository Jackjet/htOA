package com.kwchina.core.cms.controller;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforComment;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforPraise;
import com.kwchina.core.cms.service.InforCommentManager;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.cms.vo.InforCommentVo;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/inforComment.do")
public class InforCommentController {

	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private InforDocumentManager inforDocumentManager;
	
	@Resource
	private InforCommentManager inforCommentManager;


	
	/**
	 * 显示评论列表
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		String inforId = request.getParameter("inforId");
		
		List<InforComment> commentList = new ArrayList<InforComment>();
		if(inforId != null && !inforId.equals("")){
			InforDocument inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(inforId));
			if(inforDocument != null){
				commentList.addAll(inforDocument.getComments());
			}
		}
		
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		
		//转换为vo
		List vos = new ArrayList();
		for(InforComment comment : commentList){
			InforCommentVo vo = new InforCommentVo();
			vo.setCommentId(comment.getCommentId());
			vo.setContent(comment.getContent());
			vo.setInforId(comment.getInfor().getInforId());
			vo.setOperaterId(comment.getOperater().getPersonId());
			vo.setOperaterName(comment.getOperater().getPerson().getPersonName());
			vo.setOperateTime(sf.format(comment.getOperateDate()));
			vo.setOperaterPic(comment.getOperater().getPerson().getPhotoAttachment());
			
			vos.add(vo);
		}
		
		JSONArray commentArray = new JSONArray();
		commentArray = convert.modelCollect2JSONArray(vos, new ArrayList());
		jsonObj.put("_Comments", commentArray);

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);
	}
	
	/**
	 * 提交评论
	 * @param inforCommentVo
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(@ModelAttribute("inforCommentVo")
			InforCommentVo inforCommentVo,HttpServletRequest request, HttpServletResponse response) throws Exception {

		String inforId = request.getParameter("inforId");
		
		InforDocument inforDocument = new InforDocument();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		InforComment comment = new InforComment();
		
		JSONObject jsonObj = new JSONObject();

		Timestamp sysTime = new Timestamp(System.currentTimeMillis());
		try {
			if(inforId != null && !inforId.equals("")){
				inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(inforId));
				if(inforDocument != null){
					//comment.setContent(new String(inforCommentVo.getContent().getBytes("ISO-8859-1"), "utf-8"));
					comment.setContent(inforCommentVo.getContent());
					comment.setInfor(inforDocument);
					comment.setOperateDate(sysTime);
					comment.setOperater(systemUser);
					
					InforComment rtnComment = (InforComment)this.inforCommentManager.save(comment);
					
					//把当前最新评论数量返回
					Set<InforComment> newComments = inforDocument.getComments();
					newComments.add(rtnComment);
					//点赞数
					int commentCount = 0;
					for(InforComment newComment : newComments){
						//if(newComment.getPraised() == 1){
						commentCount += 1;
						//}
					}
//					if(praiseCount == 0 && Integer.valueOf(praiseTag) == 1){
//						praiseCount = 1;
//					}
					jsonObj.put("_CommentCount", commentCount);
					
					//是否成功标志
					jsonObj.put("_RtnTag", 1);
				}
			}
		} catch (Exception e) {
			jsonObj.put("_RtnTag", 0);
			e.printStackTrace();
		}
		
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
		
	}
	
	
}
