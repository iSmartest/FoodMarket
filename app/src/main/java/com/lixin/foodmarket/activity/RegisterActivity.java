package com.lixin.foodmarket.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.UserRegisterBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.StringUtils;
import com.lixin.foodmarket.utils.TimerUtil;
import com.lixin.foodmarket.utils.ToastUtils;
import com.sina.weibo.sdk.utils.LogUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class RegisterActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "ForgetPwdActivity";
    private EditText edi_phone_number,edi_verification_code,edi_password,edi_password_again;
    private TextView activity_base_textview_back;
    private Button btn_fast_register,btn_verification_code;
    private Context mContext;
    private String code;
    private String logpwd;
    private String addressId;//没有
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fast_register);
        mContext = this;
        initView();
    }

    private void initView() {
        edi_phone_number = (EditText) findViewById(R.id.edi_phone_number);
        edi_verification_code = (EditText) findViewById(R.id.edi_verification_code);
        edi_password = (EditText) findViewById(R.id.edi_password);
        edi_password_again = (EditText) findViewById(R.id.edi_password_again);
        btn_verification_code = (Button) findViewById(R.id.btn_verification_code);
        activity_base_textview_back = (TextView) findViewById(R.id.activity_base_textview_back);
        btn_fast_register = (Button) findViewById(R.id.btn_fast_register);

        edi_phone_number.setOnClickListener(this);
        edi_verification_code.setOnClickListener(this);
        edi_password.setOnClickListener(this);
        edi_password_again.setOnClickListener(this);
        btn_verification_code.setOnClickListener(this);
        btn_fast_register.setOnClickListener(this);
        activity_base_textview_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_base_textview_back:
                finish();
                break;
            case R.id.btn_verification_code:
                String user_phone_number = edi_phone_number.getText().toString().trim();
                //验证手机号是否正确
                if (!StringUtils.isMatchesPhone(user_phone_number)){
                    ToastUtils.showMessageShort(mContext,"你输入的手机号格式不正确");
                    return;
                }
                //验证电话号码不能为空
                if (TextUtils.isEmpty(user_phone_number)){
                    ToastUtils.showMessageShort(mContext,"请输入手机号！");
                    return;
                }
                code = TimerUtil.getNum();
                LogUtil.i("code","---------" + code);
                sendSMS(user_phone_number,code);
                TimerUtil mTimerUtil = new TimerUtil(btn_verification_code);
                mTimerUtil.timers();
                break;
            case R.id.btn_fast_register:
                submit();
                break;

        }
    }

    private void submit() {
        //验证密码不能为空
        String password = edi_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showMessageShort(mContext, "密码不能为空");
            return;
        }
        //验证确认密码不能为空
        String confirmpwd = edi_password_again.getText().toString().trim();
        if (TextUtils.isEmpty(confirmpwd)) {
            ToastUtils.showMessageShort(mContext, "确认密码不能为空");
            return;
        }
        //验证密码和确认密码是否相同
        if (!password.equals(confirmpwd)) {
            ToastUtils.showMessageShort(mContext, "两次输入密码不一致");
            return;
        }
        //验证密码格式是否正确
//        String inviteCode = edi_verification_code.getText().toString().trim();
//        if (TextUtils.isEmpty(inviteCode)){
//            ToastUtils.showMessageShort(mContext, "验证码不能为空");
//        }
        if (!StringUtils.isPwd(password)) {
            ToastUtils.showMessageShort(mContext, "密码格式不正确，请核对后重新输入");
            return;
        }
        String userphone = edi_phone_number.getText().toString().trim();
        logpwd = password;
        userRegister(userphone, logpwd);
    }
    /**
     * 用户注册
     *
     * @param userPhone
     * @param password
     * @param
     */
    private void userRegister(final String userPhone, final String password) {
        Map<String, String> params = new HashMap<>();
        final String json="{\"cmd\":\"userRegister\",\"userPhone\":\"" + userPhone + "\"," +
                "\"password\":\"" + password +"\"}";
        params.put("json", json);
        Log.i("6666", "userRegister: " + json);
        //车品商城服务端
        OkHttpUtils.post().url(mContext.getString(R.string.url)).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showMessageShort(mContext, e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Log.i("6666", "onResponse: " + response);
                        Gson gson = new Gson();
                        UserRegisterBean bean = gson.fromJson(response, UserRegisterBean.class);
                        if ("0".equals(bean.result)) {
                            ToastUtils.showMessageShort(mContext, "注册成功");
                            SPUtils.put(RegisterActivity.this,"uid",bean.getUid());
                            SPUtils.put(RegisterActivity.this,"userPhone",userPhone);
                            SPUtils.put(RegisterActivity.this,"password",password);
                            Bundle bundle = new Bundle();
                            bundle.putString("phone", userPhone);
                            bundle.putString("password", logpwd);
                            MyApplication.openActivity(mContext, LoginActivity.class, bundle);
                            finish();
                        } else {
                            ToastUtils.showMessageShort(mContext,"该号码已存在");
                        }
                    }
                });
    }
    /**
     * 获取短信验证码
     * @param phone
     */
    public void sendSMS(String phone, String CODE) {
        OkHttpUtils.post().url("https://v.juhe.cn/sms/send?").addParams("mobile", phone).addParams("tpl_id", "32726").addParams("tpl_value", "%23code%23%3d" + CODE).addParams("key", "3b5b27d9ca33e3873994ecab8d1ddc77").build().execute(new StringCallback() {
            @Override
            public void onResponse(String response, int id) {
                try {
                    JSONObject obj = new JSONObject(response);
                    if (obj.getString("error_code").equals("0")) {
                        ToastUtils.showMessageShort(RegisterActivity.this,"短信已发送，请注意查收");
                    } else {
                        ToastUtils.showMessageShort(RegisterActivity.this,obj.getString("reason"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            @Override
            public void onError(Call call, Exception e, int id) {

            }
        });

    }
}

