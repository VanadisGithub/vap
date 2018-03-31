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
        List<Proxy> list = proxyMapper.getGoodList(40);
        List<String> urlList = new ArrayList<String>() {
            {
//                add("http://blog.csdn.net/vanadis_outlook/article/details/79525729");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/79270395");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/79266476");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/73223918");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/73136356");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/72991117");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/72972168");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/72971080");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/72851739");
//                add("http://blog.csdn.net/vanadis_outlook/article/details/72848008");
                add("http://blog.csdn.net/vanadis_outlook/article/details/72844302");
                add("http://blog.csdn.net/vanadis_outlook/article/details/72831853");
                add("http://blog.csdn.net/vanadis_outlook/article/details/72830205");
                add("http://blog.csdn.net/vanadis_outlook/article/details/71541589");
                add("http://blog.csdn.net/vanadis_outlook/article/details/71515487");
                add("http://blog.csdn.net/vanadis_outlook/article/details/71440510");
                add("http://blog.csdn.net/vanadis_outlook/article/details/71106563");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70670699");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70332257");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70313101");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70313045");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70172029");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70158834");
                add("http://blog.csdn.net/vanadis_outlook/article/details/70144841");
                add("http://blog.csdn.net/vanadis_outlook/article/details/68926324");
                add("http://blog.csdn.net/vanadis_outlook/article/details/68485949");
                add("http://blog.csdn.net/vanadis_outlook/article/details/68482881");
                add("http://blog.csdn.net/vanadis_outlook/article/details/68061686");
                add("http://blog.csdn.net/vanadis_outlook/article/details/67634780");
                add("http://blog.csdn.net/vanadis_outlook/article/details/67634699");
            }
        };
        log.info("【访问任务开始】：链接：" + urlList.size() + "；代理：" + list.size());
        for (String url : urlList) {
            ProxyUtils.doGetWithProxyList(url, list, 100, proxyMapper);
        }
    }

    @Scheduled(cron = "0 0 0 * * ? ")//每天0点
    public void saveProxyXici() {
        ProxyUtils.saveProxyXici(proxyMapper);
    }

}