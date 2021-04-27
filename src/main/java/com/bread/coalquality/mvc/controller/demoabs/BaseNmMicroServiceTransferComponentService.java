package com.bread.coalquality.mvc.controller.demoabs;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.Maps;

import java.util.Map;

/**
 * 调用内蒙省份微服务统一基础类
 *
 * @author haoxd
 * @date 2021/01/29
 */
public class BaseNmMicroServiceTransferComponentService extends AbstractCallAbilityComponentService {



    @Override
    public JSONObject parseResp(JSONObject rsp) {
        System.out.println("BaseNmMicroServiceTransferComponentService#parseResp");
        return null;
    }
    /**
     * 入参转换
     *
     * @param requestBody
     * @return
     * @throws Exception
     */
    @Override
    public Map<String, Object> parseReq(Map<String, Object> requestBody) {
        System.out.println("BaseNmMicroServiceTransferComponentService#parseReq");
        return null;
    }



    private Map<String, Object> parseNmMicroServiceReq(Map<String, Object> requestBody) {
        Map<String, Object> req = Maps.newHashMapWithExpectedSize(1);
        req.put("requestBody", requestBody);
        return req;
    }


    protected JSONObject call(String methodName, Map<String, Object> reqBody) {
        System.out.println("BaseNmMicroServiceTransferComponentService#call->"+methodName);
        Map<String, Object> req = this.parseNmMicroServiceReq(reqBody);
        req.put("methodName", methodName);
        return super.callAbility(1L, req);
    }
}
