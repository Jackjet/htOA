package com.kwchina.oa.customer.controller;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

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

import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.customer.entity.ContactInfor;
import com.kwchina.oa.customer.entity.CustomerInfor;
import com.kwchina.oa.customer.service.ActivityInforManager;
import com.kwchina.oa.customer.service.ContactInforManager;
import com.kwchina.oa.customer.service.CustomerInforManager;
import com.kwchina.oa.customer.vo.ContactInforVo;
import com.kwchina.oa.customer.vo.CustomerInforVo;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping("/customer/customerInfor.do")
public class CustomerInforController extends BasicController {

	@Resource
	private CustomerInforManager customerInforManager;

	@Resource
	private ContactInforManager contactInforManager;

	@Resource
	private ActivityInforManager activityInforManager;

	/***************************************************************************
	 * 显示客户信息列表
	 * 
	 * @param customerInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=list")
	public void list(@ModelAttribute("customerInforVo")
	CustomerInforVo customerInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);

		// 构造查询语句(使用jqGrid栏目上的条件查询)
		String[] queryString = new String[2];
		String condition = "";
		queryString[1] = "select count(customerId) from CustomerInfor  where 1=1";
		queryString[0] = "from CustomerInfor customerInfor where 1=1";
		condition = " and manager.personId='" + user.getPersonId() + "'";

		// 选择查询范围 type：2-以有客户 3-潜在客户
		if (customerInforVo.getType().equals("2")) {
			condition += " and customerType=1 ";
		} else if (customerInforVo.getType().equals("3")) {
			condition += " and customerType=0 ";
		}
		queryString[0] += condition;
		queryString[1] += condition;
		queryString = this.customerInforManager.generateQueryString(queryString, getSearchParams(request));
		String page = request.getParameter("page"); // 当前页
		String rowsNum = request.getParameter("rows"); // 每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));

		PageList pl = this.customerInforManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();

		// 把查询到的结果转化为VO
		List customerVos = new ArrayList();
		if (list.size() > 0) {
			for (Iterator it = list.iterator(); it.hasNext();) {
				CustomerInfor customerInfor = (CustomerInfor) it.next();
				customerInforVo = this.customerInforManager.transPOToVO(customerInfor);
				customerVos.add(customerInforVo);
			}
		}

		// 定义返回的数据类型：json，使用了json-lib
		JSONObject jsonObj = new JSONObject();

		// 定义rows，存放数据
		JSONArray rows = new JSONArray();
		jsonObj.put("page", pl.getPages().getCurrPage()); // 当前页(名称必须为page)
		jsonObj.put("total", pl.getPages().getTotalPage()); // 总页数(名称必须为total)
		jsonObj.put("records", pl.getPages().getTotals()); // 总记录数(名称必须为records)

		JSONConvert convert = new JSONConvert();
		// 通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		// awareObject.add("sender.person");
		rows = convert.modelCollect2JSONArray(customerVos, awareObject);
		jsonObj.put("rows", rows); // 返回到前台每页显示的数据(名称必须为rows)

		// 设置字符编码
		// response.setCharacterEncoding("UTF-8");
		response.setContentType(CoreConstant.CONTENT_TYPE);
		response.getWriter().print(jsonObj);

	}

	/***************************************************************************
	 * 查看客户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=viewCustomer")
	public String view(HttpServletRequest request, HttpServletResponse response) throws Exception {
		int customerId = Integer.parseInt(request.getParameter("rowId"));
		CustomerInfor customerinfor = (CustomerInfor) this.customerInforManager.get(customerId);
		request.setAttribute("_customerinfor", customerinfor);

		// 查看联系人
		String SQL = " from ContactInfor contactInfor where 1=1 and customer.customerId =" + customerId;
		List contactInforList = this.customerInforManager.getResultByQueryString(SQL);
		request.setAttribute("_ContactInforList", contactInforList);

		return "viewCustomerInfor";
	}

	/***************************************************************************
	 * 编辑客户信息
	 * 
	 * @param customerInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=edit")
	public String edit(@ModelAttribute("customerInforVo")
	CustomerInforVo customerInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		int customerId = customerInforVo.getCustomerId();
		// 如果主键不为0，则本次操作为修改操作
		if (customerId != 0) {
			CustomerInfor customerInfor = new CustomerInfor();
			customerInfor = (CustomerInfor) this.customerInforManager.get(customerId);
			BeanUtils.copyProperties(customerInforVo, customerInfor);
		}
		return "editCustomerInfor";
	}

	/***************************************************************************
	 * 保存客户信息
	 * 
	 * @param customerInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=save")
	public void save(@ModelAttribute("customerInforVo")
	CustomerInforVo customerInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		SystemUserInfor user = SysCommonMethod.getSystemUser(request);
		CustomerInfor customerInfor = new CustomerInfor();
		Integer customerId = customerInforVo.getCustomerId();
		if (customerId != 0) {
			customerInfor = (CustomerInfor) this.customerInforManager.get(customerId);
		}
		BeanUtils.copyProperties(customerInfor, customerInforVo);
		customerInfor.setManager(user);
		this.customerInforManager.save(customerInfor);

//		PrintWriter out = response.getWriter();
//		out.print("<script language='javascript'>");
//		out.print("window.opener.reloadTab1();");
//		out.print("window.close();");sss
//		out.print("</script>");
	}

	/***************************************************************************
	 * 编辑联系人信息
	 * 
	 * @param contactInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=editContact")
	public String editContact(@ModelAttribute("contactInforVo")
	ContactInforVo contactInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer contactId = contactInforVo.getContactId();
		String customerIdStr = request.getParameter("customerId");
		
		//查询当前客户
		CustomerInfor customerInfor =new CustomerInfor();
		customerInfor = (CustomerInfor) this.customerInforManager.get(Integer.parseInt(customerIdStr));
		request.setAttribute("_CustomerInfor", customerInfor);
		request.setAttribute("customerId", customerIdStr);
		// 如果主键不为0，则本次操作为修改联系人信息
		if (contactId != 0) {
			ContactInfor contactInfor = new ContactInfor();
			String sql = " from ContactInfor contactInfor where 1=1 and contactId =" + contactId;
			List list = this.contactInforManager.getResultByQueryString(sql);
			if (list.size() != 0) {
				contactInfor = (ContactInfor) list.get(0);
				BeanUtils.copyProperties(contactInforVo, contactInfor);
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				String birthday = new String();
				if (contactInfor.getBirthday() != null) {
					birthday = dateFormat.format(contactInfor.getBirthday());
				}
				contactInforVo.setBirthdayStr(birthday);
			}
		}

		return "editContactInfor";
	}

	/***************************************************************************
	 * 删除联系人信息
	 * 
	 * @param contactInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=deleteContact")
	public String deleteContact(@ModelAttribute("contactInforVo")
	ContactInforVo contactInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer contactId = contactInforVo.getContactId();
		Integer customerId = Integer.parseInt(request.getParameter("customerId"));
		if (contactId != 0) {
			this.contactInforManager.remove(contactId);
		}

		return "redirect:customerInfor.do?method=viewCustomer&rowId=" + customerId;
	}

	/***************************************************************************
	 * 保存联系人
	 * 
	 * @param contactInforVo
	 * @param model
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=saveContact")
	public String saveContact(@ModelAttribute("contactInforVo")
	ContactInforVo contactInforVo, Model model, HttpServletRequest request, HttpServletResponse response) throws Exception {
		Integer customerId = Integer.parseInt(request.getParameter("customerId"));
		Integer contactId = contactInforVo.getContactId();
		CustomerInfor customer = (CustomerInfor) this.customerInforManager.get(customerId);
		ContactInfor contactInfor = new ContactInfor();
		if (contactId != 0) {
			contactInfor = (ContactInfor) this.contactInforManager.get(contactId);
		}
		contactInfor = this.contactInforManager.transVOToPO(contactInforVo);
		contactInfor.setCustomer(customer);
		this.contactInforManager.save(contactInfor);
		return "redirect:customerInfor.do?method=viewCustomer&rowId=" + customerId;

	}

	/***************************************************************************
	 * 删除客户信息
	 * 
	 * @param request
	 * @param response
	 * @return
	 * @throws Exception
	 */
	@RequestMapping(params = "method=delete")
	public String delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			String[] deleteIds = rowIds.split(",");
			if (deleteIds.length > 0) {
				for (int i = 0; i < deleteIds.length; i++) {
					int deleteId = Integer.valueOf(deleteIds[i]);

					// 删除客户信息
					this.customerInforManager.remove(deleteId);
				}
			}
		}
		return "listAllCustomer";
	}

}
