package com.kiko.chat.libs;

import android.widget.ImageView;

import com.kiko.chat.R;
import com.kiko.chat.libs.base.ImageLoader;
import com.squareup.picasso.Picasso;

public class PicassoImageLoader implements ImageLoader {

    Picasso picasso;

    public PicassoImageLoader(Picasso picasso) {
        this.picasso = picasso;
    }

    @Override
    public void loadImage(String url, ImageView imageView) {
        picasso.load(url)
                .placeholder(R.drawable.placeholder)
                .into(imageView);
    }

    @Override
    public void loadImage(String url, ImageView imageView, int width, int height) {
        picasso.load(url)
                .placeholder(R.drawable.placeholder)
                .resize(width, height)
                .into(imageView);
    }
}
