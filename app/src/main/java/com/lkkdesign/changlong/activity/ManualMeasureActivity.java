//package com.lkkdesign.changlong.activity;
//
//import android.content.Intent;
//import android.support.annotation.NonNull;
//import android.support.v7.app.AppCompatActivity;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.ImageView;
//import android.widget.LinearLayout;
//import android.widget.TextView;
//
//import com.afollestad.materialdialogs.DialogAction;
//import com.afollestad.materialdialogs.MaterialDialog;
//import com.lkkdesign.changlong.R;
//
//import butterknife.BindView;
//import butterknife.ButterKnife;
//import butterknife.OnClick;
//import mehdi.sakout.fancybuttons.FancyButton;
//
///**
// * 手动测量：测量、结果界面
// */
//public class ManualMeasureActivity extends BaseActivity {
//
//    @BindView(R.id.iv_return)
//    ImageView ivReturn;
//    @BindView(R.id.tv_title)
//    TextView tvTitle;
//    @BindView(R.id.linearlayout)
//    LinearLayout linearlayout;
//    @BindView(R.id.tv_show)
//    TextView tvShow;
//    @BindView(R.id.tv_result)
//    TextView tvResult;
//    @BindView(R.id.btn_blank)
//    FancyButton btnBlank;
//    @BindView(R.id.btn_measure)
//    FancyButton btnMeasure;
//    @BindView(R.id.btn_calculate)
//    FancyButton btnCalculate;
//    @BindView(R.id.btn_save)
//    FancyButton btnSave;
//
//    private String strTitle = "";
//    private String strInfo = "C=15.000 mg/L\n\n" +"透过率（T）：10%\n" +
//            "吸光度（A）：1.0\n" +
//            "波长（λ）：700 nm\n" +
//            "温度:25℃\n";
//    private Intent intent = new Intent();
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_manual_measure);
//
//        //绑定初始化ButterKnife
//        ButterKnife.bind(this);
//
//        initView();
//    }
//
//    private void initView() {
//
//        Intent intent = getIntent();
//        strTitle = intent.getStringExtra("wavelength");
//        tvTitle.setText(strTitle);
//        tvShow.setText(R.string.tv_show);
//    }
//
//    @OnClick({R.id.iv_return, R.id.tv_title, R.id.linearlayout, R.id.tv_show, R.id.btn_blank, R.id.btn_measure, R.id.btn_calculate, R.id.btn_save})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_return:
//                intent.setClass(this,CurveSelectActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.tv_title:
//                break;
//            case R.id.linearlayout:
//                break;
//            case R.id.tv_show:
//                break;
//            case R.id.btn_blank://空白按键
////                tvShow.setText(R.string.tv_show);
////                play(getResources().getString(R.string.tv_show));
//                tvShow.setText(R.string.tv_celiang);
////                play(getResources().getString(R.string.tv_celiang));
//                break;
//            case R.id.btn_measure:
//                tvShow.setText(strInfo);
//                tvResult.setVisibility(View.INVISIBLE);
//                tvResult.setText("");
////                play("测量已结束");
//                break;
//            case R.id.btn_calculate:
//                tvResult.setVisibility(View.VISIBLE);
//                tvResult.setText("计算结果：\nC=kA+b\nR2=99.99%");
////                play("结果已计算完毕");
//                break;
//            case R.id.btn_save:
//                showData(strInfo);
//                break;
//        }
//    }
//
//    private void showData(String string){
//        //当接收到Click事件之后触发
//        new MaterialDialog.Builder(ManualMeasureActivity.this)// 初始化建造者
//                .icon(getResources().getDrawable(R.mipmap.icon_save))
//                .title(strTitle)// 标题
//                .content("是否保存当前曲线？\n\n"+string)// 内容
//                .positiveText(R.string.save)
//                .negativeText(R.string.cancel)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        tvShow.setText("");
//                        tvResult.setVisibility(View.INVISIBLE);
//                        tvResult.setText("");
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                })
//                .show();// 显示对话框
//
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
