package com.kiko.chat.presentation.util;

import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;

public class ToolbarManager {

    static public void prepareToolbar(FragmentToolbar builder, View container) {
        if (builder.getResId() != FragmentToolbar.NO_TOOLBAR) {
            Toolbar fragmentToolbar = container.findViewById(builder.getResId());

            if (builder.getTitle() != -1) {
                fragmentToolbar.setTitle(builder.getTitle());
            }

            if(builder.getStrTitle() != null){
                fragmentToolbar.setTitle(builder.getStrTitle());
            }

            if (builder.getMenuId() != -1) {
                fragmentToolbar.inflateMenu(builder.getMenuId());
            }

            if (!builder.getMenuItems().isEmpty() && !builder.getMenuClicks().isEmpty()) {
                Menu menu = fragmentToolbar.getMenu();
                for (int i = 0; i < builder.getMenuItems().size(); i++) {
                    menu.findItem(builder.getMenuItems().get(i))
                            .setOnMenuItemClickListener(builder.getMenuClicks().get(i));
                }
            }

            if(builder.getBackButton() != null){
                fragmentToolbar.setNavigationOnClickListener(builder.getBackButton());
            }
        }
    }
}
