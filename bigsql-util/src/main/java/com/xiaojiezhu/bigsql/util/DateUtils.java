package com.xiaojiezhu.bigsql.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author xiaojie.zhu
 */
public class DateUtils {
    public static final String DEFAULT_PATTERN = "yyyy-MM-dd HH:mm:ss";

    public static String format(Date date, String pattern){
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }

    /**
     * format by default pattern(yyyy-MM-dd HH:mm:ss)
     * @param date
     * @return
     */
    public static String format(Date date){
        return format(date,DEFAULT_PATTERN);
    }
}
