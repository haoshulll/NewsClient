package com.myapp.newsclient.bean;

import java.util.List;

/**
 * @描述 : 新闻页面 网络返回的数据bean
 * 
 */
public class NewsCenterBean
{
	public List<NewCenterMenuListBean>	data;
	public List<Long>					extend;
	public int							retcode;

	public class NewCenterMenuListBean
	{
		public List<NewsCenterNewsItemBean>	children;
		public long							id;
		public String						title;
		public int							type;

		public String						url;
		public String						url1;

		public String						dayurl;
		public String						excurl;
		public String						weekurl;
	}

	public class NewsCenterNewsItemBean
	{
		public long		id;
		public String	title;
		public int		type;
		public String	url;
	}

}
