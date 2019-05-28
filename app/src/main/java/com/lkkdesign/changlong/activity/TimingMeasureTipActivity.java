package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
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
    private String strStartTime = "";
    private String strEndTime = "";
    private String jiange = "";
    private String wavelength = "";
    private String strInfo = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_timing_measure_tip);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {

        tvUser.setText(Constants.strLoginName);
        wavelength = Constants.strWavelength;
        tvCod.setText(wavelength);

    }

    @OnClick({R.id.btn_empty, R.id.btn_measure,R.id.iv_return})
    public void onViewClicked(View view) {
        Intent intent=getIntent();
        switch (view.getId()) {
            case R.id.iv_return:
                intent.setClass(this, TimingSetupActivity.class);
                startActivity(intent);
                this.finish();
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
        intent.setClass(this, TimingSetupActivity.class);
        startActivity(intent);
        this.finish();
    }
}
