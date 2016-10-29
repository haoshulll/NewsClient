package com.myapp.newsclient.base.newscentermenu;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.myapp.newsclient.base.NewsCenterBaseMenu;

/**
 *
 * @描述 : 新闻中心 页面中 组图菜单对应的内容页面
 * 
  */
public class NewsCenterPicMenu extends NewsCenterBaseMenu
{

	public NewsCenterPicMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View initView()
	{
		TextView tv = new TextView(mContext);

		tv.setText("组图页面的内容区域");
		tv.setTextSize(24);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);

		return tv;
	}

}
