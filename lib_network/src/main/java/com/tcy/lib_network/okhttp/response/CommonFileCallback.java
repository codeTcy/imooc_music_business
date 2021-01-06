package com.tcy.lib_network.okhttp.response;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import androidx.annotation.NonNull;

import com.tcy.lib_network.okhttp.exception.OkHttpException;
import com.tcy.lib_network.okhttp.listener.DisposeDataHandle;
import com.tcy.lib_network.okhttp.listener.DisposeDownloadListener;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * 专门处理文件下载回调
 */
public class CommonFileCallback implements Callback {

    /**
     * the java layer exception, do not same to the logic error
     */
    protected final int NETWORK_ERROR = -1; // the network relative error
    protected final int IO_ERROR = -2; // the JSON relative error
    protected final String EMPTY_MSG = "";
    /**
     * 将其它线程的数据转发到UI线程
     */
    private static final int PROGRESS_MESSAGE = 0x01;
    private Handler mDeliveryHandler;
    private DisposeDownloadListener mListener;

    //文件路径
    private String mFilePath;
    //文件当前进度
    private int mProgress;


    public CommonFileCallback(DisposeDataHandle handle) {
        this.mListener = (DisposeDownloadListener) handle.mListener;
        this.mFilePath = handle.mSource;
        this.mDeliveryHandler = new Handler(Looper.getMainLooper()) {
            /**
             * 处理当前进度
             * @param msg
             */
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case PROGRESS_MESSAGE:
                        mListener.onProgress((int) msg.obj);
                        break;
                }
            }
        };
    }

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

    @Override
    public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
        final File file = handleResponse(response);
        mDeliveryHandler.post(new Runnable() {
            @Override
            public void run() {
                if (file != null) {
                    //请求成功，把文件回调到业务层
                    mListener.onSuccess(file);
                } else {
                    //请求失败
                    mListener.onFailure(new OkHttpException(IO_ERROR, EMPTY_MSG));
                }
            }
        });
    }

    /**
     * 从服务端拿到流，再将流写入文件中
     *
     * @param response
     * @return
     */
    private File handleResponse(Response response) {
        if (response == null) {
            return null;
        }
        InputStream inputStream = null;//写入流
        File file;//文件
        FileOutputStream fos = null;//输出流
        byte[] buffer = new byte[2048];//缓存
        int length;
        double currentLength = 0;//当前读写的长度
        double sumLength;//文件的总长度

        //以下为业务逻辑
        try {
            /**
             * 从输入流中读,然后写到输出流中
             */

            //判断文件是否存在，如果不存在就让他创建一下
            checkLocalFilePath(mFilePath);
            file = new File(mFilePath);
            fos = new FileOutputStream(file);
            inputStream = response.body().byteStream();
            sumLength = response.body().contentLength();

            //循环读取
            while ((length = inputStream.read(buffer)) != -1) {
                //把buffer中的数据全部写道fos流里面
                fos.write(buffer, 0, buffer.length);
                currentLength += length;
                mProgress = (int) (currentLength / sumLength * 100);//计算进度
                mDeliveryHandler.obtainMessage(PROGRESS_MESSAGE, mProgress).sendToTarget();//把进度抛出去
            }
            fos.flush();
        } catch (Exception e) {
            file = null;
        } finally {
            if (fos != null) {
                try {
                    fos.close();
                } catch (IOException e) {
                    file = null;
                    e.printStackTrace();
                }
            }

            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    file = null;
                    e.printStackTrace();
                }
            }
        }
        return file;

    }

    private void checkLocalFilePath(String localFilePath) {
        File path = new File(localFilePath.substring(0,
                localFilePath.lastIndexOf("/") + 1));
        File file = new File(localFilePath);
        if (!path.exists()) {
            path.mkdirs();
        }
        if (!file.exists()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


}
