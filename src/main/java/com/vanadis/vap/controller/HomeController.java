package com.vanadis.vap.controller;

import com.vanadis.vap.model.Result;
import com.vanadis.vap.model.User;
import com.vanadis.vap.model.UserMapper;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.IpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.ResultUtils;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
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

    @RequestMapping("utils")
    public ModelAndView utils() {
        ModelAndView modelAndView = new ModelAndView("/utils");
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
        Map result = new HashMap();
        for (int i = 0; i < tags.size(); i++) {
            Element a = tags.get(i);
            if (i == 1) {
                String imgUrl = a.select("img").attr("src");
                result.put("imgUrl", imgUrl);
            }
            if (i == 2) {
                result.put("words", a.html());
            }
        }
        return ResultUtils.success(result);
    }

}
