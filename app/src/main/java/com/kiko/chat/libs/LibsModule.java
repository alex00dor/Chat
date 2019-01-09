package com.kiko.chat.libs;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.kiko.chat.libs.base.ImageLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LibsModule {

    @Singleton
    @Provides
    static ImageLoader provideGlideImageLoader(RequestManager manager){
        return new GlideImageLoader(manager);
    }


    @Singleton
    @Provides
    static RequestManager provideRequestManager(Context context){
        return Glide.with(context);
    }
}
