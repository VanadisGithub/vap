package com.vanadis.vap.controller;

import com.vanadis.vap.model.Article;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.model.Bookmark;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.*;
import java.util.ArrayList;
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
        ModelAndView modelAndView = new ModelAndView("/articleList");
        List<Article> articles = articleMapper.getList();
        modelAndView.addObject("articles", articles);
        return modelAndView;
    }

    @RequestMapping("markdown")
    public ModelAndView markdown() {
        ModelAndView modelAndView = new ModelAndView("/markdown");
        return modelAndView;
    }

    @RequestMapping("saveArticle")
    public Map<String, Object> saveArticle(String markdown) {

        String path = "src/main/resources/static/myMarkdown/" + 1 + ".md";

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path);
            fwriter.write(markdown);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Map<String, Object> result = new HashMap<>();
        return result;
    }

    @RequestMapping("getArticle")
    private Map<String, Object> getArticle(Long articleId) {
        StringBuffer resultStr = new StringBuffer();
        Map<String, Object> result = new HashMap<>();
        try {
            FileReader fr = new FileReader("src/main/resources/static/myMarkdown/" + articleId + ".md");
            BufferedReader br = new BufferedReader(fr);
            String markdown;
            while ((markdown = br.readLine()) != null) {
                resultStr.append(markdown);
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        result.put("result", resultStr.toString());
        return result;
    }

}
