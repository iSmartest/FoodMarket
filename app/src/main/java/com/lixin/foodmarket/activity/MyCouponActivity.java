package com.lixin.foodmarket.activity;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.fragment.ExpiredFragment;
import com.lixin.foodmarket.fragment.UsedFragment;
import com.lixin.foodmarket.view.LazyViewPager;
import com.lixin.foodmarket.fragment.NotUsedFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 小火
 * Create time on  2017/5/18
 * My mailbox is 1403241630@qq.com
 */

public class MyCouponActivity extends BaseActivity{
    private LazyViewPager viewPager;// 页卡内容
    private ImageView imageView,imageView1,imageView2;// 动画图片
    private TextView tv_all_order, tv_wait_payment, tv_wait_goods;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int bmpW;// 动画图片宽度
    private int selectedColor, unSelectedColor;//是否选择显示的颜色
    private static final int pageSize = 3;//页卡总数
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_coupon);
        hideBack(false);
        setTitleText("我的优惠券");
        initView();
        InitViewPager(0);
        InitImageView(0);
    }

    private void initView() {
        selectedColor = getResources().getColor(R.color.btn_login_color);
        unSelectedColor = getResources().getColor(R.color.black);
    }
    /**
     * 初始化Viewpager页
     */
    private void InitViewPager(int temp) {
        imageView = (ImageView) findViewById(R.id.cursor);
        imageView1 = (ImageView) findViewById(R.id.cursor1);
        imageView2 = (ImageView) findViewById(R.id.cursor2);
        viewPager = (LazyViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new NotUsedFragment());
        fragments.add(new UsedFragment());
        fragments.add(new ExpiredFragment());
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
                fragments));
        //页面定位
        viewPager.setCurrentItem(temp);
        //初始化头标
        tv_all_order = (TextView) findViewById(R.id.tv_my_coupon_not_used);
        tv_wait_payment = (TextView) findViewById(R.id.tv_my_coupon_used);
        tv_wait_goods = (TextView) findViewById(R.id.tv_is_expired);
        tv_all_order.setText("未使用");
        tv_wait_payment.setText("已使用");
        tv_wait_goods.setText("已失效");

        //头标定位
        if (temp == 0){
            tv_all_order.setTextColor(selectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
        }else if (temp == 1){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(selectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
        }else if (temp == 2){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(selectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
        }
        tv_all_order.setOnClickListener(new MyOnClickListener(0));
        tv_wait_payment.setOnClickListener(new MyOnClickListener(1));
        tv_wait_goods.setOnClickListener(new MyOnClickListener(2));
        viewPager.setOnPageChangeListener(new MyOnPageChangeListener());
    }

    /**
     * 初始化动画，这个就是页卡滑动时，下面的横线也滑动的效果，在这里需要计算一些数据
     */
    private void InitImageView(int temp) {
        bmpW = BitmapFactory.decodeResource(getResources(),
                R.drawable.tab_selected_bg).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / pageSize - bmpW) / 2;// 计算偏移量--(屏幕宽度/页卡总数-图片实际宽度)/2
        // = 偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        if (temp == 0){
            imageView.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 1){
            imageView1.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 2){
            imageView2.setImageMatrix(matrix);// 设置动画初始位置
        }
    }

    /**
     * 头标点击监听
     */
    private class MyOnClickListener implements View.OnClickListener {
        private int index = 0;
        public MyOnClickListener(int i) {
            index = i;
        }
        public void onClick(View v) {
            switch (index) {
                case 0:
                    tv_all_order.setTextColor(selectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(selectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(selectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    break;
            }
            viewPager.setCurrentItem(index);
        }

    }

    /**
     * 为选项卡绑定监听器
     */
    public class MyOnPageChangeListener implements LazyViewPager.OnPageChangeListener {

        int one = offset * 2 + bmpW;// 页卡1 -> 页卡2 偏移量
        int two = one * 2;// 页卡1 -> 页卡3 偏移量
        int three = one * 3;// 页卡1 -> 页卡4 偏移量


        public void onPageScrollStateChanged(int index) {
        }

        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        public void onPageSelected(int index) {
            switch (index) {
                case 0:
                    tv_all_order.setTextColor(selectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    break;
                case 1:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(selectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    break;
                case 2:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(selectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    break;
            }
        }
    }
    /**
     * 定义适配器
     */
    class myPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragmentList;

        public myPagerAdapter(FragmentManager fm, List<Fragment> fragmentList) {
            super(fm);
            this.fragmentList = fragmentList;
        }
        /**
         * 得到每个页面
         */
        @Override
        public Fragment getItem(int arg0) {
            return (fragmentList == null || fragmentList.size() == 0) ? null
                    : fragmentList.get(arg0);
        }
        /**
         * 每个页面的title
         */
        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        /**
         * 页面的总个数
         */
        @Override
        public int getCount() {
            return fragmentList == null ? 0 : fragmentList.size();
        }
    }
}

