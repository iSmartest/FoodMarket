package com.lixin.foodmarket.adapter;

import android.content.Context;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.HomeBean;
import com.lixin.foodmarket.tool.ImageManager;
import com.lixin.foodmarket.view.RoundedImageView;

import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/31
 * My mailbox is 1403241630@qq.com
 */

public class MyGridViewAdpter extends BaseAdapter{
    private Context context;
    private List<HomeBean.classifyBottom> lists;//数据源
    private int mIndex; // 页数下标，标示第几页，从0开始
    private int mPargerSize;// 每页显示的最大的数量

    public MyGridViewAdpter(Context context, List<HomeBean.classifyBottom> lists,
                            int mIndex, int mPargerSize) {
        this.context = context;
        this.lists = lists;
        this.mIndex = mIndex;
        this.mPargerSize = mPargerSize;
    }

    /**
     * 先判断数据及的大小是否显示满本页lists.size() > (mIndex + 1)*mPagerSize
     * 如果满足，则此页就显示最大数量lists的个数
     * 如果不够显示每页的最大数量，那么剩下几个就显示几个
     */
    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return lists.size() > (mIndex + 1) * mPargerSize ?
                mPargerSize : (lists.size() - mIndex*mPargerSize);
    }

    @Override
    public HomeBean.classifyBottom getItem(int arg0) {
        // TODO Auto-generated method stub
        return lists.get(arg0 + mIndex * mPargerSize);
    }

    @Override
    public long getItemId(int arg0) {
        // TODO Auto-generated method stub
        return arg0 + mIndex * mPargerSize;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_view, null);
            holder.tv_name = (TextView)convertView.findViewById(R.id.item_name);
            holder.iv_nul = (RoundedImageView)convertView.findViewById(R.id.item_image);
            convertView.setTag(holder);
        }else {
            holder = (ViewHolder)convertView.getTag();
        }
        //重新确定position因为拿到的总是数据源，数据源是分页加载到每页的GridView上的
        final int pos = position + mIndex * mPargerSize;//假设mPageSiez
        //假设mPagerSize=8，假如点击的是第二页（即mIndex=1）上的第二个位置item(position=1),那么这个item的实际位置就是pos=9
        holder.tv_name.setText(lists.get(pos).getClassifyType());
        String img = lists.get(pos).getClassifyIcon();
        if (TextUtils.isEmpty(img)){
            holder.iv_nul.setImageResource(R.drawable.image_fail_empty);
        }else {
            ImageManager.imageLoader.displayImage(img,holder.iv_nul,ImageManager.options3);
        }
//        Glide.with(context).load(lists.get(pos).getClassifyIcon()).into(holder.iv_nul);
//        添加item监听
//        convertView.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View arg0) {
//                // TODO Auto-generated method stub
//                Toast.makeText(context, "您点击了"  + lists.get(pos).getName(), Toast.LENGTH_SHORT).show();
//            }
//        });
        return convertView;
    }
    static class ViewHolder{
        private TextView tv_name;
        private RoundedImageView iv_nul;
    }
}
