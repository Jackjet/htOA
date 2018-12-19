[#ftl][#-- 注:此标签用来指定ftl标签使用方括号,不能删 --]
	[#-- 
		相关变量说明:
		tagValue: true表示新增或修改;false表示生成html模板.
		map: 修改信息时从数据库取出的值.
		formValues: 保存html时从页面取得的值.
		controlName: 控件名称.
		size: input的size长度.
		selectId: select控件的Id.
		optionValueArray: 静态select的下拉数据
		personName: 生成html页面时保存人员姓名的id
		searchType: 控件类型,如:input,select
		searchField: 查询控件的字段名
		searchOper: 查询控件执行的操作,如:=,like
		searchName: 查询控件的中文名称
	 --]

[#--input控件--]
[#macro input controlName size=40]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<input type="text" name="${controlName}" value="${map[mykey]}" size="${size}" />
[/#if]
[/#list]
[#else]
<input type="text" name="${controlName}" size="${size}" />
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<input type="hidden" name="${controlName}" value="${myvalue}"/>${myvalue}
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--hidden控件--]
[#macro hidden controlName]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<input type="hidden" name="${controlName}" value="${map[mykey]}" />
[/#if]
[/#list]
[#else]
<input type="hidden" name="${controlName}" />
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<input type="hidden" name="${controlName}" value="${myvalue}"/>
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]
	
[#--textarea控件--]
[#macro textarea controlName]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<textarea name="${controlName}" cols="60" rows="5">${map[mykey]}</textarea>
[/#if]
[/#list]
[#else]
<textarea name="${controlName}" cols="60" rows="5"></textarea>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<input type="hidden" name="${controlName}" value="${myvalue}"/>${myvalue}
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--单个日期控件--]
[#macro dateInput controlName size=15]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<input type="text" name="${controlName}" value="${map[mykey]}" size="${size}"  onfocus="datePick(this);" readonly="true"/>
[/#if]
[/#list]
[#else]
<input type="text" name="${controlName}" size="${size}"  onfocus="datePick(this);" readonly="true"/>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<input type="hidden" name="${controlName}" value="${myvalue}"/>${myvalue}
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--起止日期控件--]

[#----------]

[#--静态select控件--]
[#macro select selectId controlName optionValueArray=["否", "是"]]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<select id="${selectId}" name="${controlName}" style='color: #ff0700'></select><script>
	var options = "<option value='' >--选择数据--</option>";
		[#list optionValueArray as optionValue]
			[#if map[mykey]??]
				if ('${map[mykey]}' == '${optionValue}') {
					options += "<option value='${optionValue}' selected='selected' >${optionValue}</option>";
				}else {
					options += "<option value='${optionValue}' >${optionValue}</option>";
				}
			[#else]
				options += "<option value='${optionValue}' >${optionValue}</option>";
			[/#if]
		[/#list]
	$('#${selectId}').html(options);
</script>
[/#if]
[/#list]
[#else]
<select id="${selectId}" name="${controlName}" style='color: #ff0700'></select><script>
	var options = "<option value='' >--选择数据--</option>";
	[#list optionValueArray as optionValue]
		[#if optionValue??]
			options += "<option value='${optionValue}' >${optionValue}</option>";
		[/#if]
	[/#list] 
	$('#${selectId}').html(options); 
</script>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
${myvalue}
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--静态必选select控件--]
[#macro mSelect selectId controlName mOptionValueArray=["否", "是"]]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<select id="${selectId}" name="${controlName}"></select><script>
		var options = "";
		[#list mOptionValueArray as optionValue]
			[#if map[mykey]??]
				if ('${map[mykey]}' == '${optionValue}') {
					options += "<option value='${optionValue}' selected='selected'>${optionValue}</option>";
				}else {
					options += "<option value='${optionValue}'>${optionValue}</option>";
				}
			[#else]
				options += "<option value='${optionValue}'>${optionValue}</option>";
			[/#if]
		[/#list]
	$('#${selectId}').html(options);
</script>
[/#if]
[/#list]
[#else]
<select id="${selectId}" name="${controlName}"></select><script>
	var options = "";
	[#list mOptionValueArray as optionValue]
		[#if optionValue??]
			options += "<option value='${optionValue}'>${optionValue}</option>";
		[/#if]
	[/#list] 
	$('#${selectId}').html(options); 
</script>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
${myvalue}
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--部门select控件--]
[#macro depSelect selectId controlName organizeName="organizeName"]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<select id="${selectId}" name="${controlName}"></select><script>
			//获取部门信息
			$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
				var options = "<option value=''>--选择部门--</option>";
				$.each(data._Departments, function(i, n) {
					if ('${map[mykey]}' == n.organizeId) {
						options += "<option value='"+n.organizeId+"' selected='selected'>"+n.organizeName+"</option>";
					}else {
						options += "<option value='"+n.organizeId+"'>"+n.organizeName+"</option>";
					}
				});   
				$('#${selectId}').html(options);   
			});
</script>
[/#if]
[/#list]
[#else]
<select id="${selectId}" name="${controlName}"></select><script>
		//获取部门信息
		$.getJSON("/core/organizeInfor.do?method=getDepartments",function(data) {
			var options = "<option value=''>--选择部门--</option>";
			$.each(data._Departments, function(i, n) {
				options += "<option value='"+n.organizeId+"'>"+n.organizeName+"</option>";   
			});
			$('#${selectId}').html(options);   
		});
</script>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<ul style="padding: 0;margin: 0;" id="${organizeName}"><li></li></ul><script>
		var departmentId = "${myvalue}";
		//获取部门名称
		$.getJSON("/core/organizeInfor.do?method=getOrganizeName&departmentId="+departmentId,function(data) {
			$('#${organizeName}').html('<li style="list-style: none;">'+data._OrganizeName+'</li>');  
		});
</script>
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]
	
[#--用户select控件--]
[#macro usrSelect selectId controlName personName="personName"]
[#if tagValue == true]
[#if map??]
[#list map?keys as mykey]
[#if mykey == controlName]
<select id="${selectId}" name="${controlName}"></select><script>
		//获取用户信息
		$.getJSON("/core/systemUserInfor.do?method=getUsers",function(data) {
			var options = "<option value=''>--选择用户--</option>";
			$.each(data._Users, function(i, n) {
				if ('${map[mykey]}' == n.person.personId) {
					options += "<option value='"+n.person.personId+"' selected='selected'>"+n.person.personName+"</option>";
				}else {
					options += "<option value='"+n.person.personId+"'>"+n.person.personName+"</option>";
				}
			});   
			$('#${selectId}').html(options);   
		});
</script>
[/#if]
[/#list]
[#else]
<select id="${selectId}" name="${controlName}"></select><script>
		//获取用户信息
		$.getJSON("/core/systemUserInfor.do?method=getUsers",function(data) {
			var options = "<option value=''>--选择用户--</option>";
			$.each(data._Users, function(i, n) {
				options += "<option value='"+n.person.personId+"'>"+n.person.personName+"</option>";
			});   
			$('#${selectId}').html(options);
		});
</script>
[/#if]
[#else]
[#list formValues?keys as mykey]
[#if mykey == controlName]
[#list formValues[mykey] as myvalue]
<ul style="padding: 0;margin: 0;" id="${personName}"><li></li></ul><script>
		var personId = "${myvalue}";
		//获取人员名称
		$.getJSON("/core/systemUserInfor.do?method=getPersonName&personId="+personId,function(data) {
			$('#${personName}').html('<li style="list-style: none;">'+data._PersonName+'</li>');  
		});
</script>
[/#list]
[/#if]
[/#list]
[/#if]
[/#macro]
[#----------]

[#--部门和用户联动控件--]

[#----------]

[#--部门班组用户联动控件--]

[#----------]

[#--searchControl查询控件--]
[#macro searchControl searchType searchField searchOper searchName][/#macro]
[#----------]

	