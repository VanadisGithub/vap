package com.vanadis.vap.utils;

import com.mysql.jdbc.StringUtils;
import com.vanadis.vap.model.Proxy;
import com.vanadis.vap.model.ProxyMapper;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ProxyUtils {

    public static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);

    private static List<HttpHost> Proxys = new ArrayList<HttpHost>() {
        {
            add(new HttpHost("211.159.177.212", 3128));
            add(new HttpHost("219.135.164.245", 3128));
            add(new HttpHost("124.193.37.5", 8888));
            add(new HttpHost("120.78.182.79", 3128));
            add(new HttpHost("183.56.177.130", 808));
            add(new HttpHost("203.174.112.13", 3128));
            add(new HttpHost("120.78.78.141", 8888));
            add(new HttpHost("59.44.16.6", 80));
            add(new HttpHost("61.134.25.106", 3128));
            add(new HttpHost("121.206.245.247", 53281));
            add(new HttpHost("221.7.255.168", 8080));
            add(new HttpHost("222.186.45.65", 56721));
            add(new HttpHost("139.198.12.227", 48888));
            add(new HttpHost("14.29.47.90", 3128));
            add(new HttpHost("116.226.66.17", 9000));
            add(new HttpHost("119.28.138.104", 3128));
            add(new HttpHost("14.118.255.8", 6666));
            add(new HttpHost("175.163.66.136", 8118));
            add(new HttpHost("120.92.88.202", 10000));
            add(new HttpHost("183.56.177.130", 808));
            add(new HttpHost("203.174.112.13", 3128));
            add(new HttpHost("14.118.255.8", 6666));
            add(new HttpHost("58.216.202.149", 8118));
            add(new HttpHost("114.214.164.38", 9999));
            add(new HttpHost("221.7.255.168", 8080));
            add(new HttpHost("211.159.177.212", 3128));
            add(new HttpHost("119.28.138.104", 3128));
            add(new HttpHost("219.135.164.245", 3128));
            add(new HttpHost("124.193.37.5", 8888));
            add(new HttpHost("120.78.182.79", 3128));
            add(new HttpHost("120.78.78.141", 8888));
            add(new HttpHost("175.163.66.136", 8118));
            add(new HttpHost("120.92.88.202", 10000));
            add(new HttpHost("59.44.16.6", 80));
            add(new HttpHost("58.19.13.49", 18118));
        }
    };
    private static int size = Proxys.size();
    private static Random rand = new Random();

    //随机获取代理
    public static HttpHost getProxy() {
        return Proxys.get(rand.nextInt(size));
    }

    //访问链接
    public static void doGetWithProxyList(String url, List<Proxy> list, long delayTime, ProxyMapper proxyMapper) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        for (Proxy xici : list) {
            HttpHost proxy = new HttpHost(xici.getIp(), Integer.parseInt(xici.getPort()));
            executorService.submit(new ProxyUtils.DoGetWithProxy(url, proxy, proxyMapper));
            try {
                Thread.sleep(delayTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    //保存xici代理
    public static void saveProxyXici(ProxyMapper proxyMapper) {
        int successNum = 0;
        String[] name = {"nt", "nn", "wt", "wn"};
        for (int index = 0; index < name.length; index++) {
            for (int page = 1; page <= 3; page++) {
                String url = "http://www.xicidaili.com/" + name[index] + "/";
                if (page > 1) {
                    url += page;
                }
                String html = HttpUtils.doGet(url, null, null);
                Document doc = Jsoup.parse(html);
                Elements trs = doc.select("tr");
                for (int i = 1; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("td");
                    String ip = tds.get(1).html().trim();
                    String port = tds.get(2).html().trim();
                    Proxy proxy = new Proxy(ip, port, 0, 0, 0);
                    if (proxyMapper.isExcited(proxy.getIp()) == 0) {
                        if (proxyMapper.insert(proxy)) {
                            successNum++;
                        }
                    }
                }
                try {
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    //保存xici代理
    public static void saveProxyKuai(ProxyMapper proxyMapper) {

    }

    //Callable：用代理访问
    public static class DoGetWithProxy implements Callable<String> {

        private String url;
        private HttpHost proxy;
        private ProxyMapper proxyMapper;

        public DoGetWithProxy(String url, HttpHost proxy, ProxyMapper proxyMapper) {
            this.url = url;
            this.proxy = proxy;
            this.proxyMapper = proxyMapper;
        }

        @Override
        public String call() {
            String resultStr = null;
            try {
                resultStr = HttpUtils.visitGet(url, null, proxy);
            } catch (IOException e) {
                proxyMapper.addErrorNum(proxy.getHostName(), System.currentTimeMillis());
            }
            if (StringUtils.isNullOrEmpty(resultStr)) {
                proxyMapper.addErrorNum(proxy.getHostName(), System.currentTimeMillis());
            } else {
                if (proxyMapper.isExcited(proxy.getHostName()) == 1) {
                    proxyMapper.subErrorNum(proxy.getHostName(), System.currentTimeMillis());
                } else {
                    Proxy newProxy = new Proxy(proxy.getHostName(), String.valueOf(proxy.getPort()), 2, 0, 0);
                    proxyMapper.insert(newProxy);
                }
            }
            return resultStr;
        }
    }

}
