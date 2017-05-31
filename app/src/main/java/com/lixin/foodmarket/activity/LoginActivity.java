package com.lixin.foodmarket.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.bean.UserLoginBean;
import com.lixin.foodmarket.dialog.TipsDialog;
import com.lixin.foodmarket.http.StringCallback;
import com.lixin.foodmarket.utils.OkHttpUtils;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.StringUtils;
import com.lixin.foodmarket.utils.ToastUtils;
import com.lixin.foodmarket.dialog.ProgressDialog;
import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by 小火
 * Create time on  2017/5/22
 * My mailbox is 1403241630@qq.com
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {

    private static final String TAG = "LoginActivity";
    private ImageView user_avatar, iv_qqlogin, iv_weixinlogin;
    private EditText et_userphone,et_password;
    private TextView tv_forgetPassword,tv_register;
    private Button btn_login;
    private Dialog progressDlg;
    protected Context context;
    protected Dialog dialog1;
    private UMShareAPI mShareAPI;
    private boolean isSkip = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mShareAPI = UMShareAPI.get(this);
        context = this;
        dialog1 = ProgressDialog.createLoadingDialog(context, "加载中.....");
        initView();
        initData();
        initListener();
        isSkip = false;
        Log.i(TAG, "onCreate: " + SPUtils.get(context,"uid",""));
    }

    private void initView() {
        user_avatar = (ImageView) findViewById(R.id.user_avatar);
        iv_qqlogin = (ImageView) findViewById(R.id.iv_qqlogin);
        iv_weixinlogin = (ImageView) findViewById(R.id.iv_weixinlogin);
        et_userphone = (EditText) findViewById(R.id.et_userphone);
        et_password = (EditText) findViewById(R.id.et_password);
        tv_forgetPassword = (TextView) findViewById(R.id.tv_forgetPassword);
        tv_register = (TextView) findViewById(R.id.tv_register);
        btn_login = (Button) findViewById(R.id.btn_login);
    }
    private void initData() {
        Intent intent = getIntent();
        if (intent == null) {
            return;
        }
        String phone = intent.getStringExtra("phone");
        String password = intent.getStringExtra("password");
        et_userphone.setText(phone);
        et_password.setText(password);
    }

    private void initListener() {
        tv_forgetPassword.setOnClickListener(this);
        tv_register.setOnClickListener(this);
        tv_forgetPassword.setOnClickListener(this);
        btn_login.setOnClickListener(this);
        iv_qqlogin.setOnClickListener(this);
        iv_weixinlogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                submit();
                break;
            case R.id.iv_qqlogin://QQ登录
                progressDlg = ProgressDialog.createLoadingDialog(context, "登录跳转中...");
                progressDlg.show();
                ToastUtils.showMessageShort(context, "正在跳转QQ登录,请稍后...");
                mShareAPI.isInstall(this, SHARE_MEDIA.QQ);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.QQ, umAuthListener);
                break;
            case R.id.iv_weixinlogin://微信登录
                if (!isWeixinAvilible(this)) {
                    ToastUtils.showMessageShort(this, "请安装微信客户端");
                    return;
                }
                progressDlg = ProgressDialog.createLoadingDialog(context, "登录跳转中...");
                progressDlg.show();
                ToastUtils.showMessageShort(this, "正在跳转微信登录,请稍后...");
                mShareAPI.isInstall(this, SHARE_MEDIA.WEIXIN);
                mShareAPI.getPlatformInfo(LoginActivity.this, SHARE_MEDIA.WEIXIN, umAuthListener);
                break;
            case R.id.tv_forgetPassword:
                MyApplication.openActivity(context, ForgetPwdActivity.class);
                break;
            case R.id.tv_register:
                MyApplication.openActivity(context, RegisterActivity.class);
                break;
        }
    }

    private void submit() {
        //验证电话号码是否正确
        String userphone = et_userphone.getText().toString().trim();//电话号码
        if (TextUtils.isEmpty(userphone)) {
            ToastUtils.showMessageShort(context, "电话号码不能为空");
            return;
        }

        if (!StringUtils.isMatchesPhone(userphone)) {
            ToastUtils.showMessageShort(context, "电话号码不正确，请核对后重新输入");
            return;
        }
        //验证密码是否为空
        String password = et_password.getText().toString().trim();//密码
        if (TextUtils.isEmpty(password)) {
            ToastUtils.showMessageShort(context, "密码不能为空");
            return;
        }
        //验证密码格式是否正确
        if (!StringUtils.isPwd(password)) {
            ToastUtils.showMessageShort(context, "密码格式不正确，请核对后重新输入");
            return;
        }
        userLogin(userphone, password);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mShareAPI.onActivityResult(requestCode, resultCode, data);
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.dismiss();
        }
    }

    private UMAuthListener umAuthListener = new UMAuthListener() {
        @Override
        public void onComplete(SHARE_MEDIA share_media, int i, Map<String, String> map) {
            String screen_name = null, profile_image_url = null, openid = null,phoneNum = null;
            if (SHARE_MEDIA.QQ.equals(share_media)) {
                screen_name = map.get("screen_name");//昵称
                profile_image_url = map.get("profile_image_url");//头像
                openid = map.get("openid");//第三方平台id
            } else if (SHARE_MEDIA.WEIXIN.equals(share_media)) {
                screen_name = map.get("screen_name");//昵称
                profile_image_url = map.get("profile_image_url");//头像
                openid = map.get("openid");//第三方平台id
                phoneNum = map.get("phoneNum");
            }
            thirdLogin(openid, screen_name, profile_image_url,phoneNum);
        }

        @Override
        public void onError(SHARE_MEDIA share_media, int i, Throwable throwable) {

        }

        @Override
        public void onCancel(SHARE_MEDIA share_media, int i) {

        }
    };

    /**
     * 判断 用户是否安装微信客户端
     */
    public static boolean isWeixinAvilible(Context context) {
        final PackageManager packageManager = context.getPackageManager();// 获取packagemanager
        List<PackageInfo> pinfo = packageManager.getInstalledPackages(0);// 获取所有已安装程序的包信息
        if (pinfo != null) {
            for (int i = 0; i < pinfo.size(); i++) {
                String pn = pinfo.get(i).packageName;
                if (pn.equals("com.tencent.mm")) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * 用户登录
     * @param phone
     * @param password
     */
    private void userLogin(String phone, String password) {
        Map<String, String> params = new HashMap<>();
        String json="{\"cmd\":\"userLogin\",\"phone\":\"" + phone + "\",\"password\":\""
                + password + "\"}";
        params.put("json", json);
        dialog1.show();
        OkHttpUtils.post().url(context.getString(R.string.url)).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showMessageShort(context, e.getMessage());
                    }

                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        UserLoginBean bean = gson.fromJson(response, UserLoginBean.class);
                        if ("0".equals(bean.result)) {
                            ToastUtils.showMessageShort(context, "登录成功");
                            SPUtils.put(context, "uid", bean.uid);
                            MyApplication.openActivity(context, MainActivity.class);
                            finish();
                            dialog1.dismiss();
                        } else {
                            ToastUtils.showMessageShort(context, bean.resultNote);
                            dialog1.dismiss();
                            if (isSkip) {
                                skip();
                            } else {
                                setResult(RESULT_OK);
                                finish();
                            }
                        }
                    }
                });
    }
    private void skip() {
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }
    /**
     * 第三方登录
     * @param thirdUid
     * @param nickName
     * @param userIcon
     */
    private void thirdLogin(String thirdUid, final String nickName, final String userIcon, String phoneNum) {
        Map<String, String> params = new HashMap<>();
        String json="{\"cmd\":\"thirdLogin\",\"thirdUid\":\"" + thirdUid + "\",\"nickName\":\""
                + nickName + "\",\"userIcon\":\"" + userIcon + "\"}";
        params.put("json", json);
        Log.i("thirdLogin", "thirdLogin: " + json);
        OkHttpUtils.post().url(getString(R.string.url)).params(params).build()
                .execute(new StringCallback() {
                    @Override
                    public void onError(Call call, Exception e, int id) {
                        ToastUtils.showMessageShort(context, e.getMessage());
                    }
                    @Override
                    public void onResponse(String response, int id) {
                        Gson gson = new Gson();
                        UserLoginBean bean = gson.fromJson(response, UserLoginBean.class);
                        Log.i("thirdLogin", "onResponse: "+ response.toString());
                        if ("0".equals(bean.result)) {
                            SPUtils.put(context, "uid", bean.uid);
//                            SPUtils.put(context, "isFirst", bean.isFirst);
                            SPUtils.put(context,"userIcon",userIcon);
                            SPUtils.put(context,"nickName",nickName);
                            ToastUtils.showMessageShort(context, "登录成功");
                            Intent intent = new Intent(context ,MainActivity.class);
                            intent.putExtra("grxx",1);
                            startActivity(intent);
                            finish();
                            finish();
                        } else {
                            ToastUtils.showMessageShort(context, bean.resultNote);
                        }
                    }
                });
    }
}



