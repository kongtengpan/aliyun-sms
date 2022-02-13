package com.sms.controller;

import com.aliyuncs.utils.StringUtils;
import com.sms.service.SendSms;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@RestController
@CrossOrigin //跨域支持
public class SmsApiController {

    @Autowired
    private SendSms sendSms;

    @Autowired
    private RedisTemplate<String, String> redisTemplate;

    @GetMapping("/send/{phone}")
    public String code(@PathVariable("phone") String phone) {
        //判断redis中有没有当前手机号
        String code = redisTemplate.opsForValue().get(phone);
        if (!StringUtils.isEmpty(code)) {
            return phone + ":" + code + "已存在，还没有过期";
        }
        //生成随机四位数验证码并存储到redis
        code = UUID.randomUUID().toString().substring(0, 4);
        HashMap<String, Object> param = new HashMap<>();
        param.put("code", code);
        //发送验证码
        boolean isSend = sendSms.send(phone, "传阿里云申请的模板id", param);
        //如果发送成功存到redis
        if (isSend) {
            //5分钟过期
            redisTemplate.opsForValue().set(phone, code, 5, TimeUnit.SECONDS);
            return phone + ":" + code + "发送成功";
        } else {
            return phone + ":" + code + "发送失败";
        }
    }
}
