package com.kwchina.extend.club.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
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

import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.DateHelper;
import com.kwchina.core.util.ExcelObject;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.File;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.club.entity.ActAttendInfor;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.entity.RegisterInfor;
import com.kwchina.extend.club.service.ClubInforManager;
import com.kwchina.extend.club.vo.ClubExcelVo;
import com.kwchina.extend.club.vo.ClubInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/share/clubInfor.do")
public class ClubShareController extends BasicController {

	
	@Resource
	private ClubInforManager clubInforManager;
	

	
	
	/**
	 * 手机查看分享的活动
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewSharedClubInfor")
	public String viewSharedClubInfor(@ModelAttribute("clubInforVo")
	ClubInforVo clubInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {

		
		if(StringUtil.isNotEmpty(request.getParameter("actId"))){
			int actId = Integer.parseInt(request.getParameter("actId"));
			
			ClubInfor clubInfor = (ClubInfor) this.clubInforManager.get(actId);
			request.setAttribute("_ClubInfor", clubInfor);
		}else {
			return "/login";
		}
		
		return "/clubShare";
	}
	
}
