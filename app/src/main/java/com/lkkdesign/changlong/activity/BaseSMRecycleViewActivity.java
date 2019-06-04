package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.TabLayout;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.printer.SearchBTActivity;
import com.lkkdesign.changlong.utils.CustomToast;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenu;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuBridge;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuCreator;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItem;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;
import com.yanzhenjie.recyclerview.swipe.widget.StickyNestedScrollView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by huangyaoyu on 2017/7/21.
 */
public class BaseSMRecycleViewActivity extends AppCompatActivity implements SwipeItemClickListener {

    private final String TAG = "BSMRVActivity";
    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    private FloatingActionButton fabSearch;

    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    private String strType = "";
    private String strContent = "";
    private String strSearch= "";//搜索内容
    private String strSearchCon= "";//搜索限制条件

    private Intent intent = new Intent();
    //    private Tb_data tb_data;
//    DataDAO dataDAO = new DataDAO(BaseSMRecycleViewActivity.this);
    private Tb_measure tb_measure;
    MeasureDao measureDao = new MeasureDao(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

        initView();

        initTabLayout();

    }

    private void initView() {
        int i = measureDao.getCount();
        Log.i(TAG, "数据表中记录总数 getCount()=" + i);

//        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);
        fabSearch = (FloatingActionButton) findViewById(R.id.fab_search);

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder setDeBugDialog = new AlertDialog.Builder(BaseSMRecycleViewActivity.this);
                //获取界面
                View dialogView = LayoutInflater.from(BaseSMRecycleViewActivity.this).inflate(R.layout.input_search_data_dialoglayout, null);
                //将界面填充到AlertDiaLog容器
                setDeBugDialog.setView(dialogView);
                setDeBugDialog.create();
                final EditText etSearch = dialogView.findViewById(R.id.et_search);
                final Spinner spSearch= dialogView.findViewById(R.id.sp_search);
                final AlertDialog customAlert = setDeBugDialog.show();
                dialogView.findViewById(R.id.btn_search).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        strSearch=etSearch.getText().toString();
                        strSearchCon = (String) spSearch.getSelectedItem();
                        spSearch.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                            @Override
                            public void onItemSelected(AdapterView<?> parent, View view,
                                                       int position, long id) {
                                strSearchCon =spSearch.getSelectedItem().toString();
                            }

                            @Override
                            public void onNothingSelected(AdapterView<?> parent) {
                                // TODO Auto-generated method stub

                            }
                        });
                        Log.i("BSMRActivity","searchData:"+strSearch+","+strSearchCon);
                        intent.setClass(BaseSMRecycleViewActivity.this, SearchDataActivity.class);
                        intent.putExtra("searchContent", strContent); //传递搜索内容
                        intent.putExtra("searchConditions",strSearchCon);//传递搜索条件
                        intent.putExtra("type", "BaseSMRecycleViewActivity");//从何处跳转
                        startActivity(intent);
                        BaseSMRecycleViewActivity.this.finish();
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
        });

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList("自动测量");
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。

        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);
    }

    private void initTabLayout() {

        TabLayout tabLayout = findViewById(R.id.tab_layout);
        TabLayout.Tab tab = tabLayout.newTab();
        tab.setText("自动测量");
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("手动测量");
        tabLayout.addTab(tab);

        tab = tabLayout.newTab();
        tab.setText("定时测量");
        tabLayout.addTab(tab);

//        tab = tabLayout.newTab();
//        tab.setText("添加数据");
//        tabLayout.addTab(tab);

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {//选中是回调
//                CustomToast.showToast(BaseSMRecycleViewActivity.this, "第" + tab.getPosition() + "个Tab");
                switch (tab.getPosition()) {
                    case 0:
                        updateAdpater("自动测量");
                        strType = "自动测量";
                        break;
                    case 1:
                        updateAdpater("手动测量");
                        strType = "手动测量";
                        break;
                    case 2:
                        updateAdpater("定时测量");
                        strType = "定时测量";
                        break;
                    case 3:
                        intent.setClass(BaseSMRecycleViewActivity.this, DataAddActivity.class);
                        startActivity(intent);
                        BaseSMRecycleViewActivity.this.finish();
                        break;
                    default:
                        break;
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {//从选中到不再选中时回调

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    protected int getContentView() {
        return R.layout.activity_scroll;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }

    protected List<String> createDataList(String strClassic) {
        //查询数据库
//        List<Tb_data> listinfos = dataDAO.getScrollData(0, dataDAO.getCount());
        Log.i(TAG, "查询条件：" + strClassic);
        List<Tb_measure> listinfos = measureDao.findByClassic(strClassic);
        List<String> dataList = new ArrayList<>();
        for (Tb_measure tb_measure : listinfos) {
//            dataList.add("曲线：" + tb_measure.getItem());
            dataList.add(tb_measure.getItem());
//            dataList.add("曲线：" + tb_measure.get_id());
        }
        return dataList;

    }

    private void updateAdpater(String strClassic) {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        //mDataList = createDataList(strClassic);
        mDataList.addAll(createDataList(strClassic));
        Log.i(TAG, "mDataList_zd：" + mDataList.toString());
        mAdapter.notifyDataSetChanged(mDataList);
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Log.i(TAG, "mDataList=" + mDataList.get(position));
        showData(mDataList.get(position), mDataList.get(position));

    }

    /**
     * 弹窗显示曲线数据
     * 点击
     *
     * @param strTitle
     * @param strinfo
     */
    private void showData(String strTitle, final String strinfo) {
//        Tb_data tb_data = dataDAO.findByItem(strinfo.replaceAll("曲线：", ""));
        try {
            tb_measure = measureDao.findByItem(strinfo.replaceAll("曲线：", ""));
//            Log.i(TAG, "tb_measure=" + tb_measure.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i("BSMRVActivity", "tb_measure=" + tb_measure.toString());
        strContent = "\n分类：" + tb_measure.getClassic()
                + "\n条目：" + tb_measure.getItem()
                + "\n名称：" + tb_measure.getName()
                + "\n波长：" + tb_measure.getWavelength()
                + "\n浓度：" + tb_measure.getDensity()
                + "\n透过率：" + tb_measure.getTranatre()
                + "\n吸光度：" + tb_measure.getAbsorbance()
                + "\n操作员：" + tb_measure.getUserId()
                + "\n温度：" + tb_measure.getTemperature()
                + "\n测量结果：" + tb_measure.getResult()
                + "\n类型：" + tb_measure.getType()
                + "\n时间：" + tb_measure.getTime()
                + "\n备注：" + tb_measure.getMark()
                + "\n测点名称：" + tb_measure.getMeasure_name()
                + "\n单位名称：" + tb_measure.getEntity_name()
                + "\n取样时间：" + tb_measure.getSampling_time()
                + "\n采样人：" + tb_measure.getSampler()
                + "\n检测人：" + tb_measure.getInspector();
        Log.i("BSMRVActivity", "strContent=" + strContent);
        //当接收到Click事件之后触发
        new MaterialDialog.Builder(BaseSMRecycleViewActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title(strTitle)// 标题
                .content(strContent)// 内容
//                .positiveText(R.string.edit)
                .negativeText(R.string.cancel)
                .neutralText(R.string.print)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        intent.setClass(BaseSMRecycleViewActivity.this, DataManageActivity.class);
//                        intent.putExtra("curve", strinfo); //将计算的值回传回去
//                        startActivity(intent);
//                        BaseSMRecycleViewActivity.this.finish();
//                    }
//                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(BaseSMRecycleViewActivity.this, SearchBTActivity.class);
                        intent.putExtra("printInfo", strContent); //传递需要打印的数据
                        intent.putExtra("type", "BaseSMRecycleViewActivity");//从何处跳转
                        startActivity(intent);
                        BaseSMRecycleViewActivity.this.finish();
                    }
                })

                .show();// 显示对话框

    }


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
            {
                SwipeMenuItem deleteItem = new SwipeMenuItem(BaseSMRecycleViewActivity.this).setBackground(
                        R.drawable.selector_green)
//                        .setImage(R.mipmap.ic_action_edit)
//                        .setText("编辑")
                        .setImage(R.mipmap.ic_action_delete)
                        .setText(R.string.delete)
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

            }
        }
    };

    /**
     * RecyclerView的Item的Menu点击监听。
     * 左右滑动
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                String strItem = mDataList.get(position);

                Log.i(TAG, "position=" + position);
                Log.i(TAG, "mDataList=" + mDataList.toString());
                Log.i(TAG, "Curve=" + mDataList.get(position));
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "menuPosition=" + menuPosition);
                Log.i(TAG, "menuPositionXXX=" + mDataList.get(menuPosition));

                measureDao.deteleByItem(strItem);//删除条目

                mDataList.remove(position);
                mAdapter.notifyItemRemoved(position);

//                intent.setClass(BaseSMRecycleViewActivity.this, DataManageActivity.class);
//                intent.putExtra("curve", mDataList.get(menuPosition)); //将计算的值回传回去
//                startActivity(intent);
//                BaseSMRecycleViewActivity.this.finish();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(BaseSMRecycleViewActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return true;
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(BaseSMRecycleViewActivity.this, Main2Activity.class);
        startActivity(intent);
        BaseSMRecycleViewActivity.this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
//        initTabLayout();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
//        initTabLayout();
    }

    @Override
    protected void onStop() {
        super.onStop();
        BaseSMRecycleViewActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        BaseSMRecycleViewActivity.this.finish();
    }

}