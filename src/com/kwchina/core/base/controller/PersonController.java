package com.kwchina.core.base.controller;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.fileupload.DiskFileUpload;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import sun.misc.BASE64Decoder;

import com.kwchina.core.base.entity.OrganizeInfor;
import com.kwchina.core.base.entity.PersonInfor;
import com.kwchina.core.base.entity.StructureInfor;
import com.kwchina.core.base.entity.SystemUserInfor;
import com.kwchina.core.base.service.DataRightInforManager;
import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.base.service.PersonInforManager;
import com.kwchina.core.base.service.StructureManager;
import com.kwchina.core.base.service.ViewLogicRightManager;
import com.kwchina.core.base.vo.PersonInforVo;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.oa.util.SysCommonMethod;

@Controller
@RequestMapping(value="/core/personInfor.do")
public class PersonController extends BasicController {

	/*@Resource
	private Validator validator;*/
	
	@Autowired
	private PersonInforManager personManager;

	@Autowired
	private OrganizeManager organizeManager;

	@Autowired
	private StructureManager structureManager;
	
	@Autowired
	private ViewLogicRightManager viewLogicRightManager;
	
	@Autowired
	private DataRightInforManager dataRightInforManager;
	

	//显示所有人员
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		//构造查询语句
		//String[] queryString = this.personManager.generateQueryString("PersonInfor", "personId", getSearchParams(request));
		
		String[] queryString = new String[2];
		queryString[0] = "from PersonInfor person where 1=1";
		queryString[1] = "select count(personId) from PersonInfor person where 1=1";
		
		String isvalid = request.getParameter("isvalid");
		if (isvalid != null && isvalid.length() > 0 && ("false").equals(isvalid)) {
			queryString[0] += " and deleted = 1";
			queryString[1] += " and deleted = 1";
		}else {
			queryString[0] += " and deleted = 0";
			queryString[1] += " and deleted = 0";
		}
		
		queryString = this.personManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.personManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        JSONObject jsonObj = new JSONObject();
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("department");
		awareObject.add("structure");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	//获取查询条件数据(用户信息)
	@RequestMapping(params="method=getPersons")
	public void getPersons(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		JSONConvert convert = new JSONConvert();
		
		//人员信息
		String departmentIdStr = request.getParameter("departmentId");
		String groupIdStr = request.getParameter("groupId");
		Integer departmentId = (departmentIdStr != null && departmentIdStr.length() > 0)?Integer.valueOf(departmentIdStr):null;
		Integer groupId = (groupIdStr != null && groupIdStr.length() > 0)?Integer.valueOf(groupIdStr):null;
		JSONArray userArray = new JSONArray();
		List persons = this.personManager.getPersonByOrganize(departmentId, groupId);
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
//		awareObject.add("person");
		userArray = convert.modelCollect2JSONArray(persons, awareObject);
		jsonObj.put("_Persons", userArray);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	
	//编辑人员
	@RequestMapping(params="method=edit")
	public String edit(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		Integer personId = vo.getPersonId();

		PersonInfor person = new PersonInfor();
		
		if (personId != null && personId.intValue() > 0) {
			//判断是否有编辑权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "edit");
			//编辑
			person = (PersonInfor)this.personManager.get(personId);	
			
			//属性从model到vo
			BeanUtils.copyProperties(vo, person);			
			
			//所属部门
			if(person.getDepartment()!=null){
				vo.setDepartmentId(person.getDepartment().getOrganizeId());
			}
			//所属班组
			if(person.getGroup()!=null){
				vo.setGroupId(person.getGroup().getOrganizeId());
			}
			//所属岗位
			if(person.getStructure()!=null){
				vo.setStructureId(person.getStructure().getStructureId());
			}
			//出生日期、邮件密码
			request.setAttribute("_Birthday", person.getBirthday());
			request.setAttribute("_EmailPassword", person.getEmailPassword());
			
			// 对附件信息进行处理
			String attachmentFile = person.getPhotoAttachment();
			if (attachmentFile != null && !attachmentFile.equals("")) {
				String[][] attachment = processFile(attachmentFile);
				request.setAttribute("_Attachment_Names", attachment[1]);
				request.setAttribute("_Attachments", attachment[0]);
			}
		}else {
			vo.setPositionLayer(1);

		}
		
		//所有部门，班组以及按照树状结构组织的岗位信息
		List departments = this.organizeManager.getDepartments();
		List groups = this.organizeManager.getGroups();
		List treeStructures = this.structureManager.getStructureAsTree(CoreConstant.Structure_Begin_Id);
		
		request.setAttribute("_Departments", departments);
		request.setAttribute("_Groups", groups);
		request.setAttribute("_Structures", treeStructures);
		
		
		return "base/editPerson";
	}
	
	//保存人员
	@RequestMapping(params="method=save")
	public String save(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		try {
			Integer personId = vo.getPersonId();
			PersonInfor person = new PersonInfor();
			
			String oldFiles = "";
			
			if (personId != null && personId.intValue() != 0) {
				//修改
				person = (PersonInfor)this.personManager.get(personId);		
				
				// 修改信息时,对附件进行修改
				String filePaths = person.getPhotoAttachment();
				oldFiles = deleteOldFile(request, filePaths, "attatchmentArray");
			}
			
			// 上传附件
			String attachment = this.uploadAttachment(multipartRequest, "person");
			
			BeanUtils.copyProperties(person, vo);	
			
			//出生日期
			String birthday = request.getParameter("birthday");
			if(birthday != null && birthday.length() > 0){
				Date date = Date.valueOf(birthday);
				person.setBirthday(date);
			}
			
			//部门、班组、岗位信息
			Integer departmentId = vo.getDepartmentId();
			Integer groupId = vo.getGroupId();
			Integer structureId = vo.getStructureId();
			if(departmentId != null && departmentId.intValue() != 0){
				OrganizeInfor department = (OrganizeInfor)this.organizeManager.get(departmentId);
				person.setDepartment(department);
			}
			
			if(groupId != null && groupId.intValue() != 0){
				OrganizeInfor group = (OrganizeInfor)this.organizeManager.get(groupId);
				person.setGroup(group);
			}
			
			if(structureId != null && structureId.intValue() != 0){
				StructureInfor structure = (StructureInfor)this.structureManager.get(structureId);
				person.setStructure(structure);
			}
			
			// 对附件信息的判断
			if (attachment == null || attachment.equals("")) {
				attachment = oldFiles;
			} else {
				if (oldFiles == null || oldFiles.equals("")) {
					// attachment = attachment;
				} else {
//					attachment = attachment + "|" + oldFiles;
					attachment = attachment;
				}
			}

			// 保存附件
			person.setPhotoAttachment(attachment);
			
			this.personManager.save(person);

		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "success";
		
	}

	
	
	
	/**
	 * app端上传头像(ios)
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=uploadPic")
	public void uploadPic(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo) throws Exception {

		JSONObject jsonObj = new JSONObject();
		try {
			Integer personId = vo.getPersonId();
			PersonInfor person = new PersonInfor();
			
			if (personId != null && personId.intValue() != 0) {
				//修改
				person = (PersonInfor)this.personManager.get(personId);		
				
				
				// 定义变量存储图片地址
			    String imagePath = "";

				//接收图片数据（base64）
				String image = request.getParameter("image");
				//System.out.println("image=="+image);
				String imageName = request.getParameter("imageName");
				
		        // 将base64 转 字节数组
//			        Base64 base = new Base64();
//			        byte[] decode = base.decode(image.getBytes());
		        
		        BASE64Decoder decoder = new BASE64Decoder();


		        // 图片输出路径
		        //imagePath = commodityFilePath + "/" + System.currentTimeMillis() + ".png";
		        String savePath = CoreConstant.Attachment_Path + "person";
				
				//类似于 D:\tomcat55\webapps\ROOT/upload\message;
				File file = new File(CoreConstant.Context_Real_Path + savePath);
				if(!file.exists()){
					file.mkdir();
				}
				
				//在folder下面建立目录，以当前时间为目录
				long current = System.currentTimeMillis();
				savePath += "/" + current;
				file = new File(CoreConstant.Context_Real_Path + savePath);
				if(!file.exists()){
					file.mkdir();
				}
				
				//获取文件名，并保存到该目录下
				//String fileName = current+".png";
				String fileName_bak = current+".png";
				String fileName = imageName == null || imageName.equals("") ? fileName_bak : imageName;
				savePath +=  "/";
				String filePath = CoreConstant.Context_Real_Path + savePath;
				file = new File(filePath);
				if (!file.exists()) {
					file.mkdirs();
				}
				filePath += fileName;
				savePath += fileName;
				
				//保存到数据库中的信息(仅记录Contextpath之后的路径)
				imagePath = savePath;	

		       /* // 定义图片输入流
		        InputStream fin = new ByteArrayInputStream(decode);
		        // 定义图片输出流
		        FileOutputStream fout=new FileOutputStream(filePath);
		       
		        // 写文件
		        byte[] b=new byte[1024];
		        int length=0;
		        while((length=fin.read(b))>0){
		            fout.write(b, 0, length);
		        } 
		        
		        // 关闭数据流
		        fin.close();
		        fout.close();*/
				
				//Base64解码
	            byte[] b = decoder.decodeBuffer(image);
	            for(int i=0;i< b.length;++i){
	                if(b[i]< 0){//调整异常数据
	                	b[i]+=256;
	                }
	            }
	            //生成图片
	            OutputStream out = new FileOutputStream(filePath);    
	            out.write(b);
	            out.flush();
	            out.close();
	            
		        person.setPhotoAttachment(savePath);
				this.personManager.save(person);
				
				//将路径返回
				jsonObj.put("_Image", savePath);
				
				//是否成功标志
				jsonObj.put("_RtnTag", 1);
				
			}else {
				jsonObj.put("_RtnTag", 0);
			}
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			jsonObj.put("_RtnTag", 0);
			e.printStackTrace();
		}
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);		
	}
	
	/**
	 * app端上传头像(android)
	 * @param request
	 * @param response
	 * @param vo
	 * @throws Exception
	 */
	@RequestMapping(params="method=androidUploadPic")
	public void androidUploadPic(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {
		
		JSONObject jsonObj = new JSONObject();
		try {
			Integer personId = vo.getPersonId();
			PersonInfor person = new PersonInfor();
			
			if (personId != null && personId.intValue() != 0) {
				//修改
				person = (PersonInfor)this.personManager.get(personId);		
				
				try {
					String attachment = this.uploadAttachment(multipartRequest, "person");
					person.setPhotoAttachment(attachment);
					this.personManager.save(person);
					jsonObj.put("_RtnTag", 1);
				} catch (Exception e) {
					jsonObj.put("_RtnTag", 0);
					e.printStackTrace();
				}
				
				/*************************/
				
			}else {
				jsonObj.put("_RtnTag", 0);
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			jsonObj.put("_RtnTag", 0);
			e.printStackTrace();
		}
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);	
	}   
        
        /*// 定义变量存储图片地址
	    String imagePath = "";
	    
	    //临时目录
	    String temp=CoreConstant.Attachment_Path+"temp";  

		//接收图片数据（base64）
		String image = request.getParameter("image");
		//System.out.println("image=="+image);
		String imageName = request.getParameter("imageName");
		// 图片输出路径
        //imagePath = commodityFilePath + "/" + System.currentTimeMillis() + ".png";
        String savePath = CoreConstant.Attachment_Path + "person";
		
		//类似于 D:\tomcat55\webapps\ROOT/upload\message;
		File file = new File(CoreConstant.Context_Real_Path + savePath);
		if(!file.exists()){
			file.mkdir();
		}
		
		//在folder下面建立目录，以当前时间为目录
		long current = System.currentTimeMillis();
		savePath += "/" + current;
		file = new File(CoreConstant.Context_Real_Path + savePath);
		if(!file.exists()){
			file.mkdir();
		}
		
		//获取文件名，并保存到该目录下
		//String fileName = current+".png";
		String fileName_bak = current+".png";
		String fileName = imageName == null || imageName.equals("") ? fileName_bak : imageName;
		savePath +=  "/";
		String filePath = CoreConstant.Context_Real_Path + savePath;
		file = new File(filePath);
		if (!file.exists()) {
			file.mkdirs();
		}
//						filePath += fileName;
//						savePath += fileName;
		
		//保存到数据库中的信息(仅记录Contextpath之后的路径)
		imagePath = savePath;*/



/**************************/
//						request.setCharacterEncoding("utf-8");  
//						String ct = request.getHeader("Content-Type");
//						System.out.println("Content-Type="+ct);
//				        //获得磁盘文件条目工厂。  
//				        DiskFileItemFactory factory = new DiskFileItemFactory();  
//				        //获取文件上传需要保存的路径，upload文件夹需存在。  
////				        String path = request.getSession().getServletContext().getRealPath("/upload");  
//				        //设置暂时存放文件的存储室，这个存储室可以和最终存储文件的文件夹不同。因为当文件很大的话会占用过多内存所以设置存储室。  
//				        factory.setRepository(new File(savePath));  
//				        //设置缓存的大小，当上传文件的容量超过缓存时，就放到暂时存储室。  
//				        factory.setSizeThreshold(1024*1024);  
//				        //上传处理工具类（高水平API上传处理？）  
//				        ServletFileUpload upload = new ServletFileUpload(factory);  
//				          
//				        try{  
//				            //调用 parseRequest（request）方法  获得上传文件 FileItem 的集合list 可实现多文件上传。  
//				            List<FileItem> list = (List<FileItem>)upload.parseRequest(request);  
//				            for(FileItem item:list){  
//				                //获取表单属性名字。  
//				                String name = item.getFieldName();  
//				                //如果获取的表单信息是普通的文本信息。即通过页面表单形式传递来的字符串。  
//				                if(item.isFormField()){  
//				                    //获取用户具体输入的字符串，  
//				                    String value = item.getString();  
//				                    request.setAttribute(name, value);  
//				                }  
//				                //如果传入的是非简单字符串，而是图片，音频，视频等二进制文件。  
//				                else{   
//				                    //获取路径名  
//				                    String value = item.getName();  
//				                    //取到最后一个反斜杠。  
//				                    int start = value.lastIndexOf("\\");  
//				                    //截取上传文件的 字符串名字。+1是去掉反斜杠。  
//				                    String filename = value.substring(start+1);  
//				                    request.setAttribute(name, filename);  
//				                      
//				                    /*第三方提供的方法直接写到文件中。 
//				                     * item.write(new File(path,filename));*/  
//				                    //收到写到接收的文件中。  
//				                    OutputStream out = new FileOutputStream(new File(savePath,filename));  
//				                    InputStream in = item.getInputStream();  
//				                      
//				                    int length = 0;  
//				                    byte[] buf = new byte[1024];  
//				                    System.out.println("获取文件总量的容量:"+ item.getSize());  
//				                      
//				                    while((length = in.read(buf))!=-1){  
//				                        out.write(buf,0,length);  
//				                    }  
//				                    in.close();  
//				                    out.close();  
//				                    
//				                    person.setPhotoAttachment(savePath+"/"+filename);
//									this.personManager.save(person);
//									jsonObj.put("_RtnTag", 1);
//				                }  
//				            }  
//				        }catch(Exception e){  
//				            e.printStackTrace();  
//				        }  





/*FileOutputStream fos = null;
FileInputStream fis = null;
try {
// 保存文件那一个路径
fos = new FileOutputStream(savePath + "\\"
	+ fileName);
fis = new FileInputStream(request.getParameter("image"));
byte[] buffer = new byte[1024];
int len = 0;
while ((len = fis.read(buffer)) > 0) {
fos.write(buffer, 0, len);
}

person.setPhotoAttachment(savePath+"/"+fileName);
this.personManager.save(person);
jsonObj.put("_RtnTag", 1);
} catch (Exception e) {
jsonObj.put("_RtnTag", 0);
e.printStackTrace();
} finally {
fos.close();
fis.close();
}*/
        
		
		//						DiskFileUpload fu =new DiskFileUpload();           
		//						fu.setSizeMax(1*1024*1024);   // 设置允许用户上传文件大小,单位:字节           
		//						fu.setSizeThreshold(4096);   // 设置最多只允许在内存中存储的数据,单位:字节           
		//						fu.setRepositoryPath(temp); // 设置一旦文件大小超过getSizeThreshold()的值时数据存放在硬盘的目录                     
		//						//开始读取上传信息
		//						int index=0;           
		//						List fileItems =null;                                                                        
		//						try {                                        
		//							fileItems = fu.parseRequest(request);                                         
		//							System.out.println("fileItems="+fileItems);                                
		//						} catch (Exception e) {    
		//							jsonObj.put("_RtnTag", 0);
		//							e.printStackTrace();                                
		//						}                                                   
		////						Iterator iter = fileItems.iterator(); // 依次处理每个上传的文件
		////						while (iter.hasNext()){              
		//							//FileItem item = (FileItem)iter.next();// 忽略其他不是文件域的所有表单信息
		//						
		//						if(fileItems != null && fileItems.size() > 0){
		//							FileItem item = (FileItem)fileItems.get(0);
		//							if (!item.isFormField()){                   
		//								String name = item.getName();//获取上传文件名,包括路径                   
		//								name=name.substring(name.lastIndexOf("\\")+1);//从全路径中提取文件名
		//								long size = item.getSize();                   
		////									if((name==null||name.equals("")) && size==0)
		////										continue;                   
		//								int point = name.indexOf(".");                   
		//								name=new java.util.Date().getTime()+name.substring(point,name.length())+index;                   
		//								index++;                   
		//								File fNew=new File(savePath, name);                   
		//								try {                                        
		//									item.write(fNew);                                
		//								} catch (Exception e) { 
		//									jsonObj.put("_RtnTag", 0);
		//									e.printStackTrace();                                
		//								}          
		//
		//								person.setPhotoAttachment(savePath+"/"+name);
		//								this.personManager.save(person);
		//								jsonObj.put("_RtnTag", 1);
		//							}else{
		//								//取出不是文件域的所有表单信息    
		//								String fieldvalue = item.getString();            
		//								//如果包含中文应写为：(转为UTF-8编码)                   
		//								//String fieldvalue = new String(item.getString().getBytes(),"UTF-8");               
		//							}
		//						}
				
					
		//						}
	
	
	
	//删除人员信息
	@RequestMapping(params="method=delete")
	public void delete(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String rowIds = request.getParameter("rowIds");
		if (rowIds != null && rowIds.length() > 0) {
			
			String[] detleteIds = rowIds.split(",");
			if (detleteIds.length > 0) {
				for (int i=0;i<detleteIds.length;i++) {
					Integer personId = Integer.valueOf(detleteIds[i]);
					PersonInfor person = (PersonInfor)this.personManager.get(personId);
					//判断是否有删除权限
					this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "delete");
					this.personManager.remove(person);
				}
			}
		}
		
	}
	
	//注销或恢复人员
	@RequestMapping(params="method=cancelOrResume")
	public void cancelOrResume(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		Integer personId = vo.getPersonId();
		if (personId != null && personId.intValue() != 0) {
			//判断是否有注销或恢复权限
			this.dataRightInforManager.haveDataRight(request, response, "personId", personId, "cancelOrResume");
			
			PersonInfor person = (PersonInfor)this.personManager.get(personId);
			if (person != null) {
				if (person.isDeleted()) {
					//若已注销,则执行恢复操作
					person.setDeleted(false);
				}else {
					//若正常,则执行注销操作
					person.setDeleted(true);
				}
			}
			this.personManager.save(person);
		}

	}
	
	//编辑个人信息
	@RequestMapping(params="method=editPersonalInfor")
	public String editPersonInfor(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model) throws Exception {

		SystemUserInfor systemUser = SysCommonMethod.getSystemUser(request);
		int personId = systemUser.getPersonId().intValue();
		PersonInfor person = (PersonInfor)this.personManager.get(personId);
		
		// 对附件信息进行处理
		String attachmentFile = person.getPhotoAttachment();
		if (attachmentFile != null && !attachmentFile.equals("")) {
			String[][] attachment = processFile(attachmentFile);
			request.setAttribute("_Attachment_Names", attachment[1]);
			request.setAttribute("_Attachments", attachment[0]);
		}
		
		BeanUtils.copyProperties(vo, person);
		
		model.addAttribute("_PersonInfor", person);
		
		return "base/editPersonalInfor";
	}
	
	//保存个人信息
	@RequestMapping(params="method=savePersonalInfor")
	public String savePersonalInfor(HttpServletRequest request, HttpServletResponse response, PersonInforVo vo, Model model, DefaultMultipartHttpServletRequest multipartRequest) throws Exception {

		save(request, response, vo,multipartRequest);
		
		return editPersonInfor(request, response, vo, model);
	}
	
	
	//验证姓名重复
	@RequestMapping(params="method=valPersonName")
	@ResponseBody
	public Map<String, Object> valPersonName(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String personName = request.getParameter("personName");
		/*try {
			personName = new String(personName.getBytes("ISO-8859-1"), CoreConstant.ENCODING);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}*/
		String personId = request.getParameter("personId");
		
		/** 判断姓名是否重复:
		 * A.如果数据库中存在该姓名,则需判断该姓名是否为本身,如果是则验证通过,否则视为姓名重复;
		 * B.如果数据库中不存在该姓名,则验证通过.
		 *  */
		PersonInfor person = this.personManager.findPersonByName(personName);
		if (person != null) {
			if (personId != null && personId.length() > 0) {
				Integer personIdInt = Integer.valueOf(personId);
				if (personIdInt == person.getPersonId().intValue()) {
					map.put("isDuplicate", false);
				}else {
					map.put("isDuplicate", true);
				}
			}else {
				map.put("isDuplicate", true);
			}
			
		}else {
			map.put("isDuplicate", false);
		}
		
		return map;
	}
	
	
	//验证邮件重复
	@RequestMapping(params="method=valPersonEmail")
	@ResponseBody
	public Map<String, Object> valPersonEmail(HttpServletRequest request, HttpServletResponse response) {
		Map<String, Object> map = new HashMap<String, Object>();
		
		String email = request.getParameter("email");
		String personId = request.getParameter("personId");
		
		/** 判断邮件是否重复:
		 * A.如果数据库中存在该邮件,则需判断该邮件是否为本身,如果是则验证通过,否则视为重复;
		 * B.如果数据库中不存在该邮件,则验证通过.
		 *  */
		PersonInfor person = this.personManager.findPersonByEmail(email);
		if (person != null) {
			if (personId != null && personId.length() > 0) {
				Integer personIdInt = Integer.valueOf(personId);
				if (personIdInt == person.getPersonId().intValue()) {
					map.put("isDuplicate", false);
				}else {
					map.put("isDuplicate", true);
				}
			}else {
				map.put("isDuplicate", true);
			}
			
		}else {
			map.put("isDuplicate", false);
		}
		
		return map;
	}
	

}
