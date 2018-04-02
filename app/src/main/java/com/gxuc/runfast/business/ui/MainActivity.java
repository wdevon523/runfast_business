package com.gxuc.runfast.business.ui;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.text.TextUtils;
import android.util.Log;
import android.util.SparseArray;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.gxuc.runfast.business.BuildConfig;
import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.data.repo.BusinessRepo;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.service.GrayService;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.gxuc.runfast.business.ui.login.LoginActivity;
import com.gxuc.runfast.business.ui.mine.MineFragment;
import com.gxuc.runfast.business.ui.operation.OperationFragment;
import com.gxuc.runfast.business.ui.order.OrderManageFragment;
import com.gxuc.runfast.business.ui.order.PendingOrderFragment;
import com.gxuc.runfast.business.util.BottomNavigationViewHelper;
import com.gxuc.runfast.business.util.JobSchedulerManager;
import com.gxuc.runfast.business.util.SystemUtils;
import com.tbruyelle.rxpermissions2.RxPermissions;

import java.util.Set;
import java.util.Timer;
import java.util.TimerTask;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.api.TagAliasCallback;

import static android.content.DialogInterface.BUTTON_NEGATIVE;
import static android.content.DialogInterface.BUTTON_POSITIVE;

public class MainActivity extends BaseActivity implements LayoutProvider {

    public static final String MESSAGE_RECEIVED_ACTION = "com.gxuc.runfast.bussiness.MainActivity.MESSAGE_RECEIVED_ACTION";
    public static final String KEY_MESSAGE = "message";
    public static final String BROADCAST_ACTION = "com.lsl.corn";
    private String NEW_ORDER = "neworder.caf";
    private String NEW_CANCEL = "newcancel.caf";
    private SparseArray<Fragment> mFragments = new SparseArray<>(4);
    private int preCheckedId;
    // JobService，执行系统任务
    private JobSchedulerManager mJobManager;
    private boolean isExit = false;

    private static final int MSG_SET_ALIAS = 1001;

//    private AutoReceiveOrderReceiver receiver = new AutoReceiveOrderReceiver();

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            if (item.getItemId() == R.id.nav_mine && !CheckLoginState.hasLoggedIn()) {
                startAct(LoginActivity.class);
                return false;
            }
            commitFragment(mFragments.get(item.getItemId()));
            preCheckedId = item.getItemId();
            return true;
        }
    };
    private MessageReceiver mMessageReceiver;
    private MyBroadcastReceiver mBroadcastReceiver;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onInitViews() {

        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            Intent grayIntent = new Intent(getApplicationContext(), GrayService.class);
            startService(grayIntent);
        } else {
            mJobManager = JobSchedulerManager.getJobSchedulerInstance(this);
            mJobManager.startJobScheduler();
        }
        bindJpushAlias();
        registerMessageReceiver();
        registerWakeReceiver();
        requestPermissions();
        putFragments();
        setupNavigationView();
//        boolean wake = getIntent().getBooleanExtra("wake", false);
//        if (wake) {
//            Log.i("devon", "wake--->CATEGORY_HOME");
//            Intent intent = new Intent(Intent.ACTION_MAIN);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
//            intent.addCategory(Intent.CATEGORY_HOME);
//            startActivity(intent);
//        }
    }

    private void bindJpushAlias() {

        String alias = SystemUtils.getIMEI(this);
        if (TextUtils.isEmpty(alias)){
            alias = JPushInterface.getRegistrationID(this);
        }
        mHandler.sendMessage(mHandler.obtainMessage(MSG_SET_ALIAS, alias));


//        JPushInterface.setAlias(this, alias, new TagAliasCallback() {
//            @Override
//            public void gotResult(int i, String s, Set<String> set) {
//                Log.d("  绑定极光 ", "[initJPush] 设置别名: " + "i:" + i
//                        + ",s:" + s + ",set:" + set);
//            }
//        });

    }

    private final TagAliasCallback mAliasCallback = new TagAliasCallback() {
        @Override
        public void gotResult(int code, String alias, Set<String> tags) {
            String logs ;
            switch (code) {
                case 0:
                    logs = "Set tag and alias success";
                    Log.i("wdevon", logs);
                    // 建议这里往 SharePreference 里写一个成功设置的状态。成功设置一次后，以后不必再次设置了。
                    break;
                case 6002:
                    logs = "Failed to set alias and tags due to timeout. Try again after 60s.";
                    Log.i("wdevon", logs);
                    // 延迟 60 秒来调用 Handler 设置别名
                    mHandler.sendMessageDelayed(mHandler.obtainMessage(MSG_SET_ALIAS, alias), 1000 * 60);
                    break;
                default:
                    logs = "Failed with errorCode = " + code;
                    Log.e("wdevon", logs);
            }
        }
    };


    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(android.os.Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_SET_ALIAS:
                    Log.d("wdevon", "Set alias in handler.");
                    // 调用 JPush 接口来设置别名。
                    JPushInterface.setAliasAndTags(getApplicationContext(),
                            (String) msg.obj,
                            null,
                            mAliasCallback);
                    break;
                default:
                    Log.i("wdevon", "Unhandled msg - " + msg.what);
            }
        }
    };


    public void registerMessageReceiver() {
        mMessageReceiver = new MessageReceiver();
        IntentFilter filter = new IntentFilter();
        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
        filter.addAction(MESSAGE_RECEIVED_ACTION);
        LocalBroadcastManager.getInstance(this).registerReceiver(mMessageReceiver, filter);
    }

    private void registerWakeReceiver() {
        mBroadcastReceiver = new MyBroadcastReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BROADCAST_ACTION);
        registerReceiver(mBroadcastReceiver, intentFilter);
    }

    public class MessageReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            try {
                if (MESSAGE_RECEIVED_ACTION.equals(intent.getAction())) {
                    String sound = intent.getStringExtra(KEY_MESSAGE);
                    OrderManageFragment orderManageFragment = (OrderManageFragment) mFragments.get(R.id.nav_order_manage);
                    PendingOrderFragment pendingOrderFragment = (PendingOrderFragment) mFragments.get(R.id.nav_pending_order);

                    if (TextUtils.equals(sound, NEW_ORDER)) {
                        if (BusinessRepo.get().isAutomatic()) {
                            orderManageFragment.refreshNewOrder();
                        } else {
                            pendingOrderFragment.refreshNewOrder();
                        }
                    } else if (TextUtils.equals(sound, NEW_CANCEL)) {
                        pendingOrderFragment.refreshNewOrder();
                        orderManageFragment.refreshNewOrder();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class MyBroadcastReceiver extends BroadcastReceiver {
        public static final String TAG = "MyBroadcastReceiver";

        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "------------->onReceive");
//            ((OrdersListFragment) fragmentPagerAdapter.getItem(0)).wakeUpHandler();
        }
    }

    private void putFragments() {
        mFragments.put(R.id.nav_pending_order, new PendingOrderFragment());
        mFragments.put(R.id.nav_order_manage, new OrderManageFragment());
        mFragments.put(R.id.nav_operation, new OperationFragment());
        mFragments.put(R.id.nav_mine, new MineFragment());
    }

    private void setupNavigationView() {
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        BottomNavigationViewHelper.disableShiftMode(navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        navigation.setSelectedItemId(R.id.nav_pending_order);
    }

    private void commitFragment(@NonNull Fragment fragment) {
        Fragment preFragment = mFragments.get(preCheckedId);
        if (preFragment == fragment) {
            return;
        }

        FragmentManager fm = getFragmentManager();
        FragmentTransaction ft = fm.beginTransaction();

        if (preFragment != null) {
            ft.hide(preFragment);
        }

        String tag = fragment.getClass().getSimpleName();
        if (fragment.isAdded() || fm.findFragmentByTag(tag) != null) {
            ft.show(fragment);
        } else {
            ft.add(R.id.content, fragment, tag);
        }

        ft.commitAllowingStateLoss();
    }

//    private void registerReceiver() {
//        IntentFilter filter = new IntentFilter();
//        filter.setPriority(IntentFilter.SYSTEM_HIGH_PRIORITY);
//        filter.addAction(MESSAGE_RECEIVED_ACTION);
//        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, filter);
//    }

    private void requestPermissions() {
        new RxPermissions(this)
                .request(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .subscribe(hasPermission -> {
                    if (!hasPermission) {
                        showPermissionDialog("应用需要获取读写外部存储权限,请前往应用信息-权限中开启", false);
                    }
                });
//        new RxPermissions(this)
//                .request(Manifest.permission.READ_PHONE_STATE)
//                .subscribe(hasPermission -> {
//                    if (!hasPermission) {
//                        showPermissionDialog("应用需要获取读写设备信息权限,请前往应用信息-权限中开启", false);
//                    }
//                });

        if (!SystemUtils.isNotificationEnabled(this)) {
            showPermissionDialog("应用需要获取通知栏权限,请前往应用信息-权限中开启", true);
        }
    }

    private void toSetting() {
        Intent localIntent = new Intent();
        localIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        if (Build.VERSION.SDK_INT >= 9) {
            localIntent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
            localIntent.setData(Uri.fromParts("package", getPackageName(), null));
        } else if (Build.VERSION.SDK_INT <= 8) {
            localIntent.setAction(Intent.ACTION_VIEW);
            localIntent.setClassName("com.android.settings", "com.android.setting.InstalledAppDetails");
            localIntent.putExtra("com.android.settings.ApplicationPkgName", getPackageName());
        }
        startActivity(localIntent);
    }

    private void showPermissionDialog(String message, boolean isNotification) {
        AlertDialog dialog = new AlertDialog.Builder(this)
                .setTitle("权限申请")
                .setMessage(message)
                .setCancelable(false)
                .setNegativeButton("暂不", (dialogInterface, i) -> dialogInterface.dismiss())
                .setPositiveButton("去设置", (dialogInterface, which) -> {
                    if (isNotification) {
                        toSetting();
                    } else {
                        startActivity(new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS)
                                .setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID)));
                    }
                    dialogInterface.dismiss();
                    finish();
                })
                .show();
        dialog.setCanceledOnTouchOutside(false);
        Button negativeButton = dialog.getButton(BUTTON_NEGATIVE);
        if (negativeButton != null) {
            negativeButton.setTextColor(ContextCompat.getColor(this, R.color.gray19));
            negativeButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
        Button positiveButton = dialog.getButton(BUTTON_POSITIVE);
        if (positiveButton != null) {
            positiveButton.setTextColor(ContextCompat.getColor(this, R.color.colorAccent));
            positiveButton.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 12);
        }
    }

//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        registerReceiver();
//    }

    @Override
    protected void onDestroy() {
        unregisterReceiver(mBroadcastReceiver);
        super.onDestroy();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            exitApp();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void exitApp() {
        if (!isExit) {
            isExit = true;
            Toast.makeText(this, "再按一次返回退出程序", Toast.LENGTH_SHORT).show();
            Timer timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    isExit = false;
                }
            }, 2000);
        } else {
            // 获取自己的PID来结�?
            // android.os.Process.killProcess(android.os.Process.myPid());
            Intent intent = new Intent(Intent.ACTION_MAIN);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);// 注意
            intent.addCategory(Intent.CATEGORY_HOME);
            startActivity(intent);
//            finish();
        }
    }
}
