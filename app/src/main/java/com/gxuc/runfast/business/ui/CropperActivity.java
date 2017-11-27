package com.gxuc.runfast.business.ui;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.extension.LayoutProvider;
import com.gxuc.runfast.business.extension.WithMenu;
import com.gxuc.runfast.business.extension.WithToolbar;
import com.gxuc.runfast.business.ui.base.BaseActivity;
import com.steelkiwi.cropiwa.CropIwaView;
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig;

import java.io.File;

/**
 * 裁剪
 * Created by Berial on 2017/9/11.
 */
public class CropperActivity extends BaseActivity
        implements WithToolbar, WithMenu, LayoutProvider {

    private CropIwaView mCropView;

    @Override
    public int thisLayoutId() {
        return R.layout.activity_cropper;
    }

    @Override
    public String thisTitle() {
        return "裁剪图片";
    }

    @Override
    public int thisMenu() {
        return R.menu.menu_select_goods;
    }

    @Override
    public Toolbar.OnMenuItemClickListener thisOnMenuItemClickListener() {
        return item -> {
            mCropView.crop(new CropIwaSaveConfig.Builder(newFileUri())
                    .setCompressFormat(Bitmap.CompressFormat.PNG)
                    .setQuality(100)
                    .build());
            return true;
        };
    }

    @Override
    protected void onInitViews() {
        Uri imageUri = getIntent().getParcelableExtra("uri");

        mCropView = (CropIwaView) findViewById(R.id.crop_view);

        mCropView.setImageUri(imageUri);

        mCropView.setCropSaveCompleteListener(uri -> {
            Intent intent = new Intent();
            intent.putExtra("cropUri", uri);
            setResult(RESULT_OK, intent);
            onBackPressed();
        });

        mCropView.setErrorListener(e -> Toast.makeText(this, "裁剪失败", Toast.LENGTH_SHORT).show());
    }

    private Uri newFileUri() {
        return Uri.fromFile(new File(getFilesDir(), System.currentTimeMillis() + ".png"));
    }
}
