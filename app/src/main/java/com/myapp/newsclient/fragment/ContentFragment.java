package com.myapp.newsclient.fragment;

import android.support.v4.view.PagerAdapter;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioGroup;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.myapp.newsclient.R;
import com.myapp.newsclient.activity.MainActivity;
import com.myapp.newsclient.base.BaseFragment;
import com.myapp.newsclient.base.TabBasePager;
import com.myapp.newsclient.base.tab.TabGovPager;
import com.myapp.newsclient.base.tab.TabHomePager;
import com.myapp.newsclient.base.tab.TabNewsCenterPager;
import com.myapp.newsclient.base.tab.TabSettingPager;
import com.myapp.newsclient.base.tab.TabSmartServicePager;
import com.myapp.newsclient.widget.NoScrollViewPager;

import java.util.ArrayList;
import java.util.List;

/**
 * @包名: com.myapp.newsclient.fragment
 * @作者: haoshul
 * @时间: 2016/10/16 20:38
 * @描述: TODO
 */

public class ContentFragment extends BaseFragment implements RadioGroup.OnCheckedChangeListener
{
    private static final String	TAG	= "ContentFragment";

    @ViewInject(R.id.content_pager)
    private NoScrollViewPager	mPager;					// viewpager

    @ViewInject(R.id.content_rg)
    private RadioGroup			mRadioGroup;				// 底部容器

    private List<TabBasePager>	mPagerList;				// 页面数据

    private int					mCurrentIndex;

    @Override
    protected View initView()
    {
        // TextView tv = new TextView(mActivity);
        //
        // tv.setText("主页内容");
        //
        // return tv;

        View view = View.inflate(mActivity, R.layout.content, null);

        // Viewutil工具的注入
        ViewUtils.inject(this, view);

        return view;
    }

    @Override
    protected void initData()
    {
        // 初始化数据
        mPagerList = new ArrayList<TabBasePager>();

        // 添加实际的页面
        mPagerList.add(new TabHomePager(mActivity));
        mPagerList.add(new TabNewsCenterPager(mActivity));
        mPagerList.add(new TabSmartServicePager(mActivity));
        mPagerList.add(new TabGovPager(mActivity));
        mPagerList.add(new TabSettingPager(mActivity));

        // 给viewPager加载数据--->adapter---->list
        mPager.setAdapter(new ContentPagerAdapter());

        // 给RadioGroup 设置监听
        mRadioGroup.setOnCheckedChangeListener(this);

        // 设置初始化选中的界面
        mCurrentIndex = 0;
        mRadioGroup.check(R.id.tab_home);
    }

    class ContentPagerAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            if (mPagerList != null) { return mPagerList.size(); }
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object)
        {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position)
        {

            Log.d(TAG, "加载第" + position + "页");

            TabBasePager pager = mPagerList.get(position);
            View view = pager.getRootView();

            // viewpager需要放视图
            container.addView(view);

            // 给页面控制器加载数据
            pager.initData();

            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            Log.d(TAG, "销毁第" + position + "页");

            container.removeView((View) object);
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup group, int checkedId)
    {
        // 1. RadioGroup
        // 2. 选中的RadioButton的id

        switch (checkedId)
        {
            case R.id.tab_home:
                mCurrentIndex = 0;
                // 设置侧滑菜单不可见
                setSlidingMenuTouchEnable(false);
                break;
            case R.id.tab_newscenter:
                mCurrentIndex = 1;
                setSlidingMenuTouchEnable(true);
                break;
            case R.id.tab_smartservice:
                mCurrentIndex = 2;
                setSlidingMenuTouchEnable(true);
                break;
            case R.id.tab_gov:
                mCurrentIndex = 3;
                setSlidingMenuTouchEnable(true);
                break;
            case R.id.tab_setting:
                mCurrentIndex = 4;
                setSlidingMenuTouchEnable(false);
                break;
            default:
                break;
        }

        // 给ViewPager设置选中的页面
        mPager.setCurrentItem(mCurrentIndex);
    }

    private void setSlidingMenuTouchEnable(boolean enable)
    {
        MainActivity ui = (MainActivity) mActivity;
        SlidingMenu menu = ui.getSlidingMenu();
        menu.setTouchModeAbove(enable ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_NONE);
    }

    /**
     * 改变菜单对应的显示页面
     *
     */
    public void switchMenuPager(int position)
    {
        // 获取当前显示的tab页面
        TabBasePager pager = mPagerList.get(mCurrentIndex);

        // 调用切换菜单
        pager.switchMenuPager(position);

    }

}
