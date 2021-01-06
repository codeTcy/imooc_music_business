package com.tcy.lib_common_ui.recyclerview.base;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.text.util.Linkify;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.widget.Checkable;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;


public class ViewHolder extends RecyclerView.ViewHolder {
    private Context mContext;
    private View mConvertView;
    private SparseArray<View> mViews = new SparseArray<>();

    public ViewHolder(Context context, View view) {
        super(view);
        this.mContext = context;
        this.mConvertView = view;
    }

    public static ViewHolder createViewHolder(Context context, View view) {
        return new ViewHolder(context, view);
    }

    public static ViewHolder createViewHolder(Context context, ViewGroup viewGroup, int i) {
        return new ViewHolder(context, LayoutInflater.from(context).inflate(i, viewGroup, false));
    }

    public <T extends View> T getView(int i) {
        T t = (T) this.mViews.get(i);
        if (t != null) {
            return t;
        }
        T t2 = (T) this.mConvertView.findViewById(i);
        this.mViews.put(i, t2);
        return t2;
    }

    public View getConvertView() {
        return this.mConvertView;
    }

    public ViewHolder setText(int i, String str) {
        ((TextView) getView(i)).setText(str);
        return this;
    }

    public ViewHolder setImageResource(int i, int i2) {
        ((ImageView) getView(i)).setImageResource(i2);
        return this;
    }

    public ViewHolder setImageBitmap(int i, Bitmap bitmap) {
        ((ImageView) getView(i)).setImageBitmap(bitmap);
        return this;
    }

    public ViewHolder setImageDrawable(int i, Drawable drawable) {
        ((ImageView) getView(i)).setImageDrawable(drawable);
        return this;
    }

    public ViewHolder setBackgroundColor(int i, int i2) {
        getView(i).setBackgroundColor(i2);
        return this;
    }

    public ViewHolder setBackgroundRes(int i, int i2) {
        getView(i).setBackgroundResource(i2);
        return this;
    }

    public ViewHolder setTextColor(int i, int i2) {
        ((TextView) getView(i)).setTextColor(i2);
        return this;
    }

    public ViewHolder setTextColorRes(int i, int i2) {
        ((TextView) getView(i)).setTextColor(this.mContext.getResources().getColor(i2));
        return this;
    }

    @SuppressLint({"NewApi"})
    public ViewHolder setAlpha(int i, float f) {
        if (Build.VERSION.SDK_INT >= 11) {
            getView(i).setAlpha(f);
        } else {
            AlphaAnimation alphaAnimation = new AlphaAnimation(f, f);
            alphaAnimation.setDuration(0);
            alphaAnimation.setFillAfter(true);
            getView(i).startAnimation(alphaAnimation);
        }
        return this;
    }

    public ViewHolder setVisible(int i, boolean z) {
        getView(i).setVisibility(z ? View.VISIBLE : View.INVISIBLE);
        return this;
    }

    public ViewHolder linkify(int i) {
        Linkify.addLinks((TextView) getView(i), Linkify.ALL);
        return this;
    }

    public ViewHolder setTypeface(Typeface typeface, int... iArr) {
        for (int i : iArr) {
            TextView textView = (TextView) getView(i);
            textView.setTypeface(typeface);
            textView.setPaintFlags(textView.getPaintFlags() | 128);
        }
        return this;
    }

    public ViewHolder setProgress(int i, int i2) {
        ((ProgressBar) getView(i)).setProgress(i2);
        return this;
    }

    public ViewHolder setProgress(int i, int i2, int i3) {
        ProgressBar progressBar = (ProgressBar) getView(i);
        progressBar.setMax(i3);
        progressBar.setProgress(i2);
        return this;
    }

    public ViewHolder setMax(int i, int i2) {
        ((ProgressBar) getView(i)).setMax(i2);
        return this;
    }

    public ViewHolder setRating(int i, float f) {
        ((RatingBar) getView(i)).setRating(f);
        return this;
    }

    public ViewHolder setRating(int i, float f, int i2) {
        RatingBar ratingBar = (RatingBar) getView(i);
        ratingBar.setMax(i2);
        ratingBar.setRating(f);
        return this;
    }

    public ViewHolder setTag(int i, Object obj) {
        getView(i).setTag(obj);
        return this;
    }

    public ViewHolder setTag(int i, int i2, Object obj) {
        getView(i).setTag(i2, obj);
        return this;
    }

    public ViewHolder setChecked(int i, boolean z) {
        ((Checkable) getView(i)).setChecked(z);
        return this;
    }

    public ViewHolder setOnClickListener(int i, View.OnClickListener onClickListener) {
        getView(i).setOnClickListener(onClickListener);
        return this;
    }

    public ViewHolder setOnTouchListener(int i, View.OnTouchListener onTouchListener) {
        getView(i).setOnTouchListener(onTouchListener);
        return this;
    }

    public ViewHolder setOnLongClickListener(int i, View.OnLongClickListener onLongClickListener) {
        getView(i).setOnLongClickListener(onLongClickListener);
        return this;
    }

}
