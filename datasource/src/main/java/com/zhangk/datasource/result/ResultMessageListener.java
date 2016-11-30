package com.zhangk.datasource.result;

import com.zhangk.datasource.listener.BaseSpringContextRefreshListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.ContextRefreshedEvent;

/**
 * 子系统结果消息监听器
 * @ClassName:  ResultMessageListener   
 * @Description:TODO 
 * @author: zhangk  
 * @date:   2015年11月9日 下午4:43:55   
 *
 */
public class ResultMessageListener extends BaseSpringContextRefreshListener {

	private Logger logger = LoggerFactory.getLogger(ResultMessageListener.class);
	private String resultMessagePath;

	/**
	 * 向系统的结果消息容器中添加自定义配置
	 * <p>Title: doListen</p>   
	 * <p>Description: </p>   
	 * @param event   
	 */
	@Override
	protected void doListen(ContextRefreshedEvent event) {
		MessageContext result = event.getApplicationContext().getBean("resultMessageContext", MessageContext.class);
		logger.debug("[<============初始化子系统结果信息开始============>]");
		result.addMessageResource(resultMessagePath);
		logger.debug("[<============初始化子系统结果信息结束============>]");
	}

	public String getResultMessagePath() {
		return resultMessagePath;
	}

	public void setResultMessagePath(String resultMessagePath) {
		this.resultMessagePath = resultMessagePath;
	}
}
