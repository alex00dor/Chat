package com.kiko.chat.datasource.network.firestore;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.MetadataChanges;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.firestore.Source;
import com.google.firebase.firestore.WriteBatch;
import com.kiko.chat.datasource.network.FirebaseHelper;
import com.kiko.chat.domain.entity.Message;
import com.kiko.chat.domain.repository.ChatRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class FirestoreChatSource implements ChatRepository {

    private FirebaseHelper helper;

    public FirestoreChatSource(FirebaseHelper helper) {
        this.helper = helper;
    }


    @Override
    public Completable sendMessage(String msg, String receiver, String sender, boolean isImage) {
        return Completable.create(emitter -> {
            if (msg.trim().isEmpty()) {
                emitter.onError(new Throwable("Message cannot be empty."));
                return;
            }

            Message message = new Message();
            message.setMassage(msg);
            message.setRead(false);
            message.setReceiver(receiver);
            message.setImage(isImage);
            message.setSender(sender);
            message.setTimestamp(new Date().getTime());

            helper.getChatReference(receiver, sender)
                    .add(message.toMap())
                    .addOnCompleteListener(task -> emitter.onComplete())
                    .addOnFailureListener(emitter::onError);
        });
    }

    @Override
    public Completable setReadMessages(String receiver, String sender) {
        return Completable.create(emitter -> {
            Map<String, Object> data = new HashMap<>();
            data.put("read", true);
            helper.getChatReference(receiver, sender)
                    .whereEqualTo("sender", receiver)
                    .whereEqualTo("read", false)
                    .get(Source.SERVER)
                    .addOnCompleteListener(task -> {
                        List<DocumentSnapshot> documentSnapshotList = Objects.requireNonNull(task.getResult()).getDocuments();
                        WriteBatch batch = helper.getFirestore().batch();
                        for (DocumentSnapshot dc : documentSnapshotList) {
                            DocumentReference msgRef = helper.getChatReference(receiver, sender).document(dc.getId());
                            batch.set(msgRef, data, SetOptions.merge());
                        }
                        if(documentSnapshotList.size() > 0) {
                            batch.commit().addOnCompleteListener(t -> emitter.onComplete())
                                    .addOnFailureListener(emitter::onError);
                        }else{
                            emitter.onComplete();
                        }
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }


    @Override
    public Flowable<Message> subscribeOnChat(String receiver, String sender) {
        return Flowable.create(emitter -> {
                ListenerRegistration listener = helper.getChatReference(receiver, sender)
                        .orderBy("timestamp", Query.Direction.DESCENDING)
                        .addSnapshotListener(MetadataChanges.INCLUDE, (snapshots, e) -> {
                            if (e != null) {
                                emitter.onError(e);
                                return;
                            }
                            for (QueryDocumentSnapshot doc : snapshots) {
                                if(doc.getMetadata().hasPendingWrites() || !doc.getMetadata().isFromCache()) {
                                    Message message = new Message();
                                    message.setuID(doc.getId());
                                    message.setMassage(doc.getString("message"));
                                    message.setSender(doc.getString("sender"));
                                    message.setReceiver(doc.getString("receiver"));
                                    message.setImage(doc.getBoolean("image"));
                                    message.setRead(doc.getBoolean("read"));
                                    message.setTimestamp(doc.getLong("timestamp"));
                                    message.setSent(!doc.getMetadata().hasPendingWrites());
                                    message.setSendByCurrentUser(sender.equals(message.getSender()));
                                    emitter.onNext(message);
                                }
                            }

                        });

                emitter.setCancellable(listener::remove);
            }, BackpressureStrategy.BUFFER);
    }

    @Override
    public Single<List<Message>> getCachedMessages(String receiver, String sender) {
        return Single.create(emitter -> {
            helper.getChatReference(receiver, sender)
                    .orderBy("timestamp", Query.Direction.DESCENDING).get(Source.CACHE)
                    .addOnCompleteListener(task -> {
                        List<DocumentSnapshot> documentSnapshotList = Objects.requireNonNull(task.getResult()).getDocuments();
                        List<Message> result = new ArrayList<>();
                        for (DocumentSnapshot doc : documentSnapshotList) {
                            Message message = new Message();
                            message.setuID(doc.getId());
                            message.setMassage(doc.getString("message"));
                            message.setSender(doc.getString("sender"));
                            message.setReceiver(doc.getString("receiver"));
                            message.setRead(doc.getBoolean("read"));
                            message.setImage(doc.getBoolean("image"));
                            message.setTimestamp(doc.getLong("timestamp"));
                            message.setSent(!doc.getMetadata().hasPendingWrites());
                            message.setSendByCurrentUser(sender.equals(message.getSender()));
                            result.add(message);
                        }
                        emitter.onSuccess(result);
                    })
                    .addOnFailureListener(emitter::onError);
        });
    }
}
