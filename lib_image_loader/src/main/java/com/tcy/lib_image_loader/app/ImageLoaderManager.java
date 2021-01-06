package com.tcy.lib_image_loader.app;

import android.app.Notification;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.BitmapTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.bumptech.glide.request.target.NotificationTarget;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.tcy.lib_image_loader.R;
import com.tcy.lib_image_loader.image.CustomRequestListener;
import com.tcy.lib_image_loader.image.Utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;


/**
 * 图处加载类，外界唯一通信类,
 * 支持为view,notification,appwidget,viewGroup加载图片
 */
public class ImageLoaderManager {

    private ImageLoaderManager() {

    }

    //内部类方式写单例模式
    private static class SingleHolder {
        private static ImageLoaderManager instance = new ImageLoaderManager();
    }

    public static ImageLoaderManager getInstance() {
        return SingleHolder.instance;
    }

    /**
     * 为ImageView加载图片
     */
    public void displayImageForView(ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()//转为bitmap
                .load(url)
                .apply(initCommonRequestOption())//给一个requestOptions
                .transition(BitmapTransitionOptions.withCrossFade())//加载效果
                .into(imageView);
    }

    /**
     * 为ImageView加载圆形图片
     *
     * @param imageView
     * @param url
     */
    public void displayImageForCircle(final ImageView imageView, String url) {
        Glide.with(imageView.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new BitmapImageViewTarget(imageView) {
                    //将imageView包装成target
                    @Override
                    protected void setResource(Bitmap resource) {
                        RoundedBitmapDrawable drawable = RoundedBitmapDrawableFactory
                                .create(imageView.getResources(), resource);
                        drawable.setCircular(true);
                        imageView.setImageDrawable(drawable);
                    }
                });
    }

    /**
     * 为viewGroup设置背景，并模糊处理
     *
     * @param group
     * @param url
     */
    public void displayImageForViewGroup(final ViewGroup group, String url) {
        Glide.with(group.getContext())
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .into(new SimpleTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull final Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        /**
                         * 直接加载会比较耗时
                         * 所以要用RxJava2在子线程进行操作,来对bitmap进行转化
                         * 把bitmap映射成drawable
                         */
                        final Bitmap res = resource;
                        Observable.just(resource).map(new Function<Bitmap, Drawable>() {
                            @Override
                            public Drawable apply(Bitmap bitmap) throws Exception {
                                /**
                                 * 将bitmap进行模糊处理并转为drawable
                                 */
                                Drawable drawable = new BitmapDrawable(
                                        Utils.doBlur(res, 100, true));
                                return drawable;
                            }
                        }).subscribeOn(Schedulers.io())
                                .observeOn(AndroidSchedulers.mainThread())
                                .subscribe(new Consumer<Drawable>() {
                                    @Override
                                    public void accept(Drawable drawable) throws Exception {
                                        group.setBackground(drawable);
                                    }
                                });
                    }
                });
    }


    /**
     * 为notification加载图片
     *
     * @param context         上下文
     * @param rv              布局
     * @param id              控件id
     * @param notification    notification对象
     * @param NOTIFICATION_ID notification的Id
     * @param url             url
     */
    public void displayImageForNotification(Context context, RemoteViews rv,
                                            int id, Notification notification,
                                            int NOTIFICATION_ID, String url) {
        this.displayImageForTarget(context, initNotificationTarget(context, rv, id, notification, NOTIFICATION_ID), url);
    }


    /**
     * 为非view加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url) {
        this.displayImageForTarget(context, target, url, null);
    }


    /**
     * 为非View加载图片
     */
    private void displayImageForTarget(Context context, Target target, String url, CustomRequestListener requestListener) {
        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(initCommonRequestOption())
                .transition(BitmapTransitionOptions.withCrossFade())
                .fitCenter()
                .listener(requestListener)
                .into(target);
    }


    /**
     * 返回notification类型的target
     */
    private NotificationTarget initNotificationTarget(Context context, RemoteViews rv, int id,
                                                      Notification notification, int NOTIFICATION_ID) {
        NotificationTarget target = new NotificationTarget(context, id, rv, notification, NOTIFICATION_ID);
        return target;

    }

    private RequestOptions initCommonRequestOption() {
        RequestOptions options = new RequestOptions();
        options.placeholder(R.mipmap.b4y)//默认图片
                .error(R.mipmap.b4y)//错误图片
                .diskCacheStrategy(DiskCacheStrategy.AUTOMATIC)//缓存策略
                .skipMemoryCache(false)//使用内存缓存
                .priority(Priority.NORMAL);//优先级
        return options;
    }


}
