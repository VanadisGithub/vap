package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.*;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

@RestController
@RequestMapping("flight")
public class FlightController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() throws Exception {
        ModelAndView modelAndView = new ModelAndView("/flight");
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
        meituanUrl = replaceCity(meituanUrl, depCity, arrCity);
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
        feizhuUrl = replaceCity(feizhuUrl, depCity, arrCity);
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
        xiechengUrl = replaceCity(xiechengUrl, depCity, arrCity);

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

    public static String replaceCity(String url, String depCity, String arrCity) {
        url = url.replace(depCity, CITY_NO.get(depCity));
        url = url.replace(arrCity, CITY_NO.get(arrCity));
        return url;
    }

    public final static Map<String, String> CITY_NO = new HashMap<String, String>() {
        {
            put("北京", "PEK");
            put("沈阳", "SHE");
            put("福州", "FOC");
            put("广州", "CAN");
            put("深圳", "SZX");
            put("上海", "SHA");
            put("海口", "HAK");
            put("襄樊", "XFN");
            put("长沙", "CSX");
            put("常德", "CGD");
            put("浦东", "PVG");
            put("丹东", "DDG");
            put("锦州", "JUZ");
            put("杭州", "HGH");
            put("宁波", "NGB");
            put("天津", "TSN");
            put("南昌", "KHN");
            put("郑州", "CGO");
            put("重庆", "CKG");
            put("长春", "CGQ");
            put("昆明", "KMG");
            put("青岛", "TAO");
            put("烟台", "YNT");
            put("常州", "CZX");
            put("成都", "CTU");
            put("贵阳", "KWE");
            put("温州", "WNZ");
            put("厦门", "XMN");
            put("太原", "TYN");
            put("南京", "NKG");
            put("大连", "DLX");
            put("宜昌", "YIH");
            put("北海", "BHY");
            put("晋江", "JJN");
            put("三亚", "SYX");
            put("合肥", "HFE");
            put("西安", "SIA");
            put("武汉", "WUH");
            put("徐州", "XUZ");
            put("湛江", "ZHA");
            put("济南", "TNA");
            put("广汉", "GHN");
            put("大同", "DAT");
            put("黄山", "TXN");
            put("桂林", "KWL");
            put("兰州", "LHW");
            put("延吉", "YNJ");
            put("延安", "ENY");
            put("九江", "JIU");
            put("安康", "AKA");
            put("南宁", "NNG");
            put("伯力", "KHV");
            put("汉中", "HZG");
            put("长治", "CIU");
            put("榆林", "UYN");
            put("黄岩", "HYN");
            put("安庆", "AQG");
            put("汕头", "SWA");
            put("赣州", "KOW");
            put("朝阳", "CHG");
            put("万县", "WXN");
            put("包头", "BAV");
            put("南阳", "NNY");
            put("沙市", "SHS");
            put("吉林", "JIL");
            put("西昌", "XIC");
            put("银川", "INC");
            put("珠海", "ZUH");
            put("黑河", "HEK");
            put("衡阳", "HNY");
            put("庐山", "LUZ");
            put("铜仁", "TEN");
            put("拉萨", "LXA");
            put("洛阳", "LYA");
            put("汉城", "SEL");
            put("西宁", "XNN");
            put("衢州", "JUZ");
            put("香港", "HKG");
            put("临沂", "LYI");
            put("南充", "NAO");
            put("南通", "NTG");
            put("达县", "DAX");
            put("恩施", "ENH");
            put("澳门", "MFM");
            put("台北", "TPE");
            put("柳州", "LZH");
            put("丹山", "HSN");
            put("宜宾", "YBP");
            put("梁平", "LIA");
            put("丽江", "LJG");
            put("赤峰", "CIF");
            put("绵阳", "MIG");
            put("广元", "GYS");
            put("无锡", "WUX");
            put("吉安", "KNC");
            put("高雄", "KHH");
            put("义乌", "YIW");
            put("哈尔滨", "HRB");
            put("石家庄", "SJW");
            put("张家界", "DYG");
            put("秦皇岛", "SHP");
            put("阿勒泰", "AAT");
            put("海拉尔", "HLD");
            put("牡丹江", "MDG");
            put("佳木斯", "JMU");
            put("阿克苏", "AKU");
            put("景德镇", "JDZ");
            put("库尔勒", "KRL");
            put("连云港", "LYG");
            put("武夷山", "WUS");
            put("乌鲁木齐", "URC");
            put("齐齐哈尔", "NDG");
            put("呼和浩特", "HET");
            put("乌兰浩特", "HLH");
            put("锡林浩特", "XIL");
            put("克拉马依", "KRY");
            put("西双版纳", "JHG");
            put("北京", "PEK");
            put("沈阳", "SHE");
            put("福州", "FOC");
            put("广州", "CAN");
            put("深圳", "SZX");
            put("上海", "SHA");
            put("海口", "HAK");
            put("襄樊", "XFN");
            put("长沙", "CSX");
            put("常德", "CGD");
            put("浦东", "PVG");
            put("丹东", "DDG");
            put("锦州", "JUZ");
            put("杭州", "HGH");
            put("宁波", "NGB");
            put("天津", "TSN");
            put("南昌", "KHN");
            put("郑州", "CGO");
            put("重庆", "CKG");
            put("长春", "CGQ");
            put("昆明", "KMG");
            put("青岛", "TAO");
            put("烟台", "YNT");
            put("常州", "CZX");
            put("成都", "CTU");
            put("贵阳", "KWE");
            put("温州", "WNZ");
            put("厦门", "XMN");
            put("太原", "TYN");
            put("南京", "NKG");
            put("大连", "DLX");
            put("宜昌", "YIH");
            put("北海", "BHY");
            put("晋江", "JJN");
            put("三亚", "SYX");
            put("合肥", "HFE");
            put("西安", "SIA");
            put("武汉", "WUH");
            put("徐州", "XUZ");
            put("湛江", "ZHA");
            put("济南", "TNA");
            put("广汉", "GHN");
            put("大同", "DAT");
            put("黄山", "TXN");
            put("桂林", "KWL");
            put("兰州", "LHW");
            put("延吉", "YNJ");
            put("延安", "ENY");
            put("九江", "JIU");
            put("安康", "AKA");
            put("南宁", "NNG");
            put("伯力", "KHV");
            put("汉中", "HZG");
            put("长治", "CIU");
            put("榆林", "UYN");
            put("黄岩", "HYN");
            put("安庆", "AQG");
            put("汕头", "SWA");
            put("赣州", "KOW");
            put("朝阳", "CHG");
            put("万县", "WXN");
            put("包头", "BAV");
            put("南阳", "NNY");
            put("沙市", "SHS");
            put("吉林", "JIL");
            put("西昌", "XIC");
            put("银川", "INC");
            put("珠海", "ZUH");
            put("黑河", "HEK");
            put("衡阳", "HNY");
            put("庐山", "LUZ");
            put("铜仁", "TEN");
            put("拉萨", "LXA");
            put("洛阳", "LYA");
            put("汉城", "SEL");
            put("西宁", "XNN");
            put("衢州", "JUZ");
            put("香港", "HKG");
            put("临沂", "LYI");
            put("南充", "NAO");
            put("南通", "NTG");
            put("达县", "DAX");
            put("恩施", "ENH");
            put("澳门", "MFM");
            put("台北", "TPE");
            put("柳州", "LZH");
            put("丹山", "HSN");
            put("宜宾", "YBP");
            put("梁平", "LIA");
            put("丽江", "LJG");
            put("赤峰", "CIF");
            put("绵阳", "MIG");
            put("广元", "GYS");
            put("无锡", "WUX");
            put("吉安", "KNC");
            put("高雄", "KHH");
            put("义乌", "YIW");
            put("哈尔滨", "HRB");
            put("石家庄", "SJW");
            put("张家界", "DYG");
            put("秦皇岛", "SHP");
            put("阿勒泰", "AAT");
            put("海拉尔", "HLD");
            put("牡丹江", "MDG");
            put("佳木斯", "JMU");
            put("阿克苏", "AKU");
            put("景德镇", "JDZ");
            put("库尔勒", "KRL");
            put("连云港", "LYG");
            put("武夷山", "WUS");
            put("乌鲁木齐", "URC");
            put("齐齐哈尔", "NDG");
            put("呼和浩特", "HET");
            put("乌兰浩特", "HLH");
            put("锡林浩特", "XIL");
            put("克拉马依", "KRY");
            put("西双版纳", "JHG");
        }
    };
}
