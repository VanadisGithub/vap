package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSON;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.utils.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("http")
public class HttpController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/http");
        return modelAndView;
    }

    @RequestMapping("send")
    public Map send(String sendType, String url, String headerMap, String dataStr) {
        Map<String, Object> result;
        Map<String, Object> map = JSON.parseObject(headerMap, Map.class);
        if (sendType.equals("post")) {
            result = HttpUtils.testPost(url, dataStr, map);
        } else {
            result = HttpUtils.postmanGet(url, map);
        }
        return result;
    }

}
