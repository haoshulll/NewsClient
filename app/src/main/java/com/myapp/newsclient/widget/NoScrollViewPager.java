package com.myapp.newsclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;


public class NoScrollViewPager extends LazyViewPager
{

	public NoScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public NoScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent ev)
	{
		return false;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{
		return false;
	}

}
