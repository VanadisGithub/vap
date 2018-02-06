package com.vanadis.vap.controller;

import com.vanadis.vap.model.Article;
import com.vanadis.vap.model.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("article")
public class ArticleController extends BaseController {

    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/article/articleList");
        List<Article> articles = articleMapper.getList();
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }

    @RequestMapping("article")
    public ModelAndView article(Long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article/article");
        Article article = articleMapper.getArticle(articleId);
        modelAndView.addObject("article", article);
        return modelAndView;
    }

    @RequestMapping("markdown")
    public ModelAndView markdown() {
        ModelAndView modelAndView = new ModelAndView("/article/markdown");
        return modelAndView;
    }

    @RequestMapping("saveArticle")
    public Map<String, Object> saveArticle(Article article) {
        article.setUserId(1L);
        articleMapper.insert(article);
        Map<String, Object> result = new HashMap<>();
        result.put("msg", "保存成功");
        return result;
    }


}
