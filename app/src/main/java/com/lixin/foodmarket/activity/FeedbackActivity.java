package com.lixin.foodmarket.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.BaseBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static android.R.id.content;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class FeedbackActivity extends BaseActivity{
    private String uid;
    private EditText mTitle,mContent;
    private TextView mSure;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activtiy_feedback);
        uid = (String) SPUtils.get(FeedbackActivity.this,"uid","");
        hideBack(false);
        setTitleText("意见反馈");
        initView();
    }

    private void initView() {
        mTitle = (EditText) findViewById(R.id.edi_feedback_title);
        mContent = (EditText) findViewById(R.id.edit_feedback_dec);
        mSure = (TextView) findViewById(R.id.text_feedback_submit);
        mSure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submit();
            }
        });
    }

    private void submit() {
        String contentTitle = mTitle.getText().toString().trim();
        String content = mContent.getText().toString().trim();
        if (TextUtils.isEmpty(contentTitle)){
            ToastUtils.showMessageLong(FeedbackActivity.this,"请输入反馈内容标题");
        }else if (TextUtils.isEmpty(content)){
            ToastUtils.showMessageLong(FeedbackActivity.this,"请输入反馈内容");
        }else {
            getdata(contentTitle,content);
        }
    }

    private void getdata(String contentTitle, String content){
        Map<String, String> params = new HashMap<>();
        final String json = "{\"cmd\":\"feedbackOption\",\"uid\":\"" + uid + "\",\"contentTitle\":\"" + contentTitle + "\",\"content\":\"" +
                "" + content + "\"}";
        params.put("json", json);
        Log.i("FeedbackActivity", "response: " + json.toString());
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToastUtils.showMessageLong(context, "网络异常");
            }
            @Override
            public void onResponse(String response, int id) {
                Log.i("FeedbackActivity", "response: " + response.toString());
                Gson gson = new Gson();
                BaseBean baseBean = gson.fromJson(response, BaseBean.class);
                if (baseBean.getResult().equals("1")) {
                    ToastUtils.showMessageLong(context, baseBean.getResultNote());
                }
                ToastUtils.showMessageLong(FeedbackActivity.this,"意见反馈成功！");
                finish();
            }
        });
    }
}
