package com.lkkdesign.changlong.adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.lkkdesign.changlong.R;

public class ImageAdapter extends BaseAdapter {
    // 定义Context
    private Context mContext;
    // 定义整型数组 即图片源
    private Integer[] mImageIds =
            {
                    R.mipmap.icon_auto,
                    R.mipmap.icon_handraulic,
                    R.mipmap.icon_timing,
                    R.mipmap.icon_photometer,
                    R.mipmap.icon_curve,
                    R.mipmap.icon_lookover,
                    R.mipmap.icon_eergency,
                    R.mipmap.icon_theme,
                    R.mipmap.ic_launcher_round,

            };

    public ImageAdapter(Context c) {
        mContext = c;
    }

    // 获取图片的个数
    public int getCount() {
        return mImageIds.length;
    }

    // 获取图片在库中的位置
    public Object getItem(int position) {
        return position;
    }


    // 获取图片ID
    public long getItemId(int position) {
        return position;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            // 给ImageView设置资源
            imageView = new ImageView(mContext);
            // 设置布局 图片120×120显示
            imageView.setLayoutParams(new GridView.LayoutParams(85, 85));
            // 设置显示比例类型
            imageView.setScaleType(ImageView.ScaleType.FIT_CENTER);
        } else {
            imageView = (ImageView) convertView;
        }

        imageView.setImageResource(mImageIds[position]);
        return imageView;
    }
}