<%@ page contentType="text/html; charset=utf-8"%>
<%@ include file="/inc/taglibs.jsp"%>

<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>jQuery formValidator表单验证插件 -- by:猫冬，email:wzmaodong@126.com</title>
<meta name="Author" content="猫冬">
<meta name="description" content="jQuery formValidator表单验证插件" />
<meta name="keywords" content="jQuery,formValidator,插件,表单,验证,插件,javascript,表单验证,提示层" />
<script src="<c:url value="/"/>js/jquery.js" type="text/javascript"></script>
<link type="text/css" rel="stylesheet" href="<c:url value="/"/>components/formvalidator/css/validator.css"></link>
<script src="<c:url value="/"/>components/formvalidator/js/formValidator.js" type="text/javascript" charset="UTF-8"></script>
<script src="<c:url value="/"/>components/formvalidator/js/formValidatorRegex.js" type="text/javascript" charset="UTF-8"></script>
<script language="javascript" src="DateTimeMask.js" type="text/javascript"></script>
<script type="text/javascript">
$(document).ready(function(){
	//$.formValidator.initConfig({onerror:function(){alert("校验没有通过，具体错误请看错误提示")}});
	$.formValidator.initConfig({formid:"form1",onerror:function(msg){alert(msg)},onsuccess:function(){alert('ddd');return false;}});
	$("#test1").formValidator({onshow:"请输入用户名,只有输入\"maodong\"才是对的",onfocus:"用户名至少6个字符,最多10个字符",oncorrect:"该用户名可以注册"}).inputValidator({min:6,max:10,onerror:"你输入的用户名非法,请确认"}).regexValidator({regexp:"username",datatype:"enum",onerror:"用户名格式不正确"})
	    .ajaxValidator({
	    type : "get",
		url : "Default.aspx",
		datatype : "json",
		success : function(data){	
            if( data == "1" )
			{
                return true;
			}
            else
			{
                return false;
			}
		},
		buttons: $("#button"),
		error: function(){alert("服务器没有返回数据，可能服务器忙，请重试");},
		onerror : "该用户名不可用，请更换用户名",
		onwait : "正在对用户名进行合法性校验，请稍候..."
	});
	$("#password1").formValidator({onshow:"请输入密码",onfocus:"密码不能为空",oncorrect:"密码合法"}).inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"密码两边不能有空符号"},onerror:"密码不能为空,请确认"});
	$("#password2").formValidator({onshow:"请输入重复密码",onfocus:"两次密码必须一致哦",oncorrect:"密码一致"}).inputValidator({min:1,empty:{leftempty:false,rightempty:false,emptyerror:"重复密码两边不能有空符号"},onerror:"重复密码不能为空,请确认"}).compareValidator({desid:"password1",operateor:"=",onerror:"2次密码不一致,请确认"});
	$("input:radio[name='sex']").formValidator({tipid:"sexTip",onshow:"请选择你的性别",onfocus:"没有第三种性别了，你选一个吧",oncorrect:"输入正确",defaultvalue:[1]}).inputValidator({min:1,max:1,onerror:"性别忘记选了,请确认"});//.defaultPassed();
	$("#nl").formValidator({forcevalid:true,triggerevent:"change",onshow:"请输入的年龄（1-99岁之间）",onfocus:"只能输入1-99之间的数字哦",oncorrect:"恭喜你,你输对了"}).inputValidator({min:1,max:99,type:"value",onerrormin:"你输入的值必须大于等于1",onerror:"年龄必须在1-99之间，请确认"});//.defaultPassed();
	$("#csny").focus(function(){WdatePicker({skin:'whyGreen',oncleared:function(){$(this).blur();},onpicked:function(){$(this).blur();}})}).formValidator({onshow:"请输入的出生日期",onfocus:"请输入的出生日期，不能全部是0哦",oncorrect:"你输入的日期合法"}).inputValidator({min:"1900-01-01",max:"2000-01-01",type:"date",onerror:"日期必须在\"1900-01-01\"和\"2000-01-01\"之间"});//.defaultPassed();
	$("#sfzh").formValidator({onshow:"请输入15或18位的身份证",onfocus:"输入15或18位的身份证",oncorrect:"输入正确"}).functionValidator({fun:isCardID});
	$("#email").formValidator({onshow:"请输入邮箱",onfocus:"邮箱6-100个字符,输入正确了才能离开焦点",oncorrect:"恭喜你,你输对了",defaultvalue:"@",forcevalid:true}).inputValidator({min:6,max:100,onerror:"你输入的邮箱长度非法,请确认"}).regexValidator({regexp:"^([\\w-.]+)@(([[0-9]{1,3}.[0-9]{1,3}.[0-9]{1,3}.)|(([\\w-]+.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(]?)$",onerror:"你输入的邮箱格式不正确"});
	$("#xueli").formValidator({onshow:"请选择你的学历",onfocus:"学历必须选择",oncorrect:"谢谢你的配合",defaultvalue:"a"}).inputValidator({min:1,onerror: "你是不是忘记选择学历了!"}).defaultPassed();
	$("#ewjy").formValidator({onshow:"无论你输入什么都会提示你额外校验出错",onfocus:"如果你输入\"猫冬\"，额外校验就会成功",oncorrect:"额外校验成功"}).inputValidator({min:1,onerror:"这里至少要一个字符,请确认"}).functionValidator({
	    fun:function(val,elem){
	        if(val=="猫冬"){
			    return true;
		    }else{
			    return "额外校验失败,想额外校验通过，请输入\"猫冬\""
		    }
		}
	});
	$("#Tel_country").formValidator({tipid:"teltip",onshow:"请输入国家区号",onfocus:"国家区号2位数字",oncorrect:"恭喜你,你输对了",defaultvalue:"86"}).regexValidator({regexp:"^\\d{2}$",onerror:"国家区号不正确"});
	$("#Tel_area").formValidator({tipid:"teltip",onshow:"请输入地区区号",onfocus:"地区区号3位或4位数字",oncorrect:"恭喜你,你输对了"}).regexValidator({regexp:"^\\d{3,4}$",onerror:"地区区号不正确"});
	$("#Tel_number").formValidator({tipid:"teltip",onshow:"请输入电话号码",onfocus:"电话号码7到8位数字",oncorrect:"恭喜你,你输对了"}).regexValidator({regexp:"^\\d{7,8}$",onerror:"电话号码不正确"});
	$("#Tel_ext").formValidator({tipid:"teltip",onshow:"请输入分机号码",onfocus:"分机号码1到5位数字",oncorrect:"恭喜你,你输对了"}).regexValidator({regexp:"^\\d{1,5}$",onerror:"分机号码不正确"});
	$("input:checkbox[@name='checkbox1']").formValidator({tipid:"test3Tip",onshow:"请选择你的兴趣爱好（至少选一个）",onfocus:"你至少选择1个",oncorrect:"恭喜你,你选对了"}).inputValidator({min:1,onerror:"你选的个数不对"});
	$("input:checkbox[@name='checkbox8']").formValidator({tipid:"test2Tip",onshow:"请选择你的兴趣爱好(至少选择2个,最多选择3个)",onfocus:"你至少选择2个,最多选择3个",oncorrect:"恭喜你,你选对了",defaultvalue:["7","8"]}).inputValidator({min:2,max:3,onerror:"你选的个数不对(至少选择2个,最多选择3个)"});
	$("input:radio[@name='radio']").formValidator({tipid:"aiguoTip",onshow:"爱国的人一定要选哦",onfocus:"你得认真思考哦",oncorrect:"不知道你爱不爱，反正你是选了",defaultvalue:"4"}).inputValidator({min:1,max:1,onerror:"难道你不爱国？你给我选！！！！"}).defaultPassed();
	$("#shouji").formValidator({empty:true,onshow:"请输入你的手机号码，可以为空哦",onfocus:"你要是输入了，必须输入正确",oncorrect:"谢谢你的合作",onempty:"你真的不想留手机号码啊？"}).inputValidator({min:11,max:11,onerror:"手机号码必须是11位的,请确认"}).regexValidator({regexp:"mobile",datatype:"enum",onerror:"你输入的手机号码格式不正确"});;
	$("#lxdh").formValidator({empty:true,onshow:"请输入你的联系电话，可以为空哦",onfocus:"格式例如：0577-88888888",oncorrect:"谢谢你的合作",onempty:"你真的不想留联系电话了吗？"}).regexValidator({regexp:"^[[0-9]{3}-|\[0-9]{4}-]?([0-9]{8}|[0-9]{7})?$",onerror:"你输入的联系电话格式不正确"});
	$("#selectmore").formValidator({onshow:"按住CTRL可以多选",onfocus:"按住CTRL可以多选,至少选择2个",oncorrect:"谢谢你的合作",defaultvalue:[0,1,2]}).inputValidator({min:2,onerror:"至少选择2个"});
	$("#ms").formValidator({onshow:"请输入你的描述",onfocus:"描述至少要输入10个汉字或20个字符",oncorrect:"恭喜你,你输对了",defaultvalue:"这家伙很懒，什么都没有留下。"}).inputValidator({min:20,onerror:"你输入的描述长度不正确,请确认"});
});
function test(obj)
{
	if(obj.value=="不校验身份证")
	{
		$("#sfzh").attr("disabled",true).unFormValidator(true);
		obj.value = "校验身份证";
	}
	else
	{
		$("#sfzh").attr("disabled",false).unFormValidator(false);
		obj.value = "不校验身份证";
	}
}
function test1(obj)
{
	if(obj.value=="全角字符当做1个长度")
	{
		$.formValidator.getInitConfig("1").wideword = false;
		obj.value = "全角字符当做2个长度";
	}
	else
	{
		$.formValidator.getInitConfig("1").wideword = true;
		obj.value = "全角字符当做1个长度";
	}
	
}
</script>
<style type="text/css" media="all">
body,div{font-size:12px;}
</style>
</head>

<body>
<h3>jQuery formValidator表单验证插件示例</h3>
<p>只有一个校验组的示例</p>
<p><a href="http://wzmaodong.cnblogs.com">猫冬的博客</a></p>
<input type="button" onClick="test(this)" value="不校验身份证"  />
<label>
<input type="button" name="Submit" onClick="test1(this)" value="全角字符当做1个长度">请看最后的"你的描述"控件
</label>
<form action="" method="post" name="form1" id="form1">
  <table border="0px" style="font-size:12px" width="630px">
    <tr> 
      <td align="right">用户名:</td>
      <td><input type="text" id="test1" style="width:120px" value="maodong" /></td>
      <td><div id="test1Tip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">密码:</td>
      <td><input type="password" id="password1" style="width:120px" /></td>
      <td><div id="password1Tip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">重复密码:</td>
      <td><input type="password" id="password2" style="width:120px" /></td>
      <td><div id="password2Tip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">你的性别:</td>
      <td><input type="radio" id="sex" value="1" name="sex" />
        男&nbsp;&nbsp;&nbsp;&nbsp; <input type="radio" id="sex1" value="2" name="sex" />
        女</td>
      <td><div id="sexTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">你的年龄:</td>
      <td><input type="text" id="nl" style="width:120px" value="26" class="fv_input_text_default" /></td>
      <td><div id="nlTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">出生日期:</td>
      <td><input type="text" id="csny" style="width:120px" value="1982-09-21" /></td>
      <td><div id="csnyTip" style="width:300px"></div></td>
    </tr>
    <tr>
      <td align="right">身份证号</td>
      <td><input name="text2" type="text" id="sfzh" style="width:150px" value="" /></td>
      <td><div id="sfzhTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">电子邮箱:</td>
      <td><input type="text" id="email" style="width:120px" /></td>
      <td><div id="emailTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">你的学历:</td>
      <td> <select name="select1" id="xueli">
          <option value="">－－请选择你的学历－－</option>
          <option value="a">专科</option>
          <option value="b">本科</option>
          <option value="c">研究生</option>
          <option value="e">硕士</option>
          <option value="d">博士</option>
        </select> </td>
      <td><div id="xueliTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">额外校验:</td>
      <td><input name="text" type="text" id="ewjy" style="width:120px" /></td>
      <td><div id="ewjyTip" style="width:270px"></div></td>
    </tr>
    <tr> 
      <td colspan="3"> 国家区号 
        <input id="Tel_country" style="width: 20px;" value="" />
        -地区区号 
        <input id="Tel_area" style="width: 35px;" />
        -电话号码 
        <input id="Tel_number" style="width: 60px;" />
        -分机号码 
        <input id="Tel_ext" style="width: 30px;" /> </td>
    </tr>
    <tr>
      <td colspan="3"><div id="teltip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">兴趣爱好1:</td>
      <td colspan="2"> <input type="checkbox" name="checkbox1" id="qq1"/>
        乒乓球 
        <input type="checkbox" name="checkbox1" id="qq2" value="1" />
        羽毛球 
        <input type="checkbox" name="checkbox1" id="qq3" value="2" />
        上网 
        <input type="checkbox" name="checkbox1" id="qq4" value="3" />
        旅游 
        <input type="checkbox" name="checkbox1" id="qq5" value="4" />
        购物 </td>
    </tr>
    <tr> 
      <td colspan="3"><div id="test3Tip" style="width:350px"></div></td>
    </tr>
    <tr> 
      <td align="right">兴趣爱好2:</td>
      <td colspan="2"> <input type="checkbox" name="checkbox8" id="pp0"/>
        乒乓球 
        <input type="checkbox" name="checkbox8" id="pp1" value="5" />
        羽毛球 
        <input type="checkbox" name="checkbox8" id="pp7" value="6" />
        上网 
        <input type="checkbox" name="checkbox8" id="pp8" value="7" />
        旅游 
        <input type="checkbox" name="checkbox8" id="pp9" value="8" />
        购物 
        <input type="checkbox" name="checkbox8" id="pp6" value="9" />
        睡觉 </td>
    </tr>
    <tr> 
      <td colspan="3"><div id="test2Tip" style="width:350px"></div></td>
    </tr>
    <tr> 
      <td align="right">你爱国吗:</td>
      <td colspan="2"> <input type="radio" name="radio" value="1" id="bb1"  />
        不爱 
        <input type="radio" name="radio" id="bb2" value="2" />
        不发表意见 
        <input type="radio" name="radio" id="nn3" value="3" />
        爱 
        <input type="radio" name="radio" id="nn4" value="4" />
        爱死它了 </td>
    </tr>
    <tr> 
      <td colspan="3"><div id="aiguoTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">手机号码:</td>
      <td><input type="text" id="shouji" style="width:120px" /></td>
      <td><div id="shoujiTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right">联系电话:</td>
      <td><input type="text" id="lxdh" style="width:120px" /></td>
      <td><div id="lxdhTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right" valign="top">多选select控件:</td>
      <td valign="top">
	    <select name="select" size="3" id="selectmore" multiple>
		  <option value="1">多选1</option>
		  <option value="2">多选2</option>
		  <option value="3">多选3</option>
		</select>(按住ctrl键多选)
      </td>
      <td><div id="selectmoreTip" style="width:250px"></div></td>
    </tr>
    <tr> 
      <td align="right" valign="top">你的描述:</td>
      <td colspan="2" valign="top"> <textarea id="ms" cols="50" rows="10">这里是十个中文字符哦</textarea> 
      </td>
    </tr>
    <tr> 
      <td colspan="3"><div id="msTip" style="width:250px"></div></td>
    </tr>
  </table>
  <br />
  
    <input type="submit" name="button" id="button" value="提交" />
  
</form>
</body>
</html>
<script defer="defer" src="datepicker/WdatePicker.js" type="text/javascript"></script>