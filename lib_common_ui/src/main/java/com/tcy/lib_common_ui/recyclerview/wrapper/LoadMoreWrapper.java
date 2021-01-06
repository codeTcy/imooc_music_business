package com.tcy.lib_common_ui.recyclerview.wrapper;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.tcy.lib_common_ui.recyclerview.base.ViewHolder;
import com.tcy.lib_common_ui.recyclerview.utils.WrapperUtils;


public class LoadMoreWrapper<T> extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    public static final int ITEM_TYPE_LOAD_MORE = 2147483645;
    private RecyclerView.Adapter mInnerAdapter;
    private int mLoadMoreLayoutId;
    private View mLoadMoreView;
    private OnLoadMoreListener mOnLoadMoreListener;

    public interface OnLoadMoreListener {
        void onLoadMoreRequested();
    }

    public LoadMoreWrapper(RecyclerView.Adapter adapter) {
        this.mInnerAdapter = adapter;
    }

    private boolean hasLoadMore() {
        return (this.mLoadMoreView == null && this.mLoadMoreLayoutId == 0) ? false : true;
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private boolean isShowLoadMore(int i) {
        return hasLoadMore() && i >= this.mInnerAdapter.getItemCount();
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemViewType(int i) {
        if (isShowLoadMore(i)) {
            return ITEM_TYPE_LOAD_MORE;
        }
        return this.mInnerAdapter.getItemViewType(i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        if (i != 2147483645) {
            return this.mInnerAdapter.onCreateViewHolder(viewGroup, i);
        }
        if (this.mLoadMoreView != null) {
            return ViewHolder.createViewHolder(viewGroup.getContext(), this.mLoadMoreView);
        }
        return ViewHolder.createViewHolder(viewGroup.getContext(), viewGroup, this.mLoadMoreLayoutId);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int i) {
        if (isShowLoadMore(i)) {
            OnLoadMoreListener onLoadMoreListener = this.mOnLoadMoreListener;
            if (onLoadMoreListener != null) {
                onLoadMoreListener.onLoadMoreRequested();
                return;
            }
            return;
        }
        this.mInnerAdapter.onBindViewHolder(viewHolder, i);
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        WrapperUtils.onAttachedToRecyclerView(this.mInnerAdapter, recyclerView, new WrapperUtils.SpanSizeCallback() {
            /* class com.imooc.lib_commin_ui.recyclerview.wrapper.LoadMoreWrapper.AnonymousClass1 */

            @Override // com.imooc.lib_commin_ui.recyclerview.utils.WrapperUtils.SpanSizeCallback
            public int getSpanSize(GridLayoutManager gridLayoutManager, GridLayoutManager.SpanSizeLookup spanSizeLookup, int i) {
                if (LoadMoreWrapper.this.isShowLoadMore(i)) {
                    return gridLayoutManager.getSpanCount();
                }
                if (spanSizeLookup != null) {
                    return spanSizeLookup.getSpanSize(i);
                }
                return 1;
            }
        });
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public void onViewAttachedToWindow(RecyclerView.ViewHolder viewHolder) {
        this.mInnerAdapter.onViewAttachedToWindow(viewHolder);
        if (isShowLoadMore(viewHolder.getLayoutPosition())) {
            setFullSpan(viewHolder);
        }
    }

    private void setFullSpan(RecyclerView.ViewHolder viewHolder) {
        ViewGroup.LayoutParams layoutParams = viewHolder.itemView.getLayoutParams();
        if (layoutParams != null && (layoutParams instanceof StaggeredGridLayoutManager.LayoutParams)) {
            ((StaggeredGridLayoutManager.LayoutParams) layoutParams).setFullSpan(true);
        }
    }

    @Override // androidx.recyclerview.widget.RecyclerView.Adapter
    public int getItemCount() {
        return this.mInnerAdapter.getItemCount() + (hasLoadMore() ? 1 : 0);
    }

    public LoadMoreWrapper setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        if (onLoadMoreListener != null) {
            this.mOnLoadMoreListener = onLoadMoreListener;
        }
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(View view) {
        this.mLoadMoreView = view;
        return this;
    }

    public LoadMoreWrapper setLoadMoreView(int i) {
        this.mLoadMoreLayoutId = i;
        return this;
    }
}