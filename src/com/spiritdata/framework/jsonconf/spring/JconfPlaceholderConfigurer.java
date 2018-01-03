package com.spiritdata.framework.jsonconf.spring;

import java.util.Properties;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.config.PlaceholderConfigurerSupport;

public class JconfPlaceholderConfigurer extends PlaceholderConfigurerSupport {

    /**
     * Visit each bean definition in the given bean factory and attempt to replace ${...} property
     * placeholders with values from the given properties.
     * 通过beanFactory得到的Bean定义中的所有->..参数都替换为props中给定的值
     */
    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        System.out.println("3232");
    }
}