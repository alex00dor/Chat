package com.kiko.chat.domain.event;

import com.kiko.chat.domain.entity.Contact;

public class ContactEvent {
    private Contact contact;
    private int type;
    public final static int ACTION_CONTACT_ADD = 0;
    public final static int ACTION_CONTACT_CHANGED = 1;
    public final static int ACTION_CONTACT_DELETED = 2;

    public ContactEvent() {
    }

    public ContactEvent(Contact contact, int type) {
        this.contact = contact;
        this.type = type;
    }

    public Contact getContact() {
        return contact;
    }

    public void setContact(Contact contact) {
        this.contact = contact;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
