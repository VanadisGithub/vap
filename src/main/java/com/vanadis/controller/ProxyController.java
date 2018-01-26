package com.vanadis.controller;

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
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

@RestController
@RequestMapping("proxy")
public class ProxyController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/proxy");
        return modelAndView;
    }

    @RequestMapping("htmlToProxy")
    public Map htmlToProxy(String html) {

        Map<String, String> result = new HashMap<>();

        StringBuffer resultStr = new StringBuffer();

        //String url = "http://blog.csdn.net/vanadis_outlook/article/details/70670699";

        String url = "http://www.baidu.com";

        Document doc = Jsoup.parse(html);

        Elements trs = doc.select("tr");

        ExecutorService executorService = Executors.newCachedThreadPool();
        ArrayList<Future<HttpHost>> resultList = new ArrayList<>();
        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
            resultList.add(executorService.submit(new ProxyUtils.TestProxy(url, proxy)));
        }
        for (Future<HttpHost> fs : resultList) {
            try {
                while (!fs.isDone()) ;
                resultStr.append("Proxys.add(new HttpHost(\"" + fs.get().getHostName() + "\", " + fs.get().getPort() + "));\n");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            } finally {
                executorService.shutdown();
            }
        }
        result.put("result", resultStr.toString());
        return result;
    }

    @RequestMapping("visitCsdn")
    public int visitCsdn() {

        String url = "http://blog.csdn.net/vanadis_outlook/article/details/70670699";
        //String url = "http://www.baidu.com";

        ExecutorService executorService = Executors.newCachedThreadPool();//可缓存线程池
        ArrayList<Future<String>> resultList = new ArrayList<>();//线程返回结果
        for (int i = 0; i < 20; i++) {
            resultList.add(executorService.submit(new ProxyUtils.DoGetWithProxy(url, ProxyUtils.getProxy())));//submit返回一个Future，代表了即将要返回的结果
        }
        int num = 0;
        for (Future<String> fs : resultList) {
            try {
                while (!fs.isDone()) ;//Future返回如果没有完成，则一直循环等待，直到Future返回完成
                num++;
                System.out.println(fs.get());     //打印各个线程（任务）执行的结果
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

}
