package com.vanadis.controller;

import com.vanadis.until.HttpUtil;
import com.vanadis.until.ProxyUtils;
import org.apache.http.HttpHost;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

@RestController
@RequestMapping("proxy")
public class ProxyController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/proxy");
        return modelAndView;
    }

    @RequestMapping("visitByProxy")
    public int visitByProxy(String url) {

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<String>> resultList = new ArrayList<>();

        List<HttpHost> proxys = ProxyUtils.getXiCiProxys();
        for (HttpHost proxy : proxys) {
            executorService.submit(new DoGetWithProxy(url, proxy));
        }
        int num = 0;
        for (Future<String> fs : resultList) {
            try {
                while (!fs.isDone()) ;
                if (fs.get() != null) {
                    num++;
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                //启动一次顺序关闭，执行以前提交的任务，但不接受新任务
                executorService.shutdown();
            }
        }
        return num;
    }

    @RequestMapping("htmlToProxy")
    public Map htmlToProxy(String html) {

        Map<String, String> result = new HashMap<>();
        StringBuffer resultStr = new StringBuffer();

        Document doc = Jsoup.parse(html);
        Elements trs = doc.select("tr");

        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
            resultStr.append("Proxys.add(new HttpHost(\"" + proxy.getHostName() + "\", " + proxy.getPort() + "));\n");
        }
        result.put("result", resultStr.toString());
        return result;
    }

    @RequestMapping("test")
    public String test() {
        return null;
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

}
