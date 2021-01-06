package com.tcy.music_business.view.friend.adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;


import com.tcy.lib_audio.app.AudioHelper;
import com.tcy.lib_common_ui.MultiImageViewLayout;
import com.tcy.lib_common_ui.recyclerview.MultiItemTypeAdapter;
import com.tcy.lib_common_ui.recyclerview.base.ItemViewDelegate;
import com.tcy.lib_common_ui.recyclerview.base.ViewHolder;
import com.tcy.lib_image_loader.app.ImageLoaderManager;
import com.tcy.lib_video.videoplayer.core.VideoAdContext;
import com.tcy.music_business.R;
import com.tcy.music_business.model.friend.FriendBodyValue;
import com.tcy.music_business.utils.UserManager;
import com.tcy.music_business.view.login.LoginActivity;

import java.util.List;


public class FriendRecyclerAdapter extends MultiItemTypeAdapter {
    public static final int MUSIC_TYPE = 1;
    public static final int VIDEO_TYPE = 2;
    private Context mContext;

    public FriendRecyclerAdapter(Context context, List<FriendBodyValue> list) {
        super(context, list);
        this.mContext = context;
        addItemViewDelegate(1, new MusicItemDelegate());
        addItemViewDelegate(2, new VideoItemDelegate());
    }

    private class MusicItemDelegate implements ItemViewDelegate<FriendBodyValue> {
        @Override // com.imooc.lib_commin_ui.recyclerview.base.ItemViewDelegate
        public int getItemViewLayoutId() {
            return R.layout.item_friend_list_picture_layout;
        }

        private MusicItemDelegate() {
        }

        public boolean isForViewType(FriendBodyValue friendBodyValue, int i) {
            return friendBodyValue.type == 1;
        }

        public void convert(ViewHolder viewHolder, final FriendBodyValue friendBodyValue, int i) {
            viewHolder.setText(R.id.name_view, friendBodyValue.name + " 分享单曲:");
            viewHolder.setText(R.id.fansi_view, friendBodyValue.fans + "粉丝");
            viewHolder.setText(R.id.text_view, friendBodyValue.text);
            viewHolder.setText(R.id.zan_view, friendBodyValue.zan);
            viewHolder.setText(R.id.message_view, friendBodyValue.msg);
            viewHolder.setText(R.id.audio_name_view, friendBodyValue.audioBean.name);
            viewHolder.setText(R.id.audio_author_view, friendBodyValue.audioBean.album);
            viewHolder.setOnClickListener(R.id.album_layout, new View.OnClickListener() {
                /* class com.imooc.imooc_voice.view.friend.adapter.FriendRecyclerAdapter.MusicItemDelegate.AnonymousClass1 */

                public void onClick(View view) {
                    AudioHelper.addAudio((Activity) FriendRecyclerAdapter.this.mContext, friendBodyValue.audioBean);
                }
            });
            viewHolder.setOnClickListener(R.id.guanzhu_view, new View.OnClickListener() {
                /* class com.imooc.imooc_voice.view.friend.adapter.FriendRecyclerAdapter.MusicItemDelegate.AnonymousClass2 */

                public void onClick(View view) {
                    if (!UserManager.getInstance().hasLogin()) {
                        LoginActivity.start(FriendRecyclerAdapter.this.mContext);
                    }
                }
            });
            ImageLoaderManager.getInstance().displayImageForCircle((ImageView) viewHolder.getView(R.id.photo_view), friendBodyValue.avatr);
            ImageLoaderManager.getInstance().displayImageForView((ImageView) viewHolder.getView(R.id.album_view), friendBodyValue.audioBean.albumPic);
            ((MultiImageViewLayout) viewHolder.getView(R.id.image_layout)).setList(friendBodyValue.pics);
        }
    }

    private class VideoItemDelegate implements ItemViewDelegate<FriendBodyValue> {
        @Override // com.imooc.lib_commin_ui.recyclerview.base.ItemViewDelegate
        public int getItemViewLayoutId() {
            return R.layout.item_friend_list_video_layout;
        }

        private VideoItemDelegate() {
        }

        public boolean isForViewType(FriendBodyValue friendBodyValue, int i) {
            return friendBodyValue.type == 2;
        }

        public void convert(ViewHolder viewHolder, FriendBodyValue friendBodyValue, int i) {
            new VideoAdContext((RelativeLayout) viewHolder.getView(R.id.video_layout), friendBodyValue.videoUrl);
            viewHolder.setText(R.id.fansi_view, friendBodyValue.fans + "粉丝");
            viewHolder.setText(R.id.name_view, friendBodyValue.name + " 分享视频");
            viewHolder.setText(R.id.text_view, friendBodyValue.text);
            viewHolder.setOnClickListener(R.id.guanzhu_view, new View.OnClickListener() {
                /* class com.imooc.imooc_voice.view.friend.adapter.FriendRecyclerAdapter.VideoItemDelegate.AnonymousClass1 */

                public void onClick(View view) {
                    if (!UserManager.getInstance().hasLogin()) {
                        LoginActivity.start(FriendRecyclerAdapter.this.mContext);
                    }
                }
            });
            ImageLoaderManager.getInstance().displayImageForCircle((ImageView) viewHolder.getView(R.id.photo_view), friendBodyValue.avatr);
        }
    }
}


