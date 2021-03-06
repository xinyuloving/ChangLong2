package com.lkkdesign.changlong.holder;

import android.view.View;
import android.widget.ImageView;

import com.bigkoo.convenientbanner.holder.Holder;
import com.lkkdesign.changlong.R;

/**
 * Created by hyy on 18/8/4.
 * 本地图片Holder例子
 */
public class LocalImageHolderView extends Holder<Integer> {
    private ImageView imageView;

    public LocalImageHolderView(View itemView) {
        super(itemView);
    }

    @Override
    protected void initView(View itemView) {
        imageView =itemView.findViewById(R.id.ivPost);
    }

    @Override
    public void updateUI(Integer data) {
        imageView.setImageResource(data);
    }
}