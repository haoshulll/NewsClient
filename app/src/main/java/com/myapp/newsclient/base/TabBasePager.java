package com.myapp.newsclient.base;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.myapp.newsclient.R;
import com.myapp.newsclient.activity.MainActivity;

/**
 * 
 * @描述:viewpager的每一个子页面的controller的基类
 */
public abstract class TabBasePager implements OnClickListener
{
	protected Context		mContext;			// 上下文
	protected View			mRootView;			// 根视图
	protected TextView		mTvTitle;			// title
	protected ImageButton	mIvMenu;			// menu
	protected FrameLayout	mContentContainer;	// 内容容器

	public TabBasePager(Context context) {
		this.mContext = context;
		mRootView = initView();
	}

	protected View initView()
	{
		View view = View.inflate(mContext, R.layout.tab_base_pager, null);

		// 实现查找view
		mTvTitle = (TextView) view.findViewById(R.id.title_bar_tv_title);
		mIvMenu = (ImageButton) view.findViewById(R.id.title_bar_iv_menu);
		mContentContainer = (FrameLayout) view.findViewById(R.id.tab_base_content_container);

		mIvMenu.setOnClickListener(this);

		return view;
	}

	/**
	 * 数据加载的方法，子类如果要实现数据加载，就需要复写这个方法
	 * 
	 * @return
	 */
	public void initData()
	{

	}

	public View getRootView()
	{
		return mRootView;
	}

	@Override
	public void onClick(View v)
	{
		if (v == mIvMenu)
		{
			toggleSlidingMenu();
		}
	}

	private void toggleSlidingMenu()
	{
		// 打开slidingMenu
		MainActivity ui = (MainActivity) mContext;
		SlidingMenu menu = ui.getSlidingMenu();
		// 如果slidingmenus是打开的，那么就关闭，否则相反
		menu.toggle();
	}
	/**
	 * 菜单切换的方法，子类如果要菜单切换，就需要复写这个方法
	 * @param position
	 */
	public void switchMenuPager(int position)
	{

	}


}
