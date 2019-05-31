package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.baidutts.util.MixSpeakUtil;
import com.lkkdesign.changlong.config.Constants;
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

import static android.view.View.GONE;
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
    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView rvCurve;
    @BindView(R.id.tv_line1)
    TextView tvLine1;
    @BindView(R.id.tv_line2)
    TextView tvLine2;
    @BindView(R.id.tv_line3)
    TextView tvLine3;
    @BindView(R.id.tv_line4)
    TextView tvLine4;
    @BindView(R.id.tv_line5)
    TextView tvLine5;
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

    /*数据列表*/
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    protected BaseAdapter mAdapter;
    protected List<String> mDataList;
    private List<String> dataList = new ArrayList<>();

    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有
    private int lineState=1;//当前提示文字

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
            btnAdd.hide();
        } else if ("CurveMeasureActivity".equals(strfrom)) {
            tvLlTitle.setText("公式输入");
            btnAdd.hide();
            cardview3.setVisibility(View.GONE);

        } else if ("CMActivity_ssjz".equals(strfrom)) {
            tvLlTitle.setText("曲线校准");
            cardview3.setVisibility(View.GONE);
        } else{
            tvLlTitle.setText("光度计");
            tvCod.setText("λ= " + strTitle + " nm");
            tvLine2.setVisibility(GONE);
            tvLine3.setVisibility(GONE);
            tvLine4.setVisibility(GONE);
            tvLine5.setVisibility(GONE);
            btnAdd.hide();
            btnSave.setText(R.string.next);
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

    @OnClick({R.id.tv_user, R.id.tv_return, R.id.tv_cod, R.id.tv_result, R.id.tv_timer, R.id.btn_add, R.id.btn_blank, R.id.btn_save})
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
            case R.id.tv_timer:
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
                    booIsEmpty=true;
                    btnBlank.setVisibility(View.GONE);
                }


                break;
            case R.id.btn_save:
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
                } else{
                    switch (lineState){
                        case 1:
                            tvLine2.setVisibility(View.VISIBLE);
                            tvLine1.setVisibility(View.GONE);
                            btnBlank.setVisibility(View.VISIBLE);
                            lineState++;
                            break;
                        case 2:
                            if(booIsEmpty==false){
                                CustomToast.showToast(getApplicationContext(), "请按照步骤执行");
                            }else {
                                tvLine3.setVisibility(View.VISIBLE);
                                tvLine2.setVisibility(View.GONE);
                                btnBlank.setVisibility(View.GONE);
                                lineState++;
                            }
                            break;
                        case 3:
                            tvLine4.setVisibility(View.VISIBLE);
                            tvLine3.setVisibility(View.GONE);
                            lineState++;
                            break;
                        case 4:
                            tvLine5.setVisibility(View.VISIBLE);
                            tvLine4.setVisibility(View.GONE);
                            btnSave.setText(R.string.confirm);
                            lineState++;
                            break;
                        case 5:
                            btnSave.setText(R.string.celiang);
                            tvLine5.setVisibility(View.GONE);
                            tvShow1.setVisibility(View.VISIBLE);
                            floTranrate = RandomUntil.getNum(10, 20);
                            Log.i("PSA", "floTranrate=" + floTranrate);
                            tvTransmissionRate.setText(floTranrate + "00%\n");
                            tvCurrent.setText(RandomUntil.getNum(100, 120) + "μA\n");
                            tvVoltage.setText(RandomUntil.getNum(10, 12) + "mV\n");
                            tvTemper.setText(RandomUntil.getNum(20, 25) + "℃\n");
                            floAbsorbance = getAbsorbance(floTranrate);
                            Log.i("PSA", "floAbsorbance=" + floAbsorbance);
                            strInfo = "吸光度\nA=" + (-1) * floAbsorbance;
                            tvShow1.setText(strInfo);
                            tvShow1.setGravity(Gravity.CENTER);
                            tvShow1.setTextSize(getResources().getDimension(R.dimen.textsize_24));
                            tvShow1.setTextColor(getResources().getColor(R.color.statusBarColor));
                            break;
                    }
                   /* if (false == booIsPre) {
                        CustomToast.showToast(this, "请按步骤执行");
                        return;
                    }*/

//                    strInfo = "透过率（T）：" + RandomUntil.getNum(10, 20) + ".00%\n" +
//                            "电流：" + RandomUntil.getNum(85, 100) + " μA\n" +
//                            "电压：10 mV\n" +
//                            "温度:25℃\n";
//                    tvResult.setText(strInfo);
                }
                mixSpeakUtil.speak("计算完成");
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
