package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
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
import java.util.List;

/**
 * 曲线校准--选择曲线
 */
public class CurveSelectActivity extends AppCompatActivity implements SwipeItemClickListener {

    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    private ImageView iv_return;
    private TextView tvTime,tvReturn,tvUser;
    private Button btnCalculate;
    private Intent intent = new Intent();

    private String strType = "";
    private String strStratTime = "";
    private String strEndTime = "";
    private String strJiange = "";
    private String strInfo="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_curve_select);
//        setContentView(getContentView());
        initView();
    }

    //初始化控件
    private void initView() {

        Intent getIntent = getIntent();
        strType = getIntent.getStringExtra("type");
        Constants.strFormActivity = strType;
        Log.i("CurveSelectActivity","strType = "+strType);
        strStratTime = getIntent.getStringExtra("startTime");
        strEndTime = getIntent.getStringExtra("endTime");
        strJiange = getIntent.getStringExtra("jiange");
        Log.i("CurveSelectActivity","strJiange = "+strJiange);

        mRecyclerView = findViewById(R.id.rv_curve);
        iv_return = findViewById(R.id.iv_return);
        tvReturn = findViewById(R.id.tv_return);
        tvTime = findViewById(R.id.tv_timer);
        btnCalculate=findViewById(R.id.btn_calculate);

        tvTime.setText(DateUtil.getDate());

        tvUser=findViewById(R.id.tv_user);
        tvUser.setText(Constants.strLoginName);

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(CurveSelectActivity.this);

        mRecyclerView.setLongPressDragEnabled(false); // 长按拖拽，默认关闭。
        mRecyclerView.setItemViewSwipeEnabled(false); // 滑动删除，默认关闭。

        mRecyclerView.setSwipeMenuItemClickListener(mMenuItemClickListener); // Item的Menu点击。
        mRecyclerView.setSwipeItemLongClickListener(swipeItemLongClickListener);//Item的Menu长点击。
        mRecyclerView.setSwipeMenuCreator(mSwipeMenuCreator); // 菜单创建器。

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);
        tvReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                intent.setClass(CurveSelectActivity.this, ManualMeasureFristActivity.class);
                intent.setClass(CurveSelectActivity.this, TimingSetupActivity.class);
                startActivity(intent);
                CurveSelectActivity.this.finish();
            }
        });
    }

    /**
     * RecyclerView的Item的Menu长按点击监听。
     */
    private SwipeItemLongClickListener swipeItemLongClickListener=new SwipeItemLongClickListener() {
        @Override
        public void onItemLongClick(View view, int i) {
            intent.setClass(CurveSelectActivity.this, CurveManageActivity.class);
            intent.putExtra("curve", mDataList.get(i)); //将计算的值回传回去
            startActivity(intent);
            CurveSelectActivity.this.finish();
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

                Log.i("BDActivity", "position=" + position);
                Log.i("BDActivity", "mDataList=" + mDataList.toString());
                Log.i("BDActivity", "Curve=" + mDataList.get(menuPosition));
                Log.i("BDActivity", "position=" + position);
                Log.i("BDActivity", "menuPosition=" + menuPosition);

                intent.setClass(CurveSelectActivity.this, CurveManageActivity.class);
                intent.putExtra("curve", mDataList.get(menuPosition)); //将计算的值回传回去
                startActivity(intent);
                CurveSelectActivity.this.finish();
            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(CurveSelectActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
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
        strInfo=mDataList.get(position);
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

    public void BtnCalculate(View view){
        if(strInfo.equals("")){
            CustomToast.showToast(this, "请选择对应的曲线！");
        }else {
            intent.setClass(this, TimingMeasureTipActivity.class);
            intent.putExtra("strStartTime", strStratTime);
            intent.putExtra("strEndTime", strEndTime);
            intent.putExtra("jiange", strJiange);
            intent.putExtra("strInfo", strInfo);
            startActivity(intent);
            this.finish();
        }

    }

    @Override
    public void onBackPressed() {

        if ("curve".equals(Constants.strFormActivity)) {
            intent.setClass(CurveSelectActivity.this, Main2Activity.class);
            startActivity(intent);
            this.finish();
        } else if ("time".equals(Constants.strFormActivity)) {//定时测量
            intent.setClass(CurveSelectActivity.this, TimingMeasureActivity.class);
            startActivity(intent);
            this.finish();
        } else if ("xiaozhun".equals(Constants.strFormActivity)) {//返回主页面
            intent.setClass(CurveSelectActivity.this, Main2Activity.class);
            startActivity(intent);
            this.finish();
        } else if("manual".equals(Constants.strFormActivity)){//手动测量
            intent.setClass(CurveSelectActivity.this, ManualMeasureFristActivity.class);
            startActivity(intent);
            this.finish();
        }else{
            intent.setClass(CurveSelectActivity.this, Main2Activity.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        CurveSelectActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CurveSelectActivity.this.finish();
    }
}
