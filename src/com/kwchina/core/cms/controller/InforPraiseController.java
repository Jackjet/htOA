package com.kwchina.core.cms.controller;

import java.sql.Date;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforPraise;
import com.kwchina.core.cms.service.InforDocumentManager;
import com.kwchina.core.cms.service.InforPraiseManager;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.oa.util.SysCommonMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/inforPraise.do")
public class InforPraiseController {

	
	@Resource
	private SystemUserManager systemUserManager;
	
	@Resource
	private InforDocumentManager inforDocumentManager;
	
	@Resource
	private InforPraiseManager inforPraiseManager;


	/**
	 * 保存点赞或者取消
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String inforId = request.getParameter("inforId");
		String praiseTag = request.getParameter("praiseTag");
		
		InforDocument inforDocument = new InforDocument();
		long nowTime = System.currentTimeMillis();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int personId = systemUser.getPersonId().intValue();
		
		InforPraise praise = new InforPraise();
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			if(inforId != null && !inforId.equals("") && praiseTag != null && !praiseTag.equals("")){
				inforDocument = (InforDocument)this.inforDocumentManager.get(Integer.valueOf(inforId));
				if(inforDocument != null){
					boolean hasOped = false;
					
					Set<InforPraise> praises = inforDocument.getPraises();
					
					//不论是点赞或者取消，都先查找是否已做过此操作
					for(InforPraise tmpPraise : praises){
						int tmpPersonId = tmpPraise.getPraiser().getPersonId().intValue();
						if(tmpPersonId == personId){
							praise = tmpPraise;
							hasOped = true;
							break;
						}
					}
					
					if(hasOped){  //进行过此操作的
						
					}else {       //未进行过此操作的
						praise.setInfor(inforDocument);
						praise.setPraiser(systemUser);
					}
					
					Timestamp sysTime = new Timestamp(System.currentTimeMillis());
					praise.setPraised(Integer.valueOf(praiseTag));
					praise.setUpdateTime(sysTime);
					
					InforPraise rtnPraise = (InforPraise)inforPraiseManager.save(praise);
					
					//把当前最新数量返回
					Set<InforPraise> newPraises = inforDocument.getPraises();
					newPraises.add(rtnPraise);
					//点赞数
					int praiseCount = 0;
					for(InforPraise newPraise : newPraises){
						if(newPraise.getPraised() == 1){
							praiseCount += 1;
						}
					}
//					if(praiseCount == 0 && Integer.valueOf(praiseTag) == 1){
//						praiseCount = 1;
//					}
					jsonObj.put("_PraisedCount", praiseCount);
					
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
	/**
	 * 获取需要首页公告(重要)个数
	 *
	 */
	@ResponseBody
	@RequestMapping(params = "method=annouceCounts")
	public int annouceCounts(HttpServletRequest request, HttpServletResponse response) throws Exception{

		Date sysDate = new Date(System.currentTimeMillis());
		String queryHQL = "from InforDocument inforDocument where category.categoryId = 2 and '" + sysDate + "' between inforDocument.startDate and inforDocument.endDate";

		List annouces = this.inforDocumentManager.getResultByQueryString(queryHQL);


		return annouces.size();


	}

}
