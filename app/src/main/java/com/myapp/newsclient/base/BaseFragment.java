package com.myapp.newsclient.base;


import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * @包名: com.myapp.newsclient.base
 * @作者: haoshul
 * @时间: 2016/10/16 20:30
 * @描述: fragment的基类
 */

public abstract class BaseFragment extends Fragment {

    protected Activity mActivity;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        this.mActivity = getActivity();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return initView();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        //数据加载
        initData();
    }

    protected abstract View initView();


    /**
     * 数据加载的方法，子类如果需要加载数据，就复写这个方法
     */
    protected void initData()
    {
    }
}
