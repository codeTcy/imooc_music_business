package com.tcy.lib_network.okhttp;

import com.tcy.lib_network.okhttp.listener.DisposeDataHandle;
import com.tcy.lib_network.okhttp.response.CommonFileCallback;
import com.tcy.lib_network.okhttp.response.CommonJsonCallback;

import org.jetbrains.annotations.NotNull;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLSession;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 用来发送get,post请求的工具类，包括设置一些请求的共用参数
 */
public class CommonOkHttpClient {

    private static final int TIME_OUT = 30;
    private static OkHttpClient mOkHttpClient;

    //完成对okhttpClient的初始化
    static {
        OkHttpClient.Builder okhttpClientBuilder = new OkHttpClient.Builder();

        /**
         * 对域名都是默认信任的
         */
        okhttpClientBuilder.hostnameVerifier(new HostnameVerifier() {
            @Override
            public boolean verify(String hostname, SSLSession session) {
                return true;
            }
        });

        /**
         * 为所有请求添加请求头，看个人需求
         */
        okhttpClientBuilder.addInterceptor(new Interceptor() {
            //拦截器
            @NotNull
            @Override
            public Response intercept(@NotNull Chain chain) throws IOException {
                Request request = chain.request().newBuilder()
                        .addHeader("User-Agent", "Imooc-Mobile")// 标明发送本次请求的客户端
                        .build();
                return chain.proceed(request);
            }
        });

        okhttpClientBuilder.connectTimeout(TIME_OUT, TimeUnit.SECONDS);
        okhttpClientBuilder.readTimeout(TIME_OUT, TimeUnit.SECONDS);
        okhttpClientBuilder.writeTimeout(TIME_OUT, TimeUnit.SECONDS);
        okhttpClientBuilder.followRedirects(true);//允许重定向

        mOkHttpClient = okhttpClientBuilder.build();
    }

    /**
     * get请求
     *
     * @return
     */
    public static Call get(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * post请求
     *
     * @return
     */
    public static Call post(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonJsonCallback(handle));
        return call;
    }

    /**
     * 文件下载请求
     *
     * @return
     */
    public static Call downloadFile(Request request, DisposeDataHandle handle) {
        Call call = mOkHttpClient.newCall(request);
        call.enqueue(new CommonFileCallback(handle));
        return call;
    }
}
