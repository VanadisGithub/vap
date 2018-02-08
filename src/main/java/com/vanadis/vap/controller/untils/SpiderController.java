package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Bookmark;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.until.HttpUtils;
import com.vanadis.vap.until.ResultUtils;
import org.apache.http.client.methods.HttpGet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@RestController
@RequestMapping("spider")
public class SpiderController extends BaseController {

    String qunarUrl = "https://m.flight.qunar.com/touch/api/airline/?depCity=西双版纳&arrCity=杭州&depDate=2018-02-07&depDays=30";

    @RequestMapping("")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("/spider");
        return modelAndView;
    }

    @RequestMapping("getQunar")
    public Result getQunar(String url) throws Exception {
        String data = HttpUtils.doGet(url, null, null);
        return ResultUtils.success(data);
    }

}
