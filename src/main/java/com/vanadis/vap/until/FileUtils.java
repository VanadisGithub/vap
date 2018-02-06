package com.vanadis.vap.until;

import org.springframework.web.bind.annotation.RequestMapping;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class FileUtils {

    public Map<String, Object> saveArticle(String markdown) {

        String path = "src/main/resources/static/myMarkdown/" + 1 + ".md";

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path);
            fwriter.write(markdown);
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            try {
                fwriter.flush();
                fwriter.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        Map<String, Object> result = new HashMap<>();
        return result;
    }

    @RequestMapping("getArticle")
    private String getArticle(Long articleId) {
        StringBuffer resultStr = new StringBuffer();
        Map<String, Object> result = new HashMap<>();
        try {
            FileReader fr = new FileReader("src/main/resources/static/myMarkdown/" + articleId + ".md");
            BufferedReader br = new BufferedReader(fr);
            String markdown;
            while ((markdown = br.readLine()) != null) {
                resultStr.append(markdown);
                resultStr.append("\r\n");
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return resultStr.toString();
    }
}
