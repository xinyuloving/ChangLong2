package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.allen.library.SuperTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.UserDao;
import com.lkkdesign.changlong.data.model.Tb_user;
import com.lkkdesign.changlong.printer.SearchBTActivity;
import com.lkkdesign.changlong.utils.CustomToast;

import org.apache.commons.lang3.ArrayUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.et_Name)
    EditText etName;
    @BindView(R.id.et_pwd)
    EditText etPwd;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.stv_registe)
    SuperTextView stvRegiste;

    private String TAG = "LoginActivity";
    private Intent intent = new Intent();
    private String[] datainfo;
    private int intUserCount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        initView();

    }

    private void initView(){
        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
        etName.setText(settings.getString("Username", "").toString());
        etPwd.setText(settings.getString("Password", "").toString());

    }

    @OnClick({R.id.btn_login, R.id.stv_registe})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                String strName = etName.getText().toString().replaceAll(" ", "");
                String strPassword = etPwd.getText().toString().replaceAll(" ", "");
//                datainfo = getUserInfo(strJobNo, strPassword);
                intUserCount = getUserInfo(strName, strPassword);
                if (strName.isEmpty() || strPassword.isEmpty()) {
                    CustomToast.showToast(getApplicationContext(), "登录信息不能为空");
                }
                if (strName.length() > 0 && strPassword.length() > 0) {
//                    if (true == ArrayUtils.contains(datainfo, strJobNo)) {//如果存在，跳转至主页面
                    if (1 == intUserCount) {//如果存在，跳转至主页面

                        SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                        SharedPreferences.Editor editor = settings.edit();
                        editor.putString("Username",strName);
                        editor.putString("Password",strPassword);
                        editor.commit();

                        // 信息提示
                        CustomToast.showToast(getApplicationContext(), "欢迎使用水质检测仪");
                        Constants.strLoginName = strName;
                        intent.setClass(LoginActivity.this, Main2Activity.class);
                        intent.putExtra("userName", strName);
                        startActivity(intent);
                    } else {//如果不存在,弹窗提示
                        showDialog();
                    }
                } else {
                    CustomToast.showToast(this, "请输入完整的登录信息");
                }
                break;
            case R.id.stv_registe:
                intent.setClass(LoginActivity.this, UserAddActivity.class);
                intent.putExtra("type", "login");
                startActivity(intent);
                break;
        }
    }

    /**
     * 用户ID不存在是，弹窗提示
     */
    private void showDialog() {

        new MaterialDialog.Builder(LoginActivity.this)// 初始化建造者
//                .iconRes(R.mipmap.icon_app)
                .title(R.string.dialog_title)// 标题
                .content(R.string.login_content_err)// 内容
                .positiveText(R.string.gotoInput)
//                .negativeText(R.string.cancel)
                .neutralText(R.string.gotoRegedit)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(LoginActivity.this, UserAddActivity.class);
                        intent.putExtra("type", "login");
                        startActivity(intent);
                    }
                })
                .show();// 显示对话框
    }

    /**
     * 查询数据库中已存在的数据
     *
     * @return String 数组
     */
    private int getUserInfo(String name, String password) {
        Log.i(TAG, "name=" + name);
        Log.i(TAG, "password=" + password);
        int intCount;
        UserDao userDaoInfo = new UserDao(LoginActivity.this);
        //遍历一次已有的数据
        intCount = userDaoInfo.findByNameAndPassword(name, password);
        Log.i(TAG, "intCount=" + intCount);
        return intCount;
    }

    /**
     * 查询数据库中已存在的数据
     *
     * @return String 数组
     */
//    private String[] getUserInfo(String jobNo, String password) {
//        String[] strInfos = null;
//        UserDao userDaoInfo = new UserDao(LoginActivity.this);
//        //遍历一次已有的数据
//        List<Tb_user> listinfos = userDaoInfo.getScrollData(0, (int) userDaoInfo.getCount());
//        strInfos = new String[listinfos.size()];//
//        int m = 0;//
//        for (Tb_user tb_user : listinfos) {//
//            strInfos[m] = tb_user.getJobNo() + "";
//            m++;//
//        }
//        return strInfos;
//    }
    @Override
    protected void onStop() {
        super.onStop();
        LoginActivity.this.finish();
        Log.i("LoginActivity", "LoginActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        LoginActivity.this.finish();
        Log.i("LoginActivity", "LoginActivity-->onDestroy()");
    }
}
