package com.lixin.foodmarket.activity;

import android.os.Bundle;
import android.widget.ListView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.TimeLineAdapter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by 小火
 * Create time on  2017/5/20
 * My mailbox is 1403241630@qq.com
 */

public class DistributionInformationDecActivity extends BaseActivity{
    private ListView listView;
    private TimeLineAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_distribution_information_dec);
        hideBack(false);
        setTitleText("配送信息详情");
        initView();

    }

    private void initView() {
        listView=(ListView) findViewById(R.id.lv_list);
        listView.setDividerHeight(0);
        adapter = new TimeLineAdapter(this, initData());
        listView.setAdapter(adapter);
    }
    private List<Map<String, Object>> initData() {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("title", "提交已完成......");
        map.put("time", "2015-10-22  14:00:00");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "正在审核中......");
        map.put("time", "2015-10-22  15:00:00");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "客服将会给您打电话......");
        map.put("time", "2015-10-22  16:00:00");
        list.add(map);

        map = new HashMap<String, Object>();
        map.put("title", "订单已完成");
        map.put("time", "2015-10-22  17:00:00");
        list.add(map);
        return list;
    }
}
