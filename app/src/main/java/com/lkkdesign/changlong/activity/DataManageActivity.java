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
import android.widget.TextView;


import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.DataDAO;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_data;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.data.model.Tb_data;
import com.lkkdesign.changlong.utils.DateUtil;

import org.angmarch.views.NiceSpinner;

import java.util.Arrays;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;


public class DataManageActivity extends AppCompatActivity {
    protected static final int DATE_DIALOG_ID = 0;

    @BindView(R.id.btn_return)
    Button btnReturn;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.addbtn)
    Button addbtn;
    @BindView(R.id.cancelbtn)
    Button cancelbtn;
    @BindView(R.id.initem)
    LinearLayout initem;
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
    @BindView(R.id.nice_spinner)
    NiceSpinner niceSpinner;


    private String strInfos = "";
    private String strType = "";
    private String strItem;
    private int int_id;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Intent intent = new Intent();

//    DataDAO dataDAO = new DataDAO(DataManageActivity.this);
    MeasureDao measureDao = new MeasureDao(DataManageActivity.this);
    private String strPrayTitle = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_manage);
        ButterKnife.bind(this);
        //Application.getInstance().addActivity(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        strInfos = bundle.getString("curve").replaceAll("曲线：", "");
        Log.i("MDActivity", "strInfos=" + strInfos);
        //initView();
    }

    private void initView() {

//        Tb_data tb_data = dataDAO.findByItem(strInfos);
        Tb_measure tb_measure = measureDao.findByItem(strInfos);
        int_id = tb_measure.get_id();
//        etClassic.setText(tb_data.getClassic());
        strItem = tb_measure.getItem();
        etName.setText(tb_measure.getName());
        etWavelength.setText("" + tb_measure.getWavelength());
        etAbsorbance.setText("" + tb_measure.getAbsorbance());
        etDensity.setText("" + tb_measure.getDensity());
        etTranrate.setText("" + tb_measure.getTranatre());
        txtInTime.setText(tb_measure.getTime());
        etmark.setText(tb_measure.getMark());

        strType = tb_measure.getClassic();

//        NiceSpinner niceSpinner = (NiceSpinner) findViewById(R.id.nice_spinner);
        String[] strName = getResources().getStringArray(R.array.fceliang_name);
        final List<String> dataset = new LinkedList<>(Arrays.asList(strName));
        niceSpinner.attachDataSource(dataset);
        if ("自动测量".equals(strType)) {
            niceSpinner.setSelectedIndex(0);
        } else if ("手动测量".equals(strType)) {
            niceSpinner.setSelectedIndex(1);
        } else {
            niceSpinner.setSelectedIndex(2);
        }

        niceSpinner.addOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Log.e("什么数据", String.valueOf(dataset.get(i)));
                CustomToast.showToast(getApplicationContext(), String.valueOf(dataset.get(i)));
                strType = String.valueOf(dataset.get(i));
            }
        });

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }

    @OnClick({R.id.addbtn, R.id.cancelbtn, R.id.txtInTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addbtn:
//                Tb_data tb_data = new Tb_data();
//                tb_data.set_id(int_id);
//                tb_data.setClassic(strType);
//                tb_data.setItem(strItem);
//                tb_data.setName(etName.getText().toString());
//                tb_data.setWavelength(Integer.parseInt(etWavelength.getText().toString()));
//                tb_data.setDensity(Float.parseFloat(etAbsorbance.getText().toString()));
//                tb_data.setTranatre(Float.parseFloat(etTranrate.getText().toString()));
//                tb_data.setAbsorbance(Float.parseFloat(etAbsorbance.getText().toString()));
//                tb_data.setTime(DateUtil.getNowDateTime());
//                tb_data.setMark(etmark.getText().toString());
//                Log.i("DMActivity","tb_data="+tb_data.toString());
//                dataDAO.update(tb_data);

                Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId()+1,
                        strType,//测量类别
                        "曲线"+DateUtil.getNowDateTime(),
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
                        etmark.getText().toString().trim());
                Log.i("DMActivity","tb_measure="+tb_measure.toString());
                measureDao.update(tb_measure);

                CustomToast.showToast(getApplicationContext(), "修改成功");
                intent.setClass(DataManageActivity.this, BaseSMRecycleViewActivity.class);
                startActivity(intent);
                DataManageActivity.this.finish();
                break;
            case R.id.cancelbtn:
                //dataDAO.detele(int_id);
                //CustomToast.showToast(DataManageActivity.this, "删除成功");
                intent.setClass(DataManageActivity.this, BaseSMRecycleViewActivity.class);
                startActivity(intent);
                DataManageActivity.this.finish();
                break;
            case R.id.txtInTime:
                showDialog(DATE_DIALOG_ID);
                break;
//            case R.id.btn_delete:
//                dataDAO.deteleByItemCode(strNum);
//                break;
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

    private void updateDisplay() {
        txtInTime.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(DataManageActivity.this, BaseSMRecycleViewActivity.class);
        startActivity(intent);
        DataManageActivity.this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataManageActivity.this.finish();
        Log.i("DataManageActivity", "DataManageActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        DataManageActivity.this.finish();
        Log.i("DataManageActivity", "DataManageActivity-->onDestroy()");
    }
}
