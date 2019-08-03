package com.spring.netty.common.filter;

/**
 * @description: 
 * @email:lkw510903926@163.com
 * @author: yangqi 
 * @since: 2019-08-03 10:28:57
*/
public interface Filter{

    int order();

    Object doFilter(Object object);


    String filterType();
}