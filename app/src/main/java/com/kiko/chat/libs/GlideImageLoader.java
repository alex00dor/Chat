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
        RequestOptions options = new RequestOptions();
        load(url, imageView, options);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int width, int height) {
        RequestOptions options = new RequestOptions();
        options = options.override(width, height);
        load(url, imageView, options);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int size) {
        RequestOptions options = new RequestOptions();
        options = options.override(size);
        load(url, imageView, options);
    }

    private void load(String url, ImageView view, RequestOptions options){
        manager.load(url)
                .apply(RequestOptions.diskCacheStrategyOf(DiskCacheStrategy.ALL))
                .transition(DrawableTransitionOptions.withCrossFade())
                .thumbnail((float) 0.15)
                .apply(RequestOptions.placeholderOf(R.drawable.spinner))
                .apply(options)
                .into(view);
    }
}
