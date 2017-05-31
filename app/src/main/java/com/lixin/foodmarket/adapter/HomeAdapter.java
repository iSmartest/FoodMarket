package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.activity.ShopDecActivity;
import com.lixin.foodmarket.bean.HomeBean;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/16
 * My mailbox is 1403241630@qq.com
 */

public class HomeAdapter extends BaseAdapter {
    private Context context;
    private List<HomeBean.hotCommoditys> hotCommoditys;
    public void setHome(Context context, List<HomeBean.hotCommoditys> hotCommoditys) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_gridview3,null);
            viewHolder = new ViewHolder();
            viewHolder.hotGrid = (GridView) convertView.findViewById(R.id.gridView3);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        HomeHotGridAdapter homeHotGridAdapter = new HomeHotGridAdapter(context,hotCommoditys);
        viewHolder.hotGrid.setAdapter(homeHotGridAdapter);
        viewHolder.hotGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context,ShopDecActivity.class);
                intent.putExtra("rotateid",hotCommoditys.get(position).getCommodityid());
                intent.putExtra("rotateIcon",hotCommoditys.get(position).getCommodityIcon());
                context.startActivity(intent);
            }
        });
        return convertView;
    }


    class ViewHolder {
        GridView hotGrid;
    }
}
