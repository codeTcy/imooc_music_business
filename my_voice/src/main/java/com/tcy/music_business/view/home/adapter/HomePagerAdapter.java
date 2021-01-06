package com.tcy.music_business.view.home.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.tcy.music_business.model.CHANNEL;
import com.tcy.music_business.view.discory.DiscoryFragment;
import com.tcy.music_business.view.friend.FriendFragment;
import com.tcy.music_business.view.mine.MineFragment;


/**
 * 首页viewPager的adapter
 */
public class HomePagerAdapter extends FragmentPagerAdapter {

    private CHANNEL[] mList;

    public HomePagerAdapter(@NonNull FragmentManager fm, CHANNEL[] datas) {
        super(fm);
        mList = datas;
    }

    //初始化对应的fragment
    //这种方式，避免一次性创建所有的fragment,提高内存使用率
    @NonNull
    @Override
    public Fragment getItem(int position) {
        int type = mList[position].getValue();
        switch (type) {
            case CHANNEL.MINE_ID:
                return MineFragment.newInstance();
            case CHANNEL.DISCORY_ID:
                return DiscoryFragment.newInstance();
            case CHANNEL.FRIEND_ID:
                return FriendFragment.newInstance();
        }
        return null;
    }

    @Override
    public int getCount() {
        return mList == null ? 0 : mList.length;
    }
}
