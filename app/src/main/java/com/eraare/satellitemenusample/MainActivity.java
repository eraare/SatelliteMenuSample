package com.eraare.satellitemenusample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import com.eraare.satellitemenu.MenuItem;
import com.eraare.satellitemenu.SatelliteMenu;

public class MainActivity extends AppCompatActivity {
    private SatelliteMenu mMenuView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        /*1 获取菜单控件*/
        mMenuView = (SatelliteMenu) findViewById(R.id.sm_menu_main);
        /*2 添加子菜单项*/
        mMenuView.addMenuItem(new MenuItem(1, R.drawable.icon_item_sat));
        mMenuView.addMenuItem(new MenuItem(2, R.drawable.icon_item_sat));
        mMenuView.addMenuItem(new MenuItem(3, R.drawable.icon_item_sat));
        mMenuView.addMenuItem(new MenuItem(4, R.drawable.icon_item_sat));
        /*3 绑定点击事件*/
        mMenuView.setOnMenuItemClickListener(mOnMenuItemClickListener);
    }

    private SatelliteMenu.OnMenuItemClickListener mOnMenuItemClickListener = new SatelliteMenu.OnMenuItemClickListener() {
        @Override
        public void onMenuItemClick(View view, Object tag) {
            Toast.makeText(getApplicationContext(), "You clicked:" + tag.toString(), Toast.LENGTH_SHORT).show();
        }
    };
}
