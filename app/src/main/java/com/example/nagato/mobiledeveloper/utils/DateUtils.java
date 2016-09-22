package com.example.nagato.mobiledeveloper.utils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

/**
 * Created by nagato on 2016/9/18.
 */
public class DateUtils {
    public final static String DATE_FORMAT_ISO = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);

    public static String format(Date date) {
       String  formatDate=DATE_FORMAT.format(date);
       Pattern pattern =Pattern.compile("\\d{4}\\-\\d{2}\\-\\d{2}");
        if(pattern.matcher(formatDate).matches()){
            String[] strs = formatDate.split("-");
            return strs[0] + "/" + strs[1] + "/" + strs[2];
        }
        return  null;
    }
    public static List<String> getDateBefore(Date endDate, int n){
        ArrayList<String> dateList=new ArrayList<String>();
        Calendar cal=Calendar.getInstance();
        cal.setTime(endDate);
        for(int i=0;i<n;i++){
            cal.add(Calendar.DAY_OF_MONTH,-1);
            dateList.add(format(cal.getTime()));
        }
        if (dateList.contains(format(endDate))){
            dateList.remove(format(endDate));
        }
        return dateList;
    }
    public static List<String> getDateAfter(Date startDate){
        ArrayList<String> dateList=new ArrayList<String>();
        Date today=new Date();
        Calendar cal=Calendar.getInstance();
        cal.setTime(startDate);
        cal.add(Calendar.DAY_OF_MONTH,1);
        while (cal.getTime().before(today)){
            dateList.add(format(cal.getTime()));
            cal.add(Calendar.DAY_OF_MONTH,1);
        }
            dateList.add(format(today));
        if(dateList.contains(format(startDate))){
            dateList.remove(format(startDate));
        }
        return dateList;
    }
}
