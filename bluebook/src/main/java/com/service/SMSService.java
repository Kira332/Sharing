package com.service;

import com.github.qcloudsms.*;
import com.github.qcloudsms.httpclient.HTTPException;
import com.util.Constant;
import com.util.RedisOperator;
import com.util.SMSUtil;
import com.util.component.PhoneRandomBuilder;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;

//腾讯短信服务类
@Service
public class SMSService {

    @Autowired
    private RedisOperator redisOperator;

    public String sendMess(String msg, String number){
        try {
            SmsSingleSender ssender = new SmsSingleSender(SMSUtil.APPID, SMSUtil.APPKEY);
            SmsSingleSenderResult result = ssender.send(0, "86", number,
                    msg, "", "");
            System.out.print(result);
            return result.errMsg;
        } catch (HTTPException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String sendMesModel(String number, Integer mark){
        try {
            String code = PhoneRandomBuilder.randomBuilder();
            redisOperator.set(Constant.USER_PHONE_CODE + number, code);
            redisOperator.expire(Constant.USER_PHONE_CODE + number, 300);

            String[] params = {code};
            SmsSingleSender ssender = new SmsSingleSender(SMSUtil.APPID, SMSUtil.APPKEY);
            if(mark == 0){
                SmsSingleSenderResult result = ssender.sendWithParam("86", number,
                        SMSUtil.SHORTNOTID, params, "", "", "");
                System.out.print(result);
                return result.errMsg;
            }else if(mark == 1){
                SmsSingleSenderResult result = ssender.sendWithParam("86", number,
                        SMSUtil.UPDPWDID, params, "", "", "");
                System.out.print(result);
                return result.errMsg;
            }
        }catch (HTTPException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    public String sendMesModel(String msg, String[] numbers){
        try {
            SmsMultiSender msender = new SmsMultiSender(SMSUtil.APPID, SMSUtil.APPKEY);
            SmsMultiSenderResult result =  msender.send(0, "86", numbers,
                    msg, "", "");
            System.out.print(result);
            return result.errMsg;
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
        return null;
    }


    public String sendMesModel(String[] numbers){
        try {
            String[] params = {"hello" , "1" };
            SmsMultiSender msender = new SmsMultiSender(SMSUtil.APPID, SMSUtil.APPKEY);
            SmsMultiSenderResult result =  msender.sendWithParam("86", numbers,
                    SMSUtil.SHORTNOTID, params, SMSUtil.NOTESIGN, "", "");  // 签名参数未提供或者为空时，会使用默认签名发送短信
            System.out.print(result);
            return result.errMsg;
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
        return null;
    }

}
