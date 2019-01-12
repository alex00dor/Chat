package com.kiko.chat.presentation.contactlist.ui;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnCreateContextMenuListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.kiko.chat.R;
import com.kiko.chat.domain.entity.Contact;
import com.kiko.chat.libs.base.ImageLoader;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

public class ContactListAdapter extends RecyclerView.Adapter<ContactListAdapter.ViewHolder> {

    private List<Contact> contacts;
    private OnItemClickListener clickListener;
    private ImageLoader imageLoader;

    public ContactListAdapter(List<Contact> contacts, OnItemClickListener clickListener, ImageLoader imageLoader) {
        this.contacts = contacts;
        this.clickListener = clickListener;
        this.imageLoader = imageLoader;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.fragment_contactlist_item, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        Contact contact = contacts.get(i);
        viewHolder.setClickListener(contact, clickListener);

        String nickname = contact.getNickName();
        String photoUrl = contact.getPhotoUrl();
        boolean online = contact.isOnline();
        String status = online ? "online" : "offline";
        int color = online ? Color.BLUE : Color.RED;

        if(nickname.equals("")){
            nickname = contact.getEmail();
        }

        if(!photoUrl.equals("")){
            imageLoader.loadImage(photoUrl, viewHolder.avatar);
        }

        viewHolder.txtUser.setText(nickname);
        viewHolder.txtStatus.setText(status);
        viewHolder.txtStatus.setTextColor(color);
        viewHolder.txtMessage.setSingleLine(true);
        viewHolder.txtMessage.setText(contact.getLastMessage());
    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    private int getPositionByUsername(String name) {
        for (int i = 0; i < getItemCount(); i++) {
            if (contacts.get(i).getEmail().equals(name)) {
                return i;
            }
        }

        return -1;
    }


    private void notifyDataChanged(List<Contact> oldContacts, List<Contact> newContacts) {
        sortDataSet();
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ContactDiffCallback(oldContacts, newContacts));
        diffResult.dispatchUpdatesTo(this);
    }

    public void setContacts(List<Contact> contacts){
        this.contacts = contacts;
        sortDataSet();
        notifyDataSetChanged();
    }

    public void add(Contact contact) {
        List<Contact> temp = new ArrayList<>(contacts);
        int pos = getPositionByUsername(contact.getEmail());
        if (pos == -1) {
            this.contacts.add(contact);
        }else{
            contacts.set(pos, contact);
        }
        notifyDataChanged(temp, contacts);
    }

    public void remove(Contact contact) {
        int pos = getPositionByUsername(contact.getEmail());
        List<Contact> temp = new ArrayList<>(contacts);
        contacts.remove(pos);
        notifyDataChanged(temp, contacts);
    }

    void sortDataSet(){
        contacts.sort((o1, o2) -> Long.compare(o2.getLastMessageTime(), o1.getLastMessageTime()));
    }

    class ViewHolder extends RecyclerView.ViewHolder implements OnCreateContextMenuListener {

        @BindView(R.id.txtUser)
        TextView txtUser;
        @BindView(R.id.txtStatus)
        TextView txtStatus;
        @BindView(R.id.txtMessage)
        TextView txtMessage;
        @BindView(R.id.cardView)
        CardView cardView;
        @BindView(R.id.avatar)
        CircleImageView avatar;

        OnItemClickListener listener;
        Contact contact;
        private View view;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.view = itemView;
            ButterKnife.bind(this, view);
            this.cardView.setOnCreateContextMenuListener(this);
        }

        void setClickListener(final Contact contact, final OnItemClickListener listener) {
            this.listener = listener;
            this.contact = contact;
            cardView.setOnClickListener(view -> listener.onItemClick(contact));
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.add(0, v.getId(), 0, "Remove");
            menu.findItem(v.getId()).setOnMenuItemClickListener(item -> {
                listener.onItemRemoveClick(contact);
                return false;
            });
        }

    }

    public interface OnItemClickListener {

        void onItemClick(Contact contact);

        void onItemRemoveClick(Contact contact);
    }
}
