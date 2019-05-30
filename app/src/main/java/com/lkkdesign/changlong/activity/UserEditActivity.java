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

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.data.dao.UserDao;
import com.lkkdesign.changlong.data.model.Tb_user;
import com.lkkdesign.changlong.utils.CustomToast;
import java.util.Calendar;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class UserEditActivity extends AppCompatActivity {

    protected static final int DATE_DIALOG_ID = 0;

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.etName)
    EditText etName;
    @BindView(R.id.etPWD)
    EditText etPWD;
//    @BindView(R.id.etJobNo)
//    EditText etJobNo;
    @BindView(R.id.etCompany)
    EditText etCompany;
    @BindView(R.id.etAddress)
    EditText etAddress;
    @BindView(R.id.etContact)
    EditText etContact;
    @BindView(R.id.txtInTime)
    EditText txtInTime;
    @BindView(R.id.addbtn)
    Button addbtn;
    @BindView(R.id.cancelbtn)
    Button cancelbtn;
    @BindView(R.id.initem)
    LinearLayout initem;

    private int intId = 0;
    private String strType = "";
    private String strItem;
    private int int_id;
    private int mYear;
    private int mMonth;
    private int mDay;
    private Intent intent = new Intent();

    UserDao userDao = new UserDao(UserEditActivity.this);
    private Tb_user tb_user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_edit);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        intId = bundle.getInt("id");
        Log.i("MDActivity", "intId=" + intId);
        initView();
    }

    private void initView() {

        tb_user = userDao.findByID(""+intId);
        int_id = tb_user.get_id();
        etName.setText(tb_user.getName());
        etPWD.setText(tb_user.getPassword());
//        etJobNo.setText(tb_user.getJobNo());
        etCompany.setText(tb_user.getCompany());
        etAddress.setText(tb_user.getAddress());
        txtInTime.setText(tb_user.getTime());
        etContact.setText(tb_user.getContact());

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        updateDisplay();
    }


    @OnClick({R.id.iv_return, R.id.addbtn, R.id.cancelbtn,R.id.txtInTime})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                intent.setClass(this,UserInfoActivity.class);
                startActivity(intent);
                UserEditActivity.this.finish();
                break;
            case R.id.addbtn:
                String strName = etName.getText().toString().replaceAll(" ","");
//                String strJobNo = etJobNo.getText().toString().replaceAll(" ","");
//                if(strName.length() > 0 && strJobNo.length() >0) {
                if(strName.length() > 0 ) {
                    tb_user = new Tb_user();
                    tb_user.set_id(int_id);
                    tb_user.setAddress(etAddress.getText().toString());
                    tb_user.setCompany(etCompany.getText().toString());
                    tb_user.setContact(etContact.getText().toString());
                    tb_user.setJobNo("");
                    tb_user.setName(etName.getText().toString());
                    tb_user.setPassword(etPWD.getText().toString());
                    tb_user.setTime(txtInTime.getText().toString());

                    Log.i("UEActivity", "tb_user=" + tb_user.toString());
                    userDao.update(tb_user);
                    CustomToast.showToast(getApplicationContext(), "修改成功");
                    intent.setClass(this, UserInfoActivity.class);
                    startActivity(intent);
                    this.finish();
                }else{
                    CustomToast.showToast(getApplicationContext(), "请先填写姓名、工号");
                }

                break;
            case R.id.cancelbtn:
                etName.setText("");
//                etJobNo.setText("");
                etPWD.setText("");
                etCompany.setText("");
                etAddress.setText("");
                txtInTime.setText("");
                etContact.setText("");
                break;
            case R.id.txtInTime:
                showDialog(DATE_DIALOG_ID);
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

    private void updateDisplay() {
        txtInTime.setText(new StringBuilder().append(mYear).append("-").append(mMonth + 1).append("-").append(mDay));
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserEditActivity.this.finish();
        Log.i("UserEditActivity", "UserEditActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {

        super.onDestroy();
        UserEditActivity.this.finish();
        Log.i("UserEditActivity", "UserEditActivity-->onDestroy()");
    }
}
