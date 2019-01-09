package com.kiko.chat.presentation.addcontact.ui;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;

import com.kiko.chat.R;
import com.kiko.chat.presentation.addcontact.AddContactPresenter;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import dagger.android.support.DaggerAppCompatDialogFragment;

public class AddContactDialogFragment extends DaggerAppCompatDialogFragment implements AddContactPresenter.View, DialogInterface.OnShowListener {

    @Inject
    AddContactPresenter presenter;

    @BindView(R.id.editTxtEmail)
    EditText editTxtEmail;
    @BindView(R.id.progressBar)
    ProgressBar progressBar;
    Unbinder unbinder;
    @BindView(R.id.wrapperEmail)
    TextInputLayout wrapperEmail;

    public static AddContactDialogFragment newInstance() {
        Bundle args = new Bundle();
        AddContactDialogFragment fragment = new AddContactDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
      //  getLifecycle().addObserver((LifecycleObserver) presenter);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity())
                .setTitle(R.string.addcontact_message_title)
                .setPositiveButton(R.string.addcontact_message_add,
                        (dialog, which) -> {
                        })
                .setNegativeButton(R.string.addcontact_message_cancel,
                        (dialog, which) -> {
                        });

        LayoutInflater i = getActivity().getLayoutInflater();
        View view = i.inflate(R.layout.fragment_add_contact_dialog, null);
        unbinder = ButterKnife.bind(this, view);
        builder.setView(view);
        AlertDialog dialog = builder.create();
        dialog.setOnShowListener(this);

        return dialog;
    }

    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideProgress() {
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void showError(String message) {
        wrapperEmail.setError(message);
    }

    @Override
    public void onShow(DialogInterface dialogInterface) {
        final AlertDialog dialog = (AlertDialog) getDialog();
        if (dialog != null) {

            Button positiveButton = dialog.getButton(Dialog.BUTTON_POSITIVE);
            Button negativeButton = dialog.getButton(Dialog.BUTTON_NEGATIVE);

            positiveButton.setOnClickListener(view -> presenter.addContact(editTxtEmail.getText().toString()));

            negativeButton.setOnClickListener(view -> dismiss());
        }
    }

    @Override
    public void showInput() {
        wrapperEmail.setVisibility(View.VISIBLE);
    }

    @Override
    public void hideInput() {
        wrapperEmail.setVisibility(View.GONE);
    }

    @Override
    public void contactAdded() {

        dismiss();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        presenter.destroy();
    }

}
