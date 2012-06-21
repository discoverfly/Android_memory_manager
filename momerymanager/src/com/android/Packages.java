package com.android;

import java.util.*;

import android.app.ActivityManager;
import android.content.*;
import android.content.pm.*;
import android.os.Debug;
import android.util.Log;
public class Packages {
	private List<ApplicationInfo> allAppInfoList = null;
	PackageManager pm = null;
	ActivityManager activityManager = null;
	List<ActivityManager.RunningAppProcessInfo> processInfoList = null;
	List<ActivityManager.RunningServiceInfo> serviceInfoList = null;
	Packages(Context context){
		pm = context.getApplicationContext().getPackageManager();
		activityManager=(ActivityManager)context.getSystemService(Context.ACTIVITY_SERVICE); 
		allAppInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
		processInfoList= activityManager.getRunningAppProcesses();
		serviceInfoList = activityManager.getRunningServices(100);
		//pm.getInstalledPackages(0);
	}

	public ApplicationInfo getAppInfo(String pName){
		for (ApplicationInfo it: allAppInfoList){
			if(it.processName.equals(pName)){
				return it;
			}
		}
		return null;
	}

	public ArrayList<Process> getProcesList(){
		ArrayList<Process> res = new ArrayList<Process>();
		Process temp = null;
		ApplicationInfo appInfo = null;
		for (ActivityManager.RunningAppProcessInfo it:processInfoList){
			temp = new Process();
			appInfo = getAppInfo(it.processName);

			temp.setPid(it.pid);
			temp.setUid(it.uid);
			temp.setIcon(appInfo.loadIcon(pm));
			temp.setProcessName(it.processName);
			temp.setPackageName(appInfo.packageName);
			temp.setTitle(appInfo.loadLabel(pm).toString());
			temp.setMemSize(getMemSize(new int[]{it.pid}));
			
			res.add(temp);
		}
		Collections.sort(res);
		return res;
	}

	public ArrayList<Process> getServiceList(){
		ArrayList<Process> res = new ArrayList<Process>();
		Process temp = null;
		ApplicationInfo appInfo = null;
		for (ActivityManager.RunningServiceInfo it:serviceInfoList){
			temp = new Process();
			Log.i("service",it.process);
			appInfo = getAppInfo(it.process);

			temp.setPid(it.pid);
			temp.setUid(it.uid);
			temp.setIcon(appInfo.loadIcon(pm));
			temp.setProcessName(it.process);
			temp.setPackageName(appInfo.packageName);
			temp.setTitle(appInfo.loadLabel(pm).toString());
			temp.setMemSize(getMemSize(new int[]{it.pid}));
			
			res.add(temp);
		}
		Collections.sort(res);
		return res;
	}

	public int getMemSize(int [] pids){
		int res = 0;
		Debug.MemoryInfo[] memoryInfo = activityManager.getProcessMemoryInfo(pids); 
		for (Debug.MemoryInfo it:memoryInfo){
			res += it.dalvikPrivateDirty;  // 获取进程占内存用信息 kb单位 
		}
		return res;
	}

}
