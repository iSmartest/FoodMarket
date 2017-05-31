package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.MyCollectionShareBean;


import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyCollectionShareAdapter extends BaseAdapter{
    private Context context;
    private List<MyCollectionShareBean.commoditysList> mList;
    public void setMyCollectionShare(Context context, List<MyCollectionShareBean.commoditysList> mList, String uid) {
        this.context = context;
        this.mList = mList;
    }
    @Override
    public int getCount() {
        return mList == null ? 0 : mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder viewHolder;
        if (convertView == null){
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_collection_share_list,null);
            viewHolder = new ViewHolder();
            viewHolder.mTime = (TextView) convertView.findViewById(R.id.text_my_collection_share_time);
            viewHolder.mListView = (ListView) convertView.findViewById(R.id.my_collection_share_list);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyCollectionShareBean.commoditysList commoditysList = mList.get(position);
        viewHolder.mTime.setText(commoditysList.getSectionTime());
        MyCollectionShareDecAdapter myCollectionShareDecAdapter = new MyCollectionShareDecAdapter(commoditysList.getCommoditys(),context);
        viewHolder.mListView.setAdapter(myCollectionShareDecAdapter);
        return convertView;
    }

    class ViewHolder {
        ListView mListView;
        TextView mTime;
    }
}
