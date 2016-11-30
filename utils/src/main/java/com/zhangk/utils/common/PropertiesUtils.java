package com.zhangk.utils.common;

import com.zhangk.utils.constant.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.util.*;

/**
 * Created by zhangkui on 16/5/21.
 */
public class PropertiesUtils extends Properties{
    Logger logger = LoggerFactory.getLogger(getClass());
    private Map<String,String> properties = new HashMap<>();
    private List<String> urls = new ArrayList<>();
    public PropertiesUtils() {}

    public String get(String key){
        return properties.get(key);
    }
    public String get(Enum key){
        return properties.get(key.name());
    }

    public List<String> getUrls() {
        return urls;
    }

    public void setUrls(List<String> urls) throws Exception{
        ClassLoader currentThreadClassLoader = Thread.currentThread().getContextClassLoader();
        for (String url : urls){
            logger.debug("[<=配置文件:{}=>]",url.substring(url.lastIndexOf(Constants.Separator.SLASH)+1,url.length()));
            InputStream inputStream  = currentThreadClassLoader.getResourceAsStream(url);
            if(inputStream == null)
                inputStream = getClass().getResourceAsStream(url);
            load(inputStream);
            Enumeration names = propertyNames();
            while (names.hasMoreElements()){
                String name;
                properties.put(name = ((String) names.nextElement()),getProperty(name));
                logger.debug("[{}:{}]",name,getProperty(name));
            }
        }
        this.urls = urls;
    }
}
