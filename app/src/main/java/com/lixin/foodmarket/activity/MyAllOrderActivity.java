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
import com.lixin.foodmarket.fragment.DeliverGoodsFragment;
import com.lixin.foodmarket.fragment.MyAllOrderFragment;
import com.lixin.foodmarket.fragment.WaitEvaluateFragment;
import com.lixin.foodmarket.fragment.WaitGoodsFragment;
import com.lixin.foodmarket.fragment.WaitPaymentFragment;
import com.lixin.foodmarket.fragment.WaitRefundFragment;
import com.lixin.foodmarket.view.LazyViewPager;

import java.util.ArrayList;
import java.util.List;

public class MyAllOrderActivity extends BaseActivity implements View.OnClickListener {

    private ImageView iv_turnback;
    private TextView tv_title;
    private LazyViewPager viewPager;// 页卡内容
    private ImageView imageView,imageView0,imageView1,imageView2,imageView3,imageView4;// 动画图片
    private TextView tv_all_order, tv_wait_payment,tv_wait_pending, tv_wait_goods,tv_wait_evaluate,tv_is_complete;// 选项名称
    private List<Fragment> fragments;// Tab页面列表
    private int offset = 0;// 动画图片偏移量
    private int currIndex = 0;// 当前页卡编号
    private int bmpW;// 动画图片宽度
    private int selectedColor, unSelectedColor;//是否选择显示的颜色
    private int type;//选择的订单状态
    private static final int pageSize = 6;//页卡总数
    private int temp;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_all_order);
        hideBack(false);
        setTitleText("我的订单");
        Intent intent = getIntent();
        String result  = intent.getStringExtra("temp");
        temp = Integer.valueOf(result);
        initView();
        InitViewPager(temp);
        InitImageView(temp);
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
        imageView0 = (ImageView) findViewById(R.id.cursor0);
        imageView1 = (ImageView) findViewById(R.id.cursor1);
        imageView2 = (ImageView) findViewById(R.id.cursor2);
        imageView3 = (ImageView) findViewById(R.id.cursor3);
        imageView4 = (ImageView) findViewById(R.id.cursor4);
        viewPager = (LazyViewPager) findViewById(R.id.vPager);
        fragments = new ArrayList<Fragment>();
        fragments.add(new MyAllOrderFragment());
        fragments.add(new WaitPaymentFragment());
        fragments.add(new DeliverGoodsFragment());
        fragments.add(new WaitGoodsFragment());
        fragments.add(new WaitEvaluateFragment());
        fragments.add(new WaitRefundFragment());
        viewPager.setAdapter(new myPagerAdapter(getSupportFragmentManager(),
                fragments));
        //页面定位
        viewPager.setCurrentItem(temp);
        //初始化头标
        tv_all_order = (TextView) findViewById(R.id.tv_all_order);
        tv_wait_payment = (TextView) findViewById(R.id.tv_wait_payment);
        tv_wait_pending = (TextView) findViewById(R.id.tv_wait_pending);
        tv_wait_goods = (TextView) findViewById(R.id.tv_wait_goods);
        tv_wait_evaluate= (TextView) findViewById(R.id.tv_wait_evaluate);
        tv_is_complete= (TextView) findViewById(R.id.tv_is_complete);
        tv_all_order.setText("全部");
        tv_wait_payment.setText("待支付");
        tv_wait_pending.setText("待发货");
        tv_wait_goods.setText("待收货");
        tv_wait_evaluate.setText("待评价");
        tv_is_complete.setText("退款售后");

        //头标定位
        if (temp == 0){
            tv_all_order.setTextColor(selectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_pending.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            tv_wait_evaluate.setTextColor(unSelectedColor);
            tv_is_complete.setTextColor(unSelectedColor);
            imageView.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);

        }else if (temp == 1){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(selectedColor);
            tv_wait_pending.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            tv_wait_evaluate.setTextColor(unSelectedColor);
            tv_is_complete.setTextColor(unSelectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.VISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (temp == 2){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_pending.setTextColor(selectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            tv_wait_evaluate.setTextColor(unSelectedColor);
            tv_is_complete.setTextColor(unSelectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.VISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (temp == 3){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_pending.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(selectedColor);
            tv_wait_evaluate.setTextColor(unSelectedColor);
            tv_is_complete.setTextColor(unSelectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.VISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (temp == 4){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_pending.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            tv_wait_evaluate.setTextColor(selectedColor);
            tv_is_complete.setTextColor(unSelectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.VISIBLE);
            imageView4.setVisibility(View.INVISIBLE);
        }else if (temp == 5){
            tv_all_order.setTextColor(unSelectedColor);
            tv_wait_payment.setTextColor(unSelectedColor);
            tv_wait_pending.setTextColor(unSelectedColor);
            tv_wait_goods.setTextColor(unSelectedColor);
            tv_wait_evaluate.setTextColor(unSelectedColor);
            tv_is_complete.setTextColor(selectedColor);
            imageView.setVisibility(View.INVISIBLE);
            imageView0.setVisibility(View.INVISIBLE);
            imageView1.setVisibility(View.INVISIBLE);
            imageView2.setVisibility(View.INVISIBLE);
            imageView3.setVisibility(View.INVISIBLE);
            imageView4.setVisibility(View.VISIBLE);
        }
        tv_all_order.setOnClickListener(new MyOnClickListener(0));
        tv_wait_payment.setOnClickListener(new MyOnClickListener(1));
        tv_wait_pending.setOnClickListener(new MyOnClickListener(2));
        tv_wait_goods.setOnClickListener(new MyOnClickListener(3));
        tv_wait_evaluate.setOnClickListener(new MyOnClickListener(4));
        tv_is_complete.setOnClickListener(new MyOnClickListener(5));
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
            imageView0.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 2){
            imageView1.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 3){
            imageView2.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 4){
            imageView3.setImageMatrix(matrix);// 设置动画初始位置
        }else if (temp == 5){
            imageView4.setImageMatrix(matrix);// 设置动画初始位置
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
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 0;
                    break;
                case 1:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(selectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 1;
                    break;
                case 2:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(selectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 2;
                    break;
                case 3:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(selectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 3;
                    break;
                case 4:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(selectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type = 4;
                    break;
                case 5:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(selectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    type = 5;
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
//            Animation animation = new TranslateAnimation(one * currIndex, one
//                    * index, 0, 0);// 显然这个比较简洁，只有一行代码。
//            currIndex = index;
//            animation.setFillAfter(true);// True:图片停在动画结束位置
//            animation.setDuration(300);
            switch (index) {
                case 0:
                    tv_all_order.setTextColor(selectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type=0;
                    break;
                case 1:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(selectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.VISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type=1;
                    break;
                case 2:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(selectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.VISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type=2;
                    break;
                case 3:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(selectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.VISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type=3;
                    break;
                case 4:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(selectedColor);
                    tv_is_complete.setTextColor(unSelectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.VISIBLE);
                    imageView4.setVisibility(View.INVISIBLE);
                    type=4;
                    break;
                case 5:
                    tv_all_order.setTextColor(unSelectedColor);
                    tv_wait_payment.setTextColor(unSelectedColor);
                    tv_wait_pending.setTextColor(unSelectedColor);
                    tv_wait_goods.setTextColor(unSelectedColor);
                    tv_wait_evaluate.setTextColor(unSelectedColor);
                    tv_is_complete.setTextColor(selectedColor);
                    imageView.setVisibility(View.INVISIBLE);
                    imageView0.setVisibility(View.INVISIBLE);
                    imageView1.setVisibility(View.INVISIBLE);
                    imageView2.setVisibility(View.INVISIBLE);
                    imageView3.setVisibility(View.INVISIBLE);
                    imageView4.setVisibility(View.VISIBLE);
                    type=5;
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
