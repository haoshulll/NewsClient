package com.myapp.newsclient.activity;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.Window;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.jeremyfeinstein.slidingmenu.lib.app.SlidingFragmentActivity;
import com.myapp.newsclient.R;
import com.myapp.newsclient.fragment.ContentFragment;
import com.myapp.newsclient.fragment.LeftMenuFragment;

public class MainActivity extends SlidingFragmentActivity {

    private final static String	TAG_CONTENT		= "content";	// 内容区域的标记
    private final static String	TAG_LEFT_MENU	= "left_menu";	// 左侧区域的标记

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        // 去掉title
        requestWindowFeature(Window.FEATURE_NO_TITLE);

        super.onCreate(savedInstanceState);

        // 设置内容区域
        setContentView(R.layout.activity_main);

        // 设置behind部分（left，right）
        setBehindContentView(R.layout.main_left);
        // 获得sildingMenu实例
        SlidingMenu menu = getSlidingMenu();
        // 设置模式
        menu.setMode(SlidingMenu.LEFT);
        // 指的是菜单的边缘到屏幕边缘的距离
        // menu.setBehindOffset(180);
        // 菜单的宽度
        menu.setBehindWidth(120);
        // 设置全屏拉出:TOUCHMODE_FULLSCREEN TOUCHMODE_MARGRING TOUCHMODE_NONE
        menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);

        // 通过fragment的方式加载页面
        initFragment();
    }

    private void initFragment()
    {
        FragmentManager fm = getSupportFragmentManager();

        // 1. 开启事务
        FragmentTransaction transaction = fm.beginTransaction();

        // 添加主页fragment
        transaction.replace(R.id.main_container, new ContentFragment(), TAG_CONTENT);

        // 添加左侧fragment
        transaction.replace(R.id.main_left_container, new LeftMenuFragment(), TAG_LEFT_MENU);

        transaction.commit();
    }

    /**
     * 获取左侧菜单数据
     *
     * @return
     */
    public LeftMenuFragment getLeftFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        return (LeftMenuFragment) fm.findFragmentByTag(TAG_LEFT_MENU);
    }

    /**
     * 获取内容fragment
     *
     * @return
     */
    public ContentFragment getContentFragment()
    {
        FragmentManager fm = getSupportFragmentManager();
        return (ContentFragment) fm.findFragmentByTag(TAG_CONTENT);
    }
}