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
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.hanks.htextview.rainbow.RainbowTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PhotometerFristActivity extends AppCompatActivity {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_title_toolbar)
    TextView tvTitleToolbar;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rtv_list)
    RainbowTextView rtvList;
    @BindView(R.id.radiobutton_420)
    RadioButton radiobutton420;
    @BindView(R.id.radiobutton_460)
    RadioButton radiobutton460;
    @BindView(R.id.radiobutton_540)
    RadioButton radiobutton540;
    @BindView(R.id.radiobutton_620)
    RadioButton radiobutton620;
    @BindView(R.id.radiobutton_700)
    RadioButton radiobutton700;
    @BindView(R.id.radiogroup)
    RadioGroup radiogroup;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    private Intent intent = new Intent();
    private String strPhotometer = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photometer_frist);
        ButterKnife.bind(this);

        initView();
        monitoringRadioGrop();
    }

    private void initView() {

        tvTimer.setText(DateUtil.getDate());
        tvUser.setText(Constants.strLoginName);
    }

    private void monitoringRadioGrop() {
        radiogroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.radiobutton_420:
                        CustomToast.showToast(PhotometerFristActivity.this, "λ= 420 nm");
                        strPhotometer = "420";
                        //tvResult.setText("透过率（T）：100%\n吸光度（A）：0.000\n波长（λ）：420 nm\n温度: 25℃\n");
                        break;
                    case R.id.radiobutton_460:
                        CustomToast.showToast(PhotometerFristActivity.this, "λ= 460 nm");
                        //tvResult.setText("透过率（T）：100%\n吸光度（A）：0.000\n波长（λ）：460 nm\n温度: 25℃\n");
                        strPhotometer = "460";
                        break;
                    case R.id.radiobutton_540:
                        CustomToast.showToast(PhotometerFristActivity.this, "λ= 540 nm");
                        //tvResult.setText("透过率（T）：100%\n吸光度（A）：0.000\n波长（λ）：540 nm\n温度: 25℃\n");
                        strPhotometer = "540";
                        break;
                    case R.id.radiobutton_620:
                        CustomToast.showToast(PhotometerFristActivity.this, "λ= 620 nm");
                        //tvResult.setText("透过率（T）：100%\n吸光度（A）：0.000\n波长（λ）：620 nm\n温度: 25℃\n");
                        strPhotometer = "620";
                        break;
                    case R.id.radiobutton_700:
                        CustomToast.showToast(PhotometerFristActivity.this, "λ= 700 nm");
                        // tvResult.setText("透过率（T）：100%\n吸光度（A）：0.000\n波长（λ）：700 nm\n温度: 25℃\n");
                        strPhotometer = "700";
                        break;
                    default:
                        Log.d("PhotometerFristActivity", "怎么监听的????");
                        break;
                }
            }
        });
    }

    @OnClick({R.id.tv_return, R.id.tv_timer, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
//            case R.id.tv_show:
//                intent.setClass(this, PhotometerActivity.class);
//                intent.putExtra("type", "photometer");//光度计
//                startActivity(intent);
//                break;
//            case R.id.tv_result:
//                break;
            case R.id.tv_timer:
                break;
            case R.id.btn_calculate:
                strPhotometer = (strPhotometer.length() <= 0) ? "420" : strPhotometer;
                if (strPhotometer.length() > 0) {
//                    intent.setClass(this, PhotometerActivity.class);
//                    intent.putExtra("type", "photometer");//光度计
//                    startActivity(intent);

                    intent.setClass(this, PhotometerSecActivity.class);
                    intent.putExtra("strfrom","PhotometerFristActivity");
                    intent.putExtra("wavelength", strPhotometer);
                    startActivity(intent);
                    PhotometerFristActivity.this.finish();
                } else {
                    CustomToast.showToast(PhotometerFristActivity.this, "请选择曲波长");
                }

                break;
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(PhotometerFristActivity.this, Main2Activity.class);
        startActivity(intent);
        PhotometerFristActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        PhotometerFristActivity.this.finish();
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        PhotometerFristActivity.this.finish();
    }


}
