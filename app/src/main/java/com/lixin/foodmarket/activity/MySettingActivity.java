package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.bean.ClassListBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.tool.DataCleanManager;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;
import com.nostra13.universalimageloader.utils.StorageUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MySettingActivity extends BaseActivity{
    private LinearLayout mAboutUs,mFeedback,mVersionUpdate,mClearCache;
    private TextView mSure,mCacheSize;
    private String url;
    private String cacheSize;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_setting);
        hideBack(false);
        setTitleText("设置");
        initView();
        getdata();
    }
    private void initView() {
        mAboutUs = (LinearLayout) findViewById(R.id.linear_my_setting_about_us);
        mAboutUs.setOnClickListener(this);
        mFeedback = (LinearLayout) findViewById(R.id.linear_my_setting_feedback);
        mFeedback.setOnClickListener(this);
        mVersionUpdate = (LinearLayout) findViewById(R.id.linear_my_setting_version_update);
        mVersionUpdate.setOnClickListener(this);
        mClearCache = (LinearLayout) findViewById(R.id.linear_my_setting_clear_cache);
        mClearCache.setOnClickListener(this);
        mSure = (TextView) findViewById(R.id.text_my_setting_sure);
        mSure.setOnClickListener(this);
        mCacheSize = (TextView) findViewById(R.id.text_my_setting_clear_cache_size);
        try {
            mCacheSize.setText(DataCleanManager.getTotalCacheSize(this));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.linear_my_setting_about_us:
                Intent intent1 = new Intent(context, EventDetailsWebActivity.class);
                intent1.putExtra("isStoreDetailsOrCheckviolation","3");
                intent1.putExtra(EventDetailsWebActivity.URL, url);
                startActivity(intent1);
                break;
            case R.id.linear_my_setting_feedback:
                Intent intent = new Intent(MySettingActivity.this,FeedbackActivity.class);
                startActivity(intent);
                break;
            case R.id.linear_my_setting_version_update:
                break;
            case R.id.linear_my_setting_clear_cache:
                new Thread(new clearCache()).start();
                break;
            case R.id.text_my_setting_sure:
                break;
        }
    }
    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"aboutUs\"}";
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
                Log.i("MyCollectionFootActivity", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, baseBean.resultNote);
                    return;
                }
                url = baseBean.getAboutUrl();
            }
        });
    }
    private Handler handler = new Handler() {
        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 0:
                    ToastUtils.showMessageLong(MySettingActivity.this,"清理完成");
                    try {
                        mCacheSize.setText(DataCleanManager.getTotalCacheSize(MySettingActivity.this));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
            }
        }
    };
    class clearCache implements Runnable {
        @Override
        public void run() {
            try {
                DataCleanManager.clearAllCache(MySettingActivity.this);
                Thread.sleep(3000);
                if (DataCleanManager.getTotalCacheSize(MySettingActivity.this).startsWith("0")) {
                    handler.sendEmptyMessage(0);
                }
            } catch (Exception e) {
                return;
            }
        }

    }
}

