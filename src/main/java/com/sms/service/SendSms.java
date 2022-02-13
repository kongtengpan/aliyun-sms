package com.sms.service;

import java.util.Map;

public interface SendSms {
    /**
     *
     * @param phoneNum 传递一个手机号
     * @param templateCode 短信模板code
     * @param code 验证码
     * @return
     */
    boolean send(String phoneNum, String templateCode, Map<String,Object> code);
}

