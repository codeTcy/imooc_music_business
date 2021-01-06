package com.tcy.lib_network.okhttp.listener;

public class DisposeDataHandle {

    public DisposeDataListener mListener = null;
    public Class<?> mClass = null;
    public String mSource = null;//文件保存路径

    public DisposeDataHandle(DisposeDataListener listener) {
        this.mListener = listener;
    }

    public DisposeDataHandle(DisposeDataListener listener, Class<?> clazz) {
        /**
         * ？ 表示不确定的java类型。
         * T 表示java类型。
         * K V 分别代表java键值中的Key Value。
         * E 代表Element
         */
        this.mListener = listener;
        this.mClass = clazz;
    }

    public DisposeDataHandle(DisposeDataListener listener, String source) {
        this.mListener = listener;
        this.mSource = source;
    }
}
