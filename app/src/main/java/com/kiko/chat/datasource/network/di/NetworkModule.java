package com.kiko.chat.datasource.network.di;

import android.content.Context;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;
import com.kiko.chat.datasource.network.FirebaseAuthSource;
import com.kiko.chat.datasource.network.FirebaseHelper;
import com.kiko.chat.datasource.network.FirebaseMessagingService;
import com.kiko.chat.datasource.network.FirebaseStorageDataSource;
import com.kiko.chat.datasource.network.firestore.FirestoreChatSource;
import com.kiko.chat.datasource.network.firestore.FirestoreContactSource;
import com.kiko.chat.di.scope.PerService;
import com.kiko.chat.domain.repository.AuthorizationRepository;
import com.kiko.chat.domain.repository.ChatRepository;
import com.kiko.chat.domain.repository.ContactRepository;
import com.kiko.chat.domain.repository.StorageRepository;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import dagger.android.ContributesAndroidInjector;

@Module
public abstract class NetworkModule {

    @PerService
    @ContributesAndroidInjector()
    abstract FirebaseMessagingService bindFirebaseMessagingService();

    @Provides
    @Singleton
    static FirebaseMessagingService provideFirebaseMessagingService() {
        return new FirebaseMessagingService();
    }

    @Provides
    @Singleton
    static AuthorizationRepository provideAuthorizationRepository(FirebaseAuth firebaseAuth, FirebaseHelper helper, Context context) {
        return new FirebaseAuthSource(firebaseAuth, helper, context);
    }

    @Provides
    @Singleton
    static ContactRepository provideContactRepository(FirebaseHelper helper) {
        return new FirestoreContactSource(helper);
    }

    @Provides
    @Singleton
    static ChatRepository provideChatRepository(FirebaseHelper helper) {
        return new FirestoreChatSource(helper);
    }

    @Provides
    @Singleton
    static FirebaseAuth provideFirebaseAuth() {
        return FirebaseAuth.getInstance();
    }

    @Provides
    @Singleton
    static FirebaseHelper provideFirebaseHelper(FirebaseFirestore firestore, FirebaseDatabase database, FirebaseStorage storage) {
        return new FirebaseHelper(firestore, database, storage);
    }

    @Named("auth_email")
    @Provides
    static String provideAuthEmail(FirebaseHelper helper) {
        return helper.getAuthUserEmail();
    }

    @Provides
    @Singleton
    static FirebaseDatabase provideFirebaseDatabase() {
        return FirebaseDatabase.getInstance();
    }


    @Singleton
    @Provides
    static StorageRepository provideFirebaseStorageDataSource(FirebaseHelper helper){
        return new FirebaseStorageDataSource(helper);
    }

    @Provides
    @Singleton
    static FirebaseStorage provideFirebaseStorage(){
        return FirebaseStorage.getInstance();
    }

    @Singleton
    @Provides
    static FirebaseFirestore provideFirestore(FirebaseAuth auth) {
        FirebaseFirestore firestore = FirebaseFirestore.getInstance();
        FirebaseFirestoreSettings.Builder builder = new FirebaseFirestoreSettings.Builder();
        builder.setPersistenceEnabled(true);

        firestore.setFirestoreSettings(builder.build());

        return firestore;
    }
}
