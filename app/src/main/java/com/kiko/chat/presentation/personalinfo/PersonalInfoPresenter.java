package com.kiko.chat.presentation.personalinfo;

import android.net.Uri;

import com.kiko.chat.presentation.base.BasePresenter;
import com.kiko.chat.presentation.base.BaseView;

public interface PersonalInfoPresenter extends BasePresenter {
    interface View extends BaseView{
        void personalInfoSaved();
        void setPhotoUrl(String url);
    }

    void save(String nickname, String photoUrl);
    void saveImageInStorage(Uri uri);
}
