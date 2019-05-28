package com.lkkdesign.changlong.widgets;

import android.app.Dialog;
import android.content.Context;
import android.widget.TextView;

import com.lkkdesign.changlong.R;

public class MyProcessDialog extends Dialog{

    private TextView txt_info;

    public MyProcessDialog(Context context,String msg){
        super(context, R.style.MyProgressDialog);
        this.setContentView(R.layout.progress_dialog);
        txt_info = (TextView)this.findViewById(R.id.txt_wait);
        if(null != txt_info){
            txt_info.setText(msg);
        }
    }
    public void setMsg(String msg){
        if(null != txt_info){
            txt_info.setText(msg);
        }
    }
}