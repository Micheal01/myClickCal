package com.ndsc.myClickCal.base;

/**
 * @author Michael
 * @since 2018-8-28
 */
public enum  ResponseEnumCode {

    SUCCESS("100","成功"),

    IllegalArgumentException("400","参数异常"),

    SERVER_EXCEPTION("500","服务异常");

    private String code;

    private String msg;

    ResponseEnumCode(String code,String msg)
    {
        this.code=code;
        this.msg=msg;
    }

    public String getCode() {
        return code;
    }

    public String getMsg() {
        return msg;
    }
}
