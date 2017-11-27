package com.gxuc.runfast.business.extension.binding;

import android.content.Context;
import android.databinding.BindingAdapter;
import android.text.TextUtils;
import android.widget.ImageView;

import com.gxuc.runfast.business.R;
import com.gxuc.runfast.business.extension.glide.CropCircleTransformation;
import com.gxuc.runfast.business.extension.glide.GlideApp;
import com.gxuc.runfast.business.extension.glide.RoundedCornersTransformation;

public class ImageViewBindings {

    @BindingAdapter("circleImageUrl")
    public static void setCircleImageUrl(ImageView view, String url) {
        GlideApp.with(view.getContext())
                .load(url)
                .error(R.drawable.error_image)
                .transform(new CropCircleTransformation())
                .into(view);
    }

    @BindingAdapter({ "roundImageUrl", "roundRadius" })
    public static void setRoundImageUrl(ImageView view, String url, float radius) {
        Context context = view.getContext();
        GlideApp.with(context)
                .load(url)
                .error(R.drawable.error_image)
                .transform(new RoundedCornersTransformation(context, (int) radius, 0))
                .into(view);
    }

    @BindingAdapter(value = { "imageUrl", "defaultImage" }, requireAll = false)
    public static void setImageUrl(ImageView view, String url, boolean needDefaultImage) {
        if (needDefaultImage && TextUtils.isEmpty(url)) {
            view.setImageResource(R.drawable.shop_info_add_logo);
            return;
        }
        GlideApp.with(view.getContext())
                .load(url)
                .error(R.drawable.error_image)
                .into(view);
    }
}
