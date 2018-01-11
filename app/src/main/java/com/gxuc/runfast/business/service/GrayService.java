package com.gxuc.runfast.business.service;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

import com.gxuc.runfast.business.data.repo.LoginRepo;
import com.gxuc.runfast.business.receiver.WakeReceiver;
import com.gxuc.runfast.business.ui.MainActivity;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;


/**
 * 灰色保活手法创建的Service进程
 *
 * @author lsl
 */
public class GrayService extends Service {

    /**
     * 定时唤醒的时间间隔，5分钟
     */
//    private final static int ALARM_INTERVAL = 5 * 60 * 1000;
    private final static int ALARM_INTERVAL = 10 * 1000;
    private final static int WAKE_REQUEST_CODE = 6666;

    private final static int GRAY_SERVICE_ID = -1001;
    Timer timer;
    private LoginRepo mRepo = LoginRepo.get();


    @Override
    public void onCreate() {
        Log.i("devon", "GrayService->onCreate");
        super.onCreate();
        if (timer == null) {
            timer = new Timer();
        }

        timer.schedule(new TimerTask() {
            public void run() {
                Log.i("devon", "GrayService->timer--run");
                if (!isMainActivityAlive(getApplicationContext(), "com.gxuc.runfast.business")) {
                    //说明系统中不存在这个activity
                    Log.i("devon", "说明系统中不存在这个activity");
//                    if (mRepo.hasLoggedIn()) {
//                    } else {
//                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("wake", true);
                    startActivity(intent);

                } else {
                    Log.i("---->", "说明系统中存在这个activity");
//                    Intent intent = new Intent();
//                    intent.setClassName("com.gxuc.runfast.shop.app.ui", "MainActivity");
//                    if (intent.resolveActivity(getPackageManager()) == null) {
//                        //说明系统中不存在这个activity
//                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
//                    }
                    Intent intent1 = new Intent();
                    intent1.setAction(MainActivity.BROADCAST_ACTION);
                    sendBroadcast(intent1);
                    Log.i("devon", "------------->sendBroadcast");
                }
            }
        }, 0, 10 * 1000);

    }

    private boolean isMainActivityAlive(Context context, String activityName) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningTaskInfo> list = am.getRunningTasks(100);
        for (ActivityManager.RunningTaskInfo info : list) {
            // 注意这里的 topActivity 包含 packageName和className，可以打印出来看看
            if (info.topActivity.getPackageName().toString().equals(activityName) || info.baseActivity.toString().equals(activityName)) {
                Log.i("devon", info.topActivity.getPackageName() + " info.baseActivity.getPackageName()=" + info.baseActivity.getPackageName());
                return true;
            }
        }
        return false;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.i("---->", "GrayService->onStartCommand");
        if (Build.VERSION.SDK_INT < 18) {
            startForeground(GRAY_SERVICE_ID, new Notification());//API < 18 此方法能有效隐藏Notification上的图标
        } else {
            Intent innerIntent = new Intent(this, GrayInnerService.class);
            startService(innerIntent);
            startForeground(GRAY_SERVICE_ID, new Notification());
        }

        //发送唤醒广播来促使挂掉的UI进程重新启动起来
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent alarmIntent = new Intent();
        alarmIntent.setAction(WakeReceiver.GRAY_WAKE_ACTION);
        PendingIntent operation = PendingIntent.getBroadcast(this, WAKE_REQUEST_CODE, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), ALARM_INTERVAL, operation);
        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onDestroy() {
        Log.i("---->", "GrayService->onDestroy");
        super.onDestroy();
    }


    /**
     * 给 API >= 18 的平台上用的灰色保活手段
     */
    public static class GrayInnerService extends Service {

        @Override
        public void onCreate() {
            Log.i("---->", "InnerService -> onCreate");
            super.onCreate();
        }

        @Override
        public int onStartCommand(Intent intent, int flags, int startId) {
            Log.i("---->", "InnerService -> onStartCommand");
            startForeground(GRAY_SERVICE_ID, new Notification());
            //stopForeground(true);
            stopSelf();
            return super.onStartCommand(intent, flags, startId);
        }

        @Override
        public IBinder onBind(Intent intent) {
            // TODO: Return the communication channel to the service.
            throw new UnsupportedOperationException("Not yet implemented");
        }

        @Override
        public void onDestroy() {
            Log.i("---->", "InnerService -> onDestroy");
            super.onDestroy();
        }
    }
}
