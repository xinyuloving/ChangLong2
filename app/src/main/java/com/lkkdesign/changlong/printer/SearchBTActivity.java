package com.lkkdesign.changlong.printer;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.lkkdesign.changlong.activity.AutoMeasureActivity;
import com.lkkdesign.changlong.activity.BaseSMRecycleViewActivity;
import com.lkkdesign.changlong.activity.Main2Activity;
import com.lkkdesign.changlong.activity.ManualMeasureSecActivity;
import com.lkkdesign.changlong.config.Constants;
import com.lkkdesign.changlong.utils.CustomToast;
import com.lvrenyang.io.BTPrinting;
import com.lvrenyang.io.Pos;
import com.lvrenyang.io.base.IOCallBack;
import com.lkkdesign.changlong.R;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothClass.Service;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;


public class SearchBTActivity extends Activity implements OnClickListener, IOCallBack {

    private LinearLayout linearlayoutdevices;
    private ProgressBar progressBarSearchStatus;
    private BroadcastReceiver broadcastReceiver = null;
    private IntentFilter intentFilter = null;
//    private EditText editText;
    private TextView tvShow;

    Button btnSearch, btnDisconnect, btnPrint;
    SearchBTActivity mActivity;

    ExecutorService es = Executors.newScheduledThreadPool(30);
    Pos mPos = new Pos();
    BTPrinting mBt = new BTPrinting();

    private static String TAG = "SearchBTActivity";
    private String strType = "";
    private String strStrPrintInfo = "";
    private Intent intent = new Intent();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchbt_print);

        Intent getIntent = getIntent();
        strType = getIntent.getStringExtra("type");//从哪一个Activity进入当前页面
        strStrPrintInfo = getIntent.getStringExtra("printInfo");//需要打印的内容，从上一个页面传递过来
        Log.i(TAG,"strType="+strType);
        Log.i(TAG,"strStrPrintInfo="+strStrPrintInfo);

        mActivity = this;

        initView();
        initBroadcast();
    }

    private void initView(){
        progressBarSearchStatus = (ProgressBar) findViewById(R.id.progressBarSearchStatus);
        linearlayoutdevices = (LinearLayout) findViewById(R.id.linearlayoutdevices);
        btnSearch = (Button) findViewById(R.id.btn_Search);
        btnDisconnect = (Button) findViewById(R.id.btn_Disconnect);
        btnPrint = (Button) findViewById(R.id.btn_Print);
//        editText = (EditText) findViewById(R.id.editText);
        tvShow = (TextView) findViewById(R.id.tv_show);
        btnSearch.setOnClickListener(this);
        btnDisconnect.setOnClickListener(this);
        btnPrint.setOnClickListener(this);
        btnSearch.setEnabled(true);
        btnDisconnect.setEnabled(false);
        btnPrint.setEnabled(false);

        mPos.Set(mBt);
        mBt.SetCallBack(this);

        tvShow.setText("打印内容：\n" +strStrPrintInfo);
    }

    public void onClick(View arg0) {
        // TODO Auto-generated method stub
        switch (arg0.getId()) {
            case R.id.btn_Search: {
                btnDisconnect.setVisibility(View.INVISIBLE);
                btnPrint.setVisibility(View.INVISIBLE);
                BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();
                if (null == adapter) {
                    finish();
                    break;
                }

                if (!adapter.isEnabled()) {
                    if (adapter.enable()) {
                        while (!adapter.isEnabled())
                            ;
                        Log.v(TAG, "Enable BluetoothAdapter");
                    } else {
                        finish();
                        break;
                    }
                }

                adapter.cancelDiscovery();
                linearlayoutdevices.removeAllViews();
                adapter.startDiscovery();
                break;
            }

            case R.id.btn_Disconnect:
                es.submit(new TaskClose(mBt));
                btnPrint.setVisibility(View.INVISIBLE);
                btnDisconnect.setVisibility(View.INVISIBLE);
                break;

            case R.id.btn_Print:
                btnPrint.setEnabled(false);
                es.submit(new TaskPrint(mPos));
                break;
        }
    }

    private void initBroadcast() {
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                // TODO Auto-generated method stub
                String action = intent.getAction();
                BluetoothDevice device = intent
                        .getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                    if (device == null)
                        return;
                    final String address = device.getAddress();
                    String name = device.getName();
                    if (name == null)
                        name = "BT";
                    else if (name.equals(address))
                        name = "BT";
                    Button button = new Button(context);

                    BluetoothClass btClass = device.getBluetoothClass();
                    int nClass = 0;
                    if (btClass.hasService(Service.AUDIO))
                        nClass |= Service.AUDIO;
                    else if (btClass.hasService(Service.CAPTURE))
                        nClass |= Service.CAPTURE;
                    else if (btClass.hasService(Service.INFORMATION))
                        nClass |= Service.INFORMATION;
                    else if (btClass.hasService(Service.LIMITED_DISCOVERABILITY))
                        nClass |= Service.LIMITED_DISCOVERABILITY;
                    else if (btClass.hasService(Service.NETWORKING))
                        nClass |= Service.NETWORKING;
                    else if (btClass.hasService(Service.OBJECT_TRANSFER))
                        nClass |= Service.OBJECT_TRANSFER;
                    else if (btClass.hasService(Service.POSITIONING))
                        nClass |= Service.POSITIONING;
                    else if (btClass.hasService(Service.RENDER))
                        nClass |= Service.RENDER;
                    else if (btClass.hasService(Service.TELEPHONY))
                        nClass |= Service.TELEPHONY;

                    nClass |= btClass.getDeviceClass();

                    String strClass = String.format("%06X", nClass);

                    button.setText(name + ": " + address + "(" + strClass + ")");

                    for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                        Button btn = (Button) linearlayoutdevices.getChildAt(i);
                        if (btn.getText().equals(button.getText())) {
                            return;
                        }
                    }

                    button.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
                    button.setOnClickListener(new OnClickListener() {

                        public void onClick(View arg0) {
                            // TODO Auto-generated method stub
                            CustomToast.showLongToast(mActivity, "蓝牙正在连接中......");
                            btnSearch.setEnabled(false);
                            linearlayoutdevices.setEnabled(false);
                            for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                                Button btn = (Button) linearlayoutdevices.getChildAt(i);
                                btn.setEnabled(false);
                            }
                            btnDisconnect.setEnabled(false);
                            btnPrint.setEnabled(false);
                            es.submit(new TaskOpen(mBt, address, mActivity));
                            //es.submit(new TaskTest(mPos, mBt, address, mActivity));
                        }
                    });
                    button.getBackground().setAlpha(100);
                    linearlayoutdevices.addView(button);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_STARTED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(true);
                } else if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED
                        .equals(action)) {
                    progressBarSearchStatus.setIndeterminate(false);
                }

            }

        };
        intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothDevice.ACTION_FOUND);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);
        intentFilter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        registerReceiver(broadcastReceiver, intentFilter);
    }

    private void uninitBroadcast() {
        if (broadcastReceiver != null)
            unregisterReceiver(broadcastReceiver);
    }

    public class TaskTest implements Runnable {
        Pos pos = null;
        BTPrinting bt = null;
        String address = null;
        Context context = null;

        public TaskTest(Pos pos, BTPrinting bt, String address, Context context) {
            this.pos = pos;
            this.bt = bt;
            this.address = address;
            this.context = context;
            pos.Set(bt);
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            for (int i = 0; i < 1000; ++i) {
                for (int retry = 0; retry < 10; ++retry) {
                    if (Ticket(i))
                        break;
                    else
                        try {
                            Thread.sleep(1000);
                        } catch (InterruptedException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                }
            }
        }

        private boolean Ticket(int i) {
            boolean result = false;

            long beginTime = System.currentTimeMillis();
            if (bt.Open(address, context)) {
                long endTime = System.currentTimeMillis();
                if (pos.POS_RTQueryStatus(new byte[1], 1, 2000, 3)) {
                    pos.POS_S_Align(0);
                    pos.POS_S_TextOut(i + " " + "Open   UsedTime:" + (endTime - beginTime) + "\r\n", 0, 0, 0, 0, 0);
                    beginTime = System.currentTimeMillis();
                    result = pos.POS_RTQueryStatus(new byte[1], 1, 1000, 3);
                    endTime = System.currentTimeMillis();
                    pos.POS_S_TextOut(i + " " + "Ticket UsedTime:" + (endTime - beginTime) + " " + (result ? "Succeed" : "Failed") + "\r\n", 0, 0, 0, 0, 0);
                    result &= bt.IsOpened();
                }
                bt.Close();

                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            return result;
        }
    }

    public class TaskOpen implements Runnable {
        BTPrinting bt = null;
        String address = null;
        Context context = null;

        public TaskOpen(BTPrinting bt, String address, Context context) {
            this.bt = bt;
            this.address = address;
            this.context = context;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Open(address, context);
        }
    }

    public class TaskPrint implements Runnable {
        Pos pos = null;

        public TaskPrint(Pos pos) {
            this.pos = pos;
        }


        @Override
        public void run() {
            // TODO Auto-generated method stub
            String strPrint = "";
//            if (editText.getText().length() > 0) {
//                strPrint = editText.getText().toString() + "\r\n\r\n\r\n";
//            } else {
                strPrint = strStrPrintInfo+ "\r\n\r\n\r\n";
                Log.i(TAG,"strPrint="+strPrint);
//            }
//            final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, strPrint, AppStart.nPrintWidth,
//                    AppStart.bCutter, AppStart.bDrawer, AppStart.bBeeper, AppStart.nPrintCount,
//                    AppStart.nPrintContent, AppStart.nCompressMethod);
            final int bPrintResult = Prints.PrintTicket(getApplicationContext(), pos, strPrint, AppStart.nPrintWidth,
                    AppStart.bCutter, AppStart.bDrawer, AppStart.bBeeper, AppStart.nPrintCount,
                    1, AppStart.nCompressMethod);
            Log.i(TAG,"pos="+pos);
            Log.i(TAG,"nPrintWidth="+AppStart.nPrintWidth);
            Log.i(TAG,"AppStart.bCutter="+AppStart.bCutter);
            Log.i(TAG,"AppStart.bDrawer="+AppStart.bDrawer);
            Log.i(TAG,"AppStart.bBeeper="+ AppStart.bBeeper);
            Log.i(TAG,"AppStart.nPrintCount="+AppStart.nPrintCount);
            Log.i(TAG,"AppStart.nPrintContent="+AppStart.nPrintContent);
            Log.i(TAG,"AppStart.nCompressMethod="+AppStart.nCompressMethod);

            final boolean bIsOpened = pos.GetIO().IsOpened();

            mActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    // TODO Auto-generated method stub
                    CustomToast.showToast(mActivity.getApplicationContext(), (bPrintResult == 0) ? getResources().getString(R.string.printsuccess) : getResources().getString(R.string.printfailed) + " " + Prints.ResultCodeToString(bPrintResult));
                    mActivity.btnPrint.setEnabled(bIsOpened);
                }
            });

        }
    }

    public class TaskClose implements Runnable {
        BTPrinting bt = null;

        public TaskClose(BTPrinting bt) {
            this.bt = bt;
        }

        @Override
        public void run() {
            // TODO Auto-generated method stub
            bt.Close();
        }

    }

    @Override
    public void OnOpen() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(true);
                btnPrint.setEnabled(true);
                btnSearch.setEnabled(false);
                linearlayoutdevices.setEnabled(false);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(false);
                }
                //Toast.makeText(mActivity, "蓝牙打印机已连接", Toast.LENGTH_SHORT).show();
                CustomToast.showToast(mActivity,"蓝牙打印机已连接");
                btnDisconnect.setVisibility(View.VISIBLE);
                btnPrint.setVisibility(View.VISIBLE);
                if (AppStart.bAutoPrint) {
                    btnPrint.performClick();
                }
            }
        });
    }

    @Override
    public void OnOpenFailed() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                btnSearch.setEnabled(true);
                linearlayoutdevices.setEnabled(true);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
//                Toast.makeText(mActivity, "连接失败", Toast.LENGTH_SHORT).show();
                CustomToast.showToast(mActivity,"连接失败");
            }
        });
    }

    @Override
    public void OnClose() {
        // TODO Auto-generated method stub
        this.runOnUiThread(new Runnable() {

            @Override
            public void run() {
                btnDisconnect.setEnabled(false);
                btnPrint.setEnabled(false);
                btnSearch.setEnabled(true);
                linearlayoutdevices.setEnabled(true);
                for (int i = 0; i < linearlayoutdevices.getChildCount(); ++i) {
                    Button btn = (Button) linearlayoutdevices.getChildAt(i);
                    btn.setEnabled(true);
                }
            }
        });
    }

    @Override
    public void onBackPressed() {
        if ("main".equals(strType)) {
            intent.setClass(SearchBTActivity.this, Main2Activity.class);
            startActivity(intent);
            this.finish();
        }else if("BaseSMRecycleViewActivity".equals(strType)){
            intent.setClass(SearchBTActivity.this,BaseSMRecycleViewActivity.class);
            startActivity(intent);
            this.finish();
        } else if("AutoMeasureActivity".equals(strType)){
            intent.setClass(SearchBTActivity.this, AutoMeasureActivity.class);
            startActivity(intent);
            this.finish();
        }else if("ManualMeasureSecActivity".equals(strType)){
            intent.setClass(SearchBTActivity.this, ManualMeasureSecActivity.class);
            startActivity(intent);
            this.finish();
        }else {
            intent.setClass(SearchBTActivity.this, com.lkkdesign.changlong.printer.AppStart.class);
            startActivity(intent);
            this.finish();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        uninitBroadcast();
        btnDisconnect.performClick();
    }

}
