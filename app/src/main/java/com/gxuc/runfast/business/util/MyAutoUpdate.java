package com.gxuc.runfast.business.util;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.FileProvider;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;


import com.gxuc.runfast.business.R;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class MyAutoUpdate {
	
	private Activity mContext;
	// 提示语
	private String updateMsg = "有最新的软件包哦，亲快下载吧~";

	// 返回的安装包url
//	private String apkUrl = "http://121.15.220.150/dd.myapp.com/16891/40836342DD941D45EAD1A696BB4246FC.apk?mkey=558cc303d06518cf&f=d488&fsname=com.beautymiracle.androidclient_1.2.7_20150605.apk&asr=8eff&p=.apk";
	private String apkUrl = "http://image.gxptkc.com/apk/runfastbiz.apk";

	private Dialog noticeDialog;
	private Dialog downloadDialog;
	/* 下载包安装路径 */

	private final String saveFileName = Comm.doFolder+ "UpdateRelease.apk";

	/* 进度条与通知ui刷新的handler和msg常量 */
	private ProgressBar mProgress;
	private TextView mTextProgress;
	private StringBuffer mStringBuffer;

	private final int DOWN_UPDATE = 1;

	private final int DOWN_OVER = 2;

	private int progress;

	private Thread downLoadThread;

	private boolean interceptFlag = false;

	private Handler mHandler = new Handler() {
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWN_UPDATE:
				mProgress.setProgress(progress);
				mTextProgress.setText(mStringBuffer.toString());
				break;
			case DOWN_OVER:
				downloadDialog.dismiss();
				installApk();
				break;
			default:
				break;
			}
		};
	};

	public MyAutoUpdate(Activity context) {
		this.mContext = context;
	}

	// 外部接口让主Activity调用
	/**
	 * 
	 * author chanson
	 * creatTime 2015-6-26 下午4:18:54 
	 * @param downloadUrl apk更新链接
	 *
	 */
	public void showDownloadDialog(String downloadUrl) {
		this.apkUrl = downloadUrl;
//		AlertDialog.Builder builder = new Builder(mContext);
		downloadDialog = new Dialog(mContext, R.style.Theme_dialog);
//		downloadDialog.setTitle("软件版本更新");
		final LayoutInflater inflater = LayoutInflater.from(mContext);
		View v = inflater.inflate(R.layout.progress, null);
		mProgress = (ProgressBar) v.findViewById(R.id.progress);
		mTextProgress = (TextView) v.findViewById(R.id.text_progress);
		mStringBuffer = new StringBuffer();

		downloadDialog.setContentView(v);
//		if (showType != 1) {
//			builder.setNegativeButton("取消", new OnClickListener() {
//				@Override
//				public void onClick(DialogInterface dialog, int which) {
//					dialog.dismiss();
//					interceptFlag = true;
//				}
//			});
//			downloadDialog.setCancelable(true);
//		} else {
//			downloadDialog.setCancelable(false);
//		}
		downloadDialog.setCancelable(false);
//		downloadDialog = builder.create();
		downloadDialog.show();

		downloadApk();
	}

	private Runnable mdownApkRunnable = new Runnable() {
		@Override
		public void run() {
			try {
				URL url = new URL(apkUrl);

				HttpURLConnection conn = (HttpURLConnection) url
						.openConnection();
				conn.connect();
				int length = conn.getContentLength();
				InputStream is = conn.getInputStream();

				File file = new File(Comm.doFolder);
				if (!file.exists()) {
					file.mkdir();
				}
				String apkFile = saveFileName;
				File ApkFile = new File(apkFile);
				FileOutputStream fos = new FileOutputStream(ApkFile);

				int count = 0;
				byte buf[] = new byte[1024];

				do {
					int numread = is.read(buf);
					count += numread;
					progress = (int) (((float) count / length) * 100);
					mStringBuffer.setLength(0);
					mStringBuffer.append(count / 1024).append("k/").append(length / 1024).append("k");
					// 更新进度
					mHandler.sendEmptyMessage(DOWN_UPDATE);
					if (numread <= 0) {
						// 下载完成通知安装
						mHandler.sendEmptyMessage(DOWN_OVER);
						break;
					}
					fos.write(buf, 0, numread);
				} while (!interceptFlag);// 点击取消就停止下载.

				fos.close();
				is.close();
			} catch (MalformedURLException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	};

	/**
	 * 下载apk
	 * 
	 */

	private void downloadApk() {
		downLoadThread = new Thread(mdownApkRunnable);
		downLoadThread.start();
	}

	/**
	 * 安装apk
	 * 
	 */
	private void installApk() {
		File apkfile = new File(saveFileName);
		if (!apkfile.exists()) {
			return;
		}
		Intent intent = new Intent(Intent.ACTION_VIEW);

		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
			intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
			Uri contentUri = FileProvider.getUriForFile(mContext, "com.gxuc.runfast.business.fileprovider",apkfile);
			intent.setDataAndType(contentUri, "application/vnd.android.package-archive");
		} else {
			intent.setDataAndType(Uri.fromFile(apkfile), "application/vnd.android.package-archive");
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		}

		mContext.startActivity(intent);
		mContext.finish();

	}

}
