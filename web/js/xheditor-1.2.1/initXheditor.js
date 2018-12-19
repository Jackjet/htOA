function initEditor(id){
	$("#"+id).xheditor({skin:'nostyle',upImgUrl:"/js/xheditor-1.2.1/upload.jsp?immediate=1",onUpload:insertUpload,upImgExt:"jpg,jpeg,gif,png,bmp"});
}

//xbhEditor编辑器图片上传回调函数
//下边这个方法是空的， 这样只有等你上传完图片后，选择合适的属性，点击确定的时候才真正显示图片。
function insertUpload(msg) {
//	exec('Img') 
}
//xbhEditor编辑器图片上传回调函数
//调用下边这个方法，是上传完图片自动显示,但是设置的属性就不管用了。
function insertUpload2(msg) {
	var _msg = msg.toString();
	var _picture_name = _msg.substring(_msg.lastIndexOf("/")+1);
	var _picture_path = Substring(_msg);
	_picture_path = "<img escape='false' src='/upload"+_picture_path+"'/>";
	$("#xh_editor").xheditor().pasteHTML("<strong>"+_picture_path+"</strong>")
}
//处理服务器返回到回调函数的字符串内容,格式是JSON的数据格式.
function Substring(s){
	return s.substring(s.substring(0,s.lastIndexOf("/")).lastIndexOf("/"),s.length);
}