package com.vanadis.vap.job;

import com.vanadis.vap.controller.untils.ProxyController;
import com.vanadis.vap.model.Proxy;
import com.vanadis.vap.model.ProxyMapper;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.ProxyUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class VisitScheduler {
    private final Logger log = LoggerFactory.getLogger(getClass());

    //https://blog.csdn.net/vanadis_outlook
    public static List<String> urlList = new ArrayList<String>() {
        {
            add("http://blog.csdn.net/vanadis_outlook/article/details/73223918");
            add("http://blog.csdn.net/vanadis_outlook/article/details/72851739");
            add("http://blog.csdn.net/vanadis_outlook/article/details/72848008");
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
            add("http://blog.csdn.net/Vanadis_outlook/article/details/79793961");
            add("http://blog.csdn.net/Vanadis_outlook/article/details/73038594");
        }
    };

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    @Autowired
    private ProxyMapper proxyMapper;

    @Scheduled(cron = "0 0/30 * * * ? ")//每30分钟
    public void scheduler() {
        proxyMapper.updateErrorNum(-50);
        List<Proxy> list = proxyMapper.getGoodList(30);
        List<String> urlList = ProxyController.htmlToProxyCsdn();
        log.info("【访问任务开始】：链接：" + urlList.size() + "；代理：" + list.size());
        for (String url : urlList) {
            ProxyUtils.doGetWithProxyList(url, list, 200, proxyMapper);
            HttpUtils.doGet(url, null, null);
        }
    }

    @Scheduled(cron = "0 5/10 * * * ? ")//15/25/35分钟
    public void saveProxyKuai() {
        ProxyUtils.saveProxyKuai(proxyMapper);
    }

    @Scheduled(cron = "0 15/59 0/1 * * ? ")//15分开始 每1小时
    public void saveProxyXici() {
        ProxyUtils.saveProxyXici(proxyMapper);
    }

}