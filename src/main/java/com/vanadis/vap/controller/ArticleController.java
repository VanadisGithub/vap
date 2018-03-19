package com.vanadis.vap.controller;

import com.vanadis.vap.model.Article;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.FileUtils;
import com.vanadis.vap.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

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
    public ModelAndView markdown(HttpServletRequest request, HttpServletResponse response, long articleId) {
        ModelAndView modelAndView = new ModelAndView("/article/markdown");
        if (articleId != 0L) {
            Article article = articleMapper.getArticle(articleId);
            modelAndView.addObject("article", article);
        }
        return modelAndView;
    }

    @RequestMapping("saveArticle")
    public Result saveArticle(Article article) {
        article.setUserId(1L);
        Long articleId = articleMapper.insertWithId(article);

        String path = "src/myMarkdown/" + article.getTitle() + ".md";
        FileUtils.FileWriter(path, article.getContent());

        return ResultUtils.success("保存成功", articleId);
    }

}
