package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ManualMeasureSecActivity extends AppCompatActivity {


    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.text_userName)
    TextView textUserName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.text_lumin)
    TextView textLumin;
    @BindView(R.id.text_absor)
    TextView textAbsor;
    @BindView(R.id.text_wavelengh)
    TextView textWavelengh;
    @BindView(R.id.text_temper)
    TextView textTemper;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private String strInfo = "";
    private MixSpeakUtil mixSpeakUtil;
    private String strWavelength = "";
    private Intent intent = new Intent();

    private int intWavelength;//曲线波长
    private float floDensity;//密度
    private float floTranrate;//透过率
    private float floAbsorbance;//吸光度
    private int intResult;//测量结果
    private int inttemp;//测量结果

    private final String TAG = "MMSActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_measure_sec);
        ButterKnife.bind(this);
        Intent getIntent = getIntent();
        strWavelength = getIntent.getStringExtra("wavelength");
//        tvUser.setText(strUserName);
        mixSpeakUtil = MixSpeakUtil.getInstance(this);
        initView();
    }

    private void initView() {
        tvCod.setText(strWavelength);
        tvUser.setText(Constants.strLoginName);
        tvTimer.setText(DateUtil.getDate());

    }

    @OnClick({R.id.tv_user, R.id.tv_cod, R.id.tv_title, R.id.tv_timer,
            R.id.btn_measure, R.id.btn_save,R.id.iv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.tv_cod:
                break;
            case R.id.tv_title:
                break;
            case R.id.tv_timer:
                break;
            case R.id.iv_return:
                intent.setClass(this, ManualMeasureFristActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_measure:
                // cardview2.setVisibility(View.GONE);
//                view2.setVisibility(View.VISIBLE);
//                tvTitle.setText(RandomUntil.getNum(25, 35) + ".000 mg/L");
//                strInfo = "综合排放标准 COD：\n" +
//                        "A排放限值 ：20mg/L\n" +
//                        "B排放限值 ：30mg/L\n";
//                tvShow.setText(strInfo);
//                strInfo = "透过率（T）："+RandomUntil.getNum(20) +".00%\n"+
//                        "吸光度（A）：1.0\n" +
//                        "波长（λ）：610 nm\n" +
//                        "温度："+RandomUntil.getNum(25,37)+" ℃\n";
//                tvResult.setText(strInfo);
////                play("计算完成");
//                mixSpeakUtil.speak("计算完成");
//                view2.setVisibility(View.VISIBLE);
                intResult = RandomUntil.getNum(25, 35);
                intWavelength = 610;
                floDensity = 10.0f;
                floTranrate = RandomUntil.getRandomFloat(80.0f, 100.0f);
                floAbsorbance = RandomUntil.getRandomFloat(1.0f, 2.0f);
                inttemp = RandomUntil.getNum(27, 35);
                tvTitle.setText(intResult + ".000 mg/L");
                strInfo = Constants.strWavelength;
                //tvShow.setText(strInfo);
                strInfo = "透过率（T）：" + floTranrate + "%\n" +
                        "吸光度（A）：" + floAbsorbance + "\n" +
                        "波长（λ）：" + intWavelength + "nm\n" +
                        "温度：" + inttemp + " ℃\n";

                textLumin.setText(Constants.df.format(floTranrate * 100) + "%");
                textAbsor.setText(floAbsorbance + "");
                textWavelengh.setText(intWavelength + "nm");
                textTemper.setText(inttemp + "℃");
                // tvResult.setText(strInfo);
//                play("计算完成");
                mixSpeakUtil.speak("计算完成");
                break;
            case R.id.btn_save:
                if (tvTitle.getText().toString().length() > 0) {
                    saveData();
                } else {
                    CustomToast.showToast(ManualMeasureSecActivity.this, "没有可保存的数据，请先测量");
                }

                break;
        }
    }

    private void saveData() {

        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,
                "手动测量",//测量类别
                Constants.strLoginName + DateUtil.getNowDateTime2() + "手动测量",
                "手动测量" + Constants.strLoginName + DateUtil.getNowDateTime2(),//曲线名称
                intWavelength,//曲线波长
                floDensity,//密度
                floTranrate,//透过率
                floAbsorbance,//吸光度
                Constants.strLoginName,//操作员
                Constants.strWavelength,//COD（0-100 mg/L）
                intResult + ".000 mg/L",//测量结果
                inttemp + "℃",//温度
                DateUtil.getNowDateTime(),//时间
                "备注"
        );
        Log.i(TAG, "保存数据=" + tb_measure.toString());
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");

    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, ManualMeasureFristActivity.class);
        startActivity(intent);
        this.finish();
    }
}
