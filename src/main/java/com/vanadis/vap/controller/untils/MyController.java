package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Bookmark;
import com.vanadis.vap.model.BookmarkMapper;
import com.vanadis.vap.until.HttpUtils;
import com.vanadis.vap.until.RegexUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("my")
public class MyController extends BaseController {

    @Autowired
    private BookmarkMapper bookmarkMapper;

    @RequestMapping("task")
    public ModelAndView myCollection() {
        ModelAndView modelAndView = new ModelAndView("/myTask");
        List<Bookmark> bookmarks = bookmarkMapper.getList(1L);
        modelAndView.addObject("bookmarks", bookmarks);
        return modelAndView;
    }

}
