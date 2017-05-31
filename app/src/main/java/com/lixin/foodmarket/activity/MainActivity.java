package com.lixin.foodmarket.activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.dialog.TipsDialog;
import com.lixin.foodmarket.fragment.ClassFragment;
import com.lixin.foodmarket.fragment.HomeFragment;
import com.lixin.foodmarket.fragment.MineFragment;
import com.lixin.foodmarket.fragment.ShopCartFragment;

public class MainActivity extends BaseActivity implements HomeFragment.CallBackValue{
    private String[] titles;
    private LinearLayout[] mTextView;
    private Fragment[] mFragments;
    private FragmentTransaction transaction;
    private int current = 0;
    private String temp;
    private boolean isSkip = true;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_new_main_layout_content, new HomeFragment())
                    .commit();
        }
        initTitle();
        initView();
        initFragment();
        refreshView();
    }

    private void initTitle() {
        titles = new String[4];
        titles[0] = "";
        titles[1] = "分类";
        titles[2] = "购物车";
        titles[3] = "我的";
    }
    private void initView() {
        mTextView = new LinearLayout[4];
        mTextView[0] = (LinearLayout) findViewById(R.id.iv_main_home);
        mTextView[1] = (LinearLayout) findViewById(R.id.iv_main_store);
        mTextView[2] = (LinearLayout) findViewById(R.id.iv_main_forum);
        mTextView[3] = (LinearLayout) findViewById(R.id.iv_main_mine);
    }
    private void initFragment() {
        mFragments = new Fragment[4];
        mFragments[0] = new HomeFragment();
        mFragments[1] = new ClassFragment();
        mFragments[2] = new ShopCartFragment();
        mFragments[3] = new MineFragment();
        setCurrent(0);
    }
    private void refreshView() {
        for (int i = 0; i < mTextView.length; i++) {
            mTextView[i].setId(i);
            mTextView[i].setOnClickListener(this);
        }
    }
    private void setCurrent(int position) {
        transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.activity_new_main_layout_content, mFragments[position]);
        transaction.commitAllowingStateLoss();
        mTextView[position].setSelected(true);
        for (int i = 0; i < mTextView.length; i++) {
            if (i != position) {
                mTextView[i].setSelected(false);
            }
        }
        if (position == 0) {
            RelativeLayout lay_bg = (RelativeLayout) findViewById(R.id.lay_bg);
            setTitleText(titles[position]);
            lay_bg.setVisibility(View.GONE);
        } else if (position == 3) {
            RelativeLayout lay_bg = (RelativeLayout) findViewById(R.id.lay_bg);
            setTitleText(titles[position]);
            lay_bg.setVisibility(View.GONE);
        } else {
            hideBack(true);
            ImageView Iv_base_back = (ImageView) findViewById(R.id.Iv_base_back);
            Iv_base_back.setVisibility(View.GONE);
            setTitleText(titles[position]);
        }
        current = position;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case 0:
                setCurrent(0);
                break;
            case 1:
                setCurrent(1);
                break;
            case 2:
                setCurrent(2);
                break;
            case 3:
                setCurrent(3);
                break;
            default:
                break;
        }
    }
    @Override
    public void SendMessageValue(String strValue) {
        temp = strValue;
        if (temp.equals("6")){
            mTextView[1].setSelected(true);
            for (int i = 0; i < mTextView.length; i++) {
                if (i != 1) {
                    mTextView[i].setSelected(false);
                }
            }
            hideBack(true);
            ImageView Iv_base_back = (ImageView) findViewById(R.id.Iv_base_back);
            Iv_base_back.setVisibility(View.GONE);
            setTitleText(titles[1]);
            current = 1;
        }
    }
    private TipsDialog dialog;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (isSkip) {
                if (dialog == null)
                    dialog = new TipsDialog(MainActivity.this, R.string.ok_to_exit_it, new TipsDialog.OnSureBtnClickListener() {
                        @Override
                        public void sure() {
                            dialog.dismiss();
                            finish();
                            MyApplication.getApplication().exit();
                        }
                    });
                dialog.show();
            } else {
                setResult(RESULT_CANCELED);
                finish();
            }
        }
        return super.onKeyDown(keyCode, event);
    }
}