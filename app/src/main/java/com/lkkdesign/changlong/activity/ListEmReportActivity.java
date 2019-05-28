package com.lkkdesign.changlong.activity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.tencent.smtt.sdk.QbSdk;
import com.tencent.smtt.sdk.ValueCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ListEmReportActivity extends AppCompatActivity implements QbSdk.PreInitCallback, ValueCallback<String>, View.OnClickListener {

    @BindView(R.id.tv_user)
    TextView tvUser;
    @BindView(R.id.tv_sghow)
    TextView tvSghow;
    @BindView(R.id.iv_addUser)
    ImageView ivAddUser;
    @BindView(R.id.ll_title)
    LinearLayout llTitle;
    @BindView(R.id.lv_show_path)
    ListView lvShowPath;
    private ArrayAdapter<String> adapter;
    private ListView mShowPathLv;

    private Intent intent = new Intent();
    private final String TAG = "ListEmReportActivity";
    private PopupWindow mPopWindow;//pop弹窗
    private String strFilePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_em_report);
        ButterKnife.bind(this);

        QbSdk.initX5Environment(this, this);

        initView();
    }

    private void initView() {
        tvUser.setText(Constants.strLoginName);
        mShowPathLv = (ListView) findViewById(R.id.lv_show_path);
        adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_expandable_list_item_1);
//        adapter.addAll(getDocPathFromSD());
//        mShowPathLv.setAdapter(adapter);
        UpdateAdapterData();
        //注册点击事件
        mShowPathLv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            /**
             * @param parent   当前ListView
             * @param view     代表当前被点击的条目
             * @param position 当前条目的位置
             * @param id       当前被点击的条目的id
             */
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                strFilePath = parent.getItemAtPosition(position).toString();
                showPopupWindow();
            }
        });

        mShowPathLv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                /**
                 *
                 * @param parent 当前ListView
                 * @param view 代表当前被点击的条目
                 * @param position 当前条目的位置
                 * @param id 当前被点击的条目的id
                 */
                CustomToast.showToast(ListEmReportActivity.this, parent.getItemAtPosition(position).toString());
                String strFilePath = parent.getItemAtPosition(position).toString();
                openWordDocument(strFilePath);
                return true;
            }
        });
    }

    @OnClick({R.id.tv_user, R.id.iv_addUser})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_user:
                break;
            case R.id.iv_addUser:
                intent.setClass(ListEmReportActivity.this, EmergencyReportActivity.class);
//                intent.setClass(ListEmReportActivity.this, PoiActivity.class);
                startActivity(intent);
                ListEmReportActivity.this.finish();
                break;
        }
    }

    //Pop弹窗
    private void showPopupWindow() {

        //设置contentView
        View contentView = LayoutInflater.from(ListEmReportActivity.this).inflate(R.layout.layout_popupwindow, null);
        mPopWindow = new PopupWindow(contentView,
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        mPopWindow.setContentView(contentView);
        //设置各个控件的点击响应
        TextView tvSendTo = (TextView) contentView.findViewById(R.id.tv_sento);
        TextView tvPrint = (TextView) contentView.findViewById(R.id.tv_print);
        TextView tvDel = (TextView) contentView.findViewById(R.id.tv_del);
        TextView tvCancel = (TextView) contentView.findViewById(R.id.tv_cancel);

        tvSendTo.setOnClickListener(this);
        tvPrint.setOnClickListener(this);
        tvDel.setOnClickListener(this);
        tvCancel.setOnClickListener(this);
        //显示PopupWindow
        View rootview = LayoutInflater.from(ListEmReportActivity.this).inflate(R.layout.activity_list_em_report, null);
        mPopWindow.setBackgroundDrawable(new PaintDrawable());
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.showAtLocation(rootview, Gravity.BOTTOM, 0, 0);

    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        switch (id) {
            case R.id.tv_sento:
                CustomToast.showToast(this, "发送邮件");
                intent.setClass(ListEmReportActivity.this,SendEmailActivity.class);
                startActivity(intent);
                mPopWindow.dismiss();
                break;
            case R.id.tv_print:
                CustomToast.showToast(this, "蓝牙打印");
                mPopWindow.dismiss();
                break;
            case R.id.tv_del:
                CustomToast.showToast(this, "删除文件");
                deleteSingleFile(strFilePath);
                mPopWindow.dismiss();
                break;
            case R.id.tv_cancel:
                if (mPopWindow != null && mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                    mPopWindow = null;
                }
                break;
        }
    }

    private void openWordDocument(String strFilePath) {

        File file = new File(strFilePath);
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("local", "true");
        params.put("entryId", "2");
        params.put("allowAutoDestory", "true");

        JSONObject Object = new JSONObject();
        try {
            Object.put("pkgName", ListEmReportActivity.this.getApplication().getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("menuData", Object.toString());
        openFileReader(ListEmReportActivity.this, file.getAbsolutePath());

    }

    /**
     * 从sd卡获取Word文档路径资源
     *
     * @return
     */
    private List<String> getDocPathFromSD() {
        // 图片列表
        List<String> wordPathList = new ArrayList<String>();
        // 得到sd卡内image文件夹的路径   File.separator(/)
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/ChangLongData");
        if (!dir.exists()) {
            dir.mkdirs();
        }
        if (!dir.exists()) {
            dir.mkdirs();
        }

        String filePath = Environment.getExternalStorageDirectory().toString() + File.separator
                + "ChangLongData";

        // 得到该路径文件夹下所有的文件
        File fileAll = new File(filePath);
        File[] files = fileAll.listFiles();
        if (files.length >0) {
            // 将所有的文件存入ArrayList中,并过滤所有Doc文件
            for (int i = 0; i < files.length; i++) {
                File file = files[i];
                if (checkIsWordFile(file.getPath())) {
//                    wordPathList.add(file.getName());
                    wordPathList.add(file.getPath());
                    Log.i("ListEmReportActivity", "fiePath=" + file.getPath());
                }
            }
        }
        // 返回得到的图片列表
        return wordPathList;
    }

    /**
     * 检查扩展名，得到图片格式的文件
     *
     * @param fName 文件名
     * @return
     */
    @SuppressLint("DefaultLocale")
    private boolean checkIsWordFile(String fName) {
        boolean isWordFile = false;
        // 获取扩展名
        String FileEnd = fName.substring(fName.lastIndexOf(".") + 1,
                fName.length()).toLowerCase();
        if (FileEnd.equals("doc") || FileEnd.equals("docx")) {
            isWordFile = true;
        } else {
            isWordFile = false;
        }
        return isWordFile;
    }

    //腾讯TBS
    @Override
    public void onCoreInitFinished() {
        Log.d("test", "onCoreInitFinished");
    }

    @Override
    public void onViewInitFinished(boolean isX5Core) {
        Log.d("test", "onViewInitFinished,isX5Core =" + isX5Core);
    }

    @Override
    public void onReceiveValue(String val) {
        Log.d("test", "onReceiveValue,val =" + val);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions,
                                           int[] grantResults) {

    }

    public void openFileReader(Context context, String pathName) {

        HashMap<String, String> params = new HashMap<String, String>();
        params.put("local", "true");
        JSONObject Object = new JSONObject();
        try {
            Object.put("pkgName", context.getApplicationContext().getPackageName());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        params.put("menuData", Object.toString());
        QbSdk.getMiniQBVersion(context);
        int ret = QbSdk.openFileReader(context, pathName, params, this);

    }

    /**
     * 删除单个文件
     *
     * @param filePathName 要删除的文件的文件名
     * @return 单个文件删除成功返回true，否则返回false
     */
    private boolean deleteSingleFile(String filePathName) {
        File file = new File(filePathName);
        // 如果文件路径所对应的文件存在，并且是一个文件，则直接删除
        if (file.exists() && file.isFile()) {
            if (file.delete()) {
                CustomToast.showToast(ListEmReportActivity.this, "文件删除成功");
                UpdateAdapterData();
                return true;
            } else {
                CustomToast.showToast(ListEmReportActivity.this, "文件删除失败");
                return false;
            }
        } else {
            CustomToast.showToast(ListEmReportActivity.this, "文件不存在");
            return false;
        }
    }

    private void UpdateAdapterData(){
        adapter.clear();
        adapter.addAll(getDocPathFromSD());
        adapter.notifyDataSetChanged();
        mShowPathLv.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        intent.setClass(this, Main2Activity.class);
        startActivity(intent);
        this.finish();
    }

    // 停止自动翻页
    @Override
    protected void onStop() {
        super.onStop();
        ListEmReportActivity.this.finish();
    }

    // 停止自动翻页
    @Override
    protected void onDestroy() {
        super.onDestroy();
        ListEmReportActivity.this.finish();
    }

}
