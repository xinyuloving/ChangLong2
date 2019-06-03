package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.printer.SearchBTActivity;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;

import java.util.Calendar;

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
    @BindView(R.id.fab_print)
    FloatingActionButton fabPrint;
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

    //打印内容
    private Tb_measure tb_measure;
    MeasureDao measureDao = new MeasureDao(this);
    private String strContent = "";
    private boolean booIsSave = false;

    //保存自定义字段
    private String strMeasureName="";//测点名称
    private String strEntityName="";//单位名称
    private String strSamplingTime="";//取样时间
    private String strClassic="";//测量类型
    private String strSampler="";//采样人
    private String strInspector="";//检测人
    Calendar calendar = Calendar.getInstance();

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
                second = millisUntilFinished / 1000;
                timeCount.setText("还有" + second + "秒" + "返回主页面");
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
            R.id.btn_measure, R.id.btn_save, R.id.iv_return, R.id.tv_return,R.id.fab_print})
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
            case R.id.fab_print:
                if (true == booIsSave){
                    printData(strContent);
                }else{
                    CustomToast.showToast(ManualMeasureSecActivity.this, "没有可打印的数据，请先保存");
                }
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
                    AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
                    //获取界面
                    View dialogView = LayoutInflater.from(this).inflate(R.layout.input_save_data_dialoglayout, null);
                    //将界面填充到AlertDiaLog容器
                    setDeBugDialog.setView(dialogView);
                    setDeBugDialog.create();
                    final EditText measureName = dialogView.findViewById(R.id.et_measure_name);
                    final EditText entityName = dialogView.findViewById(R.id.et_entity_name);
                    final SuperTextView samplingTime = dialogView.findViewById(R.id.et_sampling_time);
                    final Spinner classic=dialogView.findViewById(R.id.sp_classic);
                    final EditText sampler = dialogView.findViewById(R.id.et_sampler);
                    final EditText inspector = dialogView.findViewById(R.id.et_inspector);
                    samplingTime.setCenterString(DateUtil.getNowDateTime());

                    samplingTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateUtil.showTimePickerDialog(ManualMeasureSecActivity.this, samplingTime, calendar);
                            DateUtil.showDatePickerDialog(ManualMeasureSecActivity.this, 0, samplingTime, calendar);
                        }
                    });

                    final AlertDialog customAlert = setDeBugDialog.show();
                    dialogView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strMeasureName =measureName.getText().toString();
                            strEntityName=entityName.getText().toString();
                            strSamplingTime=samplingTime.getLeftString()+" "+samplingTime.getCenterString()+"";
                            strClassic = (String) classic.getSelectedItem();
                            classic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    strClassic =classic.getSelectedItem().toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });
                            strSampler=sampler.getText().toString();
                            strInspector=inspector.getText().toString();
                            saveData();
                            customAlert.dismiss();
                        }
                    });
                    dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customAlert.dismiss();
                        }
                    });
                } else {
                    CustomToast.showToast(ManualMeasureSecActivity.this, "没有可保存的数据，请先测量");
                }

                break;
        }
    }

    private void saveData() {

        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,
                strClassic,//测量类别
                Constants.strLoginName + DateUtil.getNowDateTime2() + "手动测量",
                "手动测量" + Constants.strLoginName + DateUtil.getNowDateTime2(),//曲线名称
                intWavelength,//曲线波长
                floDensity,//密度
                floTranrate,//透过率
                floAbsorbance,//吸光度
                Constants.strLoginName,//操作员
                "COD（0-100 mg/L）",//COD（0-100 mg/L）
                intResult + ".000 mg/L",//测量结果
                inttemp + "℃",//温度
                DateUtil.getNowDateTime(),//时间
                "备注",
                strMeasureName,
                strEntityName,
                strSamplingTime,
                strSampler,
                strInspector
        );
        Log.i(TAG, "保存数据=" + tb_measure.toString());
        strContent ="\n分类：" + strClassic
                + "\n条目：" + Constants.strLoginName + DateUtil.getNowDateTime2() + "手动测量"
                + "\n名称：" + "手动测量" + Constants.strLoginName + DateUtil.getNowDateTime2()
                + "\n波长：" + intWavelength
                + "\n浓度：" +  floDensity
                + "\n透过率：" + floTranrate
                + "\n吸光度：" + floAbsorbance
                + "\n操作员：" + Constants.strLoginName
                + "\n温度：" + inttemp + "℃"
                + "\n测量结果：" + intResult + ".000 mg/L"
                + "\n类型：" + "COD（0-100 mg/L）"
                + "\n时间：" + DateUtil.getNowDateTime()
                + "\n备注：" + "备注"
                + "\n测点名称：" + strMeasureName
                + "\n单位名称：" + strEntityName
                + "\n取样时间：" + strSamplingTime
                + "\n采样人：" + strSampler
                + "\n监测人：" + strInspector;
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");
        booIsMeasure = false;
        booIsSave=true;
    }

    private void printData(String strContent){
        new MaterialDialog.Builder(ManualMeasureSecActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title("打印内容：")// 标题
                .content(strContent)// 内容
                .negativeText(R.string.cancel)
                .neutralText(R.string.print)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(ManualMeasureSecActivity.this, SearchBTActivity.class);
                        intent.putExtra("printInfo", strContent); //传递需要打印的数据
                        intent.putExtra("type","ManualMeasureSecActivity");//从何处跳转
                        startActivity(intent);
                        ManualMeasureSecActivity.this.finish();
                    }
                })

                .show();// 显示对话框

    }
    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }
}
