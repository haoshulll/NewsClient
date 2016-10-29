package com.myapp.newsclient.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.myapp.newsclient.R;
import com.myapp.newsclient.utils.CacheUtils;

import java.util.ArrayList;
import java.util.List;

public class GuideActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener,View.OnClickListener {

    private ViewPager mPager;			// viewPager
    private Button mBtnStart;			// 开启按钮
    private LinearLayout mPointContainer;	// 装点的容器
    private View mSelectedPoint;	// 选中的点
    private int	mSpace;			// 点与点间的距离
    private List<ImageView> mImgList;			// 存储viewPager中的imageView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

        mPager = (ViewPager) findViewById(R.id.guide_pager);
        mBtnStart = (Button) findViewById(R.id.guide_btn_start);
        mPointContainer = (LinearLayout) findViewById(R.id.guide_point_container);
        mSelectedPoint = findViewById(R.id.guide_point_selected);

        mBtnStart.setOnClickListener(this);

        initData();
        // 计算点与点之间的距离
        mSelectedPoint.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                // 当 UI的树布局改变时调用
                if (mImgList == null){return;}
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                    mSelectedPoint.getViewTreeObserver().removeOnGlobalLayoutListener(this);
                }
                mSpace = mPointContainer.getChildAt(1).getLeft() - mPointContainer.getChildAt(0).getLeft();
            }
        });

    }

    private void initData() {
        int[] imgRes = new int[]{R.drawable.guide_1,R.drawable.guide_2,R.drawable.guide_3};
        mImgList  = new ArrayList<>();
        for (int i=0; i<imgRes.length; i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(imgRes[i]);
            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
            mImgList.add(imageView);

            //动态添加点
            View point = new View(this);
            point.setBackgroundResource(R.drawable.guide_point_normal);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(10,10);//10px
            if (i != 0){
                params.leftMargin = 10;
            }
            mPointContainer.addView(point,params);
        }

        mPager.setAdapter(new GuidePagerAdapter());

        mPager.setOnPageChangeListener(this);
    }

    // 当viewPager 正在滑动时的回调
    // @position : 当前所处的页面
    // @positionOffset : 指的是百分比
    // @positionOffsetPixels: 实际滑动的距离px
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        // 1. 去对滑动的点做操作
        // 2. 设置marginLeft
        RelativeLayout.LayoutParams params = (android.widget.RelativeLayout.LayoutParams) mSelectedPoint.getLayoutParams();
        params.leftMargin = (int) (mSpace * position + mSpace * positionOffset + 0.5f);// 四舍五入

        mSelectedPoint.setLayoutParams(params);
    }

    // 当viewPager 某个页面选中时的回调
    // @position:当前选中的位置
    @Override
    public void onPageSelected(int position) {
        // if (position == mImgList.size() - 1)
        // {
        // // 显示button
        // mBtnStart.setVisibility(View.VISIBLE);
        // }
        // else
        // {
        // // 隐藏button
        // mBtnStart.setVisibility(View.GONE);
        // }
        mBtnStart.setVisibility(position == mImgList.size() - 1 ? View.VISIBLE : View.GONE);
    }

    // 当viewpager 的滑动状态改变时的回调
    // @state:状态值
    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public void onClick(View v)
    {
        if (v == mBtnStart)
        {
            go2Main();
        }
    }

    private void go2Main()
    {
        // 保存已经不是第一次登录了
        CacheUtils.setBoolean(this, WelcomeActivity.KEY_IS_FIRST, false);

        Intent intent = new Intent(GuideActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }

    class GuidePagerAdapter extends PagerAdapter{

        @Override
        public int getCount() {
            if (mImgList != null){return mImgList.size();}
            return 0;
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView  = mImgList.get(position);
            container.addView(imageView);
            return imageView;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }
}
