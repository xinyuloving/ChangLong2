package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import static com.lkkdesign.changlong.utils.DateUtil.intCountDwonTime;
import static com.lkkdesign.changlong.utils.MyFunc.calculateTransmittance;
import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

public class ManualMeasureSecActivity extends AppCompatActivity {

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_return)
    TextView tvReturn;
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
    @BindView(R.id.tv_ll_title)
    TextView tvLlTitle;
    @BindView(R.id.text_limitA)
    TextView textLimitA;
    @BindView(R.id.text_limitB)
    TextView textLimitB;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.timeCount)
    TextView timeCount;
    private String strInfo = "";
    private String strfrom = "";
    private MixSpeakUtil mixSpeakUtil;
    private String strWavelength = "";
    private boolean booIsMeasure = false;
    private Intent intent = new Intent();

    private int intWavelength;//曲线波长
    private float floDensity;//密度
    private float floTranrate;//透过率
    private float floAbsorbance;//吸光度
    private int intResult;//测量结果
    private int inttemp;//测量结果

    private final String TAG = "MMSActivity";
    private CountDownTimer timer;
    private long second = 0;

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
        Intent getIntent = getIntent();
        tvCod.setText(strWavelength);
        tvUser.setText(Constants.strLoginName);
        tvTimer.setText(DateUtil.getDate());
        strfrom = getIntent.getStringExtra("from");
        if ("InputDataActivity".equals(strfrom)) {
            tvLlTitle.setText(R.string.tv_cure_adjust);
        } else if ("ManualMeasureFristActivity".equals(strfrom)) {
            tvLlTitle.setText(R.string.tv_manual);
        }
        timer = new CountDownTimer(intCountDwonTime, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                second =  millisUntilFinished / 1000;
                timeCount.setText("还有"+second + "秒"+"返回主页面");
            }

            @Override
            public void onFinish() {
                intent.setClass(ManualMeasureSecActivity.this, Main2Activity.class);
                startActivity(intent);
                ManualMeasureSecActivity.this.finish();
            }
        };
        timer.start();

    }

    @OnClick({R.id.tv_user, R.id.tv_cod, R.id.tv_title, R.id.tv_timer,
            R.id.btn_measure, R.id.btn_save, R.id.iv_return, R.id.tv_return})
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
            case R.id.tv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_measure:

//                intResult = RandomUntil.getNum(25, 35);
//                intWavelength = 610;
//                floDensity = 10.0f;
//                floTranrate = RandomUntil.getRandomFloat(80.0f, 100.0f);
//                floAbsorbance = RandomUntil.getRandomFloat(1.0f, 2.0f);
//                inttemp = RandomUntil.getNum(27, 35);
//                tvTitle.setText(intResult + ".000 mg/L");
//                strInfo = Constants.strWavelength;
//                //tvShow.setText(strInfo);
//                strInfo = "透过率（T）：" + floTranrate + "%\n" +
//                        "吸光度（A）：" + floAbsorbance + "\n" +
//                        "波长（λ）：" + intWavelength + "nm\n" +
//                        "温度：" + inttemp + " ℃\n";
//
//                textLumin.setText(Constants.df.format(floTranrate * 100) + "%");
//                textAbsor.setText(floAbsorbance + "");
//                textWavelengh.setText(intWavelength + "nm");
//                textTemper.setText(inttemp + "℃");
                // tvResult.setText(strInfo);
//                play("计算完成");

                booIsMeasure = true;
                intResult = RandomUntil.getNum(25, 35);
                intWavelength = 610;
                floDensity = 10.0f;//密度
                float floClosed = RandomUntil.getRandomFloat(0.10f, 0.50f);//光源在关闭的情况下，检测器产生的电流数值
                float floEmpty = RandomUntil.getRandomFloat(100.0f, 105.50f);//光源点亮，样品仓放置空样品管的情况下，检测器产生的电流数值
                float floSample = RandomUntil.getRandomFloat(10.10f, 20.50f);//空样品管拿出来，将装有样品的样品管放置进样品仓，此时，检测器产生的电流数值
                floTranrate = calculateTransmittance(floClosed, floEmpty, floSample);//透过率
                floAbsorbance = getAbsorbance(floTranrate);//吸光度
                inttemp = RandomUntil.getNum(27, 35);
                tvTitle.setText("C=" + intResult + ".000mg/L");
                Log.i("MyFunc", "floTranrate * 100 =" + floTranrate * 100);
                //tvShow.setText(strShow);
                textLumin.setText(Constants.df.format(floTranrate * 100) + "%");
                textAbsor.setText(floAbsorbance + "");
                textWavelengh.setText(intWavelength + "nm");
                textTemper.setText(inttemp + "℃");

                mixSpeakUtil.speak("计算完成");
                break;
            case R.id.btn_save:
                if (true == booIsMeasure) {
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
        booIsMeasure = false;
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }
}
