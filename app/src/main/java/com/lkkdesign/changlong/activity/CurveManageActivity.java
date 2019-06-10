package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class CurveManageActivity extends AppCompatActivity {

    @BindView(R.id.iv_return)
    ImageView ivRturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etXishi)
    EditText etXishi;
    @BindView(R.id.stv_open)
    SuperTextView stvOpen;
    @BindView(R.id.stv_close)
    SuperTextView stvClose;
    @BindView(R.id.etTranrate)
    EditText etTranrate;
    @BindView(R.id.ns_fbl)
    NiceSpinner nsFbl;
    @BindView(R.id.et_jiaozhun)
    EditText etJiaozhun;
    @BindView(R.id.addbtn)
    Button addbtn;
    @BindView(R.id.cancelbtn)
    Button cancelbtn;
    @BindView(R.id.initem)
    LinearLayout initem;

    private Intent intent = new Intent();
    private String strType="";
    private String strCurve="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_manage);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        Intent intent = getIntent();
        strType = intent.getStringExtra("type");
        strCurve=intent.getStringExtra("curve");
        Constants.strFormActivity = strType;
        String[] strName = getResources().getStringArray(R.array.resolution_ratio);
        final List<String> dataset = new LinkedList<>(Arrays.asList(strName));
        nsFbl.attachDataSource(dataset);
//        if ("自动测量".equals(strType)) {
//            niceSpinner.setSelectedIndex(0);
//        } else if ("手动测量".equals(strType)) {
//            niceSpinner.setSelectedIndex(1);
//        } else {
//            niceSpinner.setSelectedIndex(2);
//        }
        etName.setText(strCurve);
        etXishi.setText("1*1=1mg/L");

        nsFbl.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("什么数据", String.valueOf(dataset.get(i)));
                CustomToast.showToast(getApplicationContext(), String.valueOf(dataset.get(i)));
//                strType = String.valueOf(dataset.get(i));
            }
        });
    }

    @OnClick({R.id.iv_return, R.id.tv_title, R.id.etName, R.id.etXishi, R.id.stv_open, R.id.stv_close, R.id.etTranrate, R.id.ns_fbl,
            R.id.et_jiaozhun, R.id.addbtn, R.id.cancelbtn, R.id.initem})
    public void onViewClicked(View view) {
        Intent intent = getIntent();
        switch (view.getId()) {
            case R.id.iv_return:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", Constants.strFormActivity);
                startActivity(intent);
                this.finish();
                break;
            case R.id.tv_title:
                break;
            case R.id.etName:
                break;
            case R.id.etXishi:
                break;
            case R.id.stv_open:
                break;
            case R.id.stv_close:
                break;
            case R.id.etTranrate:
                break;
            case R.id.ns_fbl:
                break;
            case R.id.et_jiaozhun:
                break;
            case R.id.addbtn:
                break;
            case R.id.cancelbtn:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", Constants.strFormActivity);
                startActivity(intent);
                this.finish();
                break;
            case R.id.initem:
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        CurveManageActivity.this.finish();
    }

    public void onBackPressed() {
        intent.setClass(this, CurveSelectActivity.class);
        intent.putExtra("type", Constants.strFormActivity);
        startActivity(intent);
        this.finish();

    }
    @Override
    protected void onDestroy() {

        super.onDestroy();
        this.finish();
    }
}
