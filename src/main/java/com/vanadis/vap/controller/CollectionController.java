package com.vanadis.vap.controller;

import com.vanadis.vap.model.Bookmark;
import com.vanadis.vap.model.BookmarkMapper;
import com.vanadis.vap.until.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@RestController
@RequestMapping("collection")
public class CollectionController extends BaseController {

    @Autowired
    private static BookmarkMapper bookmarkMapper;

    @RequestMapping("myCollection")
    public static ModelAndView getUrlIcon() {
        ModelAndView modelAndView = new ModelAndView("/myCollection");
        List<Bookmark> bookmarks = bookmarkMapper.getList(1L);
        modelAndView.addObject(bookmarks);
        return modelAndView;
    }

    @RequestMapping("addBookmark")
    public static boolean addBookmark(String url) {
        Bookmark bookmark = new Bookmark(url, "", "", 1L);
        return bookmarkMapper.insert(bookmark);
    }

    @RequestMapping("getUrlIcon")
    public static String getUrlIcon(String url) {
        String api = "http://statics.dnspod.cn/proxy_favicon/_/favicon?domain=" + url;
        String path = "src/main/resources/static/myCollection/" + url + ".png";
        InputStream inputStream = HttpUtils.getInputStream(api);
        byte[] data = new byte[1024];
        int len = 0;
        FileOutputStream fileOutputStream = null;
        try {
            fileOutputStream = new FileOutputStream(path);
            while ((len = inputStream.read(data)) != -1) {
                fileOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return path;
    }
}
