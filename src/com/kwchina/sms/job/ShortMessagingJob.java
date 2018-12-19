package com.kwchina.sms.job;

import static com.kwchina.sms.misc.SysConstants.gatewayId;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.kwchina.sms.tencentcloud.SMSSend;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

import cn.sendsms.MessageEncodings;
import cn.sendsms.OutboundMessage;
import cn.sendsms.Service;
import cn.sendsms.modem.ModemGateway;

import com.kwchina.sms.dao.BaseDAO;
import com.kwchina.sms.entity.SMSMessagesToSend;
import com.kwchina.sms.misc.SMSServlet;

public class ShortMessagingJob implements Job {

	private Service service = SMSServlet.getService();
	private ModemGateway gateway = SMSServlet.getGateway();

	private static String querySQL = "select * from SMS_MessagesToSend where transmitStatus in (0,2)";

	public void execute(JobExecutionContext context) throws JobExecutionException {
		OutboundMessage msg;

		BaseDAO<SMSMessagesToSend> baseDao = new BaseDAO(SMSMessagesToSend.class);
		List<SMSMessagesToSend> msgs = baseDao.query(querySQL, (Object[]) null, false);

		if (msgs != null) {
			if (msgs.size() > 0) {
				//logger.info("待发送短信：" + msgs.size());
				System.out.println("待发送短信：" + msgs.size());
				int realNum = 0;

				List<OutboundMessage> messages = new ArrayList<OutboundMessage>();
				try {
					for (SMSMessagesToSend message : msgs) {
						int messageId = message.getMessageId();

						if (message.getStatus() == 1) {
							//Timestamp sDate = new Timestamp(System.currentTimeMillis());

							SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
							Calendar calEnd = Calendar.getInstance();

							Calendar calStart = Calendar.getInstance();
							calStart.add(Calendar.MINUTE, -5);
							calEnd.add(Calendar.MINUTE, 5);
							java.util.Date sDate = calStart.getTime();

							java.util.Date eDate = calEnd.getTime();
							String sDateStr = df.format(sDate);
							Timestamp sd = Timestamp.valueOf(sDateStr);
							String eDateStr = df.format(eDate);
							Timestamp ed = Timestamp.valueOf(eDateStr);
							String sTime = message.getScheduleDate().toString();
							Timestamp st = Timestamp.valueOf(sTime);
							if (st.after(sd) && ed.after(st)) {

								System.out.println("定时发送短信");

							} else {
								continue;
							}


						}


						String[] mobileNos = message.getMobileNos().split(",");
						String messageText = "海通OA短信提醒：" + message.getMessageText();
						//腾讯云短信params
						String[] msgtest = new String[1];
						msgtest[0] = messageText;


						for (int j = 0; j < mobileNos.length; j++) {
							String mobileNo = mobileNos[j];
							Pattern pattern = Pattern.compile("^((\\+{0,1}86){0,1})(13|15|18)[0-9]{9}");
							Matcher matcher = pattern.matcher(mobileNo);
							// 判断手机号码格式是否规范
							if (matcher.matches() || mobileNo.equals("10086")) {

								SMSSend a = new SMSSend();
								a.SMSSendMessageSingle(mobileNo, SMSSend.TEMPLATEID1, msgtest);


							} else {
								//logger.error("错误的手机号码：" + mobileNo);
							}
						}

						//更新消息的状态为已经发送
						String updateSQL = "update SMS_MessagesToSend set transmitStatus = 1 where messageId = ?";
						Object params[] = new Object[]{messageId};
						baseDao.update(updateSQL, params, false);
					}

					//发送短信（异步发送，待发送短信进入队列，发送时不做任何等待）
//					service.queueMessages(messages, gatewayId);

					realNum += messages.size();
				} catch (Exception e) {
					//logger.error(e.getMessage());
					System.out.println(e.getMessage());
				} finally {
					msg = null;
					msgs = null;
					messages = null;
				}

				//记录实际发送的短消息条数
				//logger.info("--待发送短信：--" + realNum);
			} else {
				System.out.println("无待发送短信！");
				//logger.info("无待发送短信！");
			}
		}
	}

}
