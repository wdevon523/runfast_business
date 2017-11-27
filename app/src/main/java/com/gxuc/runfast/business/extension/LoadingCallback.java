package com.gxuc.runfast.business.extension;

public interface LoadingCallback {

    void onFirstLoadFinish();

    void onRefreshFinish();

    void onLoadMoreFinish(boolean isLastPage);

    void onLoadEmpty(String text);
}
