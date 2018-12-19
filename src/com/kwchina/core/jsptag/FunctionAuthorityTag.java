package com.kwchina.core.jsptag;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.PageContext;
import javax.servlet.jsp.tagext.Tag;
import javax.servlet.jsp.tagext.TagSupport;

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
import com.kwchina.oa.sys.SystemConstant;

public class FunctionAuthorityTag extends TagSupport {

	private String path;

	private String alias;
	
	private String method;

	private PageContext pageContext;

	private Tag parent;

	public void setPath(String path) {
		this.path = path;
	}

	public void setAlias(String alias) {
		this.alias = alias;
	}
	
	public void setMethod(String method) {
		this.method = method;
	}

	private void init() {
		path = null;
	}

	public void setPageContext(PageContext pageContext) {
		this.pageContext = pageContext;
	}

	public void setParent(Tag tag) {
		this.parent = parent;
	}

	public Tag getParent() {
		return super.getParent();
	}

	public int doStartTag() throws JspException {
		
		HttpSession session = pageContext.getSession();

		String sessionUser = SystemConstant.Session_SystemUser;
		SystemUserInfor systemUser = (SystemUserInfor) session.getAttribute(sessionUser);
		if (systemUser == null) {
			return super.SKIP_BODY;
		}

		// 如果为系统管理员,则可以查看到该菜单
		// int personId = systemUser.getPersonId().intValue();
		int userType = systemUser.getUserType();
		if (userType == 1) {
			// 系统管理员
			return super.EVAL_BODY_INCLUDE;
		}

		// 判断非管理员用户是否有访问该功能的权限
		boolean hasRight = true;
		ServletContext context = pageContext.getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(context);
		VirtualResourceManager resourceManager = (VirtualResourceManager) webContext.getBean("virtualResourceManagerImpl");
		RoleManager roleManager = (RoleManager) webContext.getBean("roleManager");
		OperationDefinitionManager operationDefinitionManager = (OperationDefinitionManager) webContext.getBean("operationDefinitionManagerImpl");

		VirtualResource virtualResource = resourceManager.getResourceByAlias(alias);
		if (virtualResource != null) {
			// 如果有设定的该资源,则需要判断权限,先设定没有权限
			hasRight = false;
			
			Set<FunctionRightInfor> functionRights = virtualResource.getFunctionRights();
			if (functionRights.isEmpty()) {
				//默认,未设定权限,表示没有人可以进行操作
				hasRight = false;
			} else {
				
				for (FunctionRightInfor functionRight : functionRights) {
					RoleInfor role = functionRight.getRole();
					long rightData = functionRight.getRightData();
					//判断用户对该操作是否拥有权限(通过对权限数据进行移位来判断)
					if (roleManager.belongRole(systemUser, role)) {
						OperationDefinition od = operationDefinitionManager.getOperationByMethod(method);
						if (od != null) {
							int position = od.getPosition();
							long a = rightData >> (position - 1);
							long result = (a & 1);
							if (result == 1) {
								hasRight = true;
								break;
							}
						}
					}
				}
			}

			if (hasRight) {
				return super.EVAL_BODY_INCLUDE;
			} else {
				return super.SKIP_BODY;
			}
		} else {
			return super.EVAL_BODY_INCLUDE;
		}
	}

	public int doEndTag() throws JspException {
		return super.EVAL_PAGE;
	}

	public void release() {
		// super.release();
		init();
	}
}
