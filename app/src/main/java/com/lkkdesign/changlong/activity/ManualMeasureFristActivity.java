package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.hanks.htextview.rainbow.RainbowTextView;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.lkkdesign.changlong.utils.RandomUntil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
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

public class ManualMeasureFristActivity extends AppCompatActivity implements SwipeItemClickListener {


    //    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.rtv_list)
    RainbowTextView rtvList;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.ll_content)
    CardView llContent;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;

    private String strInfo = "";
    private Intent intent = new Intent();
    private String TAG = "MMFActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manual_measure_frist);
        ButterKnife.bind(this);
        initView();
    }

    private void initView() {
        tvUser.setText(Constants.strLoginName);
//        strInfo = "透过率（T）：" + RandomUntil.getNum(20) + ".00%\n" +
//                "吸光度（A）：1.0\n" +
//                "波长（λ）：610 nm\n" +
//                "温度：" + RandomUntil.getNum(25, 37) + " ℃\n";
//        tvResult.setText(strInfo);
        tvTimer.setText(DateUtil.getDate());

        //曲线部分
        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(ManualMeasureFristActivity.this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。

        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

//        mRecyclerView.setAdapter(mAdapter);
//        mAdapter.notifyDataSetChanged(mDataList);
    }

    /**
     * RecyclerView的Item的Menu点击监听。
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            /*int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {
                mDataList.remove(position);
                mAdapter.notifyItemRemoved(position);

                Log.i(TAG, "position=" + position);
                Log.i(TAG, "mDataList=" + mDataList.toString());
                Log.i(TAG, "Curve=" + mDataList.get(menuPosition));
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "menuPosition=" + menuPosition);

                intent.setClass(ManualMeasureFristActivity.this, CurveManageActivity.class);
                intent.putExtra("curve", mDataList.get(menuPosition)); //将计算的值回传回去
                startActivity(intent);
                ManualMeasureFristActivity.this.finish();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(ManualMeasureFristActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
                        .show();
            }*/
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
//                SwipeMenuItem deleteItem = new SwipeMenuItem(ManualMeasureFristActivity.this).setBackground(
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

    /**
     * @param
     * @author lkkdesign
     * @time 2019/5/21  17:34
     * @describe 设置间隔
     */
    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.gray_dark));
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
//        Constants.strWavelength = mDataList.get(position);
//        if ("xiaozhun".equals(strType)) {
//            intent.setClass(this, CurveMeasureActivity.class);
//            intent.putExtra("wavelength", mDataList.get(position));
//            startActivity(intent);
//            this.finish();
//        } else if ("time".equals(strType)) {
//            intent.setClass(this, TimingMeasureActivity.class);
//            intent.putExtra("strStartTime", strStratTime);
//            intent.putExtra("strEndTime", strEndTime);
//            intent.putExtra("jiange", strJiange);
//            intent.putExtra("wavelength", mDataList.get(position));
//            startActivity(intent);
//            this.finish();
//        } else if ("main".equals(strType)) {
//            intent.putExtra("wavelength", mDataList.get(position)); //将计算的值回传回去
//            setResult(2, intent);
//            finish(); //结束当前的activity的生命周期
//        } else {
////            intent.setClass(this, ManualMeasureActivity.class);
//            intent.setClass(this, ManualMeasureSecActivity.class);
//            intent.putExtra("wavelength", mDataList.get(position));
//            startActivity(intent);
//            this.finish();
//        }
        mAdapter.setThisPosition(position);
        //嫑忘记刷新适配器
        mAdapter.notifyDataSetChanged();
        CustomToast.showToast(this, "选择的曲线\n" + mDataList.get(position));
        strInfo = mDataList.get(position);
    }

    protected List<String> createDataList() {
        List<String> dataList = new ArrayList<>();
        String[] listItem = getResources().getStringArray(R.array.list_quxian);//曲线列表
        for (int i = 0; i < listItem.length; i++) {
            dataList.add(listItem[i]);
        }
//        dataList.add("== 你已经看到我的底线啦 ==");
        return dataList;
    }

    @OnClick({R.id.iv_return, R.id.tv_return,R.id.rtv_list, R.id.tv_timer, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_return:
            case R.id.iv_return:
                intent.setClass(this, Main2Activity.class);
                startActivity(intent);
                this.finish();
                break;
//            case R.id.title:
//                break;
            case R.id.rtv_list:
                break;
//            case R.id.tv_show:
//                intent.setClass(this, CurveSelectActivity.class);
//                intent.putExtra("type", "manual");
//                startActivity(intent);
//                this.finish();
//                break;
//            case R.id.tv_result:
//                break;
            case R.id.tv_timer:
                break;
            case R.id.btn_calculate:
                if (strInfo.equals("")) {
                    CustomToast.showToast(this, "请选择对应的曲线！");
                } else {
                    intent.setClass(this, ManualMeasureTipActivity.class);
                    intent.putExtra("wavelength", strInfo);
                    startActivity(intent);
                    this.finish();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStop() {
        super.onStop();
        ManualMeasureFristActivity.this.finish();
        Log.i(TAG, "ManualMeasureFristActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ManualMeasureFristActivity.this.finish();
        Log.i(TAG, "ManualMeasureFristActivity-->onDestroy()");
    }
}
