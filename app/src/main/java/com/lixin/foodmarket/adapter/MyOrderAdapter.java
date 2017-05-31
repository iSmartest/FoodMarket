package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.MyOrderBean;

import org.json.JSONArray;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyOrderAdapter extends BaseAdapter{
    private Context context;
    private List<MyOrderBean.orders> mList;
    public void setMyOrder(Context context, List<MyOrderBean.orders> mList, String uid) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_my_order_list,null);
            viewHolder = new ViewHolder();
            viewHolder.order_number = (TextView) convertView.findViewById(R.id.order_number);
            viewHolder.order_state = (TextView) convertView.findViewById(R.id.order_state);
            viewHolder.total_price = (TextView) convertView.findViewById(R.id.total_price);
            viewHolder.commodity_lv = (ListView) convertView.findViewById(R.id.commodity_lv);
            viewHolder.bt_pay = (Button) convertView.findViewById(R.id.bt_pay);
            viewHolder.bt_delete = (Button) convertView.findViewById(R.id.bt_delete);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyOrderBean.orders ordersList = mList.get(position);
        viewHolder.order_number.setText(ordersList.getOrderId());
        viewHolder.total_price.setText(ordersList.getOderTotalPrice());
        OrderlistAdapter commodityAdapter = new OrderlistAdapter(ordersList.getOrderCommodity(),context);
        viewHolder.commodity_lv.setAdapter(commodityAdapter);
        switch (ordersList.getOrderState()){
            case "1":
                viewHolder.order_state.setText("待付款");
                //删除订单
                viewHolder.bt_delete.setVisibility(View.VISIBLE);
                viewHolder.bt_pay.setText("去付款");
                viewHolder.bt_delete.setText("取消订单");
                break;
            case "2":
                viewHolder.order_state.setText("待发货");
                //确认收货
                viewHolder.bt_pay.setText("我要退款");
                break;
            case "3":
                viewHolder.order_state.setText("待收货");
                //确认收货
                viewHolder.bt_pay.setText("确认订单");
                break;
            case "4":
                viewHolder.order_state.setText("已完成");
                //确认收货
                viewHolder.bt_pay.setText("删除订单");
                break;
            case "5":
                viewHolder.order_state.setText("退款中");
                //确认收货
                viewHolder.bt_pay.setText("查看退款详情");
                break;
        }
        return convertView;
    }

    class ViewHolder{
        TextView order_number;
        TextView order_state;
        TextView total_price;
        ListView commodity_lv;
        Button bt_pay;
        Button bt_delete;
    }
}
