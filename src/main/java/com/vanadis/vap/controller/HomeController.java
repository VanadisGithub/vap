package com.vanadis.vap.controller;

import com.mysql.jdbc.StringUtils;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.model.User;
import com.vanadis.vap.model.UserMapper;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.IpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.ResultUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.codehaus.groovy.util.StringUtil;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("")
public class HomeController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    String qqIcon = "http://q1.qlogo.cn/g?b=qq&nk=824503172&s=140&t=1368862220";

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/home");
        return modelAndView;
    }

    @RequestMapping("getUser")
    public User getUser() {
        User user = userMapper.getLastOne();
        return user;
    }

    @RequestMapping("post")
    public String doPost() {

        String url = "http://www.baidu.com";

        HttpHost proxy = new HttpHost("123.185.131.147", 8118, "http");

        String resultStr = HttpUtils.doGet(url, null, proxy);

        return resultStr;
    }

    @RequestMapping("getHomeImg")
    public Result getHomeImg() {
        String url = "http://wufazhuce.com/";
        String html = HttpUtils.doGet(url, null, null);
        Document document = Jsoup.parse(html);
        Elements tags = document.select("a");
        List<HashMap> homeList = new ArrayList<>();
        for (int i = 1; i < 13; i = i + 2) {
            Element img = tags.get(i);
            String imgUrl = img.select("img").attr("src");
            if (StringUtils.isNullOrEmpty(imgUrl)) {
                log.warn("获取首页图片失败！");
                continue;
            }
            Element word = tags.get(i + 1);
            Map result = new HashMap();
            result.put("imgUrl", imgUrl);
            result.put("words", word.html());
            homeList.add((HashMap) result);
        }
        return ResultUtils.success(homeList.get((int) (Math.random() * homeList.size())));
    }

}
