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
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;

public class TimingMeasureTipActivity extends AppCompatActivity {


    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_line1)
    TextView tvLine1;
    @BindView(R.id.tv_line2)
    TextView tvLine2;
    @BindView(R.id.tv_line3)
    TextView tvLine3;
    @BindView(R.id.tv_line4)
    TextView tvLine4;
    @BindView(R.id.tv_line5)
    TextView tvLine5;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.btn_empty)
    Button btnEmpty;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cardview0)
    CardView cardview0;
    private String strStartTime = "";
    private String strEndTime = "";
    private String jiange = "";
    private String wavelength = "";
    private String strInfo = "";
    private String strFrom = "";
    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有
    private int lineState = 1;//当前提示文字

    private boolean booEmptyTube = true;//是否放入空白比色管
    private boolean booEmptyTubeOut = true;//是否取出空白比色管
    private boolean booSampleTube = true;//是否放入样品

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_measure_tip);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent getIntent = getIntent();
        strFrom = getIntent.getStringExtra("from");
        Constants.strFormActivity = strFrom;
        Log.i("TimingMeasure", "strType" + Constants.strFormActivity);
        tvUser.setText(Constants.strLoginName);
        wavelength = Constants.strWavelength;
        tvCod.setText(wavelength);
        Intent intent = getIntent();
        if ("xiaozhun".equals(Constants.strFormActivity)) {
            tvTitle.setText(" 曲线校准");
        } else if ("time".equals(Constants.strFormActivity)) {
            tvTitle.setText(" 定时测量");
        }
        if (booEmptyTube == true) {
            tvLine1.setText("请按空白键");
            btnEmpty.setVisibility(View.VISIBLE);
            btnMeasure.setVisibility(View.GONE);
        } else {
            tvLine1.setText("请放入空白比色管");
            btnMeasure.setVisibility(View.GONE);
        }
        tvLine2.setVisibility(GONE);
        tvLine3.setVisibility(GONE);
        tvLine4.setVisibility(GONE);
        tvLine5.setVisibility(GONE);

    }

    @OnClick({R.id.btn_empty, R.id.btn_measure, R.id.tv_return})
    public void onViewClicked(View view) {
        Intent intent = getIntent();
        switch (view.getId()) {
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
            case R.id.btn_empty:
                booEmptyTube = false;
                btnEmpty.setVisibility(View.GONE);
                tvLine1.setText("请取出空白比色管");
                if (booEmptyTubeOut == true) {
                    tvLine1.setText("请放入样品");
                    if (booSampleTube == true) {
                        tvLine1.setText("请按确认");
                        btnMeasure.setVisibility(View.VISIBLE);
                        btnMeasure.setText("确定");
                    }
                }
                break;
            case R.id.btn_measure:
                strStartTime = intent.getStringExtra("strStartTime");
                strEndTime = intent.getStringExtra("strEndTime");
                jiange = intent.getStringExtra("jiange");
                strInfo = intent.getStringExtra("strInfo");
                intent.setClass(this, TimingMeasureSecActivity.class);
                intent.putExtra("from", strFrom);
                intent.putExtra("strStartTime", strStartTime);
                intent.putExtra("strEndTime", strEndTime);
                intent.putExtra("jiange", jiange);
                intent.putExtra("wavelength", wavelength);
                intent.putExtra("strInfo", strInfo);
                startActivity(intent);
                this.finish();
                break;
        }


    }



    public void onBackPressed() {
        Intent intent = getIntent();
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
}
