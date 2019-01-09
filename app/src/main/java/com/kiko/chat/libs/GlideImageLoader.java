package com.kiko.chat.libs;

import android.widget.ImageView;

import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.bumptech.glide.request.RequestOptions;
import com.kiko.chat.R;
import com.kiko.chat.libs.base.ImageLoader;

public class GlideImageLoader implements ImageLoader {

    RequestManager manager;

    public GlideImageLoader(RequestManager manager) {
        this.manager = manager;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        manager.load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail((float) 0.15)
                .apply(RequestOptions.placeholderOf(R.drawable.spinner))
                .apply(RequestOptions.overrideOf(1000))
                .into(imageView);
    }
}
