package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.printer.AppStart;
import com.lkkdesign.changlong.utils.CustomToast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SendEmailActivity extends AppCompatActivity {

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.et_email_addr)
    EditText etEmailAddr;
    @BindView(R.id.et_email_theme)
    EditText etEmailTheme;
    @BindView(R.id.et_email_fujian)
    EditText etEmailFujian;
    @BindView(R.id.et_email_cont)
    EditText etEmailCont;
    @BindView(R.id.btn_login)
    Button btnLogin;

    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_email);
        ButterKnife.bind(this);
        tvUser.setText(Constants.strLoginName);
    }

    @OnClick(R.id.btn_login)
    public void onViewClicked() {
        sendEmail();
    }

    private void sendEmail() {
//        Intent intent = new Intent(Intent.ACTION_SEND_MULTIPLE);
//        String[] tos = { "223453797@qq.com" };
//        String[] ccs = { "254570317@qq.com" };
//        String[] bccs = {"xinyuloving@163.com"};
//        intent.putExtra(Intent.EXTRA_EMAIL, tos);//收件者
//        intent.putExtra(Intent.EXTRA_CC, ccs);//抄送者
//        intent.putExtra(Intent.EXTRA_CC, bccs);//密送者
//        intent.putExtra(Intent.EXTRA_TEXT, "body");
//        intent.putExtra(Intent.EXTRA_SUBJECT, "subject");
//
//        ArrayList<uri> imageUris = new ArrayList<uri>();
//        imageUris.add(Uri.parse("file:///mnt/sdcard/a.jpg"));
//        imageUris.add(Uri.parse("file:///mnt/sdcard/b.jpg"));
//        intent.putParcelableArrayListExtra(Intent.EXTRA_STREAM, imageUris);
//        intent.setType("image/*");
//        intent.setType("message/rfc882");
//        Intent.createChooser(intent, "Choose Email Client");
//        startActivity(intent);
        Intent data = new Intent(Intent.ACTION_SENDTO);
        if (data.resolveActivity(getPackageManager()) != null) {
            data.setData(Uri.parse("2234537965@qq.com"));
            data.putExtra(Intent.EXTRA_SUBJECT, "这是标题");
            data.putExtra(Intent.EXTRA_TEXT, "这是内容");
            startActivity(data);
        } else {
            CustomToast.showToast(SendEmailActivity.this, "没有安卓原生GMAIL，不能发送邮件！");
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(SendEmailActivity.this, ListEmReportActivity.class);
        startActivity(intent);
        SendEmailActivity.this.finish();
    }
}
