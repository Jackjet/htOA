package com.kwchina.oa.sys;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kwchina.oa.workflow.entity.FlowInstanceInfor;

import cn.jpush.api.JPushClient;
import cn.jiguang.common.resp.APIConnectionException;
import cn.jiguang.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Options;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

public class PushUtil {

	private static Log log = LogFactory.getLog(PushUtil.class);

//	private static final String appKey = "0910051c61b7c1d5ab07088c";
//	private static final String masterSecret = "2f50e44b8214c8e504f59500";
//	private static final String appKey = "0a3e4087816af4df23b2839f";
//	private static final String masterSecret = "6383a77ab12519af00db4c72";
	private String appKey = "43ac465549ac12396662afb4";
	private String masterSecret = "498f61024fe91e1a1831f72a";
	private JPushClient jpushClient;
	private String title;
	private String content;
	
	public PushUtil(){}

	public PushUtil(String content) {
		try {
			this.content = content;
			jpushClient = new JPushClient(masterSecret, appKey);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public PushUtil(String content, String title) {
		this(content);
		this.title = title;
	}

	/**
	 * 向所有人发送消息
	 * 
	 * @return 消息id
	 */
	public long sendPushAll() {
		PushPayload payload = buildPushObject_all_all_alert();
		long msgId = 0;
		try {
			PushResult result = jpushClient.sendPush(payload);
			msgId = result.msg_id;
		} catch (APIConnectionException e) {
			// TODO Auto-generated catch block
			log.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			log.info("HTTP Status: " + e.getStatus());
			msgId = e.getMsgId();
		}
		return msgId;
	}

	/**
	 * 向指定别名的客户端发送消息
	 * 
	 * @param alias
	 *            所有别名信息集合，这里表示发送所有学生编号
	 * @return 消息id
	 */
	public long sendPushAlias(String platForm,String alias,Map<String, String> extras,int badge) {
//		PushPayload payloadAlias = buildPushObject_alias_alertWithTitle(platForm,alias,extras,badge);
		PushPayload payloadAlias = buildPushObject_alias_alertWithTitle(
				platForm, alias, extras, badge);
		//PushPayload payloadAlias = buildPushObject_all_alias_alertWithTitle(alias);
		long msgId = 0;
		try {
			PushResult result = jpushClient.sendPush(payloadAlias);
			System.out.println("推送结果:" + result);
			msgId = result.msg_id;

		} catch (APIConnectionException e) {
			log.error("Connection error. Should retry later. ", e);
		} catch (APIRequestException e) {
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			log.info("Msg ID: " + e.getMsgId());
			msgId = e.getMsgId();
		}
		return msgId;
	}

	/**
	 * 向指定组发送消息
	 * 
	 * @param tag
	 *            组名称
	 * @return 消息id
	 */
	public long sendPushTag(String tag) {
		PushPayload payloadtag = buildPushObject_tag_alertWithTitle("android",tag);
		long msgId = 0;
		try {
			PushResult result = jpushClient.sendPush(payloadtag);
			msgId = result.msg_id;
			log.info("Got result - " + result);
		} catch (APIConnectionException e) {
			log.error("Connection error. Should retry later. ", e);

		} catch (APIRequestException e) {
			log.info("HTTP Status: " + e.getStatus());
			log.info("Error Code: " + e.getErrorCode());
			log.info("Error Message: " + e.getErrorMessage());
			log.info("Msg ID: " + e.getMsgId());
			msgId = e.getMsgId();
		}
		return msgId;
	}

	/**
	 * 下列封装了三种获得消息推送对象（PushPayload）的方法
	 * buildPushObject_android_alias_alertWithTitle、
	 * buildPushObject_android_tag_alertWithTitle、 buildPushObject_all_all_alert
	 */
	public PushPayload buildPushObject_alias_alertWithTitle(
			String platForm,String alias,Map<String, String> extras,int badge) {
		
		
		if(platForm.equals("android")){
			return PushPayload.newBuilder()
					.setPlatform(Platform.android())
					.setAudience(Audience.alias(alias))
					.setNotification(
						Notification.android(content, title, extras))
					.build();
		}else if(platForm.equals("ios")){
			/*.setNotification(Notification.ios_set_badge(badge))
			return PushPayload.newBuilder().setPlatform(Platform.ios())
			.setAudience(Audience.alias(alias)).setNotification(Notification.ios_set_badge(badge)).setNotification(
					Notification.ios(content, extras)).setOptions(Options.newBuilder().setApnsProduction(true).build()).build();*/
			
			return PushPayload.newBuilder()
            .setPlatform(Platform.ios())
            .setAudience(Audience.alias(alias))
            .setNotification(Notification.newBuilder()
                    .addPlatformNotification(IosNotification.newBuilder()
                    .setAlert(content)
                    .setBadge(badge)
                    .setSound("happy.caf")
                    .addExtras(extras).build()).build())
             //.setMessage(Message.content(MSG_CONTENT))
            .setOptions(Options.newBuilder().setApnsProduction(true).build())
            .build();
			//True 表示推送生产环境，False 表示要推送开发环境

		}
		
		return null;
	}
	
	public PushPayload buildPushObject_all_alias_alertWithTitle(String alias) {
		
			//return PushPayload.newBuilder().setAudience(Audience.alias(alias)).setNotification(Notification.android(content, title, null)).build();
		return PushPayload.newBuilder()
		.setPlatform(Platform.all())// 所有平台
		.setAudience(Audience.alias(alias))// 向选定的人推送
		.setNotification(Notification.alert(content))// 消息内容
		.build();
	}

	public PushPayload buildPushObject_tag_alertWithTitle(String platForm,String tag) {
		if(platForm.equals("android")){
			return PushPayload.newBuilder().setPlatform(Platform.android())
			.setAudience(Audience.tag(tag)).setNotification(
					Notification.android(content, title, null)).build();
		}else if(platForm.equals("ios")){
			return PushPayload.newBuilder().setPlatform(Platform.ios())
			.setAudience(Audience.tag(tag)).setNotification(
					Notification.android(content, title, null)).build();
		}
		return null;
	}

	public PushPayload buildPushObject_all_all_alert() {
		return PushPayload.alertAll(content);
	}
	
	/**
	 * 推送待办的通用方法
	 * @param instance
	 * @param badge
	 * @param alias
	 */
	public void pushNeedDealInstances(FlowInstanceInfor instance,int badge,String alias){
		int flowId = instance.getFlowDefinition().getFlowId().intValue();
		String instanceTitle = instance.getInstanceTitle();
		int instanceId = instance.getInstanceId();
//		int badge = returnInstances_m.size();
		System.out.println("===="+alias+"：有"+badge+"条待办事项！");
		//每个用户推两个平台//"【"+flowName+"】"+
		PushUtil androidPushUtil = new PushUtil(instanceTitle,"请处理审批");
		PushUtil iosPushUtil = new PushUtil("请处理审批：" + instanceTitle,"");
		
		Map<String, String> extras = new HashMap<String, String>();
		extras.put("badge", String.valueOf(badge));//ios用的角标
		extras.put("instanceId", String.valueOf(instanceId));//instanceId
		
		//流程分类
		/*
		 * android：
		 * 发文 5
		 * 合同 6
		 * 内部报告 7
		 * 制度评审 8
		 * 收文管理 9
		 */
		int flowType = 0;
		if(flowId == 85){
			flowType = 5;
		}else if(flowId == 86){
			flowType = 6;
		}else if(flowId == 87){
			flowType = 7;
		}else if(flowId == 88){
			flowType = 8;
		}else if(flowId == 84){
			flowType = 9;
		}
		extras.put("flowType", String.valueOf(flowType));
		
		long msgId_android = androidPushUtil.sendPushAlias("android", alias, extras, badge);
		long msgId_ios = iosPushUtil.sendPushAlias("ios", alias, extras, badge);
	}
	
	public static void main(String[] args) {
		try {
			PushUtil androidUtil = new PushUtil("内容啊","报告啊");
			Map<String, String> map = new HashMap<String, String>();
			map.put("badge", "23");
			androidUtil.sendPushAlias("android", "admin", map, 23);
			
			
			PushUtil iosUtil = new PushUtil("内容啊1");
			iosUtil.sendPushAlias("ios", "admin", map, 23);
		} catch (RuntimeException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}