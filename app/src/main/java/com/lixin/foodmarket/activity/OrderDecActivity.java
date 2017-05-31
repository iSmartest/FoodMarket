package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import com.lixin.foodmarket.R;

/**
 * Created by 小火
 * Create time on  2017/5/20
 * My mailbox is 1403241630@qq.com
 */

public class OrderDecActivity extends BaseActivity{
    LinearLayout mMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_dec);
        hideBack(false);
        setTitleText("订单详情");
        initView();
    }

    private void initView() {
        mMessage = (LinearLayout) findViewById(R.id.linear_order_dec_message);
        mMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OrderDecActivity.this,DistributionInformationDecActivity.class));
            }
        });
    }
}
