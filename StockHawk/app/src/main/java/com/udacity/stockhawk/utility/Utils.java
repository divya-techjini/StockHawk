package com.udacity.stockhawk.utility;

import java.text.SimpleDateFormat;


public class Utils {

    public static String getFormatedDate(long time,String format){
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(time);
    }
}
