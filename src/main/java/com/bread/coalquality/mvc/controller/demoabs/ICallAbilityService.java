package com.bread.coalquality.mvc.controller.demoabs;

import com.alibaba.fastjson.JSONObject;

import java.util.Map;

/**
 * 调用Ability服务接口
 * 需要统一处理出入参的能力平台接口实现新建抽象类，实现此接口
 *
 * @author zhanghl
 * @date 2021/01/29
 */
public interface ICallAbilityService {
    /**
     * 入参组织
     *
     * @param req
     * @return
     * @throws Exception
     */
    Map<String, Object> parseReq(Map<String, Object> req) ;

    /**
     * 出参转换
     *
     * @param rsp
     * @return
     */
    JSONObject parseResp(JSONObject rsp) ;


    /**
     * 接口调用
     *
     * @param id
     * @param reqBody
     * @return
     */
    JSONObject callAbility(Long id, Map<String, Object> reqBody) ;

}
