package com.lixin.foodmarket.activity;

import android.app.Dialog;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.lixin.foodmarket.R;
import com.lixin.foodmarket.dialog.ProgressDialog;

/**
 * Created by 小火
 * Create time on  2017/5/15
 * My mailbox is 1403241630@qq.com
 */

public class BaseActivity extends FragmentActivity implements View.OnClickListener {
    private boolean override;
    protected Context context;
    protected Dialog dialog1;
    private static Dialog progressDlg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //页面启动 记录日志
        setContentView(R.layout.activity_base);
        context = this;
        dialog1 = ProgressDialog.createLoadingDialog(context, "加载中.....");
    }

    public void hideBack(boolean show) {
        ImageView Iv_base_back = (ImageView) findViewById(R.id.Iv_base_back);
        RelativeLayout lay_bg = (RelativeLayout) findViewById(R.id.lay_bg);
        if (show){
            Iv_base_back.setVisibility(View.GONE);
            lay_bg.setVisibility(View.VISIBLE);
        }else{
            Iv_base_back.setVisibility(View.VISIBLE);
            lay_bg.setVisibility(View.VISIBLE);
            Iv_base_back.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(BaseActivity.this, "点击了后退", Toast.LENGTH_SHORT).show();
                    finish();
                }
            });
        }
    }

    @Override
    public void onClick(View v) {

    }
    protected void overrideOnKeyDown(boolean override) {
        this.override = override;
    }
    protected void back() {
        finish();
    }
//    @Override
//    public void startActivity(Intent intent) {
//        super.startActivity(intent);
//        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//    }
//
//    @Override
//    public void startActivityForResult(Intent intent, int requestCode) {
//        super.startActivityForResult(intent, requestCode);
//        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//    }
//
//    @Override
//    public void startActivityFromChild(Activity child, Intent intent,
//                                       int requestCode) {
//        super.startActivityFromChild(child, intent, requestCode);
//        overridePendingTransition(R.anim.alpha_in, R.anim.alpha_out);
//    }
//    private TipsDialog dialog;
//
//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (override)
//            if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
//                if (dialog == null) {
//                    dialog = new TipsDialog(this, R.string.ok_to_exit_it, new TipsDialog.OnSureBtnClickListener() {
//                        @Override
//                        public void sure() {
//                            dialog.dismiss();
//                            finish();
//                            MyApplication.getApplication().exit();
//                        }
//                    });
//                }
//                dialog.show();
//            }
//        return super.onKeyDown(keyCode, event);
//    }
    public void setTitleText(String string) {
        TextView titleTv = (TextView) findViewById(R.id.tv_base_titleText);
        titleTv.setText(string);
    }
    /**
     * 关闭进度对话框
     */
    public void dismissProgressDialog() {
        if (progressDlg != null && progressDlg.isShowing()) {
            progressDlg.dismiss();
        }
    }
}
