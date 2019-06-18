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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextClock;
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
    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.tc_time)
    TextClock tcTime;
    @BindView(R.id.timeCount)
    TextView timeCount;
    @BindView(R.id.fab_print)
    FloatingActionButton fabPrint;
    @BindView(R.id.fab_cdtime)
    FloatingActionButton fabCDTime;
    @BindView(R.id.standard_type)
    Spinner standardType;
    private String strInfo = "";
    private String strShow = "COD（0-100 mg/L）";
    private MixSpeakUtil mixSpeakUtil;
    private Intent intent = new Intent();
    private final String TAG = "AutoMeasureActivity";
    private Boolean running = false;
    private String strFrom = "";

    private int intWavelength;//曲线波长
    private float floDensity;//密度
    private float floTranrate;//透过率
    private float floAbsorbance;//吸光度
    private int intResult;//测量结果
    private int inttemp;//测量结果


    private boolean booIsMeasure = false;
    private CountDownTimer timer;
    private int intCountDwonTime = 0;
    private long lonSecond = 0;
    private long minute = 0;
    //打印内容
    private Tb_measure tb_measure;
    MeasureDao measureDao = new MeasureDao(this);
    private String strContent = "";
    private boolean booIsSave = false;

    //保存自定义字段
    private String strMeasureName = "";//测点名称
    private String strEntityName = "";//单位名称
    private String strSamplingTime = "";//取样时间
    private String strClassic = "自动测量";//测量类型
    private String strStyle = "";//测量类型？
    private String strSampler = "";//采样人
    private String strInspector = "";//检测人
    private String strNote="";//备注
    private String strCODInfo = "";

    private String strStandardType = "";
    Calendar calendar = Calendar.getInstance();

    private String strCDTime = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auto_measure);
        ButterKnife.bind(this);

        mixSpeakUtil = MixSpeakUtil.getInstance(this);
        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        strFrom = intent.getStringExtra("from");
        strCODInfo = intent.getStringExtra("strInfo");
        Log.i(TAG, "strCODInfo=" + strCODInfo);
        Constants.strFormActivity = strFrom;
        tvUser.setText(Constants.strLoginName);
        tvTime.setText(DateUtil.getDate());
        if (strCODInfo == null) {
            tvCod.setText(strShow);
            Log.i(TAG, "tvCod=null " + strShow);
        } else {
            tvCod.setText(strCODInfo);
            Log.i(TAG, "tvCod=!null " + strCODInfo);
        }
        // startThread();
        //strShow = "排放标准\nA：20mg/L\t\nB：30mg/L";
//        tvCod.setText(strShow);
        /*if ("InputDataActivity".equals(Constants.strFormActivity)){
            tvTitleToolbar.setText("");
        }*/

        strStandardType = (String) standardType.getSelectedItem();
        standardType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                strStandardType = (String) standardType.getSelectedItem();
                CustomToast.showToast(AutoMeasureActivity.this, strStandardType);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        timeCount.setVisibility(View.INVISIBLE);
        strShow=tvCod.getText().toString();

    }


    @OnClick({R.id.tv_cod, R.id.cardview3, R.id.tc_time, R.id.iv_return, R.id.tv_return, R.id.btn_measure,
            R.id.btn_save, R.id.fab_print, R.id.fab_cdtime, R.id.timeCount})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
            case R.id.iv_return:
//                intent.setClass(this, Main2Activity.class);
//                startActivity(intent);
//                this.finish();
                jumpToActivity(Main2Activity.class);
                break;
            case R.id.tv_cod:
                /*intent.setClass(this, ManualMeasureFristActivity.class);
                startActivity(intent);
                this.finish();*/
                intent.putExtra("from", TAG);
                jumpToActivity(CurveSelectActivity.class);
                break;
            case R.id.cardview3:
                /*intent.setClass(this, PhotometerFristActivity.class);
                startActivity(intent);
                this.finish();*/
                break;
            case R.id.tc_time:
                /*intent.setClass(this, TimingSetupActivity.class);
                startActivity(intent);
                this.finish();*/
                jumpToActivity(TimingSetupActivity.class);
                break;
            case R.id.timeCount:

                break;
            case R.id.fab_print:
                if (true == booIsSave) {
                    printData(strContent);
                } else {
                    CustomToast.showToast(AutoMeasureActivity.this, "没有可打印的数据，请先保存");
                }
                break;
            case R.id.fab_cdtime://倒计时
                inputTimeDown();
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
                btnMeasure.setVisibility(View.INVISIBLE);
                btnSave.setVisibility(View.VISIBLE);

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
                    AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
                    //获取界面
                    View dialogView = LayoutInflater.from(this).inflate(R.layout.input_save_data_dialoglayout, null);
                    //将界面填充到AlertDiaLog容器
                    setDeBugDialog.setView(dialogView);
                    setDeBugDialog.create();
                    final EditText measureName = dialogView.findViewById(R.id.et_measure_name);
                    final EditText entityName = dialogView.findViewById(R.id.et_entity_name);
                    final SuperTextView samplingTime = dialogView.findViewById(R.id.et_sampling_time);
                    final Spinner classic = dialogView.findViewById(R.id.sp_classic);
                    final EditText sampler = dialogView.findViewById(R.id.et_sampler);
                    final EditText inspector = dialogView.findViewById(R.id.et_inspector);
                    final EditText note= dialogView.findViewById(R.id.et_note);
                    samplingTime.setLeftString(DateUtil.getNowDateTime());
                    measureName.setHint("请输入测点名称");
                    entityName.setHint("请输入单位名称");
                    sampler.setText(Constants.strLoginName);
                    inspector.setText(Constants.strLoginName);
                    note.setHint("请输入备注");

                    samplingTime.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DateUtil.showTimePickerDialog(AutoMeasureActivity.this, samplingTime, calendar);
                            DateUtil.showDatePickerDialog(AutoMeasureActivity.this, 0, samplingTime, calendar);
                        }
                    });

                    final AlertDialog customAlert = setDeBugDialog.show();
                    dialogView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strMeasureName = measureName.getText().toString();
                            strEntityName = entityName.getText().toString();
                            strSamplingTime = samplingTime.getLeftString() + " " + samplingTime.getCenterString() + "";
                            strStyle = (String) classic.getSelectedItem();
                            classic.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                @Override
                                public void onItemSelected(AdapterView<?> parent, View view,
                                                           int position, long id) {
                                    strStyle = classic.getSelectedItem().toString();
                                }

                                @Override
                                public void onNothingSelected(AdapterView<?> parent) {
                                    // TODO Auto-generated method stub

                                }
                            });
                            strSampler = sampler.getText().toString();
                            strInspector = inspector.getText().toString();
                            strNote=note.getText().toString();

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
                    CustomToast.showToast(AutoMeasureActivity.this, "没有可保存的数据，请先测量");
                }
                break;
        }
    }


    private void saveData() {
        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,
                strClassic,//测量类别
                strStyle,
                DateUtil.getNowDateTime2() + " "+strShow + " "+ Constants.strLoginName,
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
                strNote,
                strMeasureName,
                strEntityName,
                strSamplingTime,
                strSampler,
                strInspector
        );
        strContent = "\n检测项目：" + tvCod.getText().toString()
                + "\n测量结果：" + intResult + ".000 mg/L"
                + "\n监测人：" + strInspector
                + "\n波长：" + intWavelength+"nm"
                + "\n透过率：" + floTranrate
                + "\n吸光度：" + floAbsorbance
                + "\n温度：" + inttemp + "℃"
                + "\n检测时间：" + DateUtil.getNowDateTime()
                + "\n分类：" + "手动测量"
                + "\n测量类别：" + strStyle
                + "\n取样时间：" + strSamplingTime
                + "\n测点名称：" + strMeasureName
                + "\n单位名称：" + strEntityName
                + "\n采样人：" + strSampler
                + "\n备注：" + strNote;

        Log.i(TAG, "保存数据=" + tb_measure.toString());
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");
        booIsMeasure = false;//保存数据之后改变状态，防止多次提交保存同一条数据
        booIsSave = true;
        btnMeasure.setVisibility(View.VISIBLE);
        btnSave.setVisibility(View.INVISIBLE);
    }

    private void printData(String strContent) {
        new MaterialDialog.Builder(AutoMeasureActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title(DateUtil.getNowDateTime2() + " " + tvCod.getText().toString() + " " + Constants.strLoginName)// 标题
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
                        intent.setClass(AutoMeasureActivity.this, SearchBTActivity.class);
                        intent.putExtra("printInfo", strContent); //传递需要打印的数据
                        intent.putExtra("type", "AutoMeasureActivity");//从何处跳转
                        startActivity(intent);
                        AutoMeasureActivity.this.finish();
                    }
                })
                .show();// 显示对话框
    }

    private void inputTimeDown() {
        new MaterialDialog.Builder(AutoMeasureActivity.this)
                .title("设置自动测量倒计时")
                .iconRes(R.mipmap.icon_bottom)
                .content("请输入倒计时，单位：分钟")
//                                .widgetColor(Color.BLUE)//输入框光标的颜色
                .neutralText(R.string.cancel)
                .inputType(InputType.TYPE_CLASS_NUMBER)//可以输入的类型-数值
                //前2个一个是hint一个是预输入的文字
                .input(R.string.input_timedown, R.string.input_empty, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        timeCount.setVisibility(View.VISIBLE);
                        String strInput = input.toString().replaceAll(" ", "");
                        if (strInput.length() == 0) {
                            CustomToast.showToast(AutoMeasureActivity.this, "没有输入倒计时");
                            return;
                        }
                        cancleCDTime();//取消倒计时
                        intCountDwonTime = Integer.parseInt(input.toString());
                        Log.i(TAG, "输入的是：" + input);
                        CountDwonTime(intCountDwonTime);
                    }
                })

                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        if (dialog.getInputEditText().length() <= 10) {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(false);
                        } else {
                            dialog.getActionButton(DialogAction.POSITIVE).setEnabled(true);
                        }
                    }
                })
                .show();// 显示对话框
    }

    /**
     * 倒计时
     */
    private void CountDwonTime(int intCDTime) {
        timer = new CountDownTimer(intCDTime * 60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                lonSecond = millisUntilFinished / 1000;
//                second = millisUntilFinished / 1000;
//                minute = second / 60;
//                timeCount.setText(minute + "分钟后开始自动测量");
                strCDTime = Constants.secToTime(lonSecond);
                timeCount.setText(strCDTime + " 后开始自动测量");

            }

            @Override
            public void onFinish() {
//                intent.setClass(AutoMeasureActivity.this, Main2Activity.class);
//                startActivity(intent);
//                AutoMeasureActivity.this.finish();
            }
        };

        timer.start();
    }

    private void jumpToActivity(Class activityClass) {
        cancleCDTime();
        intent.setClass(this, activityClass);
        startActivity(intent);
        this.finish();
    }

    private void cancleCDTime() {
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    @Override
    public void onBackPressed() {
//        intent.setClass(this, Main2Activity.class);
//        startActivity(intent);
//        this.finish();
        jumpToActivity(Main2Activity.class);
    }


}