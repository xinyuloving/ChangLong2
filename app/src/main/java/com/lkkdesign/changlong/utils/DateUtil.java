package com.lkkdesign.changlong.utils;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.widget.DatePicker;
import android.widget.TimePicker;

import com.allen.library.SuperTextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Created by huangyaoyu on 2018/6/1.
 * description:格式化时间
 */
public class DateUtil {

    private SuperTextView superTextView;

    /**
     * 按 yyyy-MM-dd HH:mm:ss 格式化时间.
     */
    public static final SimpleDateFormat DATE_TIME_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat DATE_TIME_FORMAT2 = new SimpleDateFormat("yyyy年MM月dd日");
    public static final SimpleDateFormat DATE_TIME_FORMAT3 = new SimpleDateFormat("yyyyMMddHHmmss");
    public static final SimpleDateFormat DATE_TIME_FORMAT4 = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat DATE_TIME_FORMAT5=new SimpleDateFormat("yyyy年MM月dd日 HH:mm");

    public static long intCountDwonTime=120000;

    public static String getDate(){
        java.util.Date tdate = new java.util.Date();
        return DateUtil.DATE_TIME_FORMAT2.format(tdate);
    }

    public static String getNowDateTime() {
        java.util.Date tdate = new java.util.Date();
        return DateUtil.DATE_TIME_FORMAT.format(tdate);
    }
    public static String getNowDateTime2() {
        java.util.Date tdate = new java.util.Date();
        return DateUtil.DATE_TIME_FORMAT3.format(tdate);
    }
    public static String getNowDateTime4() {
        java.util.Date tdate = new java.util.Date();
        return DateUtil.DATE_TIME_FORMAT4.format(tdate);
    }

    public static String formatDateToMD(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("MM-dd");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }

    public static String formatDateToYMDHMS(String str) {
        SimpleDateFormat sf1 = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat sf2 = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
        String formatStr = "";
        try {
            formatStr = sf2.format(sf1.parse(str));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formatStr;
    }


    public static Long getDateTime(String strStart, String strEnd) {
        long diff = 0l;
        System.out.println("strStart:" + strStart);
        System.out.println("strEnd:" + strEnd);
        try {
            DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");

            Date startTime = df.parse(strStart);
            Date endTime = df.parse(strEnd);

            System.out.println("startTime:" + startTime);
            System.out.println("endTime:" + endTime);
            System.out.println("startTime.getTime():" + startTime.getTime());
            System.out.println("endTime.getTime():" + endTime.getTime());

//            diff = startTime.getTime() - endTime.getTime();
            diff =endTime.getTime() - startTime.getTime();
            System.out.println("diff:" + diff);
            long days = diff / (1000 * 60 * 60 * 24);
            long hours = (diff - days * (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
            long minutes = (diff - days * (1000 * 60 * 60 * 24) - hours * (1000 * 60 * 60)) / (1000 * 60);

        } catch (Exception e) {
        }
        return diff;
    }

    public static void showDatePickerDialog(Activity activity, int themeResId, final SuperTextView tv, Calendar calendar) {
        // 直接创建一个DatePickerDialog对话框实例，并将它显示出来
        new DatePickerDialog(activity
                , themeResId
                // 绑定监听器(How the parent is notified that the date is set.)
                , new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year,
                                  int monthOfYear, int dayOfMonth) {
                monthOfYear += 1;
                // 此处得到选择的时间，可以进行你想要的操作
                tv.setLeftString( year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }
                // 设置初始日期
                , calendar.get(Calendar.YEAR)
                , calendar.get(Calendar.MONTH)
                , calendar.get(Calendar.DAY_OF_MONTH)).show();
    }

    public static void showTimePickerDialog(Activity activity, final SuperTextView tv, Calendar calendar) {
        // Calendar c = Calendar.getInstance();
        // 创建一个TimePickerDialog实例，并把它显示出来
        // 解释一哈，Activity是context的子类
        new TimePickerDialog(activity,
                // 绑定监听器
                new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        String sHour = hourOfDay < 10 ? "0" + hourOfDay : "" + hourOfDay;
                        String sMinute = minute < 10 ? "0" + minute : "" + minute;
                        tv.setCenterString(sHour + ":" + sMinute);
                    }
                }
                // 设置初始时间
                , calendar.get(Calendar.HOUR_OF_DAY)
                , calendar.get(Calendar.MINUTE)
                // true表示采用24小时制
                , true).show();
    }


}
