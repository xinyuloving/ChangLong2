package com.lkkdesign.changlong.config;

/**
 * @author HuangYaoYu
 * @create 2018/10/11
 * @Describe 机器端APP与后台接口地址
 */
public class ApiAddress {

    //    URL：http://url/api/goodsList
    /**
     * 与服务器端连接的协议名
     */
    public static final String PROTOCOL = "http://";
    /**
     * 服务器IP
     */
//    public static final String HOST = "119.23.16.144";
    public static final String HOST = "182.92.219.36:8080";

    public static final String DOMAIN = PROTOCOL + HOST;
    public static final String URL_AsOrders = DOMAIN + "/api/asOrders";//打赏支付接口
    public static final String URL_ExceptionNotice = DOMAIN + "/api/exceptionNotice";//异常通知接口
    public static final String URL_InquireOrders = DOMAIN + "/api/inquireWXOrders";//根据订单号循环查询订单是否成功
    public static final String URL_GetWXORCode = DOMAIN + "/api/getWXORCode";//获取启动抽签的微信二维码
    public static final String URL_GetBlessInfo = DOMAIN + "/api/getBlessInfo";//获取详细解签内容
    public static final String URL_GetBlessNo = DOMAIN + "/api/getBlessNo";//获取详细解签内容
    public static final String URL_BlessRecord = DOMAIN + "/api/blessRecord";//祈福记录通知接口
    public static final String URL_GetMachieGridInfo = DOMAIN + "/api/getMachieGridInfo";//查询求签柜格子祈福签信息
    public static final String URL_GetCodeInfo = DOMAIN + "/api/getCodeInfo";//APP查询扫码、手机状态
    public static final String URL_ExhibitBless = DOMAIN + "/api/exhibitBless";//补签
    public static final String URL_Update = DOMAIN + "/api/update";//升级

}
