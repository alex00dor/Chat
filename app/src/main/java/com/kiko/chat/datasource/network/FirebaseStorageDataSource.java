package com.kiko.chat.datasource.network;

import android.net.Uri;

import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.kiko.chat.domain.repository.StorageRepository;

import java.util.Objects;

import io.reactivex.Single;

public class FirebaseStorageDataSource implements StorageRepository {

    FirebaseHelper helper;

    public FirebaseStorageDataSource(FirebaseHelper helper) {
        this.helper = helper;
    }

    @Override
    public Single<String> loadImage(Uri uri) {
        return Single.create(emitter -> {
            final StorageReference ref = helper.getNewImageReference();
            StorageMetadata metadata = new StorageMetadata.Builder()
                    .setContentType("image/jpg")
                    .build();
            UploadTask uploadTask = ref.putFile(uri, metadata);
            uploadTask.continueWithTask(task -> {
                if (!task.isSuccessful()) {
                    throw Objects.requireNonNull(task.getException());
                }

                return ref.getDownloadUrl();
            }).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    emitter.onSuccess(Objects.requireNonNull(task.getResult()).toString());
                } else {
                    emitter.onError(task.getException());
                }
            });

            emitter.setCancellable(uploadTask::cancel);

        });
    }
}
