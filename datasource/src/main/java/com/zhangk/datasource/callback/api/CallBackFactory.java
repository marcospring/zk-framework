package com.zhangk.datasource.callback.api;

import java.util.Map;

/**
 * 回调接口
 * Created by zhangkui on 16/5/20.
 */
public interface CallBackFactory {

    CallBackHandler build(String callBackName, Map<String, Object> param);
}
