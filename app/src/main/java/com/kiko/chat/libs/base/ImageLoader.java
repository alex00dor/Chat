package com.kiko.chat.libs.base;

import android.widget.ImageView;

public interface ImageLoader {
    void loadImage(String url, ImageView imageView);
    void loadImage(String url, ImageView imageView, int width, int height);
}
