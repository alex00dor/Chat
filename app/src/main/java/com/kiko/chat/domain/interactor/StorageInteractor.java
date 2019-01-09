package com.kiko.chat.domain.interactor;

import android.net.Uri;

import com.kiko.chat.domain.repository.StorageRepository;
import io.reactivex.Single;

public class StorageInteractor {
    StorageRepository repository;

    public StorageInteractor(StorageRepository repository) {
        this.repository = repository;
    }

    public Single<String> loadImage(Uri uri){
        return repository.loadImage(uri);
    }
}
