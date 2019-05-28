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

public class ManualMeasureTipActivity extends AppCompatActivity {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_cod)
    TextView tvCod;
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
    private String strInfo = "";
    private Intent intent = new Intent();
    private String TAG = "ManualMeasureTipActivity";
    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_measure_tip);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        Intent intent1 = getIntent();
        strInfo = intent1.getStringExtra("wavelength");
        Log.i(TAG, "strInfo=" + strInfo);
        tvUser.setText(Constants.strLoginName);
        tvCod.setText(strInfo);
    }

    @OnClick({R.id.btn_empty, R.id.btn_measure, R.id.iv_return,R.id.tv_return})
    public void onViewClicked(View view) {
//        Intent intent = getIntent();
        switch (view.getId()) {
            case R.id.iv_return:
            case R.id.tv_return:
                intent.setClass(this, ManualMeasureFristActivity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_empty:
                booIsEmpty = true;
                tvLine1.setVisibility(View.INVISIBLE);
                tvLine2.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_measure:
                if(false == booIsEmpty){
                    CustomToast.showToast(this,"请按步骤执行");
                    return;
                }
                intent.setClass(this, ManualMeasureSecActivity.class);
                intent.putExtra("wavelength", strInfo);
                startActivity(intent);
                this.finish();
                break;
        }
    }

    public void onBackPressed() {
        intent.setClass(this, ManualMeasureFristActivity.class);
        startActivity(intent);
        this.finish();
    }
}
