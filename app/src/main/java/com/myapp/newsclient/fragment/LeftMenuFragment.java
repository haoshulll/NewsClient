package com.myapp.newsclient.fragment;

import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.myapp.newsclient.R;
import com.myapp.newsclient.activity.MainActivity;
import com.myapp.newsclient.base.BaseFragment;
import com.myapp.newsclient.bean.NewsCenterBean;

import java.util.List;

/**
 * @包名: com.myapp.newsclient.fragment
 * @作者: haoshul
 * @时间: 2016/10/16 20:44
 * @描述: TODO
 */

public class LeftMenuFragment extends BaseFragment implements AdapterView.OnItemClickListener
{

    private List<NewsCenterBean.NewCenterMenuListBean> mMenuDatas;	// 菜单对应的数据
    private ListView mListView;		// 页面的listView
    private int							mCurrentItem;	// 当前选中项
    private LeftMenuAdapter				mMenuAdapter;

    @Override
    protected View initView()
    {
        // TextView tv = new TextView(mActivity);
        // tv.setText("菜单内容");
        // return tv;

        mListView = new ListView(mActivity);

        // 给listView 设置样式
        mListView.setBackgroundColor(Color.BLACK);// 背景设置
        mListView.setDividerHeight(0);// 去掉分割线
        mListView.setPadding(0, 40, 0, 0);// 设置paddingTop
        mListView.setCacheColorHint(android.R.color.transparent);// 去掉缓存颜色
        mListView.setSelector(android.R.color.transparent);// 去掉selector

        // 给listView设置item点击事件
        mListView.setOnItemClickListener(this);

        return mListView;
    }

    /**
     * 给菜单设置数据
     *
     * @param datas
     */
    public void setMenuData(List<NewsCenterBean.NewCenterMenuListBean> datas)
    {
        // 设置默认选中第一个item
        this.mCurrentItem = 0;

        // 数据接收
        this.mMenuDatas = datas;

        // 数据设置 --->adapter --->list
        mMenuAdapter = new LeftMenuAdapter();
        mListView.setAdapter(mMenuAdapter);
    }

    class LeftMenuAdapter extends BaseAdapter
    {

        @Override
        public int getCount()
        {
            if (mMenuDatas != null) { return mMenuDatas.size(); }
            return 0;
        }

        @Override
        public Object getItem(int position)
        {
            if (mMenuDatas != null) { return mMenuDatas.get(position); }
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

            if (convertView == null)
            {
                // 没有复用时
                convertView = View.inflate(mActivity, R.layout.item_menu, null);
            }
            // else
            // {
            // // 复用时
            // }

            TextView tv = (TextView) convertView;

            // 给视图加载数据
            NewsCenterBean.NewCenterMenuListBean bean = mMenuDatas.get(position);
            tv.setText(bean.title);

            // 判断默认选中项
            // if (mCurrentItem == position)
            // {
            // // 让textView enable
            // tv.setEnabled(true);
            // } else {
            // // 让textView disable
            // tv.setEnabled(false);
            // }
            tv.setEnabled(mCurrentItem == position);

            // 返回显示数据的View
            return convertView;
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        // 不做处理
        if (mCurrentItem == position) { return; }

        // 1. 选中对应的项
        this.mCurrentItem = position;
        // UI更新
        mMenuAdapter.notifyDataSetChanged();

        // 2. 收起菜单
        MainActivity ui = (MainActivity) mActivity;
        ui.getSlidingMenu().toggle();

        // 3. 右侧内容区域改变
        ContentFragment contentFra = ui.getContentFragment();
        contentFra.switchMenuPager(mCurrentItem);

    }

}
