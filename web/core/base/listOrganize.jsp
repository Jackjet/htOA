<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">

<html>
<head>

<title>部门[班组]信息</title>

<script type="text/javascript">
	//新增
	function addOrganize(){
		/*var returnOrgTag = window.showModalDialog("/core/organizeInfor.do?method=edit",null,"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnOrgTag == "refresh") {
			//保存信息后重新加载tab
			//loadTab("/core/structureInfor.do?method=list", "4");
			self.location.reload();
		}*/
        window.open("/core/organizeInfor.do?method=edit","_blank");
	}
	//修改
	function editOrganize(organizeId){
		/*var returnOrgTag = window.showModalDialog("/core/organizeInfor.do?method=edit&organizeId="+organizeId,'',"dialogWidth:700px;dialogHeight:400px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
		if(returnOrgTag == "refresh") {
			//保存信息后重新加载tab
			//loadTab("/core/organizeInfor.do?method=list", "4");
			self.location.reload();
		}*/
        window.open("/core/organizeInfor.do?method=edit&organizeId="+organizeId,"_blank");
	}
	//删除
	function doDelete(organizeId){ //确定删除吗 htOAxitong shnahuc=xuixheng shengxi
		var yes = window.confirm("确定要删除吗？");
		if (yes) {
			var form = document.getElementById('listForm');
			form.action = "<c:url value='/core/organizeInfor.do'/>?method=delete&organizeId="+organizeId;
			form.submit();
			window.location.reload();
		}
	}//确定要删除吗
    $(function () {
//        alert(document.documentElement.clientHeight)
        $(".ui-jqgrid-bdiv").css('height',document.documentElement.clientHeight-240).css('overflow-y','scroll')
        $("#jqgrid-maindiv").css('height',document.documentElement.clientHeight-240)
    })
	function reloadOrg() {
        self.location.reload();
//        loadTab("/core/organizeInfor.do?method=list", "2");
		alert("11")

    }
</script>
</head>
<body onload="init()" style="overflow-y: scroll">
<form id="listForm" action="/core/organizeInfor.do?method=list" method="post">
<div class="ui-jqgrid ui-widget ui-widget-content ui-corner-all" id="gbox_grid_35" dir="ltr" style="width: 100%">
<div class="ui-jqgrid-view" id="gview_grid_35" style="width: 100%">
  <%--<div class="ui-jqgrid-titlebar ui-widget-header ui-corner-top ui-helper-clearfix">
    <span class="ui-jqgrid-title">部门[班组]信息</span>
  </div>--%>
  <div style="width: 100%" class="ui-state-default ui-jqgrid-hdiv">
    <div class="ui-jqgrid-hbox" style="width: 100%">
      <table cellspacing="0" cellpadding="0" border="0" aria-labelledby="gbox_grid_35" role="grid" style="width: 100%" class="ui-jqgrid-htable">
          <thead>
              <tr role="rowheader" class="ui-jqgrid-labels">
                <th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 2%;">
                  <div id="jqgh_rn"></div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 35%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						部门[班组]名称
				    </div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 20%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						部门简称
				    </div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 10%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						是否归口部门
				    </div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 8%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						排序编号
				    </div>
				</th>
				
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 15%;">
				    <span class="ui-jqgrid-resize ui-jqgrid-resize-ltr" style="cursor: col-resize;">&nbsp;</span>
				    <div id="jqgh_title" class="ui-jqgrid-sortable">
						部门经理[主管]
				    </div>
				</th>
		
				<th class="ui-state-default ui-th-column ui-th-ltr" onmouseover="over(this,'ui-state-hover ui-th-column ui-th-ltr')" onmouseout="out(this)" style="width: 10%;">
				    <div id="jqgh_url" class="ui-jqgrid-sortable">
						相关操作
				    </div>
				</th>
				
	    	  </tr>
		   </thead>
       </table>
     </div>
   </div>

	<div class="ui-jqgrid-bdiv" id="jqgrid-maindiv" style="width: 100%">
		<div style="position: relative;">
			<div>
				<table cellspacing="0" cellpadding="0" border="0" id="grid_35" class="ui-jqgrid-btable" role="grid" aria-multiselectable="false" aria-labelledby="gbox_grid_35" style="width: 100%">
					<tbody>
						<c:forEach items="${_OrganizeTree}" var="organize" begin="1" varStatus="index">
							<tr class="ui-widget-content jqgrow ui-row-ltr" onmouseover="over(this,'ui-state-hover jqgrow ui-row-ltr')" onmouseout="out(this)" onclick="clicked(this)">
								<td style="text-align: center; width: 2%;" class="ui-state-default jqgrid-rownum">${index.index}</td>
								<td style="width: 35%;"><c:choose><c:when test="${organize.layer == 1}"><img src="<c:url value='/'/>images/tree_folder4.gif" border="0"/></c:when><c:otherwise><c:forEach begin="0" end="${organize.layer}">&nbsp;</c:forEach><img src="<c:url value='/'/>images/tree_folder3.gif" border="0"/></c:otherwise></c:choose>${organize.organizeName}</td>
								<td style="text-align: center; width: 20%;">${organize.shortName}</td>
								<td style="text-align: center; width: 10%;">${organize.guikou}</td>
								<td style="text-align: center; width: 8%;">${organize.orderNo}</td>
								<td style="text-align: center; width: 15%;">${organize.director.personName}</td>
								<td style="text-align: center; width: 10%;"><a href="javascript:;" onclick="editOrganize('${organize.organizeId}');">[修改]</a> <a href="javascript:;" onclick="doDelete('${organize.organizeId}');">[删除]</a></td>
							</tr>
						</c:forEach>
					</tbody>
				</table>
			</div>
		</div>
	</div>
	
	<div id="pager_35" style="width: 100%" class="ui-state-default ui-jqgrid-pager ui-corner-bottom" dir="ltr">
		<div role="group" class="ui-pager-control" id="pg_pager_35">
			<table cellspacing="0" cellpadding="0" border="0" role="row" style="width: 100%; table-layout: fixed;" class="ui-pg-table">
				<tbody>
					<tr>
						<td align="left" id="pager_35_left">
							<table cellspacing="0" cellpadding="0" border="0" style="float: left; table-layout: auto;" class="ui-pg-table navtable">
								<tbody>
									<tr>
										<td class="ui-pg-button ui-corner-all" title="刷新表格" id="refresh_grid_35">
											<div class="ui-pg-div">
												<span class="ui-icon ui-icon-refresh" id="refresh11" onclick="loadTab('/core/organizeInfor.do?method=list', '4');"></span>
											</div>
										</td>
										<td class="ui-pg-button ui-corner-all" title="点击新增信息" style="cursor: pointer;">
											<div class="ui-pg-div">
												<span class="ui-icon ui-icon-plusthick"></span>
												<span onclick="addOrganize();">新增</span>
											</div>
										</td>
									</tr>
								</tbody>
							</table>
						</td>
						<td align="right" id="pager_35_right">
							<div class="ui-paging-info" style="text-align: right;" dir="ltr">共 ${fn:length(_OrganizeTree)-1} 条</div>
						</td>
					</tr>
				</tbody>
			</table>
		</div>
	</div>
</div>
</div>
</form>
</body>
</html>