package com.vanadis.vap.utils;

import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ProxyUtils {

    public static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);

    private static List<HttpHost> Proxys = new ArrayList<HttpHost>();
    private static int size;
    private static Random rand = new Random();

    static {
        Proxys.add(new HttpHost("113.99.218.102", 9797));
        size = Proxys.size();
    }

    public static HttpHost getProxy() {
        return Proxys.get(rand.nextInt(size));
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
            String resultStr = HttpUtils.doGet(url, null, proxy);
            if (resultStr != null) {
                return proxy;
            }
            return null;
        }
    }

    public static List<HttpHost> getXiCiProxys(String visitUrl) {

        String[] name = {"nt", "nn", "wt", "wn"};
        List<HttpHost> proxys = new ArrayList<>();
        List<HttpHost> firstProxys = getXiCiProxysFirst();

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> resultList = new ArrayList<>();
        for (int index = 0; index < name.length; index++) {
            for (int page = 1; page <= 10; page++) {
                String url = "http://www.xicidaili.com/" + name[index] + "/";
                if (page > 1) {
                    url += page;
                }
                Future<String> future = executorService.submit(new DoXiCi(url, firstProxys.get(rand.nextInt(100)), visitUrl));
            }
        }
        return proxys;
    }

    public static List<HttpHost> getXiCiProxysFirst() {
        String[] name = {"nt", "nn", "wt", "wn"};
        List<HttpHost> proxys = new ArrayList<>();
        String url = "http://www.xicidaili.com/" + name[rand.nextInt(name.length)] + "/";
        String html = HttpUtils.doGet(url, null, null);
        Document doc = Jsoup.parse(html);
        Elements trs = doc.select("tr");
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
            proxys.add(proxy);
        }
        return proxys;
    }

    public static class DoXiCi implements Callable<String> {

        private String url;
        private HttpHost proxy;
        private String visitUrl;

        public DoXiCi(String url, HttpHost proxy, String visitUrl) {
            this.url = url;
            this.proxy = proxy;
            this.visitUrl = visitUrl;
        }

        @Override
        public String call() {
            String html = HttpUtils.doGet(url, null, proxy);
            Document doc = Jsoup.parse(html);
            Elements trs = doc.select("tr");

            ExecutorService executorService = Executors.newCachedThreadPool();
            for (int i = 1; i < trs.size(); i++) {
                Elements tds = trs.get(i).select("td");
                String ip = tds.get(1).html();
                String port = tds.get(2).html();
                HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
                executorService.submit(new DoGetWithProxy(visitUrl, proxy));
            }
            return html;
        }
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
            String resultStr = HttpUtils.doGet(url, null, proxy);
            System.out.println(++num);
            return resultStr;
        }
    }

    private static int num = 1;

}
