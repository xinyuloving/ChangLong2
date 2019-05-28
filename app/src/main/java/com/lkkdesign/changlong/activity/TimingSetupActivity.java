package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
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

    private String strJiange = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_setup);

        //绑定初始化ButterKnife
        ButterKnife.bind(this);
        tvUser.setText(Constants.strLoginName);

//        initView();

    }

//    private void initView() {
//        niceSpinner.setTextColor(getResources().getColor(R.color.black));
//        String[] strTime = getResources().getStringArray(R.array.countdown_time_key);
//        final List<String> dataset = new LinkedList<>(Arrays.asList(strTime));
//        niceSpinner.attachDataSource(dataset);
//        niceSpinner.setTextInternal("0");
//
//        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                CustomToast.showToast(getApplicationContext(), "时间间隔：" + String.valueOf(dataset.get(i)) + "分钟");
//                strJiange = String.valueOf(dataset.get(i));
//            }
//        });
//    }

    @OnClick({R.id.iv_return, R.id.startTime, R.id.endTime, R.id.timeInterval, R.id.btn_select_curve})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
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
                DateUtil.showDatePickerDialog(this, 2, tvEndTime, calendar);
                break;
            case R.id.timeInterval:
                setTimeInterval();
                break;
            case R.id.btn_select_curve:
                String strStartTime = tvStartTime.getLeftString().replaceAll("已选时间:", "") + " " + tvStartTime.getCenterString();
                String strEndTime = tvEndTime.getLeftString().replaceAll("已选时间:", "") + " " + tvEndTime.getCenterString();
                String strJianGe = tvTimeInterval.getLeftString().replaceAll("时间间隔：", "");
                Long longDiff = DateUtil.getDateTime(strStartTime, strEndTime);
                /*strStartTime.length() > 0 && strEndTime.length() > 0 && strJianGe.length() > 0*/
                if (strStartTime.length() > 0 && strEndTime.length() >= 0 && strJianGe.length() > 0) {
                    if (longDiff > 0) {
                        intent.setClass(this, CurveSelectActivity.class);
                        intent.putExtra("type", "time");
                        intent.putExtra("startTime", strStartTime);
                        intent.putExtra("endTime", strEndTime);
                        intent.putExtra("jiange", strJianGe);
                        startActivity(intent);
                    } else {
                        //CustomToast.showToast(this, "请选择正确的日期和时间");
                        //CustomToast.showToast(this, "时间间隔：" + longDiff);
                        intent.setClass(this, CurveSelectActivity.class);
                        intent.putExtra("type", "time");
                        intent.putExtra("startTime", strStartTime);
                        intent.putExtra("jiange", strJianGe);
                        startActivity(intent);
                    }
                } else {
                    CustomToast.showToast(this, "请设置正确的日期、时间或者时间间隔");
                }

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

    private void calculate() {
        String strStartTime = tvStartTime.getLeftString() + " " + tvStartTime.getCenterString();
        String strEndTime = tvEndTime.getLeftString() + " " + tvEndTime.getLeftString();
        Long longDiff = DateUtil.getDateTime(strStartTime, strEndTime);

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
