package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AccountActivity extends AppCompatActivity implements SwipeItemClickListener {

    @BindView(R.id.iv_return)
    ImageView ivReturn;
    @BindView(R.id.tv_return)
    TextView tvReturn;
    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.toolbar)
    LinearLayout toolbar;
    @BindView(R.id.rv_curve)
    SwipeMenuRecyclerView rvCurve;
    @BindView(R.id.ll_content)
    CardView llContent;
    @BindView(R.id.btn_switch)
    Button btnSwitch;
    @BindView(R.id.btn_del)
    Button btnDel;

    protected SwipeMenuRecyclerView mRecyclerView;
    protected RecyclerView.LayoutManager mLayoutManager;
    protected RecyclerView.ItemDecoration mItemDecoration;

    protected BaseAdapter mAdapter;
    protected List<String> mDataList;
    protected List<String> mDataListNoPWD;
    private Tb_user tb_user;
    UserDao userDao = new UserDao(AccountActivity.this);

    private Intent intent = new Intent();
    private String TAG = "AccountActivity";
    private String strSelectInfo = "";
    private String strSelectInfoPwd = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        ButterKnife.bind(this);

        initView();
    }

    private void initView(){
        tvUser.setText(Constants.strLoginName);
        mRecyclerView = findViewById(R.id.rv_curve);
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

    protected RecyclerView.LayoutManager createLayoutManager() {
        return new LinearLayoutManager(this);
    }

    protected RecyclerView.ItemDecoration createItemDecoration() {
        return new DefaultItemDecoration(ContextCompat.getColor(this, R.color.divider_color));
    }

    protected List<String> createDataList() {
        //查询数据库
        List<Tb_user> listinfos = userDao.getScrollData(0, userDao.getCount());
        List<String> dataList = new ArrayList<>();
        for (Tb_user tb_user : listinfos) {
//            dataList.add("姓名：" + tb_user.getName() + "\t\t工号：" + tb_user.getJobNo());
            dataList.add(tb_user.getName());
        }
        return dataList;
    }

    protected BaseAdapter createAdapter() {
        return new MainAdapter(this);
    }

    @Override
    public void onItemClick(View itemView, int position) {
        Log.i(TAG, "mDataList=" + mDataList.get(position));
//        showCruveData("当前用户详细信息", mDataList.get(position));
        mAdapter.setThisPosition(position);
        //嫑忘记刷新适配器
        mAdapter.notifyDataSetChanged();
        CustomToast.showToast(this, "已选账号：\n" + mDataList.get(position));
//        CustomToast.showToast(this, "已选账号的密码：\n" + ));
        strSelectInfo = mDataList.get(position);
        strSelectInfoPwd = userDao.findPWDByName(mDataList.get(position));
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
        new MaterialDialog.Builder(AccountActivity.this)// 初始化建造者
//                        .icon(R.mipmap.icon_exit)
                .title(strTitle)// 标题
                .content(strContent)// 内容
                .positiveText(R.string.edit)
                .negativeText(R.string.cancel)
                .neutralText(R.string.select)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
                        intent.setClass(AccountActivity.this, UserEditActivity.class);
                        intent.putExtra("id", tb_user.get_id()); //将id传递到下一页面
                        startActivity(intent);
                        AccountActivity.this.finish();
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
                SwipeMenuItem deleteItem = new SwipeMenuItem(AccountActivity.this).setBackground(
                        R.drawable.selector_green)
                        .setImage(R.mipmap.ic_action_delete)
                        .setText(R.string.delete)
                        .setTextColor(Color.WHITE)
                        .setWidth(width)
                        .setHeight(height);
                //swipeRightMenu.addMenuItem(deleteItem);// 添加一个按钮到右侧侧菜单。

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

                String strItem = mDataList.get(position);
//                String strItem = mDataList.get(position).replaceAll("姓名：", "");
                //String strItem = mDataList.get(position).replaceAll("姓名：.*密码：", "");

                Log.i(TAG, "strItem=" + strItem);
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "mDataList=" + mDataList.toString());
                Log.i(TAG, "Curve=" + mDataList.get(menuPosition));
                Log.i(TAG, "position=" + position);
                Log.i(TAG, "menuPosition=" + menuPosition);

                userDao.deteleByName(strItem);
                mDataList.remove(position);
                mAdapter.notifyItemRemoved(position);
                // 信息提示
                CustomToast.showToast(getApplicationContext(), "删除成功");

            } else if (direction == SwipeMenuRecyclerView.LEFT_DIRECTION) {

                Toast.makeText(AccountActivity.this, "list第" + position + "; 左侧菜单第" + menuPosition, Toast.LENGTH_SHORT)
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


    @OnClick({R.id.tv_return, R.id.btn_switch, R.id.btn_del,R.id.tv_user})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                intent.setClass(AccountActivity.this,UserInfoActivity.class);
                startActivity(intent);
                break;
            case R.id.tv_return:
                returnToActivity();
                break;
            case R.id.btn_switch:
//                strSelectInfo
                if (strSelectInfoPwd.length() > 0 && strSelectInfo.length() > 0) {
                    SharedPreferences settings = getSharedPreferences("UserInfo", 0);
                    SharedPreferences.Editor editor = settings.edit();
                    editor.putString("Username", strSelectInfo);
                    editor.putString("Password", strSelectInfoPwd);
                    editor.commit();
                    tvUser.setText(strSelectInfo);
                    Constants.strLoginName = strSelectInfo;
                }else{
                    CustomToast.showToast(this,"请选择要切换的账号");
                }

                break;
            case R.id.btn_del:
                break;
        }
    }

    private void returnToActivity(){
        intent.setClass(this, SettingsActivity.class);
        startActivity(intent);
        this.finish();
    }

    @Override
    public void onBackPressed() {
        returnToActivity();
    }

    @Override
    protected void onStop() {
        super.onStop();
        AccountActivity.this.finish();
        Log.i(TAG, "ManualMeasureFristActivity-->onStop()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        AccountActivity.this.finish();
        Log.i(TAG, "ManualMeasureFristActivity-->onDestroy()");
    }

}
