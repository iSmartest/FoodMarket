package com.lixin.foodmarket.activity;

import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.ClassListBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class MyMessageActivity extends BaseActivity{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_message);
        hideBack(false);
        setTitleText("消息");
        getdata();
    }

    private void getdata() {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"getMineMessageInfo\"}";
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
                Log.i("MyMessageActivity", "onResponse: " + response.toString());
                Gson gson = new Gson();
                dialog1.dismiss();
                ClassListBean classListBean = gson.fromJson(response, ClassListBean.class);
                if (classListBean.result.equals("1")) {
                    ToastUtils.showMessageShort(context, classListBean.resultNote);
                    return;
                }
            }
        });
    }
}
