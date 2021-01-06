package com.tcy.lib_common_ui.recyclerview;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;


import com.tcy.lib_common_ui.recyclerview.base.ItemViewDelegate;
import com.tcy.lib_common_ui.recyclerview.base.ItemViewDelegateManager;
import com.tcy.lib_common_ui.recyclerview.base.ViewHolder;

import java.util.List;


public class MultiItemTypeAdapter<T> extends RecyclerView.Adapter<ViewHolder> {
    protected Context mContext;
    protected List<T> mDatas;
    protected ItemViewDelegateManager mItemViewDelegateManager = new ItemViewDelegateManager();
    protected OnItemClickListener mOnItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(View view, RecyclerView.ViewHolder viewHolder, int i);

        boolean onItemLongClick(View view, RecyclerView.ViewHolder viewHolder, int i);
    }

    /* access modifiers changed from: protected */
    public boolean isEnabled(int i) {
        return true;
    }

    public void onViewHolderCreated(ViewHolder viewHolder, View view) {
    }

    public MultiItemTypeAdapter(Context context, List<T> list) {
        this.mContext = context;
        this.mDatas = list;
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (!useItemViewDelegateManager()) {
            return super.getItemViewType(i);
        }
        return this.mItemViewDelegateManager.getItemViewType(this.mDatas.get(i), i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        ViewHolder createViewHolder = ViewHolder.createViewHolder(this.mContext, viewGroup, this.mItemViewDelegateManager.getItemViewDelegate(i).getItemViewLayoutId());
        onViewHolderCreated(createViewHolder, createViewHolder.getConvertView());
        setListener(viewGroup, createViewHolder, i);
        return createViewHolder;
    }

    public void convert(ViewHolder viewHolder, T t) {
        this.mItemViewDelegateManager.convert(viewHolder, t, viewHolder.getAdapterPosition());
    }

    /* access modifiers changed from: protected */
    public void setListener(ViewGroup viewGroup, final ViewHolder viewHolder, int i) {
        if (isEnabled(i)) {
            viewHolder.getConvertView().setOnClickListener(new View.OnClickListener() {
                /* class com.imooc.lib_commin_ui.recyclerview.MultiItemTypeAdapter.AnonymousClass1 */

                public void onClick(View view) {
                    if (MultiItemTypeAdapter.this.mOnItemClickListener != null) {
                        MultiItemTypeAdapter.this.mOnItemClickListener.onItemClick(view, viewHolder, viewHolder.getAdapterPosition());
                    }
                }
            });
            viewHolder.getConvertView().setOnLongClickListener(new View.OnLongClickListener() {
                /* class com.imooc.lib_commin_ui.recyclerview.MultiItemTypeAdapter.AnonymousClass2 */

                public boolean onLongClick(View view) {
                    if (MultiItemTypeAdapter.this.mOnItemClickListener == null) {
                        return false;
                    }
                    return MultiItemTypeAdapter.this.mOnItemClickListener.onItemLongClick(view, viewHolder, viewHolder.getAdapterPosition());
                }
            });
        }
    }

    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        convert(viewHolder, this.mDatas.get(i));
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mDatas.size();
    }

    public List<T> getDatas() {
        return this.mDatas;
    }

    public void addDatas(List<T> list) {
        this.mDatas.addAll(list);
        notifyDataSetChanged();
    }

    public MultiItemTypeAdapter addItemViewDelegate(ItemViewDelegate<T> itemViewDelegate) {
        this.mItemViewDelegateManager.addDelegate(itemViewDelegate);
        return this;
    }

    public MultiItemTypeAdapter addItemViewDelegate(int i, ItemViewDelegate<T> itemViewDelegate) {
        this.mItemViewDelegateManager.addDelegate(i, itemViewDelegate);
        return this;
    }

    /* access modifiers changed from: protected */
    public boolean useItemViewDelegateManager() {
        return this.mItemViewDelegateManager.getItemViewDelegateCount() > 0;
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }
}