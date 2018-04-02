package com.vanadis.vap.utils;

import java.io.*;

public class FileUtils {

    public static void uploadFile(byte[] file, String filePath) {
        File targetFile = new File(filePath);
        if (!targetFile.exists()) {
            targetFile.mkdirs();
        }
        try {
            FileOutputStream out = new FileOutputStream(filePath);
            out.write(file);
            out.flush();
            out.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

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

    public static void FileWriter(String path, String content, boolean isOld) {

        FileWriter fwriter = null;
        try {
            fwriter = new FileWriter(path, isOld);
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

    public static String FileReader(String path) {
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

    public static String FileReaderWithEncode(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, "GBK");
        BufferedReader br = new BufferedReader(isr);

        String s = null;
        StringBuilder file = new StringBuilder();
        while ((s = br.readLine()) != null) {
            file.append(s);
        }
        br.close();
        isr.close();
        fis.close();
        return file.toString();
    }

    public static String FileReaderWithEncode2(String path) throws IOException {

        FileInputStream fis = new FileInputStream(path);
        InputStreamReader isr = new InputStreamReader(fis, "GBK");
        BufferedReader br = new BufferedReader(isr);

        String s = null;
        while ((s = br.readLine()) != null) {
            System.out.println("put(\"" + s + "\")");
        }
        br.close();
        isr.close();
        fis.close();
        return s;
    }
}
