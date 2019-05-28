//package com.lkkdesign.changlong.activity;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
//import android.view.View;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.lkkdesign.changlong.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//
///**
// * 光度计，测量页面
// */
//public class PhotometerMeasuringActivity extends BaseActivity {
//
//
//    @BindView(R.id.iv_return)
//    ImageView ivReturn;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.linearlayout)
//    LinearLayout linearlayout;
//    @BindView(R.id.tv_show)
//    TextView tvShow;
//    @BindView(R.id.btn_blank)
//    Button btnBlank;
//    @BindView(R.id.btn_measure)
//    Button btnMeasure;
//    private String strTitle = "";
//
//    private Intent intent = new Intent();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_photometer_measuring);
//        //绑定初始化ButterKnife
//        ButterKnife.bind(this);
//
//        initView();
//    }
//
//    private void initView(){
//
//        Intent intent = getIntent();
//        strTitle = intent.getStringExtra("wavelength");
//        tvTitle.setText(strTitle);
//
//    }
//
//    @OnClick({R.id.iv_return,R.id.tv_title, R.id.linearlayout, R.id.tv_show, R.id.btn_blank, R.id.btn_measure})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_return:
//                intent.setClass(this,PhotometerActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_title:
//                break;
//            case R.id.linearlayout:
//                break;
//            case R.id.tv_show:
//                break;
//            case R.id.btn_blank:
////                play("请放入空样品管后\n点击空白按键");
//                tvShow.setText(R.string.tv_celiang);
//                btnMeasure.setVisibility(View.VISIBLE);//显示测量按钮
//                btnBlank.setVisibility(View.INVISIBLE);//隐藏空白按钮
//
//                break;
//            case R.id.btn_measure:
////                play(getResources().getString(R.string.tv_celiang));//语音播放
////                play("测量结束");
//                tvShow.setText("吸光度 A=0.000\n" +
//                        "透过率（T）：100%\n" +
//                        "电流：0.0 μA\n" +
//                        "电压：0.000 mV\n" +
//                        "温度:25℃\n");
//                break;
//        }
//    }
//
//    @Override
//    protected void onStop() {
//        super.onStop();
//        this.finish();
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//        this.finish();
//    }
//}
