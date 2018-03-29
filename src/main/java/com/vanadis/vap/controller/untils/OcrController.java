package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@RestController
@RequestMapping("ocr")
public class OcrController extends BaseController {

    @RequestMapping("")
    public ModelAndView index() {
        ModelAndView modelAndView = new ModelAndView("/weixin");
        return modelAndView;
    }

//    @RequestMapping("test")
//    public void test() throws TesseractException {
//        File imageFile = new File("d:/test.png");
//        Tesseract instance = new Tesseract();
//
//        //将验证码图片的内容识别为字符串
//        String result = instance.doOCR(imageFile);
//        System.out.println(result);
//    }
}
