package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.lkkdesign.changlong.R;

import mehdi.sakout.aboutpage.AboutPage;
import mehdi.sakout.aboutpage.Element;

public class AboutActivity extends AppCompatActivity {

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        initData();
    }

    private void initData(){
        View aboutPage = new AboutPage(this)
                .isRTL(false)
                .setImage(R.mipmap.icon_logo)//图片
                .setDescription("长隆科技 家园守望者")//介绍
                .addItem(new Element().setTitle("软件版本 V1.0"))
                .addGroup("与我们联系")
                .addEmail("huangyaoyu@lkkdesign.com")//邮箱
                .addWebsite("http://www.changlongkeji.cn")//网站
//                .addPlayStore("http://www.liusuanyatie.cn")//应用商店
//                .addGitHub("http://www.cl39.com/")//github
                .create();

        setContentView(aboutPage);

    }

    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(AboutActivity.this,Main2Activity.class);
        startActivity(intent);
        AboutActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AboutActivity.this.finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        AboutActivity.this.finish();
    }
}
