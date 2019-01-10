package com.kiko.chat.presentation.chat.di;

import com.kiko.chat.di.scope.PerFragment;
import com.kiko.chat.domain.interactor.ChatInteractor;
import com.kiko.chat.domain.interactor.StorageInteractor;
import com.kiko.chat.domain.entity.Message;
import com.kiko.chat.libs.base.ImageLoader;
import com.kiko.chat.presentation.chat.ChatPresenter;
import com.kiko.chat.presentation.chat.ChatPresenterImpl;
import com.kiko.chat.presentation.chat.NoScopeChatKeeper;
import com.kiko.chat.presentation.chat.ui.ChatAdapter;
import com.kiko.chat.presentation.chat.ui.ChatFragment;

import java.util.List;

import javax.inject.Named;

import dagger.Module;
import dagger.Provides;

@Module
public abstract class ChatModule {
    @PerFragment
    @Provides
    static ChatPresenter provideChatPresenter(
            ChatPresenter.View view,
            ChatInteractor chatInteractor,
            StorageInteractor storageInteractor,
            @Named("receiver") String receiver,
            @Named("auth_email") String sender) {
        return new ChatPresenterImpl(view, chatInteractor, storageInteractor, receiver, sender);
    }

    @PerFragment
    @Provides
    static ChatPresenter.View provideChatView(ChatFragment fragment) {
        return fragment;
    }


    @PerFragment
    @Provides
    @Named("receiver")
    static String provideReceiver(NoScopeChatKeeper keeper) {
        return keeper.getReceiver();
    }

    @Provides
    @PerFragment
    static ChatAdapter provideChatAdapter(ChatFragment fragment, @Named("chat_data_set") List<Message> dataset, ImageLoader loader) {
        return new ChatAdapter(fragment.getContext(), dataset, loader);
    }

    @Provides
    @Named("chat_data_set")
    static List<Message> provideChatDataSet(NoScopeChatKeeper keeper) {
        return keeper.getDataSet();
    }
}
