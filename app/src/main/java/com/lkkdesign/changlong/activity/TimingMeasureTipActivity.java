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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
    private String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_measure_tip);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent getIntent = getIntent();
        strType = getIntent.getStringExtra("type");
        Constants.strFormActivity = strType;
        Log.i("TimingMeasure", "strType" + Constants.strFormActivity);
        tvUser.setText(Constants.strLoginName);
        wavelength = Constants.strWavelength;
        tvCod.setText(wavelength);
        Intent intent = getIntent();
        if ("xiaozhun".equals(Constants.strFormActivity)) {
            tvTitle.setText(" 曲线校准");
        }else if("time".equals(Constants.strFormActivity)){
            tvTitle.setText(" 定时测量");
        }

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
                tvLine1.setVisibility(View.INVISIBLE);
                tvLine2.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_measure:
                strStartTime = intent.getStringExtra("strStartTime");
                strEndTime = intent.getStringExtra("strEndTime");
                jiange = intent.getStringExtra("jiange");
                strInfo = intent.getStringExtra("strInfo");
                intent.setClass(this, TimingMeasureSecActivity.class);
                intent.putExtra("type",strType);
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
