package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.ClassListBean;
import com.lixin.foodmarket.bean.MyWelletBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MyWalletActivity extends BaseActivity{
    private TextView mMoneyDec,mWalletNum;
    private ImageView mBack;
    private int nowPage = 1;
    private String uid;
    private List<MyWelletBean.moneyList> mList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_wallet);
        uid = (String) SPUtils.get(MyWalletActivity.this,"uid","");
        mList = new ArrayList<>();
        initView();
        getMyWellet();
    }


    private void initView() {
        mMoneyDec = (TextView) findViewById(R.id.tv_my_wallet_money_dec);
        mMoneyDec.setOnClickListener(this);
        mWalletNum = (TextView) findViewById(R.id.text_my_wallet_num);
        mBack = (ImageView) findViewById(R.id.Iv_my_wallet_back);
        mBack.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.Iv_my_wallet_back:
                finish();
                break;
            case R.id.tv_my_wallet_money_dec:
                Intent intent = new Intent(MyWalletActivity.this,MoneyDecActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("moneyList", (Serializable) mList);
                intent.putExtras(bundle);
                startActivity(intent);
                break;
        }
    }

    private void getMyWellet() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getWalletInfo\",\"nowPage\":\"" + nowPage +"\",\"uid\":\""
                + uid + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageShort(context, e.getMessage());
                dialog1.dismiss();
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("MyWalletActivity", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                MyWelletBean myWelletBean = gson.fromJson(response, MyWelletBean.class);
                if (myWelletBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, myWelletBean.resultNote);
                    return;
                }
                List<MyWelletBean.moneyList> commodityslist = myWelletBean.moneyList;
                mList.addAll(commodityslist);
                mWalletNum.setText(myWelletBean.getTotalMoney());
            }
        });
    }

}
