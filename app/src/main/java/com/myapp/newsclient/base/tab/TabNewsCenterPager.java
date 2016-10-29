package com.myapp.newsclient.base.tab;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.myapp.newsclient.activity.MainActivity;
import com.myapp.newsclient.base.NewsCenterBaseMenu;
import com.myapp.newsclient.base.TabBasePager;
import com.myapp.newsclient.base.newscentermenu.NewsCenterInteractMenu;
import com.myapp.newsclient.base.newscentermenu.NewsCenterNewsMenu;
import com.myapp.newsclient.base.newscentermenu.NewsCenterPicMenu;
import com.myapp.newsclient.base.newscentermenu.NewsCenterTopicMenu;
import com.myapp.newsclient.bean.NewsCenterBean;
import com.myapp.newsclient.fragment.LeftMenuFragment;
import com.myapp.newsclient.utils.CacheUtils;
import com.myapp.newsclient.utils.Constans;

import java.util.ArrayList;
import java.util.List;

/**

 * @描述: 主页界面中tab对应的新闻中心页面
 */
public class TabNewsCenterPager extends TabBasePager
{

	protected static final String		TAG	= "TabNewsCenterPager";

	private List<NewsCenterBaseMenu>	mPagerList;				// 菜单页面的集合

	private NewsCenterBean mData;						// 页面对应的数据

	private List<NewsCenterBean.NewCenterMenuListBean>	mMenuDatas;				// 菜单对应的数据集合

	public TabNewsCenterPager(Context context) {
		super(context);
	}

	@Override
	public void initData()
	{
		// 1. title部分数据的设置
		mTvTitle.setText("新闻");
		mIvMenu.setVisibility(View.VISIBLE);

		// 2. 内容区域数据的设置 TODO:
		// TextView tv = new TextView(mContext);
		// tv.setText("新闻内容区域");
		// tv.setTextColor(Color.RED);
		// tv.setTextSize(24);
		// tv.setGravity(Gravity.CENTER);
		//
		// LayoutParams params = new LayoutParams(LayoutParams.MATCH_PARENT,
		// LayoutParams.MATCH_PARENT);
		// mContentContainer.addView(tv, params);

		// 读取缓存数据
		String json = CacheUtils.getString(mContext, Constans.NEWS_CENTER_URL);
		if (!TextUtils.isEmpty(json))
		{
			// 有缓存数据
			Log.d(TAG, "读取 本地缓存");
			processData(json);
		}

		// 通过网络获取数据,将数据加载到页面上来
		HttpUtils utils = new HttpUtils();

		// RequestParams params = new RequestParams();
		// 1.消息头
		// params.addHeader("", "");
		// 1.请求参数
		// post :
		// NameValuePair pair = new BasicNameValuePair("", "");
		// params.addBodyParameter(pair);
		// get:
		// NameValuePair pair = new BasicNameValuePair("name", "x q");
		// params.addQueryStringParameter(pair);
		// utils.send(HttpMethod.GET, Constans.NEWS_CENTER_URL, params,
		// callBack)

		utils.send(HttpRequest.HttpMethod.GET, Constans.NEWS_CENTER_URL, new RequestCallBack<String>() {

			// 访问网络成功后的回调
			@Override
			public void onSuccess(ResponseInfo<String> responseInfo)
			{
				// 取出结果值
				String result = responseInfo.result;

				Log.d(TAG, "访问网络成功 : " + result);

				// 缓存数据
				CacheUtils.setString(mContext, Constans.NEWS_CENTER_URL, result);

				Log.d(TAG, "读取 网络数据");

				// 对数据进行解析，并且将结果展示到页面上
				processData(result);
			}

			// 访问网络失败后的回调
			@Override
			public void onFailure(HttpException error, String msg)
			{
				error.printStackTrace();
				Log.d(TAG, "访问网络失败 : " + msg);
			}
		});
	}

	// 对数据进行解析，并且将结果展示到页面上
	private void processData(String json)
	{
		// 1. json串的解析
		Gson gson = new Gson();
		mData = gson.fromJson(json, NewsCenterBean.class);
		mMenuDatas = mData.data;

		// 2.将数据展示到界面上
		// 2-1 : 展示到左侧菜单
		// 获取左侧菜单的fragment
		MainActivity ui = (MainActivity) mContext;
		LeftMenuFragment leftfra = ui.getLeftFragment();
		// 设置数据
		leftfra.setMenuData(mMenuDatas);

		// 2-2 : 展示到内容区域
		mPagerList = new ArrayList<NewsCenterBaseMenu>();
		for (int i = 0; i < mMenuDatas.size(); i++)
		{
			NewsCenterBean.NewCenterMenuListBean bean = mMenuDatas.get(i);
			NewsCenterBaseMenu menuPager = null;
			switch (bean.type)
			{
				case 1:
					// 新闻
					menuPager = new NewsCenterNewsMenu(mContext, bean);
					break;
				case 10:
					// 专题
					menuPager = new NewsCenterTopicMenu(mContext);
					break;
				case 2:
					// 组图
					menuPager = new NewsCenterPicMenu(mContext);
					break;
				case 3:
					// 互动
					menuPager = new NewsCenterInteractMenu(mContext);
					break;

				default:
					break;
			}

			mPagerList.add(menuPager);
		}

		// 设置内容区域视图的展示默认值
		switchMenuPager(0);
	}

	@Override
	public void switchMenuPager(int position)
	{
		Log.d(TAG, "切换到 第" + position + "菜单");

		// 清空内容的数据
		mContentContainer.removeAllViews();

		// 设置Title显示
		NewsCenterBean.NewCenterMenuListBean bean = mMenuDatas.get(position);
		mTvTitle.setText(bean.title);

		// 页面切换
		NewsCenterBaseMenu menuPager = mPagerList.get(position);
		View view = menuPager.getRootView();

		mContentContainer.addView(view);

		// 加载数据
		menuPager.initData();
	}
}
