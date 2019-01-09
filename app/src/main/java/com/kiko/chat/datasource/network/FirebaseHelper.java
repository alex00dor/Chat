package com.kiko.chat.datasource.network;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kiko.chat.domain.util.RandomUtils;

public class FirebaseHelper {
    private FirebaseFirestore firestore;
    private FirebaseDatabase firebaseDatabase;
    private FirebaseStorage firebaseStorage;
    private final static String SEPARATOR = "___";
    private final static String CHATS_PATH = "chats/%s/messages";
    private final static String USER_PATH = "users/%s";
    private final static String CONTACTS_PATH = "users/%s/contacts";
    private final static String CONNECTION_PATH = ".info/connected";
    private final static String STATUS_PATH = "status/%s";
    private final static String IMAGE_PATH = "users/%s/%s.jpg";

    public FirebaseHelper(FirebaseFirestore firestore, FirebaseDatabase firebaseDatabase, FirebaseStorage storage){
        this.firestore = firestore;
        this.firebaseDatabase = firebaseDatabase;
        this.firebaseStorage = storage;
    }

    public StorageReference getNewImageReference(){
        String userId = getAuthUserUID();
        String imageName = RandomUtils.getRandomNameOfFile(20);
        return firebaseStorage.getReference(String.format(IMAGE_PATH, userId, imageName));
    }

    public DatabaseReference getConnectionReference(){
        return firebaseDatabase.getReference(CONNECTION_PATH);
    }

    public DatabaseReference getStatusReference(){
        return firebaseDatabase.getReference(String.format(STATUS_PATH, getAuthUserUID()));
    }

    public FirebaseFirestore getFirestore() {
        return firestore;
    }

    public String getAuthUserEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String email = null;
        if (user != null) {
            email = user.getEmail();
        }
        return email;
    }

    public String getAuthUserUID(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        String id = null;
        if (user != null) {
            id = user.getUid();
        }
        return id;
    }

    public DocumentReference getUserDocument(String email){
        DocumentReference userDocument = null;
        if (email != null) {
            userDocument = firestore.document(String.format(USER_PATH, email));
        }
        return userDocument;
    }

    public DocumentReference getCurrentUserDocument() {
        return getUserDocument(getAuthUserEmail());
    }

    public CollectionReference getContactCollection(String email){
        CollectionReference collectionReference = null;
        if(email != null)
            collectionReference = firestore.collection(String.format(CONTACTS_PATH, email));
        return collectionReference;
    }

    public CollectionReference getCurrentUserContactCollection(){
        return getContactCollection(getAuthUserEmail());
    }

    public CollectionReference getChatReference(String receiver, String sender){

        String keyChat = sender + SEPARATOR + receiver;
        if (sender.compareTo(receiver) > 0) {
            keyChat = receiver + SEPARATOR + sender;
        }
        return firestore.collection(String.format(CHATS_PATH, keyChat));
    }
}
