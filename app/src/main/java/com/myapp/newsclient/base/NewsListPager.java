package com.myapp.newsclient.base;

import android.content.Context;
import android.os.Handler;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.lidroid.xutils.BitmapUtils;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.ViewUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;
import com.lidroid.xutils.view.annotation.ViewInject;
import com.myapp.newsclient.R;
import com.myapp.newsclient.bean.NewsCenterBean;
import com.myapp.newsclient.bean.NewsListBean;
import com.myapp.newsclient.utils.CacheUtils;
import com.myapp.newsclient.utils.Constans;
import com.myapp.newsclient.widget.HorizontalScrollViewPager;
import com.myapp.newsclient.widget.RefreshListView;

import java.util.List;

/**
 * @包名: com.myapp.newsclient.base
 * @作者: haoshul
 * @时间: 2016/10/25 14:48
 * @描述:  新闻页面对应的list页面
 */

public class NewsListPager extends NewsCenterBaseMenu implements ViewPager.OnPageChangeListener, RefreshListView.OnRefreshListener
{

    protected static final String			TAG	= "NewsListPager";

    @ViewInject(R.id.news_list_pic_pager)
    private HorizontalScrollViewPager mPager;

    @ViewInject(R.id.news_list_pic_title)
    private TextView mTvTitle;

    @ViewInject(R.id.news_list_point_container)
    private LinearLayout mPointContainer;

    @ViewInject(R.id.news_list_item_list)
    private RefreshListView mListView;

    private NewsCenterBean.NewsCenterNewsItemBean mData;

    private List<NewsListBean.NewsListPagerTopnewsBean> mPicDatas;

    private BitmapUtils mBitmapUtils;

    private AutoSwitchPicTask				mSwitchPicTask;

    private List<NewsListBean.NewsListPagerNewsBean>		mNewsDatas;

    private String							mMoreUrl;

    private NewsAdapter						mNewsAdapter;

    public NewsListPager(Context context, NewsCenterBean.NewsCenterNewsItemBean data) {
        super(context);
        this.mData = data;

        mBitmapUtils = new BitmapUtils(mContext);
    }

    @Override
    protected View initView()
    {
        View view = View.inflate(mContext, R.layout.news_list_pager, null);

        // 注入
        ViewUtils.inject(this, view);

        View topNewsView = View.inflate(mContext, R.layout.news_top, null);
        ViewUtils.inject(this, topNewsView);

        // 给listView添加HeaderView
        mListView.addCustomHeaderView(topNewsView);

        // 设置刷新的监听
        mListView.setOnRefreshListener(this);

        return view;
    }

    @Override
    public void initData()
    {
        // 去网络加载数据
        loadNetData();

    }

    private void loadNetData()
    {
        final String url = Constans.SERVER_URL + mData.url;

        String json = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(json))
        {
            processData(json);
        }

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;

                Log.d(TAG, "网络数据正确返回 : " + result);

                // 存缓存
                CacheUtils.setString(mContext, url, result);

                // 处理数据
                processData(result);
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Log.d(TAG, "网络数据失败 : " + msg);
            }
        });

    }

    private void processData(String json)
    {
        // 1. json串的解析
        Gson gson = new Gson();
        NewsListBean bean = gson.fromJson(json, NewsListBean.class);
        mPicDatas = bean.data.topnews;
        mNewsDatas = bean.data.news;
        mMoreUrl = bean.data.more;

        // 给Viewpager加载数据-->adapter--->list
        mPager.setAdapter(new NewsTopPicAdapter());

        // 动态添加点
        // 1.清空点
        mPointContainer.removeAllViews();

        // 2.加点
        for (int i = 0; i < mPicDatas.size(); i++)
        {
            View point = new View(mContext);

            point.setBackgroundResource(R.drawable.dot_normal);// 默认图片
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(5, 5);
            params.leftMargin = 10;

            if (i == 0)
            {
                mTvTitle.setText(mPicDatas.get(i).title);
                point.setBackgroundResource(R.drawable.dot_focus);
            }

            mPointContainer.addView(point, params);
        }

        // 3.切换点
        mPager.setOnPageChangeListener(this);

        // 处理延时轮播
        if (mSwitchPicTask == null)
        {
            mSwitchPicTask = new AutoSwitchPicTask();
        }
        mSwitchPicTask.start();

        // 设置ViewPager的touch监听
        mPager.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event)
            {
                switch (event.getAction())
                {
                    case MotionEvent.ACTION_DOWN:
                        // 停止轮播
                        mSwitchPicTask.stop();
                        break;
                    case MotionEvent.ACTION_UP:
                    case MotionEvent.ACTION_CANCEL:
                        // 开启
                        mSwitchPicTask.start();
                        break;
                    default:
                        break;
                }

                return false;
            }
        });

        mNewsAdapter = new NewsAdapter();
        mListView.setAdapter(mNewsAdapter);
    }

    class NewsAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            if (mNewsDatas != null) { return mNewsDatas.size(); }
            return 0;
        }

        @Override
        public Object getItem(int position)
        {
            if (mNewsDatas != null) { return mNewsDatas.get(position); }
            return null;
        }

        @Override
        public long getItemId(int position)
        {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent)
        {
            ViewHolder holder = null;

            if (convertView == null)
            {
                // 没有复用
                convertView = View.inflate(mContext, R.layout.item_news, null);
                holder = new ViewHolder();

                holder.ivIcon = (ImageView) convertView.findViewById(R.id.item_news_iv_icon);
                holder.tvTitle = (TextView) convertView.findViewById(R.id.item_news_tv_title);
                holder.tvDate = (TextView) convertView.findViewById(R.id.item_news_tv_time);

                convertView.setTag(holder);
            }
            else
            {
                // 有复用
                holder = (ViewHolder) convertView.getTag();
            }

            NewsListBean.NewsListPagerNewsBean bean = mNewsDatas.get(position);

            holder.tvTitle.setText(bean.title);
            holder.tvDate.setText(bean.pubdate);

            mBitmapUtils.display(holder.ivIcon, bean.listimage);

            return convertView;
        }

    }

    static class ViewHolder
    {
        ImageView	ivIcon;
        TextView	tvTitle;
        TextView	tvDate;
    }

    class AutoSwitchPicTask extends Handler implements Runnable
    {
        private final static long	DELAYED	= 2000;

        // 开始轮播
        public void start()
        {
            stop();
            postDelayed(this, DELAYED);
        }

        @Override
        public void run()
        {
            // 让viewpager 选中下一个
            int item = mPager.getCurrentItem();
            // if (item == mPager.getAdapter().getCount() - 1)
            // {
            // item = -1;
            // }
            // mPager.setCurrentItem(++item);

            if (item == mPager.getAdapter().getCount() - 1)
            {
                mPager.setCurrentItem(0);
            }
            else
            {
                mPager.setCurrentItem(++item);
            }

            // 再次执行
            postDelayed(this, DELAYED);
        }

        // 停止轮播
        public void stop()
        {
            removeCallbacksAndMessages(null);
        }
    }

    class NewsTopPicAdapter extends PagerAdapter
    {

        @Override
        public int getCount()
        {
            if (mPicDatas != null) { return mPicDatas.size(); }
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
            ImageView iv = new ImageView(mContext);
            iv.setScaleType(ImageView.ScaleType.FIT_XY);
            iv.setImageResource(R.drawable.pic_item_list_default);// 设置默认资源

            // 设置网络图片
            NewsListBean.NewsListPagerTopnewsBean bean = mPicDatas.get(position);
            String imgUrl = bean.topimage;

            // 网络获取数据
            mBitmapUtils.display(iv, imgUrl);

            // 添加iv
            container.addView(iv);

            return iv;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object)
        {
            container.removeView((View) object);
        }
    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onPageSelected(int position)
    {
        // 点进行切换
        int count = mPointContainer.getChildCount();
        for (int i = 0; i < count; i++)
        {
            View view = mPointContainer.getChildAt(i);
            view.setBackgroundResource(R.drawable.dot_normal);
        }
        mPointContainer.getChildAt(position).setBackgroundResource(R.drawable.dot_focus);

        // 设置pic title
        NewsListBean.NewsListPagerTopnewsBean bean = mPicDatas.get(position);
        mTvTitle.setText(bean.title);
    }

    @Override
    public void onPageScrollStateChanged(int state)
    {
        // TODO Auto-generated method stub

    }

    @Override
    public void onRefreshing()
    {
        // 到网络去获取数据
        Log.d(TAG, "正在刷新......");

        final String url = Constans.SERVER_URL + mData.url;

        String json = CacheUtils.getString(mContext, url);
        if (!TextUtils.isEmpty(json))
        {
            processData(json);
        }

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;

                Log.d(TAG, "网络数据正确返回 : " + result);

                // 存缓存
                CacheUtils.setString(mContext, url, result);

                // 处理数据
                processData(result);

                // 告诉listView该收起 刷新的view
                mListView.refreshFinish();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Log.d(TAG, "网络数据失败 : " + msg);

                // 告诉listView该收起 刷新的view
                mListView.refreshFinish();
            }
        });

    }

    @Override
    public void onLoadingMore()
    {

        // 去网络加载数据
        if (TextUtils.isEmpty(mMoreUrl))
        {
            // 告知listView加载完成
            mListView.refreshFinish();

            Toast.makeText(mContext, "没有更多数据", Toast.LENGTH_SHORT).show();
            return;
        }

        String url = Constans.SERVER_URL + mMoreUrl;

        HttpUtils utils = new HttpUtils();
        utils.send(HttpRequest.HttpMethod.GET, url, new RequestCallBack<String>() {

            @Override
            public void onSuccess(ResponseInfo<String> responseInfo)
            {
                String result = responseInfo.result;

                Log.d(TAG, "网络数据正确返回 : " + result);

                // 给mNewsData.add
                Gson gson = new Gson();
                NewsListBean bean = gson.fromJson(result, NewsListBean.class);
                List<NewsListBean.NewsListPagerNewsBean> list = bean.data.news;
                mNewsDatas.addAll(list);

                mMoreUrl = bean.data.more;

                // adapter刷新
                mNewsAdapter.notifyDataSetChanged();

                // 告诉listView该收起 刷新的view
                mListView.refreshFinish();
            }

            @Override
            public void onFailure(HttpException error, String msg)
            {
                Log.d(TAG, "网络数据失败 : " + msg);

                // 告诉listView该收起 刷新的view
                mListView.refreshFinish();
            }
        });

    }

}
