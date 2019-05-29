package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

public class PhotometerSecActivity extends AppCompatActivity {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_ll_title)
    TextView tvLlTitle;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.tv_show1)
    TextView tvShow1;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_TransmissionRate)
    TextView tvTransmissionRate;
    @BindView(R.id.tv_Current)
    TextView tvCurrent;
    @BindView(R.id.tv_Voltage)
    TextView tvVoltage;
    @BindView(R.id.tv_Temper)
    TextView tvTemper;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.cardview33)
    CardView cardview33;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_blank)
    Button btnBlank;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private String strTitle = "";

    private Intent intent = new Intent();
    private String strInfo = "";
    private String strfrom = "";
    private MixSpeakUtil mixSpeakUtil;
    private boolean booIsPre = false;
    private float floTranrate = 0f;
    private float floAbsorbance = 0f;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photometer_sec);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvUser.setText(Constants.strLoginName);
        mixSpeakUtil = MixSpeakUtil.getInstance(this);
        Intent intent = getIntent();
        strTitle = intent.getStringExtra("wavelength");
        strfrom = intent.getStringExtra("from");
        tvCod.setText("λ= " + strTitle + " nm");
        tvTimer.setText(DateUtil.getDate());
        if ("InputDataActivity".equals(strfrom)) {
            tvLlTitle.setText("数据输入");
        } else if ("CurveMeasureActivity".equals(strfrom)) {
            tvLlTitle.setText("公式输入");
        } else if ("CMActivity_ssjz".equals(strfrom)) {
            tvLlTitle.setText("曲线校准");
        } else {
            tvLlTitle.setText("光度计");
        }

    }

    @OnClick({R.id.tv_user, R.id.tv_return, R.id.tv_cod, R.id.tv_result, R.id.tv_timer, R.id.btn_blank, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.tv_return:
                if ("InputDataActivity".equals(strfrom)) {
                    intent.setClass(this, InputDataActivity.class);
                    startActivity(intent);
                    this.finish();
                } else if ("CurveMeasureActivity".equals(strfrom)) {
                    intent.setClass(this, CurveMeasureActivity.class);
                    startActivity(intent);
                    this.finish();
                } else if ("CMActivity_ssjz".equals(strfrom)) {
                    intent.setClass(this, CurveMeasureActivity.class);
                    startActivity(intent);
                    this.finish();
                } else {
                    intent.setClass(PhotometerSecActivity.this, PhotometerFristActivity.class);
                    startActivity(intent);
                    PhotometerSecActivity.this.finish();
                }
                break;
            case R.id.tv_cod:
                break;
            case R.id.tv_result:
                break;
            case R.id.tv_timer:
                break;
            case R.id.btn_blank:

                tvShow1.setText("\n\n请取出空白比色管\n请放入样品\n请按确认键");
//                tvShow2.setVisibility(View.INVISIBLE);
                booIsPre = true;
                break;
            case R.id.btn_save:
                if ("InputDataActivity".equals(strfrom) || "CurveMeasureActivity".equals(strfrom)) {
                    tvTitle.setText(RandomUntil.getNum(25, 35) + ".000 mg/L");
                    strInfo = "综合排放标准 COD：\n" +
                            "A排放限值 ：20mg/L\n" +
                            "B排放限值 ：30mg/L\n";
                    tvShow1.setText(strInfo);
                    strInfo = "透过率（T）：" + RandomUntil.getNum(20) + ".00%\n" +
                            "吸光度（A）：1.0\n" +
                            "波长（λ）：610 nm\n" +
                            "温度：" + RandomUntil.getNum(25, 37) + " ℃\n";
                    tvResult.setText(strInfo);
                } else if ("CMActivity_ssjz".equals(strfrom)) {
                    tvTitle.setText("实时校准结果");
                    strInfo = "C1=5mg/L，A1=0.100\n" +
                            "C2=10mg/L，A2=0.200\n" +
                            "C3=20mg/L，A3=0.400\n" +
                            "C4=50mg/L，A4=1.000\n" +
                            "C5=100mg/L，A5=2.00\n";
                    tvShow1.setText(strInfo);
                    strInfo = "";
                    tvResult.setText(strInfo);
                } else {
                    if (false == booIsPre) {
                        CustomToast.showToast(this, "请按步骤执行");
                        return;
                    }
                    floTranrate = RandomUntil.getNum(10, 20);
                    Log.i("PSA", "floTranrate=" + floTranrate);
                    tvTransmissionRate.setText(floTranrate + "00%\n");
                    tvCurrent.setText(RandomUntil.getNum(100, 120) + "μA\n");
                    tvVoltage.setText(RandomUntil.getNum(10, 12) + "mV\n");
                    tvTemper.setText(RandomUntil.getNum(20, 25) + "℃\n");
//                    tvTitle.setText("");

                    floAbsorbance = getAbsorbance(floTranrate);
                    Log.i("PSA", "floAbsorbance=" + floAbsorbance);
                    strInfo = "吸光度\nA=" + (-1) * floAbsorbance;
                    tvShow1.setText(strInfo);
                    tvShow1.setGravity(Gravity.CENTER);
                    tvShow1.setTextSize(getResources().getDimension(R.dimen.textsize_24));
                    tvShow1.setTextColor(getResources().getColor(R.color.statusBarColor));
//                    strInfo = "透过率（T）：" + RandomUntil.getNum(10, 20) + ".00%\n" +
//                            "电流：" + RandomUntil.getNum(85, 100) + " μA\n" +
//                            "电压：10 mV\n" +
//                            "温度:25℃\n";
//                    tvResult.setText(strInfo);
                }
                mixSpeakUtil.speak("计算完成");
                break;
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        if ("InputDataActivity".equals(strfrom)) {
            intent.setClass(this, InputDataActivity.class);
            startActivity(intent);
            this.finish();
        } else if ("CurveMeasureActivity".equals(strfrom)) {
            intent.setClass(this, CurveMeasureActivity.class);
            startActivity(intent);
            this.finish();
        } else if ("CMActivity_ssjz".equals(strfrom)) {
            intent.setClass(this, CurveMeasureActivity.class);
            startActivity(intent);
            this.finish();
        } else {
            intent.setClass(PhotometerSecActivity.this, PhotometerFristActivity.class);
            startActivity(intent);
            PhotometerSecActivity.this.finish();
        }
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
