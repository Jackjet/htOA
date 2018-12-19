package com.kwchina.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ListIterator;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.Hyperlink;
import org.apache.struts.upload.FormFile;

import com.kwchina.core.util.string.StringUtil;






public class ExcelOperate {
	/**
	 * Logger for this class
	 */
	private static final Logger logger = Logger.getLogger(ExcelOperate.class);

	public void exportExcel(ExcelObject object, int rowNum,
			String fileFullPath, boolean showColumnOne,HttpServletRequest request) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet1");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		int i = 0;

		// 设定表题
		//HSSFRow row1 = sheet.createRow((short) i++);
		//HSSFCell titleCell1 = row1.createCell((short) 1);
		//titleCell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		//titleCell1.setCellValue(object.getTitle());

		// 设定表的内容
		HSSFRow row2 = sheet.createRow((short) i++);
		// 设定表的列名
		for (ListIterator cell = object.getRowName().listIterator(); cell
				.hasNext();) {
			String rowName = (String) cell.next();
			HSSFCell cell1 = row2.createCell((short) cell.previousIndex());
//			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(rowName);
		}

		// 设定表的内容
	
		for (int j = 0; j < rowNum; j++) {
			HSSFRow row3 = sheet.createRow((short) i++);
			for (ListIterator table = object.getTableContent().listIterator(); table
					.hasNext();) {
				String[] tableContent = (String[]) table.next();
				String content = (String) tableContent[j];
				HSSFCell cell1 = row3.createCell((short) table.previousIndex());
//				cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
				if (FormatUtil.isNumber(content)) {
					cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell1.setCellValue(content);
				} else {
					cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
					cell1.setCellValue(content);
				}

				if (logger.isDebugEnabled()) {
					 logger.debug(i + "行：exportExcel(ExcelObject) - content="
					 + content);
				}
			}
		}

		// 隐藏第一列
		if (!showColumnOne)
			sheet.setColumnWidth((short) 0, (short) 0);
		String filePath = fileFullPath;
		//if (filePath == null || filePath.equals("")) {
			filePath = rootPath + object.getFilePath();
					//+ object.getFileName() + ".xls";
		//}
		 java.io.File f = new java.io.File(filePath);

	/*	if (!f.exists()) {
			f.mkdir();
		}*/
		 createFilePath(f); 
		filePath = filePath + object.getFileName() + ".xls";
		File ioFile= new File(filePath);
		
		ioFile.createNewFile();
		FileOutputStream fs = new FileOutputStream(ioFile);
		wb.write(fs);
		fs.close();
	}
	
	/**
	 * 导出含有超链接的excel
	 * @param object
	 * @param rowNum
	 * @param fileFullPath
	 * @param showColumnOne
	 * @param request
	 * @throws IOException
	 */
	public void exportExcelWithLink(ExcelObject object, int rowNum,
			String fileFullPath, boolean showColumnOne,HttpServletRequest request) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet1");
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		int i = 0;

		// 设定表题
		//HSSFRow row1 = sheet.createRow((short) i++);
		//HSSFCell titleCell1 = row1.createCell((short) 1);
		//titleCell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		//titleCell1.setCellValue(object.getTitle());
		
		//超链接样式
		HSSFCellStyle linkStyle = wb.createCellStyle();
		HSSFFont cellFont= wb.createFont();
		cellFont.setUnderline((byte) 1);
		cellFont.setColor(HSSFColor.BLUE.index);
		linkStyle.setFont(cellFont);
//		linkStyle.setWrapText(true);
//		linkStyle.setHidden(true);

		// 设定表的内容
		HSSFRow row2 = sheet.createRow((short) i++);
		// 设定表的列名
		for (ListIterator cell = object.getRowName().listIterator(); cell
				.hasNext();) {
			String rowName = (String) cell.next();
			HSSFCell cell1 = row2.createCell((short) cell.previousIndex());
//			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(rowName);
		}

		// 设定表的内容
	
		for (int j = 0; j < rowNum; j++) {
			HSSFRow row3 = sheet.createRow((short) i++);
			for (ListIterator table = object.getTableContent().listIterator(); table
					.hasNext();) {
				String[] tableContent = (String[]) table.next();
				String content = (String) tableContent[j];
				
				
				//判断超链接
				if(StringUtil.isNotEmpty(content) && content.startsWith("hyperlink-")){
					String cellValue = "";
					/*
					 * 1. 首先在需要强制换行的单元格里使用poi的样式，并且把样式设定为自动换行 
						   # HSSFCellStyle cellStyle=workbook.createCellStyle();     
						   # cellStyle.setWrapText(true);     
						   # cell.setCellStyle(cellStyle); 
						2. 其次是在需要强制换行的单元格，使用/就可以实再强制换行 
						   1. HSSFCell cell = row.createCell((short)0); 
						   2. cell.setCellStyle(cellStyle);                           cell.setCellValue(new HSSFRichTextString("hello/r/n world!")); 
						这样就能实现强制换行，
						换行后的效里是单元格里强制换行
						hello
						world!
					 */
//					HSSFCellStyle cellBrStyle=wb.createCellStyle();     
//					cellBrStyle.setWrapText(true);     
//					cell1.setCellStyle(cellBrStyle); 

//					HSSFCell tmpCell0 = row3.createCell((short) (table.previousIndex()));
//					HSSFCell tmpCell1 = row3.createCell((short) (table.previousIndex() + 1));
//					HSSFCell tmpCell2 = row3.createCell((short) (table.previousIndex() + 2));
					
					sheet.setColumnWidth((short) table.previousIndex(), (short) 0);
					sheet.setColumnWidth((short) (table.previousIndex()+1), (short) 0);
					sheet.setColumnWidth((short) (table.previousIndex()+2), (short) 0);
					
					HSSFCell tmpHeadCell = row2.createCell((short) (table.previousIndex()+3));
//					cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
					tmpHeadCell.setCellValue("所含附件");
					
					int tmpIndex = content.indexOf("-");
					content = content.substring(tmpIndex + 1);
					String[] attachList = content.split("\\|");
					for(int m=0;m<attachList.length;m++){
//						System.out.println("index------"+table.previousIndex() + "---------m-------" + m);
						String tmpAttachLink = attachList[m];
						int fileNameIndex = tmpAttachLink.lastIndexOf("/");
						String linkName = tmpAttachLink.substring(fileNameIndex + 1);

						
						
						HSSFCell linkCell = row3.createCell((short) (table.previousIndex() + 3 + m));
						
//						 CreationHelper createHelper = wb.getCreationHelper();  
//						Hyperlink link = createHelper.createHyperlink(Hyperlink.LINK_URL);
//						link.setAddress("http://poi.apache.org/");
//						linkCell.setHyperlink(link);  
						
						
						//linkCell.setEncoding(HSSFCell.ENCODING_UTF_16);
						linkCell.setCellType(HSSFCell.CELL_TYPE_FORMULA);
						
						if(StringUtil.isNotEmpty(tmpAttachLink)){
							int lastSysbolIndex = tmpAttachLink.lastIndexOf("&");
							System.out.println(tmpAttachLink);
							tmpAttachLink = tmpAttachLink.substring(0,lastSysbolIndex);
						}
						
						linkCell.setCellFormula("HYPERLINK(\"" + tmpAttachLink + "\",\"" + linkName + "\")");

						//把style应用到cell上去
						linkCell.setCellStyle(linkStyle);
						
//						sheet.autoSizeColumn(table.previousIndex() + m, true);
//						if(m < attachList.length - 1){
//							cellValue += "/r/n";
//						}
					}
					
					
					
					
				}else {
					HSSFCell cell1 = row3.createCell((short) table.previousIndex());
//					cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
					
					if (FormatUtil.isNumber(content)) {
						cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell1.setCellValue(content);
					} else {
						cell1.setCellType(HSSFCell.CELL_TYPE_STRING);
						cell1.setCellValue(content);
					}
				}
				

				if (logger.isDebugEnabled()) {
					 logger.debug(i + "行：exportExcel(ExcelObject) - content="
					 + content);
				}
			}
		}

		// 隐藏第一列
		if (!showColumnOne)
			sheet.setColumnWidth((short) 0, (short) 0);
		String filePath = fileFullPath;
		filePath = rootPath + object.getFilePath();
		java.io.File f = new java.io.File(filePath);

		createFilePath(f);
		filePath = filePath + object.getFileName() + ".xls";
		File ioFile = new File(filePath);

		ioFile.createNewFile();
		FileOutputStream fs = new FileOutputStream(ioFile);
		wb.write(fs);
		fs.close();
	}

	public void exportExcelByRow(ExcelObject object, int colNum,
			String fileFullPath, boolean showColumnOne,HttpServletRequest request) throws IOException {
		HSSFWorkbook wb = new HSSFWorkbook();
		HSSFSheet sheet = wb.createSheet("Sheet1");
		int i = 0;
		String rootPath = request.getSession().getServletContext().getRealPath("/");
		// 设定表题
		HSSFRow row1 = sheet.createRow((short) i++);
		HSSFCell titleCell1 = row1.createCell((short) 1);
//		titleCell1.setEncoding(HSSFCell.ENCODING_UTF_16);
		titleCell1.setCellValue(object.getTitle());
		// 设定表的内容
		HSSFRow row2 = sheet.createRow((short) i++);
		// 设定表的列名
		for (ListIterator cell = object.getRowName().listIterator(); cell
				.hasNext();) {
			String rowName = (String) cell.next();
			HSSFCell cell1 = row2.createCell((short) cell.previousIndex());
//			cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
			cell1.setCellValue(rowName);
		}

		for (ListIterator table = object.getTableContent().listIterator(); table
				.hasNext();) {
			String[] tableContent = (String[]) table.next();
			HSSFRow row = sheet.createRow((short) i++);
			for (int j = 0; j < colNum; j++) {
				String content = (String) tableContent[j];
				HSSFCell cell1 = row.createCell((short) j);
//				cell1.setEncoding(HSSFCell.ENCODING_UTF_16);
				cell1.setCellValue(content);
			}
		}
		// 隐藏第一列
		if (!showColumnOne)
			sheet.setColumnWidth((short) 0, (short) 0);
		String filePath = fileFullPath;
		if (filePath == null || filePath.equals("")) {
			filePath =  rootPath + object.getFilePath()
					+ object.getFileName() + ".xls";
		}
		File f = new File(filePath);
		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();
		FileOutputStream fs = new FileOutputStream(f);
		wb.write(fs);
		fs.close();
	}

	public ExcelObject importExcel(FormFile file) throws FileNotFoundException,
			IOException {
		if (logger.isDebugEnabled()) {
			// logger.debug("importExcel(FormFile) - start");
		}

		// 把文件读入
		String filePath = "/uploadfiles/submit/" + file.getFileName();
		File f = new File(filePath);

		if (f.exists()) {
			f.delete();
		}
		f.createNewFile();

		InputStream stream = file.getInputStream();
		OutputStream bos = new FileOutputStream(filePath);
		int bytesRead = 0;
		byte[] buffer = new byte[8192];
		while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
			// 将文件写入服务器
			bos.write(buffer, 0, bytesRead);
		}
		bos.close();
		stream.close();

		ExcelObject object = importExcel(f);

		if (logger.isDebugEnabled()) {
			// logger.debug("importExcel(FormFile) - end");
		}
		return object;
	}

	public ExcelObject importExcel(File file) throws FileNotFoundException,
			IOException {
		ExcelObject object = new ExcelObject();
		object.setFileName(file.getName().substring(0,
				file.getName().lastIndexOf(".")));
		// 创建对Excel工作簿文件的引用
		HSSFWorkbook workbook = new HSSFWorkbook(new FileInputStream(file));
		// 创建对工作表的引用。
		// 本例是按名引用（让我们假定那张表有着缺省名"Sheet1"）
		HSSFSheet sheet = workbook.getSheet("Sheet1");

		int i = 0;

		// 读取Excel内容
		HSSFRow row1 = sheet.getRow(i++);
		HSSFCell aCell = row1.getCell((short) 1);
		if (aCell != null)
			object.setTitle(aCell.getStringCellValue());

		HSSFRow row2 = sheet.getRow(i++);

		// 获得列名
		List rowName = new ArrayList();
		List tableContent = new ArrayList();
		if (logger.isDebugEnabled()) {
			logger.debug("importExcel(FormFile) cellNum = "
					+ row2.getPhysicalNumberOfCells());
			logger.debug("importExcel(FormFile) RowNum = "
					+ sheet.getPhysicalNumberOfRows());
		}
		for (int j = 0; j < row2.getPhysicalNumberOfCells(); j++) {
			HSSFCell cell = row2.getCell((short) j);
			rowName.add(cell.getStringCellValue());
			if (logger.isDebugEnabled()) {
				// logger.debug("importExcel(FormFile) CellValue = " +
				// cell.getStringCellValue());
			}
			// 获得表的内容
			List list = new ArrayList();

			for (int k = 2; k < sheet.getPhysicalNumberOfRows(); k++) {
				HSSFRow row3 = sheet.getRow(k);
				HSSFCell cell3 = row3.getCell((short) j);
				if (cell3 == null) {
					list.add("");
					continue;
				}

				switch (cell3.getCellType()) {
				// 如果单元格为空
				case HSSFCell.CELL_TYPE_BLANK:
					list.add("");
					break;
				// 如果单元格为布尔型
				case HSSFCell.CELL_TYPE_BOOLEAN:
					list.add(String.valueOf(cell3.getBooleanCellValue()));
					break;
				// 如果单元格为错误型
				case HSSFCell.CELL_TYPE_ERROR:
					list.add("");
					break;
				// 如果单元格为公式
				case HSSFCell.CELL_TYPE_FORMULA:
					list.add(cell3.getCellFormula());
					break;
				// 如果单元格为数字
				case HSSFCell.CELL_TYPE_NUMERIC:
					if (HSSFDateUtil.isCellDateFormatted(cell3)) {
						// 如果是日期
						Date cellDate = cell3.getDateCellValue();
						list.add(new java.sql.Date(cellDate.getTime()));
					} else {
						// 如果是数字
						list.add(String.valueOf(cell3.getNumericCellValue()));
					}
					break;
				// 如果单元格为字符
				case HSSFCell.CELL_TYPE_STRING:
					list.add(cell3.getStringCellValue());
					break;
				}
			}
			tableContent.add(list);
		}
		object.setRowName(rowName);
		object.setTableContent(tableContent);
		return object;
	}

	public void exportExcel(ExcelObject object, int rowNum, boolean showRowOne,HttpServletRequest request)
			throws IOException {
		exportExcel(object, rowNum, null, showRowOne,request);
	}
	
	 public void createFilePath(java.io.File ioFile){
	    	if(ioFile!=null){
	    		java.io.File parentFile = ioFile.getParentFile();
	    		if (parentFile!=null && !parentFile.exists()){
	    			createFilePath(parentFile);
	    			parentFile.mkdir();
	    		}
	    		ioFile.mkdir();
	    	}
	    	
	    }

	/**
	 * 测试使用的POI版本是3.1
	 * 
	 * @param args
	 */
	public static void main(String[] args) throws IOException {
//		HSSFWorkbook wb = new HSSFWorkbook();
//		HSSFSheet sheet = wb.createSheet("new sheet");
//
//		HSSFRow row = sheet.createRow(1);
//		HSSFCell cell = row.createCell((short) 1);
//		cell.setCellValue("This is a test of merging");
//
//		// 1.生成字体对象
//		HSSFFont font = wb.createFont();
//		font.setFontHeightInPoints((short) 10);
//		font.setFontName("新宋体");
//		font.setColor(HSSFColor.BLUE.index);
//		font.setBoldweight((short) 0.8);
//		// 2.生成样式对象
//		HSSFCellStyle style = wb.createCellStyle();
//		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
//		style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//		style.setFont(font); // 调用字体样式对象
//		style.setWrapText(true);
//		// 增加表格边框的样式 例子
//		style.setBorderTop(HSSFCellStyle.BORDER_DOUBLE);
//		style.setBorderLeft(HSSFCellStyle.BORDER_DOUBLE);
//		style.setTopBorderColor(HSSFColor.GOLD.index);
//		style.setLeftBorderColor(HSSFColor.PLUM.index);
//
//		// 3.单元格应用样式
//		cell.setCellStyle(style);
//
//		// 新版用法 3.8版
//		// sheet.addMergedRegion(new CellRangeAddress(
//		// 1, //first row (0-based) from 行
//		// 2, //last row (0-based) to 行
//		// 1, //first column (0-based) from 列
//		// 1 //last column (0-based) to 列
//		// ));
//		// 表示合并B2,B3
//		sheet.addMergedRegion(new Region(1, // first row (0-based)
//				(short) 1, // first column (0-based)
//				2, // last row (0-based)
//				(short) 1 // last column (0-based)
//		));
//
//		// 合并叠加 表示合并B3 B4。但是B3已经和B2合并了，所以，变成B2:B4合并了
//		sheet.addMergedRegion(new Region(2, // first row (0-based)
//				(short) 1, // first column (0-based)
//				3, // last row (0-based)
//				(short) 1 // last column (0-based)
//		));
//
//		// 一下代码表示在D4 cell 插入一段字符串
//		HSSFRow row2 = sheet.createRow(3);
//		HSSFCell cell2 = row2.createCell((short) 3);
//		cell2.setCellValue("this is a very very very long string , please check me out.");
//		// cell2.setCellValue(new HSSFRichTextString("我是单元格！"));
//
//		// Write the output to a file
//		FileOutputStream fileOut = new FileOutputStream("workbook.xls");
//		wb.write(fileOut);
//		fileOut.close();
		
		/*try {      
            HSSFWorkbook wb = new HSSFWorkbook();      
            HSSFSheet sheet = wb.createSheet("sheet1");      
            HSSFCellStyle style = wb.createCellStyle(); // 样式对象      
     
            style.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);// 垂直      
            style.setAlignment(HSSFCellStyle.ALIGN_CENTER);// 水平      
            HSSFRow row = sheet.createRow((short) 0);      
            HSSFRow row2 = sheet.createRow((short) 1);      
     
            sheet.addMergedRegion(new Region(0, (short) 0, 1, (short) 0));      
            HSSFCell ce = row.createCell((short) 0);      
            ce.setEncoding(HSSFCell.ENCODING_UTF_16);// 中文处理      
            ce.setCellValue("项目\\日期"); // 表格的第一行第一列显示的数据      
            ce.setCellStyle(style); // 样式，居中      
            int num = 0;      
            for (int i = 0; i < 9; i++) { // 循环9次，每一次都要跨单元格显示      
                // 计算从那个单元格跨到那一格      
                int celln = 0;      
                int celle = 0;      
                if (i == 0) {      
                    celln = 0;      
                    celle = 1;      
                } else {      
                    celln = (i * 2);      
                    celle = (i * 2 + 1);      
                }      
                // 单元格合并      
                // 四个参数分别是：起始行，起始列，结束行，结束列      
                sheet.addMergedRegion(new Region(0, (short) (celln + 1), 0,      
                        (short) (celle + 1)));      
                HSSFCell cell = row.createCell((short) (celln + 1));      
                cell.setCellValue("merging" + i); // 跨单元格显示的数据      
                cell.setCellStyle(style); // 样式      
                // 不跨单元格显示的数据，如：分两行，上一行分别两格为一格，下一行就为两格，“数量”，“金额”      
                HSSFCell cell1 = row2.createCell((short) celle);      
                HSSFCell cell2 = row2.createCell((short) (celle + 1));      
                cell1.setEncoding(HSSFCell.ENCODING_UTF_16);      
                cell1.setCellValue("数量");      
                cell1.setCellStyle(style);      
                cell2.setEncoding(HSSFCell.ENCODING_UTF_16);      
                cell2.setCellValue("金额");      
                cell2.setCellStyle(style);      
                num++;      
            }      
     
            // 在后面加上合计百分比      
     
            // 合计 在最后加上，还要跨一个单元格      
            sheet.addMergedRegion(new Region(0, (short) (2 * num + 1), 0,      
                    (short) (2 * num + 2)));      
            HSSFCell cell = row.createCell((short) (2 * num + 1));      
            cell.setEncoding(HSSFCell.ENCODING_UTF_16);      
            cell.setCellValue("合计");      
            cell.setCellStyle(style);      
            HSSFCell cell1 = row2.createCell((short) (2 * num + 1));      
            HSSFCell cell2 = row2.createCell((short) (2 * num + 2));      
            cell1.setEncoding(HSSFCell.ENCODING_UTF_16);      
            cell1.setCellValue("数量");      
            cell1.setCellStyle(style);      
            cell2.setEncoding(HSSFCell.ENCODING_UTF_16);      
            cell2.setCellValue("金额");      
            cell2.setCellStyle(style);      
     
            // 百分比 同上      
            sheet.addMergedRegion(new Region(0, (short) (2 * num + 3), 0,      
                    (short) (2 * num + 4)));      
            HSSFCell cellb = row.createCell((short) (2 * num + 3));      
            cellb.setEncoding(HSSFCell.ENCODING_UTF_16);      
             
            cellb.setCellValue("百分比");      
            cellb.setCellStyle(style);      
              
            HSSFCell cellb1 = row2.createCell((short) (2 * num + 3));      
            HSSFCell cellb2 = row2.createCell((short) (2 * num + 4));      
            cellb1.setEncoding(HSSFCell.ENCODING_UTF_16);      
            cellb1.setCellValue("数量");      
            cellb1.setCellStyle(style);      
            cellb2.setEncoding(HSSFCell.ENCODING_UTF_16);      
            cellb2.setCellValue("金额");      
            cellb2.setCellStyle(style);      
     
            *//***这里是问题的关键，将这个工作簿写入到一个流中就可以输出相应的名字，这里需要写路径就ok了。 **//*  
            FileOutputStream fileOut = new FileOutputStream("workbook1.xls");     
            wb.write(fileOut);     
            fileOut.close(); 
             
              
              
            *//**第二种是输出到也面中的excel名称 
             * pName="栏目统计表";    
    response.reset();    
    response.setContentType("application/x-msdownload");    
    response.setHeader("Content-Disposition","attachment; filename="+new String(pName.getBytes("gb2312"),"ISO-8859-1")+".xls");    
    ServletOutputStream outStream=null;    
   
    try{    
        outStream = response.getOutputStream();    
        wb.write(outStream);    
    }catch(Exception e)    
    {    
     e.printStackTrace();    
    }finally{    
        outStream.close();    
    }    
             * *//*  
            System.out.print("OK");      
        } catch (Exception ex) {      
            ex.printStackTrace();      
        }     */ 
     
    }      

}