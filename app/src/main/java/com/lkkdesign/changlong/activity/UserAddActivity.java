package com.lkkdesign.changlong.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.data.dao.UserDao;
import com.lkkdesign.changlong.data.model.Tb_user;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;

import org.apache.commons.lang3.ArrayUtils;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserAddActivity extends AppCompatActivity {

    @BindView(R.id.textView9)
    TextView textView9;
    @BindView(R.id.textView11)
    TextView textView11;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.textView2)
    TextView textView2;
//    @BindView(R.id.etJobNo)
//    EditText etJobNo;
    @BindView(R.id.et_pwd)
    EditText etPWD;
    @BindView(R.id.textView5)
    TextView textView5;
    @BindView(R.id.etCompany)
    EditText etCompany;
    @BindView(R.id.textView4)
    TextView textView4;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.textView3)
    TextView textView3;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.textView6)
    TextView textView6;
    @BindView(R.id.txtInTime)
    EditText txtInTime;
    @BindView(R.id.addbtn)
    Button addbtn;
    @BindView(R.id.cancelbtn)
    Button cancelbtn;
    @BindView(R.id.initem)
    LinearLayout initem;
    @BindView(R.id.iv_return)
    ImageView ivReturn;

    protected static final int DATE_DIALOG_ID = 0;
    protected static final String FLG = "UserAddActivity";
    private int mYear;
    private int mMonth;
    private int mDay;
    private int mHour;
    private int mMinute;
    private int mSecond;
    private String[] datainfo;
    private Intent intent = new Intent();
    private String strType = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_add);
        ButterKnife.bind(this);

        Intent getIntent = getIntent();
        strType = getIntent.getStringExtra("type");

        initView();
    }

    private void initView() {

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        mSecond = c.get(Calendar.SECOND);
        datainfo = getdataInfo();
        Log.i(FLG,"存在的数据="+ Arrays.toString(datainfo));
        updateDisplay();

    }

    @OnClick({R.id.addbtn, R.id.cancelbtn,R.id.iv_return})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.addbtn:
//                if(etJobNo.getText().toString().length() > 0 || etName.getText().toString().length() > 0
//                        || etPWD.getText().toString().length() > 0) {
                    if( etName.getText().toString().isEmpty() || etPWD.getText().toString().isEmpty()) {
                        CustomToast.showToast(this,"请输入完整的用户信息");
                    }else{
                        saveData();
                    }
//                }
                break;
            case R.id.cancelbtn:
                if("login".equals(strType)) {
                    intent.setClass(UserAddActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(UserAddActivity.this, UserInfoActivity.class);
                }
                startActivity(intent);
                UserAddActivity.this.finish();
                break;
            case R.id.iv_return:
                if("login".equals(strType)) {
                    intent.setClass(UserAddActivity.this, LoginActivity.class);
                }else{
                    intent.setClass(UserAddActivity.this, UserInfoActivity.class);
                }
                startActivity(intent);
                UserAddActivity.this.finish();
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
     * @return String 数组
     */
    private String[] getdataInfo() {
        String[] strInfos = null;
        UserDao userDaoInfo = new UserDao(UserAddActivity.this);
        //遍历一次已有的数据
        List<Tb_user> listinfos = userDaoInfo.getScrollData(0, (int) userDaoInfo.getCount());
        strInfos = new String[listinfos.size()];//
        int m = 0;//
        for (Tb_user tb_user : listinfos) {//
            strInfos[m] = tb_user.getName() + "";
            m++;//
        }
        return strInfos;
    }

    /**
     * 保存用户数据
     */
    private void saveData() {
        StringBuffer sb = new StringBuffer();
//        String strJobNo = etJobNo.getText().toString().trim();
        String strName = etName.getText().toString().trim();
        Log.i(FLG, "strName=" + strName);

        if(true == ArrayUtils.contains(datainfo,strName)){
            // 信息提示
            CustomToast.showToast(getApplicationContext(), "工号已存在，请检查");
            return;
        }

        UserDao userDao = new UserDao(UserAddActivity.this);
        Tb_user tb_user = new Tb_user(userDao.getMaxId() + 1,
                etName.getText().toString().trim(),
                etPWD.getText().toString().trim(),
//                etJobNo.getText().toString().trim(),
                "",
                etCompany.getText().toString().trim(),
                etContact.getText().toString().trim(),
                etAddress.getText().toString().trim(),
                DateUtil.getNowDateTime()
        );
        userDao.add(tb_user);//保存数据
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "添加成功");
        if("login".equals(strType)) {
            intent.setClass(UserAddActivity.this, LoginActivity.class);
        }else{
            intent.setClass(UserAddActivity.this, UserInfoActivity.class);
        }
//        Intent intent = new Intent(UserAddActivity.this, UserInfoActivity.class);
        startActivity(intent);
        UserAddActivity.this.finish();

    }

    private void updateDisplay() {
        txtInTime.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserAddActivity.this.finish();
        Log.i("UserAddActivity", "UserAddActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        UserAddActivity.this.finish();
        Log.i("UserAddActivity", "UserAddActivity-->onDestroy()");
    }
}
