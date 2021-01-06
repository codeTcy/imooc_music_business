package com.tcy.music_business.view.friend;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import com.tcy.lib_common_ui.recyclerview.wrapper.LoadMoreWrapper;
import com.tcy.lib_network.okhttp.listener.DisposeDataListener;
import com.tcy.music_business.R;
import com.tcy.music_business.api.RequestCenter;
import com.tcy.music_business.model.friend.BaseFriendModel;
import com.tcy.music_business.model.friend.FriendBodyValue;
import com.tcy.music_business.view.friend.adapter.FriendRecyclerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * 首页朋友fragment
 */

public class FriendFragment extends Fragment implements
        SwipeRefreshLayout.OnRefreshListener,
        LoadMoreWrapper.OnLoadMoreListener {
    private FriendRecyclerAdapter mAdapter;
    private Context mContext;
    private List<FriendBodyValue> mDatas = new ArrayList();
    private LoadMoreWrapper mLoadMoreWrapper;
    private BaseFriendModel mRecommandData;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;

    public static Fragment newInstance() {
        return new FriendFragment();
    }

    @Override // androidx.fragment.app.Fragment
    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mContext = getActivity();
    }

    @Override // androidx.fragment.app.Fragment
    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_discory_layout, (ViewGroup) null);
        this.mSwipeRefreshLayout = (SwipeRefreshLayout) inflate.findViewById(R.id.refresh_layout);
        this.mSwipeRefreshLayout.setColorSchemeColors(getResources().getColor(R.color.color_fb7299));
        this.mSwipeRefreshLayout.setOnRefreshListener(this);
        this.mRecyclerView = (RecyclerView) inflate.findViewById(R.id.recyclerview);
        this.mRecyclerView.setLayoutManager(new LinearLayoutManager(this.mContext));
        return inflate;
    }

    @Override // androidx.fragment.app.Fragment
    public void onViewCreated(@NonNull View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        requestData();
    }

    @Override // androidx.swiperefreshlayout.widget.SwipeRefreshLayout.OnRefreshListener
    public void onRefresh() {
        requestData();
    }

    @Override // com.imooc.lib_commin_ui.recyclerview.wrapper.LoadMoreWrapper.OnLoadMoreListener
    public void onLoadMoreRequested() {
        loadMore();
    }

    private void loadMore() {
        RequestCenter.requestFriendData(new DisposeDataListener() {
            /* class com.imooc.imooc_voice.view.friend.FriendFragment.AnonymousClass1 */

            @Override // com.imooc.lib_network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object obj) {
                FriendFragment.this.mDatas.addAll(((BaseFriendModel) obj).data.list);
                FriendFragment.this.mLoadMoreWrapper.notifyDataSetChanged();
            }

            @Override // com.imooc.lib_network.okhttp.listener.DisposeDataListener
            public void onFailure(Object obj) {
//                onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.FRIEND_DATA, BaseFriendModel.class));
            }
        });
    }

    private void requestData() {
        RequestCenter.requestFriendData(new DisposeDataListener() {
            /* class com.imooc.imooc_voice.view.friend.FriendFragment.AnonymousClass2 */

            @Override // com.imooc.lib_network.okhttp.listener.DisposeDataListener
            public void onSuccess(Object obj) {
                FriendFragment.this.mRecommandData = (BaseFriendModel) obj;
                FriendFragment.this.updateView();
            }

            @Override // com.imooc.lib_network.okhttp.listener.DisposeDataListener
            public void onFailure(Object obj) {
//                onSuccess(ResponseEntityToModule.parseJsonToModule(MockData.FRIEND_DATA, BaseFriendModel.class));
            }
        });
    }

    /* access modifiers changed from: private */
    /* access modifiers changed from: public */
    private void updateView() {
        this.mSwipeRefreshLayout.setRefreshing(false);
        this.mDatas = this.mRecommandData.data.list;
        this.mAdapter = new FriendRecyclerAdapter(this.mContext, this.mDatas);
        this.mLoadMoreWrapper = new LoadMoreWrapper(this.mAdapter);
        this.mLoadMoreWrapper.setLoadMoreView(R.layout.default_loading);
        this.mLoadMoreWrapper.setOnLoadMoreListener(this);
        this.mRecyclerView.setAdapter(this.mLoadMoreWrapper);
    }
}
