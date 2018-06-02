package com.vanadis.vap.controller.untils;

import com.alibaba.fastjson.JSONObject;
import com.vanadis.vap.Api.TenXunAIApi;
import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.*;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import sun.misc.BASE64Decoder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.Charset;

import static com.vanadis.vap.utils.ImgUtils.dataType.CHI;
import static com.vanadis.vap.utils.ImgUtils.dataType.ENG;

@RestController
@RequestMapping("utils")
public class UtilsController extends BaseController {

    @Autowired
    private ArticleMapper articleMapper;

    @RequestMapping("")
    public ModelAndView utils() {
        ModelAndView modelAndView = new ModelAndView("/utils");
        return modelAndView;
    }

    @RequestMapping("base64")
    public Result base64(String str, int type) {
        String resultStr = null;
        if (type == 1) {
            resultStr = Base64Utils.encode(str);
        } else {
            resultStr = Base64Utils.decode(str);
        }
        return ResultUtils.success(resultStr);
    }

    @RequestMapping("img")
    public Result img(String image, String type) {
        String fileName = "tesseract/" + System.currentTimeMillis() + ".png";
        image = image.substring(image.indexOf(",") + 1);
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] decodedBytes = decoder.decodeBuffer(image);
            FileOutputStream out = new FileOutputStream(fileName);
            out.write(decodedBytes);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        String result;
        if ("chi".equals(type)) {
            result = ImgUtils.tessercatImg(fileName, CHI);
        } else {
            result = ImgUtils.tessercatImg(fileName, ENG);
        }
        return ResultUtils.success(result);
    }

    @RequestMapping("texttrans")
    public Result texttrans(String text, int type) {
        return ResultUtils.success(TenXunAIApi.texttransApi(text, type));
    }

    @RequestMapping("cloudWord")
    public Result cloudWord(String text) throws UnsupportedEncodingException {
        text = "图悦-在线词频分析工具-词云图制作软件";
        String utf8 = new String(text.getBytes( "UTF-8"));
        System.out.println(utf8);
        String unicode = new String(utf8.getBytes(),"UTF-8");
        System.out.println(unicode);
        String gbk = new String(unicode.getBytes("GBK"));
        JSONObject obj = TenXunAIApi.wordsegApi("%E7%94%B1%E8%85%BE%E8%AE%AF%E5%9F%BA%E9%87%91%E4%BC%9A%E5%8F%91%E8%B5%B7%E7%9A%84%E4%B8%AD%E5%9B%BD%E4%BA%92%E8%81%94%E7%BD%91%E5%85%AC%E7%9B%8A%E5%B3%B0%E4%BC%9A%E5%9C%A8%E6%9C%AC%E6%9C%88%E4%B8%AD%E6%97%AC%E4%BA%8E%E5%8C%97%E4%BA%AC%E4%B8%BE%E8%A1%8C%EF%BC%8C%E5%B3%B0%E4%BC%9A%E7%9A%84%E4%B8%BB%E9%A2%98%E6%98%AF%EF%BC%9A%E4%BA%92%E8%81%94%E7%BD%91%E6%94%B9%E5%8F%98%E5%85%AC%E7%9B%8A%E3%80%82%E4%BC%9A%E4%B8%8A%E9%99%88%E4%B8%80%E4%B8%B9%E5%AE%A3%E5%B8%83%EF%BC%8C%E8%85%BE%E8%AE%AF%E5%B0%86%E5%87%BA20%E4%BA%BF%E8%B5%84%E6%BA%90%E5%8A%A9%E5%8A%9B%E5%85%AC%E7%9B%8A%E7%94%9F%E6%80%81%E3%80%82");
        return ResultUtils.success(obj);
    }
}
