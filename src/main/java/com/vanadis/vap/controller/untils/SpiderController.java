package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Bookmark;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.until.*;
import org.apache.http.client.methods.HttpGet;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("spider")
public class SpiderController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("/spider");
        return modelAndView;
    }

    @RequestMapping("getFlight")
    public Result getFlight(String depCity, String arrCity, String startDate) throws Exception {
        Map<String, Object> result = new HashMap<>();
        result.put("qunar", getQunar(depCity, arrCity, startDate));
        result.put("meituan", getMeiTuan(depCity, arrCity, startDate));
        result.put("jingdong", getJingDong(depCity, arrCity, startDate));
        result.put("feizhu", getFeiZhu(depCity, arrCity, startDate));
        result.put("xiecheng", getXieCheng(depCity, arrCity, startDate));
        return ResultUtils.success(result);
    }

    public Map<String, Integer> getQunar(String depCity, String arrCity, String startDate) {
        Map<String, Integer> qunar = new HashMap<>();
        String qunarUrl = "https://m.flight.qunar.com/touch/api/airline/?depCity=" + depCity + "&arrCity=" + arrCity + "&depDate=" + startDate + "&depDays=30";
        String data = HttpUtils.doGet(qunarUrl, null, null);
        JSONObject result = JSONObject.parseObject(data);
        if (result.getIntValue("code") == 0) {
            JSONArray array = result.getJSONArray("data");
            for (int i = 0; i < array.size(); i++) {
                JSONObject ariplane = array.getJSONObject(i);
                int price = ariplane.getIntValue("price");
                String date = ariplane.getString("depDate");
                qunar.put(date, price);
            }
        }
        return qunar;
    }

    public Map<String, Integer> getMeiTuan(String depCity, String arrCity, String startDate) {
        Map<String, Integer> meituan = new HashMap<>();
        String meituanUrl = "https://kuxun-i.meituan.com/getLowPriceCalendar/other/4/mt%7Cm%7Cm/?startdate=" + startDate + "&depart=" + depCity + "&arrive=" + arrCity;
        meituanUrl = replaceCity(meituanUrl);
        String data = HttpUtils.doGet(meituanUrl, null, null);
        JSONObject result = JSONObject.parseObject(data).getJSONObject("data");
        if (result.getIntValue("code") == 0) {
            JSONArray array = result.getJSONArray("dataList");
            for (int i = 0; i < 30; i++) {
                JSONObject ariplane = array.getJSONObject(i);
                int price = ariplane.getIntValue("price");
                String date = ariplane.getString("date");
                meituan.put(date, price);
            }
        }
        return meituan;
    }

    public Map<String, Integer> getFeiZhu(String depCity, String arrCity, String startDate) throws ParseException {
        Map<String, Integer> feizhu = new HashMap<>();
        JSONArray array = getFeiZhuArray(depCity, arrCity, startDate);
        for (int i = 0; i < array.size(); i++) {
            JSONObject ariplane = array.getJSONObject(i);
            int price = ariplane.getIntValue("price");
            String date = ariplane.getString("leaveDate");
            feizhu.put(date, price);
        }
        if (array.size() < 32) {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
            Date start = df.parse(startDate);
            Date nextMonth = new Date(start.getYear(), start.getMonth() + 1, 1);
            String nextMonthStr = df.format(nextMonth);
            JSONArray array2 = getFeiZhuArray(depCity, arrCity, nextMonthStr);
            for (int i = 0; i < array2.size(); i++) {
                JSONObject ariplane = array2.getJSONObject(i);
                int price = ariplane.getIntValue("price");
                String date = ariplane.getString("leaveDate");
                feizhu.put(date, price);
            }
        }
        return feizhu;
    }

    public static JSONArray getFeiZhuArray(String depCity, String arrCity, String startDate) {
        JSONArray array = new JSONArray();
        String feizhuUrl = "https://r.fliggy.com/cheapestCalendar/pc?_ksTS=" + System.currentTimeMillis() + "_2864&callback=jsonp_trip_2470&bizType=0&tripType=0&calendarType=1&depCityCode=" + depCity + "&arrCityCode="
                + arrCity + "&leaveDate=" + startDate;
        feizhuUrl = replaceCity(feizhuUrl);
        Map<String, Object> headerMap = new HashMap<>();
        String resultStr = HttpUtils.doGet(feizhuUrl, null, null);
        resultStr = resultStr.substring(16, resultStr.length() - 1);
        JSONObject data = JSONObject.parseObject(resultStr);
        if (data.containsKey("result")) {
            array = data.getJSONArray("result");
        }
        return array;
    }

    public Map<String, Integer> getJingDong(String depCity, String arrCity, String startDate) {
        Map<String, Integer> jingdong = new HashMap<>();
        JSONArray array = getJingDongArray(depCity, arrCity, startDate);
        for (int i = 0; i < array.size(); i++) {
            JSONObject ariplane = array.getJSONObject(i);
            getJingDongPrice(jingdong, ariplane);
        }
        if (array.size() < 30) {
            try {
                DateFormat df = new SimpleDateFormat("yyyy-MM-dd");
                Date start = df.parse(startDate);
                Date nextMonth = new Date(start.getYear(), start.getMonth() + 1, 1);
                String nextMonthStr = df.format(nextMonth);
                JSONArray array2 = getJingDongArray(depCity, arrCity, nextMonthStr);
                for (int i = 0; i < array2.size(); i++) {
                    JSONObject ariplane = array2.getJSONObject(i);
                    getJingDongPrice(jingdong, ariplane);
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        return jingdong;
    }

    public static JSONArray getJingDongArray(String depCity, String arrCity, String startDate) {
        String jingdongUrl = "https://jipiaoh5-gw.jd.com/calendar/monthlowprice";
        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Content-Type", "application/json;charset=UTF-8");
        headerMap.put("Accept", "application/json, text/plain, */*");
        String dataStr = "{\"depCity\":\"" + depCity + "\",\"arrCity\":\"" + arrCity + "\",\"depDate\":\"" + startDate + "\"}";
        String data = HttpUtils.doPost(jingdongUrl, dataStr, headerMap, null);
        JSONObject result = JSONObject.parseObject(data).getJSONObject("data");
        JSONArray array = result.getJSONArray("lowPriceCalendarList");
        return array;
    }

    public static void getJingDongPrice(Map<String, Integer> jingdong, JSONObject ariplane) {
        String priceStr = ariplane.getString("title");
        int price = 0;
        if (isInteger(priceStr)) {
            price = Integer.valueOf(priceStr);
        }
        String date = ariplane.getString("start");
        jingdong.put(date, price);
    }

    public static JSONObject getXieCheng(String depCity, String arrCity, String startDate) {
        String xiechengUrl = "http://flights.ctrip.com/domesticsearch/search/SearchFirstRouteFlights?DCity1=" + depCity + "&ACity1=" + arrCity + "&SearchType=S&DDate1=" + startDate + "&IsNearAirportRecommond=0&LogToken=3f4a55df5bfb46a39cbd1cdd36ab919a&rk=4.196383294554751215345&CK=8B72BDDDEF20A3D89D7DC60E2BEC3FCC&r=0.51100000101289162620014";
        xiechengUrl = replaceCity(xiechengUrl);

        Map<String, Object> headerMap = new HashMap<>();
        headerMap.put("Cookie", "UM_distinctid=160c109bf5313c-073bb098047f8f-5a442916-1fa400-160c109bf5448f; _RSG=RNahF19doECETJJo3fOe7A; _RDG=28acf22945bb6b2ffe31948227ebaaf969;");
        headerMap.put("Referer", "http://flights.ctrip.com/booking/KMG-HGH-day-1.html?DDate1=2018-02-10");
        String data = HttpUtils.doGet(xiechengUrl, headerMap, null);
        JSONObject result = JSONObject.parseObject(data).getJSONObject("lps");
        return result;
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]*$");
        return pattern.matcher(str).matches();
    }

    public static String replaceCity(String url) {
        url = url.replace("杭州", "HGH");
        url = url.replace("昆明", "KMG");
        url = url.replace("西双版纳", "JHG");
        return url;
    }
}
