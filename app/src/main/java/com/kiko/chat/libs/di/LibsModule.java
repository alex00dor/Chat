package com.kiko.chat.libs.di;

import com.kiko.chat.libs.PicassoImageLoader;
import com.kiko.chat.libs.base.ImageLoader;
import com.squareup.picasso.Picasso;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class LibsModule {

    @Singleton
    @Provides
    static ImageLoader providePicassoImageLoader(Picasso picasso){
        return new PicassoImageLoader(picasso);
    }

    @Singleton
    @Provides
    static Picasso providePiccaso(){
        return Picasso.get();
    }
}
