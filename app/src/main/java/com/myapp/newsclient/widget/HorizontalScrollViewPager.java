package com.myapp.newsclient.widget;

import android.content.Context;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

/**
 * @描述 : 水平滚动的Viewpager不让父Viewpager抢占touch
 */
public class HorizontalScrollViewPager extends ViewPager
{

	private float	downX;
	private float	downY;

	public HorizontalScrollViewPager(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public HorizontalScrollViewPager(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	public boolean dispatchTouchEvent(MotionEvent ev)
	{
		// 1.从右往左
		// 如果是在第一个页面，手指 从 右往左滑动 进入下一个页面，自己响应touch
		// 如果是在第二个页面，手指 从 右往左滑动 进入下一个页面，自己响应touch

		// 如果是最后一个页面，手指 从 右往左滑动 父容器响应touch

		// 2.从左往右
		// 如果是在第一个页面，手指 从 左往右滑动 , 父容器响应touch

		// 如果是在除去第一和最后的页面，手指 从 左往右滑动 ，自己响应touch

		// 如果是最后一个页面，手指 从 左往右滑动 , 自己响应touch

		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				getParent().requestDisallowInterceptTouchEvent(true);

				downX = ev.getX();
				downY = ev.getY();

				break;
			case MotionEvent.ACTION_MOVE:
				float moveX = ev.getX();
				float moveY = ev.getY();

				float diffX = moveX - downX;
				float diffY = moveY - downY;

				// diffX > 0 : 从 左往右
				// diffY < 0 : 从 右往左

				if (Math.abs(diffX) > Math.abs(diffY))
				{
					// 认为用户操作为水平操作

					// 如果是在第一个页面，手指 从 左往右滑动 , 父容器响应touch
					if (diffX > 0 && getCurrentItem() == 0)
					{
						getParent().requestDisallowInterceptTouchEvent(false);
					}
					else if (diffX > 0 && getCurrentItem() != 0)
					{
						// 如果是在除去第一和最后的页面，手指 从 左往右滑动 ，自己响应touch
						getParent().requestDisallowInterceptTouchEvent(true);
					}
					else if (diffX < 0 && (getAdapter().getCount() - 1) == getCurrentItem())
					{
						// 如果是最后一个页面，手指 从 右往左滑动 父容器响应touch
						getParent().requestDisallowInterceptTouchEvent(false);
					}
					else
					{
						// 从右往左
						// 如果是在第一个页面，手指 从 右往左滑动 进入下一个页面，自己响应touch
						// 如果是在第二个页面，手指 从 右往左滑动 进入下一个页面，自己响应touch
						getParent().requestDisallowInterceptTouchEvent(true);
					}
				}
				else
				{
					// touch交给父容器
					getParent().requestDisallowInterceptTouchEvent(false);
				}

				break;

			default:
				break;
		}

		return super.dispatchTouchEvent(ev);
	}
}
