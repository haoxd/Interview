package com.bread.coalquality.sys.common;


import com.bread.coalquality.sys.constans.RespConstans;
import lombok.*;
import lombok.experimental.Accessors;
import org.joda.time.DateTime;

import java.io.Serializable;
import java.util.Date;


@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
@Builder
public class RespInfo<T> implements Serializable {


    private static final long serialVersionUID = 1L;
    private String respCode;
    private String respDesc;
    private String time = new DateTime(new Date().getTime()).toString("yyyyMMddHHmmssSSS");
    private Object data;



    public static RespInfo<Void> success() {
        return new RespInfo<Void>().setRespCode(RespConstans.SUCCESS_CODE.getCode()).setRespDesc(RespConstans.SUCCESS_CODE.getCodeDesc());
    }

    public static RespInfo<Void> success(Object data) {
        return new RespInfo<Void>()
                .setRespCode(RespConstans.SUCCESS_CODE.getCode())
                .setRespDesc(RespConstans.SUCCESS_CODE.getCodeDesc())
                .setData(data);
    }

    public static RespInfo<Void> fail(String message) {
        return new RespInfo<Void>().setRespCode(RespConstans.FAIL_CODE.getCode()).setRespDesc(message);

    }
}