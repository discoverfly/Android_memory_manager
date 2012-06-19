package com.android;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.app.ListActivity;
import android.app.TabActivity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
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
import android.app.Dialog;;

public class MomerymanagerActivity extends TabActivity implements OnTabChangeListener {

	private static final String LIST1_TAB_TAG = "Process";
	private static final String LIST2_TAB_TAG = "Task";
	private static final String LIST3_TAB_TAG = "Service";
	private TabHost tabHost;
	private ActivityManager activityManager; 
	private List<Map<String,Object>> activityList;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		tabHost.addTab(tabHost.newTabSpec(LIST1_TAB_TAG)
				.setIndicator("Process",getApplicationContext().getResources().getDrawable(R.drawable.process))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getActivityInfoView();
					}
				})
				);
		tabHost.addTab(tabHost.newTabSpec(LIST3_TAB_TAG)
				.setIndicator("Task",getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getTaskInfoView();
					}
				})
				);

		tabHost.addTab(tabHost.newTabSpec(LIST3_TAB_TAG)
				.setIndicator(LIST3_TAB_TAG,getApplicationContext().getResources().getDrawable(R.drawable.task))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						return getServiceInfoView();
					}
				})
				);

	}

	public void onTabChanged(String tabName) {
		if(tabName.equals(LIST1_TAB_TAG)) {
			//do something
		}
		else if(tabName.equals(LIST2_TAB_TAG)) {
			//do something
		} else if (tabName.equals(LIST3_TAB_TAG)){

		}
	}

	private View getServiceInfoView() {

		ListView listView = (ListView) findViewById(R.id.list3);
		List<String> listStrings = new ArrayList<String>();
		List<ActivityManager.RunningServiceInfo> out = activityManager.getRunningServices(10);
		String res = null;
		for (ActivityManager.RunningServiceInfo it:out){
			res = it.toString();

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
		SimpleAdapter processAdapter = getProcessAdapter();
		listView.setAdapter(getProcessAdapter());


		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) { 
				new AlertDialog.Builder(parent.getContext())
				.setMessage("Kill the process?")
				.setPositiveButton("YES", new DialogInterface.OnClickListener() {

					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						String packageName = (String)activityList.get(position).get("pkgname");
						String processName = (String)activityList.get(position).get("name");
						//Toast.makeText(getApplicationContext(), processName + "to be removed", Toast.LENGTH_SHORT).show();
						activityManager.killBackgroundProcesses(packageName);
						Log.i("OnClick", processName + " ~~ " + packageName);
						ListView listView = (ListView) findViewById(R.id.list1);
						SimpleAdapter processAdapter = getProcessAdapter();
						listView.setAdapter(getProcessAdapter());
					}
				}).setNegativeButton("NO", new DialogInterface.OnClickListener() {
					
					public void onClick(DialogInterface dialog, int which) {
						// TODO Auto-generated method stub
						dialog.cancel();
					}
				}).create().show();
			}
		});
		return listView;
	}

	public View getTaskInfoView(){
		ListView listView = (ListView) findViewById(R.id.list2);

		List<String> listStrings = new ArrayList<String>();

		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		List<ActivityManager.RunningTaskInfo> out = activityManager.getRunningTasks(10);
		String res = null;
		for (ActivityManager.RunningTaskInfo it:out){
			res = it.toString();
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

	public List<Map<String,Object>> getProcessAdapterList(){
		Map<String,Object> tmap; 
		List<Map<String,Object>> activityList = new ArrayList<Map<String,Object>>();
		Packages pk = new Packages(this.getApplicationContext());
		ArrayList<Process> pArrayList = pk.getProcesList();
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			tmap.put("name",it.getProcessName() + "  ");
			tmap.put("pkgname",it.getPackageName());
			tmap.put("pid"," PID: " + it.getPid());
			tmap.put("memsize","   MEM: " + it.getMemSize() + " KB");
			activityList.add(tmap);
		}
		return activityList;
	}
	public SimpleAdapter getProcessAdapter()
	{
		activityList = getProcessAdapterList();
		SimpleAdapter processAdapter =
				new SimpleAdapter(this,activityList,R.layout.listitem,
						new String[]{"icon","title","name","pkgname","pid","memsize"},
						new int[]{R.id.icon,R.id.title,R.id.name,R.id.packagename,R.id.pid,R.id.memsize});

		processAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if ((view instanceof ImageView) && (data instanceof Drawable)) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable)data);
					return true;
				}
				return false;
			}
		});
		return processAdapter;
	}
}

