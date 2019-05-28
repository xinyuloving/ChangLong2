package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lkkdesign.changlong.utils.DateUtil;
import com.yanzhenjie.recyclerview.swipe.SwipeItemClickListener;
import com.yanzhenjie.recyclerview.swipe.SwipeMenuRecyclerView;
import com.yanzhenjie.recyclerview.swipe.widget.DefaultItemDecoration;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 选择波长
 */
public class PhotometerActivity extends AppCompatActivity implements SwipeItemClickListener {

    //    protected Toolbar mToolbar;
//    protected ActionBar mActionBar;
//    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;
    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.textView8)
    TextView textView8;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.rv_photometer)
    SwipeMenuRecyclerView mRecyclerView;
    @BindView(R.id.view3)
    View view3;
    @BindView(R.id.tv_timer)
    TextView tvTimer;
    @BindView(R.id.view4)
    View view4;
    @BindView(R.id.btn_measure)
    Button btnMeasure;
    @BindView(R.id.btn_calculate)
    Button btnCalculate;
    @BindView(R.id.btn_save)
    Button btnSave;
    @BindView(R.id.ll_bottom)
    LinearLayout llBottom;

//    private ImageView ivReturn;
    protected BaseAdapter mAdapter;
    protected List<String> mDataList;//自动测量
    private Intent intent = new Intent();
    private int intPosition = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photometer);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
//        mToolbar = findViewById(R.id.toolbar);
//        mRecyclerView = findViewById(R.id.rv_photometer);
//        ivReturn = findViewById(R.id.iv_return);

//        ivReturn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                intent.setClass(PhotometerActivity.this, MainActivity.class);
//                startActivity(intent);
//                PhotometerActivity.this.finish();
//            }
//        });

//        setSupportActionBar(mToolbar);
//        mActionBar = getSupportActionBar();
//        if (displayHomeAsUpEnabled()) {
//            mActionBar.setDisplayHomeAsUpEnabled(true);
//        }

        tvTimer.setText(DateUtil.getDate());

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
        mAdapter = createAdapter();

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.addItemDecoration(mItemDecoration);
        mRecyclerView.setSwipeItemClickListener(PhotometerActivity.this);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged(mDataList);
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

    protected boolean displayHomeAsUpEnabled() {
        return true;
    }

    @Override
    public void onItemClick(View itemView, int position) {
//        intent.setClass(this, PhotometerMeasuringActivity.class);
//        intent.putExtra("wavelength", mDataList.get(position));
//        startActivity(intent);
        intPosition = position;
        CustomToast.showToast(this, mDataList.get(position));
    }

    protected List<String> createDataList() {
        List<String> dataList = new ArrayList<>();
        for (int i = 0; i < 30; i++) {
            dataList.add(i + ". λ= " + (300 + i * 30) + " nm");
        }
        return dataList;
    }

    @OnClick({R.id.iv_return, R.id.ll_title, R.id.btn_calculate})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                intent.setClass(PhotometerActivity.this, PhotometerFristActivity.class);
                startActivity(intent);
                PhotometerActivity.this.finish();
                break;
            case R.id.ll_title:
                break;
            case R.id.btn_calculate:
                intent.setClass(this, PhotometerSecActivity.class);
                intent.putExtra("wavelength", mDataList.get(intPosition));
                startActivity(intent);
                PhotometerActivity.this.finish();
                break;
        }
    }

    /**
     * 监听返回键
     */
    @Override
    public void onBackPressed() {
        // super.onBackPressed();//注释掉这行,back键不退出activity
        intent.setClass(PhotometerActivity.this, PhotometerFristActivity.class);
        startActivity(intent);
        PhotometerActivity.this.finish();
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
