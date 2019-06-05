package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.SyncStateContract;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.MeasureDao;
import com.lkkdesign.changlong.data.model.Tb_measure;
import com.lkkdesign.changlong.printer.SearchBTActivity;
import com.lkkdesign.changlong.utils.CustomToast;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchDataActivity extends AppCompatActivity implements SwipeItemClickListener {
    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView mRecyclerView;

    private Intent intent = new Intent();
    private String strSearchContent = "";
    private String strSearchCondition = "";
    protected List<String> mDataList;
    private Tb_measure tb_measure;
    MeasureDao measureDao = new MeasureDao(this);
    private final String TAG = "BSMRVActivity";
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    protected BaseAdapter mAdapter;
    private String strContent = "";

    private SearchView mSearchView;
    private SearchView.SearchAutoComplete mSearchAutoComplete;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_data);
        ButterKnife.bind(this);
        initView();
    }

    public void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mSearchAutoComplete.isShown()) {
                    try {
                        mSearchAutoComplete.setText("");
                        Method method = mSearchView.getClass().getDeclaredMethod("onCloseClicked");
                        method.setAccessible(true);
                        method.invoke(mSearchView);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    finish();
                }
            }
        });

        Intent intent = getIntent();
        strSearchContent = intent.getStringExtra("searchContent");
        strSearchCondition = intent.getStringExtra("searchConditions");

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList(strSearchContent, strSearchCondition);
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。


        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.search_view, menu);
        MenuItem searchItem = menu.findItem(R.id.menu_search);

        //通过MenuItem得到SearchView
        mSearchView = (SearchView) MenuItemCompat.getActionView(searchItem);
        mSearchAutoComplete = (SearchView.SearchAutoComplete) mSearchView.findViewById(R.id.search_src_text);
        mSearchView.setQueryHint("搜索本地数据");

        //设置输入框提示文字样式
        mSearchAutoComplete.setHintTextColor(getResources().getColor(android.R.color.darker_gray));
        mSearchAutoComplete.setTextColor(getResources().getColor(android.R.color.background_light));
        mSearchAutoComplete.setTextSize(14);
        //设置触发查询的最少字符数（默认2个字符才会触发查询）
        mSearchAutoComplete.setThreshold(1);

        //设置搜索框有字时显示叉叉，无字时隐藏叉叉
        mSearchView.onActionViewExpanded();
        mSearchView.setIconified(true);

        //修改搜索框控件间的间隔
        LinearLayout search_edit_frame = (LinearLayout) mSearchView.findViewById(R.id.search_edit_frame);
        ViewGroup.MarginLayoutParams params = (ViewGroup.MarginLayoutParams) search_edit_frame.getLayoutParams();
        params.leftMargin = 0;
        params.rightMargin = 0;
        search_edit_frame.setLayoutParams(params);

        //监听SearchView的内容
        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                CustomToast.showToast(SearchDataActivity.this, s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {

                //Cursor cursor = TextUtils.isEmpty(s) ? null : queryData(s);

//                if (mSearchView.getSuggestionsAdapter() == null) {
//                    mSearchView.setSuggestionsAdapter(new SimpleCursorAdapter(SearchViewActivity2.this, R.layout.item_layout, cursor, new String[]{"name"}, new int[]{R.id.text1}));
//                } else {
//                    mSearchView.getSuggestionsAdapter().changeCursor(cursor);
//                }
                //setAdapter(cursor);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    // 让菜单同时显示图标和文字
    @Override
    public boolean onMenuOpened(int featureId, Menu menu) {
        if (menu != null) {
            if (menu.getClass().getSimpleName().equalsIgnoreCase("MenuBuilder")) {
                try {
                    Method method = menu.getClass().getDeclaredMethod("setOptionalIconsVisible", Boolean.TYPE);
                    method.setAccessible(true);
                    method.invoke(menu, true);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return super.onMenuOpened(featureId, menu);
    }

//    private void setAdapter(Cursor cursor) {
//        if (mLv.getAdapter() == null) {
//            SimpleCursorAdapter adapter = new SimpleCursorAdapter(SearchDataActivity.this, R.layout.item_layout, cursor, new String[]{"name"}, new int[]{R.id.text1});
//            mLv.setAdapter(adapter);
//        } else {
//            ((SimpleCursorAdapter) mLv.getAdapter()).changeCursor(cursor);
//        }
//    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }

    protected List<String> createDataList(String strSearchContent, String strSearchCondition) {
        //查询数据库
//        List<Tb_data> listinfos = dataDAO.getScrollData(0, dataDAO.getCount());
        Log.i("SearchDataActivity", "查询条件：" + strSearchContent + "" + strSearchCondition);
        List<Tb_measure> listinfos = measureDao.findBySearchCondition(strSearchContent, strSearchCondition);
        List<String> dataList = new ArrayList<>();
        for (Tb_measure tb_measure : listinfos) {
//            dataList.add("曲线：" + tb_measure.getItem());
            dataList.add(tb_measure.getItem());
//            dataList.add("曲线：" + tb_measure.get_id());
        }
        return dataList;

    }

    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(SearchDataActivity.this, Main2Activity.class);
        startActivity(intent);
        SearchDataActivity.this.finish();
    }

    @Override
    public void onItemClick(View itemView, int position) {
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
                + "\n测量类别：" + tb_measure.getStyle()
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
        new MaterialDialog.Builder(SearchDataActivity.this)// 初始化建造者
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
                        intent.setClass(SearchDataActivity.this, SearchBTActivity.class);
                        intent.putExtra("printInfo", strContent); //传递需要打印的数据
                        intent.putExtra("type", "BaseSMRecycleViewActivity");//从何处跳转
                        startActivity(intent);
                        SearchDataActivity.this.finish();
                    }
                })

                .show();// 显示对话框

    }

//    @OnClick(R.id.tv_return)
//    public void onViewClicked() {
//        intent.setClass(SearchDataActivity.this, BaseSMRecycleViewActivity.class);
//        startActivity(intent);
//        SearchDataActivity.this.finish();
//    }
}
