package com.vanadis.until;

import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;

public class ProxyUtils {

    public static final Logger LOGGER = LoggerFactory.getLogger(ProxyUtils.class);

    public static List<HttpHost> Proxys = new ArrayList<HttpHost>();
    public static int size = 0;
    static Random rand = new Random();

    static {
        Proxys.add(new HttpHost("175.146.97.91", 8080));
        Proxys.add(new HttpHost("113.89.55.147", 9999));
        Proxys.add(new HttpHost("183.184.192.228", 9797));
        Proxys.add(new HttpHost("123.207.25.143", 3128));
        Proxys.add(new HttpHost("114.234.99.69", 8118));
        Proxys.add(new HttpHost("163.125.99.84", 9797));
        Proxys.add(new HttpHost("61.166.251.84", 9999));
        Proxys.add(new HttpHost("60.205.125.201", 8888));
        Proxys.add(new HttpHost("58.220.95.107", 8080));
        Proxys.add(new HttpHost("117.117.196.9", 80));
        Proxys.add(new HttpHost("219.223.251.173", 3128));
        Proxys.add(new HttpHost("114.228.208.177", 8118));
        Proxys.add(new HttpHost("163.125.99.84", 9797));
        Proxys.add(new HttpHost("61.166.251.84", 9999));
        Proxys.add(new HttpHost("60.205.125.201", 8888));
        Proxys.add(new HttpHost("58.220.95.107", 8080));
        Proxys.add(new HttpHost("117.117.196.9", 80));
        Proxys.add(new HttpHost("219.223.251.173", 3128));
        Proxys.add(new HttpHost("114.228.208.177", 8118));
        Proxys.add(new HttpHost("113.128.29.35", 49284));
        Proxys.add(new HttpHost("27.213.107.44", 8118));
        Proxys.add(new HttpHost("222.64.105.221", 9000));
        Proxys.add(new HttpHost("120.15.159.105", 9000));
        Proxys.add(new HttpHost("14.221.166.106", 9000));
        Proxys.add(new HttpHost("219.137.72.197", 53281));
        Proxys.add(new HttpHost("125.122.119.211", 8118));
        Proxys.add(new HttpHost("183.30.197.214", 9797));
        Proxys.add(new HttpHost("14.117.209.42", 9797));
        Proxys.add(new HttpHost("14.20.182.197", 8118));
        Proxys.add(new HttpHost("183.189.232.40", 80));
        Proxys.add(new HttpHost("114.243.44.138", 9000));
        Proxys.add(new HttpHost("125.126.166.70", 40808));
        Proxys.add(new HttpHost("120.78.78.141", 8888));
        Proxys.add(new HttpHost("27.40.153.26", 61234));
        Proxys.add(new HttpHost("49.87.75.58", 44806));
        Proxys.add(new HttpHost("114.230.123.85", 47681));
        Proxys.add(new HttpHost("114.231.67.226", 29039));
        Proxys.add(new HttpHost("218.20.55.198", 9999));
        size = Proxys.size();
    }

    public static HttpHost getProxy() {
        return Proxys.get(rand.nextInt(size));
    }

    public static class DoGetWithProxy implements Callable<String> {

        private String url;
        private HttpHost proxy;

        public DoGetWithProxy(String url, HttpHost proxy) {
            this.url = url;
            this.proxy = proxy;
        }

        @Override
        public String call() {
            String resultStr = HttpUtil.doGet(url, null, proxy);
            return resultStr;
        }
    }

    public static class TestProxy implements Callable<HttpHost> {

        private String url;
        private HttpHost proxy;

        public TestProxy(String url, HttpHost proxy) {
            this.url = url;
            this.proxy = proxy;
        }

        @Override
        public HttpHost call() {
            String resultStr = HttpUtil.doGet(url, null, proxy);
            if (resultStr != null) {
                return proxy;
            }
            return null;
        }
    }

}
