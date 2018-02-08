package com.vanadis.vap.job;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.until.EmailUtils;
import com.vanadis.vap.until.FileUtils;
import com.vanadis.vap.until.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;

import java.io.FileWriter;

@Component
public class AirplaneSchedule {
    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    private JavaMailSender mailSender; //自动注入的Bean

    @Value("${spring.mail.username}")
    private String Sender; //读取配置文件中的参数

    private int minPrice = 1600;

    //@Scheduled(cron = "0/10 * * * * ? ") // 每10秒执行一次
    @Scheduled(cron = "0 0/20 * * * ? ") // 每30分钟执行一次
    public void scheduler1() {
        String qunarUrl = "https://m.flight.qunar.com/touch/api/airline/?depCity=昆明&arrCity=杭州&depDate=2018-02-24&depDays=1";
        String data = HttpUtils.doGet(qunarUrl, null, null);
        JSONObject result = JSONObject.parseObject(data);
        if (result.getIntValue("code") == 0) {
            JSONArray array = result.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject ariplane = array.getJSONObject(i);
                int price = ariplane.getIntValue("price");
                String date = ariplane.getString("depDate");
                FileUtils.FileWriter("d:/去哪儿.txt", "去哪儿|" + date + "|" + price + "\r\n", true);
                if (minPrice >= price) {
                    String content = "有票了，在【去哪儿】：日期：" + date + "，价钱：" + price;
                    EmailUtils.send(mailSender, Sender, "872671438@qq.com", "飞机票", content);
                }
            }
        }
    }

    //@Scheduled(cron = "0/10 * * * * ? ") // 每10秒执行一次
    @Scheduled(cron = "0 0/20 * * * ? ") // 每30分钟执行一次
    public void scheduler2() {
        String qunarUrl = "https://kuxun-i.meituan.com/getLowPriceCalendar/other/4/mt%7Cm%7Cm/?startdate=2018-02-24&depart=KMG&arrive=HGH";
        String data = HttpUtils.doGet(qunarUrl, null, null);
        JSONObject result = JSONObject.parseObject(data).getJSONObject("data");
        if (result.getIntValue("code") == 0) {
            JSONArray array = result.getJSONArray("dataList");
            for (int i = 0; i < 1; i++) {
                JSONObject ariplane = array.getJSONObject(i);
                int price = ariplane.getIntValue("price");
                String date = ariplane.getString("date");
                FileUtils.FileWriter("d:/美团.txt", "美团|" + date + "|" + price + "\r\n", true);
                if (minPrice >= price) {
                    String content = "有票了，在【美团】：日期：" + date + "，价钱：" + price;
                    EmailUtils.send(mailSender, Sender, "872671438@qq.com", "飞机票", content);
                }
            }
        }
    }
}