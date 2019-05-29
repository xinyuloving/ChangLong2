package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class TimingSetupActivity extends AppCompatActivity {

    Calendar calendar = Calendar.getInstance();
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.startTime)
    SuperTextView startTime;
    @BindView(R.id.tv_startTime)
    SuperTextView tvStartTime;
    @BindView(R.id.endTime)
    SuperTextView endTime;
    @BindView(R.id.tv_endTime)
    SuperTextView tvEndTime;
    @BindView(R.id.timeInterval)
    SuperTextView timeInterval;
    @BindView(R.id.tv_timeInterval)
    SuperTextView tvTimeInterval;
    @BindView(R.id.btn_select_curve)
    Button btnSelectCurve;
    @BindView(R.id.tv_show_info)
    TextView tvShowInfo;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    private Intent intent = new Intent();
    private String TAG = "TimingSetupActivity";
    private String strStartTime = "";
    private String strEndTime = "";
    private String strJianGe = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_setup);

        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        tvUser.setText(Constants.strLoginName);

    }

    @OnClick({R.id.iv_return, R.id.tv_return, R.id.startTime, R.id.endTime, R.id.timeInterval, R.id.btn_select_curve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
            case R.id.tv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.startTime:
                DateUtil.showTimePickerDialog(this, tvStartTime, calendar);
                DateUtil.showDatePickerDialog(this, 0, tvStartTime, calendar);
                break;
            case R.id.endTime:
                DateUtil.showTimePickerDialog(this, tvEndTime, calendar);
                DateUtil.showDatePickerDialog(this, 0, tvEndTime, calendar);
                break;
            case R.id.timeInterval://时间间隔
                setTimeInterval();
                break;
            case R.id.btn_select_curve:
                strStartTime = tvStartTime.getLeftString().replaceAll("已选时间:", "") + " " + tvStartTime.getCenterString();
                strEndTime = tvEndTime.getLeftString().replaceAll("已选时间:", "") + " " + tvEndTime.getCenterString();
                strJianGe = tvTimeInterval.getLeftString().replaceAll("时间间隔：", "");
//                Log.i(TAG, "strStartTime=" + strStartTime);
//                Log.i(TAG, "strEndTime=" + strEndTime);
//                Log.i(TAG, "strJianGe=" + strJianGe);
                Long lonCalculate = calculate(strStartTime, strEndTime,strJianGe);
                Log.i(TAG, "calculate=" + lonCalculate);
                //1、先判断开始时间的合法性
                if (0 == lonCalculate) {
                    CustomToast.showToast(this, "请设置正确的时间和间隔");
                    return;
                } else if (lonCalculate > 0) {
//                    CustomToast.showToast(this, "请设置正确的开始时间");
//                    return;
                    intent.setClass(this, CurveSelectActivity.class);
                    intent.putExtra("type", "time");
                    intent.putExtra("startTime", strStartTime);
                    intent.putExtra("endTime", strEndTime);
                    intent.putExtra("jiange", strJianGe);
                }else{
                    intent.setClass(this, CurveSelectActivity.class);
                    intent.putExtra("type", "time");
                    intent.putExtra("startTime", strStartTime);
                    intent.putExtra("jiange", strJianGe);
                }

//                Long longDiff = DateUtil.getDateTime(strStartTime, strEndTime);
                /*strStartTime.length() > 0 && strEndTime.length() > 0 && strJianGe.length() > 0*/
//                if (strStartTime.length() > 0 && strEndTime.length() >= 0 && strJianGe.length() > 0) {
//                    Long longDiff = DateUtil.getDateTime(strStartTime, strEndTime);
//                    if (longDiff > 0) {
//                        intent.setClass(this, CurveSelectActivity.class);
//                        intent.putExtra("type", "time");
//                        intent.putExtra("startTime", strStartTime);
//                        intent.putExtra("endTime", strEndTime);
//                        intent.putExtra("jiange", strJianGe);
//                        //startActivity(intent);
//                    } else {
////                        CustomToast.showToast(this, "请选择正确的日期和时间");
//                        //CustomToast.showToast(this, "时间间隔：" + longDiff);
//
//                        intent.setClass(this, CurveSelectActivity.class);
//                        intent.putExtra("type", "time");
//                        intent.putExtra("startTime", strStartTime);
//                        intent.putExtra("jiange", strJianGe);
//                        //startActivity(intent);
//                    }
//                } else {
//                    CustomToast.showToast(this, "请设置正确的日期、时间或者时间间隔");
//                }

                break;

        }
    }

    private void setTimeInterval() {
        new MaterialDialog.Builder(TimingSetupActivity.this)
                .title(R.string.input_hint)
                .iconRes(R.mipmap.ic_warning_black_18dp)
                .content(R.string.input_prefill)
//                                .widgetColor(Color.BLUE)//输入框光标的颜色
                .inputType(InputType.TYPE_CLASS_NUMBER)//可以输入的类型-数字
                //前2个一个是hint一个是预输入的文字
                .input(R.string.input_hint, R.string.input_prefill2, new MaterialDialog.InputCallback() {

                    @Override
                    public void onInput(@NonNull MaterialDialog dialog, CharSequence input) {
                        tvTimeInterval.setLeftString("时间间隔：" + input);
                        timeInterval.setCenterString(input);
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

    /**
     * 没有开始时间，直接返回0
     * 有开始时间，先判断开始时间是否小于当前时间，小于当前时间返回0.大于零表示开始时间正确。
     *
     * @param strStartTime
     * @param strEndTime
     * @return long数值，0表示没有开始时间，大于零表示开始时间小于当前时间
     */
    private long calculate(String strStartTime, String strEndTime,String strJianGe) {
        Long longDiff = 0L;
        String strDateTime = DateUtil.getNowDateTime4();//获取系统时间
        Log.i(TAG, "strDateTime=" + strDateTime);
        if (" ".equals(strStartTime) || "".equals(strJianGe)) {//没有开始时间,直接提示
            Log.i(TAG, "没有开始时间！");
            longDiff = 0L;
        } else {//有开始时间
            Log.i(TAG, "strStartTime=" + strStartTime);
            //先判断开始时间是否小于当前时间,时间不合法
            if (DateUtil.getDateTime(strStartTime, strDateTime) > 0) {
                longDiff = 0L;
                return longDiff;
            }
            if (" ".equals(strEndTime)) {//（1）没有结束时间
                Log.i(TAG, "没有结束时间！");
                longDiff = DateUtil.getDateTime(strStartTime, strDateTime);
                return longDiff;
            } else {//有结束时间，先与当前时间做比较
                Log.i(TAG, "strEndTime=" + strEndTime);
                if (DateUtil.getDateTime(strEndTime, strDateTime) > 0) {
                    longDiff = 0L;
                    return longDiff;
                } else {//结束时间合法，开始时间与结束时间做比较
                    longDiff = DateUtil.getDateTime(strStartTime, strEndTime);
                    return longDiff;
                }
            }
        }
        return longDiff;
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
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
