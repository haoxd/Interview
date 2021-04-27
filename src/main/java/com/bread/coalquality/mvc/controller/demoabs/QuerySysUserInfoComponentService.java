package com.bread.coalquality.mvc.controller.demoabs;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

/**
 * @Description: 查询中台前台登录用户信息
 * @Author: haoxd
 * @Version: 1.0
 */
@Service
public class QuerySysUserInfoComponentService extends BaseNmMicroServiceTransferComponentService {


    private static final String METHOD_NAME = "getUserInfoByLoginName";


    /**
     * 查询用户信息
     *
     * @return
     */
    public void query(String loginName) {
        System.out.println("QuerySysUserInfoComponentService#query");

        Map<String, Object> stringObjectMap = parseReq(new HashMap<>());
        JSONObject call = call(METHOD_NAME, stringObjectMap);
        JSONObject jsonObject = parseResp(call);
    }


    @Override
    public JSONObject parseResp(JSONObject rsp) {
        System.out.println("QuerySysUserInfoComponentService#parseResp");

        return new JSONObject();
    }

    @Override
    public Map<String, Object> parseReq(Map<String, Object> requestBody) {
        System.out.println("QuerySysUserInfoComponentService#parseReq");
        return new HashMap<>();
    }
    public static void main(String[] args) {
        QuerySysUserInfoComponentService smsAbilityComponentService = new QuerySysUserInfoComponentService();
        smsAbilityComponentService.query("1");
    }
}
