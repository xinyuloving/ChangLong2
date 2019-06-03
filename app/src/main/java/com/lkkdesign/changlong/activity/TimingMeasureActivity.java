package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimingMeasureActivity extends AppCompatActivity {


    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_title_show)
    TextView tvTitleShow;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.linearlayout)
    CardView linearlayout;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    private String strTitle = "";
    private String strStartTime = "";
    private String strEndTime = "";
    private String strJiange = "";

    private String strInfo = "C=15.000 mg/L\n\n" + "透过率（T）：10%\n" +
            "吸光度（A）：1.0\n" +
            "波长（λ）：700 nm\n" +
            "温度:25℃\n";
    private Intent intent = new Intent();
    private int intNum = 0;

    private int intWavelength;//曲线波长
    private float floDensity;//密度
    private float floTranrate;//透过率
    private float floAbsorbance;//吸光度
    private int intResult;//测量结果
    private int inttemp;//测量结果

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_measure);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {

        Intent intent = getIntent();
        strTitle = intent.getStringExtra("wavelength");
        strStartTime = intent.getStringExtra("strStartTime");
        strEndTime = intent.getStringExtra("strEndTime");
        strJiange = intent.getStringExtra("jiange");
        Log.i("TimingMeasureActivity", "strJiange = " + strJiange);
        tvCod.setText(strTitle);
        tvShow.setText(R.string.tv_show);
        tvTimer.setText(DateUtil.getDate());

    }

    @OnClick({R.id.tv_user, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.btn_calculate:
                intNum++;
                if (2 == intNum) {
                    intent.setClass(TimingMeasureActivity.this, TimingMeasureSecActivity.class);
                    intent.putExtra("strStartTime", strStartTime);
                    intent.putExtra("strEndTime", strEndTime);
                    intent.putExtra("jiange", strJiange);
                    intent.putExtra("wavelength", strTitle);
                    startActivity(intent);
                } else {
//                    tvTitle.setText(RandomUntil.getNum(25, 35) + ".000 mg/L");
//                    strInfo = "综合排放标准 COD：\n" +
//                            "A排放限值 ：20mg/L\n" +
//                            "B排放限值 ：30mg/L\n";
//                    tvShow.setText(strInfo);
//                    view2.setVisibility(View.VISIBLE);
//                    strInfo = "开始时间：" + strStartTime + "\n" +
//                            "时间间隔：" + strJiange + " 分钟" + "\n" +
//                            "结束时间：" + strEndTime + "\n";
//                    tvResult.setText(strInfo);
                    //view2.setVisibility(View.VISIBLE);
                    intResult = RandomUntil.getNum(25, 35);
                    intWavelength = 610;
                    floDensity = 10.0f;
                    floTranrate = RandomUntil.getRandomFloat(80.0f, 100.0f);
                    floAbsorbance = RandomUntil.getRandomFloat(1.0f, 2.0f);
                    inttemp = RandomUntil.getNum(27, 35);
                    tvTitle.setText(intResult + ".000 mg/L");
                    strInfo = Constants.strWavelength;
                    tvShow.setText(strInfo);
                    strInfo = "透过率（T）：" + floTranrate + "%\n" +
                            "吸光度（A）：" + floAbsorbance + "\n" +
                            "波长（λ）：" + intWavelength + "nm\n" +
                            "温度：" + inttemp + " ℃\n";
                    tvResult.setText(strInfo);
                    saveData();
//                play("计算完成");
//                    mixSpeakUtil.speak("计算完成");

                }

                break;
        }
    }

    private void saveData() {

        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,
                "定时测量",//测量类别
                Constants.strLoginName + DateUtil.getNowDateTime2() + "定时测量",
                "定时测量" + Constants.strLoginName + DateUtil.getNowDateTime2(),//曲线名称
                intWavelength,//曲线波长
                floDensity,//密度
                floTranrate,//透过率
                floAbsorbance,//吸光度
                Constants.strLoginName,//操作员
                Constants.strWavelength,//COD（0-100 mg/L）
                intResult + ".000 mg/L",//测量结果
                inttemp + "℃",//温度
                DateUtil.getNowDateTime(),//时间
                "备注",
                "",//测点名称
                "",//单位名称
                "",//采样时间
                "",//采样员
                ""//检测员
        );
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");

    }

//    @OnClick({R.id.iv_return, R.id.linearlayout, R.id.tv_show, R.id.btn_blank, R.id.btn_measure, R.id.btn_calculate, R.id.btn_save, R.id.tv_result})
//    public void onViewClicked(View view) {
//        switch (view.getId()) {
//            case R.id.iv_return:
//                intent.setClass(this,CurveSelectActivity.class);
//                startActivity(intent);
//                break;
//            case R.id.linearlayout:
//                break;
//            case R.id.tv_show:
//                break;
//            case R.id.btn_blank:
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
//                tvResult.setText("计算结果：\tC=kA+b\nR2=99.99%");
////                play("结果已计算完毕");
//                break;
//            case R.id.btn_save:
//                showData(strInfo);
//                break;
//            case R.id.tv_result:
//                break;
//        }
//    }

//    private void showData(String string){
//        //当接收到Click事件之后触发
//        new MaterialDialog.Builder(TimingMeasureActivity.this)// 初始化建造者
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

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(this, TimingSetupActivity.class);
        startActivity(intent);
        this.finish();
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
