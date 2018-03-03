package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sun.scenario.effect.impl.sw.sse.SSEBlend_SRC_OUTPeer;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.until.FileUtils;
import com.vanadis.vap.until.HttpUtils;
import com.vanadis.vap.until.RegexUtils;
import com.vanadis.vap.until.ResultUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
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
        String activityUrl = "";
        if (activityId == null) {
            activityUrl = "https://uland.taobao.com/coupon/edetail?itemId=" + id + "&pid=mm_109312617_42546978_242072916";

        } else {
            activityUrl = "https://uland.taobao.com/coupon/edetail?activityId=" + activityId + "&itemId=" + id + "&pid=mm_109312617_42546978_242072916";

        }
        return ResultUtils.success(activityUrl);
    }

    @RequestMapping("test3")
    public void test3(String word) throws Exception {
        String keyword = URLEncoder.encode(word, "UTF-8");
        String url = "https://s.taobao.com/api?_ksTS=1518242494244_280&callback=jsonp281&ajax=true&m=customized&sourceId=tb.index&_input_charset=utf-8&bcoffset=0&commend=all&suggest=history_1&source=suggest&search_type=item&ssid=s5-e&suggest_query=&spm=a21bo.2017.201856-taobao-item.2&q="+keyword+"&s=36&initiative_id=tbindexz_20170306&imgfile=&wq=&ie=utf8&rn=ea966764701c0e6e7e3079e04a2e8805";
        String str = HttpUtils.doGet(url, null, null);
        str = str.substring("jsonp281(".length() + 2, str.length() - 2);
        JSONArray array = JSONObject.parseObject(str).getJSONObject("API.CustomizedApi").getJSONObject("itemlist").getJSONArray("auctions");
        test(array);
        FileUtils.FileWriter("d:/111.txt", " " + word + "\r\n", true);

    }

    public void test(JSONArray array) throws Exception {
        int notFound = 0;
        int nomoney = 0;
        int havemoney = 0;
        int havemoneyquan = 0;
        for (int i = 0; i < array.size(); i++) {
            long itemId = array.getJSONObject(i).getLong("nid");
            System.out.println(itemId);
            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("referer", "https://item.taobao.com/ ");

            String activityUrl = "https://detailskip.taobao.com/service/getData/1/p1/item/detail/sib.htm?itemId=" + itemId + "&callback=onSibRequestSuccess&modules=couponActivity";
            String resultStr = HttpUtils.doGet(activityUrl, headerMap, null);
            if (resultStr.contains("ITEM_NOT_FOUND")) {
                System.out.println(itemId);
                notFound++;
                i--;
                continue;
            }
            String itemUrl = "https://detail.tmall.com/item.htm?id=" + itemId;
            if (getAuctionTag(itemUrl) == false) {
                nomoney++;
                continue;
            }
            String rgex = "onSibRequestSuccess\\((.*?)\\);";
            JSONObject data = JSONObject.parseObject(RegexUtils.getSubUtilSimple(resultStr, rgex));
            JSONArray couponList = data.getJSONObject("data").getJSONObject("couponActivity").getJSONObject("coupon").getJSONArray("couponList");
            if (couponList == null) {
                havemoney++;
                String url = "https://uland.taobao.com/coupon/edetail?itemId=" + itemId + "&pid=mm_109312617_42546978_242072916";

            } else {
                havemoneyquan++;
                String activityId = couponList.getJSONObject(0).getString("activityId");
                String url = "https://uland.taobao.com/coupon/edetail?activityId=" + activityId + "&itemId=" + itemId + "&pid=mm_109312617_42546978_242072916";
            }
        }
        System.out.println(array.size());
        System.out.println(notFound);
        System.out.println(nomoney);
        System.out.println(havemoney);
        System.out.println(havemoneyquan);
        FileUtils.FileWriter("d:/111.txt", array.size() + "|" + notFound + "|" + nomoney  + "|" + havemoney + "|" + havemoneyquan + "", true);
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

        String tbToken = getRandomString(13);//"39e1b9375e56e";

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

    public static String getRandomString(int length) {
        //随机字符串的随机字符库
        String KeyString = "abcdefghijklmnopqrstuvwxyz0123456789";
        StringBuffer sb = new StringBuffer();
        int len = KeyString.length();
        for (int i = 0; i < length; i++) {
            sb.append(KeyString.charAt((int) Math.round(Math.random() * (len - 1))));
        }
        return sb.toString();
    }
}
