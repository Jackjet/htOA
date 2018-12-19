package com.kwchina.core.sys;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Set;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

import com.kwchina.core.base.entity.FunctionRightInfor;
import com.kwchina.core.base.entity.OperationDefinition;
import com.kwchina.core.base.entity.RoleInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.entity.VirtualResource;
import com.kwchina.core.base.service.OperationDefinitionManager;
import com.kwchina.core.base.service.RoleManager;
import com.kwchina.core.base.service.VirtualResourceManager;
import com.kwchina.oa.util.SysCommonMethod;

public class AuthorizationFilter extends HttpServlet implements Filter {

	private FilterConfig filterConfig;

	private VirtualResourceManager resourceManager;

	private RoleManager roleManager;
	
	private OperationDefinitionManager operationDefinitionManager;

	Logger logger = Logger.getLogger(Log4jHandlerAOP.class);
	public static String getRequestURL(HttpServletRequest request) { 
	   	if (request == null) { 
	   		return ""; 
	   	} 
	   	String url = ""; 
	   	url = request.getContextPath(); 
	   	url = url + request.getServletPath(); 
	
	   	java.util.Enumeration names = request.getParameterNames(); 
	   	int i = 0; 
	//   	if (!"".equals(request.getQueryString()) || request.getQueryString() != null) { 
	//   		url = url + "?" + request.getQueryString(); 
	//   	} 
	
	   	if (names != null) { 
		    	while (names.hasMoreElements()) { 
			    	String name = (String) names.nextElement(); 
			    	if (i == 0) { 
			    		url = url + "?"; 
			    	} else { 
			    		url = url + "&"; 
			    	} 
			    	i++; 
			
			    	String value = request.getParameter(name); 
			    	if (value == null) { 
			    		value = ""; 
			    	} 
			
			    	url = url + name + "=" + value; 
			    	try { 
			    		// java.net.URLEncoder.encode(url, "ISO-8859"); 
			    	} catch (Exception e) { 
			    		e.printStackTrace(); 
			    	} 
		    	} 
	   	} 
	   	try { 
	   	// String enUrl = java.net.URLEncoder.encode(url, "utf-8"); 
	   	} catch (Exception ex) { 
	   		ex.printStackTrace(); 
	   	} 
	
	   	return url; 
	}  
	//初始化
	public void init(FilterConfig config) {
		this.filterConfig = config;

		ServletContext context = filterConfig.getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		this.resourceManager = (VirtualResourceManager) webContext.getBean("virtualResourceManagerImpl");
		this.roleManager = (RoleManager) webContext.getBean("roleManager");
		this.operationDefinitionManager = (OperationDefinitionManager) webContext.getBean("operationDefinitionManagerImpl");
	}

	//过滤
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		String contextPath = httpRequest.getContextPath();

		try {
			httpRequest.setCharacterEncoding(CoreConstant.ENCODING);
			String pathURI = httpRequest.getRequestURI();
			pathURI = pathURI.replaceAll(contextPath, "");
			SystemUserInfor systemUser = SysCommonMethod.getSystemUser(httpRequest);

			if (systemUser == null) {
				if(pathURI!=null && pathURI.equals("/meeting/meetInfor.do")
						&& httpRequest.getParameter("method") != null
						&& httpRequest.getParameter("method") != ""
						&& (httpRequest.getParameter("method").equals("viewIndex")
						|| httpRequest.getParameter("method").equals("getMeetCounts"))){
					filterChain.doFilter(request, response);
				}
				//用户若没有登陆,则返回到登陆页面
				HttpServletResponse httpResponse = (HttpServletResponse) response;
				httpResponse.sendRedirect("//login.do");


			}else{
				//用户已经登陆,则需要判断所访问的资源是否有访问设置
				if (pathURI.indexOf("login.jsp") >= 0 || pathURI.indexOf("images") >= 0 || pathURI.indexOf("js") >= 0 
						|| pathURI.indexOf("css") >= 0 || pathURI.indexOf("templates") >= 0 || pathURI.indexOf("inc") >= 0) {
					filterChain.doFilter(request, response);
				}else{
					
//					logger.info("用户【"+systemUser.getUserName()+"】 执行操作："+getRequestURL(httpRequest));
					
					VirtualResource virtualResource = resourceManager.getVirtualResource(pathURI);
					boolean hasRight = false;	//是否有权限
					boolean redirect = false;	//是否跳转
					String redirectPath = "";	//跳转的路径
					String method = httpRequest.getParameter("method");

					if (systemUser.getUserType() == 1) {
						//系统管理员拥有所有权限
						hasRight = true;
						if (virtualResource != null && virtualResource.getChilds().size() > 0) {
							//取第一个子资源中的URI进行跳转
							Set childs = virtualResource.getChilds();
							ArrayList ls = new ArrayList();
							ls.addAll(childs);
							VirtualResource tempResource = (VirtualResource) ls.get(0);
							redirectPath = tempResource.getResourcePath() + "?method=list";
							redirect = true;
						}
					}else{
						if (virtualResource != null) {
							/** 需要对该资源或其子资源进行权限判断 */
							if (virtualResource.getChilds().size() == 0) {
								//需要判断本身的权限
								hasRight = judgeRight(virtualResource, method, systemUser);
							}else{
								//需要判断子权限
								Set<VirtualResource> childs = virtualResource.getChilds();
								for (VirtualResource tempResource : childs){
									if (judgeRight(tempResource, method, systemUser)) {
										hasRight = true;
										redirect = true;
										redirectPath = tempResource.getResourcePath() + "?method=" + method;
										break;
									}
								}
							}
						}else{
							hasRight = true;
						}
					}

					if (hasRight) {
						if (redirect) {
							//跳转到指定URI
							RequestDispatcher rd = this.filterConfig.getServletContext().getRequestDispatcher(redirectPath);
							rd.forward(request, response);
						}else{
							//不跳转,直接访问导航栏URI
								filterChain.doFilter(request, response);
						}
					}else{
						
						if (method != null && method.length() > 0 
								&& (method.indexOf("edit")>-1 || method.indexOf("add")>-1)) {
							RequestDispatcher rd = this.filterConfig.getServletContext().getRequestDispatcher("/common/error.jsp");
							request.setAttribute("_ErrorMessage", "对不起,您无权进行该操作,请与系统管理员联系!");
							rd.forward(request, response);
						}else {
							response.setContentType(CoreConstant.CONTENT_TYPE);
							PrintWriter out = response.getWriter();
							out.print("<script language='javascript'>");
							out.print("alert('对不起,您无权进行该操作,请与系统管理员联系!');");
							out.print("window.history.go(-1)");
							out.print("</script>");
						}
					}
				}
			}
		} catch (Exception ex) {
			filterConfig.getServletContext().log(ex.getMessage());
		}

	}

	//用户权限判断(需要判断用户在模块内对哪些操作拥有权限)
	private boolean judgeRight(VirtualResource virtualResource, String method, SystemUserInfor systemUser) {
		boolean hasRight = false;

		Set<FunctionRightInfor> functionRights = virtualResource.getFunctionRights();
		if (!functionRights.isEmpty()) {
			for (FunctionRightInfor functionRight : functionRights) {
				RoleInfor role = functionRight.getRole();
//				System.out.println(role.getRoleId());
				long rightData = functionRight.getRightData();
				//判断用户对该操作是否拥有权限(通过对权限数据进行移位来判断)
				if (this.roleManager.belongRole(systemUser, role)) {
					method = (method.indexOf("get")>-1||method.indexOf("view")>-1)?"list":method;
					method = (method.indexOf("edit")>-1||method.indexOf("add")>-1)?"edit":method;
					OperationDefinition od = this.operationDefinitionManager.getOperationByMethod(method);
					if (od != null) {
						int position = od.getPosition();
						long a = rightData >> (position - 1);
						long result = (a & 1);
						if (result == 1) {
							hasRight = true;
							break;
						}
					}else{
						hasRight = true;
						break;
					}
				}
			}
		}
		return hasRight;
	}
	

	//销毁
	public void destroy() {
		
	}

}
