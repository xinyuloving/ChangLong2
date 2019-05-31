package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.adapter.BaseAdapter;
import com.lkkdesign.changlong.adapter.MainAdapter;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.data.dao.UserDao;
import com.lkkdesign.changlong.data.model.Tb_user;
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
import butterknife.OnClick;

public class UserInfoActivity extends AppCompatActivity implements SwipeItemClickListener {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.title)
    TextView title;
    @BindView(R.id.iv_addUser)
    ImageView ivAddUser;
    @BindView(R.id.recycler_view)
    SwipeMenuRecyclerView recyclerView;
    @BindView(R.id.scroll_view)
    StickyNestedScrollView scrollView;

    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    protected BaseAdapter mAdapter;
    protected List<String> mDataList;

    private Intent intent = new Intent();
    private Tb_user tb_user;
    UserDao userDao = new UserDao(UserInfoActivity.this);

    private final String TAG = "UserInfoActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(getContentView());
        ButterKnife.bind(this);

//        mToolbar = findViewById(R.id.toolbar);
        mRecyclerView = findViewById(R.id.recycler_view);

        mLayoutManager = createLayoutManager();
        mItemDecoration = createItemDecoration();
        mDataList = createDataList();
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

    protected int getContentView() {
        return R.layout.activity_user_info;
    }

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }

    @OnClick({R.id.iv_return, R.id.iv_addUser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_return:
                intent.setClass(UserInfoActivity.this,Main2Activity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
                break;
            case R.id.iv_addUser:
                intent.setClass(UserInfoActivity.this, UserAddActivity.class);
                startActivity(intent);
                UserInfoActivity.this.finish();
                break;
        }
    }

    protected List<String> createDataList() {
        //查询数据库
        List<Tb_user> listinfos = userDao.getScrollData(0, userDao.getCount());
        List<String> dataList = new ArrayList<>();
        for (Tb_user tb_user : listinfos) {
//            dataList.add("姓名：" + tb_user.getName() + "\t\t工号：" + tb_user.getJobNo());
            dataList.add("姓名：" + tb_user.getName());
        }
        return dataList;
    }

    /**
     *
     */
    private void updateAdpater() {
        mDataList.clear();
        mAdapter.notifyDataSetChanged();
        //mDataList = createDataList(strClassic);
        mDataList.addAll(createDataList());
        Log.i(TAG, "mDataList_zd：" + mDataList.toString());
        mAdapter.notifyDataSetChanged(mDataList);
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Log.i(TAG, "mDataList=" + mDataList.get(position));
        showCruveData("当前用户详细信息", mDataList.get(position));
    }

    /**
     * 弹窗显示曲线数据
     *
     * @param strTitle
     * @param strInfo
     */
    private void showCruveData(String strTitle, final String strInfo) {


        Log.i(TAG, "strinfo=" + strInfo.toLowerCase().replaceAll("姓名：", ""));

        try {
//            tb_user = userDao.findByName(strInfo.replaceAll("^.*工号：", ""));
            tb_user = userDao.findByName(strInfo.replaceAll("姓名：", ""));
            Log.i(TAG, "tb_user=" + tb_user.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        Log.i(TAG, "tb_user=" + tb_user.toString());
        String strContent = "\n姓名："
                + tb_user.getName()
//                + "\n工号："
//                + tb_user.getJobNo()
                + "\n单位："
                + tb_user.getCompany() + "\n联系方式："
                + tb_user.getContact() + "\n地址："
                + tb_user.getAddress();
        //当接收到Click事件之后触发
        new MaterialDialog.Builder(UserInfoActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title(strTitle)// 标题
                .content(strContent)// 内容
                .positiveText(R.string.edit)
                .negativeText(R.string.cancel)
                .neutralText(R.string.select)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(UserInfoActivity.this, UserEditActivity.class);
                        intent.putExtra("id", tb_user.get_id()); //将id传递到下一页面
                        startActivity(intent);
                        UserInfoActivity.this.finish();
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .onNeutral(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(MaterialDialog dialog, DialogAction which) {
                        Constants.strLoginName = tb_user.getName();
                        // 信息提示
                        CustomToast.showLongToast(getApplicationContext(), "已选择用户："+tb_user.getJobNo());
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(UserInfoActivity.this).setBackground(
                        R.drawable.selector_green)
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
     */
    private SwipeMenuItemClickListener mMenuItemClickListener = new SwipeMenuItemClickListener() {
        @Override
        public void onItemClick(SwipeMenuBridge menuBridge, int position) {
            menuBridge.closeMenu();

            int direction = menuBridge.getDirection(); // 左侧还是右侧菜单。
            int menuPosition = menuBridge.getPosition(); // 菜单在RecyclerView的Item中的Position。

            if (direction == SwipeMenuRecyclerView.RIGHT_DIRECTION) {

                String strItem = mDataList.get(position).replaceAll("姓名：.*工号：", "");

                Log.i(TAG, "strItem=" + strItem);
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "mDataList=" + mDataList.toString());
                Log.i(TAG, "Curve=" + mDataList.get(menuPosition));
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "menuPosition=" + menuPosition);

                userDao.deteleByJobNo(strItem);
                mDataList.remove(position);
                mAdapter.notifyItemRemoved(position);
                // 信息提示
                CustomToast.showToast(getApplicationContext(), "删除成功");

            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(UserInfoActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
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


    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop() {
        super.onStop();
        UserInfoActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        UserInfoActivity.this.finish();
    }

}
