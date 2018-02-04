package com.vanadis.vap.controller;

import com.vanadis.vap.model.User;
import com.vanadis.vap.model.UserMapper;
import com.vanadis.vap.action.AutoCodeAction;
import com.vanadis.vap.until.HttpUtils;
import org.apache.http.HttpHost;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("")
public class HomeController extends BaseController {

    @Autowired
    private UserMapper userMapper;

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

    @RequestMapping("test")
    public void test() {
        AutoCodeAction.autoCode();
    }

}
