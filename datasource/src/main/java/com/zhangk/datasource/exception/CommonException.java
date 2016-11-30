package com.zhangk.datasource.exception;

/**
 * Created by zhangkui on 2016/11/30.
 */
public class CommonException extends BusinessException{
    private static final long serialVersionUID = 1L;

    public CommonException() {
    }

    public CommonException(String exceptionKey) {
        setExceptionKey(exceptionKey);
    }
}
