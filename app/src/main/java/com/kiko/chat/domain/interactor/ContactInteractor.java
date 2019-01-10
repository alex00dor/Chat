package com.kiko.chat.domain.interactor;

import com.kiko.chat.domain.event.ContactEvent;
import com.kiko.chat.domain.entity.Contact;
import com.kiko.chat.domain.repository.ContactRepository;

import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Flowable;
import io.reactivex.Single;

public class ContactInteractor {
    ContactRepository repository;

    public ContactInteractor(ContactRepository repository) {
        this.repository = repository;
    }

    public Completable addContact(String email){
        return repository.addContact(email);
    }

    public Completable removeContact(String email){
        return repository.removeContact(email);
    }

    public Flowable<ContactEvent> getContacts(){
        return repository.getContactsObservable();
    }

    public Single<List<Contact>> getCachedContacts(){ return repository.getCachedContacts(); }
}
