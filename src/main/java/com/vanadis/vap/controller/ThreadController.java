package com.vanadis.vap.controller;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.vanadis.vap.model.Article;
import com.vanadis.vap.model.ArticleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;

@RestController
@RequestMapping("thread")
public class ThreadController extends BaseController {

    private static final int POOL_SIZE = 64;
    private static final ThreadFactory SHOP_COPY_FACTORY = new ThreadFactoryBuilder().setNameFormat("Thread-%d").build();
    private static final ThreadPoolExecutor EXECUTOR = new ThreadPoolExecutor(POOL_SIZE, POOL_SIZE, 0L, TimeUnit.MILLISECONDS,
            new LinkedBlockingQueue<Runnable>(30), SHOP_COPY_FACTORY);

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/article/articleList");
        return modelAndView;
    }

    @RequestMapping("test")
    public void test() {
        for (int i = 0; i < 70; i++) {
            int finalI = i;
            EXECUTOR.execute(new Thread(() -> System.out.println(finalI + ":running")));
        }
    }

}
