package com.zhangk.datasource.result;

import java.io.InputStream;

/**
 * 结果消息容器接口
 * @ClassName:  MessageContext   
 * @Description:TODO 
 * @author: zhangk  
 * @date:   2015年11月6日 下午8:05:48   
 *
 */
public interface MessageContext {
	/**
	 * 通过键值获取结果信息对象
	 * @param key
	 * @return
	 * ResultInfo
	 *
	 */
	ResultInfo getResultInfo(String key);

	/**
	 * 通过路径加载结果消息
	 * @param path
	 * void
	 *
	 */
	void addMessageResource(String path);

	/**
	 * 通过输入流加载结果消息
	 * @param in
	 * void
	 *
	 */
	void addMessageResourceFromStream(InputStream in);

}

