package com.lkkdesign.changlong.activity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lkkdesign.changlong.utils.DateUtil.calculate;

public class TimingMeasureSecActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    @BindView(R.id.iv_return)
    ImageView ivReturn;
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
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_startTime)
    SuperTextView tvStartTime;
    @BindView(R.id.tv_jgTime)
    SuperTextView tvJgTime;
    @BindView(R.id.tv_endTime)
    SuperTextView tvEndTime;
    @BindView(R.id.start_line)
    LinearLayout startLine;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.tv_ll_title)
    TextView tvLlTitle;
    private String strTitle = "";
    private String strStartTime = "";
    private String strEndTime = "";
    private String strJiange = "";
    private String strSetStartTime = "";
    private String strSetEndTime = "";
    private String strSetJiange = "";
    private String strDateTime=DateUtil.getNowDateTime();
    private String strTime = "";
    private String strInfo = "";
    private String strType = "";

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_measure_sec);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        strType = intent.getStringExtra("type");
        Constants.strFormActivity = strType;
        strTitle = intent.getStringExtra("wavelength");
        strStartTime = intent.getStringExtra("strStartTime");
        strEndTime = intent.getStringExtra("strEndTime");
        if (strEndTime == null) {
            strEndTime = "";
        }
        strJiange = intent.getStringExtra("jiange");
        tvTimer.setText(DateUtil.getDate());
        tvUser.setText(Constants.strLoginName);
        tvStartTime.setLeftString(strStartTime);
        tvJgTime.setLeftString(strJiange + "分钟");
        tvEndTime.setLeftString(strEndTime);
        strTime = "开始时间：" + strStartTime + "\n" +
                "时间间隔：" + strJiange + " 分钟" + "\n" +
                "结束时间：" + strEndTime + "\n";
        strInfo = intent.getStringExtra("strInfo");
        tvShow.setText(strInfo);
        if ("xiaozhun".equals(Constants.strFormActivity)) {
            tvLlTitle.setText(" 曲线校准");
        } else if ("time".equals(Constants.strFormActivity)) {
            tvLlTitle.setText(" 定时测量");
        }

    }

    @OnClick({R.id.tv_user, R.id.btn_calculate, R.id.iv_return, R.id.tv_return, R.id.cardview1, R.id.tv_startTime, R.id.tv_jgTime,
            R.id.tv_endTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.btn_calculate:
                tvTitle.setText(RandomUntil.getNum(25, 35) + ".000 mg/L");
                break;
            case R.id.iv_return:
            case R.id.tv_return:
                if ("time".equals(Constants.strFormActivity)) {
                    intent.setClass(this, TimingSetupActivity.class);
                    startActivity(intent);
                    this.finish();
                } else if ("xiaozhun".equals(Constants.strFormActivity)) {
                    intent.setClass(this, CurveSelectActivity.class);
                    startActivity(intent);
                    this.finish();
                }
                break;
            case R.id.cardview1:
                Dialog dialog = new Dialog(TimingMeasureSecActivity.this);
                dialog.setContentView(R.layout.history_dialog_layout);
                dialog.setTitle("历史记录");
                dialog.setCancelable(true);

                TextView text = (TextView) dialog.findViewById(R.id.tv_scorll);
                text.setText("9:00，C1=1.000 mg/L\n" +
                        "11:00，C2=1.100 mg/L\n" +
                        "13:00，C3=1.200 mg/L\n" +
                        "15:00，C4=0.900mg/L\n" +
                        "17:00，C5=0.800mg/L\n" +
                        "9:00，C1=1.000 mg/L\n" +
                        "11:00，C2=1.100 mg/L\n" +
                        "13:00，C3=1.200 mg/L\n" +
                        "15:00，C4=0.900mg/L\n" +
                        "17:00，C5=0.800mg/L\n" +
                        "9:00，C1=1.000 mg/L\n" +
                        "11:00，C2=1.100 mg/L\n" +
                        "13:00，C3=1.200 mg/L\n" +
                        "15:00，C4=0.900mg/L\n" +
                        "17:00，C5=0.800mg/L\n" +
                        "9:00，C1=1.000 mg/L\n" +
                        "11:00，C2=1.100 mg/L\n" +
                        "13:00，C3=1.200 mg/L\n" +
                        "15:00，C4=0.900mg/L\n" +
                        "17:00，C5=0.800mg/L\n");
                dialog.show();



                /*new MaterialDialog.Builder(this)
                        .title(R.string.materialDialog_title)
                        .iconRes(R.mipmap.icon_timing_24)
                        .content("9:00，C1=1.000 mg/L\n" +
                                "11:00，C2=1.100 mg/L\n" +
                                "13:00，C3=1.200 mg/L\n" +
                                "15:00，C4=0.900mg/L\n" +
                                "17:00，C5=0.800mg/L\n"+
                                "9:00，C1=1.000 mg/L\n" +
                                "11:00，C2=1.100 mg/L\n" +
                                "13:00，C3=1.200 mg/L\n" +
                                "15:00，C4=0.900mg/L\n" +
                                "17:00，C5=0.800mg/L\n")
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
                        .show();*/
                break;
            case R.id.tv_startTime:
                DateUtil.showTimePickerDialog(this, tvStartTime, calendar);
                DateUtil.showDatePickerDialog(this, 0, tvStartTime, calendar);
                strStartTime=tvStartTime.getLeftString()+" "+tvStartTime.getCenterString();

                break;
            case R.id.tv_jgTime:
                setTimeInterval();
                break;
            case R.id.tv_endTime:
                DateUtil.showTimePickerDialog(this, tvEndTime, calendar);
                DateUtil.showDatePickerDialog(this, 0, tvEndTime, calendar);
                strEndTime=tvEndTime.getLeftString()+" "+tvEndTime.getCenterString();
                break;

        }
    }

    @Override
    public void onBackPressed() {
        if ("time".equals(Constants.strFormActivity)) {
            intent.setClass(this, TimingSetupActivity.class);
            startActivity(intent);
            this.finish();
        } else if ("xiaozhun".equals(Constants.strFormActivity)) {
            intent.setClass(this, CurveSelectActivity.class);
            startActivity(intent);
            this.finish();
        }

    }

    private void setTimeInterval() {
        new MaterialDialog.Builder(this)
                .title(R.string.input_hint)
                .iconRes(R.mipmap.ic_warning_black_18dp)
                .content(R.string.input_prefill)
//                                .widgetColor(Color.BLUE)//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_NUMBER)//可以输入的类型-数字
                //前2个一个是hint一个是预输入的文字
                .input(R.string.input_hint, R.string.input_prefill2, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        if(Long.valueOf(input.toString())>(calculate(strStartTime, strEndTime)/60000)){
                            tvJgTime.setLeftString(strJiange + "分钟");
                            Log.i("TimingMeasureSec","error");
                        }else{
                            tvJgTime.setLeftString(input + "分钟");
                        }

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
                .show();
    }


}
