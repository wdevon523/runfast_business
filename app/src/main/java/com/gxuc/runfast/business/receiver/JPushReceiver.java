package com.gxuc.runfast.business.receiver;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;

import com.google.gson.annotations.SerializedName;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.DataLayer;
import com.gxuc.runfast.business.ui.MainActivity;

import cn.jpush.android.api.JPushInterface;

import static android.content.Context.NOTIFICATION_SERVICE;

/**
 * 自定义接收器
 * <p>
 * 如果不定义这个 JPushReceiver，则：
 * 1) 默认用户会打开主界面
 * 2) 接收不到自定义消息
 */
public class JPushReceiver extends BroadcastReceiver {

    public static final String MESSAGE_RECEIVED_ACTION = "com.gxuc.runfast.business.MESSAGE_RECEIVED_ACTION";

    private static final int NOTIFICATION_SHOW_SHOW_AT_MOST = 5;   //推送通知最多显示条数

    private static final String NEW_ORDER = "neworder.caf";
    private static final String NEW_CANCEL = "newcancel.caf";

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();

        if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
            processCustomMessage(context, bundle);
        }
    }

    /**
     * 实现自定义推送声音
     */
    private void processCustomMessage(Context context, Bundle bundle) {
        NotificationCompat.Builder notification = new NotificationCompat.Builder(context);

        String title = bundle.getString(JPushInterface.EXTRA_TITLE);
        String msg = bundle.getString(JPushInterface.EXTRA_MESSAGE);
        String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);
        Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher);

        Intent mIntent = new Intent(context, MainActivity.class);
        mIntent.putExtras(bundle);
        mIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, mIntent, 0);

        notification.setContentIntent(pendingIntent)
                .setAutoCancel(true)
                .setContentText(msg)
                .setContentTitle("".equals(title) ? context.getString(R.string.app_name) : title)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(bitmap)
                .setNumber(NOTIFICATION_SHOW_SHOW_AT_MOST);

        if (!TextUtils.isEmpty(extras)) {
            Message message = DataLayer.getGson().fromJson(extras, Message.class);
            String sound = message.sound;
            if (TextUtils.isEmpty(sound)) return;
            if (NEW_ORDER.equals(sound)) {
                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.neworder));
            } else if (NEW_CANCEL.equals(sound)) {
                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.newcancel));
            } else {
                notification.setSound(Uri.parse("android.resource://" + context.getPackageName() + "/" + R.raw.default_sound));
            }
        }

        Notification build = notification.build();

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(NOTIFICATION_SERVICE);
        notificationManager.notify(NOTIFICATION_SHOW_SHOW_AT_MOST, build);  //id随意，正好使用定义的常量做id，0除外，0为默认的Notification

        sendMsg(context);
    }

    private void sendMsg(Context context) {
        Intent msgIntent = new Intent(MESSAGE_RECEIVED_ACTION);
        context.sendBroadcast(msgIntent);

        Intent Intent = new Intent(MainActivity.MESSAGE_RECEIVED_ACTION);
//        msgIntent.putExtra(MainActivity.KEY_MESSAGE, message);

        LocalBroadcastManager.getInstance(context).sendBroadcast(Intent);
    }

    private static class Message {
        @SerializedName("sound")
        String sound;
    }
}
