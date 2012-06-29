package com.android;

import java.io.File;
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

	private static final String pString = "进程";
	private static final String tString = "任务";
	private static final String sString = "服务";

	private final String[] menuTitle = {"结束选中的进程","全选","取消所有选中的条目","取消"};
	private final int[] menuID = {Menu.FIRST + 1, Menu.FIRST + 2,Menu.FIRST + 3, Menu.FIRST + 4};
	private TabHost tabHost;
	private TabSpec processTab, taskTab, serviceTab;
	private SimpleAdapter processAdapter;
	private List<String> toBeKilledProcess = null;
	private View processView, taskView,  serviceView; 
	private ActivityManager activityManager;
	private List<Map<String,Object>> processList;
	private List<Map<String,Object>> serviceList;
	Packages pk;
	@Override
	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.tabs);

		tabHost = getTabHost();
		tabHost.setOnTabChangedListener(this);
		activityManager=(ActivityManager)getSystemService(Context.ACTIVITY_SERVICE); 
		toBeKilledProcess = new ArrayList<String>();
		processTab = tabHost.newTabSpec(pString);
		pk = new Packages(this);
		tabHost.addTab(processTab
				.setIndicator(pString,getApplicationContext().getResources().getDrawable(R.drawable.process))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						processView = getProcessInfoView();
						return processView;
					}
				})
				);
		tabHost.addTab(tabHost.newTabSpec(tString)
				.setIndicator(tString,getApplicationContext().getResources().getDrawable(R.drawable.task))
				.setContent(new TabHost.TabContentFactory() {
					public View createTabContent(String tag) {
						taskView = getTaskInfoView();
						return taskView;
					}
				})
				);

		tabHost.addTab(tabHost.newTabSpec(sString)
				.setIndicator(sString,getApplicationContext().getResources().getDrawable(R.drawable.service))
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
		killSelectedProcess();
		processAdapter = getProcessAdapter();
		((ListView)processView).setAdapter(processAdapter);
		return true;
		case Menu.FIRST + 2:
			setCheckBoxes(true);
		return true;
		case Menu.FIRST + 3:
			setCheckBoxes(false);
		return true;
		case Menu.FIRST + 4:
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
					//Log.i("Kill ", " " +  packageName);
					++cnt;
				}
				//TextView tv  = (TextView) it.findViewWithTag(R.id.packagename);
				//Log.i("in list View", " " + c.isChecked());
			}
		}
		Log.i("to Kill size ", toBeKilledProcess.size() + " ");
	}

	void setCheckBoxes(boolean flag){
		
		View it;
		for (int i = ((ListView)processView).getFirstVisiblePosition(); i < ((ListView)processView).getCount(); ++i){
			it  = (View)((ListView)processView).getChildAt(i);
			if (it != null)
			{
				CheckBox c = (CheckBox)it.findViewById(R.id.pchose);
				c.setChecked(flag);
			}
		}

		//Toast.makeText(this, "cache size: " + getTotalCache() + "Byte",Toast.LENGTH_SHORT).show();

	}

//	long getTotalCache(){
//		long res = 0;
//		String mdir = "/data/data";
//		res = getFileSize(new File(mdir));
//		return res;
//	}
//	long getFileSize(File file){
//		Log.i("debug", file.getPath());
//		long res = 0;
//		if (file == null);
//		else if (file.isFile()){
//			Log.i("getFileSize", file.getPath());
//			res += file.length();
//		}
//		else if (file.isDirectory()){
//		    Log.i("getFiles Size", file.getPath());
//		    File[] sfiles = file.listFiles();
//		    
//			if (sfiles != null && sfiles.length > 0){
//				
//				Log.i("in dir", sfiles.length + "");
//				//res += getFileSize(ifile);
//			}
//		}
//		return res;
//	}
	
	public void onTabChanged(String tabName) {
		if(tabName.equals(pString)) {
			//do something
		}
		else if(tabName.equals(tString)) {
			//do something
		} else if (tabName.equals(sString)){
		}
	}


	private View getProcessInfoView() {

		ListView listView = (ListView) findViewById(R.id.list1);
		processAdapter = getProcessAdapter();
		listView.setAdapter(processAdapter);
		listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
		return listView;
	}


	public List<Map<String,Object>> getProcessAdapterList(){
		Map<String,Object> tmap; 
		List<Map<String,Object>> processList = new ArrayList<Map<String,Object>>();
		ArrayList<Process> pArrayList = pk.getProcesList();
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			tmap.put("cpu", "CPU: " + it.getcpuPercent());
			tmap.put("pkgname",it.getPackageName());
			tmap.put("uid", "UID: " + it.getUser());
			tmap.put("pid","    PID: " + it.getPid());
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


		//List<String> listStrings = new ArrayList<String>();
		List<Map<String,Object>> taskList = new ArrayList<Map<String, Object> >();
		Map<String,Object> tmap;
		List<ActivityManager.RunningTaskInfo> out = activityManager.getRunningTasks(10);
		//StringBuffer res = new StringBuffer();
		for (ActivityManager.RunningTaskInfo it:out){

			tmap = new HashMap<String, Object>();
			tmap.put("t_name", "Name:");
			tmap.put("t_namev", it.getClass().getSimpleName());
			tmap.put("t_num_running", "Num of runnings:");
			tmap.put("t_num_runningv",it.numRunning);
			tmap.put("t_num_activity","Num of activities:");
			tmap.put("t_num_activityv",it.numActivities);
			tmap.put("t_top_activity", "Top Activity:");
			if (it.topActivity != null)tmap.put("t_top_activityv", it.topActivity.getPackageName());
			else tmap.put("t_top_activityv","");

			tmap.put("t_bas_activity","Bas Activity:");
			if (it.baseActivity != null)tmap.put("t_bas_activityv",it.baseActivity.getPackageName());
			else tmap.put("t_bas_activityv", "Bas Activity:   ");

			tmap.put("t_description","Description:");
			if (it.description != null )tmap.put("t_descriptionv",it.description);
			else tmap.put("t_descriptionv","None");
			taskList.add(tmap);
		}

		SimpleAdapter taskAdapter =
				new SimpleAdapter(this,taskList,R.layout.taskitem,
						new String[]{"t_name","t_namev","t_num_activity","t_num_activityv","t_num_running","t_num_runningv","t_bas_activity","t_bas_activityv",
						"t_top_activity","t_top_activityv","t_description","t_descriptionv"},
						new int[]{R.id.taskname,R.id.tasknamev,R.id.t_num_activity,R.id.t_num_activityv,R.id.t_num_running,R.id.t_num_runningv,R.id.t_bas_activity,R.id.t_bas_activityv,
						R.id.t_top_activity, R.id.t_top_activityv,R.id.t_description,R.id.t_descriptionv});
		listView.setAdapter(taskAdapter);

		return listView;
	}

	private View getServiceInfoView() {

		ListView listView = (ListView) findViewById(R.id.list3);
		SimpleAdapter serviceAdapter = getServiceAdapter();
		listView.setAdapter(serviceAdapter);
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
		ArrayList<Process> pArrayList = pk.getServiceList();
		Log.i("serviceList Size",pArrayList.size() + " ");
		for (Process it: pArrayList){
			tmap = new HashMap<String, Object>();
			tmap.put("icon",it.getIcon());
			tmap.put("title",it.getTitle());
			//tmap.put("name",it.getProcessName() + "  ");
			tmap.put("pkgname",it.getPackageName());
			tmap.put("pid","   PID: " + it.getPid());
			tmap.put("uid", "UID: " + it.getUser());
			tmap.put("memsize","MEM: " + it.getMemSize() + " KB");
			serviceList.add(tmap);
		}
		return serviceList;
	}
}

