package com.lixin.foodmarket.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.VerificationCodeBean;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.Md5Util;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.StringUtils;
import com.lixin.foodmarket.utils.TimerUtil;
import com.lixin.foodmarket.utils.ToastUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

import static com.umeng.socialize.Config.dialog;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class ForgetPwdActivity extends Activity implements View.OnClickListener{
    private static final String TAG = "ForgetPwdActivity";
    private EditText edi_phone_number,edi_verification_code,edi_password,edi_password_again;
    private TextView activity_base_textview_back;
    private Button btn_confirm_modification,btn_verification_code;
    private CheckBox iv_check;
    private Context mContext;
    private String code;
    private String token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgrt_password);
        mContext = this;
        TelephonyManager mTelephonyManager = (TelephonyManager)mContext.getSystemService(TELEPHONY_SERVICE);
        token = mTelephonyManager.getDeviceId();
        initView();
    }
    private void initView() {

        edi_phone_number = (EditText) findViewById(R.id.edi_phone_number);
        edi_verification_code = (EditText) findViewById(R.id.edi_verification_code);
        edi_password = (EditText) findViewById(R.id.edi_password);
        edi_password_again = (EditText) findViewById(R.id.edi_password_again);
        btn_verification_code = (Button) findViewById(R.id.btn_verification_code);
        activity_base_textview_back = (TextView) findViewById(R.id.activity_base_textview_back);
        iv_check = (CheckBox) findViewById(R.id.iv_check);
        iv_check.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                // TODO Auto-generated method stub
                if(isChecked){
                    //如果选中，显示密码
                    //点击使密码显示出来
                    edi_password.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    edi_password_again.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }else{
                    //否则隐藏密码
                    edi_password.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    edi_password_again.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            }
        });
        btn_confirm_modification = (Button) findViewById(R.id.btn_confirm_modification);

        edi_phone_number.setOnClickListener(this);
        edi_verification_code.setOnClickListener(this);
        edi_password.setOnClickListener(this);
        edi_password_again.setOnClickListener(this);
        btn_verification_code.setOnClickListener(this);

        btn_confirm_modification.setOnClickListener(this);
        activity_base_textview_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.activity_base_textview_back:
                finish();
                break;
            case R.id.btn_verification_code:
                //验证手机号不能为空
                String userphone = edi_phone_number.getText().toString().trim();
                if (TextUtils.isEmpty(userphone)) {
                    ToastUtils.showMessageShort(mContext, "电话号码不能为空");
                    return;
                }
                //验证电话号码是否正确
                if (!StringUtils.isMatchesPhone(userphone)) {
                    ToastUtils.showMessageShort(mContext, "电话号码不正确，请核对后重新输入");
                    return;
                }
                code = TimerUtil.getNum();
                getPin(userphone);
                TimerUtil timerUtil = new TimerUtil(btn_verification_code);
                timerUtil.timers();
                break;
            case R.id.btn_confirm_modification:
                submit();
                break;
        }
    }

    private void submit() {
        //验证验证码不能为空
        String passPin = edi_verification_code.getText().toString().trim();
        if (TextUtils.isEmpty(passPin)) {
            ToastUtils.showMessageShort(mContext, "验证码不能为空");
            return;
        }
        //验证验证码是否正确
        if (!passPin.equals(code)) {
            ToastUtils.showMessageShort(mContext, "验证码不正确");
            return;
        }
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
        if (!StringUtils.isPwd(password)) {
            ToastUtils.showMessageShort(mContext, "密码格式不正确，请核对后重新输入");
            return;
        }
        //验证电话号码不能为空
        String userphone = edi_phone_number.getText().toString().trim();
        try {
            findPassword(userphone, Md5Util.md5Encode(password));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取短信验证码
     * @param phone
     */
    private void getPin(String phone) {
        Map<String, String> params = new HashMap<>();
        try {
            params.put("tpl_value", URLEncoder.encode("#code#=" + code, "utf-8"));
            params.put("dtype", "json");
            params.put("tpl_id", "28078");
            params.put("key", "c6bc033aec60a1073b6950471592618f");
            params.put("mobile", phone);
            Log.e("tiramisu",params.toString());
            dialog.show();
            //聚合验证码
            OkHttpUtils.post().url(getString(R.string.juhe_url)).params(params).build()
                    .execute(new StringCallback() {
                        @Override
                        public void onError(Call call, Exception e, int id) {
                            ToastUtils.showMessageShort(mContext, e.getMessage());
                            dialog.dismiss();
                        }

                        @Override
                        public void onResponse(String response, int id) {
                            Gson gson = new Gson();
                            VerificationCodeBean Vbean = gson.fromJson(response, VerificationCodeBean.class);
                            if ("操作成功".equals(Vbean.reason)) {
                                ToastUtils.showMessageShort(mContext, "验证码已发送");
                                TimerUtil timerUtil = new TimerUtil(btn_verification_code);
                                timerUtil.timers();
                                dialog.dismiss();
                            } else {
                                ToastUtils.showMessageShort(mContext, "验证码发送失败");
                                dialog.dismiss();
                            }
                        }
                    });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    /**
     * 忘记密码
     * @param phone
     * @param password
     */
    private void findPassword(final String phone, final String password) {
        Map<String, String> params = new HashMap<>();
       /* params.put("cmd", "findPassword");
        params.put("phone", phone);
        params.put("password", password);*/
        String json="{\"cmd\":\"forgetPassword\",\"phoneNum\":\"" + phone + "\"," +
                "\"password\":\"" + password +"\",\"inviteCode\":\"" + token + "\"}";
        params.put("json", json);
        dialog.show();
        OkHttpUtils.post().url(getString(R.string.url)).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showMessageShort(mContext, e.getMessage());
                        dialog.dismiss();
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        try {
                            JSONObject object=new JSONObject(response);
                            String result = object.getString("result");
                            String resultNote = object.getString("resultNote");
                            if ("0".equals(result)){
                                ToastUtils.showMessageShort(mContext,"找回密码成功");
                                SPUtils.put(mContext,"phoneNum",phone);
                                SPUtils.put(mContext,"password",password);
                                finish();
                                dialog.dismiss();
                            }else {
                                ToastUtils.showMessageShort(mContext, resultNote);
                                dialog.dismiss();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                });

    }
}

