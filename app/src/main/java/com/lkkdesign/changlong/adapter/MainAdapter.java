/*
 * Copyright 2016 Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lkkdesign.changlong.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.lkkdesign.changlong.R;

import java.util.List;

/**
 * Created by YOLANDA on 2016/7/22.
 */
public class MainAdapter extends BaseAdapter<MainAdapter.ViewHolder> {

    private List<String> mDataList;
    //先声明一个int成员变量
    private int thisPosition;

    public MainAdapter(Context context) {
        super(context);
    }

    public void notifyDataSetChanged(List<String> dataList) {
        this.mDataList = dataList;
        super.notifyDataSetChanged();
    }

    //再定义一个int类型的返回值方法
    public int getthisPosition() {
        return thisPosition;
    }
    //其次定义一个方法用来绑定当前参数值的方法
    //此方法是在调用此适配器的地方调用的，此适配器内不会被调用到
    public void setThisPosition(int thisPosition) {
        this.thisPosition = thisPosition;
    }

    @Override
    public int getItemCount() {
        return mDataList == null ? 0 : mDataList.size();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(getInflater().inflate(R.layout.item_menu_main, parent, false));
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        //隔行变色，但有小问题。下半部分不起效果，往回滑动也不起作用。
//        if (position % 2 == 0) {
//            holder.v.setBackgroundColor(Color.rgb(225,255,255));
//        }
        holder.setData(mDataList.get(position));
        if (position == getthisPosition()) {
            holder.v.setBackgroundColor(Color.rgb(235,235,235));
        } else {
            holder.v.setBackgroundColor(Color.WHITE);
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTitle;
        View v;

        public ViewHolder(View itemView) {
            super(itemView);
            tvTitle = (TextView) itemView.findViewById(R.id.tv_title);
            v = itemView;
        }

        public void setData(String title) {
            this.tvTitle.setText(title);
        }
    }

}
