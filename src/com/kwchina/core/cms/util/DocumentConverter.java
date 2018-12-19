package com.kwchina.core.cms.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Calendar;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;

import com.kwchina.core.base.service.OrganizeManager;
import com.kwchina.core.cms.entity.InforCategory;
import com.kwchina.core.cms.entity.InforDocument;
import com.kwchina.core.cms.entity.InforField;
import com.kwchina.core.sys.CoreConstant;

import freemarker.template.Configuration;
import freemarker.template.DefaultObjectWrapper;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class DocumentConverter {
	



	//根据用户设定,自动生成添加页面里的字段信息
	public static String createAddFields(InforCategory category, InforDocument inforDocument) {
		
		//对附件信息进行处理
		String attachmentFile = inforDocument.getAttachment();
		String[] arrayFiles = new String[0];
		String[] attachmentNames = new String[0];
		if (attachmentFile != null && (!attachmentFile.equals(""))) {
			arrayFiles = attachmentFile.split("\\|");

			attachmentNames = new String[arrayFiles.length];
			for (int k = 0; k < arrayFiles.length; k++) {
				attachmentNames[k] = com.kwchina.core.util.File.getFileName(arrayFiles[k]);
			}
		}
		//对图片附件进行处理
		String defaultPicUrl = inforDocument.getDefaultPicUrl();
		String[] arrayPics = new String[0];
		String[] pictureNames = new String[0];
		if (defaultPicUrl != null && (!defaultPicUrl.equals(""))) {
			arrayPics = defaultPicUrl.split("\\|");

			pictureNames = new String[arrayPics.length];
			for (int k = 0; k < arrayPics.length; k++) {
				pictureNames[k] = com.kwchina.core.util.File.getFileName(arrayPics[k]);
			}
		}
		
		StringBuffer addFields = new StringBuffer();
		


		
		Set fields = category.getFields();
		for (Iterator it=fields.iterator();it.hasNext();) {
			InforField field = (InforField)it.next();
			if (field.isDisplayed()) {
				/** 相关字段 */
				if (field.getFieldName().equals("inforTitle")) {
					//信息主题
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"inforTitle\" size=\"100\" value=\""+(inforDocument.getInforTitle()==null?"":inforDocument.getInforTitle())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("issueUnit")) {
					//发布单位
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"issueUnit\" value=\""+(inforDocument.getIssueUnit()==null?"":inforDocument.getIssueUnit())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("important")) {
					//是否重要
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><select name=\"important\">");
					if (inforDocument.isHomepage()) {
						addFields.append("<option value=\"false\">否</option><option value=\"true\" selected=\"true\">是</option>");
					}else {
						addFields.append("<option value=\"false\" selected=\"true\">否</option><option value=\"true\">是</option>");
					}
					addFields.append("</select></td></tr>");
				}else if(field.getFieldName().equals("keyword")) {
					//关键字
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"keyword\" value=\""+(inforDocument.getKeyword()==null?"":inforDocument.getKeyword())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("inforContent")) {
					//信息内容
					//addFields.append("<tr><td valign=\"top\">"+field.getDisplayTitle()+"：</td><td><textarea id=\"inforContent\" name=\"inforContent\" cols=\"90\" rows=\"10\">"+(inforDocument.getInforContent()==null?"":inforDocument.getInforContent())+"</textarea></td></tr>");
					addFields.append("<tr><td valign=\"top\">"+field.getDisplayTitle()+"：</td><td><textarea id=\"inforContent\" name=\"inforContent\" cols=\"90\" rows=\"10\">"+(inforDocument.getInforContent()==null?"":inforDocument.getInforContent())+"</textarea></td></tr>");
				}else if(field.getFieldName().equals("attachment")) {
					//附件路径
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><table cellpadding=\"0\" cellspacing=\"0\" style=\"margin-bottom:0;margin-top:0\"><tr><td><input type=\"file\" name=\"attachment\" size=\"50\"/> <input type=\"button\" value=\"更多附件..\" onclick=\"addtable('attachmentFile')\"/></td></tr></table><span id=\"attachmentFile\"></span></td></tr>");
					if (attachmentNames != null && attachmentNames.length > 0) {
						addFields.append("<td colspan=\"2\">");
						for (int i=0;i<attachmentNames.length;i++) {
							addFields.append("<input type=\"checkbox\" name=\"filebox\" value=\""+i+"\"/><a href="+CoreConstant.Context_Real_Path+arrayFiles[i]+">"+attachmentNames[i]+"</a><br/>");
						}
						addFields.append("</td>");
					}
				}else if(field.getFieldName().equals("inforTime")) {
					//信息时间
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"inforTime\" size=\"12\" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" readonly=\"true\" value=\""+(inforDocument.getInforTime()==null?"":inforDocument.getInforTime())+"\"/></td></tr>");
				}else if (field.getFieldName().equals("relateUrl")) {
					//相关链接
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"relateUrl\" size=\"30\" value=\""+(inforDocument.getRelateUrl()==null?"":inforDocument.getRelateUrl())+"\"/></td></tr>");
				}else if (field.getFieldName().equals("source")) {
					//信息来源
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"source\" size=\"30\" value=\""+(inforDocument.getSource()==null?"":inforDocument.getSource())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defaultPicUrl")) {
					//图片路径
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><table cellpadding=\"0\" cellspacing=\"0\" style=\"margin-bottom:0;margin-top:0\"><tr><td><input type=\"file\" name=\"defaultPicUrl\" size=\"50\"/> <input type=\"button\" value=\"更多图片..\" onclick=\"addtable('defaultPicUrlFile')\"/></td></tr></table><span id=\"defaultPicUrlFile\"></span></td></tr>");
					if (pictureNames != null && pictureNames.length > 0) {
						addFields.append("<td colspan=\"2\">");
						for (int i=0;i<pictureNames.length;i++) {
							addFields.append("<input type=\"checkbox\" name=\"picbox\" value=\""+i+"\"/><a href="+CoreConstant.Context_Real_Path+arrayPics[i]+">"+pictureNames[i]+"</a><br/>");
						}
						addFields.append("</td>");
					}
				}else if(field.getFieldName().equals("defStr1")) {
					//自定义字符1
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defStr1\" value=\""+(inforDocument.getDefStr1()==null?"":inforDocument.getDefStr1())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defStr2")) {
					//自定义字符2
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defStr2\" value=\""+(inforDocument.getDefStr2()==null?"":inforDocument.getDefStr2())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defStr3")) {
					//自定义字符3
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defStr3\" value=\""+(inforDocument.getDefStr3()==null?"":inforDocument.getDefStr3())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defDate1")) {
					//自定义时间1
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defDate1\" size=\"12\" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" readonly=\"true\" value=\""+(inforDocument.getDefDate1()==null?"":inforDocument.getDefDate1())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defDate2")) {
					//自定义时间2
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defDate2\" size=\"12\" onclick=\"WdatePicker({dateFmt:'yyyy-MM-dd'})\" readonly=\"true\" value=\""+(inforDocument.getDefDate2()==null?"":inforDocument.getDefDate2())+"\"/></td></tr>");
				}else if(field.getFieldName().equals("defBool1")) {
					//自定义Boolean型
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><select name=\"defBool1\">");
					if (inforDocument.isDefBool1()) {
						addFields.append("<option value=\"false\">否</option><option value=\"true\" selected=\"true\">是</option>");
					}else {
						addFields.append("<option value=\"false\" selected=\"true\">否</option><option value=\"true\">是</option>");
					}
					addFields.append("</select></td></tr>");
				}else if(field.getFieldName().equals("defDec1")) {
					//自定义Decimal型
					addFields.append("<tr><td>"+field.getDisplayTitle()+"：</td><td><input type=\"text\" name=\"defDec1\" size=\"10\" value=\""+(inforDocument.getDefDec1()==null?"":inforDocument.getDefDec1())+"\"/></td></tr>");
				}
			}
		}
		
		if (addFields != null && addFields.length() > 0) {
			return addFields.toString();
		}
		
		return null;
	}
	
	/**
	 * 根据相关信息,自动生成静态html页面
	 * @param templateName	模板文件名称
	 * @param pagePath		生成的html页面路径
	 * @param map			构造的数据对象
	 */
	public static String createHtml(String templateName, String pagePath, Map<?, ?> map) {
		
		//生成页面的路径
		String returnPath = "";
		
		//模板路径
		String templatePath = CoreConstant.Context_Real_Path + InforConstant.Cms_Template_Path;
		
		try {
			
			//初使化FreeMarker配置
			Configuration config = new Configuration();
			
			//设置freemarker的标签默认使用方括号
			config.setTagSyntax(Configuration.SQUARE_BRACKET_TAG_SYNTAX);
			
			//设置要解析的模板所在的目录,并加载模板文件
			config.setDirectoryForTemplateLoading(new File(templatePath));
			
			//设置包装器,并将对象包装为数据模型
			config.setObjectWrapper(new DefaultObjectWrapper());
			
			//获取模板,并设置编码方式,这个编码必须要与页面中的编码格式一致,否则会出现乱码
			Template template = config.getTemplate(templateName, CoreConstant.ENCODING);
			
			//根据时间定义文件名
			Calendar calendar = Calendar.getInstance();
			String fileName = String.valueOf(calendar.getTimeInMillis()) + ".html";
			
			//保存html文件
			String filePath = CoreConstant.Context_Real_Path + InforConstant.Cms_Html_Path + pagePath;	//生成的html文件保存路径
			com.kwchina.core.util.File fileOperator = new com.kwchina.core.util.File();
			File ioFile = new File(filePath);
			fileOperator.createFilePath(ioFile);
			String htmlPath = filePath + "/" + fileName;
			FileOutputStream fos = new FileOutputStream(htmlPath);
			Writer out = new OutputStreamWriter(fos, CoreConstant.ENCODING);
			
			returnPath = InforConstant.Cms_Html_Path + pagePath + "/" + fileName;
			
			template.process(map, out);
			out.flush();
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (TemplateException e) {
			e.printStackTrace();
		}
		
		return returnPath;
	}
	
}
