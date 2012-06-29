package com.androidbook.hello;


import android.app.Activity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.*;

public class HelloAndroidActivity extends TabActivity {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// tabHost是一个标签容器
		TabHost tabHost = this.getTabHost();

		// 每一个TabSpec对象就是个标签
		// TabSpec.setIndicator()方法是设置标签显示样式
		// TabSpec.setContent()方法显示标签下方的内容显示

		// 定义第一个标签
		tabHost.addTab(tabHost
				.newTabSpec("OneTab")
				.setIndicator("OneTab",getResources().getDrawable(android.R.drawable.star_on))
				.setContent(R.id.linearLayout1));

		// 定义第二个标签
		tabHost.addTab(tabHost
				.newTabSpec("TwoTab")
				.setIndicator("TwoTab",getResources().getDrawable(android.R.drawable.star_off))
				.setContent(R.id.linearLayout2));

		// 定义第三个标签
		tabHost.addTab(tabHost
				.newTabSpec("ThreeTab")
				.setIndicator("ThreeTab",getResources().getDrawable(android.R.drawable.stat_notify_call_mute))
				.setContent(R.id.linearLayout3));

	}

} 