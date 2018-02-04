package com.vanadis.vap.controller;

import com.vanadis.vap.model.Article;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.model.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/articleList");
        List<Article> articles = articleMapper.getList();
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }

}
