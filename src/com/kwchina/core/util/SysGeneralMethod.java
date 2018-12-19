package com.kwchina.core.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.kwchina.oa.sys.SystemConstant;


public class SysGeneralMethod {
    private static final Log logger = LogFactory.getLog(SysGeneralMethod.class);

    /*public static void saveUpFile(String path, FormFile upfile) {
        try {
            InputStream stream = upfile.getInputStream();
            OutputStream bos = new FileOutputStream(path);
            int bytesRead = 0;
            byte[] buffer = new byte[8192];
            while ((bytesRead = stream.read(buffer, 0, 8192)) != -1) {
                bos.write(buffer, 0, bytesRead);
            }

            bos.close();
            stream.close();
            bos = null;
            stream = null;
        } catch (FileNotFoundException ex) {
            logger.error(ex);
        } catch (IOException ex) {
            logger.error(ex);
        }
    }*/

    public static int getStrLength(String txt, String charset) {
        try {
            return txt.getBytes(charset).length;
        } catch (UnsupportedEncodingException ex) {
            return txt.length();
        }
    }

    public static String encodeURL(String url, String charset) {
        if (url != null && url.length() > 0) {
            try {
                return URLEncoder.encode(url, charset);
            } catch (UnsupportedEncodingException ex) {
                return url;
            }
        }
        return url;
    }

    public static void delFile(String filePath) {
        File picFile = new File(filePath);
        if (picFile.exists()) {
            picFile.delete();
        }
        picFile = null;
    }

    public static void cpoyFile(String src, String to) {
        File srcFile = new File(src);
        if (srcFile.exists()) {
            try {
                FileInputStream bw = new FileInputStream(srcFile);
                OutputStream bos = new FileOutputStream(to);
                int bytesRead = 0;
                byte[] buffer = new byte[8192];
                while ((bytesRead = bw.read(buffer, 0, 8192)) != -1) {
                    bos.write(buffer, 0, bytesRead);
                }
                bos.close();
                bw.close();
                bos = null;
                bw = null;
            } catch (FileNotFoundException ex) {
            } catch (IOException ex) {
            }
        }
    }

    public static boolean judgeNullOrBlankStr(String[] validateStr) {
        boolean isNullOrBlank = false;

        for (int i = 0; i < validateStr.length; i++) {
            if (validateStr[i] == null || validateStr.equals("")) {
                isNullOrBlank = true;
                break;
            }
        }

        return isNullOrBlank;
    }


    /*public static String[] getPagesign(MessageResources messages,
                                       HttpServletRequest request) {
        String[] pagessign = {
            messages.getMessage(request.getLocale(), "bbscs.pages.first"),
            messages.getMessage(request.getLocale(), "bbscs.pages.previous"),
            messages.getMessage(request.getLocale(), "bbscs.pages.next"),
            messages.getMessage(request.getLocale(), "bbscs.pages.end")};
        return pagessign;
    }


    public static String[] getPagesign(HttpServletRequest request) {
        String[] pagessign = {
            SystemConstant.MESSAGE.getMessage(request.getLocale(), "bbscs.pages.first"),
            SystemConstant.MESSAGE.getMessage(request.getLocale(), "bbscs.pages.previous"),
            SystemConstant.MESSAGE.getMessage(request.getLocale(), "bbscs.pages.next"),
            SystemConstant.MESSAGE.getMessage(request.getLocale(), "bbscs.pages.end")};
        return pagessign;
    }*/

    public static String[] getPagesign() {
        String[] pagessign = {
            "首页",
            "上一页",
            "下一页",
            "尾页"};
        return pagessign;
    }
    
    public static String[] getPagesign(HttpServletRequest request) {
        String[] pagessign = {
        		"首页",
                "上一页",
                "下一页",
                "尾页"};
        return pagessign;
    }

}
