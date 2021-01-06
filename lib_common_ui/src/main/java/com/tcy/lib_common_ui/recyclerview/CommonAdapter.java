package com.tcy.lib_common_ui.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;

import com.tcy.lib_common_ui.recyclerview.base.ItemViewDelegate;
import com.tcy.lib_common_ui.recyclerview.base.ViewHolder;

import java.util.List;


public abstract class CommonAdapter<T> extends MultiItemTypeAdapter<T> {
    protected Context mContext;
    protected List<T> mDatas;
    protected LayoutInflater mInflater;
    protected int mLayoutId;

    /* access modifiers changed from: protected */
    public abstract void convert(ViewHolder viewHolder, T t, int i);

    public CommonAdapter(Context context, final int i, List<T> list) {
        super(context, list);
        this.mContext = context;
        this.mInflater = LayoutInflater.from(context);
        this.mLayoutId = i;
        this.mDatas = list;
        addItemViewDelegate(new ItemViewDelegate<T>() {
            /* class com.imooc.lib_commin_ui.recyclerview.CommonAdapter.AnonymousClass1 */

            @Override // com.imooc.lib_commin_ui.recyclerview.base.ItemViewDelegate
            public boolean isForViewType(T t, int i) {
                return true;
            }

            @Override // com.imooc.lib_commin_ui.recyclerview.base.ItemViewDelegate
            public int getItemViewLayoutId() {
                return i;
            }

            @Override // com.imooc.lib_commin_ui.recyclerview.base.ItemViewDelegate
            public void convert(ViewHolder viewHolder, T t, int i) {
                CommonAdapter.this.convert(viewHolder, t, i);
            }
        });
    }
}
