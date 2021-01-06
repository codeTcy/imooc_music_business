package com.tcy.music_business.application;

import android.app.Application;

import com.alibaba.android.arouter.launcher.ARouter;
import com.tcy.lib_audio.app.AudioHelper;
import com.tcy.lib_share.share.ShareManager;

public class ImoocVoiceApplication extends Application {

    private static ImoocVoiceApplication mApplication = null;

    @Override
    public void onCreate() {
        super.onCreate();
        mApplication = this;
        //音频SDK初始化
        AudioHelper.init(this);
        //分享组件初始化
        ShareManager.initSDK(this);
        //ARouter初始化
        ARouter.init(this);

    }

    public static ImoocVoiceApplication getInstance() {
        return mApplication;
    }
}
