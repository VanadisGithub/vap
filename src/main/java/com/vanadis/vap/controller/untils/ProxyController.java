package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.job.VisitScheduler;
import com.vanadis.vap.model.Proxy;
import com.vanadis.vap.model.ProxyMapper;
import com.vanadis.vap.utils.HttpUtils;
import com.vanadis.vap.utils.ImgUtils;
import com.vanadis.vap.utils.ProxyUtils;
import com.vanadis.vap.utils.RegexUtils;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpHost;
import org.apache.http.client.methods.HttpGet;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@RestController
@RequestMapping("proxy")
public class ProxyController extends BaseController {

    @Autowired
    private ProxyMapper proxyMapper;

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/proxy");
        return modelAndView;
    }

    /**
     * 通过代理刷访问
     *
     * @param url
     * @throws InterruptedException
     */
    @RequestMapping("visitByProxy")
    public void visitByProxy(String url) {
        int mostErrorNum = proxyMapper.getMostErrorNum();
        List<Proxy> list = proxyMapper.getGoodList(mostErrorNum);
        log.info("【访问开始】：链接：" + url + "；代理：" + list.size());
        String[] urlArr = url.split("/[\n,]/g");
        for (int i = 0; i < urlArr.length; i++) {
            String eUrl = urlArr[i];
            ProxyUtils.doGetWithProxyList(eUrl, list, 200, proxyMapper);
        }
    }


    /**
     * 西刺页面转代码
     *
     * @param html
     * @return
     */
    @RequestMapping("htmlToProxy")
    public Map htmlToProxy(String html) {

        Map<String, String> result = new HashMap<>();
        StringBuffer resultStr = new StringBuffer();

        Document doc = Jsoup.parse(html);
        Elements trs = doc.select("tr");

        for (int i = 1; i < trs.size(); i++) {
            Elements tds = trs.get(i).select("td");
            String ip = tds.get(1).html();
            String port = tds.get(2).html();
            HttpHost proxy = new HttpHost(ip, Integer.valueOf(port));
            resultStr.append("Proxys.add(new HttpHost(\"" + proxy.getHostName() + "\", " + proxy.getPort() + "));\n");
        }
        return result;
    }

    /**
     * 大爷页面转代码
     *
     * @param html
     * @return
     */
    @RequestMapping("htmlToProxy2")
    public Map htmlToProxy2(String html) {

        Map<String, String> result = new HashMap<>();
        StringBuffer resultStr = new StringBuffer();
        Matcher matcher = Pattern.compile("<td v=(.*?)</td>").matcher(html);
        List<String> ipList = new ArrayList<>();
        while (matcher.find()) {
            String ip = matcher.group();
            if (!ip.contains("v2")) {
                ip = RegexUtils.getSubUtilSimple(ip, ">(.*?)<");
                ipList.add(ip);
                Proxy proxy = new Proxy(ip, "8080", 1, 0, 0);
                if (proxyMapper.isExcited(proxy.getIp()) == 0) {
                    if (proxyMapper.insert(proxy)) {
                    }
                }
            }
        }

        List<String> imgList = new ArrayList<>();
        String imgHead = "http://ip.zdaye.com";
        Matcher matcher2 = Pattern.compile("<td width=\"35\"><img width=40 height=20 src=(.*?)></td>").matcher(html);
        while (matcher2.find()) {
            String img = matcher2.group();
            img = RegexUtils.getSubUtilSimple(img, "<td width=\"35\"><img width=40 height=20 src=\"(.*?)\"></td>");
            img = img.replace(" ", "%20");
            String fileName = img.substring(img.indexOf("_"), img.indexOf(".")) + ".png";
            img = imgHead + img;
            imgList.add(img);
//            HttpUtils.savePng("", "proxyCode/" + fileName);
            System.out.println(img);
        }

        return result;
    }

    /**
     * CSDN页面转链接
     *
     * @param html
     * @return
     */
    public Map htmlToProxyCsdn(String html) {
        Map<String, String> result = new HashMap<>();
        StringBuffer resultStr = new StringBuffer();
        Matcher matcher = Pattern.compile("<td class='tdleft'><a href='(.*?)' target=_blank>").matcher(html);
        while (matcher.find()) {
            resultStr.append(matcher.group());
        }
        result.put("result", resultStr.toString());
        return result;
    }

    /**
     * 获取xici代理
     *
     * @return
     */
    @RequestMapping("saveProxyXici")
    public String saveProxyXici() {
        ProxyUtils.saveProxyXici(proxyMapper);
        return "获取完成了！";
    }


    /**
     * 获取kuai代理
     *
     * @return
     */
    @RequestMapping("saveProxyKuai")
    public String saveProxyKuai() {
        ProxyUtils.saveProxyKuai(proxyMapper);
        return "获取完成了！";
    }

    /**
     * 导出完美代理
     *
     * @return
     */
    @RequestMapping("exportPerfectProxy")
    public String exportPerfectProxy() {
        List<Proxy> list = proxyMapper.getPerfectList();
        StringBuffer resultStr = new StringBuffer();
        for (Proxy proxy : list) {
            resultStr.append("Proxys.add(new HttpHost(\"" + proxy.getIp() + "\", " + proxy.getPort() + "));\n");
        }
        System.out.println(resultStr.toString());
        return resultStr.toString();
    }

}
