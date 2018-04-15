package com.jiem.study.v1.httpclient;

import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.ExecutionContext;
import org.apache.http.protocol.HttpContext;
import org.apache.http.util.EntityUtils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * 使用 HttpClient 获取网页内容
 * Created by jiem on 2018/4/14.
 */
public class HttpClientStudy {

    public void get(String urlString) {

        try {

            //HttpClient主要负责执行请求
            HttpClient httpClient = new DefaultHttpClient();

            //利用HTTP GET向服务器发起请求
            HttpGet httpGet = new HttpGet(urlString);

            //获得服务器响应的的所有信息
            HttpResponse response = httpClient.execute(httpGet);

            //获得服务器响应回来的消息体（不包括HTTP HEAD）
            HttpEntity entity = response.getEntity();
            if (entity != null) {

                //获得响应的字符集编码信息
                //即获取HTTP HEAD的：Content-Type:text/html;charset=UTF-8中的字符集信息
                String charSet = EntityUtils.getContentCharSet(entity);
                System.out.println("响应的字符集是：" + charSet);

                InputStream inputStream = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                inputStream.close();
            }

            //释放所有的链接资源，一般在所有的请求处理完成之后，才需要释放
            httpClient.getConnectionManager().shutdown();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void proxyGet(String urlString) {

        try {
            //HttpClient主要负责执行请求
            DefaultHttpClient httpClient = new DefaultHttpClient();

            //设置代理服务器
            httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, new HttpHost("121.12.249.207", 3128));

            //利用HTTP GET向服务器发起请求
            HttpGet httpGet = new HttpGet(urlString);

            //获得服务器响应的的所有信息
            HttpResponse response = httpClient.execute(httpGet);

            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream inputStream = entity.getContent();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
                BufferedReader reader = new BufferedReader(inputStreamReader);
                String line;
                while ((line = reader.readLine()) != null) {
                    System.out.println(line);
                }
                inputStream.close();
            }

            //释放所有的链接资源，一般在所有的请求处理完成之后，才需要释放
            httpClient.getConnectionManager().shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void targetGet(String urlString) {

        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();

            HttpGet httpGet = new HttpGet(urlString);
            BasicHttpContext httpContext = new BasicHttpContext();

            HttpResponse execute = httpClient.execute(httpGet, httpContext);

            //获得重定向之后的主机地址信息 v
            HttpHost targetHost = (HttpHost) httpContext.getAttribute(ExecutionContext.HTTP_TARGET_HOST);
            System.out.println(targetHost);

            //获得实际的请求对象的URI（即重定向之后的"/cms/backend/login.jsp"）
            HttpUriRequest actualRequest = (HttpUriRequest) httpContext.getAttribute(ExecutionContext.HTTP_REQUEST);
            System.out.println(actualRequest.getURI());

            //释放所有的链接资源，一般在所有的请求处理完成之后，才需要释放
            httpClient.getConnectionManager().shutdown();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void cookieGet(String urlString, String loginString) {

        try {

            //HttpClient主要负责执行请求
            HttpClient httpclient = new DefaultHttpClient();
            HttpContext context = new BasicHttpContext();

            //利用HTTP GET向服务器发起请求，
            HttpGet get = new HttpGet(urlString);
            //获得服务器响应的的所有信息
            HttpResponse response = httpclient.execute(get, context);

            //获得服务器响应回来的消息体（不包括HTTP HEAD）
            HttpEntity entity = response.getEntity();
            String charset =  null;
            if (entity != null) {
                //获得响应的字符集编码信息
                //即获取HTTP HEAD的：Content-Type:text/html;charset=UTF-8中的字符集信息
                charset = EntityUtils.getContentCharSet(entity);
                System.out.println("响应的字符集是：" + charset);
                InputStream is = entity.getContent();
                //使用响应中的编码来解释响应的内容
                BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                is.close();
            }

            //************* 执行登录请求 ********************//
            HttpPost post = new HttpPost(loginString);

            //添加POST参数
            List<NameValuePair> nvps = new ArrayList<>();
            nvps.add(new BasicNameValuePair("username", "admin"));
            nvps.add(new BasicNameValuePair("password", "admin"));

            post.setEntity(new UrlEncodedFormEntity(nvps, charset));
            response = httpclient.execute(post);
            entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                //使用响应中的编码来解释响应的内容
                BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                is.close();
            }

            //******************* 请求文章查询 ********************//

            get = new HttpGet("http://localhost:8080/cms/backend/ArticleServlet");
            response = httpclient.execute(get);
            entity = response.getEntity();
            if (entity != null) {
                InputStream is = entity.getContent();
                //使用响应中的编码来解释响应的内容
                BufferedReader br = new BufferedReader(new InputStreamReader(is, charset));
                String line = null;
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                is.close();
            }
            //释放所有的链接资源，一般在所有的请求处理完成之后，才需要释放
            httpclient.getConnectionManager().shutdown();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
