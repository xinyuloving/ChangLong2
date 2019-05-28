package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanks.htextview.rainbow.RainbowTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.utils.DateUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CureAdjustActivity extends AppCompatActivity {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.rtv_list)
    RainbowTextView rtvList;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;

    private Intent intent= new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cure_adjust);
        ButterKnife.bind(this);
        tvTimer.setText(DateUtil.getDate());
    }

    @OnClick({R.id.tv_show, R.id.tv_result, R.id.tv_timer, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_show:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", "CureAdjust");
                startActivity(intent);
                break;
            case R.id.tv_result:
                break;
            case R.id.tv_timer:
                break;
            case R.id.btn_calculate:
                break;
        }
    }
}
