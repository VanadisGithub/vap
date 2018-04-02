package com.vanadis.vap.controller.untils;

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
    public @ResponseBody
    Result img(String image, int type) {
        String result = "";
        String fileName = "tesseract/" + System.currentTimeMillis() + ".png";
//        FileUtils.FileWriter(fileName, image);
//        ImgUtils.convert(fileName);
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
        File imageFile = new File(fileName);
        Tesseract instance = new Tesseract();
        if (type == 2) {
            instance.setLanguage("chi_sim");//中文识别
        }
        try {
            result = instance.doOCR(imageFile);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return ResultUtils.success(result);
    }

}
