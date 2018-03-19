package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Bookmark;
import com.vanadis.vap.model.BookmarkMapper;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.RegexUtils;
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
    private BookmarkMapper bookmarkMapper;

    @RequestMapping("myCollection")
    public ModelAndView myCollection() {
        ModelAndView modelAndView = new ModelAndView("/myCollection");
        List<Bookmark> bookmarks = bookmarkMapper.getList(1L);
        modelAndView.addObject("bookmarks", bookmarks);
        return modelAndView;
    }

    @RequestMapping("addBookmark")
    public boolean addBookmark(String url) {
        String html = HttpUtils.doGet(url, null, null);
        String regx = "<title>(.*?)</title>";
        String urlName = RegexUtils.getSubUtilSimple(html, regx);
        String urlHost = RegexUtils.getHost(url);
        Bookmark bookmark = new Bookmark(url, urlHost, urlName, 0, 0, 1L);
        getUrlIcon(urlHost);
        return bookmarkMapper.insert(bookmark);
    }

    @RequestMapping("getUrlIcon")
    public static String getUrlIcon(String urlHost) {
        String api = "http://statics.dnspod.cn/proxy_favicon/_/favicon?domain=" + urlHost;
        String path = "src/main/resources/static/myCollection/" + urlHost + ".png";
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
