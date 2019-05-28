package com.lkkdesign.changlong.activity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.ArrayList;

import io.reactivex.functions.Consumer;


//public abstract class  BaseActivity extends AppCompatActivity implements TextToSpeech.OnInitListener {
public abstract class BaseActivity extends AppCompatActivity {

    //    private TextToSpeech textToSpeech; // TTS对象，谷歌自带语音合成
    private static final String TAG = "RxPermission";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //基类设置默认值,这里是非沉浸,状态栏颜色值#878787,字体颜色为黑色。
//        setStatusColor();
//        setSystemInvadeBlack();
        hideBottomUIMenu();
        //初始化语音合成
//        textToSpeech = new TextToSpeech(this, this); // 参数Context,TextToSpeech.OnInitListener
        //initPermission();

        requestPermissions();
    }


    /**
     * 用来初始化TextToSpeech引擎
     * status:SUCCESS或ERROR这2个值
     * setLanguage设置语言，帮助文档里面写了有22种
     * TextToSpeech.LANG_MISSING_DATA：表示语言的数据丢失。
     * TextToSpeech.LANG_NOT_SUPPORTED:不支持
     */
//    @Override
//    public void onInit(int status) {
////        Toast.makeText(this, "status="+status, Toast.LENGTH_SHORT).show();
//        if (status == TextToSpeech.SUCCESS) {
////            Toast.makeText(this, "合成成功", Toast.LENGTH_SHORT).show();
//            int result = textToSpeech.setLanguage(Locale.CHINA);
//            if (result == TextToSpeech.LANG_MISSING_DATA
//                    || result == TextToSpeech.LANG_NOT_SUPPORTED) {
//                Toast.makeText(this, "语音合成，数据丢失或不支持", Toast.LENGTH_SHORT).show();
//            }
//        }else{
//            Toast.makeText(this, "合成失败", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    public void play(String strInfo){
//        if (textToSpeech != null && !textToSpeech.isSpeaking()) {
//            String strTTS = strInfo.replaceAll("mg/l","豪克每升");
//            // 设置音调，值越大声音越尖（女生），值越小则变成男声,1.0是常规
//            textToSpeech.setPitch(1.0f);
//            //设定语速 ，默认1.0正常语速
//            textToSpeech.setSpeechRate(1.0f);
//            //朗读，注意这里三个参数的added in API level 4   四个参数的added in API level 21
//            textToSpeech.speak(strTTS, TextToSpeech.QUEUE_ADD, null);
//        }
//    }

    // 全屏显示
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus && Build.VERSION.SDK_INT >= 19) {
            View decorView = getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }

    //  下面是android 6.0以上的动态授权
    private void requestPermissions() {
        RxPermissions rxPermission = new RxPermissions(BaseActivity.this);
        rxPermission
                .requestEach(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_CALENDAR,
                        Manifest.permission.READ_CALL_LOG,
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.READ_SMS,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.CAMERA,
                        Manifest.permission.CALL_PHONE,
                        Manifest.permission.SEND_SMS)
                .subscribe(new Consumer<Permission>() {
                    @Override
                    public void accept(Permission permission) throws Exception {
                        if (permission.granted) {
                            // 用户已经同意该权限
                            Log.d(TAG, permission.name + " is granted.");
                        } else if (permission.shouldShowRequestPermissionRationale) {
                            // 用户拒绝了该权限，没有选中『不再询问』（Never ask again）,那么下次再次启动时。还会提示请求权限的对话框
                            Log.d(TAG, permission.name + " is denied. More info should be provided.");
                        } else {
                            // 用户拒绝了该权限，而且选中『不再询问』
                            Log.d(TAG, permission.name + " is denied.");
                        }
                    }
                });


    }

    /**
     * 隐藏虚拟按键，并且全屏
     */
    protected void hideBottomUIMenu() {
        //隐藏虚拟按键，并且全屏
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) { // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY | View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
//        if( textToSpeech != null ){
//            // 停止TextToSpeech 不管是否正在朗读TTS都被打断
//            textToSpeech.stop();
//            //释放 TextToSpeech占用的资源
//            textToSpeech.shutdown();
//        }

    }
}
