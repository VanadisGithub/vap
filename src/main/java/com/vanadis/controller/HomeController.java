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
        ModelAndView modelAndView = new ModelAndView("/untils");
        return modelAndView;
    }

    @RequestMapping("getUser")
    public User getUser() throws Exception {
        User user = userMapper.getLastOne();
        return user;
    }

}
