package com.sms.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.aliyuncs.CommonRequest;
import com.aliyuncs.CommonResponse;
import com.aliyuncs.DefaultAcsClient;
import com.aliyuncs.IAcsClient;
import com.aliyuncs.exceptions.ClientException;
import com.aliyuncs.exceptions.ServerException;
import com.aliyuncs.http.MethodType;
import com.aliyuncs.profile.DefaultProfile;
import com.sms.service.SendSms;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class SendSmsImpl implements SendSms {

    @Override
    public boolean send(String phoneNum, String templateCode, Map<String, Object> code) {
        //regionId不用变
        //accessKeyId是在阿里云申请的key
        //secret:是密码
        //连接阿里云
        DefaultProfile profile = DefaultProfile.getProfile("cn-qingdao", "<your-access-key-id>", "<your-access-key-secret>");
        IAcsClient client = new DefaultAcsClient(profile);
        //构建请求
        CommonRequest request = new CommonRequest();
        request.setSysMethod(MethodType.POST);
        request.setSysDomain("dysmsapi.aliyuncs.com"); //不用改
        request.setSysVersion("2017-05-25");//不用改
        request.setSysAction("SendSms");//一般不用该，是一个事件的名称
        //自定义参数(手机号，验证码，签名，模板)
        request.putQueryParameter("PhoneNumbers",phoneNum);
        request.putQueryParameter("SignName","阿里云申请的签名名称");
        request.putQueryParameter("TemplateCode",templateCode);
        //构建一个短信验证码
        request.putQueryParameter("TemplateParam", JSONObject.toJSONString(code));
        try {
            CommonResponse response = client.getCommonResponse(request);
            System.out.println(response.getData());
            //判断是否发送成功
            return response.getHttpResponse().isSuccess();
        } catch (ServerException e) {
            e.printStackTrace();
        } catch (ClientException e) {
            e.printStackTrace();
        }
        return false;
    }
}
