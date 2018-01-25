package com.vanadis.controller;

import com.vanadis.until.HttpUtil;
import com.vanadis.until.ProxyUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static com.vanadis.until.HttpUtil.UserAgent;

@RestController
@RequestMapping("proxy")
public class ProxyController extends BaseController {

    @RequestMapping("getProxy")
    public String getProxy() throws Exception {

        String result = "";

        String url = "http://www.baidu.com";

        String html = "";

        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("tr");

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();//
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
            results.add(exec.submit(new ProxyUtils.DoGetWithProxy(url, proxy)));//submit返回一个Future，代表了即将要返回的结果
        }
        for (int i = 0; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            if (results.get(i) != null) {
                System.out.println("Proxys.add(new HttpHost(\"" + ip + "\", " + port + "));");
                result += "Proxys.add(new HttpHost(\"" + ip + "\", " + port + "));\n";
            } else {
                System.out.println(ip + "失败");
            }
        }
        return result;
    }

    @RequestMapping("testProxy")
    public String testProxy() throws Exception {

        String result = "";
        List<HttpHost> Proxys = ProxyUtils.Proxys;

        String url = "http://www.baidu.com";

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();//
        for (int i = 0; i < Proxys.size(); i++) {
            results.add(exec.submit(new ProxyUtils.DoGetWithProxy(url, Proxys.get(i))));//submit返回一个Future，代表了即将要返回的结果
        }
        for (int i = 0; i < Proxys.size(); i++) {
            String ip = Proxys.get(i).getHostName();
            int port = Proxys.get(i).getPort();
            if (results.get(i) != null) {
                System.out.println("Proxys.add(new HttpHost(\"" + ip + "\", " + port + "));");
                result += "Proxys.add(new HttpHost(\"" + ip + "\", " + port + "));\n";
            } else {
                System.out.println(ip + "失败");
            }
        }
        return result;
    }

    @RequestMapping("visitCsdn")
    public int visitCsdn() throws Exception {

        String url = "http://blog.csdn.net/vanadis_outlook/article/details/72991117";
        //String url = "http://www.baidu.com";

        ExecutorService exec = Executors.newCachedThreadPool();//工头
        ArrayList<Future<String>> results = new ArrayList<Future<String>>();//
        for (int i = 0; i < 10; i++) {
            results.add(exec.submit(new ProxyUtils.DoGetWithProxy(url, ProxyUtils.getProxy())));//submit返回一个Future，代表了即将要返回的结果
        }
        int i = 0;
        for (Future<String> s : results) {
            if (s.isDone()) {
                if (s.get() != null) {
                    i++;
                }
            }
        }
        return i;
    }

}
