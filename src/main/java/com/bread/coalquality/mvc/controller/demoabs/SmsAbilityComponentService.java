package com.bread.coalquality.mvc.controller.demoabs;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 短信能力组件服务
 * @Author: haoxd
 * @Version: 1.0
 */
@Service
public class SmsAbilityComponentService extends BaseNmMicroServiceTransferComponentService {


    private static final String METHOD_NAME = "sendSmsMessage";


    /**
     * 发送短信
     *
     * @param serialNumber 手机号
     * @param message      短信内容
     */
    public void send(String serialNumber, String message) {

        System.out.println("QuerySysUserInfoComponentService#query");
        Map<String, Object> stringObjectMap = parseReq(new HashMap<>());
        JSONObject call = call(METHOD_NAME, stringObjectMap);
        JSONObject jsonObject = parseResp(call);
        System.out.println("QuerySysUserInfoComponentService#query");

    }

    @Override
    public JSONObject parseResp(JSONObject rsp) {
        System.out.println("SmsAbilityComponentService#parseResp");

        return new JSONObject();
    }

    @Override
    public Map<String, Object> parseReq(Map<String, Object> requestBody) {
        System.out.println("SmsAbilityComponentService#parseReq");
        return new HashMap<>();
    }

    public static void main(String[] args) {
        SmsAbilityComponentService smsAbilityComponentService = new SmsAbilityComponentService();
        smsAbilityComponentService.send("1", "2");
    }

}
