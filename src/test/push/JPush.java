package test.push;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import com.kwchina.core.util.DateHelper;

import cn.jpush.api.JPushClient;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.Notification;

public class JPush {

	private static final String appKey ="0910051c61b7c1d5ab07088c";
	private static final String masterSecret = "2f50e44b8214c8e504f59500";
	
	
	
	private static void testSend() {
		JPushClient jpushClient = new JPushClient(masterSecret, appKey, 3);
		//PushResult result = jpushClient.sendPush(payload); 
		/**
		 * 应用客户推送消息
		 *参数：payload 即第二步创建的推送对象
		 *返回值：PushResult 表示服务端推送结果
		 *包含下列数据：
		 *msg_id：返回推送消息的id
		 **/

	}
	
	
	/**
	 * 确定推送消息的目标，包括推送的平台（Android、IOS）、消息内容和目标（所有人、别名、标签），构建简单的推送对象：向所有平台，所有人，推送内容为content 的通知。
	 * @param content
	 * @return
	 */
	public static PushPayload buildPushObject_all_all_alert(String content) {
		return PushPayload.alertAll(content);  
		}
	
	/**
	 * 构建推送对象：所有平台，推送目标是别名为alias，通知内容为 content。
	 * @param alias
	 * @param content
	 * @return
	 */
	public static PushPayload buildPushObject_all_alias_alert(String alias,String content) {
		return PushPayload.newBuilder()
			.setPlatform(Platform.all())// 所有平台
			.setAudience(Audience.alias(alias))// 向选定的人推送
			.setNotification(Notification.alert(content))// 消息内容
			.build();
	} 
	
	
	/**
	 * 构建推送对象：向android平台，向目标标签tag，通知标题title，内容为 content。
	 * @param alias
	 * @param title
	 * @param content
	 * @return
	 */
	public static PushPayload buildPushObject_android_tag_alertWithTitle (String tag,String title,String content) {
		return PushPayload.newBuilder().setPlatform(Platform.android())
			.setAudience(Audience.tag(tag))// 向指定的组推送
			.setNotification(Notification.android(content, title, null)).build();        
	}
	
	public static void main(String[] args) {
		Timestamp t1 = new Timestamp(System.currentTimeMillis());
		SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Timestamp t2 = Timestamp.valueOf(sf.format(DateHelper.addSecond(DateHelper.addMinute(t1, -2), -30)));
		
		System.out.println(t1);
		System.out.println(t2);
		System.out.println(t2==t2);
	}
}
