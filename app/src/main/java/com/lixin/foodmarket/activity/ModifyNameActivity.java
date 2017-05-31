package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.utils.SPUtils;
import com.lixin.foodmarket.utils.ToastUtils;

/**
 * Created by 小火
 * Create time on  2017/5/19
 * My mailbox is 1403241630@qq.com
 */

public class ModifyNameActivity extends BaseActivity{
    private EditText et_myname;
    private Button btn_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_name);
        hideBack(false);
        setTitleText("修改昵称");
        initView();//初始化控件
        initData();//初始化数据
        initListener();//初始化点击事件
    }
    /**
     * 初始化控件
     */
    private void initView() {
        et_myname = (EditText) findViewById(R.id.et_myname);
        btn_confirm = (Button) findViewById(R.id.btn_confirm);

    }
    /**
     * 初始化数据
     */
    private void initData() {
        String nickName = (String) SPUtils.get(context,"nickName","");
        et_myname.setHint(nickName);
    }

    /**
     * 初始化点击事件
     */
    private void initListener() {
        btn_confirm.setOnClickListener(this);
    }
    /**
     * 点击事件处理
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_confirm:
                submit();
                break;
        }
    }
    /**
     * 提交信息验证
     */
    private void submit() {
        String myname = et_myname.getText().toString().trim();
        if (TextUtils.isEmpty(myname)) {
            ToastUtils.showMessageShort(context,"昵称不能为空");
            return;
        }
        ToastUtils.showMessageShort(context,"修改成功！");
        SPUtils.put(context,"nickName",myname);
        Intent intent=new Intent();
        intent.putExtra("result", myname);
        setResult(3002,intent);
        finish();
    }
}
