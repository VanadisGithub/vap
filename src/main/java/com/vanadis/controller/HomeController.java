package com.vanadis.controller;

import com.vanadis.model.User;
import com.vanadis.model.UserMapper;
import com.vanadis.until.HttpUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("home")
public class HomeController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/home");
        return modelAndView;
    }

    @RequestMapping("util")
    public ModelAndView untils() {
        ModelAndView modelAndView = new ModelAndView("/util");
        return modelAndView;
    }

    @RequestMapping("getUser")
    public User getUser() throws Exception {
        User user = userMapper.getLastOne();
        return user;
    }

    @RequestMapping("post")
    public String doPost() throws Exception {

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", "_free_proxy_session=BAh7B0kiD3Nlc3Npb25faWQGOgZFVEkiJWJkMmVkMjUyNmYxYjcwMTU4ZDExMDg3NjRmNWM1N2EzBjsAVEkiEF9jc3JmX3Rva2VuBjsARkkiMXhOd3NuM3J6Skt4aStlRnhaZ2FOOEh4aTZPemR4cUZZQVphb0R3OXZrV1U9BjsARg%3D%3D--4f01ce2c8e784873485751c1d9e9264a0b93d248; Hm_lvt_0cf76c77469e965d2957f0553e6ecf59=1516797204,1516797371,1516797557,1516797640; Hm_lpvt_0cf76c77469e965d2957f0553e6ecf59=1516797640");

        String resultStr = HttpUtil.doGet("http://www.xicidaili.com/", headerMap);
        return resultStr;
    }

}
