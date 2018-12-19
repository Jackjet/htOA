package com.kwchina.extend.club.controller;

import java.io.PrintWriter;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforPraise;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.service.ClubInforManager;
import com.kwchina.extend.club.service.RegisterInforManager;
import com.kwchina.extend.club.util.MatrixToImageWriter;
import com.kwchina.extend.club.vo.ClubInforVo;
import com.kwchina.oa.util.SysCommonMethod;

/**
 * 报名
 * @author suguan
 *
 */
@Controller
@RequestMapping("/club/registerInfor.do")
public class RegisterInforController extends BasicController {

	@Resource
	private SystemUserManager systemUserManager;

	@Resource
	private ClubInforManager clubInforManager;
	
	@Resource
	private RegisterInforManager registerInforManager;

	/**
	 * 报名
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveRegister")
	public void saveRegister(HttpServletRequest request, HttpServletResponse response) throws Exception {

		String actId = request.getParameter("actId");
		
		ClubInfor clubInfor = new ClubInfor();
		long nowTime = System.currentTimeMillis();
		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		
		RegisterInfor registerInfor = new RegisterInfor();
		
		JSONObject jsonObj = new JSONObject();
		
		try {
			if(actId != null && !actId.equals("")){
				clubInfor = (ClubInfor)this.clubInforManager.get(Integer.valueOf(actId));
				if(clubInfor != null){
					//未报过名的才保存
					Set<RegisterInfor> registers = clubInfor.getRegisters();
					//是否已报过
					boolean hasReged = false;
					for(RegisterInfor reg : registers){
						if(reg.getReger().getPersonId().intValue() == systemUser.getPersonId().intValue()){
							hasReged = true;
						}
					}
					
					if(!hasReged){
						registerInfor.setClubInfor(clubInfor);
						registerInfor.setReger(systemUser);
						
						Timestamp sysTime = new Timestamp(nowTime);
						registerInfor.setRegTime(sysTime);
						
						RegisterInfor rtnRegister = (RegisterInfor)registerInforManager.save(registerInfor);
					}
					
					
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
