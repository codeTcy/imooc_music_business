package com.tcy.lib_network.okhttp.listener;

public interface DisposeDownloadListener extends DisposeDataListener {
    void onProgress(int progress);
}
