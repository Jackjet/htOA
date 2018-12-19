package com.kwchina.oa.purchase.sanfang.utils;

import org.apache.commons.fileupload.FileItem;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by lijj.
 * com.kwchina.oa.purchase.sanfang.utils
 * 2018/7/27 16:51
 *
 * @desc
 */
public class Convert {
    public static Date StrToDate(String dateStr,String format) {
        return getDate(dateStr,format);
    }

    private static Date getDate(String dateStr,String format) {

        DateFormat dateFormat = new SimpleDateFormat(format);
        Date date = null;
        try {
            date = dateFormat.parse(dateStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }


}





