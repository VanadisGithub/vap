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

    public static String doPost(String url, String entityStr, Map<String, Object> headerMap) {
        try {
            HttpPost post = new HttpPost(url);

            if (!StringUtils.isNullOrEmpty(entityStr)) {
                StringEntity entity = new StringEntity(entityStr, Charset.forName("utf-8"));
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }

            for (String key : headerMap.keySet()) {
                post.setHeader(key, String.valueOf(headerMap.get(key)));
            }

            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();

            log.info("PostUrl:" + url + " " + statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                return resultStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String doPost() throws IOException {

        int timeout = 30000;

        String url = "http://www.baidu.com";

        HttpHost proxy = new HttpHost("61.155.164.110", 3128, "http");

        //访问目标地址
        HttpGet get = new HttpGet(url);

        RequestConfig requestConfig = RequestConfig.custom().setMaxRedirects(20).setCircularRedirectsAllowed(true).setConnectTimeout(timeout)
                .setSocketTimeout(timeout).setProxy(proxy).build();
        get.setConfig(requestConfig);

        HttpClient httpClient = HttpClients.createDefault();
        HttpResponse response = httpClient.execute(get);

        int statusCode = response.getStatusLine().getStatusCode();

        log.info("GetUrl:" + url + " " + statusCode);

        if (statusCode == HttpStatus.SC_OK) {
            String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
            return resultStr;
        }
        return null;
    }

    public static String doGet(String url, Map<String, Object> headerMap) {
        try {
            HttpGet get = new HttpGet(url);

            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    get.setHeader(key, String.valueOf(headerMap.get(key)));
                }
            }

            HttpClient httpClient = HttpClients.createDefault();
            HttpResponse response = httpClient.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();

            log.info("GetUrl:" + url + " " + statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                return resultStr;
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
        }
        return null;
    }
}
