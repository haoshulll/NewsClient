package com.myapp.newsclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.viewpagerindicator.TabPageIndicator;

/**
 * @描述 : 不让父窗体抢占touch事件
 * 
 */
public class TouchedTabPageIndicator extends TabPageIndicator
{

	public TouchedTabPageIndicator(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public TouchedTabPageIndicator(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// true : 不希望父容器去拦截touch事件
		// false: 希望父容器去拦截touch事件
		getParent().requestDisallowInterceptTouchEvent(true);

		return super.dispatchTouchEvent(ev);
	}

}
