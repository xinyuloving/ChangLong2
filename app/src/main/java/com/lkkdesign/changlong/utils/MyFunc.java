package com.lkkdesign.changlong.utils;

import android.util.Log;

import com.lkkdesign.changlong.config.Constants;

import org.apache.commons.lang3.StringUtils;

import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * @author huangyaoyu
 * 数据转换工具
 */
public class MyFunc {

    //-------------------------------------------------------
    // 判断奇数或偶数，位运算，最后一位是1则为奇数，为0是偶数
    static public int isOdd(int num) {
        return num & 0x1;
    }

    //-------------------------------------------------------
    static public int HexToInt(String inHex)//Hex字符串转int
    {
        return Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public byte HexToByte(String inHex)//Hex字符串转byte
    {
        return (byte) Integer.parseInt(inHex, 16);
    }

    //-------------------------------------------------------
    static public String Byte2Hex(Byte inByte)//1字节转2个Hex字符
    {
        return String.format("%02x", inByte).toUpperCase();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr)//字节数组转转hex字符串
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = inBytArr.length;
        for (int i = 0; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
            strBuilder.append(" ");
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    static public String ByteArrToHex(byte[] inBytArr, int offset, int byteCount)//字节数组转转hex字符串，可选长度
    {
        StringBuilder strBuilder = new StringBuilder();
        int j = byteCount;
        for (int i = offset; i < j; i++) {
            strBuilder.append(Byte2Hex(inBytArr[i]));
        }
        return strBuilder.toString();
    }

    //-------------------------------------------------------
    //转hex字符串转字节数组
    static public byte[] HexToByteArr(String inHex)//hex字符串转字节数组
    {
        int hexlen = inHex.length();
        byte[] result;
        if (isOdd(hexlen) == 1) {//奇数
            hexlen++;
            result = new byte[(hexlen / 2)];
            inHex = "0" + inHex;
        } else {//偶数
            result = new byte[(hexlen / 2)];
        }
        int j = 0;
        for (int i = 0; i < hexlen; i += 2) {
            result[j] = HexToByte(inHex.substring(i, i + 2));
            j++;
        }
        return result;
    }

    /**
     * 字符串转换成十六进制字符串
     *
     * @param str 待转换的ASCII字符串
     * @return String 每个Byte之间空格分隔，如: [61 6C 6B]
     */
    public static String str2HexStr(String str) {

        char[] chars = "0123456789ABCDEF".toCharArray();
        StringBuilder sb = new StringBuilder("");
        byte[] bs = str.getBytes();
        int bit;

        for (int i = 0; i < bs.length; i++) {
            bit = (bs[i] & 0x0f0) >> 4;
            sb.append(chars[bit]);
            bit = bs[i] & 0x0f;
            sb.append(chars[bit]);
            sb.append(' ');
        }
        return sb.toString().trim();
    }

    /**
     * 十六进制转换字符串
     *
     * @param hexStr Byte字符串(Byte之间无分隔符 如:[616C6B])
     * @return String 对应的字符串
     */
    public static String hexStr2Str(String hexStr) {
        String str = "0123456789ABCDEF";
        char[] hexs = hexStr.toCharArray();
        byte[] bytes = new byte[hexStr.length() / 2];
        int n;

        for (int i = 0; i < bytes.length; i++) {
            n = str.indexOf(hexs[2 * i]) * 16;
            n += str.indexOf(hexs[2 * i + 1]);
            bytes[i] = (byte) (n & 0xff);
        }
        return new String(bytes);
    }


    /**
     * 方法用途: 对所有传入参数按照字段名的Unicode码从小到大排序（字典序），并且生成url参数串，排除空字段
     * 实现步骤: <br>
     *
     * @param paraMap    要排序的Map对象
     * @param urlEncode  是否需要URLENCODE
     * @param keyToLower 是否需要将Key转换为全小写 true:key转化成小写，false:不转化
     * @return
     */
    public static String formatUrlMap(Map<String, String> paraMap, boolean urlEncode, boolean keyToLower) {
        String buff = "";
        Map<String, String> tmpMap = paraMap;
        try {
            List<Map.Entry<String, String>> infoIds = new ArrayList<Map.Entry<String, String>>(tmpMap.entrySet());
            // 对所有传入参数按照字段名的 ASCII 码从小到大排序（字典序）
            Collections.sort(infoIds, new Comparator<Map.Entry<String, String>>() {

                @Override
                public int compare(Map.Entry<String, String> o1, Map.Entry<String, String> o2) {
                    return (o1.getKey()).toString().compareTo(o2.getKey());
                }
            });
            // 构造URL 键值对的格式，去除value为空
            StringBuilder buf = new StringBuilder();
            for (Map.Entry<String, String> item : infoIds) {
                if (StringUtils.isNotBlank(item.getKey()) && StringUtils.isNotBlank(item.getValue())) {
                    String key = item.getKey();
                    String val = item.getValue();
                    if (urlEncode) {
                        val = URLEncoder.encode(val, "utf-8");
                    }
                    if (keyToLower) {
                        buf.append(key.toLowerCase() + "=" + val);
                    } else {
                        buf.append(key + "=" + val);
                    }
                    buf.append("&");
                }

            }
            buff = buf.toString();
            if (buff.isEmpty() == false) {
                buff = buff.substring(0, buff.length() - 1);
            }
        } catch (Exception e) {
            return null;
        }
        return buff;
    }


    /**
     * 十六进制求和
     *
     * @param hexdata
     * @return
     */
    public static String hexSum(String hexdata) {
        if (hexdata == null || hexdata.equals("")) {
            return "00";
        }
        hexdata = hexdata.replaceAll(" ", "");
        int total = 0;
        int len = hexdata.length();
        if (len % 2 != 0) {
            return "00";
        }
        int num = 0;
        while (num < len) {
            String s = hexdata.substring(num, num + 2);
            total += Integer.parseInt(s, 16);
            num = num + 2;
        }
        return hexInt(total);
    }

    private static String hexInt(int total) {
        int a = total / 256;
        int b = total % 256;
        if (a > 255) {
            return hexInt(a) + format(b);
        }
        return format(a) + format(b);
    }

    private static String format(int hex) {
        String hexa = Integer.toHexString(hex);
        int len = hexa.length();
        if (len < 2) {
            hexa = "0" + hexa;
        }
        return hexa;
    }

    /**
     * 阿拉伯数字转换成中文
     *
     * @param No
     * @return
     */
    public static String getLocation(int No) {
        int intQuotient = No / 15;//商
        int intRemainder = No % 15;//余数
        return format(intQuotient) + format(intRemainder);
    }

    /**
     * 计算透过率
     *
     * @param floClosed 光源关闭的状态下，由环境光强造成检测器产生的电流经A/D转换后生成的数字，假设为floClosed
     * @param floEmpty  在光路中，放进空样品管，此时，检测器（硅光电池）接收到的光强产生的电流经A/D转换后生成的数字，假设为 floEmpty（此数值可定义为空白值）
     * @param floSample 将装有样品的样品管放进光路，此时检测器接收到的光强产生的电流经A/D转换后生成的数字，假设为floSample
     * @return 计算结果 float
     * 计算方法描述：
     * 举例：假设光源在关闭的情况下，检测器产生的电流数值为0.1微安；
     * 将光源点亮，样品仓放置空样品管的情况下，检测器产生的电流数值为100.1微安，
     * 将空样品管拿出来，将装有样品的样品管放置进样品仓，此时，检测器产生的电流数值为20.1，那么
     * 透过率T=(10.1-0.1)/(100.1-0.1)*100%=10%。
     */
    public static float calculateTransmittance(float floClosed, float floEmpty, float floSample) {
        float floResult = 0.0f;
        float f1 = floSample - floClosed;
        float f2 =floEmpty - floClosed;


        Log.i("MyFunc","floClosed：" + floClosed);
        Log.i("MyFunc","floEmpty：" + floEmpty);
        Log.i("MyFunc","floSample：" + floSample);
        Log.i("MyFunc","f1：" + f1);
        Log.i("MyFunc","f2：" + f2);
//        douResult = (double) (Math.round(d1 / d2 * 100) / 100);//计算结果不正确
//        douResult = (double) d1 / d2 ;//没有保留两个有效数字
        floResult = (float) (Math.round((f1 / f2) * 100) / 100.0);
        Log.i("MyFunc","floResult：" + floResult);
        return floResult;
    }

    /**
     * 计算吸光度
     * 如果透过率T=(Y1-Yd)/(Y0-Yd)*100%
     * 吸光度A=-lgT
     *
     * @param transmittance 透过率
     * @return
     */
    public static float getAbsorbance(float transmittance) {
        double douResult = 0;
        douResult = (-1) * Math.log10(transmittance);
//        douResult = (-1) * Math.round(Math.log10(transmittance) * 100) / 100;
        return Float.parseFloat(Constants.df.format(douResult));
    }
}