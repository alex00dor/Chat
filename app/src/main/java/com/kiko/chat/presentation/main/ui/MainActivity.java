package com.kiko.chat.presentation.main.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentTransaction;

import com.kiko.chat.R;
import com.kiko.chat.presentation.base.BaseActivity;
import com.kiko.chat.presentation.chat.NoScopeChatKeeper;
import com.kiko.chat.presentation.chat.ui.ChatFragment;
import com.kiko.chat.presentation.contactlist.ui.ContactListFragment;
import com.kiko.chat.presentation.login.ui.LoginActivity;
import com.kiko.chat.presentation.main.MainPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MainActivity extends BaseActivity implements MainPresenter.View {

    public static final String TO_CHAT = "toChat";

    @Inject
    MainPresenter presenter;
    @BindView(R.id.container)
    ConstraintLayout container;

    @Inject
    NoScopeChatKeeper noScopeChatKeeper;

    ContactListFragment contactListFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        getLifecycle().addObserver((LifecycleObserver) presenter);
        openChat();
        setupFragmentContactList();
    }

    void openChat(){
        if(getIntent() != null){
            String receiver = getIntent().getStringExtra(TO_CHAT);
            if(receiver != null){
                navigateToChat(receiver, false);
            }
        }
    }

    private void setupFragmentContactList() {
        if(contactListFragment == null && !checkContactListInstance() && noScopeChatKeeper.getReceiver() == null) {
            contactListFragment = ContactListFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.fragmentContainer, contactListFragment)
                    .commit();
        }
    }
    private boolean checkContactListInstance(){
        for(Object o: getSupportFragmentManager().getFragments()){
            if(o instanceof ContactListFragment)
                return true;
        }

        return false;
    }

    public void navigateToChat(String email, boolean addToBackStack) {
        noScopeChatKeeper.cleanDataSet();
        noScopeChatKeeper.setReceiver(email);
        FragmentTransaction fragmentTransaction = getSupportFragmentManager()
                .beginTransaction()
                .setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left, R.anim.slide_in_left, R.anim.slide_out_right)
                .replace(R.id.fragmentContainer, ChatFragment.newInstance());

        if(addToBackStack)
            fragmentTransaction.addToBackStack(null);

        fragmentTransaction.commit();
    }


    public void logout(){
        presenter.logout();
    }

    @Override
    public void showError(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void navigateToLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
                | Intent.FLAG_ACTIVITY_NEW_TASK
                | Intent.FLAG_ACTIVITY_CLEAR_TASK);

        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        noScopeChatKeeper.cleanReceiver();
    }
}
