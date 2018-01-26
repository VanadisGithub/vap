package com.vanadis.vap.until;

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

public class ProxyUtils {

    public static final Logger log = LoggerFactory.getLogger(ProxyUtils.class);

    private static List<HttpHost> Proxys = new ArrayList<HttpHost>();
    private static int size;
    private static Random rand = new Random();

    static {
        Proxys.add(new HttpHost("113.99.218.102", 9797));
        Proxys.add(new HttpHost("122.72.18.35", 80));
        Proxys.add(new HttpHost("14.153.52.28", 3128));
        Proxys.add(new HttpHost("115.239.73.26", 3128));
        Proxys.add(new HttpHost("219.223.251.173", 3128));
        Proxys.add(new HttpHost("112.112.210.176", 9999));
        Proxys.add(new HttpHost("121.199.42.198", 3129));
        Proxys.add(new HttpHost("139.224.80.139", 3128));
        Proxys.add(new HttpHost("122.72.18.34", 80));
        Proxys.add(new HttpHost("60.205.125.201", 8888));
        Proxys.add(new HttpHost("211.159.177.212", 3128));
        Proxys.add(new HttpHost("58.220.95.107", 8080));
        Proxys.add(new HttpHost("110.250.15.174", 8118));
        Proxys.add(new HttpHost("112.250.65.222", 53281));
        Proxys.add(new HttpHost("122.136.212.132", 53281));
        Proxys.add(new HttpHost("183.30.197.51", 9797));
        Proxys.add(new HttpHost("123.14.229.74", 8080));
        Proxys.add(new HttpHost("182.38.140.196", 8118));
        Proxys.add(new HttpHost("182.244.204.10", 8118));
        Proxys.add(new HttpHost("182.244.207.139", 8118));
        Proxys.add(new HttpHost("117.36.103.170", 8118));
        Proxys.add(new HttpHost("112.238.242.235", 8118));
        Proxys.add(new HttpHost("182.126.179.12", 8118));
        Proxys.add(new HttpHost("182.35.144.226", 8118));
        Proxys.add(new HttpHost("182.244.206.21", 8118));
        Proxys.add(new HttpHost("123.114.39.183", 8118));
        Proxys.add(new HttpHost("182.244.205.30", 8118));
        Proxys.add(new HttpHost("27.200.144.114", 8118));
        Proxys.add(new HttpHost("123.207.25.143", 3128));
        Proxys.add(new HttpHost("163.125.73.107", 9797));
        Proxys.add(new HttpHost("182.244.205.229", 8118));
        Proxys.add(new HttpHost("182.244.205.80", 8118));
        Proxys.add(new HttpHost("182.244.206.76", 8118));
        Proxys.add(new HttpHost("122.138.170.202", 808));
        Proxys.add(new HttpHost("113.124.222.11", 41877));
        Proxys.add(new HttpHost("49.81.123.140", 8118));
        Proxys.add(new HttpHost("117.23.47.196", 808));
        Proxys.add(new HttpHost("180.118.240.141", 61234));
        Proxys.add(new HttpHost("182.244.206.160", 8118));
        Proxys.add(new HttpHost("115.217.255.108", 43667));
        Proxys.add(new HttpHost("182.244.205.202", 8118));
        Proxys.add(new HttpHost("182.244.207.3", 8118));
        Proxys.add(new HttpHost("219.138.58.194", 3128));
        Proxys.add(new HttpHost("182.244.205.60", 8118));
        Proxys.add(new HttpHost("182.244.206.19", 8118));
        Proxys.add(new HttpHost("182.244.206.207", 8118));
        Proxys.add(new HttpHost("182.244.207.191", 8118));
        Proxys.add(new HttpHost("114.252.231.54", 9000));
        Proxys.add(new HttpHost("118.190.19.135", 8118));
        Proxys.add(new HttpHost("61.144.100.13", 9797));
        Proxys.add(new HttpHost("175.16.223.136", 80));
        Proxys.add(new HttpHost("121.31.149.154", 8123));
        Proxys.add(new HttpHost("58.19.12.246", 808));
        Proxys.add(new HttpHost("119.96.191.136", 8118));
        Proxys.add(new HttpHost("222.65.234.67", 8118));
        Proxys.add(new HttpHost("113.88.179.92", 8088));
        Proxys.add(new HttpHost("180.122.150.99", 47022));
        Proxys.add(new HttpHost("121.31.148.251", 8123));
        Proxys.add(new HttpHost("171.38.85.0", 8123));
        Proxys.add(new HttpHost("222.222.169.60", 53281));
        Proxys.add(new HttpHost("27.225.184.109", 8118));
        Proxys.add(new HttpHost("113.65.10.119", 9797));
        Proxys.add(new HttpHost("123.139.56.238", 9999));
        Proxys.add(new HttpHost("112.81.30.60", 8118));
        Proxys.add(new HttpHost("223.199.152.186", 9797));
        Proxys.add(new HttpHost("122.241.221.111", 30378));
        Proxys.add(new HttpHost("218.73.138.11", 36444));
        Proxys.add(new HttpHost("120.78.78.141", 8888));
        Proxys.add(new HttpHost("144.255.13.210", 30586));
        Proxys.add(new HttpHost("116.52.198.58", 8118));
        Proxys.add(new HttpHost("180.212.140.229", 8118));
        Proxys.add(new HttpHost("27.46.48.135", 9797));
        Proxys.add(new HttpHost("117.117.196.9", 80));
        Proxys.add(new HttpHost("119.96.116.47", 8118));
        Proxys.add(new HttpHost("14.211.123.221", 9797));
        Proxys.add(new HttpHost("121.31.159.11", 8123));
        Proxys.add(new HttpHost("202.113.76.200", 1080));
        Proxys.add(new HttpHost("121.205.176.54", 24389));
        Proxys.add(new HttpHost("121.31.152.219", 8123));
        Proxys.add(new HttpHost("112.114.79.16", 8118));
        Proxys.add(new HttpHost("58.49.233.156", 8081));
        Proxys.add(new HttpHost("125.45.129.22", 8118));
        Proxys.add(new HttpHost("223.203.192.142", 8088));
        Proxys.add(new HttpHost("115.230.60.235", 1080));
        Proxys.add(new HttpHost("27.40.150.62", 61234));
        Proxys.add(new HttpHost("175.148.62.32", 80));
        Proxys.add(new HttpHost("140.143.96.216", 80));
        Proxys.add(new HttpHost("223.241.79.106", 8010));
        Proxys.add(new HttpHost("27.37.242.8", 9797));
        Proxys.add(new HttpHost("116.27.244.134", 47018));
        Proxys.add(new HttpHost("219.138.58.216", 3128));
        Proxys.add(new HttpHost("58.49.232.114", 8081));
        Proxys.add(new HttpHost("27.190.26.57", 8118));
        Proxys.add(new HttpHost("27.40.151.195", 61234));
        Proxys.add(new HttpHost("121.31.155.34", 8123));
        Proxys.add(new HttpHost("221.215.169.40", 8118));
        Proxys.add(new HttpHost("223.241.119.196", 8010));
        Proxys.add(new HttpHost("140.224.98.41", 31982));
        Proxys.add(new HttpHost("218.67.158.103", 9999));
        Proxys.add(new HttpHost("113.108.253.195", 9797));

        Proxys.add(new HttpHost("111.225.9.158", 9999));
        Proxys.add(new HttpHost("113.89.53.140", 9999));
        Proxys.add(new HttpHost("222.195.92.76", 3128));
        Proxys.add(new HttpHost("61.155.164.110", 3128));
        Proxys.add(new HttpHost("221.200.117.65", 8118));
        Proxys.add(new HttpHost("61.155.164.111", 3128));
        Proxys.add(new HttpHost("180.173.148.255", 9797));
        Proxys.add(new HttpHost("61.135.217.7", 80));
        Proxys.add(new HttpHost("115.29.236.46", 3128));
        Proxys.add(new HttpHost("122.114.31.177", 808));
        Proxys.add(new HttpHost("14.117.177.103", 808));
        Proxys.add(new HttpHost("182.244.207.91", 8118));
        Proxys.add(new HttpHost("203.174.112.13", 3128));
        Proxys.add(new HttpHost("61.155.164.107", 3128));
        Proxys.add(new HttpHost("123.163.146.86", 808));
        Proxys.add(new HttpHost("221.217.54.249", 9000));
        Proxys.add(new HttpHost("139.129.166.68", 3128));
        Proxys.add(new HttpHost("180.121.129.68", 808));
        Proxys.add(new HttpHost("180.173.84.248", 8118));
        Proxys.add(new HttpHost("115.46.77.43", 8123));
        Proxys.add(new HttpHost("182.244.205.167", 8118));
        Proxys.add(new HttpHost("125.107.187.44", 8118));
        Proxys.add(new HttpHost("1.196.161.162", 9999));
        Proxys.add(new HttpHost("125.115.131.215", 8118));
        Proxys.add(new HttpHost("182.244.204.213", 8118));
        Proxys.add(new HttpHost("182.244.206.124", 8118));
        Proxys.add(new HttpHost("218.17.197.114", 10800));
        Proxys.add(new HttpHost("119.122.40.139", 9000));
        Proxys.add(new HttpHost("110.73.42.43", 8123));
        Proxys.add(new HttpHost("110.88.1.222", 8118));
        Proxys.add(new HttpHost("112.232.32.180", 8118));
        Proxys.add(new HttpHost("182.244.204.195", 8118));
        Proxys.add(new HttpHost("182.244.205.54", 8118));
        Proxys.add(new HttpHost("118.81.71.180", 9797));
        Proxys.add(new HttpHost("1.25.17.28", 8118));
        Proxys.add(new HttpHost("60.174.74.40", 8118));
        Proxys.add(new HttpHost("27.200.199.251", 8118));
        Proxys.add(new HttpHost("110.73.1.204", 8123));
        Proxys.add(new HttpHost("115.213.176.194", 808));
        Proxys.add(new HttpHost("121.207.93.172", 8118));
        Proxys.add(new HttpHost("182.244.206.190", 8118));
        Proxys.add(new HttpHost("106.81.228.197", 8118));
        Proxys.add(new HttpHost("114.234.83.154", 36249));
        Proxys.add(new HttpHost("60.184.206.107", 41371));
        Proxys.add(new HttpHost("101.31.146.3", 80));
        Proxys.add(new HttpHost("112.114.76.34", 8118));
        Proxys.add(new HttpHost("144.255.13.210", 30586));
        Proxys.add(new HttpHost("122.241.221.111", 30378));
        Proxys.add(new HttpHost("111.155.124.84", 8123));
        Proxys.add(new HttpHost("218.72.108.135", 808));
        Proxys.add(new HttpHost("222.74.225.231", 3128));
        Proxys.add(new HttpHost("42.56.62.79", 80));
        Proxys.add(new HttpHost("114.243.235.224", 1080));
        Proxys.add(new HttpHost("122.225.17.123", 8080));
        Proxys.add(new HttpHost("125.81.79.192", 8123));
        Proxys.add(new HttpHost("115.183.11.158", 9999));
        Proxys.add(new HttpHost("171.113.158.46", 808));
        Proxys.add(new HttpHost("222.133.160.222", 8118));
        Proxys.add(new HttpHost("218.13.11.186", 8118));
        Proxys.add(new HttpHost("171.116.155.92", 9797));
        Proxys.add(new HttpHost("175.17.156.25", 8080));
        Proxys.add(new HttpHost("118.254.147.115", 3128));
        Proxys.add(new HttpHost("119.164.31.196", 8118));
        Proxys.add(new HttpHost("180.122.149.67", 23027));
        Proxys.add(new HttpHost("117.60.179.157", 36674));
        Proxys.add(new HttpHost("113.79.74.24", 808));
        Proxys.add(new HttpHost("121.31.146.164", 8123));
        Proxys.add(new HttpHost("121.31.199.84", 8123));
        Proxys.add(new HttpHost("36.101.35.27", 8118));
        Proxys.add(new HttpHost("115.46.99.230", 8123));
        Proxys.add(new HttpHost("113.106.195.98", 8088));
        Proxys.add(new HttpHost("110.72.43.87", 8123));
        Proxys.add(new HttpHost("110.73.6.97", 8123));
        Proxys.add(new HttpHost("125.37.169.16", 8118));
        Proxys.add(new HttpHost("171.38.24.68", 8123));
        Proxys.add(new HttpHost("115.202.238.148", 39008));
        Proxys.add(new HttpHost("59.50.68.34", 53281));
        Proxys.add(new HttpHost("112.114.78.155", 8118));
        Proxys.add(new HttpHost("123.177.20.80", 8118));
        Proxys.add(new HttpHost("27.193.4.54", 8118));
        Proxys.add(new HttpHost("14.20.235.101", 9797));
        Proxys.add(new HttpHost("223.241.116.162", 8010));
        Proxys.add(new HttpHost("182.244.206.41", 8118));
        Proxys.add(new HttpHost("182.91.104.87", 9999));
        Proxys.add(new HttpHost("110.73.7.253", 8123));
        Proxys.add(new HttpHost("27.40.141.176", 61234));
        Proxys.add(new HttpHost("183.48.90.235", 8118));
        Proxys.add(new HttpHost("218.20.54.125", 9797));
        Proxys.add(new HttpHost("115.171.69.135", 8118));
        Proxys.add(new HttpHost("220.189.252.194", 3128));
        Proxys.add(new HttpHost("113.246.132.72", 8118));
        Proxys.add(new HttpHost("112.251.1.179", 8118));
        Proxys.add(new HttpHost("58.254.61.158", 8118));
        Proxys.add(new HttpHost("121.31.152.168", 8123));
        Proxys.add(new HttpHost("110.73.51.202", 8123));
        Proxys.add(new HttpHost("116.192.59.107", 1080));
        Proxys.add(new HttpHost("122.235.232.149", 8118));
        Proxys.add(new HttpHost("115.203.217.209", 42818));
        Proxys.add(new HttpHost("182.88.89.34", 8123));
        Proxys.add(new HttpHost("114.99.93.40", 39905));


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
            String resultStr = HttpUtil.doGet(url, null, proxy);
            if (resultStr != null) {
                return proxy;
            }
            return null;
        }
    }

    public static List<HttpHost> getXiCiProxys() {

        String[] name = {"nt", "nn", "wt", "wn"};
        List<HttpHost> proxys = new ArrayList<>();

        for (int index = 0; index < name.length; index++) {

            for (int page = 1; page <= 10; page++) {
                String url = "http://www.xicidaili.com/" + name[index] + "/";
                if (page > 1) {
                    url += page;
                }

                String html = HttpUtil.doGet(url, null, getProxy());
                Document doc = Jsoup.parse(html);
                Elements trs = doc.select("tr");

                for (int i = 1; i < trs.size(); i++) {
                    Elements tds = trs.get(i).select("td");
                    String ip = tds.get(1).html();
                    String port = tds.get(2).html();
                    HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
                    proxys.add(proxy);
                }
            }
        }
        return proxys;
    }

}
