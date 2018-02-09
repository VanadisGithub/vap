package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.until.HttpUtils;
import com.vanadis.vap.until.RegexUtils;
import com.vanadis.vap.until.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Pattern;

@RestController
@RequestMapping("taobao")
public class TaobaoController extends BaseController {

    String qunarUrl = "https://m.flight.qunar.com/touch/api/airline/?depCity=西双版纳&arrCity=杭州&depDate=2018-02-07&depDays=30";

    String meituanUrl = "https://detailskip.taobao.com/service/getData/1/p1/item/detail/sib.htm?itemId=35158433106&callback=onSibRequestSuccess&modules=couponActivity";

    @RequestMapping("")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("/taobao");
        return modelAndView;
    }

    @RequestMapping("getCouponList")
    public Result getCouponList(String url) throws Exception {
        Map<String, Object> result = new HashMap<>();

        String id = RegexUtils.getId(url);

        String activityUrl = "https://detailskip.taobao.com/service/getData/1/p1/item/detail/sib.htm?itemId=" + id + "&callback=onSibRequestSuccess&modules=couponActivity";

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("referer", "https://item.taobao.com/ ");

        String resultStr = HttpUtils.doGet(activityUrl, headerMap, null);
        String rgex = "onSibRequestSuccess\\((.*?)\\);";
        JSONObject data = JSONObject.parseObject(RegexUtils.getSubUtilSimple(resultStr, rgex));
        JSONArray couponList = data.getJSONObject("data").getJSONObject("couponActivity").getJSONObject("coupon").getJSONArray("couponList");

        return ResultUtils.success(couponList);
    }

    @RequestMapping("getBuyUrl")
    public Result getBuyUrl(String url, String activityId) throws Exception {
        String id = RegexUtils.getId(url);
        String activityUrl = "https://uland.taobao.com/coupon/edetail?activityId=" + activityId + "&itemId=" + id + "&pid=mm_129079587_41432077_176400272";
        return ResultUtils.success(activityUrl);
    }

}
