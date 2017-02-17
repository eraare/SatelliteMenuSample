package com.eraare.satellitemenu;

import android.widget.ImageView;

/**
 * 卫星菜单的菜单项
 */
public class MenuItem {
    public ImageView menuView; /*菜单视图*/
    public Object menuTag; /*菜单标志*/
    public int menuIcon; /*菜单图标*/

    public MenuItem(Object menuTag, int menuIcon) {
        this.menuTag = menuTag;
        this.menuIcon = menuIcon;
    }

}
