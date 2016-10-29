package com.myapp.newsclient.base;

import android.content.Context;
import android.view.View;

/**
 * @描述:新闻中心 内容区域的基类
 */
public abstract class NewsCenterBaseMenu
{
	protected Context	mContext;	// 上下文
	protected View		mRootView;	// 根视图

	public NewsCenterBaseMenu(Context context) {
		this.mContext = context;

		mRootView = initView();
	}

	/**
	 * 让子类去实现自己长什么样子
	 * 
	 * @return
	 */
	protected abstract View initView();

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

}
