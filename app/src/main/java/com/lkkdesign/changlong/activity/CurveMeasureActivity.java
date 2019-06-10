package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 曲线校准--空白、测量、计算、保存
 */
public class CurveMeasureActivity extends AppCompatActivity {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.rb_shuju)
    RadioButton rbShuju;
    @BindView(R.id.rb_gongsi)
    RadioButton rbGongsi;
    @BindView(R.id.rb_ssxz)
    RadioButton rbSsxz;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_result2)
    TextView tvResult2;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.tv_result)
    TextView tvResult;
    private String strTitle = "";
    private String strInfo = "";
    private int intSelectFun = 0;
    private String strType="";
    private Intent intent = new Intent();

    private Boolean booformula=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_measure);
        //绑定初始化ButterKnife
        ButterKnife.bind(this);

        initView();
        monitoringRadioGrop();
    }

    private void initView() {

        Intent intent = getIntent();
        strType = intent.getStringExtra("type");
        Constants.strFormActivity = strType;
        strTitle = intent.getStringExtra("wavelength");
        strInfo = intent.getStringExtra("strInfo");
        tvUser.setText(Constants.strLoginName);
        tvTimer.setText(DateUtil.getDate());
        tvResult2.setText(strInfo);

//        tvCod.setText(strTitle);
//        tvTitle.setText(strTitle);
//        tvShow.setText(R.string.tv_show);
    }

    private void monitoringRadioGrop() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb_shuju:
                        CustomToast.showToast(CurveMeasureActivity.this, "已选择：数据输入");
                        intSelectFun = 1;
                        break;
                    case R.id.rb_gongsi:
//                        CustomToast.showToast(CurveMeasureActivity.this, "公式输入");
                        alert_edit();
                        intSelectFun = 2;
                        break;
                    case R.id.rb_ssxz:
                        CustomToast.showToast(CurveMeasureActivity.this, "已选择：实时校准");
                        intSelectFun = 3;
                        break;
                    default:
                        Log.d("CurveMeasureActivity", "怎么监听的????");
                        break;
                }
            }
        });
    }

    //当接收到Click事件之后触发
    public void alert_edit() {
        final EditText et = new EditText(this);
        new AlertDialog.Builder(this).setTitle("请输入公式：C=kA+b\n或 A=Kc+b")
                .setIcon(android.R.drawable.sym_def_app_icon)
                .setView(et)
                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //按下确定键后的事件
                        //view2.setVisibility(View.VISIBLE);
                        if(et.getText().toString().isEmpty()){
                            CustomToast.showToast(getApplicationContext(), "不可输入空值！");
                            booformula=true;
                        }else{
                            tvResult2.setText("输入的公式：\n" + et.getText().toString());
                            booformula=false;
                        }

                        //Toast.makeText(getApplicationContext(), et.getText().toString(), Toast.LENGTH_LONG).show();
                    }
                }).setNegativeButton("取消", null).show();
    }


    @OnClick({R.id.tv_return,R.id.tv_user, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", Constants.strFormActivity);
                startActivity(intent);
                this.finish();
                break;
            case R.id.tv_user:
                break;
            case R.id.btn_calculate:
                if (intSelectFun > 0) {
                    if (1 == intSelectFun) {
                        intent.setClass(this, InputDataActivity.class);
                        intent.putExtra("type",Constants.strFormActivity);
                        intent.putExtra("from","InputDataActivity");
                        intent.putExtra("strInfo", strInfo);
                        intent.putExtra("wavelength", strTitle);
                        startActivity(intent);
                    } else if (2 == intSelectFun) {
                        if(booformula){
                            CustomToast.showToast(getApplicationContext(), "当前公式为空！");

                        }else{
                            intent.setClass(this,ManualMeasureTipActivity.class);
                            intent.putExtra("type",Constants.strFormActivity);
                            intent.putExtra("from", "InputDataActivity");
                            intent.putExtra("strInfo", strInfo);
                            intent.putExtra("wavelength", strInfo);
                            startActivity(intent);
                        }

                    } else {
                        intent.setClass(this, InputDataActivity.class);
                        intent.putExtra("type",Constants.strFormActivity);
                        intent.putExtra("from", "CMActivity_ssjz");
                        intent.putExtra("strInfo", strInfo);
                        intent.putExtra("wavelength", strTitle);
                        startActivity(intent);
                    }

                } else {
                    CustomToast.showToast(CurveMeasureActivity.this, "请选择校准模式");
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, CurveSelectActivity.class);
        intent.putExtra("type", Constants.strFormActivity);
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
