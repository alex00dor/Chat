package com.kiko.chat.datasource.network;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;
import com.kiko.chat.domain.repository.AuthorizationRepository;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Completable;

public class FirebaseAuthSource implements AuthorizationRepository {

    private FirebaseAuth firebaseAuth;
    private FirebaseHelper helper;
    private Context context;

    public FirebaseAuthSource(FirebaseAuth firebaseAuth, FirebaseHelper helper, Context context) {
        this.firebaseAuth = firebaseAuth;
        this.helper = helper;
        this.context = context;
    }

    @Override
    public Completable login(final String username, final String password) {
        return Completable.create(
                emitter -> firebaseAuth.signInWithEmailAndPassword(username, password)
                        .addOnSuccessListener(authResult -> {
                            FirebaseMessaging.getInstance().setAutoInitEnabled(true);
                            FirebaseInstanceId.getInstance().getInstanceId()
                                    .addOnSuccessListener(instanceIdResult -> sendNotificationKey(instanceIdResult.getToken()).subscribe());
                            emitter.onComplete();
                        })
                        .addOnFailureListener(e -> emitter.onError(new Throwable(e.getLocalizedMessage()))));
    }

    @Override
    public Completable newUser(final String username, final String password) {
        return Completable.create(
                emitter -> firebaseAuth.createUserWithEmailAndPassword(username, password)
                        .addOnSuccessListener(authResult -> emitter.onComplete())
                        .addOnFailureListener(e -> emitter.onError(new Throwable(e.getLocalizedMessage()))));
    }

    @Override
    public Completable checkCurrentSession() {
        return Completable.create(e -> {
            if (firebaseAuth.getCurrentUser() != null)
                e.onComplete();
            else
                e.onError(new Throwable("Cannot find any session"));
        });
    }

    @Override
    public Completable logout() {
        return Completable.create(e -> {
            FirebaseMessaging.getInstance().setAutoInitEnabled(false);
            sendNotificationKey("").subscribe();
            firebaseAuth.signOut();
            if (firebaseAuth.getCurrentUser() == null)
                e.onComplete();
            else
                e.onError(new Throwable("Cannot make logout"));
        });
    }

    @Override
    public Completable sendNotificationKey(String key) {
        return Completable.create(emitter -> {
            if(helper.getAuthUserEmail() != null) {
                Map<String, Object> data = new HashMap<>();
                data.put("nToken", key);
                helper.getCurrentUserDocument().set(data, SetOptions.merge())
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError);
            }else{
                emitter.onError(new Throwable("No current auth user"));
            }
        });
    }

    @Override
    public Completable updateUserInfo(String nickname, String photoUrl) {
        return Completable.create(emitter -> {

            if(nickname == null){
                emitter.onError(new Throwable("Nickname cannot be empty."));
                return;
            }

            if(photoUrl == null){
                emitter.onError(new Throwable("Select photo, please."));
                return;
            }

            if(helper.getAuthUserEmail() != null){
                Map<String, Object> data = new HashMap<>();
                data.put("nickname", nickname);
                data.put("photoUrl", photoUrl);
                helper.getCurrentUserDocument().set(data, SetOptions.merge())
                        .addOnCompleteListener(task -> emitter.onComplete())
                        .addOnFailureListener(emitter::onError);
            }else{
                emitter.onError(new Throwable("No current auth user"));
            }
        });
    }

    @Override
    public void initConnection() {
        helper.getConnectionReference().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                boolean connected = dataSnapshot.getValue(Boolean.class);
                if (connected && helper.getAuthUserEmail() != null) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("email", helper.getAuthUserEmail());
                    data.put("online", true);
                    helper.getStatusReference().setValue(data);
                }

                Map<String, Object> data = new HashMap<>();
                data.put("email", helper.getAuthUserEmail());
                data.put("online", false);
                helper.getStatusReference().onDisconnect().setValue(data);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }


}
