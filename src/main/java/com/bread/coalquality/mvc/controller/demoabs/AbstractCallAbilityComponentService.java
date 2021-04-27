package com.bread.coalquality.mvc.controller.demoabs;


import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * @Description:抽象的调用三方能力服务实现类
 * @Author: haoxd
 * @Version: 1.0
 */
public abstract class AbstractCallAbilityComponentService implements ICallAbilityService {


    /**
     * 入参组织
     *
     * @param req
     * @return
     * @throws Exception
     */
    @Override
    public abstract Map<String, Object> parseReq(Map<String, Object> req);

    /**
     * 出参转换
     *
     * @param rsp
     * @return
     */
    @Override
    public abstract JSONObject parseResp(JSONObject rsp);

    /**
     * 接口调用
     *
     * @param id      接口编码
     * @param reqBody 请求参数
     * @return
     */
    @Override
    public JSONObject callAbility(Long id, Map<String, Object> reqBody) {
        System.out.println("AbstractCallAbilityComponentService#callAbility");
        return new JSONObject();
    }
}
