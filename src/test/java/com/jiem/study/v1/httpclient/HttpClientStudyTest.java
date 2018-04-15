package com.jiem.study.v1.httpclient;

import org.junit.Test;

/**
 * 使用 HttpClient 获取网页内容测试
 * Created by jiem on 2018/4/14.
 */
public class HttpClientStudyTest {

    HttpClientStudy hcs = new HttpClientStudy();

    @Test
    public void testGet() throws Exception {
        String url = "http://www.hao123.com";
        hcs.get(url);
    }

    @Test
    public void testProxyGet() throws Exception {
        String url = "http://www.hao123.com";
        hcs.proxyGet(url);
    }

    @Test
    public void testTargetGet() throws Exception {
        String url = "http://www.baidu.com";
        hcs.targetGet(url);
    }
}
