package com.tcy.music_business.utils;

import com.tcy.music_business.model.user.User;

/**
 * 单例管理登录用户信息
 * （这次用双检查机制，之前用的是内部类）
 * 两个if，所以被称为双检查机制
 */
public class UserManager {

    private static UserManager mInstance;
    private User mUser;

    public static UserManager getInstance() {
        if (mInstance == null) {
            //加锁，保证只有一个单例对象
            //加的是字节码的锁，保证唯一性
            synchronized (UserManager.class) {
                //进入锁以后再看看instance是不是空
                if (mInstance == null) {
                    mInstance = new UserManager();
                }
            }
        }
        return mInstance;
    }

    /**
     * 保存用户信息到内存
     */
    public void saveUser(User user) {
        mUser = user;
        saveLocal(user);
    }

    /**
     * 保存用户信息到本地数据库
     * 持久化用户信息
     */
    private void saveLocal(User user) {

    }

    /**
     * 获取用户信息
     *
     * @return
     */
    public User getUser() {
        return mUser;
    }

    /**
     * 从本地获取
     *
     * @return
     */
    public User getLocal() {
        //后面用greenDao来做
        return null;
    }

    /**
     * 判断是否登录过
     *
     * @return
     */
    public boolean hasLogin() {
        return getUser() == null ? false : true;
    }

    /**
     * 清空用户的内存数据
     */
    public void removeUser() {
        mUser = null;
        removeLocal();
    }

    /**
     * 清空用户的数据库信息
     */
    private void removeLocal() {
        //后面用greenDao来做
    }
}
