package com.bread.coalquality.sys.constans;


/**
 * @Description: 状态码枚举
 * @Author: haoxd
 * @Version: 1.0
 */
public enum RespConstans {

    REPALCE_FAIL("-5", "重复操作"),
    RPC_FAIL("-4", "远程调用失败"),
    ACCESS_FAIL("-3", "没有权限"),
    USERNAME_PASSWORD_FAIL("-2", "用户名密码错误"),
    FAIL_CODE("9999", "失败"),
    SUCCESS_CODE("0000", "成功");

    RespConstans(String code, String codeDesc) {
        this.codeDesc = codeDesc;
        this.code = code;
    }

    private String codeDesc;
    private String code;


    public String getCodeDesc() {
        return codeDesc;
    }

    public void setCodeDesc(String codeDesc) {
        this.codeDesc = codeDesc;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
