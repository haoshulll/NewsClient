package com.myapp.newsclient.bean;

import java.util.List;

/**
 *
 * @描述 : newslist页面的bean
 *
 * 
 */
public class NewsListBean
{

	public NewsListPagerBean	data;
	public int					retcode;

	public class NewsListPagerBean
	{
		public String							countcommenturl;
		public String							more;
		public List<NewsListPagerNewsBean>		news;
		public String							title;
		public List<NewsListPagerTopicBean>		topic;
		public List<NewsListPagerTopnewsBean>	topnews;
	}

	public class NewsListPagerNewsBean
	{
		public boolean	comment;
		public String	commentlist;
		public String	commenturl;
		public long		id;
		public String	listimage;
		public String	pubdate;
		public String	title;
		public String	type;
		public String	url;
	}

	public class NewsListPagerTopicBean
	{
		public String	description;
		public long		id;
		public String	listimage;
		public int		sort;
		public String	title;
		public String	url;
	}

	public class NewsListPagerTopnewsBean
	{
		public boolean	comment;
		public String	commentlist;
		public String	commenturl;
		public long		id;
		public String	pubdate;
		public String	title;
		public String	topimage;
		public String	type;
		public String	url;
	}
}
