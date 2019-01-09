package com.kiko.chat.presentation.chat.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.kiko.chat.R;
import com.kiko.chat.domain.model.Message;
import com.kiko.chat.presentation.base.BaseFragmentWithToolbar;
import com.kiko.chat.presentation.chat.ChatPresenter;
import com.kiko.chat.presentation.chat.NoScopeChatKeeper;
import com.kiko.chat.presentation.main.ui.MainActivity;
import com.kiko.chat.presentation.util.FragmentToolbar;
import com.kiko.chat.presentation.util.ImagePickerHelper;
import com.kiko.chat.presentation.util.NotificationHelper;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class ChatFragment extends BaseFragmentWithToolbar implements ChatPresenter.View {

    @Inject
    ChatPresenter presenter;

    @Inject
    ChatAdapter adapter;

    @Inject
    NoScopeChatKeeper keeper;

    @Inject
    MainActivity activity;

    @Inject
    NotificationHelper notificationHelper;

    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.editTxtMessage)
    EditText editTxtMessage;
    @BindView(R.id.btnSendMessage)
    ImageButton btnSendMessage;
    Unbinder unbinder;

    private static final int IMAGE_REQUEST = 1991;

    public static ChatFragment newInstance() {
        Bundle args = new Bundle();
        ChatFragment fragment = new ChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chat, container, false);
        getLifecycle().addObserver((LifecycleObserver) presenter);
        unbinder = ButterKnife.bind(this, view);

        setupRecycler();
        return view;
    }


    private void setupRecycler() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setItemViewCacheSize(50);
        recyclerView.setAdapter(adapter);
        recyclerView.setKeepScreenOn(true);
    }


    private void moveMessageListToLast() {
        recyclerView.scrollToPosition(0);
    }

    @Override
    public void newMessage(Message message) {
        if (adapter.add(message)) {
            moveMessageListToLast();
        }
    }

    @Override
    public void setMessagesDataSet(List<Message> messages) {
        adapter.setMessages(messages);
    }

    @Override
    public void cancelNotification() {
        notificationHelper.cancel(keeper.getReceiver(), notificationHelper.getGroupID(keeper.getReceiver()));
    }

    @OnClick(R.id.btnSendMessage)
    void sendMessage() {
        String msg = editTxtMessage.getText().toString();
        presenter.sendMessage(msg);
        editTxtMessage.setText("");
    }

    @OnClick(R.id.btnAttachPhoto)
    void sendPhoto(){
        Intent intent = ImagePickerHelper.getPickImageIntent(getContext());
        startActivityForResult(intent, IMAGE_REQUEST);
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
        activity.showError(message);
    }

    @Override
    public void onResume() {
        super.onResume();
        keeper.setPause(false);
    }

    @Override
    public void onPause() {
        super.onPause();
        keeper.setPause(true);
    }

    @Override
    public void onStop() {
        super.onStop();
        keeper.setDataSet(adapter.getDataSet());
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }

    @Override
    protected FragmentToolbar toolbarBuilder() {
        return new FragmentToolbar.Builder()
                .withId(R.id.toolbar)
                .withTitle(keeper.getReceiver())
                .onBackButton(v -> activity.onBackPressed())
                .build();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUEST:
                Uri imageUri = ImagePickerHelper.getImageUriFromResult(getContext(), resultCode, data);
                presenter.loadImage(imageUri);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
