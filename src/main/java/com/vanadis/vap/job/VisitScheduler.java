package com.vanadis.vap.job;

import com.vanadis.vap.model.Proxy;
import com.vanadis.vap.model.ProxyMapper;
import com.vanadis.vap.utils.ProxyUtils;
import org.apache.http.HttpHost;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Component
public class VisitScheduler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    @Autowired
    private ProxyMapper proxyMapper;

    @Scheduled(cron = "0 0/30 * * * ? ")//每30分钟
    public void scheduler() {
        List<Proxy> list = proxyMapper.getAll();
        List<String> urlList = new ArrayList<String>() {
            {
                add("https://blog.csdn.net/vanadis_outlook/article/details/72972168");
                add("https://blog.csdn.net/vanadis_outlook/article/details/72991117");
                add("https://blog.csdn.net/vanadis_outlook/article/details/79525729");
                add("https://blog.csdn.net/vanadis_outlook/article/details/79270395");
                add("https://blog.csdn.net/vanadis_outlook/article/details/79266476");
                add("https://blog.csdn.net/vanadis_outlook/article/details/72823024");
            }
        };
        log.info("【访问任务开始】：链接：" + urlList.size() + "；代理：" + list.size());
        for (String url : urlList) {
            ProxyUtils.doGetWithProxyList(url, list, 100, proxyMapper);
        }
    }

    @Scheduled(cron = "0 0 0 1/1 * ?")//每天
    public void saveProxyXici() {
        ProxyUtils.saveProxyXici(proxyMapper);
    }

}