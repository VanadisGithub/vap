package com.vanadis.vap.controller.life;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.FileUtils;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("taobao")
public class TaobaoController extends BaseController {

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
        String activityUrl;
        if (activityId == null) {
            activityUrl = "https://uland.taobao.com/coupon/edetail?itemId=" + id + "&pid=mm_129079587_40306380_154602897";
        } else {
            activityUrl = "https://uland.taobao.com/coupon/edetail?activityId=" + activityId + "&itemId=" + id + "&pid=mm_129079587_40306380_154602897";
        }
        return ResultUtils.success(activityUrl);
    }

    public static boolean getAuctionTag(String wareUrl) {
        String auctionTag = "";
        long nowTime = System.currentTimeMillis();
        try {
            wareUrl = URLEncoder.encode(wareUrl, "utf-8");
            //wareUrl = URLEncoder.encode(wareUrl, "utf-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        int random = (int) (Math.random() * 99899) + 100;//3~5位整数

        String tbToken = RegexUtils.getRandomString(13);//"39e1b9375e56e";

        //String url = "http://pub.alimama.com/items/search.json?queryType=2&q=https%3A%2F%2Fdetail.tmall.com%2Fitem.htm%3Fspm%3Da2e2i.10477964.main.80.56a6760fTUqf6F%26id%3D556962541149&auctionTag=&perPageSize=50&shopTag=yxjh&t=1511597611585&_tb_token_=39e1b9375e56e&pvid=10_115.196.136.248_9912_1511597611493";

        String url = "http://pub.alimama.com/items/search.json?queryType=2&q=" + wareUrl + "&auctionTag=&perPageSize=50" +
                //"&shopTag=yxjh" + //勾选营销计划
                "&shopTag=" +
                "&t=" + nowTime +
                "&_tb_token_=" + tbToken +
                "&pvid=10_115.196.136.248_" + random + "_" + (nowTime - 98);

        String res = HttpUtils.doGet(url, null, null);
        JSONObject resObj = JSONObject.parseObject(res);
        JSONObject obj = resObj.getJSONObject("data");
        if ("OK".equals(obj.getJSONObject("head").getString("status"))) {
            return true;
//            JSONArray pageList = obj.getJSONArray("pageList");
//            auctionTag = pageList.getJSONObject(0).getString("auctionTag");
//            return auctionTag;
        }
        return false;
    }


}
