package com.kiko.chat.datasource.network.firestore;

import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.Source;
import com.kiko.chat.datasource.network.FirebaseHelper;
import com.kiko.chat.domain.event.ContactEvent;
import com.kiko.chat.domain.model.Contact;
import com.kiko.chat.domain.repository.ContactRepository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class FirestoreContactSource implements ContactRepository {

    private FirebaseHelper helper;
    private Flowable<ContactEvent> contactsObservable;

    public FirestoreContactSource(FirebaseHelper helper) {
        this.helper = helper;
    }

    @Override
    public Completable addContact(String email) {
        return Completable.create(emitter -> {
            helper.getUserDocument(email).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Map<String, Object> data = new HashMap<>();
                        data.put("lastMessage", "");
                        data.put("lastTimeMessage", 0);
                        data.put("online", false);
                        helper.getCurrentUserContactCollection()
                                .document(email)
                                .set(data)
                                .addOnCompleteListener(t -> emitter.onComplete())
                                .addOnFailureListener(emitter::onError);
                    } else {
                        emitter.onError(new Throwable("Can't find this email."));
                    }
                } else {
                    emitter.onError(task.getException());
                }
            });
        });
    }

    @Override
    public Completable removeContact(String email) {
        return Completable.create(emitter -> helper.getCurrentUserContactCollection()
                .document(email).delete()
                .addOnCompleteListener(task -> emitter.onComplete())
                .addOnFailureListener(emitter::onError));
    }

    @Override
    public Single<List<Contact>> getCachedContacts(){
        return Single.create(emitter -> helper.getCurrentUserContactCollection()
                .get(Source.CACHE)
                .addOnCompleteListener(task -> {
                    List<DocumentSnapshot> documentSnapshotList = Objects.requireNonNull(task.getResult()).getDocuments();
                    List<Contact> result = new ArrayList<>();
                    for(DocumentSnapshot doc: documentSnapshotList){
                        Contact contact = new Contact(doc.getId(), false);
                        contact.setLastMessage(doc.getString("lastMessage"));
                        if(doc.getLong("lastTimeMessage") != null) {
                            contact.setLastMessageTime(doc.getLong("lastTimeMessage"));
                        }
                        result.add(contact);
                    }
                    emitter.onSuccess(result);
                })
                .addOnFailureListener(emitter::onError));
    }

    @Override
    public Flowable<ContactEvent> getContactsObservable() {
        if (contactsObservable == null) {
            contactsObservable = Flowable.create(emitter -> {
                ListenerRegistration listener = helper.getCurrentUserContactCollection().addSnapshotListener((snapshots, e) -> {
                    if (e != null) {
                        emitter.onError(e);
                        return;
                    }

                    for (DocumentChange dc : snapshots.getDocumentChanges()) {
                        QueryDocumentSnapshot document = dc.getDocument();
                        switch (dc.getType()) {
                            case ADDED:
                                if(document.getBoolean("online") == null)
                                    continue;
                                Contact contact = new Contact(document.getId(), document.getBoolean("online"));
                                contact.setLastMessage(document.getString("lastMessage"));
                                if(document.getLong("lastTimeMessage") != null) {
                                    contact.setLastMessageTime(document.getLong("lastTimeMessage"));
                                }
                                emitter.onNext(new ContactEvent(contact, ContactEvent.ACTION_CONTACT_ADD));
                                break;
                            case MODIFIED:
                                if(document.getBoolean("online") == null)
                                    continue;
                                Contact contactModified = new Contact(document.getId(), document.getBoolean("online"));
                                contactModified.setLastMessage(document.getString("lastMessage"));
                                if(document.getLong("lastTimeMessage") != null) {
                                    contactModified.setLastMessageTime(document.getLong("lastTimeMessage"));
                                }
                                emitter.onNext(new ContactEvent(contactModified, ContactEvent.ACTION_CONTACT_CHANGED));
                                break;
                            case REMOVED:
                                Contact remove = new Contact(document.getId(), false);
                                emitter.onNext(new ContactEvent(remove, ContactEvent.ACTION_CONTACT_DELETED));
                                break;
                        }
                    }

                });
                emitter.setCancellable(listener::remove);

            }, BackpressureStrategy.BUFFER);
        }
        return contactsObservable;
    }
}
