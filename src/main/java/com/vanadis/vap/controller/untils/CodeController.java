package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("entity")
public class CodeController extends BaseController {

    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/entity");
        return modelAndView;
    }

}
