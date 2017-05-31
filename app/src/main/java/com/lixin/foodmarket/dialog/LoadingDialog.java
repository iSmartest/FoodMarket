package com.lixin.foodmarket.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver.OnPreDrawListener;
import android.view.Window;
import android.widget.ImageView;

import com.lixin.foodmarket.R;

/**
 * 上传照片对话框
 */
public class LoadingDialog extends Dialog {
    private AnimationDrawable rocketAnimation;
//    private TextView textView;

    public LoadingDialog(Context context) {
        super(context, R.style.LoadingDialogStyle);
        init(context);
    }

    private void init(Context context) {
        final View v = LayoutInflater.from(context).inflate(R.layout.dialog_loading, null);
        ImageView imageView = (ImageView) v.findViewById(R.id.dialog_loading_iv_img);
        rocketAnimation = (AnimationDrawable) imageView.getBackground();
//        textView = (TextView) v.findViewById(R.id.dialog_loading_tv_text);

        imageView.getViewTreeObserver().addOnPreDrawListener(preDrawListener);

        setContentView(v);
    }

    @Override
    public void show() {
        Window window = getWindow(); // 得到对话框
        window.setBackgroundDrawableResource(android.R.color.transparent); // 设置对话框背景为透明

        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        if (rocketAnimation != null) {
            rocketAnimation.stop();
            rocketAnimation = null;
        }
    }

    private OnPreDrawListener preDrawListener = new OnPreDrawListener() {
        @Override
        public boolean onPreDraw() {
            if (rocketAnimation != null)
                rocketAnimation.start();
            return true; // 必须要有这个true返回
        }
    };

//    public TextView getTextView()
//    {
//        return textView;
//    }
}
