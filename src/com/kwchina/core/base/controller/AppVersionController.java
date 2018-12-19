package com.kwchina.core.base.controller;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.entity.AppVersion;
import com.kwchina.core.base.service.AppVersionManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.sys.SystemConstant;

/**
 * 检查版本更新
 * @author suguan
 *
 */
@Controller
@RequestMapping("/version.do")
public class AppVersionController extends BasicController {
	
	@Resource
	private AppVersionManager appVersionManager;
	
	
	
	/**
	 * 最新版本
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params="method=checkUpdate")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//返回值
		boolean success = true;
		String message = "";
		
		//部门信息
//		JSONArray versionArray = new JSONArray();
		AppVersion newestVersionEntity = new AppVersion();
		
		//参数：客户端带入的version
		String oldVersion = request.getParameter("version");
		//参数：客户端带入的平台
		String platform = request.getParameter("platform");
		
		//将平台放入session
//		List<String> platformList = new ArrayList<String>();
//		platformList.add(platform);
		request.getSession().setAttribute(SystemConstant.Session_Platform, platform);
		
		try {
			if(oldVersion != null && !oldVersion.equals("")){
				String queryString = "from AppVersion version where version.platform = '" + platform + "' order by version.updateTime desc";
				List allVersions = this.appVersionManager.getResultByQueryString(queryString);
				if(allVersions != null && allVersions.size() > 0){
					AppVersion newVersionEntity = (AppVersion)allVersions.get(0);
					if(newVersionEntity != null){
						String newVersion = newVersionEntity.getVersion();
						if(oldVersion.equals(newVersion)){
							success = false;
							message = "当前已是最新版本！";
						}else {
							//禁止更新为false
							success = true;
//							success = false;
							message = "检测到新版本"+newVersion+"！";
							newestVersionEntity = newVersionEntity;
						}
					}else {
						success = false;
						message = "未检测到更新版本！";
					}
				}else {
					success = false;
					message = "未检测到更新版本！";
				}
				
			}else {
				success = false;
				message = "未获得当前版本，请检查后重试！";
			}
			
			
		} catch (RuntimeException e) {
			success = false;
			message = "后台出错，请重试！";
			e.printStackTrace();
		}
		//jsonObj.put("_LoginUsers", userArray);
		//返回值
		jsonObj.put("Success", success);
		jsonObj.put("Message", message);
		jsonObj.put("Result", newestVersionEntity);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	
	
}
