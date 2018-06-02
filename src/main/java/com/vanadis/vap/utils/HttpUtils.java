package com.vanadis.vap.utils;

import com.mysql.jdbc.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

public class HttpUtils {

    private static Logger log = LoggerFactory.getLogger(HttpUtils.class);

    public static String UserAgent = "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Maxthon/4.9.2.1000 Chrome/39.0.2146.0 Safari/537.36";

    private static int timeout = 30000;

    private static RequestConfig baseRequestConfig = RequestConfig.custom()
            .setMaxRedirects(20)
            .setCircularRedirectsAllowed(true)
            .setConnectTimeout(timeout)
            .setSocketTimeout(timeout)
            .build();

    public static String doPost(String url, String dataStr, Map<String, Object> headerMap, HttpHost proxy) {
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost post = new HttpPost(url);

            if (!StringUtils.isNullOrEmpty(dataStr)) {
                StringEntity entity = new StringEntity(dataStr, Charset.forName("utf-8"));
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

    public static Map<String, Object> testPost(String url, String dataStr, Map<String, Object> headerMap) {
        Map<String, Object> result = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();

        try {
            HttpPost post = new HttpPost(url);

            if (!StringUtils.isNullOrEmpty(dataStr)) {
                StringEntity entity = new StringEntity(dataStr, Charset.forName("utf-8"));
                entity.setContentEncoding("utf-8");
                entity.setContentType("application/json");
                post.setEntity(entity);
            }

            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    post.setHeader(key, String.valueOf(headerMap.get(key)));
                }
            }

            HttpResponse response = httpClient.execute(post);

            int statusCode = response.getStatusLine().getStatusCode();

            result.put("status", statusCode);

            log.info("testPost:" + url + " " + statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                result.put("result", resultStr);
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
        return result;
    }

    /**
     * @param url
     * @param headerMap
     * @param proxy
     * @return
     */
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
        } finally {
            try {
                httpClient.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static String visitGet(String url, Map<String, Object> headerMap, HttpHost proxy) throws IOException {
        CloseableHttpClient httpClient = HttpClients.createDefault();
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

        log.info("visitGet:" + url + " " + statusCode);

        if (statusCode == HttpStatus.SC_OK) {
            String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
            return resultStr;
        }
        return null;
    }

    public static Map<String, Object> postmanGet(String url, Map<String, Object> headerMap) {
        Map<String, Object> result = new HashMap<>();
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);

            if (headerMap != null) {
                for (String key : headerMap.keySet()) {
                    get.setHeader(key, String.valueOf(headerMap.get(key)));
                }
            }

            HttpResponse response = httpClient.execute(get);

            int statusCode = response.getStatusLine().getStatusCode();

            log.info("postmanGet:" + url + " " + statusCode);

            result.put("status", statusCode);

            if (statusCode == HttpStatus.SC_OK) {
                String resultStr = EntityUtils.toString(response.getEntity(), "utf-8");
                result.put("result", resultStr);
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
        return result;
    }

    //获取文件流
    public static InputStream getInputStream(String urlStr) {
        InputStream inputStream = null;
        HttpURLConnection httpURLConnection = null;
        try {
            URL url = new URL(urlStr);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            // 设置网络连接超时时间
            httpURLConnection.setConnectTimeout(3000);
            // 设置应用程序要从网络连接读取数据
            httpURLConnection.setDoInput(true);
            httpURLConnection.setRequestMethod("GET");
            int responseCode = httpURLConnection.getResponseCode();
            log.info("getInputStream:" + url + " " + responseCode);
            if (responseCode == 200) {
                inputStream = httpURLConnection.getInputStream();
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return inputStream;
    }

    //保存文件
    public static String saveFile(String url, String path) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);
            HttpResponse response = httpClient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("saveFile:" + url + " " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                OutputStream out = new FileOutputStream(path);
                byte[] bytes = EntityUtils.toByteArray(entity);
                out.write(bytes);
                out.flush();
                out.close();
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
        return path;
    }

    //保存文件
    public static String savePng(String url, String path) {
        CloseableHttpClient httpClient = HttpClients.createDefault();
        try {
            HttpGet get = new HttpGet(url);
            get.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");
            get.setHeader("Accept-Encoding", "gzip, deflate");
            get.setHeader("Accept-Language", "zh-CN,zh;q=0.9");
            get.setHeader("Cache-Control", "max-age=0");
            get.setHeader("Connection", "keep-alive");
            get.setHeader("Cookie", "AJSTAT_ok_times=2; acw_tc=AQAAANORPxwNGAsA6xsKcP///41yto8u; ASPSESSIONIDAATABACT=CLEDKBNDJOIBIOLLCIIKKLLJ; __51cke__=; Hm_lvt_8fd158bb3e69c43ab5dd05882cf0b234=1522511191; ASPSESSIONIDAATCCDBS=KLPPKINDIDKECHAJPJGOHMAK; ASPSESSIONIDCCQBCCBS=JENHLPNDDFBJGGPHBJKCCJFG; ASPSESSIONIDAARBABDS=AOKLLGODNDEDEHIIIAEHDBGG; acw_sc__=5abfd0bdf986ed4a688171a3901e03c7b20eb77b; __tins__16949115=%7B%22sid%22%3A%201522519656345%2C%20%22vd%22%3A%204%2C%20%22expires%22%3A%201522522064477%7D; __51laig__=10; Hm_lpvt_8fd158bb3e69c43ab5dd05882cf0b234=1522520265");
            get.setHeader("Host", "ip.zdaye.com");
            get.setHeader("Referer", "http://ip.zdaye.com/");
            get.setHeader("Upgrade-Insecure-Requests", "1");
            get.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/65.0.3325.181 Safari/537.36");
            HttpResponse response = httpClient.execute(get);
            int statusCode = response.getStatusLine().getStatusCode();
            log.info("saveFile:" + url + " " + statusCode);
            if (statusCode == HttpStatus.SC_OK) {
                HttpEntity entity = response.getEntity();
                OutputStream out = new FileOutputStream(path);
                byte[] bytes = EntityUtils.toByteArray(entity);
                out.write(bytes);
                out.flush();
                out.close();
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
        return path;
    }
}
