package com.kwchina.extend.club.service.impl;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.common.BitMatrix;
import com.kwchina.core.common.service.BasicManagerImpl;
import com.kwchina.core.sys.CoreConstant;
import com.kwchina.extend.club.dao.ClubInforDAO;
import com.kwchina.extend.club.entity.ClubInfor;
import com.kwchina.extend.club.service.ClubInforManager;
import com.kwchina.extend.club.util.MatrixToImageWriter;

@Service("clubInforManager")
public class ClubInforManagerImpl extends BasicManagerImpl<ClubInfor> implements ClubInforManager {

	private ClubInforDAO clubInforDAO;


	//注入的方法
	@Autowired
	public void setClubInforDAO(ClubInforDAO clubInforDAO) {
		this.clubInforDAO = clubInforDAO;
		super.setDao(clubInforDAO);
	}
	

	//得到所有未结束的活动
	public List<ClubInfor> getUnfinishedInfors(){
		List<ClubInfor> list = new ArrayList<ClubInfor>();
		String hql = "from ClubInfor clubInfor where isDeleted=0 and status<"+ClubInfor.Club_Status_Over;
		//and to_char(toTime,'yyyy-MM-dd')='"+todayStr+"' 
		list = this.clubInforDAO.getResultByQueryString(hql);
		return list;
	}
	
	//更改活动状态
	public void changeTaskStatus(ClubInfor clubInfor,int status){
		
		try {
			long current = System.currentTimeMillis();
			
			//状态值为2时，即开始报名，此时生成签到二维码  同时，默认发起人及管理员报名(后改为默认都不报名)
			if(status == ClubInfor.Club_Status_Reging){
				if(clubInfor.getTwoPic() == null || clubInfor.getTwoPic().equals("")){
					/*************生成签到二维码***************/
					//二维码内容
					String content = "/club/actAttendInfor.do?method=saveAttend&actId="+clubInfor.getActId()+"&attLocation=";
					int width = 300;          
					int height = 300;          
					//二维码的图片格式          
					String format = "gif";          
					Hashtable hints = new Hashtable();          
					//内容所使用编码          
					hints.put(EncodeHintType.CHARACTER_SET, "utf-8");          
					BitMatrix bitMatrix = new MultiFormatWriter().encode(content,BarcodeFormat.QR_CODE, width, height, hints); 
					//生成二维码     
					//路径
					String savePath = CoreConstant.Attachment_Path + "clubInfor";
					java.io.File file = new java.io.File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					//在folder下面建立目录，以当前时间为目录
					savePath += "/" + current;
					file = new java.io.File(CoreConstant.Context_Real_Path + savePath);
					if(!file.exists()){
						file.mkdir();
					}
					
					savePath += "/" + current + ".gif";
					
					java.io.File outputFile = new java.io.File(CoreConstant.Context_Real_Path + savePath);          
					MatrixToImageWriter.writeToFile(bitMatrix, format, outputFile); 
					
					clubInfor.setTwoPic(CoreConstant.Attachment_Path + "clubInfor" + "/" + current + "/" + current + ".gif");
					/***************************/
				}
				
				/*//报名
				Set<RegisterInfor> registers = clubInfor.getRegisters();
				Timestamp sysTime = new Timestamp(current);
				
				//是否已报过
				boolean hasCreated = false;
				boolean hasManaged = false;
				for(RegisterInfor reg : registers){
					if(reg.getReger().getPersonId().intValue() == clubInfor.getCreater().getPersonId().intValue()){
						hasCreated = true;
					}
					if(reg.getReger().getPersonId().intValue() == clubInfor.getManager().getPersonId().intValue()){
						hasManaged = true;
					}
				}
				
				//发起者
				if(!hasCreated){
					RegisterInfor register1 = new RegisterInfor();
					register1.setClubInfor(clubInfor);
					register1.setReger(clubInfor.getCreater());
					register1.setRegTime(sysTime);
					this.registerInforManager.save(register1);
				}
				
				
				//发起者
				if(!hasManaged){
					RegisterInfor register2 = new RegisterInfor();
					register2.setClubInfor(clubInfor);
					register2.setReger(clubInfor.getManager());
					register2.setRegTime(sysTime);
					this.registerInforManager.save(register2);
				}*/
				
			}
			
			//状态值为5时，即结束，此时保存结束时间
			if(status == ClubInfor.Club_Status_Over){
				clubInfor.setEndTime(new java.sql.Date(current));
			}
			
			clubInfor.setStatus(status);
			
			save(clubInfor);
		} catch (Exception e) {
			
		}
	}

}
