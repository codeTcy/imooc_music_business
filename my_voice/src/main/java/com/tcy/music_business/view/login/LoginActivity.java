package com.tcy.music_business.view.login;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.tcy.lib_common_ui.base.BaseActivity;
import com.tcy.lib_network.okhttp.listener.DisposeDataListener;
import com.tcy.music_business.R;
import com.tcy.music_business.api.RequestCenter;
import com.tcy.music_business.model.login.LoginEvent;
import com.tcy.music_business.model.user.User;
import com.tcy.music_business.utils.UserManager;

import org.greenrobot.eventbus.EventBus;

/**
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements DisposeDataListener {


    //给外部提供一个启动方法
    public static void start(Context context) {
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewById(R.id.login_view).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RequestCenter.login(LoginActivity.this);
            }
        });
    }

    @Override
    public void onSuccess(Object responseObj) {
        //登录成功
        //处理正常逻辑
        User user = (User) responseObj;
        UserManager.getInstance().saveUser(user);
        /**
         * 保存完信息以后，就对外发送一个事件
         * （因为有多个页面可能需要更新，所以不能单独跳转）
         * 以观察者模式同时通知多个UI进行更新
         */
        EventBus.getDefault().post(new LoginEvent());
        finish();
    }

    @Override
    public void onFailure(Object reasonObj) {
        //登录失败

    }
}
