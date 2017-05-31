package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.foodmarket.R;

import java.util.List;
import java.util.Map;

/**
 * Created by 小火
 * Create time on  2017/5/20
 * My mailbox is 1403241630@qq.com
 */

public class TimeLineAdapter extends BaseAdapter{
    private Context context;
    private List<Map<String,Object>> list;
    private LayoutInflater inflater;
    public TimeLineAdapter(Context context, List<Map<String, Object>> list) {
        super();
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        TimeLineHolder viewHolder = null;
        if (convertView == null) {
            inflater = LayoutInflater.from(parent.getContext());
            convertView = inflater.inflate(R.layout.item_logistics_time, null);
            viewHolder = new TimeLineHolder();

            viewHolder.title = (TextView) convertView.findViewById(R.id.title);
            viewHolder.time = (TextView) convertView.findViewById(R.id.time);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (TimeLineHolder) convertView.getTag();
        }

        String titleStr = list.get(position).get("title").toString();
        String timeStr = list.get(position).get("time").toString();
        viewHolder.title.setText(titleStr);
        viewHolder.time.setText(timeStr);

        return convertView;

    }

    static class TimeLineHolder{
        private TextView title,time;
    }

}
