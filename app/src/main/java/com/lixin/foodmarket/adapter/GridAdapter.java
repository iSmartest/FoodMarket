package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.ClassBean;
import com.lixin.foodmarket.tool.ImageManager;


import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/16
 * My mailbox is 1403241630@qq.com
 */

public class GridAdapter extends BaseAdapter{
    private Context context;
    private List<ClassBean.classifyMeun.meun> meun;
    private int defPostion;
    public GridAdapter(Context context) {
        this.context = context;
    }
    public void setGrid(Context context, List<ClassBean.classifyMeun.meun> meun) {
        this.context = context;
        this.meun = meun;
    }
    @Override
    public int getCount() {
        return meun == null ? 0 : meun.size();
    }

    @Override
    public Object getItem(int position) {
        return meun.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_picture,null);
            viewHolder = new ViewHolder();
            viewHolder.mPicture = (ImageView) convertView.findViewById(R.id.iv_grid_picture);
            viewHolder.mName = (TextView) convertView.findViewById(R.id.text_grid_name);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        ClassBean.classifyMeun.meun meunList = meun.get(position);
        viewHolder.mName.setText(meunList.getMeunType());
        Log.i("ClassFragment", "getView: " + meunList.getMeunType());
        String img = meunList.getAdvertisementIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManager.imageLoader.displayImage(img,viewHolder.mPicture,ImageManager.options3);
        }
        return convertView;
    }

    public void setDefSelect(int position) {
        this.defPostion = position;
        notifyDataSetChanged();
    }


    class ViewHolder {
        ImageView mPicture;
        TextView mName;
    }
}
