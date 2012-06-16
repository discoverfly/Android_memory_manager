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
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.*;


@SuppressWarnings("deprecation")
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
				.setIndicator("Process",getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getActivityInfo();
					}
				})
				);

		tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG)
				.setIndicator(LIST2_TAB_TAG,getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
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

	private View getActivityInfo() {

		ListView listView = (ListView) findViewById(R.id.list1);
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningAppProcessInfo> out = activityManager.getRunningAppProcesses();

		Map<String,Object> tmap; 

		List<Map<String,Object>> activityList = new ArrayList<Map<String,Object>>();
		Packages pk = new Packages(this.getApplicationContext());
		PackageManager pkm = this.getApplicationContext().getPackageManager();
		ApplicationInfo appInfo;
		
		for (ActivityManager.RunningAppProcessInfo it:out){
			tmap = new HashMap<String, Object>();
			appInfo= pk.appInfo(it.processName);
			tmap.put("icon",this.getResources().getDrawable(R.drawable.helloworld));
			tmap.put("title",appInfo.loadLabel(pkm).toString());
			tmap.put("name",it.processName);
			tmap.put("pid","PID: "+it.pid);
			int[] myMempid = new int[] { it.pid }; 
			Debug.MemoryInfo[] memoryInfo = activityManager  
					.getProcessMemoryInfo(myMempid);  

			int memSize = memoryInfo[0].dalvikPrivateDirty;  // 获取进程占内存用信息 kb单位  
			tmap.put("memsize","MEM: " + memSize + " KB");
			activityList.add(tmap);
		}

		SimpleAdapter activityAdapter =
				new SimpleAdapter(this,activityList,R.layout.listitem,
				new String[]{"icon","title","name","pid","memsize"},
				new int[]{R.id.icon,R.id.title,R.id.name,R.id.pid,R.id.memsize});
		
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

}
