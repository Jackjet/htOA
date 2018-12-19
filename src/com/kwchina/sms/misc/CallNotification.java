package com.kwchina.sms.misc;

import cn.sendsms.ICallNotification;

public class CallNotification implements ICallNotification {
	public void process(String gatewayId, String callerId) {

		//logger.info(">>> 监测到有电话打入: " + gatewayId + " : " + callerId);
	}
}