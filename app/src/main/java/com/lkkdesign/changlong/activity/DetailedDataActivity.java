package com.lkkdesign.changlong.activity;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.utils.CustomToast;

/**
 * 查看数据的列表
 */
public class DetailedDataActivity extends AppCompatActivity {

    private SuperTextView autoMeasurement;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailed_data);
        initView();
    }

    private void initView(){
        autoMeasurement = (SuperTextView) findViewById(R.id.autoMeasurement);
        final String strinfo = "曲线1.COD（0-100mg/L）\n检测光源波长λ=460nm\n浓度C=15mg/L\n透过率T=10%\n吸光度A=10.23";
        autoMeasurement.setOnSuperTextViewClickListener(new SuperTextView.OnSuperTextViewClickListener() {
            @Override
            public void onClickListener(SuperTextView superTextView) {
                showData("曲线1-2018.8.28-14:03:56",strinfo);
            }
        });
    }

    private void showData(String strTitle,String strinfo){
        //当接收到Click事件之后触发
        new MaterialDialog.Builder(DetailedDataActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title(strTitle)// 标题
                .content(strinfo)// 内容
                .positiveText(R.string.confirm)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();// 显示对话框

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
