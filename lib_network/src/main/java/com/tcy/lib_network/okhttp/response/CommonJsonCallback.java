package com.tcy.lib_network.okhttp.response;

import android.os.Looper;

import com.google.gson.Gson;
import com.tcy.lib_network.okhttp.exception.OkHttpException;
import com.tcy.lib_network.okhttp.listener.DisposeDataHandle;
import com.tcy.lib_network.okhttp.listener.DisposeDataListener;

import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.io.IOException;

import android.os.Handler;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 处理Json类型的响应
 */
public class CommonJsonCallback implements Callback {

    protected final String EMPTY_MSG = "";

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int JSON_ERROR = -2; // the JSON relative error
    protected final int OTHER_ERROR = -3; // the unknown error


    //回调,与应用层进行通信
    private DisposeDataListener mListener;
    //字节码，决定了我们把json转为哪个类的对象
    private Class<?> mClass;
    //利用Handler把数据发送到UI线程中
    private Handler mDeliveryHandler;

    public CommonJsonCallback(DisposeDataHandle handle) {
        this.mListener = handle.mListener;
        this.mClass = handle.mClass;
        //1.要刷新UI，handler要用到主线程的looper。那么在主线程 Handler handler = new Handler();，
        // 如果在其他线程，也要满足这个功能的话，要Handler handler = new Handler(Looper.getMainLooper());
        //2.不用刷新ui,只是处理消息。 当前线程如果是主线程的话，Handler handler = new Handler();
        // 不是主线程的话，Looper.prepare(); Handler handler = new Handler();Looper.loop();
        // 或者Handler handler = new Handler(Looper.getMainLooper());
        //若是实例化的时候用Looper.getMainLooper()就表示放到主UI线程去处理。
        //如果不是的话，因为只有UI线程默认Loop.prepare();Loop.loop();过，其他线程需要手动调用这两个，否则会报错。
        //https://blog.csdn.net/thanklife/article/details/17006865
        this.mDeliveryHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * 响应失败
     *
     * @param call
     * @param e
     */
    @Override
    public void onFailure(@NotNull Call call, @NotNull final IOException e) {
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                //OkHttpException是自定义异常类
                mListener.onFailure(new OkHttpException(NETWORK_ERROR, e));
            }
        });
    }

    /**
     * 响应成功
     *
     * @param call
     * @param response
     * @throws IOException
     */
    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        final String result = response.body().string();
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                handleResponse(result);
            }
        });
    }

    private void handleResponse(Object responseObj) {
        if (responseObj == null || "".equals(responseObj.toString().trim())) {
            //失败
            mListener.onFailure(new OkHttpException(NETWORK_ERROR, EMPTY_MSG));
            return;
        }

        /**
         * 解析json,可以用gson、fastJson
         */
        try {

            JSONObject result = new JSONObject(responseObj.toString());
            if (mClass == null) {
                //不想解析，只想拿原始数据
                mListener.onSuccess(result);
            } else {
                //网络框架帮应用层进行解析
                //解析为实体对象
                Object obj = new Gson().fromJson(responseObj.toString(), mClass);
                if (obj != null) {
                    //解析成功
                    mListener.onSuccess(obj);
                } else {
                    //解析失败
                    mListener.onFailure(new OkHttpException(JSON_ERROR, EMPTY_MSG));
                }
            }

        } catch (Exception e) {
            mListener.onFailure(new OkHttpException(OTHER_ERROR, e.getMessage()));
            e.printStackTrace();
        }
    }
}
