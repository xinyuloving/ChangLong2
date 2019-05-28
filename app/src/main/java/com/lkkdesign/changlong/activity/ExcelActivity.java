package com.lkkdesign.changlong.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;

import com.lkkdesign.changlong.R;
import com.lkkdesign.changlong.excel.Const;
import com.lkkdesign.changlong.excel.ExcelUtil;
import com.lkkdesign.changlong.excel.Order;
import com.lkkdesign.changlong.utils.DateUtil;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class ExcelActivity extends AppCompatActivity {

    List<Order> orders = new ArrayList<Order>();
    @BindView(R.id.button)
    Button button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_excel);
        ButterKnife.bind(this);

        initView();
    }

    private void initView() {
        int length = Const.OrderInfo.orderOne.length;
        for (int i = 0; i < length; i++) {
            Order order = new Order(Const.OrderInfo.orderOne[i][0], Const.OrderInfo.orderOne[i][1], Const.OrderInfo.orderOne[i][2], Const.OrderInfo.orderOne[i][3]);
            orders.add(order);
        }
    }

    @OnClick(R.id.button)
    public void onViewClicked() {
        try {
            ExcelUtil.writeExcel(ExcelActivity.this,
                    orders, "水质监测数据导出-" + DateUtil.getNowDateTime());
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
