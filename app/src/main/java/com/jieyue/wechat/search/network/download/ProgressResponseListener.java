package com.jieyue.wechat.search.network.download;

/**
 * Created by ____ Bye丶 on 2016/9/21.
 */
public interface ProgressResponseListener {
    public void onResponseProgress(long totalBytesRead, long l, boolean b);
}
