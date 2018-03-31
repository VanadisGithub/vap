package com.vanadis.vap.controller.untils;

import com.vanadis.vap.controller.BaseController;
import com.vanadis.vap.model.Proxy;
import com.vanadis.vap.model.ProxyMapper;
import com.vanadis.vap.utils.ProxyUtils;
import com.vanadis.vap.utils.RegexUtils;
import javafx.scene.Parent;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.apache.http.HttpHost;
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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        List<Proxy> list = proxyMapper.getAll();
        String[] urlArr = url.split("/[\n,]/g");
        for (int i = 0; i < urlArr.length; i++) {
            String eUrl = urlArr[i];
            ProxyUtils.doGetWithProxyList(eUrl, list, 100, proxyMapper);
        }
    }


    /**
     * xici页面转代码
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
     * csdn页面转代码
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

    @RequestMapping("test")
    public String test() {
        String source = "d:/m_25e694199ca6b268.dib";
        String result1 = "d:/m_25e694199ca6b268.png";
        convert(source, result1);

        String result = "";
        File imageFile3 = new File(result1);
        Tesseract instance3 = new Tesseract();
        //instance3.setLanguage("chi_sim");//中文识别
        try {
            result += instance3.doOCR(imageFile3);
        } catch (TesseractException e) {
            e.printStackTrace();
        }
        System.out.println(result);
        return result;
    }

    @RequestMapping("img")
    public static void convert(String source, String result) {
        File imageFile3 = new File(source);
        try {
            cleanLinesInImage(imageFile3, "d:/");
        } catch (IOException e) {
            e.printStackTrace();
        }
        String formatName = "png";
        try {
            File f = new File(source);
            f.canRead();
            BufferedImage src = ImageIO.read(f);
            ImageIO.write(src, formatName, new File(result));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void cleanLinesInImage(File sfile, String destDir) throws IOException {
        File destF = new File(destDir);
        if (!destF.exists()) {
            destF.mkdirs();
        }

        BufferedImage bufferedImage = ImageIO.read(sfile);
        int h = bufferedImage.getHeight();
        int w = bufferedImage.getWidth();

        // 灰度化
        int[][] gray = new int[w][h];
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int argb = bufferedImage.getRGB(x, y);
                // 图像加亮（调整亮度识别率非常高）
                int r = (int) (((argb >> 16) & 0xFF) * 1.1 + 30);
                int g = (int) (((argb >> 8) & 0xFF) * 1.1 + 30);
                int b = (int) (((argb >> 0) & 0xFF) * 1.1 + 30);
                if (r >= 255) {
                    r = 255;
                }
                if (g >= 255) {
                    g = 255;
                }
                if (b >= 255) {
                    b = 255;
                }
                gray[x][y] = (int) Math.pow((
                        Math.pow(r, 2.2) * 0.2973
                                + Math.pow(g, 2.2) * 0.6274
                                + Math.pow(b, 2.2) * 0.0753), 1 / 2.2);
            }
        }

        // 二值化
        int threshold = ostu(gray, w, h);
        BufferedImage binaryBufferedImage = new BufferedImage(w, h, BufferedImage.TYPE_BYTE_BINARY);
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                if (gray[x][y] > threshold) {
                    gray[x][y] |= 0x00FFFF;
                } else {
                    gray[x][y] &= 0xFF0000;
                }
                binaryBufferedImage.setRGB(x, y, gray[x][y]);
            }
        }

        //去除干扰线条
        for (int y = 1; y < h - 1; y++) {
            for (int x = 1; x < w - 1; x++) {
                boolean flag = false;
                if (isBlack(binaryBufferedImage.getRGB(x, y))) {
                    int flagNum = 0;
                    //左右均为空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x - 1, y)) && isWhite(binaryBufferedImage.getRGB(x + 1, y))) {
                        flag = true;
                        flagNum += 2;
                    }
                    //上下均为空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x, y + 1)) && isWhite(binaryBufferedImage.getRGB(x, y - 1))) {
                        flag = true;
                        flagNum += 2;
                    }
                    //斜上下为空时，去掉此点
                    if (isWhite(binaryBufferedImage.getRGB(x - 1, y + 1)) && isWhite(binaryBufferedImage.getRGB(x + 1, y - 1))) {
                        flag = true;
                        flagNum += 2;
                    }
                    if (isWhite(binaryBufferedImage.getRGB(x + 1, y + 1)) && isWhite(binaryBufferedImage.getRGB(x - 1, y - 1))) {
                        flag = true;
                        flagNum += 2;
                    }
                    if (flagNum > 3) {
                        binaryBufferedImage.setRGB(x, y, -1);
                    }
                }
            }
        }


        // 矩阵打印
        for (int y = 0; y < h; y++) {
            for (int x = 0; x < w; x++) {
                if (isBlack(binaryBufferedImage.getRGB(x, y))) {
                    System.out.print("*");
                } else {
                    System.out.print(" ");
                }
            }
            System.out.println();
        }

        ImageIO.write(binaryBufferedImage, "jpg", new File(destDir, sfile
                .getName()));
    }

    public static boolean isBlack(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() <= 300) {
            return true;
        }
        return false;
    }

    public static boolean isWhite(int colorInt) {
        Color color = new Color(colorInt);
        if (color.getRed() + color.getGreen() + color.getBlue() > 300) {
            return true;
        }
        return false;
    }

    public static int isBlackOrWhite(int colorInt) {
        if (getColorBright(colorInt) < 30 || getColorBright(colorInt) > 730) {
            return 1;
        }
        return 0;
    }

    public static int getColorBright(int colorInt) {
        Color color = new Color(colorInt);
        return color.getRed() + color.getGreen() + color.getBlue();
    }

    public static int ostu(int[][] gray, int w, int h) {
        int[] histData = new int[w * h];
        // Calculate histogram
        for (int x = 0; x < w; x++) {
            for (int y = 0; y < h; y++) {
                int red = 0xFF & gray[x][y];
                histData[red]++;
            }
        }

        // Total number of pixels
        int total = w * h;

        float sum = 0;
        for (int t = 0; t < 256; t++)
            sum += t * histData[t];

        float sumB = 0;
        int wB = 0;
        int wF = 0;

        float varMax = 0;
        int threshold = 0;

        for (int t = 0; t < 256; t++) {
            wB += histData[t]; // Weight Background
            if (wB == 0)
                continue;

            wF = total - wB; // Weight Foreground
            if (wF == 0)
                break;

            sumB += (float) (t * histData[t]);

            float mB = sumB / wB; // Mean Background
            float mF = (sum - sumB) / wF; // Mean Foreground

            // Calculate Between Class Variance
            float varBetween = (float) wB * (float) wF * (mB - mF) * (mB - mF);

            // Check if new maximum found
            if (varBetween > varMax) {
                varMax = varBetween;
                threshold = t;
            }
        }

        return threshold;
    }

}
