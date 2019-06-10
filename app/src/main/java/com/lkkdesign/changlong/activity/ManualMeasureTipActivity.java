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
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cardview0)
    CardView cardview0;
    private String strInfo = "";
    private Intent intent = new Intent();
    private String TAG = "ManualMeasureTipActivity";
    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有
    private int lineState = 1;//当前提示文字
    private String strfrom = "";

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
        strfrom = intent1.getStringExtra("from");
        Constants.strFormActivity=strfrom;
        Log.i(TAG, "strInfo=" + strInfo);
        tvUser.setText(Constants.strLoginName);
        tvCod.setText(strInfo);
        tvLine2.setVisibility(GONE);
        tvLine3.setVisibility(GONE);
        tvLine4.setVisibility(GONE);
        tvLine5.setVisibility(GONE);
        if("ManualMeasureFristActivity".equals(Constants.strFormActivity)){
            tvTitle.setText(R.string.tv_manual);
        }else if("InputDataActivity_qxjz".equals(Constants.strFormActivity)){
            lineState=4;
            tvLine1.setVisibility(GONE);
            tvLine4.setVisibility(View.VISIBLE);
        }else {
            tvTitle.setText(R.string.tv_cure_adjust);
        }
    }

    @OnClick({R.id.btn_empty, R.id.btn_measure, R.id.iv_return, R.id.tv_return})
    public void onViewClicked(View view) {
//        Intent intent = getIntent();
        switch (view.getId()) {
            case R.id.iv_return:
            case R.id.tv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_empty:
                booIsEmpty = true;
                btnEmpty.setVisibility(View.GONE);
                break;
            case R.id.btn_measure:
                switch (lineState) {
                    case 1:
                        tvLine2.setVisibility(View.VISIBLE);
                        tvLine1.setVisibility(View.GONE);
                        btnMeasure.setText("空 白");
                        lineState++;
                        break;
                    case 2:
                        /*if (booIsEmpty == false) {
                            CustomToast.showToast(getApplicationContext(), "请按照步骤执行");
                        } else {
                            tvLine3.setVisibility(View.VISIBLE);
                            tvLine2.setVisibility(View.GONE);
                            btnEmpty.setVisibility(View.GONE);
                            lineState++;
                        }*/
                        tvLine3.setVisibility(View.VISIBLE);
                        tvLine2.setVisibility(View.GONE);
                        btnMeasure.setText(R.string.next);
                        lineState++;
                        break;
                    case 3:
                        tvLine4.setVisibility(View.VISIBLE);
                        tvLine3.setVisibility(View.GONE);
                        lineState++;
                        break;
                    case 4:
                        tvLine5.setVisibility(View.VISIBLE);
                        tvLine4.setVisibility(View.GONE);
                        btnMeasure.setText(R.string.confirm);
                        lineState++;
                        break;
                    case 5:
                        intent.setClass(this, ManualMeasureSecActivity.class);
                        intent.putExtra("wavelength", strInfo);
                        intent.putExtra("from",strfrom);
                        startActivity(intent);
                        this.finish();
                        break;
                }
                break;
        }
    }

    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }
}
