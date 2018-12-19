package com.kwchina.sms.tencentcloud;



import cn.sendsms.OutboundMessage;
import cn.sendsms.Service;
import cn.sendsms.modem.ModemGateway;
import com.kwchina.sms.dao.BaseDAO;
import com.kwchina.sms.entity.SMSMessagesToSend;
import com.kwchina.sms.job.ShortMessagingJob;
import com.kwchina.sms.misc.SMSServlet;
import com.kwchina.sms.service.SmsManager;
import com.kwchina.sms.tencentcloud.SMSSend;

import javax.annotation.Resource;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Create by yuanjl on 2018/11/4 0004
 */
public class toSend {

    @Resource
    private SmsManager smsManager;

    public void run(){

        String hql ="from SMSMessagesToSend a where a.transmitStatus = 0";
        List<SMSMessagesToSend> msgs = this.smsManager.getResultByQueryString(hql);

        if (msgs != null) {
            if (msgs.size() > 0) {
                System.out.println("待发送短信：" + msgs.size());
                try {
                    for (SMSMessagesToSend message : msgs) {
                        if (message.getStatus() == 1) {
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
                        String messageText = "" + message.getMessageText();
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
                        message.setTransmitStatus(1);
                        this.smsManager.save(message);
                    }
                } catch (Exception e) {
                    //logger.error(e.getMessage());
                    System.out.println(e.getMessage());
                } finally {
                    msgs = null;
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

