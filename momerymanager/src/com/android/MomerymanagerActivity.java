package com.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ListActivity;
import android.app.TabActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import android.content.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.*;
import android.widget.SimpleAdapter.ViewBinder;


public class MomerymanagerActivity extends TabActivity implements OnTabChangeListener {

	private static final String LIST1_TAB_TAG = "Process";
	private static final String LIST2_TAB_TAG = "Service";
	private TabHost tabHost;
	private ActivityManager activityManager; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);

		tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG)
				.setIndicator("Process",getApplicationContext().getResources().getDrawable(android.R.drawable.star_on))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getActivityInfoView();
					}
				})
				);

		tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG)
				.setIndicator(LIST2_TAB_TAG,getApplicationContext().getResources().getDrawable(android.R.drawable.btn_star))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getServiceInfo();
					}
				})
				);

	}

	public void onTabChanged(String tabName) {
		if(tabName.equals(LIST2_TAB_TAG)) {
			//do something
		}
		else if(tabName.equals(LIST1_TAB_TAG)) {
			//do something
		}
	}

	private View getServiceInfo() {

		ListView listView = (ListView) findViewById(R.id.list2);

		List<String> listStrings = new ArrayList<String>();

		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningServiceInfo> out = activityManager.getRunningServices(10);
		String res = null;
		String[] ars = null;
		for (int i= 0; i  < out.size(); ++i){
			res = out.get(i).toString();
			ars = res.split("\\.|\\$|@");
			listStrings.add(res);
		}

		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listStrings));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
				String item = (String) parent.getAdapter().getItem(position);
				if(item != null){
					((ArrayAdapter)parent.getAdapter()).remove(item);
					Toast.makeText(getApplicationContext(), item + " has been removed", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return listView;
	}

	private View getActivityInfoView() {

		ListView listView = (ListView) findViewById(R.id.list1);
		List<Map<String,Object>> activityList = getProcessAdapterList();
		SimpleAdapter activityAdapter =
				new SimpleAdapter(this,activityList,R.layout.listitem,
						new String[]{"icon","title","name","pid","memsize"},
						new int[]{R.id.icon,R.id.title,R.id.name,R.id.pid,R.id.memsize});

		activityAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if ((view instanceof ImageView) && (data instanceof Drawable)) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable)data);
					return true;
				}
				return false;
			}
		});
		listView.setAdapter(activityAdapter);

		
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
				Map<String,Object> item = (Map<String,Object>) parent.getAdapter().getItem(position);
				if(item != null){
					Toast.makeText(getApplicationContext(), item.toString(), Toast.LENGTH_SHORT).show();
				}
			}
		});
		return listView;
	}
	
	public List<Map<String,Object>> getProcessAdapterList(){
		Map<String,Object> tmap; 
		List<Map<String,Object>> activityList = new ArrayList<Map<String,Object>>();
		Packages pk = new Packages(this.getApplicationContext());
		ArrayList<Process> pArrayList = pk.getProcesList();
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			tmap.put("name",it.getProcessName());
			tmap.put("pid"," PID: " + it.getPid());
			tmap.put("memsize","   MEM: " + it.getMemSize() + " KB");
			activityList.add(tmap);
		}
		return activityList;
	}

}
