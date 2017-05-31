package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.foodmarket.R;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class NowBuyAdapter extends BaseAdapter{
    private Context context;
    @Override
    public int getCount() {
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_shop_dec,null);
            viewHolder = new ViewHolder();
            viewHolder.mShopPicture = (ImageView) convertView.findViewById(R.id.iv_now_buy_shop_picture);
            viewHolder.mShopName = (TextView) convertView.findViewById(R.id.text_now_buy_shop_name);
            viewHolder.mShopFlavor = (TextView) convertView.findViewById(R.id.text_now_buy_shop_flavor);
            viewHolder.mShopUnitPrice = (TextView) convertView.findViewById(R.id.text_now_buy_shop_unit_price);
            viewHolder.mShopNum = (TextView) convertView.findViewById(R.id.text_now_buy_shop_num);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        return convertView;
    }
    class ViewHolder{
        ImageView mShopPicture;
        TextView mShopName,mShopFlavor,mShopUnitPrice,mShopNum;
    }
}
