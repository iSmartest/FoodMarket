package com.lixin.foodmarket.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.lixin.foodmarket.tool.ImageManager;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;

//import com.baidu.mapapi.SDKInitializer;
//import com.lixin.carclassstore.tools.ImageManager;
//import com.umeng.socialize.PlatformConfig;
//import com.umeng.socialize.UMShareAPI;

public class MyApplication extends Application {
    public static Context CONTEXT;
    private static MyApplication myApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT=getApplicationContext();
        initImageLoader(this);
        UMShareAPI.get(this);
//        SDKInitializer.initialize(CONTEXT);
        PlatformConfig.setWeixin("wxf4d512f8f11f566f","81acb0fcccd4e0ea7c835f4da19155d3");
        PlatformConfig.setQQZone("1106112021","tNy60cXvvRrG1goP");
        myApplication = this;
}
    public static MyApplication getApplication() {
        return myApplication;
    }

    /**
     * 通过类名启动Activity
     *
     * @param targetClass
     */
    public static void openActivity(Context context, Class<?> targetClass) {
        openActivity(context, targetClass, null);
    }

    /**
     * 通过类名启动Activity，并且含有Bundle数据
     *
     * @param targetClass
     * @param extras
     */
    public static void openActivity(Context context, Class<?> targetClass,
                                    Bundle extras) {
        Intent intent = new Intent(context, targetClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }

    public static void openActivityForResult(Activity activity,
                                             Class<?> targetClass, Bundle extras, int requestCode) {
        Intent intent = new Intent(activity, targetClass);
        if (extras != null) {
            intent.putExtras(extras);
        }
        activity.startActivityForResult(intent, requestCode);
    }

    public static void openActivityForResult(Activity activity,
                                             Class<?> targetClass, int requestCode) {
        openActivityForResult(activity, targetClass, null, requestCode);
    }

    /**
     * 通过Action启动Activity
     *
     * @param action
     */
    public static void openActivity(Context context, String action) {
        openActivity(context, action, null);
    }

    /**
     * 通过Action启动Activity，并且含有Bundle数据
     *
     * @param action
     * @param extras
     */
    public static void openActivity(Context context, String action,
                                    Bundle extras) {
        Intent intent = new Intent(action);
        if (extras != null) {
            intent.putExtras(extras);
        }
        context.startActivity(intent);
    }
    public void exit() {
        //使用状态统计-结束
//        ImageManager.imageLoader.destroy();
        android.os.Process.killProcess(android.os.Process.myPid());
        ActivityManager activityMgr = (ActivityManager) this.getSystemService(ACTIVITY_SERVICE);
        activityMgr.killBackgroundProcesses(getPackageName());
        System.exit(0);
    }

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();
        ImageLoader.getInstance().init(config);

        ImageManager.init();
    }
}
