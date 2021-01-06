package com.tcy.lib_common_ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.LinearLayout;


import com.tcy.lib_common_ui.utils.StatusBarUtil;
import com.tcy.lib_image_loader.app.ImageLoaderManager;

import java.util.List;

/**
 * 显示1~N张图片的View
 */

public class MultiImageViewLayout extends LinearLayout {
    public static int MAX_WIDTH;
    private int MAX_PER_ROW_COUNT = 3;
    private List<String> imagesList;
    private OnItemClickListener mOnItemClickListener;
    private LinearLayout.LayoutParams morePara;
    private LinearLayout.LayoutParams moreParaColumnFirst;
    private LinearLayout.LayoutParams onePicPara;
    private int pxImagePadding = StatusBarUtil.dip2px(getContext(), 3.0f);
    private int pxMoreWandH = 0;
    private LinearLayout.LayoutParams rowPara;

    public interface OnItemClickListener {
        void onItemClick(View view, int i);
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    public MultiImageViewLayout(Context context) {
        super(context);
    }

    public MultiImageViewLayout(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
    }

    public void setList(List<String> list) throws IllegalArgumentException {
        if (list != null) {
            this.imagesList = list;
            int i = MAX_WIDTH;
            if (i > 0) {
                this.pxMoreWandH = (i - (this.pxImagePadding * 2)) / 3;
                initImageLayoutParams();
            }
            initView();
            return;
        }
        throw new IllegalArgumentException("imageList is null...");
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        int measureWidth;
        if (MAX_WIDTH == 0 && (measureWidth = measureWidth(i)) > 0) {
            MAX_WIDTH = measureWidth;
            List<String> list = this.imagesList;
            if (list != null && list.size() > 0) {
                setList(this.imagesList);
            }
        }
        super.onMeasure(i, i2);
    }

    private int measureWidth(int i) {
        int mode = View.MeasureSpec.getMode(i);
        int size = View.MeasureSpec.getSize(i);
        if (mode == 1073741824) {
            return size;
        }
        if (mode == Integer.MIN_VALUE) {
            return Math.min(0, size);
        }
        return 0;
    }

    private void initImageLayoutParams() {
        this.onePicPara = new LinearLayout.LayoutParams(-2, -2);
        int i = this.pxMoreWandH;
        this.moreParaColumnFirst = new LinearLayout.LayoutParams(i, i);
        int i2 = this.pxMoreWandH;
        this.morePara = new LinearLayout.LayoutParams(i2, i2);
        this.morePara.setMargins(this.pxImagePadding, 0, 0, 0);
        this.rowPara = new LinearLayout.LayoutParams(-1, -2);
    }

    private void initView() {
        setOrientation(VERTICAL);
        removeAllViews();
        if (MAX_WIDTH == 0) {
            addView(new View(getContext()));
            return;
        }
        List<String> list = this.imagesList;
        if (!(list == null || list.size() == 0)) {
            if (this.imagesList.size() == 1) {
                addView(createImageView(0, false));
                return;
            }
            int size = this.imagesList.size();
            if (size == 4) {
                this.MAX_PER_ROW_COUNT = 2;
            } else {
                this.MAX_PER_ROW_COUNT = 3;
            }
            int i = this.MAX_PER_ROW_COUNT;
            int i2 = (size / i) + (size % i > 0 ? 1 : 0);
            for (int i3 = 0; i3 < i2; i3++) {
                LinearLayout linearLayout = new LinearLayout(getContext());
                linearLayout.setOrientation(HORIZONTAL);
                linearLayout.setLayoutParams(this.rowPara);
                if (i3 != 0) {
                    linearLayout.setPadding(0, this.pxImagePadding, 0, 0);
                }
                int i4 = this.MAX_PER_ROW_COUNT;
                if (size % i4 != 0) {
                    i4 = size % i4;
                }
                if (i3 != i2 - 1) {
                    i4 = this.MAX_PER_ROW_COUNT;
                }
                addView(linearLayout);
                int i5 = this.MAX_PER_ROW_COUNT * i3;
                for (int i6 = 0; i6 < i4; i6++) {
                    linearLayout.addView(createImageView(i6 + i5, true));
                }
            }
        }
    }

    private ImageView createImageView(int i, boolean z) {
        String str = this.imagesList.get(i);
        ImageView imageView = new ImageView(getContext());
        if (z) {
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setLayoutParams(i % this.MAX_PER_ROW_COUNT == 0 ? this.moreParaColumnFirst : this.morePara);
        } else {
            imageView.setAdjustViewBounds(true);
            imageView.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
            imageView.setLayoutParams(this.onePicPara);
        }
        imageView.setId(str.hashCode());
        imageView.setOnClickListener(new ImageOnClickListener(i));
        ImageLoaderManager.getInstance().displayImageForView(imageView, str);
        return imageView;
    }

    /* access modifiers changed from: private */
    public class ImageOnClickListener implements View.OnClickListener {
        private int position;

        public ImageOnClickListener(int i) {
            this.position = i;
        }

        public void onClick(View view) {
            if (MultiImageViewLayout.this.mOnItemClickListener != null) {
                MultiImageViewLayout.this.mOnItemClickListener.onItemClick(view, this.position);
            }
        }
    }
}