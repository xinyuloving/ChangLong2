package com.lkkdesign.changlong.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.LeastSquares;
import com.lkkdesign.changlong.utils.RandomUntil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeItemLongClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.lkkdesign.changlong.config.Constants.df_4;
import static com.lkkdesign.changlong.utils.MyFunc.calculateTransmittance;
import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

public class CurveMeasureInputActivity extends AppCompatActivity implements SwipeItemClickListener {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.tv_show_data)
    TextView tvShowData;
    @BindView(R.id.cardview3)
    CardView cardview3;
    @BindView(R.id.tc_time)
    TextClock tcTime;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;

    private Intent intent = new Intent();
    private String strTitle = "";
    private String strInfo = "";
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    private String TAG = "CMIActivity.this";
    private String strAValue = "";
    private String strCValue = "";
    private String strType = "";
    private String strfrom = "";
    private int intPosition = 0;
    private List<String> dataList = new ArrayList<>();
    private List<Double> ListAddAValue = new ArrayList<>();//添加值 光度计
    private List<Double> ListAddCValue = new ArrayList<>();//添加值 浓度

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_measure_input);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        Intent intent = getIntent();
        strType = intent.getStringExtra("type");
        Constants.strFormActivity = strType;
        strTitle = intent.getStringExtra("wavelength");
        strInfo = intent.getStringExtra("strInfo");
        strfrom = intent.getStringExtra("from");

        tvUser.setText(Constants.strLoginName);
        tvCod.setText(strInfo);

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(CurveMeasureInputActivity.this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。

        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeItemLongClickListener(swipeItemLongClickListener);//Item的Menu长点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

    }

    @OnClick({R.id.tv_return, R.id.btn_add, R.id.tv_show_data, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", Constants.strFormActivity);
                intent.putExtra("strInfo", strInfo);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_add:
                alertDialog("add");//弹出窗口，添加曲线
                break;
            case R.id.tv_show_data:
                showData();
                break;
            case R.id.btn_calculate:

//                Double[] x = ListAddCValue.toArray(new Double[ListAddCValue.size()]);
//                Double[] y = ListAddAValue.toArray(new Double[ListAddAValue.size()]);
                Double[] x ={0.0904,0.166,0.2453,0.3327,0.3969,0.4987,0.7102,0.8803,1.2546};
                Double[] y ={0d,0.2,0.4,0.6,0.8,1d,1.5,2d,3d};
//                Double[] x1 ={0.01,0.049,0.11,0.21,0.5};//吸光度
//                Double[] y1 ={1d,5d,10d,20d,50d};//浓度
//
//                Double[] x2 ={0.01, 0.049, 0.11, 0.21, 0.5};//吸光度
//                Double[] y2 ={1.0, 5.0, 10.0, 20.0, 50.0};//浓度


                Log.i(TAG, "Double[] x =" + Arrays.toString(x));
                Log.i(TAG, "Double[] y =" + Arrays.toString(y));
                Double a = LeastSquares.getA(x, y);
                Double b = LeastSquares.getB(x, y);
                tvShowData.setText("C=" + df_4.format(a) + "A+" + df_4.format(b));
                Log.i(TAG, "k值：=" + a);
                Log.i(TAG, "b值：" + b);
                Log.i(TAG, "格式化k值：=" + df_4.format(a));
                Log.i(TAG, "格式化b值：" + df_4.format(b));

//                Log.i(TAG, "Double[] x1 =" + Arrays.toString(x1));
//                Log.i(TAG, "Double[] y1 =" + Arrays.toString(y1));
//                Double a1 = LeastSquares.getA(x1, y1);
//                Double b1 = LeastSquares.getB(x1, y1);
//                tvShowData.setText("k值：" + df_4.format(a1) + "\t\tb值：" + df_4.format(b1));
//                Log.i(TAG, "k1值：=" + a1);
//                Log.i(TAG, "b1值：" + b1);
//                Log.i(TAG, "格式化k1值：=" + df_4.format(a1));
//                Log.i(TAG, "格式化b1值：" + df_4.format(b1));
//
//                Log.i(TAG, "Double[] x2 =" + Arrays.toString(x2));
//                Log.i(TAG, "Double[] y2 =" + Arrays.toString(y2));
//                Double a2 = LeastSquares.getA(x2, y2);
//                Double b2 = LeastSquares.getB(x2, y2);
//                tvShowData.setText("k值：" + df_4.format(a1) + "\t\tb值：" + df_4.format(b1));
//                Log.i(TAG, "k2值：=" + a2);
//                Log.i(TAG, "b2值：" + b2);
//                Log.i(TAG, "格式化k2值：=" + df_4.format(a2));
//                Log.i(TAG, "格式化b2值：" + df_4.format(b2));

//                new AlertDialog.Builder(this)
//                        .setTitle("保存")
//                        .setMessage("保存吗？")
//                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                intent.setClass(CurveMeasureInputActivity.this, ManualMeasureTipActivity.class);
//                                intent.putExtra("from", "CurveMeasureInputActivity");
//                                intent.putExtra("wavelength", strInfo);
//                                intent.putExtra("type", Constants.strFormActivity);
//                                intent.putExtra("strInfo", strInfo);
//                                startActivity(intent);
//                            }
//                        })
//                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//
//                            }
//                        })
//                        .show();
                break;
        }
    }

    /**
     * RecyclerView的Item的Menu长按点击监听。
     */
    private SwipeItemLongClickListener swipeItemLongClickListener = new SwipeItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int i) {
//            intent.setClass(InputDataActivity.this, CurveManageActivity.class);
//            intent.putExtra("curve", mDataList.get(i)); //将计算的值回传回去
//            startActivity(intent);
//            InputDataActivity.this.finish();
//            CustomToast.showToast(CurveMeasureInputActivity.this, mDataList.get(i));
//            CustomToast.showToast(CurveMeasureInputActivity.this, "i = "+i);
//            intPosition = i;
//            alertDialog("modify");
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                mDataList.remove(position);
                mAdapter.notifyItemRemoved(position);

                Log.i(TAG, "position=" + position);
                Log.i(TAG, "mDataList=" + mDataList.toString());
                Log.i(TAG, "Curve=" + mDataList.get(menuPosition));
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "menuPosition=" + menuPosition);

                intent.setClass(CurveMeasureInputActivity.this, CurveManageActivity.class);
                intent.putExtra("curve", mDataList.get(menuPosition)); //将计算的值回传回去
                startActivity(intent);
                CurveMeasureInputActivity.this.finish();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {
                Toast.makeText(CurveMeasureInputActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    /**
     * 菜单创建器。
     */
    private SwipeMenuCreator mSwipeMenuCreator = new SwipeMenuCreator() {
        @Override
        public void onCreateMenu(SwipeMenu swipeLeftMenu, SwipeMenu swipeRightMenu, int position) {
            int width = getResources().getDimensionPixelSize(R.dimen.dp_70);

            // 1. MATCH_PARENT 自适应高度，保持和Item一样高;
            // 2. 指定具体的高，比如80;
            // 3. WRAP_CONTENT，自身高度，不推荐;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;

            // 添加右侧的，如果不添加，则右侧不会出现菜单。
//            {
//                SwipeMenuItem deleteItem = new SwipeMenuItem(CurveMeasureInputActivity.this).setBackground(
//                        R.drawable.selector_green)
//                        .setImage(R.mipmap.ic_action_edit)
//                        .setText("编辑")
//                        .setTextColor(Color.WHITE)
//                        .setWidth(width)
//                        .setHeight(height);
//                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。
//
//            }
        }
    };


    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color_pa));
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
//        Constants.strWavelength = mDataList.get(position);
//
//        mAdapter.setThisPosition(position);
//        //嫑忘记刷新适配器
//        mAdapter.notifyDataSetChanged();
//        CustomToast.showToast(this, mDataList.get(position));
        intPosition = position;
        alertDialog("modify");
    }

    protected List<String> createDataList() {
        String[] listItem = getResources().getStringArray(R.array.list_shuju);//曲线列表
        for (int i = 0; i < listItem.length; i++) {
            dataList.add(listItem[i]);
        }
        return dataList;
    }

    private void alertDialog(String strType) {

        AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
        //获取界面
        View dialogView = LayoutInflater.from(this).inflate(R.layout.inputdata_dialog_layout, null);
        //将界面填充到AlertDiaLog容器
        setDeBugDialog.setView(dialogView);
        setDeBugDialog.create();
        final EditText et_aValue = dialogView.findViewById(R.id.et_aValue);
        final EditText et_cValue = dialogView.findViewById(R.id.et_cValue);
        if ("modify".equals(strType)) {//判断添加与修改
            et_cValue.setText("" + ListAddCValue.get(intPosition));
            et_aValue.setText("" + ListAddAValue.get(intPosition));
        }
        final AlertDialog customAlert = setDeBugDialog.show();
        dialogView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                strCValue = et_cValue.getText().toString();
                strAValue = et_aValue.getText().toString();
                if (strCValue.length() > 0 && strAValue.length() > 0) {
                    if ("add".equals(strType)) {//添加新的曲线
                        dataList.add("C=" + strCValue + "mg/L\nA=" + strAValue);
                        //mAdapter.notifyDataSetChanged(mDataList);
                        ListAddAValue.add(Double.parseDouble(strAValue));
                        ListAddCValue.add(Double.parseDouble(strCValue));
                        int length = ListAddAValue.size();
                        Double[] x = new Double[length];
                        Double[] y = new Double[length];
                        for (int i = 0; i < length; i++) {
                            x[i] = (Double) ListAddAValue.get(i);
                            y[i]=(Double) ListAddCValue.get(i);
                        }
                        Double a = LeastSquares.getA(x, y);
                        Double b = LeastSquares.getB(x, y);
                        if(length<2){
                            tvShowData.setText("C=kA+b");
                        }else{
                            tvShowData.setText("C=" + df_4.format(a) + "A+" + df_4.format(b));
                        }

                    } else {//修改当前曲线
                        dataList.set(intPosition, "C=" + strCValue + "mg/L\nA=" + strAValue);
                        ListAddAValue.set(intPosition, Double.parseDouble(strAValue));
                        ListAddCValue.set(intPosition, Double.parseDouble(strCValue));
                        mAdapter.notifyDataSetChanged(mDataList);
                    }

                } else {
                    CustomToast.showToast(getApplicationContext(), "没有输入数值");
                }
                customAlert.dismiss();
            }
        });
        dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customAlert.dismiss();
            }
        });
    }

    private void showData() {
//        strInfo = edInput.getText().toString();
        //当接收到Click事件之后触发
        new MaterialDialog.Builder(this)// 初始化建造者
                .icon(getResources().getDrawable(R.mipmap.icon_save))
                .title(strTitle)// 标题
                .content("是否保存当前曲线？\n\n" + strInfo)// 内容
                .positiveText(R.string.save)
                .negativeText(R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();// 显示对话框
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, CurveSelectActivity.class);
        intent.putExtra("type", Constants.strFormActivity);
        intent.putExtra("strInfo", strInfo);
        startActivity(intent);
        this.finish();
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
