package com.kiko.chat.presentation.util;

import java.util.ArrayList;
import java.util.List;

import android.support.annotation.IdRes;
import android.support.annotation.MenuRes;
import android.support.annotation.StringRes;
import android.view.MenuItem;
import android.view.View;

public class FragmentToolbar {
    static public final int NO_TOOLBAR = -1;

    private int resId;
    private int title;
    private String strTitle;
    private int menuId;
    private List<Integer> menuItems;
    private List<MenuItem.OnMenuItemClickListener> menuClicks;
    private View.OnClickListener backButton;

    private FragmentToolbar(
            @IdRes int resId,
            @StringRes int title,
            String strTitle,
            @MenuRes int menuId,
            List<Integer> menuItems,
            List<MenuItem.OnMenuItemClickListener> menuClicks,
            View.OnClickListener backButton
    ) {
        this.resId = resId;
        this.title = title;
        this.strTitle = strTitle;
        this.menuId = menuId;
        this.menuItems = menuItems;
        this.menuClicks = menuClicks;
        this.backButton = backButton;
    }

    public View.OnClickListener getBackButton() {
        return backButton;
    }

    public String getStrTitle() {
        return strTitle;
    }

    public int getResId() {
        return resId;
    }

    public int getTitle() {
        return title;
    }

    public int getMenuId() {
        return menuId;
    }

    public List<Integer> getMenuItems() {
        return menuItems;
    }

    public List<MenuItem.OnMenuItemClickListener> getMenuClicks() {
        return menuClicks;
    }

    static public class Builder {
        int resId = -1;
        int title = -1;
        String strTitle = null;
        int menuId = -1;
        List<Integer> menuItems = new ArrayList<>();
        List<MenuItem.OnMenuItemClickListener> menuClicks = new ArrayList<>();
        View.OnClickListener backButton = null;

        public Builder withId(@IdRes int resId) {
            this.resId = resId;
            return this;
        }

        public Builder withTitle(@StringRes int title) {
            this.title = title;
            return this;
        }

        public Builder withTitle(String title) {
            this.strTitle = title;
            return this;
        }

        public Builder withMenu(@MenuRes int menuId) {
            this.menuId = menuId;
            return this;
        }

        public Builder withMenuItems(List<Integer> menuItems, List<MenuItem.OnMenuItemClickListener> menuClicks) {
            this.menuItems = menuItems;
            this.menuClicks = menuClicks;
            return this;
        }

        public Builder onBackButton(View.OnClickListener listener){
            backButton = listener;
            return this;
        }

        public FragmentToolbar build() {
            return new FragmentToolbar(resId, title, strTitle, menuId, menuItems, menuClicks, backButton);
        }
    }
}
