<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>


<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd">
<meta http-equiv="X-UA-Compatible" content="IE=edge,Chrome=1" />
<script src="<c:url value="/"/>datePicker/WdatePicker.js" type="text/javascript"></script>
<script src="<c:url value="/"/>components/jquery-1.4.2.js" type="text/javascript"></script> <!--jquery包-->

<script src="<c:url value="/"/>components/jqgrid/js/jquery-ui-1.8.2.custom.min.js" type="text/javascript"></script><!--jquery ui-->
<script src="<c:url value="/"/>components/jquery.layout.js" type="text/javascript"></script><!--jquery 布局-->
<script src="<c:url value="/"/>components/jqgrid/js/grid.locale-cn.js" type="text/javascript"></script> <!--jqgrid 中文包-->
<script src="<c:url value="/"/>components/jqgrid/js/jquery.jqGrid.min.js" type="text/javascript"></script> <!--jqgrid 包-->
<script src="<c:url value="/"/>js/commonFunction.js"></script>
<script src="<c:url value="/"/>js/inc_javascript.js"></script>
<script src="<c:url value="/"/>js/multisearch.js"></script> <!--加载模态多条件查询相关js-->
<script src="<c:url value="/"/>js/changeclass.js"></script> <!--用于改变页面样式-->

<%--<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/jquery-ui-1.7.2.custom.css" />--%>
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>css/base/jquery-ui-1.9.2.custom.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/ui.jqgrid.css" />
<link rel="stylesheet" type="text/css" media="screen" href="<c:url value="/"/>components/jqgrid/css/tabstyle.css" />

<style>
	li,a:focus{outline:none;!important; color:white!important; }
	#multiSearchDialog100{
		height: 260px!important;
	}
</style>
<title></title>
<body style="border:1px solid #0DE8F5;border-radius: 5px">
<div id="viewUser" style="display:none;"></div>
<div id="dialog" align="center" style="height:auto;display: none" title="新增选择">
	<table style="text-align: center">
		<tr>
			<td>
				<select id="fs">
					<option value="0">新增</option>
					<option value="1">修改</option>
					<option value="2">销毁</option>
				</select>
			</td>
			<td>
				<select id="st">
					<option value="0">文件</option>
					<option value="1">流程</option>
					<option value="2">文件+流程</option>
				</select>
			</td>

		</tr>
	</table>
</div>

  		<div>
			<table id="list80"></table>
			<div id="pager80"></div>
		</div>

<div id="multiSearchDialog80" style="display: none;overflow: auto">
	<table >
		<tbody>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="instanceTitle"/>标题：
				<input type="hidden" class="searchOper" value="cn"/>
			</td>
			<td>
				<input type="text" class="searchString" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="type"/>类型：
				<input type="hidden" class="searchOper" value="cn"/>
			</td>
			<td>
				<%--<input type="text" class="searchString" id="typeFF" size="25"/>--%>
				<select class="searchString" id="typeFF">
					<option value="请选择">请选择</option>
					<option value="新增文件">新增文件</option>
					<option value="新增流程">新增流程</option>
					<option value="新增文件流程">新增文件流程</option>

					<option value="修改文件">修改文件</option>
					<option value="修改流程">修改流程</option>
					<option value="修改文件流程">修改文件流程</option>

					<option value="销毁文件">销毁文件</option>
					<option value="销毁流程">销毁流程</option>
					<option value="销毁文件流程">销毁文件流程</option>

				</select>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden"  class="searchField" value="department.organizeId"/>申请部门：
				<input type="hidden" class="searchOper" value="eq"/>
			</td>
			<td>
				<select class="searchString" id="departmentId"></select>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="applier.personId"/>申请人：
				<input type="hidden" class="searchOper" value="eq"/>
			</td>
			<td>
				<select class="searchString" id="applierId"></select>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="fileName"/>文件名：
				<input type="hidden" class="searchOper" value="cn"/>
			</td>
			<td>
				<input type="text" class="searchString" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="flowName"/>流程名：
				<input type="hidden" class="searchOper" value="cn"/>
			</td>
			<td>
				<input type="text" class="searchString" size="25"/>
			</td>
		</tr>
		<tr>
			<td>
				<input type="hidden" class="searchField" value="fileNo"/>文件编号：
				<input type="hidden" class="searchOper" value="cn"/>
			</td>
			<td>
				<input type="text" class="searchString" size="25"/>
			</td>
		</tr>

		</tbody>
		<%--<tbody id="searchFields"></tbody>--%>
	</table>
</div>
</body>


<script type="text/javascript">


    //新增
    function addInfor(){
        $("#dialog").dialog({
            resizable: false,
            height:140,
            modal: true,
            buttons: {

                "确定": function() {
                    var fs = $("#fs option:selected").val();
                    var st = $("#st option:selected").val();
//                       alert(fs)
//                       alert(st)
                    if(fs==0){
                        if(st==0){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=90", "_blank");
                        }
                        if(st==1){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=94", "_blank");
                        }
                        if(st==2){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=98", "_blank");
                        }
                    }
                    if(fs==1){
                        if(st==0){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=92", "_blank");
                        }
                        if(st==1){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=95", "_blank");
                        }
                        if(st==2){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=99", "_blank");
                        }
                    }
                    if(fs==2){
                        if(st==0){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=93", "_blank");
                        }
                        if(st==1){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=96", "_blank");
                        }
                        if(st==2){
                            window.open("/workflow/instanceInfor.do?method=edit&flowId=100", "_blank");
                        }
                    }
                    $( this ).dialog( "close" );
                },
                "取消": function() {
                    $( this ).dialog( "close" );
                }
            }
        });
        return ;


    }
    //授权
    function doAuthorize(){
        //获取选择的行的Id
        var rowIds = jQuery("#list80").jqGrid('getGridParam','selarrrow');

        if(rowIds != null && rowIds.length > 0){

            window.open("/workflow/instanceInfor.do?method=editInforsRight&rowIds="+rowIds, "_blank");
        }else{
            alert("请选择要授权的数据！");
        }
    }
    //批量删除
    function deleteInfor(){
        //获取选择的行的Id
        var rowIds = jQuery("#list80").jqGrid('getGridParam','selarrrow');

        if(rowIds != null && rowIds.length > 0){
            var yes = window.confirm("确定要删除吗？");
            if (yes) {
                $.ajax({
                    url: "/workflow/instanceInfor.do?method=recycle&flowId=80&rowIds="+rowIds,	//删除数据的url
                    cache: false,
                    type: "POST",
                    dataType: "html",
                    beforeSend: function (xhr) {
                    },

                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        if (returnValues["_CanRecycle"]) {
                            alert("数据已经删除！");
                            $("#list80").trigger("reloadGrid");
                        }else {
                            alert("对不起,您无权进行该操作,请与系统管理员联系!");
                        }
                    }
                });
            }
        }else {
            alert("请选择要删除的数据！");
        }
    }
    //自定义显示标题
    function formatTitle(cellValue, options, rowObject) {
        var html = '';
        html = "<a href='javascript:;' onclick='doView("+options.rowId+")'>" + cellValue + "</a>";
        return html;
    }
    function formatStatus(cellValue, options, rowObject) {
        var html = '';
        html = "<font color='red'>" + cellValue + "</font>";
        return html;
    }
    //查看
    function doView(rowId){
        //window.showModalDialog("/workflow/instanceInfor.do?method=view&instanceId="+rowId,'',"dialogWidth:900px;dialogHeight:800px;center:Yes;dialogTop: 200px; dialogLeft: 300px;");
        window.open("/workflow/instanceInfor.do?method=view&instanceId="+rowId, "_blank");
    }
    //自定义具有浏览权限人员
    function formatViewUser(cellValue, options, rowObject) {
        var html = '';
        html += "<a href='#none' class='tooltip' onClick='getViewUsers(this,"+rowObject.instanceId+");'";
        html +=" title='单击查看此公文的浏览人员' style='z-index:9999;'>";
        html += "<img src='<c:url value="/"/>img/llr.png' width='20' height='16' border='0'/>"
        html +=" </a>";
        return html;
    }
    //自定义查看统计
    function formatViewCount(cellValue, options, rowObject) {
        var html = '';
        html += "<a href='<c:url value="/"/>workflow/instanceInfor.do?method=viewCount&instanceId="+rowObject.instanceId+"' target='_blank' class='tooltip' ";//  onClick='getViewUsers(this,"+rowObject.instanceId+");'";
        html +=" title='单击查看此公文的审核统计信息' style='z-index:9999;'>";
        html += "<img src='<c:url value="/"/>images/menu/asset.gif' width='16' height='16' border='0'/>"
        html +=" </a>";
        return html;
    }
    function getViewUsers(obj,instanceId){
        var html = "<br/>";

        $.getJSON("/workflow/instanceInfor.do?method=getViewUsers&instanceId="+instanceId,function(data) {
            if (data != null) {
                $.each(data._ViewUsers, function(i, n) {
                    //获取人员名称
                    $.ajax({
                        url: "/core/systemUserInfor.do?method=getPersonName&personId="+n.personId,
                        type: "get",
                        dateType: "json",
                        async: false,
                        success: function(datas) {
                            var _data = eval("("+datas+")");
                            var personName = _data._PersonName;
                            html += "<span style='white-space:nowrap;'><font color=yellow>"+personName+"</font></span>&nbsp;&nbsp;";
                            if((i+1)%7 == 0){
                                html += "<br/><br/>";
                            }
                        }
                    });

                });
            }
        });
        $("#viewUser").dialog({
            autoOpen: false,
            modal: true,
            resizable: true,
            width: 350,
            title: "具有浏览权限的人员",
            buttons: {
                "关闭": function(){$("#viewUser").dialog("close");}
            }
        });
        $("#viewUser").html("<br/>加载中，请稍候...");
        $("#viewUser").dialog("open");

        setTimeout(function(){$("#viewUser").html(html);},"500");
    }

            //打开查询窗口并进行窗口初始化
            var multiSearchParams = new Array();
            function openDialog() {

                multiSearchParams[0] = "#list80";				//列表Id
                multiSearchParams[1] = "#multiSearchDialog80";//查询模态窗口Id
                initSearchDialog();

                //获取自定义表单查询字段
//                getSearchFields();

                $(multiSearchParams[1]).dialog("open");

            }
            //获取自定义表单查询字段
//            function getSearchFields() {
//                $.ajax({
//                    url: "/workflow/instanceInfor.do?method=getSearchFields&flowId=100",
//                    type: "post",
//                    dataType: "json",
//                    async: false,	//设置为同步
//                    beforeSend: function (xhr) {
//                    },
//                    complete : function (req, err) {
//                        $('#searchFields').empty();
//                        var returnValues = eval("("+req.responseText+")");
//                        console.info(returnValues)
//                        if (returnValues["_SearchFields"] != null && returnValues["_SearchFields"].length > 0) {
//                            for(var i=0;i<returnValues["_SearchFields"].length;i++) {
//                                var field = returnValues['_SearchFields'][i];
//                                if (field != null) {
//                                    var searchType = field[0]["searchType"];
//                                    var searchField = field[1]['searchField'];
//                                    var searchOper = field[2]['searchOper'];
//                                    var searchName = field[3]["searchName"];
//                                    var searchFieldHtml = "<tr><td><input type='hidden' class='searchField' value='"+searchField+"'/>"+searchName+"：";
//                                    searchFieldHtml += "<input type='hidden' class='searchOper' value='"+searchOper+"'/></td>";
//                                    if (searchType=="input") {
//                                        //一般input
//                                        searchFieldHtml += "<td><input type='text' class='searchString' size='25'/></td></tr>";
//                                    }else if (searchType=="dateInput") {
//                                        //日期input
//                                        searchFieldHtml += "<td><input type='text' class='searchString' onclick='WdatePicker()' readonly='true' size='12'/></td></tr>";
//                                    }else if (searchType=="orgSelect") {
//                                        //部门下拉
//                                        $.ajax({
//                                            url: "/core/organizeInfor.do?method=getDepartments",
//                                            type: "post",
//                                            dataType: "json",
//                                            async: false,	//设置为同步
//                                            beforeSend: function (xhr) {
//                                            },
//                                            complete : function (req, err) {
//                                                var returnValues = eval("("+req.responseText+")");
//                                                searchFieldHtml += "<td><select class='searchString' id='"+searchField+"'><option value=''>请选择</option>";
//                                                $.each(returnValues["_Departments"], function(i, n) {
//                                                    searchFieldHtml += "<option value='"+ n.organizeId + "'>" + n.organizeName + "</option>";
//                                                });
//                                                searchFieldHtml += "</select></td>";
//                                            }
//                                        });
//                                    }else if (searchType=="usrSelect") {
//
//                                        //用户下拉
//                                        $.ajax({
//                                            url: "/core/systemUserInfor.do?method=getUsers",
//                                            type: "post",
//                                            dataType: "json",
//                                            async: false,	//设置为同步
//                                            beforeSend: function (xhr) {
//                                            },
//                                            complete : function (req, err) {
//                                                var returnValues = eval("("+req.responseText+")");
//                                                searchFieldHtml += "<td><select class='searchString' id='"+searchField+"'><option value=''>请选择</option>";
//                                                $.each(returnValues["_Users"], function(i, n) {
//                                                    searchFieldHtml += "<option value='"+ n.personId + "'>" + n.person.personName + "</option>";
//                                                });
//                                                searchFieldHtml += "</select></td>";
//                                            }
//                                        });
//                                    }
//                                    searchFieldHtml += "</tr>";
////                                    $('#searchFields').append(searchFieldHtml);
//                                }
//                            }
//                        }
//                    }
//                });
//            }

            //自定义部门信息
            function formatOrg(cellValue, options, rowObject) {
                var html = '';
                //获取部门名称
                $.ajax({
                    url: "/core/organizeInfor.do?method=getOrganizeName&departmentId="+cellValue,
                    type: "get",
                    dateType: "json",
                    async: false,
                    success: function(datas) {
                        var data = eval("("+datas+")");
                        html += data._OrganizeName;
                    }
                });
                return html;
            }

			var flowName = "文件流程";

			//获取自定义列
    		if("${_SYSTEM_USER.userType}" == "1") {
                var col_names = ['Id','正文标题', '类型', '状态',  '创建时间', '申请人', '编制人','所属部门', '浏览人', '文件名称', '流程名称', '文件科室', '流程科室', '文件编号', '流程版本', '文件版本', '流程支持文件',
					/*'流程修改位置及原因','文件修改位置及原因',
					'流程修改后内容','文件修改后内容',*/ '受影响的其他文件', '受影响的其他流程',/*'流程销毁原因','文件销毁原因',*/'统计'];
				var col_model = [
					    {name: 'instanceId', index: 'instanceId',
                   			 width:0, sorttype: "int", search: false, key: true, hidden:true},
                 	    {name: 'instanceTitle', index: 'instanceTitle',align: 'center', width:360,sortable: true, sorttype: "string",
                        formatter: formatTitle},
						{name: 'type', index: 'type', align: 'center', sortable: false, sorttype:"string"},
						{name: 'status',index: 'status',align: 'center',sortable: false,sorttype: "string",formatter: formatStatus},
						{name: 'updateTime', index:'updateTime', width:150, align:'center'},
						{name: 'applier', index:'applier', width:100, align:'center'},
                  	    {name: 'compactor', index: 'compactor', align:'center',sortable:false,sorttype:"string"},
						{name:'department',index:'department', width:100, align:'center'},
						{name:'do', width:80, align:'center',sortable:false, formatter:formatViewUser},
                   		{name: 'fileName',index: 'fileName',align:'center', sortable:false,sorttype: "string" /*,formatter:formatOrg*/},
						{name:'flowName',index:'flowName',align: 'center',sortable:false, sorttype: "string" /*,formatter:formatOrg*/},
						{name: 'organizeIdw', index: 'organizeIdw',align:'center', sortable:false, sorttype:"string"
							/*,formatter:formatOrg*/},
						{name: 'organizeIdl', index: 'organizeIdl', align:'center'
                            , sortable:false
                            , sorttype:"string"
							/*,formatter:formatOrg*/}, //					{name:'fileName',index:'fileName',width:40, sortable:true, sorttype:"string"},
						{name: 'fileNo', index: 'fileNo', align:'center',sortable:false,
                            sorttype:"string"},
						{name:'flowEdition',index:'flowEdition',align:'center', sortable:false, sorttype:"string"},
						{name: 'fileEdition',index: 'fileEdition', align:'center', sortable:false, sorttype:"string"},
						{name: 'flowSup', index:'flowSup', align: 'center',sortable: false, sorttype: "string"}, //					{name:'flowUpdatePo',index:'flowUpdatePo',width:200,align:'center', sortable:true, sorttype:"string"}, //					{name:'fileUpdatePo',index:'fileUpdatePo',width:200,align:'center',sortable:true, sorttype:"string"},
                    //					{name:'flowNew',index:'flowNew',width:200,align:'center',sortable:true, sorttype:"string"},
                    //					{name:'fileNew',index:'fileNew',width:200,align:'center', sortable:true, sorttype:"string"},
						{name:'beAffw',index: 'beAffw',width:200,align:'center', sortable:false, sorttype:"string"},
						{name:'beAffl',index: 'beAffl',align:'center', sortable:false, sorttype:"string"}, //					{name:'flowDe',index:'flowDe',align:'center', sortable:true, sorttype:"string"}, //					{name:'fileDe',index:'fileDe',align:'center',sortable:true, sorttype:"string"},
						{name:'status', index: 'status', align: 'center',sortable:false,formatter: formatViewCount}

					  ];
			}
			else{
                var col_names = ['Id', '正文标题', '类型', '状态', '创建时间', '申请人', '编制人', '所属部门', '浏览人', '文件名称', '流程名称', '文件科室', '流程科室', '文件编号', '流程版本', '文件版本', '流程支持文件',
					/*'流程修改位置及原因','文件修改位置及原因',
					 '流程修改后内容','文件修改后内容',*/ '受影响的其他文件', '受影响的其他流程'/*'流程销毁原因','文件销毁原因',*/];
				var col_model = [{
					name: 'instanceId', index: 'instanceId',
					width:0, sorttype: "int", search: false, key: true, hidden:true},
                    {
                        name: 'instanceTitle',
                        index: 'instanceTitle',
                        align: 'center',
                        width:360,
                        sortable: true,
                        sorttype: "string",
                        formatter: formatTitle},
					{name: 'type', index: 'type', align: 'center', sortable: false, sorttype:"string"},
					{
						name: 'status',
						index: 'status',
						align: 'center',
						sortable: false,
						sorttype: "string",
						formatter: formatStatus},

					{name: 'updateTime', index:'updateTime', width:150, align:'center'},
					{name: 'applier', index:'applier', width:100, align:'center'},
                    {name: 'compactor', index: 'compactor', align:'center',sortable:false,
                        sorttype:"string"},
					{name:'department',index:'department', width:100, align:'center'},
					{name:'do', width:80, align:'center',sortable:false, formatter:formatViewUser},
					{name: 'fileName',index: 'fileName',align:'center', sortable:false,
						sorttype: "string" /*,formatter:formatOrg*/},
					{name:'flowName',index:'flowName',align: 'center',
						sortable:false, sorttype: "string" /*,formatter:formatOrg*/},
					{name: 'organizeIdw', index: 'organizeIdw',align:'center', sortable:false
						, sorttype:"string"
						/*,formatter:formatOrg*/},
					{
						name: 'organizeIdl', index: 'organizeIdl', align:'center'
						, sortable:false
						, sorttype:"string"
						/*,formatter:formatOrg*/}, //					{name:'fileName',index:'fileName',width:40, sortable:true, sorttype:"string"},
					{name: 'fileNo', index: 'fileNo', align:'center',sortable:false,
						sorttype:"string"},
					{name:'flowEdition',index:'flowEdition',align:'center', sortable:false, sorttype:"string"},
					{name: 'fileEdition',index: 'fileEdition', align:'center', sortable:false, sorttype:"string"},
					{name: 'flowSup', index:'flowSup', align: 'center',sortable: false, sorttype: "string"}, //					{name:'flowUpdatePo',index:'flowUpdatePo',width:200,align:'center', sortable:true, sorttype:"string"}, //					{name:'fileUpdatePo',index:'fileUpdatePo',width:200,align:'center',sortable:true, sorttype:"string"},
					//					{name:'flowNew',index:'flowNew',width:200,align:'center',sortable:true, sorttype:"string"},
					//					{name:'fileNew',index:'fileNew',width:200,align:'center', sortable:true, sorttype:"string"},
					{name:'beAffw',index: 'beAffw',width:200,align:'center', sortable:false, sorttype:"string"},
					{name:'beAffl',index: 'beAffl',align:'center', sortable:false, sorttype:"string"}, //					{name:'flowDe',index:'flowDe',align:'center', sortable:true, sorttype:"string"}, //					{name:'fileDe',index:'fileDe',align:'center',sortable:true, sorttype:"string"},
//					{name:'status', index: 'status', align: 'center',sortable:false,formatter: formatViewCount}

					];
			}


			//加载表格数据
			var $mygrid = jQuery("#list80").jqGrid({
                url:"/workflow/instanceInfor.do?method=list_File&flowId=100",
	            rownumbers: true, datatype: "json", width:"90%", autowidth: true,
				height: document.documentElement.clientHeight-140,
                colNames: col_names,//表的第一行标题栏
	            colModel: col_model,
	            sortname: 'instanceId',
	            sortorder: 'desc',
	            multiselect: true,	//是否支持多选,可用于批量删除
	            viewrecords: true,
	            rowNum: 20,
	            rowList: [10,20,30],
//	            scroll: false,
//	            scrollrows: false,
                shrinkToFit:false,
	            jsonReader:{
	                repeatitems: false
	            },
	            pager: "#pager80"
//	            caption: flowName,

	        }).navGrid('#pager80',{edit:false,add:false,del:false,search:false}).closest(".ui-jqgrid-bdiv")/*.css({ "overflow-x" : "auto" })*/;
			
			//自定义按钮
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"刷新表格", title:"点击刷新表格",buttonicon:'none', onClickButton: function refresh(){window.location.reload();}
			});
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"新增", title:"点击新增信息", buttonicon:'ui-icon-plusthick', onClickButton: addInfor
			});
            jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
                caption:"<span style='color: red;'>批量删除</span>", title:"点击批量删除", buttonicon:'ui-icon-trash', onClickButton: deleteInfor
            });
            jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
                caption:"查询", title:"点击查询", buttonicon:'ui-icon-search', onClickButton: openDialog
            });
		//如果是主办人或者管理员
		if(${_SYSTEM_USER.personId == param.chargerId || _SYSTEM_USER.userType == 1}){
			jQuery("#list80").jqGrid('navButtonAdd','#pager80', {
				caption:"批量授权", title:"点击批量授权", buttonicon:'ui-icon-script', onClickButton: doAuthorize
			});
	    }




    /** 查询条件中的部门,班组,用户下拉联动 */
    //部门信息初始化
    $('#departmentId').selectInit();
    //加载部门及联动信息
    $.loadDepartments("departmentId", null, "applierId");


//    $('#typeFF').selectInitType();




//    $.loadDepartments("typeFF", null, "applierId");
</script>