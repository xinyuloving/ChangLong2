package com.lkkdesign.changlong.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_data;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.data.dao.DataDAO;
import com.lkkdesign.changlong.utils.DateUtil;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 新增应急数据页面
 */
public class DataAddActivity extends AppCompatActivity {
    protected static final int DATE_DIALOG_ID = 0;
    protected static final String TAG = "DataAddActivity";
    private int mYear;
    private int mMonth;
    private int mDay;

    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etWavelength)
    EditText etWavelength;
    @BindView(R.id.etAbsorbance)
    EditText etAbsorbance;
    @BindView(R.id.etDensity)
    EditText etDensity;
    @BindView(R.id.etTranrate)
    EditText etTranrate;
    @BindView(R.id.txtInTime)
    EditText txtInTime;
    @BindView(R.id.etmark)
    EditText etmark;
    @BindView(R.id.addbtn)
    Button addbtn;
    @BindView(R.id.cancelbtn)
    Button cancelbtn;
    @BindView(R.id.initem)
    LinearLayout initem;
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;

    private String strPrayTitle = "自动测量";
    private String strType = "自动测量";
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_add);
        ButterKnife.bind(this);
        //Application.getInstance().addActivity(this);

        initView();
    }

    private void initView() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();

        String[] strName = getResources().getStringArray(R.array.fceliang_name);
        final List<String> dataset = new LinkedList<>(Arrays.asList(strName));
        niceSpinner.attachDataSource(dataset);
        niceSpinner.setSelectedIndex(0);

        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("什么数据", String.valueOf(dataset.get(i)));
                CustomToast.showToast(getApplicationContext(), String.valueOf(dataset.get(i)));
                strType = String.valueOf(dataset.get(i));
            }
        });

    }


    @OnClick({R.id.addbtn, R.id.cancelbtn, R.id.txtInTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.txtInTime:
                showDialog(DATE_DIALOG_ID);
                break;
            case R.id.addbtn:
                saveData();
                Log.i(TAG, "添加按钮事件");
                break;
            case R.id.cancelbtn:
//                etClassic.setText("");
                etName.setText("");
                etWavelength.setText("");
                etAbsorbance.setText("");
                etDensity.setText("");
                etTranrate.setText("");
                txtInTime.setHint(DateUtil.getNowDateTime());
                etmark.setText("");
                CustomToast.showToast(getApplicationContext(), "取消添加");
                Intent intent = new Intent(DataAddActivity.this, BaseSMRecycleViewActivity.class);
                startActivity(intent);
                DataAddActivity.this.finish();
                break;
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG_ID:
                return new DatePickerDialog(this, mDateSetListener, mYear, mMonth, mDay);
        }
        return null;
    }

    private DatePickerDialog.OnDateSetListener mDateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            updateDisplay();
        }
    };

    /**
     * 添加数据之前先查询一次数据库中已存在的数据
     *
     * @return []
     */
    private String[] getdataInfo() {
        String[] strInfos = null;
        MeasureDao measureinfo = new MeasureDao(DataAddActivity.this);
        //遍历一次已有的数据
        List<Tb_measure> listinfos = measureinfo.getScrollData(0, (int) measureinfo.getCount());//第二版
        strInfos = new String[listinfos.size()];//
        int m = 0;//
        for (Tb_measure tb_measure : listinfos) {//
            strInfos[m] = tb_measure.get_id() + "";
            Log.i(TAG, "strInfos=" + tb_measure.getItem());
            Log.i(TAG, "strInfos=" + tb_measure.getName());
            Log.i(TAG, "strInfos=" + tb_measure.getClassic());
            m++;//
        }
        return strInfos;
    }

    private void saveData() {
        StringBuffer sb = new StringBuffer();
        String stritemSum = etDensity.getText().toString().trim();
        Log.i(TAG, "stritemSum=" + stritemSum);
        Log.i(TAG, "titlem=" + strPrayTitle);

        MeasureDao measureDao = new MeasureDao(DataAddActivity.this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId()+1,
                strType,//测量类别
                "曲线"+DateUtil.getNowDateTime2(),
                etName.getText().toString().trim(),//曲线名称
                Integer.parseInt(etWavelength.getText().toString().trim()),//曲线波长
                Float.parseFloat(etDensity.getText().toString().trim()),//密度
                Float.parseFloat(etTranrate.getText().toString().trim()),//透过率
                Float.parseFloat(etAbsorbance.getText().toString().trim()),//吸光度
                Constants.strLoginName,//操作员
                "COD（0-100 mg/L）",//COD（0-100 mg/L）
                "C=15.000 mg/L",//测量结果
                "温度℃",//温度
                DateUtil.getNowDateTime(),//时间
                etmark.getText().toString().trim()
                );
        Log.i(TAG,"保存数据="+tb_measure.toString());
        measureDao.add(tb_measure);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "添加成功");
        Intent intent = new Intent(DataAddActivity.this, BaseSMRecycleViewActivity.class);
        startActivity(intent);
        DataAddActivity.this.finish();

    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(DataAddActivity.this, BaseSMRecycleViewActivity.class);
        startActivity(intent);
        DataAddActivity.this.finish();
    }

    private void updateDisplay() {
        txtInTime.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataAddActivity.this.finish();
        Log.i("DataAddActivity", "DataAddActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        DataAddActivity.this.finish();
        Log.i("DataAddActivity", "DataAddActivity-->onDestroy()");
    }
}
