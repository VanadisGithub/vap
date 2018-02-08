package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSON;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.until.HttpUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;

@RestController
@RequestMapping("http")
public class HttpController extends BaseController {

    @Autowired
    private ArticleMapper articleMapper;

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
            result = HttpUtils.testGet(url, map);
        }
        return result;
    }

//    @RequestMapping("send")
//    public Map send(String sendType, String url, String headerMap, String dataStr) throws InterruptedException {
//        Map<String, Object> result = null;
//        Map<String, Object> map = JSON.parseObject(headerMap, Map.class);
//
//        for (int i = 64; i < 1000; i++) {
//            if (sendType.equals("post")) {
//                dataStr = "tag=29&gcid=8&siteid=42608322&selectact=add&newadzonename=%E6%8E%A8%E5%B9%BF%E4%BD%8D" + i + "&t=1518070053967&_tb_token_=eefb9386ee133&pvid=10_183.156.108.183_541_1518070034228";
//
//                result = HttpUtils.testPost(url, dataStr, map);
//
//                System.out.println(result);
//            } else {
//                result = HttpUtils.testGet(url, map);
//            }
//            Thread.sleep(2000);
//        }
//        return result;
//    }

}
