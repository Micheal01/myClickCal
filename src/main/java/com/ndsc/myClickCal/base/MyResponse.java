package com.ndsc.myClickCal.base;

import lombok.Data;
import org.slf4j.Logger;

import java.io.Serializable;

/**
 * 返回响应基类
 * @author Michael
 * @since 2018-8-28
 */
@Data
public class MyResponse implements Serializable {

    private static final long serialVersionUID = -6851011668887357743L;

    private String code;

    private Object data;

    private String msg;

    public void success(Object data)
    {
        this.code=ResponseEnumCode.SUCCESS.getCode();
        this.msg=ResponseEnumCode.SUCCESS.getMsg();
        this.data=data;
    }

    public void fail(ResponseEnumCode code)
    {
        fail(code,null,null);
    }
    public void fail(ResponseEnumCode code,Logger logger)
    {
        fail(code,logger,null);
    }

    public void fail(ResponseEnumCode code, Logger logger, Exception e)
    {
        this.code=code.getCode();
        this.msg=code.getMsg();
        if(logger!=null)
        {
            //本地文件输出日志
            String errMsg=code.getMsg();
            logger.error(errMsg+":\r\n");
            if(e!=null)
            {
                e.printStackTrace();
            }

        }
    }

}
