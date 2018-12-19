package com.kwchina.core.common.vo;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import com.kwchina.core.sys.CoreConstant;

public class BaseVo {
	
	private Integer pageNo;			//页码
	private Integer pageSize = 10;	//每页显示的信息条数
	private String orderBy = "";	//按字段排序
	private String order = "";		//升序or降序(asc or desc)
	private String url = "";

	
	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

	public String getOrderBy() {
		return orderBy;
	}

	public void setOrderBy(String orderBy) {
		this.orderBy = orderBy;
	}

	public Integer getPageNo() {
		return pageNo;
	}

	public void setPageNo(Integer pageNo) {
		if (pageNo != null && pageNo.intValue() > 0)
			this.pageNo = pageNo;
		else
			this.pageNo = 1;
	}

	public Integer getPageSize() {
		return pageSize;
	}

	public void setPageSize(Integer pageSize) {
		if (pageSize != null && pageSize.intValue() > 0)
			this.pageSize = pageSize;
		else
			this.pageSize = 10;
	}

	public void constructUrl(HttpServletRequest request) {
		Map map = request.getParameterMap();
		Object[] obj = map.entrySet().toArray();
		if (obj.length > 0) {
			this.url = "";
			for (int i = 0; i < obj.length; i++) {
				Entry e = (Entry) obj[i];
				String tmp = String.valueOf(e.getKey());
				String[] value = (String[]) e.getValue();
				System.out.println(tmp.indexOf("jmesa"));
				if (tmp.indexOf("jmesa") >= 0) {
					try {
						url += tmp + "=" + URLEncoder.encode(value[0], CoreConstant.ENCODING)
								+ "&";
					} catch (UnsupportedEncodingException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			}
			if (url.length() > 0)
				this.url = this.url.substring(0, url.length() - 1);
		}
	}

}
