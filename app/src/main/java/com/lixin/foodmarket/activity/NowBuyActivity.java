package com.lixin.foodmarket.activity;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Switch;
import android.widget.TextView;
import com.lixin.foodmarket.R;
import com.lixin.foodmarket.adapter.NowBuyAdapter;

import java.util.Calendar;

/**
 * Created by 小火
 * Create time on  2017/5/17
 * My mailbox is 1403241630@qq.com
 */

public class NowBuyActivity extends BaseActivity{
    private TextView mInformation,mTextChooseTime,mShopNum,totalNum,totalPrice,mBalance,mSure;
    private ImageView mAddInformation,mReduce,mAdd;
    private LinearLayout mChooseTime,mCoupon;
    private EditText mLeavingMessage;
    private Switch mSwitch;
    private View headView,footView;
    private ListView now_buy_list;
    private NowBuyAdapter mAdapter;
    final int DATE_DIALOG = 1;
    int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_now_buy);
        hideBack(false);
        setTitleText("立即购买");
        initView();
    }

    private void initView() {
        now_buy_list = (ListView) findViewById(R.id.now_buy_list);
        headView = LayoutInflater.from(NowBuyActivity.this).inflate(R.layout.now_buy_head,null);
        mInformation = (TextView) headView.findViewById(R.id.tv_personal_information);
        mAddInformation = (ImageView) headView.findViewById(R.id.iv_add_information);
        mAddInformation.setOnClickListener(this);
        mChooseTime = (LinearLayout) headView.findViewById(R.id.linear_choose_time);
        mChooseTime.setOnClickListener(this);
        mTextChooseTime = (TextView) headView.findViewById(R.id.text_choose_time);
        now_buy_list.addHeaderView(headView);
        footView = LayoutInflater.from(NowBuyActivity.this).inflate(R.layout.now_buy_foot,null);
        mReduce = (ImageView) footView.findViewById(R.id.iv_buy_foot_reduce);
        mReduce.setOnClickListener(this);
        mAdd = (ImageView) footView.findViewById(R.id.iv_buy_foot_shop_add);
        mAdd.setOnClickListener(this);
        mShopNum = (TextView) footView.findViewById(R.id.tv_buy_foot_shop_num);
        mLeavingMessage = (EditText) footView.findViewById(R.id.a_now_foot_leaving_a_message);
        totalNum = (TextView) footView.findViewById(R.id.text_buy_foot_shop_num);
        totalPrice = (TextView) footView.findViewById(R.id.text_buy_foot_shop_price);
        mBalance = (TextView) footView.findViewById(R.id.text_buy_foot_balance);
        mCoupon = (LinearLayout) footView.findViewById(R.id.linear_buy_foot_coupon);
        mCoupon.setOnClickListener(this);
        mSwitch = (Switch) footView.findViewById(R.id.switch_yes_on);
        now_buy_list.addHeaderView(footView);
        mSure = (TextView) findViewById(R.id.text_now_buy_sure);
        mSure.setOnClickListener(this);
        mAdapter = new NowBuyAdapter();
        now_buy_list.setAdapter(mAdapter);
        final Calendar ca = Calendar.getInstance();
        mYear = ca.get(Calendar.YEAR);
        mMonth = ca.get(Calendar.MONTH);
        mDay = ca.get(Calendar.DAY_OF_MONTH);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_add_information:
                Intent intent = new Intent(NowBuyActivity.this,MyAddressActivity.class);
                startActivityForResult(intent,1001);
                break;
            case R.id.linear_choose_time:
                showDialog(DATE_DIALOG);
                break;
            case R.id.iv_buy_foot_shop_add:
                break;
            case R.id.iv_buy_foot_reduce:
                break;
            case R.id.linear_buy_foot_coupon:
                Intent intent1 = new Intent(NowBuyActivity.this,MyCouponActivity.class);
                startActivityForResult(intent1,1002);
                break;
            case R.id.text_now_buy_sure:
                popuWindowShow mPopuWindowShow = new popuWindowShow(NowBuyActivity.this);
                mPopuWindowShow.showAtLocation(mSure, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    public class popuWindowShow extends PopupWindow {
        private LayoutInflater mInflater1;
        private View view;
        private TextView mTvOk,mNum,mShopPrice;
        private ImageView mShopName,mClose;

        public popuWindowShow(final Activity context) {
            super(context);
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.order_dec_popupwindow, null);
            mTvOk = (TextView) view.findViewById(R.id.popupwind_ok1);
            mNum = (TextView) view.findViewById(R.id.tv_shop_num1);
            mShopPrice = (TextView) view.findViewById(R.id.text_shop_price2);
            mClose = (ImageView) view.findViewById(R.id.text_order_dec_close);
            mInflater1 = LayoutInflater.from(context);
            mTvOk.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
            // 设置SelectPicPopupWindow的View
            this.setContentView(view);
            // 设置SelectPicPopupWindow弹出窗体的宽
            this.setWidth(ViewPager.LayoutParams.MATCH_PARENT);
            // 设置SelectPicPopupWindow弹出窗体的高
            this.setHeight(ViewPager.LayoutParams.WRAP_CONTENT);
            // 在PopupWindow里面就加上下面两句代码，让键盘弹出时，不会挡住pop窗口。
            this.setInputMethodMode(PopupWindow.INPUT_METHOD_NEEDED);
            this.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            // 设置popupWindow以外可以触摸
            this.setOutsideTouchable(true);
            // 以下两个设置点击空白处时，隐藏掉pop窗口
            this.setFocusable(true);
            this.setBackgroundDrawable(new BitmapDrawable());
            // 设置popupWindow以外为半透明0-1 0为全黑,1为全白
            backgroundAlpha(0.3f);
            // 添加pop窗口关闭事件
            this.setOnDismissListener(new poponDismissListener());
            // 设置动画--这里按需求设置成系统输入法动画
            this.setAnimationStyle(R.style.AnimBottom);
            // mMenuView添加OnTouchListener监听判断获取触屏位置如果在选择框外面则销毁弹出框
            view.setOnTouchListener(new View.OnTouchListener() {
                public boolean onTouch(View v, MotionEvent event) {
                    int height = view.findViewById(R.id.pop_layout)
                            .getTop();
                    int y = (int) event.getY();
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        if (y < height) {
                            dismiss();
                        }
                    }
                    return true;
                }
            });
            mClose.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dismiss();
                }
            });
        }
    }

    public void backgroundAlpha(float bgAlpha) {
        WindowManager.LayoutParams lp = this.getWindow().getAttributes();
        lp.alpha = bgAlpha;
        this.getWindow().setAttributes(lp);
    }
    class poponDismissListener implements PopupWindow.OnDismissListener {

        @Override
        public void onDismiss() {
            backgroundAlpha(1f);
        }
    }
    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case DATE_DIALOG:
                return new DatePickerDialog(this, mdateListener, mYear, mMonth, mDay);
        }
        return null;
    }

    public void display() {
        mTextChooseTime.setText(new StringBuffer().append(mMonth + 1).append("-").append(mDay).append("-").append(mYear).append(" "));
    }

    private DatePickerDialog.OnDateSetListener mdateListener = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            mYear = year;
            mMonth = monthOfYear;
            mDay = dayOfMonth;
            display();
        }
    };
}
