package com.yyy.fuzhuangpad.util;

import android.view.View;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.yyy.fuzhuangpad.R;

/**
 * 图片加载框架封装类
 */
public class ImageLoaderUtil {
    public static void loadDrawableImg(ImageView v, Integer id) {
        Glide.with(v.getContext()).load(id)
                .apply(new RequestOptions()
                        .centerCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(v);
    }

    public static void loadImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .centerInside()
                        .placeholder(R.mipmap.default_style)
                        .error(R.mipmap.default_style)
                        .diskCacheStrategy(DiskCacheStrategy.ALL))

                .into(v);
    }

    public static void loadImg(ImageView v, String url, @DrawableRes int placeholder) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .placeholder(placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.default_style))
                .into(v);
    }

    public static void loadGifImg(ImageView v, String url) {
        Glide.with(v.getContext())
                .asBitmap()
                .load(url)
                .apply(new RequestOptions()
                        .fitCenter()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
//                .placeholder(R.drawable.head_portrait)
                .into(v);
    }


    public static void loadCircleImg(ImageView v, String url, @DrawableRes int placeholder) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .placeholder(placeholder)
                        .optionalCircleCrop()
                        .diskCacheStrategy(DiskCacheStrategy.ALL))
                .into(v);
    }

    public static void loadCircleImg(ImageView v, int url, @DrawableRes int placeholder) {
        Glide.with(v.getContext())
                .load(url)
                .apply(new RequestOptions()
                        .circleCrop()
                        .autoClone()
                        .placeholder(placeholder)
                        .diskCacheStrategy(DiskCacheStrategy.ALL)
                        .error(R.mipmap.default_style))
                .into(v);
    }
//
//    public static void loadRoundImg(ImageView v, String url, @DrawableRes int placeholder) {
//        Glide.with(v.getContext())
//                .load(url)
//                .transform(new GlideRoundTransform(v.getContext()))
//                .placeholder(placeholder)
//                .into(v);
//    }

    public static void loadImgFillCenter(ImageView v, String localPath) {
        Glide.with(v.getContext())
                .load(localPath)
                .apply(new RequestOptions()
                        .centerCrop())
                .into(v);
    }

    public static void loadAdapterImg(ImageView v, String url, final View itemView) {
        Glide.with(v.getContext())
                .load(url).apply(new RequestOptions()
                .centerCrop()
                .diskCacheStrategy(DiskCacheStrategy.ALL))
//                .placeholder(R.drawable.placeholder_image)
                .into(v)
                .getSize(new SizeReadyCallback() {

                    @Override
                    public void onSizeReady(int width, int height) {

                        if (!itemView.isShown()) {
                            itemView.setVisibility(View.VISIBLE);
                        }
                    }
                });
    }


}