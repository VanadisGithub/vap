package com.vanadis.until;

import com.mysql.jdbc.StringUtils;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

public class HttpUtil {

    private static Logger log = Logger.getLogger(HttpUtil.class);

    public static String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.9.2.1000 Chrome/39.0.2146.0 Safari/537.36";

    private static int timeout = 30000;

    private static RequestConfig baseRequestConfig = RequestConfig.custom()
            .setMaxRedirects(20)
            .setCircularRedirectsAllowed(true)
            .setConnectTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();

    public static String doPost(String url, String entityStr, Map<String, Object> headerMap, HttpHost proxy) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost post = new HttpPost(url);

            if (!StringUtils.isNullOrEmpty(entityStr)) {
                StringEntity entity = new StringEntity(entityStr, Charset.forName("utf-8"));
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }

            //默认添加请求头
            post.addHeader("user-agent", UserAgentUtils.getUserAgent());


            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    post.setHeader(key, String.valueOf(headerMap.get(key)));
                }
            }

            if (proxy != null) {
                RequestConfig requestConfig = RequestConfig.copy(baseRequestConfig).setProxy(proxy).build();
                post.setConfig(requestConfig);
            }

            HttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();

            log.info("doPost:" + url + " " + statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                return resultStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String doGet(String url, Map<String, Object> headerMap, HttpHost proxy) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);

            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    get.setHeader(key, String.valueOf(headerMap.get(key)));
                }
            }

            //默认添加请求头
            get.addHeader("user-agent", UserAgentUtils.getUserAgent());

            if (proxy != null) {
                RequestConfig requestConfig = RequestConfig.copy(baseRequestConfig).setProxy(proxy).build();
                get.setConfig(requestConfig);
            }

            HttpResponse response = httpClient.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();

            log.info("doGet:" + url + " " + statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                return resultStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
