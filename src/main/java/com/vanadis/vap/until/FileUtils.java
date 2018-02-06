package com.vanadis.vap.until;

import java.io.*;

public class FileUtils {

    public static void FileWriter(String path, String content) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path);
            fwriter.write(content);
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
    }

    private static String FileReader(String path) {
        StringBuffer resultStr = new StringBuffer();
        try {
            FileReader fr = new FileReader(path);
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
