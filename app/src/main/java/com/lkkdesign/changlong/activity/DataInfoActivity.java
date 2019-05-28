package com.lkkdesign.changlong.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.data.dao.DataDAO;
import com.lkkdesign.changlong.data.model.Tb_data;

import java.util.List;

public class DataInfoActivity extends AppCompatActivity {
    public static final String FLAG = "num";
    private ListView lvinfo;
    private String strType = "";
    private Button btnaddData;
    private ImageView iv_return;
    DataDAO dataDAO = new DataDAO(DataInfoActivity.this);
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_info);

        initView();
        showInfo();

    }

    protected void initView() {
        lvinfo = (ListView) findViewById(R.id.lvinaccountinfo);
        btnaddData = (Button) findViewById(R.id.btnaddData);
        iv_return = (ImageView) findViewById(R.id.iv_return);

        lvinfo.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String strInfo = String.valueOf(((TextView) view).getText());
                android.util.Log.i("strInfo", "=strInfo=" + strInfo);
                String strinfo[] = strInfo.split("\t\t\t");
                showData(strinfo[0].replaceAll("柜号：", ""));
            }
        });

        btnaddData.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                intent.setClass(DataInfoActivity.this, DataAddActivity.class);
                startActivity(intent);
            }
        });
    }

    private void showInfo() {
        String[] strInfos = null;
        ArrayAdapter<String> arrayAdapter = null;
        //DataDAO blessinginfo = new DataDAO(DataInfoActivity.this);
        List<Tb_data> listinfos = dataDAO.getScrollData(0, (int) dataDAO.getCount());
        strInfos = new String[listinfos.size()];
        int m = 0;
        for (Tb_data tb_data : listinfos) {
            strInfos[m] = "柜号：" + tb_data.getItem() + "\t\t\t福签：" + tb_data.getName() + "\t\t\t签数：" + tb_data.getTime();
            System.out.println("strInfos[m]="+strInfos[m]);
            m++;
        }

        arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, strInfos);
        lvinfo.setAdapter(arrayAdapter);

    }


    private void showData(final String strTitle) {

//        Tb_data tb_data = dataDAO.findByClassic(strTitle);
        Log.i("DataInfoActivity", "strTitle=" + strTitle);

//        final String strinfo = tb_data.getItem() + "\n" + tb_data.getName() + "\n" + tb_data.getTime() + "\n" + tb_data.getMark();
//        //当接收到Click事件之后触发
//        new MaterialDialog.Builder(DataInfoActivity.this)// 初始化建造者
//                .title(tb_data.getItem())// 标题
//                .content(strinfo)// 内容
//                .positiveText(R.string.confirm)
//                .negativeText(R.string.cancel)
//                .neutralText(R.string.delete)
//                .onPositive(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                })
//                .onNegative(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//
//                    }
//                })
//                .onNeutral(new MaterialDialog.SingleButtonCallback() {
//                    @Override
//                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {
//                        dataDAO.deteleByItemCode(strTitle);
//                        showInfo();
//                    }
//                })
//                .show();// 显示对话框

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        showInfo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        DataInfoActivity.this.finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DataInfoActivity.this.finish();
    }


}
