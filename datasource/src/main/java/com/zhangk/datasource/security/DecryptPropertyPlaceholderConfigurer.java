package com.zhangk.datasource.security;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PropertyPlaceholderConfigurer;

import java.util.Properties;

/**
 * Created by zhangbin on 16/6/4.
 */
public class DecryptPropertyPlaceholderConfigurer extends PropertyPlaceholderConfigurer {


    private static String keyPath;


    private String[] encryptPropNames;


    public void setEncryptPropNames(String[] encryptPropNames) {
        this.encryptPropNames = encryptPropNames;
    }

    public static void setKeyPath(String keyPath) {
        DecryptPropertyPlaceholderConfigurer.keyPath = keyPath;
    }


    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactoryToProcess, Properties props) throws BeansException {

        if (encryptPropNames != null && encryptPropNames.length > 0 && props.size() > 0) {
            for (String propName : encryptPropNames) {
                String value = props.getProperty(propName);
                System.out.println( DESEncryptUtil.doEncrypt(value, keyPath));
                props.setProperty(propName, DESEncryptUtil.doEncrypt(value, keyPath));
            }
        }

        super.processProperties(beanFactoryToProcess, props);
    }

}
