package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.graphics.Paint;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.HomeBean;
import com.lixin.foodmarket.tool.ImageManager;


import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class HomeHotGridAdapter extends BaseAdapter{
    private Context context;
    private List<HomeBean.hotCommoditys> hotCommoditys;
    public HomeHotGridAdapter(Context context, List<HomeBean.hotCommoditys> hotCommoditys) {
        this.context = context;
        this.hotCommoditys = hotCommoditys;
    }

    @Override
    public int getCount() {
        return hotCommoditys == null ? 0 : hotCommoditys.size();
    }

    @Override
    public Object getItem(int position) {
        return hotCommoditys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_grid_shop,null);
            viewHolder = new ViewHolder();
            viewHolder.mPicture = (ImageView) convertView.findViewById(R.id.ima_grid_shop_picture);
            viewHolder.mShopName = (TextView) convertView.findViewById(R.id.text_grid_shop_name);
            viewHolder.mNowPrice = (TextView) convertView.findViewById(R.id.text_grid_shop_now_price);
            viewHolder.mMarketPrice = (TextView) convertView.findViewById(R.id.text_grid_shop_market_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeBean.hotCommoditys mList = hotCommoditys.get(position);
        viewHolder.mShopName.setText(mList.commodityTitle);
        viewHolder.mNowPrice.setText("现价:" + mList.getCommodityNewPrice());
        viewHolder.mMarketPrice.setText(mList.getCommodityOriginalPrice());
        String img = mList.getCommodityIcon();
        if (TextUtils.isEmpty(img)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManager.imageLoader.displayImage(img, viewHolder.mPicture, ImageManager.options3);
        }
        viewHolder.mMarketPrice.getPaint().setFlags(Paint. STRIKE_THRU_TEXT_FLAG|Paint.ANTI_ALIAS_FLAG);
        return convertView;
    }
    class ViewHolder{
        ImageView mPicture;
        TextView mShopName,mNowPrice,mMarketPrice;

    }
}
