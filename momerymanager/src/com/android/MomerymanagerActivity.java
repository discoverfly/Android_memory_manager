package com.android;

import java.util.ArrayList;
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
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.Toast;
import android.content.*;

/**
 * This activity allows you to have multiple views (in this case two {@link ListView}s)
 * in one tab activity.  The advantages over separate activities is that you can
 * maintain tab state much easier and you don't have to constantly re-create each tab
 * activity when the tab is selected.
 */
public class MomerymanagerActivity extends TabActivity implements OnTabChangeListener {

	private static final String LIST1_TAB_TAG = "Activity";
	private static final String LIST2_TAB_TAG = "Service";
	private TabHost tabHost;
	private ActivityManager activityManager; 

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);

		tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG).setIndicator(LIST1_TAB_TAG).setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return getActivityInfo();
			}
		}));

		tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG).setIndicator(LIST2_TAB_TAG).setContent(new TabHost.TabContentFactory() {
			public View createTabContent(String tag) {
				return getServiceInfo();
			}
		}));
		
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
//		listStrings.add("Item 1");
//		listStrings.add("Item 2");
//		listStrings.add("Item 3");
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningServiceInfo> out = activityManager.getRunningServices(10);
		String res = null;
		String[] ars = null;
		for (int i= 0; i  < out.size(); ++i){
			res = out.get(i).toString();
			ars = res.split("\\.|\\$|@");
			listStrings.add(res);
		}

		listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, listStrings));

		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView parent, View view, int position, long id) { 
				String item = (String) parent.getAdapter().getItem(position);
				if(item != null){
					((ArrayAdapter)parent.getAdapter()).remove(item);
					Toast.makeText(getApplicationContext(), item + " has been removed", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return listView;
	}

	private View getActivityInfo() {

		ListView listView = (ListView) findViewById(R.id.list1);
		List<String> listStrings = new ArrayList<String>();
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningAppProcessInfo> out = activityManager.getRunningAppProcesses();
		String res = null;
		String[] ars = null;
		for (int i= 0; i  < out.size(); ++i){
			res = out.get(i).toString();
			ars = res.split("\\.|\\$|@");
			listStrings.add(res);
		}
		
		listView.setAdapter(new ArrayAdapter(this, android.R.layout.simple_list_item_1, listStrings));
		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
			public void onItemClick(AdapterView parent, View view, int position, long id) { 
				String item = (String) parent.getAdapter().getItem(position);
				if(item != null){
					((ArrayAdapter)parent.getAdapter()).remove(item);
					//Toast.makeText(getApplicationContext(),out.toString(), Toast.LENGTH_LONG);
					Toast.makeText(getApplicationContext(), item + " has been removed", Toast.LENGTH_SHORT).show();
				}
			}
		});
		return listView;
	}

}
