package com.kwchina.core.config.controller;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.struts.upload.FormFile;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.kwchina.core.base.entity.DirAndSupInfor;
import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.DirAndSupInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforCategoryRight;
import com.kwchina.core.cms.service.InforCategoryManager;
import com.kwchina.core.cms.util.InforConstant;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.config.entity.ConfigIndexFun;
import com.kwchina.core.config.service.ConfigIndexFunManager;
import com.kwchina.core.config.vo.PicConfigVo;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.util.SysCommonMethod;


@Controller
@RequestMapping("/core/config.do")
public class ConfigController extends BasicController {
	
	@Resource
	private OrganizeManager organizeManager;
	
	@Resource
	private PersonInforManager personInforManager;
	
	@Resource
	private DirAndSupInforManager dirAndSupInforManager;
	
	@Resource
	private InforCategoryManager inforCategoryManager;
	
	@Resource
	private ConfigIndexFunManager configIndexFunManager;
	
	
	//OA产品化配置   图片配置 
	@RequestMapping(params="method=picConfig")
	public void picConfig(HttpServletRequest request, HttpServletResponse response) {
		//登录页图片IO流
		FileInputStream logInputStream = null;
		FileOutputStream loginOutputStream = null;
		//主页上方图片IO流
		FileInputStream indexInputStream = null;
		FileOutputStream indexOutputStream = null;
		
		String info = "";
		
		try {
			String loginPicPath = request.getParameter("loginPic");
			String indexPicPath = request.getParameter("indexPic");

			
			File loginPic = new File(loginPicPath);
			File indexPic = new File(indexPicPath);
			
			logInputStream = new FileInputStream(loginPic);
			indexInputStream = new FileInputStream(indexPic);
			
			//得到项目真实路径
			String realPath = request.getSession().getServletContext().getRealPath("/");
			
			//得到登录图片保存位置   \images\login
			String loginPicSavePath = realPath+"/images/login/";
			//将登录图片保存为“login.gif”
//			String loginFileName = loginPic.getName();
			String loginFileName = "login.gif";
			
			//得到主页图片保存位置    \css\theme\7
			String indexPicSavepath = realPath+"/css/theme/7/";
			//将主页图片保存为："topbar_left_bg.png"
			String indexFileName = "topbar_left_bg.png";
			
			//如果路径不存在，则创建
//			File loginPicFile = new File(loginPicSavePath); 
//			
//			if(!loginPicFile.exists()){
//				loginPicFile.mkdirs();
//			}

			
			//将登录图片写入
			byte[] loginBuffer = new byte[1024];
			int loginLen = 0;
			loginOutputStream = new FileOutputStream(loginPicSavePath+loginFileName);
			
			while((loginLen=logInputStream.read(loginBuffer)) != -1){
				loginOutputStream.write(loginBuffer);
			}
			
			//将主页图片写入
			byte[] indexBuffer = new byte[1024];
			int indexLen = 0;
			indexOutputStream = new FileOutputStream(indexPicSavepath+indexFileName);
			
			while((indexLen=indexInputStream.read(indexBuffer)) != -1){
				indexOutputStream.write(indexBuffer);
			}
			
			PrintWriter out = response.getWriter();
			out.println("上传成功！");
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println("上传出错，请重试!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}finally{

			try {
				loginOutputStream.close();
				logInputStream.close();
				indexOutputStream.close();
				indexInputStream.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
	}
	
	//获取所有发布信息类别
	@RequestMapping(params="method=allInforCategory")
	public void allInforCategory(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		/** 根据用户权限加载信息发布分类 */
		List categoryMenu = new ArrayList();
		//获取根分类下的所有子类
		InforCategory category = (InforCategory)this.inforCategoryManager.get(InforConstant._Root_Category_Id);
		if (category != null && category.getChilds() != null) {
			Set childs = category.getChilds();
			for (Iterator it=childs.iterator();it.hasNext();) {
				InforCategory tmpCategory = (InforCategory)it.next();
				//把公司公告排除，因为在首页总要显示
				if(tmpCategory.getCategoryId().intValue() != InforConstant.Cms_Category_Annouce){
					categoryMenu.add(tmpCategory);	
				}
				
			}
		}
//		request.setAttribute("_CategoryMenu", categoryMenu);
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		JSONArray jsonArray = new JSONArray();
		
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author.person");
		
		jsonArray = convert.modelCollect2JSONArray(categoryMenu, awareObject);
		jsonObj.put("_AllInfoCategory", jsonArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
		
		
	}
	
	//保存所选显示模块信息
	@RequestMapping(params="method=setIndexFun")
	public void setIndexFun(HttpServletRequest request, HttpServletResponse response) {
		try {
			
			String funName[] = request.getParameterValues("category");
			
			if(funName!=null && funName.length>0){
				for(int i=0;i<funName.length;i++){
					ConfigIndexFun indexFun = new ConfigIndexFun();
					
					//设置的config_indexfunc中的ID，判断其是否为空
					String ided = request.getParameter("id"+funName[i]);
					
					//修改
					if((ided!=null && ided.length() >0) && (request.getParameter("funcname"+ided)!=null||!request.getParameter("funcname"+ided).equals(""))){
						
						System.out.println(ided+"++"+request.getParameter("funcname"+ided));
						indexFun = (ConfigIndexFun)configIndexFunManager.get(Integer.valueOf(ided));
						
						String categoryId = request.getParameter("funcname"+ided);
						InforCategory category = (InforCategory)inforCategoryManager.get(Integer.valueOf(categoryId));
						indexFun.setCategory(category);
						indexFun.setDisplayCount(Integer.valueOf(request.getParameter("displayCount"+categoryId)));
						indexFun.setDisplayName(request.getParameter("displayName"+categoryId));
						indexFun.setDisplayTime(Boolean.valueOf(request.getParameter("displayTime"+categoryId)));
						indexFun.setRoll(Boolean.valueOf(request.getParameter("roll"+categoryId)));
						
						configIndexFunManager.save(indexFun);
					}else if(ided==null || ided.length() <= 0){//新增
						String categoryId = funName[i];
						InforCategory category = (InforCategory)inforCategoryManager.get(Integer.valueOf(categoryId));
						indexFun.setCategory(category);
						indexFun.setDisplayCount(Integer.valueOf(request.getParameter("displayCount"+categoryId)));
						indexFun.setDisplayName(request.getParameter("displayName"+categoryId));
						indexFun.setDisplayTime(Boolean.valueOf(request.getParameter("displayTime"+categoryId)));
						indexFun.setRoll(Boolean.valueOf(request.getParameter("roll"+categoryId)));
						
						configIndexFunManager.save(indexFun);
					}
					
				}
			}

			
			String ids[] = request.getParameterValues("id");
			//如果数据库中存在记录，但页面传过来的有id，但没有显示名称，说明是原来选中的，现在取消选中，则删除之
			if(ids!=null && ids.length>0){
				for(int i=0;i<ids.length;i++){
//					System.out.println(ids[i]+"----"+request.getParameter("funcname"+ids[i]));
					if((ids[i]!=null && ids[i].length() >0) && (request.getParameter("funcname"+ids[i])==null||request.getParameter("funcname"+ids[i]).equals(""))){
						configIndexFunManager.remove(Integer.valueOf(ids[i]));
					}
				}
			}
			
			
			PrintWriter out = response.getWriter();
			out.println("保存成功！");
			
		} catch (Exception e) {
			e.printStackTrace();
			PrintWriter out;
			try {
				out = response.getWriter();
				out.println("保存失败！");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		
	}
	
	
	//查找出已显示模块信息
	@RequestMapping(params="method=allreadyDis")
	public void allreadyDis(HttpServletRequest request, HttpServletResponse response) {
		try {
			List<ConfigIndexFun> indexFunList = new ArrayList<ConfigIndexFun>();
			indexFunList = configIndexFunManager.getAll() ;
			
			JSONObject jsonObj = new JSONObject();
			JSONConvert convert = new JSONConvert();
			JSONArray jsonArray = new JSONArray();
			
			//通知Convert，哪些关联对象需要获取
			List awareObject = new ArrayList();
//			awareObject.add("author.person");
			
			jsonArray = convert.modelCollect2JSONArray(indexFunList, awareObject);
			jsonObj.put("_AllIndexFun", jsonArray);
			
			//设置字符编码
	        response.setContentType(CoreConstant.CONTENT_TYPE);
	        response.getWriter().print(jsonObj);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		
		
	}
	
}
