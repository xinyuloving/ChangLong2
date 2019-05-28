package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;

/**
 *
 */
public class ViewDataActivity extends BaseActivity {

    private SuperTextView tvShow, autoMeasurement, manualMeasurement, timeMeasurement;

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_data);

        initView();
    }

    private void initView() {
        tvShow = (SuperTextView) findViewById(R.id.tv_show);
        autoMeasurement = (SuperTextView) findViewById(R.id.autoMeasurement);
        manualMeasurement = (SuperTextView) findViewById(R.id.manualMeasurement);
        timeMeasurement = (SuperTextView) findViewById(R.id.timeMeasurement);

        tvShow.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                Toast.makeText(ViewDataActivity.this, "整个item的点击事件", Toast.LENGTH_SHORT).show();
                intent.setClass(ViewDataActivity.this, MainActivity.class);
                startActivity(intent);
                ViewDataActivity.this.finish();
            }
        });

        autoMeasurement.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                Toast.makeText(ViewDataActivity.this, "详情页面", Toast.LENGTH_SHORT).show();
                intent.setClass(ViewDataActivity.this, DataInfoActivity.class);
                startActivity(intent);
                ViewDataActivity.this.finish();
            }
        });

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
