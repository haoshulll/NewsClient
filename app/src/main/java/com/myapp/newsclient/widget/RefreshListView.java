package com.myapp.newsclient.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.myapp.newsclient.R;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @描述 : 下拉刷新，上拉加载
 *
 */
public class RefreshListView extends ListView implements OnScrollListener
{
	private static final String	TAG						= "RefreshListView";
	// 1. 需要下拉刷新
	// 2. 释放刷新
	// 3. 正在刷新
	public static final int		STATE_PULL_DOWN_REFRESH	= 0;						// 下拉刷新
	public static final int		STATE_RELEASE_REFRESH	= 1;						// 释放刷新
	public static final int		STATE_REFRESHING		= 2;						// 释放刷新

	private static final long	DURATION				= 200;

	private LinearLayout		mHeaderLayout;										// 包含
	// 刷新的view
	// 和
	// 自定义的headerView

	private View				mCustomHeaderView;									// 自定义的headerView
	private View				mRefreshView;										// 刷新的view

	private int					mRefreshViewHeight;								// 刷新view的高度

	private float				mDownX;
	private float				mDownY;

	private int					mCurrentState			= STATE_PULL_DOWN_REFRESH;	// 默认值

	private ProgressBar			mPb;												// 进度
	private ImageView			mIvArrow;											// 箭头
	private TextView			mTvState;											// 状态显示
	private TextView			mTvUpdateTime;										// 时间显示

	private RotateAnimation		mDown2UpAnimation;									// 给释放刷新准备的
	private RotateAnimation		mUp2DownAnimation;									// 给下拉刷新准备的
	private OnRefreshListener	mRefreshListener;

	private View				mFooterLayout;										// 底部加载更多的布局
	private int					mFooterHeight;
	private boolean				isLoadingMore;										// 标记是否是加载更多

	public RefreshListView(Context context) {
		super(context);

		// 加载头布局
		initHeaderLayout();
		initFooterLayout();

		initAnimation();
	}

	public RefreshListView(Context context, AttributeSet attrs) {
		super(context, attrs);

		// 加载头布局
		initHeaderLayout();
		initFooterLayout();

		initAnimation();
	}

	private void initHeaderLayout()
	{
		// 加载 头布局
		mHeaderLayout = (LinearLayout) View.inflate(getContext(), R.layout.refresh_header_layout, null);

		// 添加到listView的headerView中
		this.addHeaderView(mHeaderLayout);

		// 需要隐藏刷新的View
		mRefreshView = mHeaderLayout.findViewById(R.id.refresh_header_refresh_part);
		mPb = (ProgressBar) mHeaderLayout.findViewById(R.id.refresh_header_pb);
		mIvArrow = (ImageView) mHeaderLayout.findViewById(R.id.refresh_header_arrow);
		mTvState = (TextView) mHeaderLayout.findViewById(R.id.refresh_header_tv_state);
		mTvUpdateTime = (TextView) mHeaderLayout.findViewById(R.id.refresh_header_tv_date);

		// 给头布局设置PaddingTop为负数来隐藏控件
		mRefreshView.measure(0, 0);
		mRefreshViewHeight = mRefreshView.getMeasuredHeight();
		Log.d(TAG, "高度 : " + mRefreshViewHeight);
		mHeaderLayout.setPadding(0, -mRefreshViewHeight, 0, 0);
	}

	private void initFooterLayout()
	{
		mFooterLayout = View.inflate(getContext(), R.layout.refresh_footer_layout, null);
		// 添加到footerView中
		this.addFooterView(mFooterLayout);
		// 隐藏footerLayout
		mFooterLayout.measure(0, 0);
		mFooterHeight = mFooterLayout.getMeasuredHeight();
		mFooterLayout.setPadding(0, -mFooterHeight, 0, 0);

		// 设置当listView滑动时的监听
		this.setOnScrollListener(this);
	}

	private void initAnimation()
	{
		mUp2DownAnimation = new RotateAnimation(0,
				180,
				Animation.RELATIVE_TO_SELF,
				0.5f,
				Animation.RELATIVE_TO_SELF,
				0.5f);
		mUp2DownAnimation.setDuration(DURATION);
		mUp2DownAnimation.setFillEnabled(true);
		mUp2DownAnimation.setFillAfter(true);

		mDown2UpAnimation = new RotateAnimation(180,
				360,
				Animation.RELATIVE_TO_SELF,
				0.5f,
				Animation.RELATIVE_TO_SELF,
				0.5f);
		mDown2UpAnimation.setDuration(DURATION);
		mDown2UpAnimation.setFillEnabled(true);
		mDown2UpAnimation.setFillAfter(true);
	}

	public void addCustomHeaderView(View headerView)
	{
		this.mCustomHeaderView = headerView;
		mHeaderLayout.addView(headerView);
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev)
	{

		switch (ev.getAction())
		{
			case MotionEvent.ACTION_DOWN:
				mDownX = ev.getX();
				mDownY = ev.getY();

				break;
			case MotionEvent.ACTION_MOVE:
				float moveX = ev.getX();
				float moveY = ev.getY();

				float diffY = moveY - mDownY;

				// 如果当前的状态是正在刷新
				if (mCurrentState == STATE_REFRESHING)
				{
					break;
				}

				// 获得listView的坐标
				// int[] listViewLoc = new int[2];// 0:x 1:y
				// getLocationOnScreen(listViewLoc);
				// Log.d(TAG, "listView x : " + listViewLoc[0]);
				// Log.d(TAG, "listView y : " + listViewLoc[1]);
				//
				// int[] customLoc = new int[2];
				// mCustomHeaderView.getLocationOnScreen(customLoc);
				// getLocationOnScreen(customLoc);
				//
				// Log.d(TAG, "CustomHeader x : " + customLoc[0]);
				// Log.d(TAG, "CustomHeader y : " + customLoc[1]);

				// diffY > 0:下拉
				// diffY < 0:上拉

				// 如果，第一个item可以见时才可以下拉 && 往下拉
				// int position = getFirstVisiblePosition();
				if (getFirstVisiblePosition() == 0 && diffY > 0)
				{
					// 给头布局设置paddingTop
					int hiddenHeight = (int) (mRefreshViewHeight - diffY + 0.5f);
					mHeaderLayout.setPadding(0, -hiddenHeight, 0, 0);

					// diffY < mRefreshViewHeight : 下拉刷新
					if (diffY < mRefreshViewHeight && mCurrentState == STATE_RELEASE_REFRESH)
					{
						// 更新状态
						mCurrentState = STATE_PULL_DOWN_REFRESH;

						// UI 更新
						Log.d(TAG, "下拉刷新");
						refreshUI();
					}
					else if (diffY >= mRefreshViewHeight && mCurrentState == STATE_PULL_DOWN_REFRESH)
					{
						// diffY > mRefreshViewHeight : 释放刷新
						// 更新状态
						mCurrentState = STATE_RELEASE_REFRESH;

						// UI 更新
						Log.d(TAG, "释放刷新");
						refreshUI();
					}
					// 需要自己响应touch
					return true;
				}
				break;
			case MotionEvent.ACTION_UP:
			case MotionEvent.ACTION_CANCEL:
				mDownY = 0;

				// 释放后的操作
				if (mCurrentState == STATE_PULL_DOWN_REFRESH)
				{
					// 如果是 下拉刷新状态，直接缩回去
					mHeaderLayout.setPadding(0, -mRefreshViewHeight, 0, 0);
				}
				else if (mCurrentState == STATE_RELEASE_REFRESH)
				{
					// 如果是释放刷新状态，用户希望去 刷新数据--》正在刷新状态
					mCurrentState = STATE_REFRESHING;

					// 设置paddingTop 为0
					mHeaderLayout.setPadding(0, 0, 0, 0);

					// UI更新
					refreshUI();

					// 通知 调用者 现在处于 正在刷新状态
					if (mRefreshListener != null)
					{
						mRefreshListener.onRefreshing();
					}

				}
				break;
			default:
				break;
		}
		return super.onTouchEvent(ev);
	}

	private void refreshUI()
	{
		switch (mCurrentState)
		{
			case STATE_PULL_DOWN_REFRESH:// 下拉刷新
				// 1.箭头要显示，进度条隐藏
				mPb.setVisibility(View.GONE);
				mIvArrow.setVisibility(View.VISIBLE);
				// 2. 状态显示
				mTvState.setText("下拉刷新");

				// 3. 箭头动画:
				mIvArrow.startAnimation(mDown2UpAnimation);

				break;
			case STATE_RELEASE_REFRESH:// 松开刷新
				// 1.箭头要显示，进度条隐藏
				mPb.setVisibility(View.GONE);
				mIvArrow.setVisibility(View.VISIBLE);
				// 2. 状态显示
				mTvState.setText("松开刷新");

				// 3. 箭头动画:
				mIvArrow.startAnimation(mUp2DownAnimation);
				break;
			case STATE_REFRESHING:// 正在刷新
				mIvArrow.clearAnimation();
				// 1.箭头隐藏，进度条显示
				mPb.setVisibility(View.VISIBLE);
				mIvArrow.setVisibility(View.GONE);
				// 2. 状态显示
				mTvState.setText("正在刷新");
				break;

			default:
				break;
		}
	}

	public void setOnRefreshListener(OnRefreshListener listener)
	{
		this.mRefreshListener = listener;
	}

	public interface OnRefreshListener
	{
		/**
		 * 正在刷新时的回调
		 */
		void onRefreshing();

		/**
		 * 加载更多的时候的回调
		 */
		void onLoadingMore();
	}

	/**
	 * 告知 ListView刷新完成
	 */
	public void refreshFinish()
	{
		if (isLoadingMore)
		{
			// 上拉加载
			mFooterLayout.setPadding(0, -mFooterHeight, 0, 0);

			isLoadingMore = false;
		}
		else
		{
			// 下拉刷新
			Log.d(TAG, "刷新结束");
			// 设置当前更新的时间
			mTvUpdateTime.setText(getCurrentTimeString());

			// 隐藏 刷新的View
			mHeaderLayout.setPadding(0, -mRefreshViewHeight, 0, 0);

			// 状态重置
			mCurrentState = STATE_PULL_DOWN_REFRESH;

			// UI更新
			refreshUI();
		}
	}

	private String getCurrentTimeString()
	{
		long time = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(new Date(time));
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState)
	{

		// 最后一个可见的时候
		int lastVisiblePosition = getLastVisiblePosition();
		if (lastVisiblePosition == getAdapter().getCount() - 1)
		{
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE
					|| scrollState == OnScrollListener.SCROLL_STATE_TOUCH_SCROLL)
			{
				if (!isLoadingMore)
				{
					// 滑动到了底部
					Log.d(TAG, "滑动到底部");
					// UI操作
					mFooterLayout.setPadding(0, 0, 0, 0);

					// 自动默认选中i
					setSelection(getAdapter().getCount());

					// 是滑动到底部
					isLoadingMore = true;

					// 通知状态变化，
					if (mRefreshListener != null)
					{
						mRefreshListener.onLoadingMore();
					}

				}

			}
		}

	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount)
	{
		// TODO Auto-generated method stub

	}
}
