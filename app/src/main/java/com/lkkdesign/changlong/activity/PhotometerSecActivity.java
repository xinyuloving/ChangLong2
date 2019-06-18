package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.dao.PhotometerDao;
import com.lkkdesign.changlong.data.model.Tb_photometer;
import com.lkkdesign.changlong.printer.SearchBTActivity;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

public class PhotometerSecActivity extends AppCompatActivity {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_ll_title)
    TextView tvLlTitle;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.tv_show1)
    TextView tvShow1;
    @BindView(R.id.tv_show2)
    TextView tvShow2;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_TransmissionRate)
    TextView tvTransmissionRate;
    @BindView(R.id.tv_Current)
    TextView tvCurrent;
    @BindView(R.id.tv_Voltage)
    TextView tvVoltage;
    @BindView(R.id.tv_Temper)
    TextView tvTemper;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tv_result)
    TextView tvResult;
    @BindView(R.id.cardview33)
    CardView cardview33;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_blank)
    Button btnBlank;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;
    //    @BindView(R.id.btn_add)
//    FloatingActionButton btnAdd;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView rvCurve;
    //    @BindView(R.id.tv_line1)
//    TextView tvLine1;
//    @BindView(R.id.tv_line2)
//    TextView tvLine2;
//    @BindView(R.id.tv_line3)
//    TextView tvLine3;
//    @BindView(R.id.tv_line4)
//    TextView tvLine4;
//    @BindView(R.id.tv_line5)
//    TextView tvLine5;
    @BindView(R.id.fab_print)
    FloatingActionButton fabPrint;
    @BindView(R.id.tc_time)
    TextClock tcTime;

    private String strTitle = "";

    private Intent intent = new Intent();
    private String strInfo = "";
    private String strfrom = "";
    private MixSpeakUtil mixSpeakUtil;
    private boolean booIsPre = false;
    private float floTranrate = 0f;
    private float floAbsorbance = 0f;
    private String strType = "";
    private String strAValue = "";
    private String strCValue = "";
    private boolean booIsSave = false;
    private boolean booIsMeasure = false;
    private String strContent = "";

    private String strWavelength = "";//波长
    private String strAbsorbance = "";// 吸光度
    private String strTranatre = "";//透过率
    private String strCurrent = "";//电流
    private String strVoltage = "";//电压
    private String strTemperature = "";//温度
    private String strTime = "";//时间

    private boolean booEmptyTube = true;//是否放入空白比色管
    private boolean booEmptyTubeOut = true;//是否取出空白比色管
    private boolean booSampleTube = true;//是否放入样品


    /*数据列表*/
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    protected BaseAdapter mAdapter;
    protected List<String> mDataList;
    private List<String> dataList = new ArrayList<>();

    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有
    private int lineState = 1;//当前提示文字

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photometer_sec);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvUser.setText(Constants.strLoginName);
        mixSpeakUtil = MixSpeakUtil.getInstance(this);
        Intent intent = getIntent();
        strType = intent.getStringExtra("type");
        Constants.strFormActivity = strType;
        strTitle = intent.getStringExtra("wavelength");
        strfrom = intent.getStringExtra("from");
        strInfo = intent.getStringExtra("strInfo");
        tvCod.setText(strInfo);
        tvTimer.setText(DateUtil.getDate());
        if ("InputDataActivity".equals(strfrom)) {
            tvLlTitle.setText("数据输入");
            cardview3.setVisibility(View.GONE);
//            btnAdd.hide();
        } else if ("CurveMeasureActivity".equals(strfrom)) {
            tvLlTitle.setText("公式输入");
//            btnAdd.hide();
            cardview3.setVisibility(View.GONE);

        } else if ("CMActivity_ssjz".equals(strfrom)) {
            tvLlTitle.setText("曲线校准");
            cardview3.setVisibility(View.GONE);
        } else {
            tvLlTitle.setText("光度计");
            tvCod.setText("λ= " + strTitle + " nm");
            strWavelength = strTitle;
            if (booEmptyTube == true) {
                tvShow1.setText("请按空白键");
                btnBlank.setVisibility(View.VISIBLE);
                btnMeasure.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
            } else {
                tvShow1.setText("请放入空白比色管");
                btnMeasure.setVisibility(View.GONE);
                btnSave.setVisibility(View.GONE);
            }

            strTime = tcTime.getText().toString();


        }

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        rvCurve.setLayoutManager(mLayoutManager);
        rvCurve.addItemDecoration(mItemDecoration);

        rvCurve.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        rvCurve.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。
        rvCurve.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);
        rvCurve.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);



    }

    @OnClick({R.id.tv_user, R.id.tv_return, R.id.tv_cod, R.id.tv_result, R.id.tv_timer, R.id.btn_add,
            R.id.btn_blank, R.id.btn_save, R.id.fab_print, R.id.btn_measure})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.tv_return:
                onBackPressed();
                break;
            case R.id.tv_cod:
                break;
            case R.id.tv_result:
                break;
            case R.id.fab_print:
                if (true == booIsSave) {
                    printData(strContent);
                } else {
                    CustomToast.showToast(PhotometerSecActivity.this, "没有可打印的数据，请先保存");
                }
                break;
            case R.id.btn_add:

                final EditText inputServer = new EditText(this);
                inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("请输入C值：").setIcon(android.R.mipmap.sym_def_app_icon).setView(inputServer)
                        .setNegativeButton("Cancel", null);
                builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        strCValue = inputServer.getText().toString();
                        rvCurve.setVisibility(View.GONE);
                        tvShow1.setVisibility(View.VISIBLE);
                        tvShow1.setText("请放入空白比色管\n请按空白键\n请取出空白比色管\n请放入样品\n请按确认键");
                    }
                });
                builder.show();
                break;
            case R.id.btn_blank:
                if ("InputDataActivity".equals(strfrom) || "CurveMeasureActivity".equals(strfrom)) {
                    //tvShow1.setText("\n\n请取出空白比色管\n请放入样品\n请按确认键");
//                tvShow2.setVisibility(View.INVISIBLE);
                } else {
                    booEmptyTube = false;
                    btnBlank.setVisibility(View.GONE);
                    tvShow1.setText("请取出空白比色管");
                    if (booEmptyTubeOut == true) {
                        tvShow1.setText("请放入样品");
                        if (booSampleTube == true) {
                            tvShow1.setText("请按确认");
                            btnMeasure.setVisibility(View.VISIBLE);
                            btnMeasure.setText("确定");
                        }
                    }
                }
                break;

            case R.id.btn_measure:
                if ("InputDataActivity".equals(strfrom) || "CurveMeasureActivity".equals(strfrom)) {
                    tvTitle.setText(RandomUntil.getNum(25, 35) + ".000 mg/L");
                    strInfo = "综合排放标准 COD：\n" +
                            "A排放限值 ：20mg/L\n" +
                            "B排放限值 ：30mg/L\n";
                    tvShow1.setText(strInfo);
                    /*strInfo = "透过率（T）：" + RandomUntil.getNum(20) + ".00%\n" +
                            "吸光度（A）：1.0\n" +
                            "波长（λ）：610 nm\n" +
                            "温度：" + RandomUntil.getNum(25, 37) + " ℃\n";*/
                    tvResult.setText(strInfo);
                } else if ("CMActivity_ssjz".equals(strfrom)) {
                    tvTitle.setText("实时校准结果");
                    dataList.add("C=" + strCValue + "mg/L,\n" + "A=0.666");
                    strInfo = "";
                    tvResult.setText(strInfo);
                    //文字提示隐藏，数据列表显示
                    tvShow1.setVisibility(View.GONE);
                    rvCurve.setVisibility(View.VISIBLE);
                    if (strAValue.equals("0") && strCValue.equals("0")) {
                        intent.setClass(PhotometerSecActivity.this, PhotometerSecActivity.class);
                        intent.putExtra("from", "InputDataActivity");
                        intent.putExtra("wavelength", strTitle);
                        intent.putExtra("type", Constants.strFormActivity);
                        intent.putExtra("strInfo", strInfo);
                        startActivity(intent);
                    }
                } else {
                    if(booIsMeasure==true){
                        btnMeasure.setVisibility(View.GONE);
                        btnSave.setVisibility(View.VISIBLE);
                    }
                    btnMeasure.setText(R.string.celiang);
//                            tvLine5.setVisibility(View.GONE);
                    tvShow1.setVisibility(View.VISIBLE);
                    floTranrate = RandomUntil.getNum(10, 20);
                    Log.i("PSA", "floTranrate=" + floTranrate);
                    tvTransmissionRate.setText(floTranrate + "00%\n");
                    tvCurrent.setText(RandomUntil.getNum(100, 120) + "μA\n");
                    tvVoltage.setText(RandomUntil.getNum(10, 12) + "mV\n");
                    tvTemper.setText(RandomUntil.getNum(20, 25) + "℃\n");
                    floAbsorbance = getAbsorbance(floTranrate);
                    Log.i("PSA", "floAbsorbance=" + floAbsorbance);
                    strInfo = "A=" + (-1) * floAbsorbance;


                    strAbsorbance = (-1) * floAbsorbance + "";//吸光度
                    strTranatre = floTranrate + "00%";//透光率
                    strCurrent = RandomUntil.getNum(100, 120) + "";//电流
                    strVoltage = RandomUntil.getNum(10, 12) + "";//电压
                    strTemperature = RandomUntil.getNum(20, 25) + "";//温度

                    tvShow1.setText("吸光度");
                    tvShow2.setText(strInfo);
                    booIsMeasure = true;
                }

                mixSpeakUtil.speak("计算完成");
//                btnMeasure.setVisibility(View.INVISIBLE);
                break;
            case R.id.btn_save:
                saveData();
                break;
        }
    }


    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color_pa));
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    protected List<String> createDataList() {

        String[] listItem = getResources().getStringArray(R.array.list_shuju);//曲线列表
        for (int i = 0; i < listItem.length; i++) {
            dataList.add(listItem[i]);
        }
//        dataList.add("== 你已经看到我的底线啦 ==");
        return dataList;
    }


    private void saveData() {
        PhotometerDao photometerDao = new PhotometerDao(this);
        Tb_photometer tb_photometer = new Tb_photometer(photometerDao.getMaxId() + 1,
                null,//用户id
                strWavelength,//波长
                strAbsorbance,//吸光度
                strTranatre,//透过率
                strCurrent,//电流
                strVoltage,//电压
                strTemperature,//温度
                DateUtil.getNowDateTime()//时间
        );

        strContent = DateUtil.getNowDateTime2() + "光度计" + Constants.strLoginName
                + "\n测量结果：" + "A=" + strAbsorbance
                + "\n波长：" + strWavelength + "nm"
                + "\n透光率：" + strTranatre
                + "\n电流：" + strCurrent + "μA"
                + "\n电压：" + strVoltage + "mV"
                + "\n温度：" + strTemperature + "℃"
                + "\n时间：" + DateUtil.getNowDateTime()
                + "\n操作人：" + Constants.strLoginName;

        photometerDao.add(tb_photometer);
        // 信息提示
        CustomToast.showToast(getApplicationContext(), "数据保存成功");
        booIsMeasure = true;//保存数据之后改变状态，防止多次提交保存同一条数据
        booIsSave = true;
        btnMeasure.setVisibility(View.VISIBLE);
        /*btnSave.setVisibility(View.INVISIBLE);*/
    }

    private void printData(String strContent) {
        new MaterialDialog.Builder(PhotometerSecActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title("打印内容：")// 标题
                .content(strContent)// 内容
                .negativeText(R.string.cancel)
                .neutralText(R.string.print)
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(PhotometerSecActivity.this, SearchBTActivity.class);
                        intent.putExtra("printInfo", strContent); //传递需要打印的数据
                        intent.putExtra("type", "PhotometerSecActivity");//从何处跳转
                        startActivity(intent);
                        PhotometerSecActivity.this.finish();
                    }
                })
                .show();// 显示对话框
        booIsSave = false;
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        if ("InputDataActivity".equals(strfrom)) {
            intent.setClass(this, InputDataActivity.class);
            intent.putExtra("type", Constants.strFormActivity);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            this.finish();
        } else if ("CurveMeasureActivity".equals(strfrom)) {
            intent.setClass(this, CurveMeasureActivity.class);
            intent.putExtra("type", Constants.strFormActivity);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            this.finish();
        } else if ("CMActivity_ssjz".equals(strfrom)) {
            intent.setClass(this, CurveMeasureActivity.class);
            intent.putExtra("type", Constants.strFormActivity);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            this.finish();
        } else {
            intent.setClass(PhotometerSecActivity.this, PhotometerFristActivity.class);
            intent.putExtra("type", Constants.strFormActivity);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            PhotometerSecActivity.this.finish();
        }
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
