package com.myapp.newsclient.base.newscentermenu;


import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.TextView;

import com.myapp.newsclient.base.NewsCenterBaseMenu;

/**
 * 
 * @包名: org.itheima.zhbj51.base.newscentermenu
 * @类名: NewsCenterInteractMenu
 * @创建人: 肖琦
 * @创建时间 : 2015-3-9 上午9:41:35
 * 
 * @描述 : 新闻中心 页面中 互动菜单对应的内容页面
 * 
 * @SVN版本号: $Rev: 9 $
 * @更新时间: $Date: 2015-03-09 09:42:41 +0800 (星期一, 09 三月 2015) $
 * @更新人: $Author: xq $
 * @更新描述 : TODO
 * 
 */
public class NewsCenterInteractMenu extends NewsCenterBaseMenu
{

	public NewsCenterInteractMenu(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected View initView()
	{
		TextView tv = new TextView(mContext);

		tv.setText("互动页面的内容区域");
		tv.setTextSize(24);
		tv.setTextColor(Color.RED);
		tv.setGravity(Gravity.CENTER);

		return tv;
	}

}
