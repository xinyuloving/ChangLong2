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

import static com.lkkdesign.changlong.utils.MyFunc.calculateTransmittance;
import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

/**
 * 自动测量界面
 */
public class AutoMeasureActivity extends AppCompatActivity {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.text_userName)
    TextView textUserName;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.text_limitA)
    TextView textLimitA;
    @BindView(R.id.text_limitB)
    TextView textLimitB;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.text_lumin)
    TextView textLumin;
    @BindView(R.id.text_absor)
    TextView textAbsor;
    @BindView(R.id.text_wavelengh)
    TextView textWavelengh;
    @BindView(R.id.text_temper)
    TextView textTemper;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_save)
    Button btnSave;
    private String strInfo = "";
    private String strShow = "COD（0-100 mg/L）";
    private MixSpeakUtil mixSpeakUtil;
    private Intent intent = new Intent();
    private final String TAG = "AutoMeasureActivity";

    private int intWavelength;//曲线波长
    private float floDensity;//密度
    private float floTranrate;//透过率
    private float floAbsorbance;//吸光度
    private int intResult;//测量结果
    private int inttemp;//测量结果
    private boolean booIsMeasure = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_measure);
        ButterKnife.bind(this);

        mixSpeakUtil = MixSpeakUtil.getInstance(this);
        initView();
    }

    private void initView() {
        tvUser.setText(Constants.strLoginName);
        tvTime.setText(DateUtil.getDate());
        //strShow = "排放标准\nA：20mg/L\t\nB：30mg/L";
//        tvCod.setText(strShow);
    }


    @OnClick({R.id.iv_return, R.id.tv_return, R.id.btn_measure, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
            case R.id.iv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_measure:
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

                /*strInfo = "透过率（T）：" + Constants.df.format(floTranrate * 100) + "%\n" +
                        "吸光度（A）：" + floAbsorbance + "\n" +
                        "波长（λ）："+intWavelength+"nm\n" +
                        "温度：" + inttemp + " ℃\n";
                tvResult.setText(strInfo);*/
//                play("计算完成");
                mixSpeakUtil.speak("计算完成");
                break;
            case R.id.btn_save:
                if (true == booIsMeasure) {//判断是否已有测量的数据
                    saveData();
                } else {
                    CustomToast.showToast(AutoMeasureActivity.this, "没有可保存的数据，请先测量");
                }
                break;
        }
    }


    private void saveData() {

        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,
                "自动测量",//测量类别
                Constants.strLoginName + DateUtil.getNowDateTime2() + "自动测量",
                "自动测量" + Constants.strLoginName + DateUtil.getNowDateTime2(),//曲线名称
                intWavelength,//曲线波长
                floDensity,//密度
                floTranrate,//透过率
                floAbsorbance,//吸光度
                Constants.strLoginName,//操作员
                strShow,//COD（0-100 mg/L）
                intResult + ".000 mg/L",//测量结果
                inttemp + "℃",//温度
                DateUtil.getNowDateTime(),//时间
                "备注"
        );
        Log.i(TAG, "保存数据=" + tb_measure.toString());
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");
        booIsMeasure = false;//保存数据之后改变状态，防止多次提交保存同一条数据
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }


}
