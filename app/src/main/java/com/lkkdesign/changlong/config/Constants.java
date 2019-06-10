package com.lkkdesign.changlong.config;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import okhttp3.MediaType;

//import android.hardware.ChanglongManager;
/**
 * ---------------------------------------------------------------------
 * Copyright © 2018-2020, 深圳洛可可设计工业有限公司, All rights reserved.
 * ---------------------------------------------------------------------
 *
 * @File: Constants.java
 * @Author: admin
 * @Version: V1.0
 * @Create: 2018/2/24 15:21
 * @Description: //基本常量
 * <p>
 * ---------------------------------------------------------------------
 * @Project: ZuoYuoWeiLai
 * ---------------------------------------------------------------------
 */

public final class Constants {


    public static String strLoginName = "";
    public static String strFormActivity = "";

    public static String strWavelength = "";

    private Constants() {
    }

    //倒计时时间长度
    public static int intCountDwonTime = 120000;//默认120秒
    public static int intADTime = 60000;


    public static final String DOMAIN = "http://182.92.219.36:8080";
    public static final String APIURL = "http://182.92.219.36:8080";

    /**
     * 应用上下文名
     */
    public static final String API = "api";

    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    //public static int machineId = 0;//设备编号

    public static final String strKey = "192006250b4c09247ec02edce69f6a2d";

    public static final int intBaudRate_115200 = 115200;//波特率 115200
    public static final int intBaudRate_9600 = 9600;//波特率 9600
    public static final int intBaudRate_4800 = 4800;//波特率 4800
    public static final int intBaudRate_2400 = 2400;//波特率 2400
    public static final String strDoorPort = "/dev/ttyS3";//扫码使用ttyS4

    public static final String strOpenAllDooor = "F11F02000101131FF1";//柜门全开
    public static final String strCloseAllDooor = "F11F03000101141FF1";//柜门全关
    public static final String strOpenLED = "F11F04000101151FF1";//打开LED
    public static final String strColseLED = "F11F05000101161FF1";//关闭LED

    public static Map serialMapData = new HashMap();

    public static DecimalFormat df = new DecimalFormat(".00");

    /**
     * MD5加密
     *
     * @param string
     * @return String 32位 大写
     */
    public static String md5(String string) {
        System.out.println("待加密：" + string);
        byte[] hash;
        try {
            hash = MessageDigest.getInstance("MD5").digest(string.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Huh, MD5 should be supported?", e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Huh, UTF-8 should be supported?", e);
        }

        StringBuilder hex = new StringBuilder(hash.length * 2);
        for (byte b : hash) {
            if ((b & 0xFF) < 0x10)
                hex.append("0");
            hex.append(Integer.toHexString(b & 0xFF));
        }
        System.out.println("加密值：" + hex.toString().toUpperCase());
        return hex.toString().toUpperCase();
    }

    public static String convertShipError(String returnCode) {
        String strCardState = null;
        switch (returnCode) {
            case "01":
                strCardState = "电机到达目标层时错误，可能是电机堵转或者限位出错";
                break;
            case "02":
                strCardState = "到达出口过程中，层电机处理错误，可能是电机堵死";
                break;
            case "03":
                strCardState = "货物推出时错误，可能是电机堵转或者程序问题";
                break;
            case "04":
                strCardState = "等待取货时发生错误，可能是红外检测问题";
                break;
            case "05":
                strCardState = "表示门口有货物没有取走";
                break;
            case "06":
                strCardState = "电机故障或开门卡顿";
                break;
            default:
                break;
        }
        return strCardState;
    }

   public static String convertSearch(String strItem) {
        String strSearch = null;
        switch (strItem) {
            case "执法检测":
                strSearch = "style";
                break;
            case "应急监测":
                strSearch = "style";
                break;
            case "自行监测":
                strSearch = "style";
                break;
            case "条目":
                strSearch = "item";
                break;
            case "名称":
                strSearch = "name";
                break;
            case "波长":
                strSearch = "wavelength";
                break;
            case "密度":
                strSearch = "density";
                break;
            case "透过率":
                strSearch = "tranatre";
                break;
            case "吸光度":
                strSearch = "absorbance";
                break;
            case "操作员ID":
                strSearch = "userId";
                break;
            case "曲线方程":
                strSearch = "type";
                break;
            case "测量结果":
                strSearch = "result";
                break;
            case "温度":
                strSearch = "temperature";
                break;
            case "测点名称":
                strSearch = "measure_name";
                break;
            case "单位名称":
                strSearch = "entity_name";
                break;
            case "取样时间":
                strSearch = "sampling_time";
                break;
            case "采样员":
                strSearch = "sampler";
                break;
            case "检测员":
                strSearch = "inspector";
                break;
            case "备注":
                strSearch = "mark";
                break;
            default:
                strSearch = "noColumn";
                break;
        }
        return strSearch;
    }

    public static String[] chars = new String[]{"a", "b", "c", "d", "e", "f", "g", "h", "i", "j", "k", "l", "m", "n", "o", "p", "q", "r",
            "s", "t", "u", "v", "w", "x", "y", "z", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "A", "B", "C", "D", "E", "F", "G",
            "H", "I", "J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z"};


    /**
     * 判断是否是手机号
     *
     * @param mobile
     * @return
     */
    public static boolean isMobile(String mobile) {
        String regex = "^((13[0-9])|(14[5,7])|(15[0-3,5-9])|(17[0,3,5-8])|(18[0-9])|166|198|199|(147))\\d{8}$";
        Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher m = p.matcher(mobile);
        return m.matches();
    }


}
