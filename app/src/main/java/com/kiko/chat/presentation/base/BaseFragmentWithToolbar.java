package com.kiko.chat.presentation.base;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;

import com.kiko.chat.presentation.util.FragmentToolbar;
import com.kiko.chat.presentation.util.ToolbarManager;

import dagger.android.support.DaggerFragment;

public abstract class BaseFragmentWithToolbar extends DaggerFragment {
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ToolbarManager.prepareToolbar(toolbarBuilder(), view);
    }

    protected abstract FragmentToolbar toolbarBuilder();
}
