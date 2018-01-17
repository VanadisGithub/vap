package com.vanadis.controller;

import com.vanadis.model.User;
import com.vanadis.model.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

    @RequestMapping("getUser")
    public User getUser() throws Exception {
        userMapper.insert(new User("Vanadis", "ling123427", "872671438@qq.com", "Vanadis", System.currentTimeMillis()));
        User user = userMapper.getLastOne();
        return user;
    }

}
