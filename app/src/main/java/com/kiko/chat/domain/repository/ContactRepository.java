package com.kiko.chat.domain.repository;

import com.kiko.chat.domain.event.ContactEvent;
import com.kiko.chat.domain.model.Contact;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public interface ContactRepository {
    Completable addContact(String email);
    Completable removeContact(String email);
    Flowable<ContactEvent> getContactsObservable();
    Single<List<Contact>> getCachedContacts();
}
