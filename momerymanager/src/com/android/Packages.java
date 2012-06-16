package com.android;

import java.util.*;
import android.content.*;
import android.content.pm.*;
public class Packages {
	private List<ApplicationInfo> allAppInfoList;

	Packages(Context context){
		PackageManager pm = context.getApplicationContext().getPackageManager();  
		allAppInfoList = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES); 
		//pm.getInstalledPackages(0);
	}
	
	public ApplicationInfo appInfo(String pName){
		for (ApplicationInfo it: allAppInfoList){
			if(it.processName.equals(pName)){
				return it;
			}
		}
		return null;
	}
}
