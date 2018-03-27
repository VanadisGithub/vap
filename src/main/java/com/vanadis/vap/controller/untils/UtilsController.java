package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.ArticleMapper;
import com.vanadis.vap.model.Result;
import com.vanadis.vap.utils.Base64Utils;
import com.vanadis.vap.utils.RegexUtils;
import com.vanadis.vap.utils.ResultUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

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

}
