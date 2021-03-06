package com.myapp.newsclient.base.tab;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout.LayoutParams;
import android.widget.TextView;

import com.myapp.newsclient.base.TabBasePager;

/**
 * 
 * @描述: 主页界面中tab对应的home页面
 */
public class TabSmartServicePager extends TabBasePager
{

	public TabSmartServicePager(Context context) {
		super(context);
	}

	@Override
	public void initData()
	{
		// 1. title部分数据的设置
		mTvTitle.setText("生活");
		mIvMenu.setVisibility(View.VISIBLE);

		// 2. 内容区域数据的设置
		TextView tv = new TextView(mContext);
		tv.setText("智慧服务内容区域");
		tv.setTextColor(Color.RED);
		tv.setTextSize(24);
		tv.setGravity(Gravity.CENTER);

		LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		mContentContainer.addView(tv, params);
	}
}
