package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.MyCollectionShareBean;
import com.lixin.foodmarket.tool.ImageManager;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyCollectionShareDecAdapter extends BaseAdapter{
    private Context context;
    private List<MyCollectionShareBean.commoditysList.commoditys> commoditys;
    public MyCollectionShareDecAdapter(List<MyCollectionShareBean.commoditysList.commoditys> commoditys, Context context) {
        this.context = context;
        this.commoditys = commoditys;
    }

    @Override
    public int getCount() {
        return commoditys == null ? 0 : commoditys.size();
    }

    @Override
    public Object getItem(int position) {
        return commoditys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collection_share,null);
            viewHolder = new ViewHolder();
            viewHolder.mDelete = (ImageView) convertView.findViewById(R.id.iv_my_collection_share_delete);
            viewHolder.mPicture = (ImageView) convertView.findViewById(R.id.iv_my_collection_share_shop_picture);
            viewHolder.mTitle = (TextView) convertView.findViewById(R.id.text_my_collection_share_shop_name);
            viewHolder.mNowPrice = (TextView) convertView.findViewById(R.id.text_my_collection_share_shop_price);
            viewHolder.mOriginalPrice = (TextView) convertView.findViewById(R.id.text_my_collection_share_shop_original_price);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyCollectionShareBean.commoditysList.commoditys mList = commoditys.get(position);
        viewHolder.mTitle.setText(mList.getCommodityTitle());
        viewHolder.mNowPrice.setText(mList.getCommodityNewPrice());
        viewHolder.mOriginalPrice.setText(mList.getCommodityOriginalPrice());
        String url = mList.getCommodityIcon();
        if (TextUtils.isEmpty(url)){
            viewHolder.mPicture.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManager.imageLoader.displayImage(url,viewHolder.mPicture,ImageManager.options3);
        }


        return convertView;
    }
    class ViewHolder {
        ImageView mPicture,mDelete;
        TextView mTitle,mNowPrice,mOriginalPrice;
    }
}
