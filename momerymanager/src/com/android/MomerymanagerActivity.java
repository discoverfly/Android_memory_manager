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
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
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

	private final String[] menuTitle = {"Kill selected processes","Select all","Not select any","Exit"};
	private final int[] menuID = {Menu.FIRST + 1, Menu.FIRST + 2,Menu.FIRST + 3, Menu.FIRST + 4};
	private TabHost tabHost;
	private TabSpec processTab, taskTab, serviceTab;
	private SimpleAdapter processAdapter;
	private List<String> toBeKilledProcess = null; 
	private View processView, taskView,  serviceView; 
	private ActivityManager activityManager;
	private List<Map<String,Object>> processList;
	private List<Map<String,Object>> serviceList;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		toBeKilledProcess = new ArrayList<String>();
		processTab = tabHost.newTabSpec(LIST1_TAB_TAG);
		tabHost.addTab(processTab
				.setIndicator("Process",getApplicationContext().getResources().getDrawable(R.drawable.process))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						processView = getProcessInfoView();
						return processView;
					}
				})
				);
		tabHost.addTab(tabHost.newTabSpec(LIST2_TAB_TAG)
				.setIndicator("Task",getApplicationContext().getResources().getDrawable(R.drawable.ic_launcher))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						taskView = getTaskInfoView();
						return taskView;
					}
				})
				);

		tabHost.addTab(tabHost.newTabSpec(LIST3_TAB_TAG)
				.setIndicator(LIST3_TAB_TAG,getApplicationContext().getResources().getDrawable(R.drawable.task))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						serviceView = getServiceInfoView();
						return serviceView;
					}
				})
				);
	}

	public boolean onCreateOptionsMenu(Menu menu){
		super.onCreateOptionsMenu(menu);
		for (int i = 0; i < menuID.length; ++i){
			menu.add(Menu.NONE,menuID[i],Menu.NONE, menuTitle[i]);
		}
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item){
		return (applyMenuChoice(item)||super.onOptionsItemSelected(item));
	}

	public boolean applyMenuChoice(MenuItem item){
		switch(item.getItemId()){
		case Menu.FIRST + 1:
			Log.i("menu", "to KILL");
		//Toast.makeText(getApplicationContext(), "to KIll", Toast.LENGTH_SHORT).show();
		killSelectedProcess();
		processAdapter = getProcessAdapter();
		((ListView)processView).setAdapter(processAdapter);
		return true;
		case Menu.FIRST + 2:
			return true;
		}
		return false;
	}

	public void killSelectedProcess(){

		int cnt = 0;
		String packageName;
		View it;
		int j = 0;
		for (int i = ((ListView)processView).getFirstVisiblePosition(); i < ((ListView)processView).getCount(); ++i,++j){
			
			it  = (View)((ListView)processView).getChildAt(i);
			if (it != null)
			{
				CheckBox c = (CheckBox)it.findViewById(R.id.pchose);
				if (c.isChecked()){
					packageName  = (String) processList.get(j).get("pkgname");
					activityManager.killBackgroundProcesses(packageName);
					Log.i("Kill ", " " +  packageName);
				}
				//TextView tv  = (TextView) it.findViewWithTag(R.id.packagename);
				Log.i("in list View", " " + c.isChecked());
			}
		}
		Log.i("to Kill size ", toBeKilledProcess.size() + " ");
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


	private View getProcessInfoView() {

		ListView listView = (ListView) findViewById(R.id.list1);
		processAdapter = getProcessAdapter();
		listView.setAdapter(processAdapter);



		//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		//
		//			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
		//				if (toBeKilledProcess == null) toBeKilledProcess = new ArrayList<String>();
		//				
		//				String packageName = (String)processList.get(position).get("pkgname");
		//				String processName = (String)processList.get(position).get("name");
		//				Log.i("OnClick", processName + " ~~ " + packageName);
		//				toBeKilledProcess.add(packageName);
		//			}
		//				new AlertDialog.Builder(parent.getContext())
		//				.setMessage("Kill the process?")
		//				.setPositiveButton("YES", new DialogInterface.OnClickListener() {
		//
		//					public void onClick(DialogInterface dialog, int which) {
		//						// TODO Auto-generated method stub
		//						if (toBeKilledProcess == null) toBeKilledProcess = new ArrayList<String>();
		//						String packageName = (String)processList.get(position).get("pkgname");
		//						String processName = (String)processList.get(position).get("name");
		//						toBeKilledProcess.add(packageName);
		//						//Toast.makeText(getApplicationContext(), processName + "to be removed", Toast.LENGTH_SHORT).show();
		//						//activityManager.killBackgroundProcesses(packageName);
		//						Log.i("OnClick", processName + " ~~ " + packageName);
		//						//ListView listView = (ListView) findViewById(R.id.list1);
		//						//SimpleAdapter processAdapter = getProcessAdapter();
		//						//listView.setAdapter(getProcessAdapter());
		//					}
		//				}).setNegativeButton("NO", new DialogInterface.OnClickListener() {
		//
		//					public void onClick(DialogInterface dialog, int which) {
		//						// TODO Auto-generated method stub
		//						dialog.cancel();
		//					}
		//				}).create().show();
		//			}
		//});
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		return listView;
	}


	public List<Map<String,Object>> getProcessAdapterList(){
		Map<String,Object> tmap; 
		List<Map<String,Object>> processList = new ArrayList<Map<String,Object>>();
		Packages pk = new Packages(this.getApplicationContext());
		ArrayList<Process> pArrayList = pk.getProcesList();
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			tmap.put("cpu", "  CPU: " + it.getcpuPercent());
			tmap.put("pkgname",it.getPackageName());
			tmap.put("uid", "UID: " + it.getUid());
			tmap.put("pid","PID: " + it.getPid());
			tmap.put("check", false);
			tmap.put("memsize","   MEM: " + it.getMemSize() + " KB ");
			processList.add(tmap);
		}
		return processList;
	}
	public SimpleAdapter getProcessAdapter()
	{
		processList = getProcessAdapterList();
		SimpleAdapter processAdapter =
				new SimpleAdapter(this,processList,R.layout.listitem,
						new String[]{"icon","title","pkgname","uid","cpu","pid","memsize","check"},
						new int[]{R.id.icon,R.id.title,R.id.packagename,R.id.uid,R.id.cpu, R.id.pid,R.id.memsize, R.id.pchose});

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

	public View getTaskInfoView(){
		ListView listView = (ListView) findViewById(R.id.list2);


		List<String> listStrings = new ArrayList<String>();

		List<ActivityManager.RunningTaskInfo> out = activityManager.getRunningTasks(10);
		StringBuffer res = new StringBuffer();
		for (ActivityManager.RunningTaskInfo it:out){
			res = new StringBuffer();
			res.append("~name: " +it.toString());
			res.append("\n~id : " + it.id);
			res.append("\n~numActivities: " + it.numActivities);
			res.append("\n~description: " + it.description);
			if (it.description != null)Log.i("Task",it.description.toString());
			listStrings.add(res.toString());
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

	private View getServiceInfoView() {

		ListView listView = (ListView) findViewById(R.id.list3);
		SimpleAdapter serviceAdapter = getServiceAdapter();
		listView.setAdapter(serviceAdapter);
		//		List<String> listStrings = new ArrayList<String>();
		//		
		//		List<ActivityManager.RunningServiceInfo> out = activityManager.getRunningServices(10);
		//		String res = null;
		//		for (ActivityManager.RunningServiceInfo it:out){
		//			res = it.toString();
		//			if (it.process != null) Log.i("Service", it.process);
		//			listStrings.add(res);
		//		}
		//
		//		listView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, listStrings));
		//
		//		listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
		//			public void onItemClick(AdapterView<?> parent, View view, int position, long id) { 
		//				String item = (String) parent.getAdapter().getItem(position);
		//				if(item != null){
		//					((ArrayAdapter)parent.getAdapter()).remove(item);
		//					Toast.makeText(getApplicationContext(), item + " has been removed", Toast.LENGTH_SHORT).show();
		//				}
		//			}
		//		});
		return listView;
	}

	public SimpleAdapter getServiceAdapter(){
		serviceList = getServiceAdapterList();
		Log.i("serviceList Size",serviceList.size() + " ");
		SimpleAdapter serviceAdapter =
				new SimpleAdapter(this,serviceList,R.layout.serviceitem,
						new String[]{"icon","title","pkgname","uid","pid","memsize"},
						new int[]{R.id.serviceicon,R.id.servicetitle,
						R.id.servicepackagename,R.id.serviceuid, R.id.servicepid,R.id.servicememsize});

		serviceAdapter.setViewBinder(new ViewBinder() {
			public boolean setViewValue(View view, Object data, String textRepresentation) {
				if ((view instanceof ImageView) && (data instanceof Drawable)) {
					ImageView iv = (ImageView) view;
					iv.setImageDrawable((Drawable)data);
					return true;
				}
				return false;
			}
		});
		return serviceAdapter;
	}

	private List<Map<String, Object>> getServiceAdapterList() {
		// TODO Auto-generated method stub
		Map<String,Object> tmap; 
		List<Map<String,Object>> serviceList = new ArrayList<Map<String,Object>>();
		Packages pk = new Packages(this.getApplicationContext());
		ArrayList<Process> pArrayList = pk.getServiceList();
		Log.i("serviceList Size",pArrayList.size() + " ");
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			//tmap.put("name",it.getProcessName() + "  ");
			tmap.put("pkgname",it.getPackageName());
			tmap.put("pid"," PID: " + it.getPid());
			tmap.put("uid", "UID: " + it.getUid());
			tmap.put("memsize","   MEM: " + it.getMemSize() + " KB");
			serviceList.add(tmap);
		}
		return serviceList;
	}
}

