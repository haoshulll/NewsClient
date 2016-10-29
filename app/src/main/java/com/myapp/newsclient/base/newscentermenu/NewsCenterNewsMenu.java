package com.myapp.newsclient.base.newscentermenu;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.lidroid.xutils.view.annotation.event.OnClick;
import com.myapp.newsclient.R;
import com.myapp.newsclient.activity.MainActivity;
import com.myapp.newsclient.base.NewsCenterBaseMenu;
import com.myapp.newsclient.base.NewsListPager;
import com.myapp.newsclient.bean.NewsCenterBean;
import com.myapp.newsclient.widget.TouchedTabPageIndicator;

import java.util.List;

/**
 *
 * @描述 : 新闻中心 页面中 新闻菜单对应的内容页面
 * 
 */
public class NewsCenterNewsMenu extends NewsCenterBaseMenu implements OnPageChangeListener
{

	@ViewInject(R.id.newscenter_news_indicator)
	private TouchedTabPageIndicator mIndicator;

	@ViewInject(R.id.newscenter_news_pager)
	private ViewPager						mPager;

	@ViewInject(R.id.newscenter_news_arrow)
	private ImageView						mIvArrow;		// 箭头

	private NewsCenterBean.NewCenterMenuListBean mMenuData;		// 菜单数据

	private List<NewsCenterBean.NewsCenterNewsItemBean>	mPagerDatas;	// viewpager对应的数据

	public NewsCenterNewsMenu(Context context, NewsCenterBean.NewCenterMenuListBean data) {
		super(context);

		this.mMenuData = data;
		mPagerDatas = this.mMenuData.children;
	}

	@Override
	protected View initView()
	{
		// TextView tv = new TextView(mContext);
		// tv.setText("新闻页面的内容区域");
		// tv.setTextSize(24);
		// tv.setTextColor(Color.RED);
		// tv.setGravity(Gravity.CENTER);
		// return tv;

		View view = View.inflate(mContext, R.layout.newscenter_news, null);

		// ViewUtils注入
		ViewUtils.inject(this, view);

		return view;
	}

	@Override
	public void initData()
	{
		// 给viewpager设adapter-->list
		mPager.setAdapter(new NewsPagerAdapter());

		// 给indicator设置viewpager
		mIndicator.setViewPager(mPager);

		// 给viewpager设置选中监听
		// 当Viewpager和Indicator搭配使用时，要设置监听，必须设置indicator的监听，不可以设置viewpager的监听
		mIndicator.setOnPageChangeListener(this);
	}

	@OnClick(R.id.newscenter_news_arrow)
	public void clickArrow(View view)
	{
		// 让viewpager选中下一个
		int item = mPager.getCurrentItem();
		mPager.setCurrentItem(++item);
	}

	class NewsPagerAdapter extends PagerAdapter
	{

		@Override
		public int getCount()
		{
			if (mPagerDatas != null) { return mPagerDatas.size(); }
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

			NewsCenterBean.NewsCenterNewsItemBean bean = mPagerDatas.get(position);

			// 页面临时显示
//			TextView tv = new TextView(mContext);
//			tv.setText(bean.title);
//			tv.setTextSize(24);
//			tv.setTextColor(Color.RED);
//			tv.setGravity(Gravity.CENTER);
//
//			container.addView(tv);
//
//			return tv;
			NewsListPager pager = new NewsListPager(mContext,bean);
			//加载视图
			View view = pager.getRootView();
			container.addView(view);
			//加载数据
			pager.initData();
			return view;
		}
		@Override
		public void destroyItem(ViewGroup container, int position, Object object)
		{
			container.removeView((View) object);
		}

		// 返回viewpager对应的title
		@Override
		public CharSequence getPageTitle(int position)
		{
			if (mPagerDatas != null) { return mPagerDatas.get(position).title; }

			return super.getPageTitle(position);
		}
	}

	@Override
	public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
	{

	}

	@Override
	public void onPageSelected(int position)
	{
		MainActivity ui = (MainActivity) mContext;
		SlidingMenu menu = ui.getSlidingMenu();

		// if (position == 0)
		// {
		// //当选中第一页时，slidingMenu的touchMode应该为full_screen,
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_FULLSCREEN);
		// } else {
		// //其他情况为none
		// menu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
		// }
		menu.setTouchModeAbove(position == 0 ? SlidingMenu.TOUCHMODE_FULLSCREEN : SlidingMenu.TOUCHMODE_NONE);
	}

	@Override
	public void onPageScrollStateChanged(int state)
	{
		// TODO Auto-generated method stub

	}

}
