package com.gxuc.runfast.business.util;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.JsonSyntaxException;
import com.gxuc.runfast.business.ui.login.LoginActivity;
import com.orhanobut.logger.Logger;

import org.reactivestreams.Subscriber;
import org.reactivestreams.Subscription;

import java.net.ConnectException;
import java.net.SocketException;
import java.net.SocketTimeoutException;
import java.net.UnknownHostException;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import retrofit2.HttpException;

/**
 * 响应Subscriber
 * Created by Berial on 2016/10/18.
 */

public abstract class ResponseSubscriber<T> implements Observer<T> {

    private Context mContext;

    public ResponseSubscriber(Context context) {
        mContext = context.getApplicationContext();
    }

    @Override
    public void onSubscribe(Disposable d) {
        if (!NetworkUtils.isAvailable(mContext)) {
            toast("尚未连接网络，请打开网络重新加载");
            onComplete();
        }
    }


    @Override
    public void onNext(T t) {
//        toast("xxxxx: " + t.toString());
//        Log.d("devon", "xxxxx: " + t.toString());
        onSuccess(t);
    }

    public abstract void onSuccess(T t);

    @Override
    public void onComplete() {
    }

    @Override
    public void onError(Throwable e) {
        if (e instanceof ConnectException || e instanceof SocketException) {
            toast("网络连接异常，请稍后重试");
        } else if (e instanceof SocketTimeoutException) {
            toast("网络请求超时，请检查网络状态");
        } else if (e instanceof JsonSyntaxException) {
            String message = e.getMessage();
            if (message.contains("relogin")) {
                Intent intent = new Intent(mContext, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            } else {
                toast("服务器数据异常，请稍候重试");
            }
        } else if (e instanceof UnknownHostException) {
            toast("当前网络不可用，请检查网络情况");
        } else if (e instanceof HttpException) {
            toast("服务器数据异常，请稍候重试");
        } else {
            Logger.d(e);
        }
    }

    private void toast(String message) {
        if (mContext != null) {
            Toast.makeText(mContext, message, Toast.LENGTH_SHORT).show();
        }
    }
}

