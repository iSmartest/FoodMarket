package com.lixin.foodmarket.activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.MyWelletBean;

import java.util.ArrayList;

/**
 * Created by 小火
 * Create time on  2017/5/27
 * My mailbox is 1403241630@qq.com
 */

public class MoneyDecAdapter extends BaseAdapter{
    private Context context;
    private ArrayList<? extends MyWelletBean.moneyList> mList;
    public MoneyDecAdapter(Context context, ArrayList<? extends MyWelletBean.moneyList> mList) {
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
            convertView = LayoutInflater.from(context).inflate(R.layout.item_money_dec,null);
            viewHolder = new ViewHolder();
            viewHolder.mMoneyNum = (TextView) convertView.findViewById(R.id.text_item_money_dec_num);
            viewHolder.mMoneySoure = (TextView) convertView.findViewById(R.id.text_item_money_dec_money_soure);
            viewHolder.mMoneyTime = (TextView) convertView.findViewById(R.id.text_item_money_dec_time);
            convertView.setTag(viewHolder);
        }else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        MyWelletBean.moneyList moneyList = mList.get(position);
        viewHolder.mMoneyNum.setText(moneyList.getMoneyNum());
        viewHolder.mMoneyTime.setText(moneyList.getMoneyTime());
        if (moneyList.getMoneySoure().equals("0")){
            viewHolder.mMoneySoure.setText("补差");
        }else if (moneyList.getMoneySoure().equals("1")){
            viewHolder.mMoneySoure.setText("退款");
        }else if (moneyList.getMoneySoure().equals("2")){
            viewHolder.mMoneySoure.setText("消费");
        }
        return null;
    }
    class ViewHolder{
        TextView mMoneySoure,mMoneyNum,mMoneyTime;
    }
}
