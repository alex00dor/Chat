package com.kiko.chat.presentation.contactlist.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.kiko.chat.R;
import com.kiko.chat.domain.model.Contact;
import com.kiko.chat.presentation.addcontact.ui.AddContactDialogFragment;
import com.kiko.chat.presentation.base.BaseFragmentWithToolbar;
import com.kiko.chat.presentation.contactlist.ContactListPresenter;
import com.kiko.chat.presentation.main.ui.MainActivity;
import com.kiko.chat.presentation.util.FragmentToolbar;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ContactListFragment extends BaseFragmentWithToolbar implements ContactListPresenter.View, ContactListAdapter.OnItemClickListener {

    MainActivity activity;

    @Inject
    ContactListAdapter adapter;

    @Inject
    ContactListPresenter presenter;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;

    Unbinder unbinder;


    public static ContactListFragment newInstance() {
        Bundle args = new Bundle();
        ContactListFragment fragment = new ContactListFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contactlist, container, false);
        getLifecycle().addObserver((LifecycleObserver) presenter);
        unbinder = ButterKnife.bind(this, view);
        setupRecyclerView();
        setupActivity();
        return view;
    }

    private void setupActivity() {
        activity = (MainActivity) getActivity();
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
    }

    @Override
    public void setContactDataSet(List<Contact> contactList) {
        adapter.setContacts(contactList);
    }

    @Override
    public void onContactAdded(Contact contact) {
        adapter.add(contact);
    }

    @Override
    public void onContactChanged(Contact contact) {
        adapter.add(contact);
    }

    @Override
    public void onContactRemoved(Contact contact) {
        adapter.remove(contact);
    }

    @OnClick(R.id.fab)
    public void addContact() {
        if (getFragmentManager() != null)
            AddContactDialogFragment.newInstance().show(getFragmentManager(), "");
    }

    @Override
    public void showProgress() {
        activity.showProgress();
    }

    @Override
    public void hideProgress() {
        activity.hideProgress();
    }

    @Override
    public void showError(String message) {

    }

    @Override
    public void onItemClick(Contact contact) {
        activity.navigateToChat(contact.getEmail(), true);
    }

    @Override
    public void onItemRemoveClick(Contact contact) {
        showComfirmDialog(contact);
    }

    void showComfirmDialog(Contact contact) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(getContext());
        dialog.setMessage(R.string.contactlist_remove_message)
                .setPositiveButton("Yes", (dialog12, which) -> {
                    presenter.removeContact(contact.getEmail());
                    dialog12.dismiss();
                })
                .setNegativeButton("No", (dialog1, which) -> dialog1.dismiss())
                .show();

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected FragmentToolbar toolbarBuilder() {
        List<Integer> menuItems = new ArrayList<>();
        List<MenuItem.OnMenuItemClickListener> menuClicks = new ArrayList<>();

        menuItems.add(R.id.addcontact);
        menuClicks.add(item -> {
            addContact();
            return false;
        });

        menuItems.add(R.id.logout);
        menuClicks.add(item -> {
            activity.logout();
            return false;
        });

        return new FragmentToolbar.Builder()
                .withId(R.id.toolbar)
                .withTitle(R.string.contactlist_toolbar_title)
                .withMenu(R.menu.menu_main)
                .withMenuItems(menuItems, menuClicks)
                .build();
    }
}
