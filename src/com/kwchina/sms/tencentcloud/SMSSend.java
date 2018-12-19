package com.kwchina.sms.tencentcloud;


import com.github.qcloudsms.SmsMultiSender;
import com.github.qcloudsms.SmsMultiSenderResult;
import com.github.qcloudsms.SmsSingleSender;
import com.github.qcloudsms.SmsSingleSenderResult;
import com.github.qcloudsms.httpclient.HTTPException;
import org.json.JSONException;

import java.io.IOException;
/**
 * Create by yuanjl on 2018/5/8
 */
public class SMSSend{
    // 短信应用SDK AppID
    public static final int APPID = 1400116660;
    // 短信应用SDK AppKey
    public static final String APPKEY ="66dcfdd8ad43871bd111cf9022633186";
    // 短信模板ID，需要在短信应用中申请
    public static final int TEMPLATEID1 = 179160;
    // 签名
    public static final String SMSSIGN= "海通OA";
    //群发
    public void SMSSendMessageALL(String[] phoneNumbers,int templateId,String[] params) {

        try {
            //            String[] params = {"5678"};
            SmsMultiSender msender = new SmsMultiSender(APPID, APPKEY);
            SmsMultiSenderResult result = msender.sendWithParam("86", phoneNumbers,
                    templateId, params, SMSSIGN, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }
    public void SMSSendMessageSingle(String phoneNumber,int templateId,String[] params) {
        try {
//                String[] params = {"5678"};
            SmsSingleSender ssender = new SmsSingleSender(APPID, APPKEY);
            SmsSingleSenderResult result = ssender.sendWithParam("86", phoneNumber,
                    templateId, params, SMSSIGN, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
        } catch (HTTPException e) {
            // HTTP响应码错误
            e.printStackTrace();
        } catch (JSONException e) {
            // json解析错误
            e.printStackTrace();
        } catch (IOException e) {
            // 网络IO错误
            e.printStackTrace();
        }
    }

}

