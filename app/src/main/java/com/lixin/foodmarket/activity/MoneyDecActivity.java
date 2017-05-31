package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ListView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.MyWelletBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MoneyDecActivity extends BaseActivity{
    private ArrayList<? extends MyWelletBean.moneyList> mList;
    private ListView money_dec_list;
    private MoneyDecAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moeny_dec);
        Intent intent = getIntent();
        mList = intent.getParcelableArrayListExtra("moneyList");
        hideBack(false);
        setTitleText("零钱明细");
        initView();
    }

    private void initView() {
        money_dec_list = (ListView) findViewById(R.id.money_dec_list);
        mAdapter = new MoneyDecAdapter(MoneyDecActivity.this,mList);
        money_dec_list.setAdapter(mAdapter);
    }
}
