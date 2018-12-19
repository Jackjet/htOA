package com.kwchina.extend.loginLog.controller;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.util.CellRangeAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.kwchina.core.base.service.SystemUserManager;
import com.kwchina.core.common.controller.BasicController;
import com.kwchina.core.common.page.PageList;
import com.kwchina.core.common.page.Pages;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.core.util.ExcelOperate;
import com.kwchina.core.util.json.JSONConvert;
import com.kwchina.core.util.string.StringUtil;
import com.kwchina.extend.loginLog.service.AppModuleLogManager;
import com.kwchina.extend.loginLog.vo.AppModuleLogFormed;
import com.kwchina.extend.loginLog.vo.AppModuleLogVo;

@Controller
@RequestMapping(value="/extend/appModuleLog.do")
public class AppModuleLogController extends BasicController {

	
	@Autowired
	private AppModuleLogManager appModuleLogManager;

	@Autowired
	private SystemUserManager systemUserManager;
	
	
	public JSONObject getInstances(HttpServletRequest request, HttpServletResponse response, boolean isExcel){
		JSONObject jsonObj = new JSONObject();
		
		//构造查询语句
		//String[] queryString = this.personManager.generateQueryString("PersonInfor", "personId", getSearchParams(request));
		
		String[] queryString = new String[2];
		queryString[0] = "from AppModuleLog log where 1=1";
		queryString[1] = "select count(logId) from AppModuleLog log where 1=1";
		
		
		queryString = this.appModuleLogManager.generateQueryString(queryString, getSearchParams(request));
		
		String page = request.getParameter("page");		//当前页
		String rowsNum = request.getParameter("rows"); 	//每页显示的行数
		Pages pages = new Pages(request);
		pages.setPage(Integer.valueOf(page));
		pages.setPerPageNum(Integer.valueOf(rowsNum));
		
		PageList pl = this.appModuleLogManager.getResultByQueryString(queryString[0], queryString[1], true, pages);
		List list = pl.getObjectList();
		
		//定义返回的数据类型：json，使用了json-lib
        
                  
        //定义rows，存放数据
        JSONArray rows = new JSONArray();
        jsonObj.put("page", pl.getPages().getCurrPage());   //当前页(名称必须为page)
        jsonObj.put("total", pl.getPages().getTotalPage()); //总页数(名称必须为total)
        jsonObj.put("records", pl.getPages().getTotals());	//总记录数(名称必须为records)        
        
		JSONConvert convert = new JSONConvert();
		//通知Convert，哪些关联对象需要获取
		List awareObject = new ArrayList();
		awareObject.add("author");
		rows = convert.modelCollect2JSONArray(list, awareObject);
		jsonObj.put("rows", rows);							//返回到前台每页显示的数据(名称必须为rows)
		
		return jsonObj;
	}
	

	//显示所有
	@RequestMapping(params="method=list")
	public void list(HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		JSONObject jsonObj = getInstances(request, response, false);
		
		//设置字符编码
        response.setContentType(CoreConstant.CONTENT_TYPE);
        response.getWriter().print(jsonObj);
	}
	
	
	/**
	 * 导出excel
	 * 
	 * @param inforPath
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping(params = "method=expertExcel")
	public String expertExcel(HttpServletRequest request, HttpServletResponse response) throws Exception {
		try {
			String rootPath = request.getSession().getServletContext().getRealPath("/");
			JSONObject jsonObj = getInstances(request, response, false);
			
			JSONArray listArray = jsonObj.getJSONArray("rows");
			
			List<AppModuleLogFormed> infors = (List)JSONArray.toCollection(listArray, AppModuleLogFormed.class);
			
			//转为以模块名为索引，计数总次数
			List<AppModuleLogVo> vos = new ArrayList<AppModuleLogVo>();
			
			//模块名
			Set<String> moduleNameSet = new HashSet<String>();
			for(AppModuleLogFormed tmpLog : infors){
				moduleNameSet.add(tmpLog.getModuleName());
			}
			
			
			//设定excel表头
			HSSFWorkbook wb = new HSSFWorkbook();
			HSSFSheet sheet = wb.createSheet("Sheet1");
			
			HSSFCellStyle style = wb.createCellStyle(); // 样式对象      
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直      
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平
			
			HSSFRow headRow = sheet.createRow((short) 0);      
			
			//设定excel表头
			HSSFCell cell1 = headRow.createCell((short)0);
//			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue("模块");
			cell1.setCellStyle(style);
			
			HSSFCell cell2 = headRow.createCell((short)1);
//			cell2.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell2.setCellValue("平台");
			cell2.setCellStyle(style);
			
			HSSFCell cell3 = headRow.createCell((short)2);
//			cell3.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell3.setCellValue("用户名");
			cell3.setCellStyle(style);

			HSSFCell cell4 = headRow.createCell((short)3);
//			cell4.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell4.setCellValue("使用次数");
			cell4.setCellStyle(style);

//			HSSFCell cell5 = headRow.createCell((short)4);
//			cell5.setEncoding(HSSFCell.ENCODING_UTF_16);
//			cell5.setCellValue("");
//			cell5.setCellStyle(style);
			
			//总序号索引
			int allUserIndex = 0;
			//模块下用户数（用以合并单元格使用）
			int currentModuleUserCount = 0;
			for(String tmpModuleName : moduleNameSet){
//				AppModuleLogVo vo = new AppModuleLogVo();
//				vo.setModuleName(tmpModuleName);
				
				//某一模块下、各平台的用户集合
				Set<String> moduleAndroidUserSet = new HashSet<String>();
				Set<String> moduleIosUserSet = new HashSet<String>();
				for(AppModuleLogFormed tmpLog : infors){
					//System.out.println(tmpLog.getLogId());
					if(tmpLog.getModuleName().equals(tmpModuleName) && StringUtil.isNotEmpty(tmpLog.getPlatform()) && tmpLog.getPlatform().equals("android")){
						moduleAndroidUserSet.add(tmpLog.getUserName());
					}
					if(tmpLog.getModuleName().equals(tmpModuleName) && StringUtil.isNotEmpty(tmpLog.getPlatform()) && tmpLog.getPlatform().equals("ios")){
						moduleIosUserSet.add(tmpLog.getUserName());
					}
				}

				int androidUserCount = moduleAndroidUserSet.size();
				int iosUserCount = moduleIosUserSet.size();
				
				//模块单元格
//				sheet.addMergedRegion(new Region(currentModuleUserCount + 1, (short) 0, currentModuleUserCount + androidUserCount + iosUserCount, (short) (0)));    
				
				if((androidUserCount + iosUserCount) > 1){
					sheet.addMergedRegion(new CellRangeAddress(currentModuleUserCount + 1, currentModuleUserCount + androidUserCount + iosUserCount, 0, 0));
//					sheet.addMergedRegion(new CellRangeAddress(currentModuleUserCount + 1, currentModuleUserCount + androidUserCount + iosUserCount, 4, 4));
				}

				HSSFRow moduleRow1 = sheet.createRow((short) (currentModuleUserCount + 1)); 
				HSSFCell moduleCell = moduleRow1.createCell(0);      
//				moduleCell.setEncoding(HSSFCell.ENCODING_UTF_16);   
//				System.out.println(tmpModuleName);
				moduleCell.setCellValue(new HSSFRichTextString(tmpModuleName));  
				moduleCell.setCellStyle(style);
//				moduleCell.setCellType(HSSFCell.CELL_TYPE_ERROR);
				
//				System.out.println(moduleCell.getStringCellValue());
				HSSFCell cell = moduleRow1.getCell((short)0); 
				HSSFRichTextString ts = new HSSFRichTextString(tmpModuleName); 
				cell.setCellValue(ts);
				//System.out.println(cell.getStringCellValue().toString());
				
				
				
				/********区分平台 android ios*********/
				//android
//				List<AppModuleLogVo> androidLogVos = new ArrayList<AppModuleLogVo>();
				int androidIndex = 0;
				
				
				
				if(moduleAndroidUserSet != null && moduleAndroidUserSet.size() > 0){
//					sheet.addMergedRegion(new Region(currentModuleUserCount + 1, (short) 1, currentModuleUserCount + androidUserCount, (short) (1))); 
					
					if(androidUserCount > 1){
						sheet.addMergedRegion(new CellRangeAddress(currentModuleUserCount + 1, currentModuleUserCount + androidUserCount,  1, 1));
					}
					
					for(String tmpUserName : moduleAndroidUserSet){
						
						//生成excel单元格
						//平台
						
						/*******这一行非常重要,如果不加判断的话,会重新创建row,即会覆盖前面创建过一次的同一行,导致前面列的值为空********/
						HSSFRow androidRow = moduleRow1;
						if(androidIndex > 0){
							androidRow = sheet.createRow((short) (currentModuleUserCount + 1 + androidIndex));
						}
						
						HSSFCell androidCell = androidRow.createCell((short) (1));      
//						androidCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						androidCell.setCellValue("android");  
						androidCell.setCellStyle(style);
						
						//用户名
						HSSFCell userNameCell = androidRow.createCell((short) (2));      
//						userNameCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						userNameCell.setCellValue(tmpUserName);  
						userNameCell.setCellStyle(style);

						int androidModuleUserLogCount = 0;
						for(AppModuleLogFormed tmpLog : infors){
							if(tmpLog.getModuleName().equals(tmpModuleName) && tmpLog.getUserName().equals(tmpUserName) && StringUtil.isNotEmpty(tmpLog.getPlatform()) && tmpLog.getPlatform().toLowerCase().equals("android")){
								androidModuleUserLogCount ++;
							}
						}
						
						//次数
						HSSFCell countCell = androidRow.createCell((short) (3));      
//						countCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						countCell.setCellValue(androidModuleUserLogCount);  
						countCell.setCellStyle(style);
						
						androidIndex ++;
					}
				}
				
				//ios
//				List<AppModuleLogVo> iosLogVos = new ArrayList<AppModuleLogVo>();
				int iosIndex = 0;
				
				
				if(moduleIosUserSet != null && moduleIosUserSet.size() > 0){
//					sheet.addMergedRegion(new Region(currentModuleUserCount + androidUserCount + 1, (short) 1, currentModuleUserCount + androidUserCount + iosUserCount, (short) (1)));
					
					if(iosUserCount > 1){
						sheet.addMergedRegion(new CellRangeAddress(currentModuleUserCount + androidUserCount + 1, currentModuleUserCount + androidUserCount + iosUserCount, (short) 1, (short) (1)));
					}
					
					for(String tmpUserName : moduleIosUserSet){
						//生成excel单元格
						//平台
						HSSFRow iosRow = null;
						if(iosIndex == 0 && androidUserCount == 0){
							iosRow = moduleRow1;
						}else {
							iosRow = sheet.createRow((short) (currentModuleUserCount + androidUserCount + 1 + iosIndex));
						}
						
						HSSFCell iosCell = iosRow.createCell((short) (1));      
//						iosCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						iosCell.setCellValue("ios");  
						iosCell.setCellStyle(style);
						
						//用户名
						HSSFCell userNameCell = iosRow.createCell((short) (2));      
//						userNameCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						userNameCell.setCellValue(tmpUserName);  
						userNameCell.setCellStyle(style);

						int iosModuleUserLogCount = 0;
						for(AppModuleLogFormed tmpLog : infors){
							if(tmpLog.getModuleName().equals(tmpModuleName) && tmpLog.getUserName().equals(tmpUserName) && StringUtil.isNotEmpty(tmpLog.getPlatform()) && tmpLog.getPlatform().toLowerCase().equals("ios")){
								iosModuleUserLogCount ++;
							}
						}
						
						//次数
						HSSFCell countCell = iosRow.createCell((short) (3));      
//						countCell.setEncoding(HSSFCell.ENCODING_UTF_16);      
						countCell.setCellValue(iosModuleUserLogCount);  
						countCell.setCellStyle(style);
						
						iosIndex ++;
					}
				}
				
				

				currentModuleUserCount += androidUserCount + iosUserCount;
				allUserIndex += currentModuleUserCount;
			}
			
			long time = System.currentTimeMillis();
			String filePath = "/"+CoreConstant.Attachment_Path + "appModuleLog/";

//			ExcelOperate excelOperate = new ExcelOperate();
//			excelOperate.createFilePath(f); 

//			request.getSession().setAttribute("_File_Path", "");
			request.getSession().removeAttribute("_File_Path_module");
			request.getSession().setAttribute("_File_Path_module", filePath +  "app模块使用记录.xls");
			
			filePath = rootPath + filePath;
			
			java.io.File f = new java.io.File(filePath);
			ExcelOperate excelOperate = new ExcelOperate();
			excelOperate.createFilePath(f);
			

			filePath = filePath +  "app模块使用记录.xls";
			
			File ioFile= new File(filePath);
			
			ioFile.createNewFile();
			FileOutputStream fs = new FileOutputStream(ioFile);
			wb.write(fs);
			fs.close();
			
//			//按照orderNo排序
//	 		//Collections.sort(vos, new BeanComparator("logCount"));
//			Comparator mycmp = ComparableComparator.getInstance();       
//	        mycmp = ComparatorUtils.nullLowComparator(mycmp);  //允许null       
//	        mycmp = ComparatorUtils.reversedComparator(mycmp); //逆序       
//	        Comparator cmp = new BeanComparator("logCount", mycmp);    
//	        Collections.sort(vos, cmp);
			

			
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return "/common/download_module";
	}
	
	
	

}
