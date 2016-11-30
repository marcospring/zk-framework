package com.zhangk.datasource.callback.api;


import com.zhangk.datasource.apisupport.BaseService;

import java.util.Map;

/**
 * Created by zhangkui on 16/5/20.
 */
public interface CallBackHandler {

    Map<String,Object> handle(Map<String, Object> param, BaseService service);
}
