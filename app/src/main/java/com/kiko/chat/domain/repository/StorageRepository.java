package com.kiko.chat.domain.repository;

import android.net.Uri;

import io.reactivex.Single;

public interface StorageRepository {
    Single<String> loadImage(Uri uri);
}
