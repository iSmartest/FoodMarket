package com.lixin.foodmarket.activity;

import android.os.Bundle;

import com.lixin.foodmarket.R;

/**
 * Created by 小火
 * Create time on  2017/5/20
 * My mailbox is 1403241630@qq.com
 */

public class RefundDecActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_refund_dec);
        hideBack(false);
        setTitleText("我要退款");
    }
}
