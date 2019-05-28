//package com.lkkdesign.changlong.base;
//
//import android.content.Context;
//import android.content.Intent;
//import android.util.Log;
//import android.widget.Toast;
//
//import com.lkkdesign.changlong.R;
//
//import android.hardware.ChanglongManager;
//
//import com.lkkdesign.changlong.activity.MainActivity;
//import com.lkkdesign.changlong.bean.ComBean;
//import com.lkkdesign.changlong.config.Constants;
//import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
//import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
//import com.nostra13.universalimageloader.cache.memory.impl.UsingFreqLimitedMemoryCache;
//import com.nostra13.universalimageloader.core.ImageLoader;
//import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
//import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
//import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
//import com.nostra13.universalimageloader.utils.StorageUtils;
//import com.squareup.leakcanary.LeakCanary;
//
//import java.io.File;
//import java.io.IOException;
//import java.security.InvalidParameterException;
//import java.util.LinkedList;
//import java.util.Locale;
//import java.util.Queue;
//
//import static com.lkkdesign.changlong.utils.MyFunc.ByteArrToHex;
//
//public class CLApplication extends android.app.Application {
//
//    private static CLApplication instance;
//    private ChanglongManager mClManager = null;
//    private boolean mLightState = false;
////    private boolean stopThread = false;
////    SerialControl serialControl;
////    public static SerialControl ComDoor;//扫打开柜门
////    public DispQueueThread DispQueue;//刷新显示线程
////    DataDAO dataDAO = new DataDAO(this);
////    MeasureDao measureDao = new MeasureDao(this);
//
////    public static TextToSpeech textToSpeech; // TTS对象
//
//    @Override
//    public void onCreate() {
//        super.onCreate();
//        //mClManager = (ChanglongManager) getSystemService(Context.CHANGLONG_SERVICE);
//        instance = this;
//        // 程序崩溃时触发线程  以下用来捕获程序崩溃异常
//        //Thread.setDefaultUncaughtExceptionHandler(handler);
//
////        initImageLoader(this);
////        initSerialConrrol();//初始化串口
//        //leakcanary
////        if (LeakCanary.isInAnalyzerProcess(this)) {
////            // This process is dedicated to LeakCanary for heap analysis.
////            // You should not init your app in this process.
////            return;
////        }
////        LeakCanary.install(this);
//        // Normal app init code...
//
//        //初始化语音合成
////        textToSpeech = new TextToSpeech(this, this); // 参数Context,TextToSpeech.OnInitListener
//
//    }
//
//    /**
//     * @param
//     * @author huangyaoyu
//     * @time 2019/5/20  14:58
//     * @describe 打开所有二极管
//     */
//    private void openAllLight() {
//        mClManager.setLedAllOn();
//    }
//
//
//    /**
//     * @param
//     * @author huangyaoyu
//     * @time 2019/5/20  14:59
//     * @describe 关闭所有二极管
//     */
//    private void closedAllLight() {
//        mClManager.setLedAllOff();
//    }
//
//
//    /**
//     * @param wavelength
//     * @author huangyaoyu
//     * @time 2019/5/20  15:02
//     * @describe 根据传入参数，打开特定的二极管
//     */
//    private void openTheDesignationLight(int wavelength) {
//        //波长：420、440、540、620、700nm
//        switch (wavelength) {
//            case 420:
//                mClManager.setLed420nmOnOff(true);
//                break;
//            case 440:
//                mClManager.setLed440nmOnOff(true);
//                break;
//            case 540:
//                mClManager.setLed540nmOnOff(true);
//                break;
//            case 620:
//                mClManager.setLed620nmOnOff(true);
//                break;
//            case 700:
//                mClManager.setLed700nmOnOff(true);
//                break;
//            default:
//                System.out.println("未知波长");
//        }
//    }
//
//    /**
//     * @author huangyaoyu
//     * @time 2019/5/20  15:26
//     * @param
//     * @describe
//     */
//    private int getTheMinValue() {
//        int val_420 = mClManager.getLed420nmChannelValue();
//        int val_440 = mClManager.getLed440nmChannelValue();
//        int val_540 = mClManager.getLed540nmChannelValue();
//        int val_620 = mClManager.getLed620nmChannelValue();
//        int val_700 = mClManager.getLed700nmChannelValue();
//        //定义数组并初始化
//        int[] arr = new int[]{val_420, val_440, val_540, val_620, val_700};
//        int intMax = arr[0];//将数组的第一个元素赋给max
//        int intMin = arr[0];//将数组的第一个元素赋给min
//        for (int i = 1; i < arr.length; i++) {//从数组的第二个元素开始赋值，依次比较
//            if (arr[i] > intMax) {//如果arr[i]大于最大值，就将arr[i]赋给最大值
//                intMax = arr[i];
//            }
//            if (arr[i] < intMin) {//如果arr[i]小于最小值，就将arr[i]赋给最小值
//                intMin = arr[i];
//            }
//        }
//        return intMin;
//    }
//
//    /**
//     * 用来初始化TextToSpeech引擎
//     * status:SUCCESS或ERROR这2个值
//     * setLanguage设置语言，帮助文档里面写了有22种
//     * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
//     * TextToSpeech.LANG_NOT_SUPPORTED:不支持
//     */
////    @Override
////    public void onInit(int status) {
////        Toast.makeText(this, "status="+status, Toast.LENGTH_SHORT).show();
////        if (status == TextToSpeech.SUCCESS) {
////            Toast.makeText(this, "合成成功", Toast.LENGTH_SHORT).show();
////            int result = textToSpeech.setLanguage(Locale.CHINA);
////            if (result == TextToSpeech.LANG_MISSING_DATA
////                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
////                Toast.makeText(this, "数据丢失或不支持", Toast.LENGTH_SHORT).show();
////            }
////        }else{
////            Toast.makeText(this, "合成失败", Toast.LENGTH_SHORT).show();
////        }
////    }
////
////    public static void play(String strInfo){
////        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
////            String strTTS = strInfo.replaceAll("mg/l","豪克每升");
////            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
////            textToSpeech.setPitch(1.0f);
////            //设定语速 ，默认1.0正常语速
////            textToSpeech.setSpeechRate(1.0f);
////            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
////            textToSpeech.speak(strTTS, TextToSpeech.QUEUE_FLUSH, null);
////        }
////    }
//
////    private void initSerialConrrol(){
////        DispQueue = new DispQueueThread();
////        DispQueue.start();
////        ComDoor = new SerialControl();//柜门串口
////        openDoorPort();//打开串口
////    }
//
//    //加载图片
////    private static void initImageLoader(Context context) {
////        //缓存文件的文件夹
////        File cacheDir = StorageUtils.getOwnCacheDirectory(context, "universalimageloader/Cache");
////        //File cacheDir2 = new File(Environment.getExternalStorageDirectory() + "/LKKApp/imgCache");
////        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
////                .memoryCacheExtraOptions(480, 800) // max width, max height，即保存的每一个缓存文件的最大长宽
////                .threadPoolSize(3) //线程池内线程的数量
////                .threadPriority(Thread.NORM_PRIORITY - 2)
////                .denyCacheImageMultipleSizesInMemory()
////                .diskCacheFileNameGenerator(new Md5FileNameGenerator()) //将保存的时候的URI名称用MD5 加密
////                .memoryCache(new UsingFreqLimitedMemoryCache(2 * 1024 * 1024))
////                .memoryCacheSize(2 * 1024 * 1024) // 内存缓存的最大值
////                .diskCacheSize(50 * 1024 * 1024)  // SD卡缓存的最大值
////                .tasksProcessingOrder(QueueProcessingType.LIFO)
////                // 由原先的discCache -> diskCache
////                .diskCache(new UnlimitedDiskCache(cacheDir))//自己定义缓存路径
////                // 自定义缓存路径
////                .imageDownloader(new BaseImageDownloader(context, 5 * 1000, 30 * 1000)) // connectTimeout (5 s), readTimeout (30 s)超时时间
////                .writeDebugLogs() // Remove for release app
////                .build();
////        //全局初始化此配置
////        ImageLoader.getInstance().init(config);
////    }
//
//    //监听异常，自动重启APP
//    private Thread.UncaughtExceptionHandler handler = new Thread.UncaughtExceptionHandler() {
//        @Override
//        public void uncaughtException(Thread t, Throwable e) {
//            restartApp(); //发生崩溃异常时,重启应用
//        }
//    };
//
//    private void restartApp() {
//
//        Intent intent = new Intent(instance, MainActivity.class);
//        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//        instance.startActivity(intent);
//        android.os.Process.killProcess(android.os.Process.myPid());  //结束进程之前可以把你程序的注销或者退出代码放在这段代码之前
//
//    }
//
////    //打开柜门串口
////    public void openDoorPort() {
////        //柜门
////        ComDoor.setPort(Constants.strDoorPort);
////        ComDoor.setBaudRate(Constants.intBaudRate_9600);
////        OpenComPort(ComDoor);
////
////    }
//
//    // ----------------------------------------------------串口控制类
////    public class SerialControl extends SerialHelper {
////
////        public SerialControl() {
////        }
////
////        @Override
////        protected void onDataReceived(final ComBean ComRecData) {
////            //数据接收量大或接收时弹出软键盘，界面会卡顿,可能和6410的显示性能有关
////            //直接刷新显示，接收数据量大时，卡顿明显，但接收与显示同步。
////            //用线程定时刷新显示可以获得较流畅的显示效果，但是接收数据速度快于显示速度时，显示会滞后。
////            //最终效果差不多-_-，线程定时刷新稍好一些。
////            DispQueue.AddQueue(ComRecData);//线程定时刷新显示(推荐)
////            /*
////            runOnUiThread(new Runnable()//直接刷新显示
////			{
////				public void run()
////				{
////					DispRecData(ComRecData);
////				}
////			});*/
////        }
////    }
////
////    //----------------------------------------------------刷新显示线程
////    private class DispQueueThread extends Thread {
////        private Queue<ComBean> QueueList = new LinkedList<ComBean>();
////
////        @Override
////        public void run() {
////            super.run();
////            //while (!isInterrupted()) {
////            while (!stopThread) {
////                final ComBean ComData;
////                while ((ComData = QueueList.poll()) != null) {
////                    DispRecData(ComData);
//////                    runOnUiThread(new Runnable() {
//////                        public void run() {
//////                            DispRecData(ComData);
//////                        }
//////                    });
////                    try {
////                        Thread.sleep(100);//默认100 显示性能高的话，可以把此数值调小。
////                    } catch (Exception e) {
////                        e.printStackTrace();
////                    }
////                    break;
////                }
////            }
////        }
////
////        public synchronized void AddQueue(ComBean ComData) {
////            QueueList.add(ComData);
////        }
////    }
//
////    //接收串口返回的数据
////    public void DispRecData(ComBean ComRecData) {
////        StringBuilder sMsg = new StringBuilder();
////        sMsg.append(ComRecData.sRecTime);
////        sMsg.append("[");
////        sMsg.append(ComRecData.sComPort);
////        sMsg.append("]");
////        sMsg.append("[Hex] ");
////        String strData = new String(ComRecData.bRec);
////        sMsg.append(strData);
////        //Log.i("RData", "接收的数据1：" + new String(ComRecData.bRec));
////        Log.i("RData", "接收的数据：" + ByteArrToHex(ComRecData.bRec));
////
////        if ("/dev/ttyS3".equals(ComRecData.sComPort)) {//柜门 /dev/ttyS3返回的数据
////            Log.i("APPData", "串口数据：" + ByteArrToHex(ComRecData.bRec));
////            Constants.serialMapData.put("ttyS3", ByteArrToHex(ComRecData.bRec));
////            //searchBottleCode(strODCode);
////        } else {
////
////        }
////    }
////
////
////    //----------------------------------------------------设置自动发送延时
////    public void setDelayTime(String sTime) {
////        SetiDelayTime(serialControl, sTime);
////    }
////
////    //----------------------------------------------------设置自动发送数据
////    public void setSendData(String strSendPortData) {
////        SetLoopData(serialControl, strSendPortData);
////    }
////
////    //----------------------------------------------------设置自动发送延时
////    public void SetiDelayTime(SerialHelper ComPort, String sTime) {
////        ComPort.setiDelay(Integer.parseInt(sTime));
////    }
////
////    //----------------------------------------------------设置自动发送数据
////    public void SetLoopData(SerialHelper ComPort, String sLoopData) {
////
////        ComPort.setHexLoopData(sLoopData);
////    }
////
////    //----------------------------------------------------设置自动发送模式开关
////    public void SetAutoSend(SerialHelper ComPort, boolean isAutoSend) {
////        if (isAutoSend) {
////            ComPort.startSend();
////        } else {
////            ComPort.stopSend();
////        }
////    }
////
////    //---------------------------------------------------
////    // -串口发送十六进制数据
////    public static void sendPortData_Hex(SerialHelper ComPort, String sOut) {
////        System.out.println("发送十六进制：" + sOut);
////        if (ComPort != null && ComPort.isOpen()) {
////            ComPort.sendHex(sOut);
////        }
////    }
////
////    //---------------------------------------------------
////    // -串口发送文本
////    public void sendPortData_Text(SerialHelper ComPort, String sOut) {
////        System.out.println("发送文本：" + sOut);
////        if (ComPort != null && ComPort.isOpen()) {
////            ComPort.sendTxt(sOut);
////        }
////    }
////
////    //----------------------------------------------------关闭串口
////    public void CloseComPort(SerialHelper ComPort) {
////        if (ComPort != null) {
////            ComPort.stopSend();
////            ComPort.close();
////        }
////    }
////
////    //----------------------------------------------------打开串口
////    public void OpenComPort(SerialHelper ComPort) {
////        try {
////            ComPort.open();
////            //ShowMessage(" 串口打开成功!");
////        } catch (SecurityException e) {
////            ShowMessage("打开串口失败:没有串口读/写权限!");
////        } catch (IOException e) {
////            ShowMessage("打开串口失败:未知错误!");
////        } catch (InvalidParameterException e) {
////            ShowMessage("打开串口失败:参数错误!");
////        }
////    }
////
////    //------------------------------------------显示消息
////    public void ShowMessage(String sMsg) {
////        Toast.makeText(this, sMsg, Toast.LENGTH_SHORT).show();
////        //CustomToast.showToast(getApplication(), sMsg);
////    }
//
////    //保存曲线测量结果
////    public static void saveMeasureData(String classic, String item, String name, int wavelength, float density,
////                                float tranatre, float absorbance, String userId, String type, String result,
////                                String temperature, String time, String mark) {
////        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,"String classic", "String item", "String name", 140, 10.4f,
////        1.20f, 100.6f, "String userId", "String type", "String result",
////                "String temperature", "String time", "String mark");
////        measureDao.add(tb_measure);
////        // 信息提示
////        CustomToast.showToast(getApplicationContext(), "数据保存成功");
////    }
//
//}
