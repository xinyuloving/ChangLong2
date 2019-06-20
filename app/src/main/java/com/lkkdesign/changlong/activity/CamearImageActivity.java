package com.lkkdesign.changlong.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.utils.CustomToast;

import java.io.File;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CamearImageActivity extends AppCompatActivity {

    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;
    @BindView(R.id.iv)
    ImageView iv;

    private String path;
    Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camear_image);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        //        当前项目的缓存路径下  ：/sdcard/Android/data/当前项目的包名/cache/....
        path = getExternalCacheDir().getAbsolutePath() + File.separator + "222.png";
    }

    @OnClick({R.id.btn1, R.id.btn2, R.id.iv})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn1://打开照相机，跳转到拍照的界面
                try {
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);   //拍照界面的隐式意图
                    startActivityForResult(intent, 200);
                } catch (Exception e) {
                    CustomToast.showToast(this, "设备没有摄像头，或者摄像有已损坏");
                }
                break;
            case R.id.btn2: //跳转照相界面，把图片保存到指定路径下，并且回传，显示在imageview
                try {
                    intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
//                向意图对象当中，传入指定的路径
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(path)));
                    startActivityForResult(intent, 300);
                } catch (Exception e) {
                    CustomToast.showToast(this, "设备没有摄像头，或者摄像有已损坏");
                }
                break;
            case R.id.iv:
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == Activity.RESULT_OK) {
            Bundle bundle = data.getExtras();  //bundle 可以存放键值对
            Bitmap bm = (Bitmap) bundle.get("data");
            if (bm != null) {
                iv.setImageBitmap(bm);
            }
        } else if (requestCode == 300 && resultCode == Activity.RESULT_OK) {
//            发现图片在ImageView上无法显示，原因是图片过大导致的，所以要对于图片进行处理。
//            二次采样   对于图片的宽度和高度进行处理，对于图片的质量进行处理

//            1.获取用于设置图片属性的参数
            BitmapFactory.Options options = new BitmapFactory.Options();
//            2.对于属性进行设置，需要解锁边缘
            options.inJustDecodeBounds = true;
//            3.对于图片进行编码处理
            BitmapFactory.decodeFile(path, options);
//            4.获取原来图片的宽度和高度
            int outHeight = options.outHeight;
            int outWidth = options.outWidth;
//            5.200,200  获得要压缩的比例
            int sampleHeight = outHeight / 200;  //2
            int sampleWidth = outWidth / 200;    //1.5
//            6.获取较大的比例
            int size = Math.max(sampleHeight, sampleWidth);
//            7.设置图片压缩的比例
            options.inSampleSize = size;
            /**图片的质量   1个字节是8位
             * ARGB_8888  32位     4字节   100*100*4 = 40000 个字节
             * ARGB_4444  16位     2字节   100*100*2 = 20000 个字节
             * RGB_565    16位      2字节  100*100*2 = 20000 个字节
             * Alpha_8    8位       1字节  100*100*1 = 10000 个字节
             *
             * 100px*100px  的图片
             * */
            options.inPreferredConfig = Bitmap.Config.RGB_565;   //设置图片的质量类型
//            8.锁定边缘
            options.inJustDecodeBounds = false;
            Bitmap bitmap = BitmapFactory.decodeFile(path, options);
            iv.setImageBitmap(bitmap);
        }
    }

    private void jumpToActivity(Class activityClass) {
        intent.setClass(this, activityClass);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        jumpToActivity(Main2Activity.class);
    }

    @Override
    protected void onStop() {
        super.onStop();
        this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        this.finish();
    }
}
