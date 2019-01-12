package com.kiko.chat.presentation.personalinfo.ui;

import android.arch.lifecycle.LifecycleObserver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.constraint.ConstraintLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;

import com.kiko.chat.R;
import com.kiko.chat.presentation.base.BaseActivity;
import com.kiko.chat.presentation.personalinfo.PersonalInfoPresenter;
import com.kiko.chat.presentation.util.ImagePickerHelper;

import java.util.Objects;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class PersonalInfoActivity extends BaseActivity implements PersonalInfoPresenter.View{

    @Inject
    PersonalInfoPresenter presenter;
    @BindView(R.id.nickname)
    TextInputEditText nickname;
    @BindView(R.id.container)
    ConstraintLayout container;

    private String photoUrl = null;


    private static final int IMAGE_REQUEST = 1991;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_information);
        ButterKnife.bind(this);

        getLifecycle().addObserver((LifecycleObserver) presenter);
    }

    @OnClick(R.id.selectPhoto)
    void selectPhoto(){
        Intent intent = ImagePickerHelper.getPickImageIntent(this);
        startActivityForResult(intent, IMAGE_REQUEST);
    }

    @OnClick(R.id.save)
    void save(){
        String nick = Objects.requireNonNull(nickname.getText()).toString();
        presenter.save(nick, photoUrl);
    }

    @Override
    public void showError(String message) {
        Snackbar.make(container, message, Snackbar.LENGTH_SHORT).show();
    }

    @Override
    public void personalInfoSaved() {
        super.onBackPressed();
    }

    @Override
    public void setPhotoUrl(String url) {
        photoUrl = url;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case IMAGE_REQUEST:
                Uri imageUri = ImagePickerHelper.getImageUriFromResult(this, resultCode, data);
                presenter.saveImageInStorage(imageUri);
                break;
            default:
                super.onActivityResult(requestCode, resultCode, data);
                break;
        }
    }
}
