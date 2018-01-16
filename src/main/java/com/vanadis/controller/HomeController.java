package com.vanadis.controller;

import com.vanadis.entity.User;
import com.vanadis.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/home")
public class HomeController extends BaseController {

    @Autowired
    private UserMapper userMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/home");
        return modelAndView;
    }

    @RequestMapping("/getUser")
    public void getUser() throws Exception {
        //userMapper.insert(new User("Vanadis", "ling123427", "872671438@qq.com", "Vanadis", "0"));
        //List<User> users = userMapper.getAll();
        User user = userMapper.getOne(1L);
        System.out.println(user.toString());
    }

}
