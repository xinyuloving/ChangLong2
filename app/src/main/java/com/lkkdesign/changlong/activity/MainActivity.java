package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
//import com.lkkdesign.changlong.base.CLApplication;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.StatusBarUtil;
//import com.squareup.haha.perflib.Main;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.linearlayout)
    LinearLayout linearlayout;
    @BindView(R.id.tv_show)
    TextView tvShow;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawer;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.tv_timer)
    TextView tvTimer;

    private String strTitle = "自动测量";
    private String strInfo = "";
    private String strUserName = "";

//    private DrawerLayout drawer;
    private int mStatusBarColor;
    private Intent intent = new Intent();

    private String strCurve = "";
    private final static int REQUESTCODE = 1; // 返回的结果码

    private MixSpeakUtil mixSpeakUtil;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //绑定初始化ButterKnife
        ButterKnife.bind(this);
//        Intent getIntent = getIntent();
//        strUserName = getIntent.getStringExtra("userName");
//        tvUser.setText(strUserName);

//        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//        setStatusBar();
//        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
//        toolbar.setTitle("Admin");
//        setSupportActionBar(toolbar);


//        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
//                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
//        drawer.setDrawerListener(toggle);
//        toggle.setDrawerIndicatorEnabled(false);//禁止默认图标
//        toggle.setHomeAsUpIndicator(R.mipmap.icon_logo_while);//自定义图标
//        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
//            @Override
//            //绑定点击事件 这里可以优化代码。
//            public void onClick(View v) {
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.openDrawer(GravityCompat.START);
//            }
//        });
//        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        navigationView.setItemIconTintList(null);//菜单项图标显示彩色

        mixSpeakUtil=MixSpeakUtil.getInstance(this);

        initView();

    }

    private void initView(){
        tvUser.setText(Constants.strLoginName);
        tvTimer.setText(DateUtil.getDate());
    }

    @OnClick({R.id.tv_title, R.id.linearlayout, R.id.tv_show, R.id.btn_measure, R.id.btn_calculate, R.id.btn_save,
                R.id.tv_user,R.id.tv_cod,R.id.tv_result,R.id.tv_timer})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_title:
                intent.setClass(MainActivity.this, CurveSelectActivity.class);
                intent.putExtra("type", "main");
                startActivityForResult(intent, REQUESTCODE); //REQUESTCODE--->1
                break;
            case R.id.linearlayout:
                intent.setClass(MainActivity.this, CurveSelectActivity.class);
                intent.putExtra("type", "main");
                startActivityForResult(intent, REQUESTCODE); //REQUESTCODE--->1
                break;
            case R.id.tv_show:
                break;
            case R.id.tv_timer:
                intent.setClass(MainActivity.this, TimingSetupActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_result:
                intent.setClass(MainActivity.this, PhotometerActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_cod:
                intent.setClass(MainActivity.this, CurveSelectActivity.class);
                intent.putExtra("type", "manual");
                startActivity(intent);
                break;
            case R.id.tv_user:
                intent.setClass(MainActivity.this,UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.btn_measure:
                strInfo = "综合排放标准 COD：\n" +
                        "A排放限值 ：20mg/L\n" +
                        "B排放限值 ：30mg/L\n";
                    tvShow.setText(strInfo);
                mixSpeakUtil.speak("正在测量");
//                DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
//                drawer.openDrawer(GravityCompat.START);//打开抽屉
                break;
            case R.id.btn_calculate:
                tvTitle.setText("C=15000.000 mg/L");
                strInfo = "综合排放标准 COD：\n" +
                        "A排放限值 ：20mg/L\n" +
                        "B排放限值 ：30mg/L\n";
                tvShow.setText(strInfo);
                strInfo = "C=15.000 mg/L\n透过率（T）：10%\n" +
                        "吸光度（A）：1.0\n" +
                        "波长（λ）：610 nm\n" +
                        "温度：25℃\n";
                tvResult.setText(strInfo);
//                play("计算完成");
                mixSpeakUtil.speak("计算完成");

                showData("");
                break;
            case R.id.btn_save:

                break;
        }
    }

    // 为了获取结果
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        // RESULT_OK，判断另外一个activity已经结束数据输入功能，Standard activity result:
        // operation succeeded. 默认值是-1
        if (resultCode == 2) {
            if (requestCode == REQUESTCODE) {
                strCurve = data.getStringExtra("wavelength");
                //设置结果显示框的显示数值
                tvTitle.setText(strCurve);
            }
        }
    }

    private void showData(String string) {
        //当接收到Click事件之后触发
        new MaterialDialog.Builder(MainActivity.this)// 初始化建造者
                .icon(getResources().getDrawable(R.mipmap.icon_save))
                .title(strTitle)// 标题
                .content("是否保存当前测量结果？\n\n" + string)// 内容
                .positiveText(R.string.save)
                .negativeText("")
                .neutralText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        addMeasureData();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();// 显示对话框

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //    @Override
    protected void setStatusBar() {
        mStatusBarColor = getResources().getColor(R.color.statusBarColor);
        StatusBarUtil.setColorNoTranslucentForDrawerLayout(this, (DrawerLayout) findViewById(R.id.drawer_layout), mStatusBarColor);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_auto) {//自动测量
            intent.setClass(MainActivity.this, AutoMeasureActivity.class);
            intent.putExtra("type", "Auto");
            startActivity(intent);

        }else if (id == R.id.nav_manual) {//手动测量
//            intent.setClass(MainActivity.this, CurveSelectActivity.class);
            intent.setClass(MainActivity.this, ManualMeasureFristActivity.class);
            intent.putExtra("type", "manual");
            startActivity(intent);

        } else if (id == R.id.nav_time) {//定时测量
            intent.setClass(MainActivity.this, TimingSetupActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_photometer) {//光度计
//            intent.setClass(MainActivity.this, PhotometerActivity.class);
            intent.setClass(MainActivity.this, PhotometerFristActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_curve) {//曲线校准
            intent.setClass(MainActivity.this, CurveSelectActivity.class);
            intent.putExtra("type", "curve");
            startActivity(intent);
        } else if (id == R.id.nav_DataView) {
            //查看数据
            intent.setClass(MainActivity.this, BaseSMRecycleViewActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_EmergencyReport) {
            //应急报告
            intent.setClass(MainActivity.this, LineChartActivity.class);
            startActivity(intent);

        } else if (id == R.id.exit) {
            /* @setIcon 设置对话框图标
             * @setTitle 设置对话框标题
             * @setMessage 设置对话框消息提示
             * setXXX方法返回Dialog对象，因此可以链式设置属性
             */
            final AlertDialog.Builder normalDialog =
                    new AlertDialog.Builder(MainActivity.this);
            normalDialog.setIcon(R.mipmap.icon_app);
            normalDialog.setTitle("温馨提示");
            normalDialog.setMessage("确定退出当前应用？");
            normalDialog.setPositiveButton("确定",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            android.os.Process.killProcess(android.os.Process.myPid());
                            System.exit(0);
                        }
                    });
            normalDialog.setNegativeButton("取消",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //...To-do
                        }
                    });
            // 显示
            normalDialog.show();
        } else if (id == R.id.about) {
            intent.setClass(MainActivity.this, AboutActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.help) {
            intent.setClass(MainActivity.this, UserInfoActivity.class);
            startActivity(intent);
            MainActivity.this.finish();
        } else if (id == R.id.setting) {
            intent.setClass(MainActivity.this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void addMeasureData(){
        MeasureDao measureDao = new MeasureDao(this);
        Tb_measure tb_measure = new Tb_measure(measureDao.getMaxId() + 1,"自动测量", "","String item", "String name", 140, 10.4f,
                1.20f, 100.6f, "String userId", "COD（0-100 mg/L）", "C=15.000 mg/L",
                "25.20℃", DateUtil.getNowDateTime(), "备注","","","","","");
        measureDao.add(tb_measure);
        CustomToast.showToast(this,"测量数据保存成功");
    }

//    @Override
//    protected void onStop() {
//        super.onStop();
//        MainActivity.this.finish();
//        Log.i("MainActivity", "MainActivity-->onStop()");
//    }
//
//    @Override
//    protected void onDestroy() {
//
//        super.onDestroy();
//        MainActivity.this.finish();
//        Log.i("MainActivity", "MainActivity-->onDestroy()");
//    }
}
