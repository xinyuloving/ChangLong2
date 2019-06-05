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
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.lkkdesign.changlong.utils.RandomUntil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeItemLongClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static android.view.View.GONE;
import static com.lkkdesign.changlong.utils.MyFunc.calculateTransmittance;
import static com.lkkdesign.changlong.utils.MyFunc.getAbsorbance;

public class InputDataActivity extends AppCompatActivity implements SwipeItemClickListener {


    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    @BindView(R.id.tv_cod)
    TextView tvCod;
    @BindView(R.id.cardview1)
    CardView cardview1;
    @BindView(R.id.btn_add)
    FloatingActionButton btnAdd;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.cardview2)
    CardView cardview2;
    @BindView(R.id.ed_input)
    EditText edInput;
    @BindView(R.id.view2)
    View view2;
    @BindView(R.id.tv_show_data)
    TextView tvShowData;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.btn_blank)
    Button btnBlank;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.line_xiaozhun)
    LinearLayout lineXiaozhun;
    @BindView(R.id.tv_show1)
    TextView tvShow1;
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
    private Intent intent = new Intent();
    private String strTitle = "";
    private String strInfo = "";
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    private String TAG = "InputDataActivity";
    private String strAValue = "";
    private String strCValue = "";
    private String strType = "";
    private String strfrom = "";
    private List<String> dataList = new ArrayList<>();

    private boolean booAddBtn = false;//默认添加按钮的状态为false，当false时，A/C的值默认为零
    private boolean booIsEmpty = false;//是否已按“空白”键，默认没有
    private int lineState=4;//当前提示文字


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_input_data);
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
        tvTimer.setText(DateUtil.getDate());

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(InputDataActivity.this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。

        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeItemLongClickListener(swipeItemLongClickListener);//Item的Menu长点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

        if ("InputDataActivity".equals(strfrom)) {

        } else if ("CurveMeasureActivity".equals(strfrom)) {

        } else if ("CMActivity_ssjz".equals(strfrom)) {
            tvCod.setText(strInfo);
            btnCalculate.setVisibility(GONE);
            lineXiaozhun.setVisibility(View.VISIBLE);
            tvShow1.setVisibility(View.GONE);
            mRecyclerView.setVisibility(View.VISIBLE);
            btnBlank.setVisibility(View.GONE);

        }
    }

    @OnClick({R.id.tv_return, R.id.btn_add, R.id.tv_show_data, R.id.btn_calculate, R.id.btn_blank, R.id.btn_save})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
                intent.setClass(this, CurveSelectActivity.class);
                intent.putExtra("type", Constants.strFormActivity);
                intent.putExtra("strInfo", strInfo);
                startActivity(intent);
                this.finish();
                break;
            case R.id.btn_blank:
                if ("InputDataActivity".equals(strfrom) || "CurveMeasureActivity".equals(strfrom)) {
                    tvShow1.setText("\n\n请取出空白比色管\n请放入样品\n请按确认键");
//                tvShow2.setVisibility(View.INVISIBLE);
//                    booIsPre = true;
                } else {
                    booIsEmpty=true;
                    btnBlank.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
//                    booIsPre=true;
                }
                break;
            case R.id.btn_save:
                if (booAddBtn == true) {
                    tvTitle.setText("实时校准结果");
                    float floClosed = RandomUntil.getRandomFloat(0.10f, 0.50f);//光源在关闭的情况下，检测器产生的电流数值
                    float floEmpty = RandomUntil.getRandomFloat(100.0f, 105.50f);//光源点亮，样品仓放置空样品管的情况下，检测器产生的电流数值
                    float floSample = RandomUntil.getRandomFloat(10.10f, 20.50f);//空样品管拿出来，将装有样品的样品管放置进样品仓，此时，检测器产生的电流数值
                    float floTranrate = calculateTransmittance(floClosed, floEmpty, floSample);//透过率
                    strAValue = getAbsorbance(floTranrate) + "";
                    strInfo = "";
                    //tvResult.setText(strInfo);
                    //文字提示隐藏，数据列表显示
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
                            tvShow1.setVisibility(View.GONE);
                            mRecyclerView.setVisibility(View.VISIBLE);
                            dataList.add("C=" + strCValue + "mg/L,\n" + "A=" + strAValue);
                            booAddBtn = false;
                            btnSave.setText(R.string.save);
                            break;
                    }

                } else {


                    new AlertDialog.Builder(this)
                            .setTitle("保存")
                            .setMessage("保存吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent.setClass(InputDataActivity.this, ManualMeasureSecActivity.class);
                                    intent.putExtra("from", "InputDataActivity");
                                    intent.putExtra("wavelength", strTitle);
                                    intent.putExtra("type", Constants.strFormActivity);
                                    intent.putExtra("strInfo", strInfo);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                }
                            })
                            .show();
                }


                break;
            case R.id.btn_add:
                if ("InputDataActivity".equals(strfrom)) {
                    AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(this);
                    //获取界面
                    View dialogView = LayoutInflater.from(this).inflate(R.layout.inputdata_dialog_layout, null);
                    //将界面填充到AlertDiaLog容器
                    setDeBugDialog.setView(dialogView);
                    setDeBugDialog.create();
                    final EditText aValue = dialogView.findViewById(R.id.et_aValue);
                    final EditText cValue = dialogView.findViewById(R.id.et_cValue);
                    final AlertDialog customAlert = setDeBugDialog.show();
                    dialogView.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            strCValue = "C=" + cValue.getText().toString().replace("", "") + "mg/L";
                            strAValue = "A=" + aValue.getText().toString().replace("", "");
                            if (cValue.getText().toString().isEmpty() || aValue.getText().toString().isEmpty()) {
                                CustomToast.showToast(getApplicationContext(), "A、C值不可为空！");
                            }
                            if (cValue.getText().toString().length() > 0 && aValue.getText().toString().length() > 0) {
                                dataList.add(strCValue + "\n" + strAValue);
                                customAlert.dismiss();
                            }
                        }
                    });
                    dialogView.findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            customAlert.dismiss();
                        }
                    });
                } else if ("CurveMeasureActivity".equals(strfrom)) {

                } else if ("CMActivity_ssjz".equals(strfrom)) {

                    final EditText inputServer = new EditText(this);
                    inputServer.setInputType(InputType.TYPE_CLASS_NUMBER);
                    AlertDialog.Builder builder = new AlertDialog.Builder(this);
                    builder.setTitle("请输入C值：").setIcon(android.R.mipmap.sym_def_app_icon).setView(inputServer)
                            .setNegativeButton("Cancel", null);
                    builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            if(inputServer.getText().toString().isEmpty()){
                                CustomToast.showToast(getApplicationContext(), "不可输入空值！");
                            }else{
                                tvLine1.setVisibility(View.GONE);
                                tvLine2.setVisibility(View.GONE);
                                tvLine3.setVisibility(View.GONE);
                                tvLine4.setVisibility(View.VISIBLE);
                                tvLine5.setVisibility(View.GONE);
                                lineState=4;
                                strCValue = inputServer.getText().toString();
                                mRecyclerView.setVisibility(View.GONE);
                                btnBlank.setVisibility(View.GONE);
                                btnSave.setText(R.string.next);
                                booAddBtn = true;
                            }

                        }
                    });
                    builder.show();

                }
                break;

            case R.id.tv_show_data:
                showData();
                break;

            case R.id.btn_calculate:
                if ("InputDataActivity".equals(strfrom) || "CurveMeasureActivity".equals(strfrom) ) {
                    new AlertDialog.Builder(this)
                            .setTitle("保存")
                            .setMessage("保存吗？")
                            .setPositiveButton("是", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    intent.setClass(InputDataActivity.this, ManualMeasureTipActivity.class);
                                    intent.putExtra("from", "InputDataActivity");
                                    intent.putExtra("wavelength", strInfo);
                                    intent.putExtra("type", Constants.strFormActivity);
                                    intent.putExtra("strInfo", strInfo);
                                    startActivity(intent);
                                }
                            })
                            .setNegativeButton("否", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                }
                            })
                            .show();
                }
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

                intent.setClass(InputDataActivity.this, CurveManageActivity.class);
                intent.putExtra("curve", mDataList.get(menuPosition)); //将计算的值回传回去
                startActivity(intent);
                InputDataActivity.this.finish();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(InputDataActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
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
//                SwipeMenuItem deleteItem = new SwipeMenuItem(CurveSelectActivity.this).setBackground(
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
        Constants.strWavelength = mDataList.get(position);
       /* Constants.strWavelength = mDataList.get(position);
        if ("xiaozhun".equals(strType)) {
            intent.setClass(this, CurveMeasureActivity.class);
            intent.putExtra("wavelength", Constants.strWavelength);
            startActivity(intent);
            this.finish();
        } else if ("time".equals(strType)) {
            intent.setClass(this, TimingMeasureTipActivity.class);
            intent.putExtra("strStartTime", strStratTime);
            intent.putExtra("strEndTime", strEndTime);
            intent.putExtra("jiange", strJiange);

            strInfo=mDataList.get(position);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            this.finish();
        } else if ("main".equals(strType)) {
            intent.putExtra("wavelength", mDataList.get(position)); //将计算的值回传回去
            setResult(2, intent);
            finish(); //结束当前的activity的生命周期
        } else {
//            intent.setClass(this, ManualMeasureActivity.class);
            intent.setClass(this, ManualMeasureSecActivity.class);
            intent.putExtra("wavelength", mDataList.get(position));
            startActivity(intent);
            this.finish();
        }*/
        mAdapter.setThisPosition(position);
        //嫑忘记刷新适配器
        mAdapter.notifyDataSetChanged();
        CustomToast.showToast(this, mDataList.get(position));
//        strInfo = mDataList.get(position);
    }

    protected List<String> createDataList() {

        String[] listItem = getResources().getStringArray(R.array.list_shuju);//曲线列表
        for (int i = 0; i < listItem.length; i++) {
            dataList.add(listItem[i]);
        }
//        dataList.add("== 你已经看到我的底线啦 ==");
        return dataList;
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
