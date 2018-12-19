[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
[#include "customizeTag.ftl"/]
[#-- 注:使用自定义标签时,controlName要放在最后 --]
<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
    <td class="ui-state-default jqgrid-rownum">文件名称：</td>
    <td colspan="3">[@input size="20" controlName="fileName"/]</td>
</tr>
<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
    <td class="ui-state-default jqgrid-rownum" style="width: 15%">所属科室：</td>
    <td>[@input size="20" controlName="organizeIdw"/]
    </td>
</tr>
<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
    <td class="ui-state-default jqgrid-rownum">编制人：</td>
    <td colspan="3">[@input size="20" controlName="compactor"/]</td>

</tr>
<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
    <td class="ui-state-default jqgrid-rownum">文件版本号：</td>
    <td colspan="3">[@input size="20" controlName="fileEdition"/]</td>
</tr>
<tr class="ui-widget-content jqgrow ui-row-ltr" style="height: 30px;">
    <td class="ui-state-default jqgrid-rownum">销毁原因：</td>
    <td colspan="3">[@textarea controlName="fileDe"/]</td>
</tr>

<script>
    $(document).ready(function () {
//        var organizeNamel = document.getElementsByName("organizeIdl");
        var organizeNamew = document.getElementsByName("organizeIdw");
	[#--organizeNamel[0].value = "${_GLOBAL_PERSON.department.organizeName}";--]
        organizeNamew[0].value = "${_GLOBAL_PERSON.department.organizeName}"
    });

</script>
[#--<tr style="display: none;">
    <td>[@hidden controlName="reportYear"/]</td>
    <td colspan="2">[@hidden controlName="serialNo"/]</td>
    <td><script>

        $(document).ready(function(){
            document.getElementsByName("fileNo")[0].readOnly = "true";
        });
        function createFileNo() {
            var category = $("#category option:selected").val();
            var fileNos = document.getElementsByName("fileNo");
            var sysDate = new Date();
            var sysYear = sysDate.getFullYear();
            var sysMonth = sysDate.getMonth()+1;
            var sysDate = sysDate.getDate();
            if (category != '' && category.length != null) {
                //获取最大流水号
                var serialNo = "";
                $.ajax({
                    url: "/workflow/submit.do?method=getSerialNo&tableName=Customize_neibubaogao&fieldName=serialNo&fieldYear=reportYear&reportYear="+sysYear+"&categoryName="+encodeURI(category),
                    type: "post",
                    dataType: "json",
                    async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        serialNo = returnValues["serialNo"];
                    }
                });
                //获取部门名称
                var organizeName = "";
                $.ajax({
                    url: "/core/organizeInfor.do?method=getOrganizeName&departmentId="+${_GLOBAL_PERSON.department.organizeId},
                    type: "post",
                    dataType: "json",
                    async: false,	//设置为同步
                    beforeSend: function (xhr) {
                    },
                    complete : function (req, err) {
                        var returnValues = eval("("+req.responseText+")");
                        organizeName = returnValues["_OrganizeName"];
                    }
                });
                fileNos[0].value = category+"-"+sysYear+"-"+serialNo;
                var reportYears = document.getElementsByName("reportYear");
                var serialNos = document.getElementsByName("serialNo");
                reportYears[0].value = sysYear;
                serialNos[0].value = serialNo;
            }else {
                fileNos[0].value = "";
            }
        }
        if ('${_InstanceId}'==null||'${_InstanceId}'=='') {
            $("#category111").change(function(){createFileNo();});
        }
    </script></td>
</tr>--]
[#--<tr style="display: none;">
    <td colspan="4">[@searchControl searchType="input" searchField="fileNo" searchOper="cn" searchName="文件字号"/]</td>
</tr>--]

